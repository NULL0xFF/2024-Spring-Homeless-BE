package kr.or.argos.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
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

@JsonIncludeProperties({"name", "description"})
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "groups")
public class Group extends BaseEntity implements GrantedAuthority {

  @Column(unique = true)
  @NotNull
  private String name;
  @NotNull
  private String description;
  @JsonBackReference
  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<UserGroup> userGroups = new HashSet<>();

  @Override
  public String getAuthority() {
    return name;
  }

  @Override
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    // @formatter:off
    builder
        .append("id", getId())
        .append("name", name)
        .append("description", description)
        .append("createdAt", getCreatedAt())
        .append("updatedAt", getUpdatedAt());
    // @formatter:on
    return builder.toString();
  }
}