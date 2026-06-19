package remo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import remo.backend.dto.MediaDto;
import remo.backend.entity.Media;
import remo.backend.service.MediaService;

@RestController
@RequestMapping("/media")
@CrossOrigin("*")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/{mediaId}")
    @PreAuthorize("@groupSecurityService.canViewMedia(#mediaId, authentication.name)")
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

    @PatchMapping("/{mediaId}/toggleLike")
    @PreAuthorize("@groupSecurityService.canViewMedia(#mediaId, authentication.name)")
    public ResponseEntity<Media> likeMedia(@PathVariable Long mediaId, Authentication authentication) {
        return ResponseEntity.ok(mediaService.likeMedia(mediaId, authentication));
    }
}
