package kr.or.argos.domain.user.repository;

import kr.or.argos.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
