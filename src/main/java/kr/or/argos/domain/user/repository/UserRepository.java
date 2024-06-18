package kr.or.argos.domain.user.repository;

import java.util.Optional;
import java.util.UUID;
import kr.or.argos.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  void deleteByUsername(String username);
}