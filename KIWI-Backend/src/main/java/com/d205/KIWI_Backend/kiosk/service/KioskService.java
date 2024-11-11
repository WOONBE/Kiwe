package com.d205.KIWI_Backend.kiosk.service;

import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.kiosk.dto.KioskRequest;
import com.d205.KIWI_Backend.kiosk.dto.KioskResponse;
import com.d205.KIWI_Backend.kiosk.repository.KioskRepository;
import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.member.domain.Member; // Member import 추가
import com.d205.KIWI_Backend.member.repository.MemberRepository;
import com.d205.KIWI_Backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final KioskRepository kioskRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Transactional
    public KioskResponse createKiosk(KioskRequest request) {
        // OwnerId로 Member 객체 가져오기
        Member owner = memberRepository.findById(request.getOwnerId())
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        Kiosk kiosk = Kiosk.builder()
            .location(request.getLocation())
            .status(request.getStatus())
            .member(owner) // Member 객체를 사용
            .build();

        Kiosk savedKiosk = kioskRepository.save(kiosk);
        return KioskResponse.fromKiosk(savedKiosk);
    }
    @Transactional(readOnly = true)
    public KioskResponse getKioskById(Integer kioskId) {
        Kiosk kiosk = kioskRepository.findById(kioskId)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_KIOSK_ID));
        return KioskResponse.fromKiosk(kiosk);
    }



    @Transactional
    public KioskResponse updateKiosk(Integer id, KioskRequest request) {
        Kiosk existingKiosk = kioskRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_KIOSK_ID));

        existingKiosk.updateLocation(request.getLocation()); // location 업데이트
        existingKiosk.updateStatus(request.getStatus()); // status 업데이트

        Kiosk updatedKiosk = kioskRepository.save(existingKiosk);
        return KioskResponse.fromKiosk(updatedKiosk);
    }

    @Transactional
    public void deleteKiosk(Integer id) {
        Kiosk existingKiosk = kioskRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_KIOSK_ID));
        kioskRepository.delete(existingKiosk);
    }

    @Transactional(readOnly = true)
    public List<KioskResponse> getAllKiosks() {
        return kioskRepository.findAll().stream()
            .map(KioskResponse::fromKiosk)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<KioskResponse> getMyKiosks() {
        Integer currentMemberId = memberService.getCurrentMemberId();

        // 현재 사용자의 ID로 Member 조회
        Member owner = memberRepository.findById(currentMemberId)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER_ID));

        // owner와 연관된 모든 키오스크 조회
        return kioskRepository.findByMember(owner).stream()
            .map(KioskResponse::fromKiosk)
            .toList();
    }

    @Transactional
    public KioskResponse updateMyKiosk(Integer kioskId, KioskRequest request) {
        Integer currentMemberId = memberService.getCurrentMemberId();  // 현재 사용자 ID 조회
        Kiosk kiosk = kioskRepository.findById(kioskId)
            .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_KIOSK_ID));

        if (!kiosk.getMember().getId().equals(currentMemberId)) {
            throw new BadRequestException(ExceptionCode.NOT_VALID_UPDATE_KIOSK); // 권한이 없는 경우 예외 발생
        }

        kiosk.updateLocation(request.getLocation());
        kiosk.updateStatus(request.getStatus());

        Kiosk updatedKiosk = kioskRepository.save(kiosk);
        return KioskResponse.fromKiosk(updatedKiosk);
    }

}
