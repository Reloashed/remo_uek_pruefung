package remo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import remo.backend.entity.Account;
import remo.backend.entity.Group;
import remo.backend.repository.AccountRepository;
import remo.backend.repository.GroupRepository;

@RestController
@RequestMapping("/groups")
@CrossOrigin("*")
public class GroupManageController {

    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;
    @Autowired
    public GroupManageController(GroupRepository groupRepository, AccountRepository accountRepository) {
        this.groupRepository = groupRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping("{groupId}/invite/users/{userId}")
    @PreAuthorize("@groupSecurityService.isGroupOwner(#groupId, authentication.name)")
    public ResponseEntity<Void> inviteMember(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow();

        Account user = accountRepository.findById(userId)
                .orElseThrow();

        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            groupRepository.save(group);
        }

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("{groupId}/remove/users/{userId}")
    @PreAuthorize("@groupSecurityService.isGroupOwner(#groupId, authentication.name)")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow();

        group.getMembers()
                .removeIf(member ->
                        member.getId().equals(userId));

        groupRepository.save(group);

        return ResponseEntity.ok().build();
    }
}
