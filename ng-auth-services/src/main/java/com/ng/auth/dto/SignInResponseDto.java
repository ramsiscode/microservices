package com.ng.auth.dto;

public class SignInResponseDto {

    private String message;

    private String data;

    private Integer statusCode;


    public SignInResponseDto() {
		super();
	}

	public SignInResponseDto(String message, String data, Integer statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public SignInResponseDto(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public SignInResponseDto(String message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
