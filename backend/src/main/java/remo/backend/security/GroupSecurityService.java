package remo.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remo.backend.entity.Group;
import remo.backend.entity.Media;
import remo.backend.entity.MediaStatus;
import remo.backend.repository.GroupRepository;
import remo.backend.repository.MediaRepository;

@Service
@RequiredArgsConstructor
public class GroupSecurityService {

    private final GroupRepository groupRepository;
    private final MediaRepository mediaRepository;

    public boolean canViewMedia(Long mediaId, String username) {
        Media media = mediaRepository.findById(mediaId)
                .orElse(null);
        if (media == null)
            return false;
        if (media.getMediaStatus() == MediaStatus.DRAFT && media.getOwner().getUsername().equals(username)) {
            return true;
        } else if (media.getMediaStatus() == MediaStatus.DRAFT) {
            return false;
        }
        Group group = media.getGroup();
        if (group == null)
            return false;
        return isMember(group.getId(), username);
    }

    public boolean isMember(Long groupId, String username) {

        Group group = groupRepository.findById(groupId)
                .orElse(null);

        if (group == null)
            return false;

        return group.getMembers().stream()
                .anyMatch(member ->
                        member.getUsername().equals(username));
    }

    public boolean isGroupOwner(Long groupId, String username) {

        Group group = groupRepository.findById(groupId)
                .orElse(null);

        return group != null
                && group.getOwner().getUsername().equals(username);
    }

    public boolean isMediaOwner(Long mediaId, String username) {
        Media media = mediaRepository.findById(mediaId)
                .orElse(null);
        if (media == null)
            return false;
        return media.getOwner().getUsername().equals(username);
    }
}
