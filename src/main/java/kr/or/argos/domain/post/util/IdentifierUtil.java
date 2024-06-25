package kr.or.argos.domain.post.util;

import java.security.SecureRandom;

public class IdentifierUtil {

  private static final SecureRandom RANDOM = new SecureRandom();

  public static String generateIdentifier(int length) {
    StringBuilder identifier = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      identifier.append(getChar(RANDOM.nextInt(62)));
    }
    return identifier.toString();
  }

  private static char getChar(int index) {
    if (index < 10) {
      return (char) ('0' + index);
    } else if (index < 36) {
      return (char) ('A' + index - 10);
    } else if (index < 62) {
      return (char) ('a' + index - 36);
    } else {
      throw new IllegalArgumentException("Invalid index: " + index);
    }
  }
}