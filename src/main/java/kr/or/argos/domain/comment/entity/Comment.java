package kr.or.argos.domain.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.or.argos.domain.common.BaseEntity;
import kr.or.argos.domain.post.entity.Post;
import kr.or.argos.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    private String content;

    private long parentId;

//    @Column(name = "is_secret", nullable = false)
//    private boolean isSecret;

//     Not implement now.
//     @Column(name = "password", length = 80)
//     private String password;

    // @AllArgsConstructor annotation is dangerous.
    @Builder
    private Comment(
            final User user,
            final Post post,
            final String content,
            final long parentId
    ) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.parentId = parentId;
    }
}
