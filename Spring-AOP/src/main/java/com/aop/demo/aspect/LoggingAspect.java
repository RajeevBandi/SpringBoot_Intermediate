package com.aop.demo.aspect;

import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Pointcut for all methods in the CalculatorService
    @Pointcut("execution(* com.aop.demo.service.CalculatorService.*(..))")
    public void calculatorServiceMethods() {}

    // Before Advice
    @Before("calculatorServiceMethods()")
    public void logBefore() {
        System.out.println("Before method execution...");
    }

    // After Advice
    @After("calculatorServiceMethods()")
    public void logAfter() {
        System.out.println("After method execution...");
    }

    // After Returning Advice
    @AfterReturning(pointcut = "calculatorServiceMethods()", returning = "result")
    public void logAfterReturning(Object result) {
        System.out.println("Method returned with value: " + result);
    }

    // After Throwing Advice
    @AfterThrowing(pointcut = "calculatorServiceMethods()", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        System.out.println("An exception occurred: " + ex.getMessage());
    }

    // Around Advice
    @Around("calculatorServiceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Around: Before method execution...");
        Object result = joinPoint.proceed(); // Proceed with the method call
        System.out.println("Around: After method execution...");
        return result;
    }
}
