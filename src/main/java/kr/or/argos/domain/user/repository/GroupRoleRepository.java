package kr.or.argos.domain.user.repository;

import java.util.UUID;
import kr.or.argos.domain.user.entity.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, UUID> {

}
