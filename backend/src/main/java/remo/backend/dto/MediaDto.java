package remo.backend.dto;

import remo.backend.entity.Account;

import java.util.List;

public record MediaDto(String media, int likeCount, List<Account> likedBy) {
}
