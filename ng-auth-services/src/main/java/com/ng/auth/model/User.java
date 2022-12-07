package com.ng.auth.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "secret", length = 16)
    private String secret;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "additional_security", nullable = false)
    private Boolean additionalSecurity;

    @Column(name = "back_up_codes")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Code> codes = new ArrayList<>();

    @Transient
    private boolean totp;

    @Transient
    private String password;

    @Transient
    private String captcha;

    @Transient
    private String hiddenCaptcha;

    @Transient
    private String realCaptcha;

    public User() {
    }

    public User(String username, String passwordHash, String secret, Boolean enabled, Boolean additionalSecurity) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.secret = secret;
        this.enabled = enabled;
        this.additionalSecurity = additionalSecurity;
    }

    public User(String username, String passwordHash, String secret, Boolean enabled, Boolean additionalSecurity, List<Code> codes) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.secret = secret;
        this.enabled = enabled;
        this.additionalSecurity = additionalSecurity;
        this.codes = codes;
    }

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAdditionalSecurity() {
        return additionalSecurity;
    }

    public void setAdditionalSecurity(Boolean additionalSecurity) {
        this.additionalSecurity = additionalSecurity;
    }

    public boolean isTotp() {
        return totp;
    }

    public void setTotp(boolean totp) {
        this.totp = totp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getHiddenCaptcha() {
        return hiddenCaptcha;
    }

    public void setHiddenCaptcha(String hiddenCaptcha) {
        this.hiddenCaptcha = hiddenCaptcha;
    }

    public String getRealCaptcha() {
        return realCaptcha;
    }

    public void setRealCaptcha(String realCaptcha) {
        this.realCaptcha = realCaptcha;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", secret='" + secret + '\'' +
                ", enabled=" + enabled +
                ", additionalSecurity=" + additionalSecurity +
                ", totp=" + totp +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                ", hiddenCaptcha='" + hiddenCaptcha + '\'' +
                ", realCaptcha='" + realCaptcha + '\'' +
                '}';
    }
}
