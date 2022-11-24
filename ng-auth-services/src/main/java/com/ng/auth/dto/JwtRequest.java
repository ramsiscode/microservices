package com.ng.auth.dto;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
private String username;
private String password;
}
