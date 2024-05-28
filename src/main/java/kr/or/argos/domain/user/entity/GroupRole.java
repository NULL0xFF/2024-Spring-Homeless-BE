package kr.or.argos.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import kr.or.argos.domain.common.entity.BaseEntity;
import kr.or.argos.security.entity.Permission;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "group_roles")
public class GroupRole extends BaseEntity {

  @Getter
  // @formatter:off
  private static final GroupRole defaultGroupRole = GroupRole
      .builder()
      .name("DEFAULT")
      .description("기본 그룹 역할")
      .permission(Permission.getDefaultPermission())
      .group(Group.getDefaultGroup())
      .build();
  // @formatter:on

  @NotNull
  @Column(unique = true)
  private String name;

  @NotNull
  private String description;

  @NotNull
  @ManyToOne(optional = false)
  @JoinColumn(name = "group_id")
  private Group group;

  @NotNull
  @ManyToOne(optional = false)
  @JoinColumn(name = "permission_id")
  private Permission permission;

  @Override
  public String toString() {
    // @formatter:off
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("name", name)
        .append("description", description)
        .append("group", group.getName())
        .append("permission", permission.getName())
        .append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt())
        .toString();
    // @formatter:on
  }
}
