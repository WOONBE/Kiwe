package com.d205.KIWI_Backend.member.service;

import com.d205.KIWI_Backend.global.config.TokenProvider;
import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.kiosk.repository.KioskRepository;
import com.d205.KIWI_Backend.member.domain.Member;
import com.d205.KIWI_Backend.member.domain.MemberRefreshToken;
import com.d205.KIWI_Backend.member.dto.MemberRequest;
import com.d205.KIWI_Backend.member.dto.MemberResponse;
import com.d205.KIWI_Backend.member.dto.SignInResponse;
import com.d205.KIWI_Backend.member.repository.MemberRefreshTokenRepository;
import com.d205.KIWI_Backend.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final KioskRepository kioskRepository;

    @Transactional
    public MemberResponse registerMember(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.getEmail()).isPresent()) {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID);
        }
        Member member = Member.toMember(memberRequest, passwordEncoder);

        Member savedMember = memberRepository.save(member);
        return MemberResponse.fromMember(savedMember);
    }

    @Transactional
    public SignInResponse signIn(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadRequestException(ExceptionCode.INVALID_PASSWORD);
        }

        // Member 객체를 영속화하여 식별자를 보장
        member = memberRepository.save(member);

        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", member.getId(), member.getType()));
        String refreshToken = tokenProvider.createRefreshToken();

        // MemberRefreshToken이 존재하지 않으면 새로 생성합니다.
        Member finalMember = member;
        memberRefreshTokenRepository.findByMember(member)
            .ifPresentOrElse(
                it -> it.updateRefreshToken(refreshToken),
                () -> memberRefreshTokenRepository.save(new MemberRefreshToken(finalMember, refreshToken))
            );

        return new SignInResponse(member.getEmail(), member.getType(), accessToken, refreshToken);
    }

    @Transactional
    public SignInResponse oauthSignIn(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // Member 객체를 영속화하여 식별자를 보장
            member = memberRepository.save(member);

            String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", member.getId(), member.getType()));
            String refreshToken = tokenProvider.createRefreshToken();

            // MemberRefreshToken이 존재하지 않으면 새로 생성합니다.
            Member finalMember = member;
            memberRefreshTokenRepository.findByMember(member)
                .ifPresentOrElse(
                    it -> it.updateRefreshToken(refreshToken),
                    () -> memberRefreshTokenRepository.save(new MemberRefreshToken(finalMember, refreshToken))
                );

            return new SignInResponse(member.getEmail(), member.getType(), accessToken, refreshToken);
        } else {
            String randomPassword = UUID.randomUUID().toString();
            MemberRequest memberRequest = new MemberRequest();
            memberRequest.setEmail(email);
            memberRequest.setPassword(randomPassword);

            MemberResponse newMember = registerMember(memberRequest); // 프로필 이미지 없이 회원가입
            String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", newMember.getId(), newMember.getType()));
            String refreshToken = tokenProvider.createRefreshToken();

            // 새로 생성된 Member에 대해 MemberRefreshToken을 저장합니다.
            memberRefreshTokenRepository.save(new MemberRefreshToken(memberRepository.findById(newMember.getId()).orElseThrow(), refreshToken));

            return new SignInResponse(newMember.getEmail(), newMember.getType(), accessToken, refreshToken);
        }
    }

    @Transactional
    public MemberResponse updateMemberInfo(Integer memberId, MemberRequest memberRequest)
        throws BadRequestException {
        // 기존 멤버 조회
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        // 멤버 정보 업데이트
        if (memberRequest != null) { // memberRequest가 null인지 체크
            if (memberRequest.getName() != null) {
                existingMember.updateName(memberRequest.getName());
            }

            if (memberRequest.getEmail() != null) {
                existingMember.updateEmail(memberRequest.getEmail());
            }

            if (memberRequest.getPassword() != null) {
                existingMember.updatePassword(passwordEncoder.encode(memberRequest.getPassword()));
            }

            // 키오스크 목록 업데이트
            if (memberRequest.getKioskIds() != null) {
                // 현재 멤버의 키오스크를 모두 제거
                List<Kiosk> kiosksToRemove = new ArrayList<>(existingMember.getKiosks());
                for (Kiosk kiosk : kiosksToRemove) {
                    existingMember.removeKiosk(kiosk);
                }

                // 요청된 키오스크 ID 목록을 기반으로 키오스크 추가
                for (Integer kioskId : memberRequest.getKioskIds()) {
                    if (kioskId != null) { // 키오스크 ID가 null인지 체크
                        Kiosk kiosk = kioskRepository.findById(kioskId)
                            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_KIOSK_ID));
                        existingMember.addKiosk(kiosk);
                    }
                }
            }
        } else {
            throw new BadRequestException(ExceptionCode.INVALID_UPDATE);
        }

        // 수정된 member를 saveAndFlush() 후 바로 반환
        memberRepository.saveAndFlush(existingMember);
        return MemberResponse.fromMember(existingMember);  // 변경된 정보를 정확히 반영
    }


//    @Transactional
//    public MemberResponse updateMemberInfo(Integer memberId, MemberRequest memberRequest)
//        throws BadRequestException {
//        // 기존 멤버 조회
//        Member existingMember = memberRepository.findById(memberId)
//            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));
//
//        // 멤버 정보 업데이트
//        if (memberRequest != null) { // memberRequest가 null인지 체크
//            if (memberRequest.getName() != null) {
//                existingMember.updateName(memberRequest.getName());
//            }
//
//            if (memberRequest.getEmail() != null) {
//                existingMember.updateEmail(memberRequest.getEmail());
//            }
//
//            if (memberRequest.getPassword() != null) {
//                existingMember.updatePassword(passwordEncoder.encode(memberRequest.getPassword()));
//            }
//
//            // 키오스크 목록 업데이트
//            if (memberRequest.getKioskIds() != null) {
//                // 현재 멤버의 키오스크를 모두 제거(초기화 안해서 터진듯)
//                List<Kiosk> kiosksToRemove = new ArrayList<>(existingMember.getKiosks());
//                for (Kiosk kiosk : kiosksToRemove) {
//                    existingMember.removeKiosk(kiosk);
//                }
//
//                // 요청된 키오스크 ID 목록을 기반으로 키오스크 추가
//                for (Integer kioskId : memberRequest.getKioskIds()) {
//                    if (kioskId != null) { // 키오스크 ID가 null인지 체크
//                        Kiosk kiosk = kioskRepository.findById(kioskId)
//                            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_KIOSK_ID));
//                        existingMember.addKiosk(kiosk);
//                    }
//                }
//            }
//        } else {
//            throw new BadRequestException(ExceptionCode.INVALID_UPDATE);
//        }
//
//        Member updatedMember = memberRepository.saveAllAndFlush(existingMember);
//        return MemberResponse.fromMember(updatedMember);
//    }


    @Transactional
    public List<MemberResponse> getAllUsers() {
        return memberRepository.findAll().stream()
            .map(MemberResponse::fromMember)
            .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponse findMemberById(Integer memberId) {
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        return MemberResponse.fromMember(existingMember);
    }

    @Transactional
    public MemberResponse findMemberByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_EMAIL);
        }

        Member existingMember = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        return MemberResponse.fromMember(existingMember);
    }

    public Integer getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Integer.parseInt(userDetails.getUsername());
    }
}
