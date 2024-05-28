package kr.or.argos.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import kr.or.argos.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "permissions")
public class Permission extends BaseEntity {

  @Getter
  // @formatter:off
  private static final Permission defaultPermission = Permission
      .builder()
      .name("DEFAULT")
      .description("기본 권한")
      .build();
  // @formatter:on

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull
  private String description;

  @Override
  public String toString() {
    // @formatter:off
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("name", name)
        .append("description", description)
        .append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt())
        .toString();
    // @formatter:on
  }
}
