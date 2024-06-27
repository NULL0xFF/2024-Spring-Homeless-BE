package kr.or.argos.domain.user.dto;

import java.util.Date;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserRecord(String username, String email, Integer studentId, String surname,
                         String forename, Date birthday, Set<String> authorities, Boolean enabled) {

}