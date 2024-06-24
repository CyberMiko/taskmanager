package com.example.taskmanagernew.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum Role {
    USER(Arrays.asList("user:read")),
    ADMIN(Arrays.asList("admin:read", "admin:create", "admin:update", "admin:delete"));

    @Getter
    private final List<String> permissions;
}
