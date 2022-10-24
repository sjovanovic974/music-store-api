package com.example.demo.security.registration.model;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {

    USER(Sets.newHashSet(UserPermission.PRODUCT_READ)),
    ADMIN(Sets.newHashSet(
            UserPermission.PRODUCT_READ,
            UserPermission.PRODUCT_WRITE,
            UserPermission.ARTIST_READ,
            UserPermission.ARTIST_WRITE,
            UserPermission.CATEGORY_READ,
            UserPermission.CATEGORY_WRITE
    ));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permissions;
    }
}
