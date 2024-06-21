package kr.or.argos.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import kr.or.argos.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

  @Column(unique = true)
  @NotNull
  private String username;
  @JsonIgnore
  @NotNull
  private String password;
  @Column(unique = true)
  @NotNull
  private Integer studentId;
  @NotNull
  private String name;
  @JsonIgnore
  @Builder.Default
  private boolean enabled = true;
  @JsonBackReference
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<UserGroup> userGroups = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return userGroups.stream().map(UserGroup::getGroup).collect(Collectors.toSet());
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
    return enabled;
  }

  @Override
  public String toString() {
    // @formatter:off
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("username", username)
        .append("studentId", studentId)
        .append("name", name)
        .append("user_groups", userGroups)
        .append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt())
        .toString();
    // @formatter:on
  }
}