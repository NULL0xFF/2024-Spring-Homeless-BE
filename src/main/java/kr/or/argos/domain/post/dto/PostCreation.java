package kr.or.argos.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostCreation {

  @NotNull
  @NotBlank
  private String title;

  @NotNull
  private boolean announcement;

  @NotNull
  private String content;
}