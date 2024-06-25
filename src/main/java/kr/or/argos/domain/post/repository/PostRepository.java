package kr.or.argos.domain.post.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.or.argos.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

  Optional<Post> findByIdentifier(String identifier);

  List<Post> findAllByAnnouncementIsTrue();

  boolean existsByIdentifier(String identifier);
}