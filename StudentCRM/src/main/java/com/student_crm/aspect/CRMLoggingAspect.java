package com.student_crm.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {

	// create logger
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	// add pointcut declarations
	@Pointcut("execution(* com.student_crm.controller.*.*(..))")
	public void forControllerPackage() {}
	
	@Pointcut("execution(* com.student_crm.service.*.*(..))")
	public void forServicePackage() {}
	
	@Pointcut("execution(* com.student_crm.dao.*.*(..))")
	public void forDAOPackage() {}
	
	@Pointcut("forControllerPackage() || forServicePackage() || forDAOPackage()")
	public void forAppFlow() {}
	
	// add @Before Advice
	@Before("forAppFlow()")
	public void beforeAppFlowAdvice(JoinPoint theJoinpoint) {
		
		// display the method name
		String methodName = theJoinpoint.getSignature().toShortString();
		myLogger.info("\n ====>>>> Apply @Before Advice on method: "+methodName);
		// get the arguments
		Object[] args = theJoinpoint.getArgs();
		// looping through args
		for(Object tempArg:args) {
			myLogger.info("\n ====>>>> Argument: "+tempArg);
		}
		
	}
	
	// add @AfterReturing Advice
	@AfterReturning(
			pointcut = "forAppFlow()",
			returning = "theResult")
	public void afterReturningAppFlowAdvice(JoinPoint theJoinPoint,Object theResult) {
		
		// display the method name
		String methodName = theJoinPoint.getSignature().toShortString();
		myLogger.info("\n ====>>>> Apply @AfterReturning Advice on method: "+methodName);
		// print the result
		myLogger.info("\n ====>>>> Result: "+theResult);
		
	}
}
