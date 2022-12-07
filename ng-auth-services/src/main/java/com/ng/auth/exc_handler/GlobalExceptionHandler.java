package com.ng.auth.exc_handler;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ng.auth.dto.ErrorResponseDto;

@ControllerAdvice // stereotype annotation . Class level annotation .
//Supplies common advice to all controllers  n rest controllers.
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//	@ExceptionHandler(UserHandlingException.class)
//	public ResponseEntity<?> handleUserHandlingException(UserHandlingException exc, WebRequest request) {
//		System.out.println("handling  res not found exc ");
//	
//		ErrorResponse errResp = new ErrorResponse(exc.getMessage(), request.getDescription(false));
//		return new ResponseEntity<>(errResp, HttpStatus.NOT_FOUND);
//	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
		System.out.println("handling constraint violation exc");
		ErrorResponseDto errResp = new ErrorResponseDto(e.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errResp, HttpStatus.BAD_REQUEST);
	}

	// This Exception is thrown when a method argument fails validation typically
	// due to @Valid style validation
	// or request body content is required.
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("handling method arg not valid");
		
		
		List<String> validationErrs=new ArrayList<>();
		for(FieldError err : ex.getBindingResult().getFieldErrors())
			validationErrs.add(err.getDefaultMessage());
		
		ErrorResponseDto errResp=new ErrorResponseDto("Validation Failed", validationErrs.toString());
		return new ResponseEntity<Object>(errResp, status);//HTTP 400

	}
	//handle REST call related exc
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<?> handleRestClntException(RestClientException exc, WebRequest request) {
		System.out.println("in handle rest clnt exception "+exc);
		System.out.println(exc.getMostSpecificCause());
		ErrorResponseDto errResp = new ErrorResponseDto("Rest API Call Failed ", request.getDescription(false));
		return new ResponseEntity<>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// handle ANY other exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleAnyException(Exception exc, WebRequest request) {
		System.out.println("in handle exception "+exc);
		
		ErrorResponseDto errResp = new ErrorResponseDto(exc.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDataIntegrityViolationException(Exception exc, WebRequest request) {
		System.out.println("in handle exception "+exc);
		
		ErrorResponseDto errResp = new ErrorResponseDto("Data already exist!!", request.getDescription(false));
		return new ResponseEntity<>(errResp, HttpStatus.CONFLICT);
	}
}
