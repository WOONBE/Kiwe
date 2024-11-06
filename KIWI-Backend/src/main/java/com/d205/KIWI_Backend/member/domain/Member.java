package com.d205.KIWI_Backend.member.domain;


import com.d205.KIWI_Backend.global.common.BaseEntity;
import com.d205.KIWI_Backend.kiosk.domain.Kiosk;
import com.d205.KIWI_Backend.member.dto.MemberRequest;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Integer id;

    @Column(name = "MEMBER_NAME", nullable = false)
    private String name;

    @Column(name = "MEMBER_EMAIL", unique = true, nullable = false)
    private String email;

    @Column(name = "MEMBER_PASSWORD", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Kiosk> kiosks = new ArrayList<>(); //키오스크 목록

    //비즈니스 로직
    public void addKiosk(Kiosk kiosk) {
        kiosks.add(kiosk);
        kiosk.updateMember(this); // 키오스크와 멤버 관계 설정
    }

    public void removeKiosk(Kiosk kiosk) {
        kiosks.remove(kiosk);
        kiosk.updateMember(null); // 키오스크와 멤버 관계 해제
    }

    public void updateName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void updateEmail(String email) {
        if (email != null) {
            this.email = email;
        }
    }

    public void updatePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }

    public static Member toMember(MemberRequest memberRequest, PasswordEncoder encoder) {
        return Member.builder()
            .name(memberRequest.getName())
            .email(memberRequest.getEmail())
            .password(encoder.encode(memberRequest.getPassword()))
            .type(MemberType.USER)  // 기본 값으로 USER 설정
            .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + type.name()));
    }


}
