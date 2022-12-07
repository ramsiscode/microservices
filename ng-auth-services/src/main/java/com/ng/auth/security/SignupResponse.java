package com.ng.auth.security;

import java.util.ArrayList;
import java.util.List;

public class SignupResponse {

    public enum Status {
        OK, USERNAME_TAKEN, WEAK_PASSWORD
    }

    private final Status status;

    private final String username;

    private final String secret;

    private List<String> backUpCode = new ArrayList<>();

    public SignupResponse(Status status) {
        this(status, null, null, null);
    }


    @Override
    public String toString() {
        return "SignupResponse{" +
                "status=" + status +
                ", username='" + username + '\'' +
                ", secret='" + secret + '\'' +
                ", backUpCode=" + backUpCode +
                '}';
    }

    public SignupResponse(Status status, String username, String secret, List<String> backUpCode) {
        this.status = status;
        this.username = username;
        this.secret = secret;
        this.backUpCode = backUpCode;
    }

    public SignupResponse(Status status, String username, String secret) {
        this.status = status;
        this.username = username;
        this.secret = secret;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getSecret() {
        return this.secret;
    }

    public String getUsername() {
        return this.username;
    }

    public List<String> getBackUpCode() {
        return backUpCode;
    }


}
