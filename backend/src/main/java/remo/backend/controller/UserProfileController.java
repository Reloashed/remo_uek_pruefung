package remo.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import remo.backend.entity.ProfileStatus;
import remo.backend.entity.UserProfile;
import remo.backend.service.UserProfileService;

import java.net.URI;

@RestController
@RequestMapping("/api/me/profile")
@CrossOrigin("*")
public class UserProfileController {
    private final UserProfileService userProfileService;
    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> getOwnProfile(Authentication authentication) {
        return ResponseEntity.ok(userProfileService.getOwnProfile(authentication.getName()));
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfile> updateOwnProfile(
            @RequestBody UserProfile request,
            Authentication authentication) {
        return ResponseEntity.created(URI.create("/api/me/profile")).body(userProfileService.updateOwnProfile(
                authentication.getName(), request));
    }

    @PutMapping("/{profileId}")
    @PreAuthorize("@userProfileSecurity.isOwner(authentication, #profileId)")
    public ResponseEntity<UserProfile> updateOwnProfile(
            @PathVariable Long profileId,
            @RequestBody UserProfile request) {
        return ResponseEntity.created(URI.create("/api/me/profile")).body(userProfileService.updateOwnProfile(
                profileId, request));
    }

    @PatchMapping("/{profileId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfile> verifyProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(userProfileService.setProfileStatus(
                profileId, ProfileStatus.VERIFIED));
    }

    @PatchMapping("/{profileId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfile> lockProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(userProfileService.setProfileStatus(
                profileId, ProfileStatus.LOCKED));
    }

    @PatchMapping("/{profileId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfile> unlockProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(userProfileService.setProfileStatus(
                profileId, ProfileStatus.UNVERIFIED));
    }
}

