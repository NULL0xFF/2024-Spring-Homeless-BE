package kr.or.argos.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberStatus {

    NOT_GRANTED(0),
    GENERAL(1),
    ADMIN(2),
    ;

    private int statusNumber;

    MemberStatus(final int statusNumber) {
        this.statusNumber = statusNumber;
    }
}
