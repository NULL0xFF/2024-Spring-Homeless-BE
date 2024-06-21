package kr.or.argos.domain.user.service;

import jakarta.annotation.PostConstruct;
import kr.or.argos.domain.user.entity.Group;
import kr.or.argos.domain.user.repository.GroupRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

  private final GroupRepository groupRepository;
  @Getter
  private Group userGroup;
  @Getter
  private Group adminGroup;

  @PostConstruct
  private void initializeDefaultGroup() {
    userGroup = groupRepository.findByName("USERS").orElseGet(() -> {
      Group initUserGroup = Group.builder().name("USERS").description("사용자 그룹").build();
      return groupRepository.save(initUserGroup);
    });
    adminGroup = groupRepository.findByName("ADMINS").orElseGet(() -> {
      Group initAdminGroup = Group.builder().name("ADMINS").description("관리자 그룹").build();
      return groupRepository.save(initAdminGroup);
    });
  }
}