package kr.or.argos.security.repository;

import java.util.Optional;
import java.util.UUID;
import kr.or.argos.security.entity.SecuritySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecuritySettingsRepository extends JpaRepository<SecuritySettings, UUID> {

  Optional<SecuritySettings> findByName(String name);
}