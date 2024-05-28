package kr.or.argos.security.repository;

import java.util.UUID;
import kr.or.argos.security.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

}
