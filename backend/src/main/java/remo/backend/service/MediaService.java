package remo.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import remo.backend.dto.CreateMediaDto;
import remo.backend.dto.MediaDto;
import remo.backend.entity.Account;
import remo.backend.entity.Group;
import remo.backend.entity.Media;
import remo.backend.entity.MediaStatus;
import remo.backend.exceptions.*;
import remo.backend.repository.AccountRepository;
import remo.backend.repository.GroupRepository;
import remo.backend.repository.MediaRepository;
import remo.backend.security.GroupSecurityService;

import java.util.List;
import java.util.Optional;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupSecurityService groupSecurityService;


    public Optional<Media> getMediaById(Long mediaId) {
        return mediaRepository.findById(mediaId);
    }

    public Media createMedia(CreateMediaDto media, Authentication authentication){
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(authentication.getName());
        if (accountOptional.isEmpty()) {
            throw new AccountNotFoundException("Account not found for username: " + authentication.getName());
        }
        Optional<Group> groupOptional = groupRepository.findById(media.groupId());
        if (groupOptional.isEmpty()) {
            throw new GroupNotFoundException("Group not found for id: " + media.groupId());
        }
        if (groupSecurityService.isMember(media.groupId(), authentication.getName())) {
            Media toSave = new Media(
                    null,
                    media.media(),
                    MediaStatus.DRAFT,
                    groupOptional.get(),
                    accountOptional.get(),
                    List.of()
            );
            return mediaRepository.save(toSave);
        }
        throw new NotAllowedToModifyGroup("User is not allowed to modify this group");
    }

    public Media likeMedia(Long mediaId, Authentication authentication) {
        Optional<Media> mediaToLike = mediaRepository.findById(mediaId);
        Optional<Account> account = accountRepository.findAccountByUsername(authentication.getName());
        if (mediaToLike.isPresent() && account.isPresent()) {
            Media media = mediaToLike.get();
            Account acc = account.get();
            if (media.getLikedBy().contains(acc)) {
                media.getLikedBy().remove(acc);
            } else {
                media.getLikedBy().add(acc);
            }
            return mediaRepository.save(media);
        }
        throw new MediaOrAccountNotFoundException("Media or account not found");
    }

    public MediaDto getLikeCount(Long mediaId, Authentication authentication) {
        Optional<Media> mediaOptional = mediaRepository.findById(mediaId);
        if (mediaOptional.isPresent()) {
            Media media = mediaOptional.get();
            if (media.getGroup().getOwner().getUsername().equals(authentication.getName())) {
                return new MediaDto(media.getMedia(), media.getLikedBy().size(), media.getLikedBy());
            }
            return new MediaDto(media.getMedia(), media.getLikedBy().size(), List.of());
        }
        throw new MediaNotFoundException("Media not found");
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public Media postMedia(Long mediaId) {
        Optional<Media> mediaOptional = mediaRepository.findById(mediaId);
        if (mediaOptional.isPresent()) {
            Media media = mediaOptional.get();
            media.setMediaStatus(MediaStatus.PUBLIC);
            return mediaRepository.save(media);
        }
        throw new MediaNotFoundException("Media not found");
    }
}
