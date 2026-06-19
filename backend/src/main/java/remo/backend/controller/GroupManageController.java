package remo.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import remo.backend.entity.Account;
import remo.backend.entity.Group;
import remo.backend.repository.AccountRepository;
import remo.backend.repository.GroupRepository;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GroupManageController {

    private final GroupRepository groupRepository;
    private final AccountRepository accountRepository;


    @PostMapping("{groupId}/invite/users/{userId}")
    @PreAuthorize("@groupSecurityService.isOwner(#groupId, authentication.name)")
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
    @PreAuthorize("@groupSecurityService.isOwner(#groupId, authentication.name)")
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
