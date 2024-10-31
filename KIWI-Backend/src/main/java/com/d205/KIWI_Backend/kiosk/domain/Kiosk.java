package com.d205.KIWI_Backend.kiosk.domain;


import com.d205.KIWI_Backend.global.common.BaseEntity;
import com.d205.KIWI_Backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "KIOSK") // 테이블 이름
public class Kiosk extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KIOSK_ID")
    private Integer id; // 키오스크 ID

    @Column(name = "KIOSK_LOCATION")
    private String location; // 키오스크 위치

    @Column(name = "KIOSK_STATUS")
    private String status; // 키오스크 상태

    @ManyToOne // 점주와의 관계 설정
    @JoinColumn(name = "OWNER_ID")
    private Member member;


    public void updateMember(Member member) {
        this.member = member; // 키오스크와 점주 관계 설정
    }

    public void updateLocation(String location) {
        this.location = location; // 키오스크 위치 설정
    }

    public void updateStatus(String status) {
        this.status = status; // 키오스크 상태 설정
    }






}
