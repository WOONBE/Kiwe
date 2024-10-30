package com.d205.KIWI_Backend.kiosk.service;

import com.d205.KIWI_Backend.global.exception.ExceptionCode;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.kiosk.dto.KioskRequest;
import com.d205.KIWI_Backend.kiosk.dto.KioskResponse;
import com.d205.KIWI_Backend.kiosk.repository.KioskRepository;
import com.d205.KIWI_Backend.global.exception.BadRequestException;
import com.d205.KIWI_Backend.member.domain.Member; // Member import 추가
import com.d205.KIWI_Backend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final KioskRepository kioskRepository;
    private final MemberRepository memberRepository;

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
}
