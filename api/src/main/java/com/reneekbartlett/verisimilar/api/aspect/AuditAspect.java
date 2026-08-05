package com.reneekbartlett.verisimilar.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.reneekbartlett.verisimilar.api.shared.annotation.Audit;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    @Around("@annotation(auditAnnotation)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Audit auditAnnotation) throws Throwable {
        String action = auditAnnotation.action();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String arguments = Arrays.toString(joinPoint.getArgs());
        String currentUser = getCurrentUser(); // Fetch your context/session user here

        log.info("[AUDIT-START] User: '{}' is executing action: '{}' via method: '{}' with arguments: {}", 
                 currentUser, action, methodName, arguments);

        Object result;
        try {
            // Proceed with the actual business logic method
            result = joinPoint.proceed();

            // Log Success
            log.info("[AUDIT-SUCCESS] User: '{}' successfully completed action: '{}'", currentUser, action);

            return result;

        } catch (Throwable throwable) {
            // Log Failure with the exact exception message
            log.error("[AUDIT-FAILURE] User: '{}' failed action: '{}'. Error: {}", 
                      currentUser, action, throwable.getMessage());
            throw throwable; // Re-throw so the application handles the error normally
        }
    }

    // Mocking user retrieval. In production, replace this with:
    // SecurityContextHolder.getContext().getAuthentication().getName();
    private String getCurrentUser() {
        return "admin_user_42"; 
    }
}
