package com.ng.auth.security;

import java.util.Objects;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.ng.auth.db.tables.records.AppUserRecord;
import com.ng.auth.model.User;

public class AppUserDetail {

  private final Long appUserId;

  private final String username;

  private final boolean enabled;

  private final String secret;

  private final Set<GrantedAuthority> authorities;

  public AppUserDetail(AppUserRecord user) {
    this.appUserId = user.getId();
    this.username = user.getUsername();
    this.authorities = Set.of();
    this.secret = user.getSecret();
    this.enabled = Objects.requireNonNullElse(user.getEnabled(), false);
  }

  public AppUserDetail(User user) {
    this.appUserId = user.getId();
    this.username = user.getUsername();
    this.authorities = Set.of();
    this.secret = user.getSecret();
    this.enabled = Objects.requireNonNullElse(user.getEnabled(), false);
  }

  public Long getAppUserId() {
    return this.appUserId;
  }

  public String getUsername() {
    return this.username;
  }

  public Set<GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public String getSecret() {
    return this.secret;
  }

}
