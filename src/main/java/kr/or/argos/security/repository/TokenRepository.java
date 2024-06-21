package kr.or.argos.security.repository;

import java.util.Optional;
import java.util.UUID;
import kr.or.argos.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, UUID> {

  Optional<Token> findByUsername(String username);
}
