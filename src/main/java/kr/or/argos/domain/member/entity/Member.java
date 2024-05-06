package kr.or.argos.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.or.argos.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 15, nullable = false)
    private String webId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String studentNumber;

    @Column(length = 15, nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Column(length = 2000)
    private String profileImageUrl;

    @Builder
    private Member(
            final String webId,
            final String password,
            final String studentNumber,
            final String name,
            final MemberStatus status,
            final String profileImageUrl
    ) {
        this.webId = webId;
        this.password = password;
        this.studentNumber = studentNumber;
        this.name = name;
        this.status = status;
        this.profileImageUrl = profileImageUrl;
    }
}
