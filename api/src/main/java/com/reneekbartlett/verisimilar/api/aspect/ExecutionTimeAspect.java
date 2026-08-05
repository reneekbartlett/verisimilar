package com.reneekbartlett.verisimilar.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

    // Pointcut targeting all methods inside the 'service' package
    @Around("execution(* com.reneekbartlett.verisimilar.api.service.*.*(..))")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        // Execute the actual business logic method
        Object result = joinPoint.proceed(); 

        long executionTime = System.currentTimeMillis() - start;

        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");

        return result;
    }
}
