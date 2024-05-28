package kr.or.argos.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import kr.or.argos.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

  @NotNull
  private String username;

  @NotNull
  private String password;

  @NotNull
  private Integer studentId;

  @NotNull
  private String name;

  @ManyToOne
  @Builder.Default
  @JoinColumn(name = "group_id")
  private Group group = Group.getDefaultGroup();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO: 계정 만료 여부 확인
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO: 계정 잠금 여부 확인
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO: 자격 증명 만료 여부 확인
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO: 계정 활성화 여부 확인
    return true;
  }

  @Override
  public String toString() {
    // @formatter:off
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("username", username)
        .append("studentId", studentId)
        .append("name", name)
        .append("group", group.getName())
        .append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt())
        .toString();
    // @formatter:on
  }
}
