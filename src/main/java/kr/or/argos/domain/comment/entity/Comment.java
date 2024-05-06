package kr.or.argos.domain.comment.entity;

import jakarta.persistence.*;
import kr.or.argos.domain.common.BaseEntity;
import kr.or.argos.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_secret", nullable = false)
    private boolean isSecret;

    // Not implement now.
    // @Column(name = "password", length = 80)
    // private String password;

    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    private String content;

    @Column(name = "status", nullable = false)
    private Integer status;  // Need to be replaced to ENUM.

    public User getUser() {
        return this.user;
    }

    public boolean getIsSecret() {
        return this.isSecret;
    }

    public String getContent() {
        return this.content;
    }

    public Integer status() {
        return this.status;
    }

    @Builder
    // @AllArgsConstructor annotation is dangerous.
    private Comment(
        final boolean isSecret, 
        final String content, 
        final Integer status
    ) {
        this.isSecret = isSecret;
        this.content = content;
        this.status = status;
    }
}
