package remo.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import remo.backend.dto.MediaDto;
import remo.backend.entity.Account;
import remo.backend.entity.Media;
import remo.backend.exceptions.MediaOrAccountNotFoundException;
import remo.backend.repository.AccountRepository;
import remo.backend.repository.MediaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private AccountRepository accountRepository;


    public Optional<Media> getMediaById(Long mediaId) {
        return mediaRepository.findById(mediaId);
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
        throw new MediaOrAccountNotFoundException("Media not found");
    }
}
