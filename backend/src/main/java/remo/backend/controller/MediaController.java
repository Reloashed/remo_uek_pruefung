package remo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import remo.backend.dto.CreateMediaDto;
import remo.backend.dto.MediaDto;
import remo.backend.entity.Media;
import remo.backend.service.MediaService;

import java.util.List;

@RestController
@RequestMapping("/media")
@CrossOrigin("*")
public class MediaController {

    private final MediaService mediaService;
    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<List<Media>> getAllMedia() {
        return ResponseEntity.ok(mediaService.getAllMedia());
    }

    @GetMapping("/{mediaId}")
    @PreAuthorize("@groupSecurityService.canViewMedia(#mediaId, authentication.name) || hasRole('COACH')")
    public ResponseEntity<Media> getMediaById(@PathVariable Long mediaId) {
        return mediaService.getMediaById(mediaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{mediaId}/likeCount")
    @PreAuthorize("@groupSecurityService.canViewMedia(#mediaId, authentication.name)")
    public ResponseEntity<MediaDto> getLikeCount(@PathVariable Long mediaId, Authentication authentication) {
        return ResponseEntity.ok(mediaService.getLikeCount(mediaId, authentication));
    }

    @PostMapping
    public ResponseEntity<Media> createMedia(@RequestBody CreateMediaDto media, Authentication authentication) {
        return ResponseEntity.ok(mediaService.createMedia(media, authentication));
    }

    @PatchMapping("/{mediaId}/toggleLike")
    @PreAuthorize("@groupSecurityService.canViewMedia(#mediaId, authentication.name)")
    public ResponseEntity<Media> likeMedia(@PathVariable Long mediaId, Authentication authentication) {
        return ResponseEntity.ok(mediaService.likeMedia(mediaId, authentication));
    }

    @PatchMapping("/{mediaId}/post")
    @PreAuthorize("@groupSecurityService.isMediaOwner(#mediaId, authentication.name)")
    public ResponseEntity<Media> postMedia(@PathVariable Long mediaId) {
        return ResponseEntity.ok(mediaService.postMedia(mediaId));
    }
}
