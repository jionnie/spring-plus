package org.example.expert.domain.log.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.expert.common.security.CustomUserDetails;
import org.example.expert.domain.log.service.LogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final LogService logService;

    @Pointcut("@annotation(org.example.expert.domain.log.annotation.ManagerSaveCheck)")
    public void managerSaveCheckPointCut() {

    }

    @Around("managerSaveCheckPointCut()")
    public Object adviceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long userId = null;
        String requestUrl = null;
        String httpMethod = null;
        String clientIp = null;
        Object result;
        boolean success = true;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userId = user.getId();
            requestUrl = request.getRequestURL().toString();
            httpMethod = request.getMethod();
            clientIp = request.getRemoteAddr();

            result = joinPoint.proceed();
            return result;
        } catch (Exception exception) {
            success = false;
            throw exception;
        } finally {
            logService.managerSaveLog(success, userId, requestUrl, httpMethod, clientIp);
        }
    }
}
