package org.example.jtsb02.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.jtsb02.member.dto.MemberDto;
import org.example.jtsb02.member.model.MemberRole;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberId;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public static Member of(String memberId, String nickname, String password, String email) {
        return Member.builder()
            .memberId(memberId)
            .nickname(nickname)
            .password(password)
            .email(email)
            .role(MemberRole.USER)
            .build();
    }

    public static Member fromMemberDto(MemberDto memberDto) {
        return Member.builder()
            .id(memberDto.getId())
            .memberId(memberDto.getMemberId())
            .nickname(memberDto.getNickname())
            .email(memberDto.getEmail())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 동일 객체
        if (o == null || getClass() != o.getClass()) return false; // 타입 비교
        Member member = (Member) o;
        return Objects.equals(id, member.id); // id를 기준으로 비교
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // id를 기준으로 해시코드 생성
    }
}