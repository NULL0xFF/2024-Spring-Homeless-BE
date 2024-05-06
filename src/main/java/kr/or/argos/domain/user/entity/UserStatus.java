package kr.or.argos.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserStatus {

    NOT_GRANTED(0),
    GENERAL(1),
    ADMIN(2),
    ;

    private int statusNumber;

    UserStatus(final int statusNumber) {
        this.statusNumber = statusNumber;
    }
}
