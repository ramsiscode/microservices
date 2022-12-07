package com.ng.org.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAdvice {
	@Pointcut(value = "execution(* com.ng.admin.*.*.*(..) ) && !execution(* com.ng.admin.filter.*.*(..))")
	public void myPointcut() {

	}

	@Around("myPointcut()")
	public Object applicationLogger(ProceedingJoinPoint joinPoint) throws Throwable {
		ObjectMapper mapper = new ObjectMapper();
		String methodName = joinPoint.getSignature().getName();// get method name
		String className = joinPoint.getTarget().getClass().toString();// get class name
		Object[] args = joinPoint.getArgs();// get method i/p arguments
		log.info("method invoked " + methodName + "() className " + className + " arguments "
			/*	+ mapper.writeValueAsString(args)*/);
		Object object = joinPoint.proceed();
		log.info("method invoked " + methodName + "()  className " + className + " arguments "
		/* + mapper.writeValueAsString(object) */);
		return object;
	}
}
