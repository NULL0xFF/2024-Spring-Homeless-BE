package kr.or.argos.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
@Table(name = "groups")
public class Group extends BaseEntity {

  @Getter
  private static final Group defaultGroup = Group
      .builder()
      .name("DEFAULT")
      .description("기본 그룹")
      .roles(List.of())
      .build();

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull
  private String description;

  @NotNull
  @OneToMany(mappedBy = "group")
  private List<GroupRole> roles;

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    // @formatter:off
    builder
        .append("id", getId())
        .append("name", name)
        .append("description", description);
    // @formatter:on
    roles.forEach(role -> builder.append("role", role.getName()));
    // @formatter:off
    builder.append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt());
    // @formatter:on
    return builder.toString();
  }
}
