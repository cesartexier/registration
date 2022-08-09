package com.registration.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Api Controller Aspect
 * Log entry parameters and response for Controller endpoints
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RestControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(RestControllerAspect.class);

    private final ObjectMapper mapper;

    /**
     * Controller pointcut
     * intercept RequestMapping annotated methods
     * log request and response parameters
     */
    @Pointcut("within(com.registration.controller..*) " +
        "&& @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void pointcut() {
        // Pointcut
    }


    /**
     * log request method and parameters
     * When entering endpoint log path(s), RequestMapping method and arguments of the request
     *
     * @param joinPoint the AspectJ JointPoint
     */
    @Before("pointcut()")
    public void logMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);
        Map<String, Object> parameters = getParameters(joinPoint);

        try {
            var parametersAsString = mapper.writeValueAsString(parameters);
            logger.info("==> path(s): {}, method(s): {}, arguments: {} ",
                mapping.path(), mapping.method(), parametersAsString);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    /**
     * log request method and response
     * When leaving endpoint log path(s), RequestMapping method and response
     *
     * @param joinPoint the AspectJ JointPoint
     */
    @AfterReturning(pointcut = "pointcut()", returning = "entity")
    public void logMethodAfter(JoinPoint joinPoint, ResponseEntity<?> entity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping mapping = signature.getMethod().getAnnotation(RequestMapping.class);

        try {
            var entityAsString = mapper.writeValueAsString(entity);
            logger.info("<== path(s): {}, method(s): {}, retuning: {}",
                mapping.path(), mapping.method(), entityAsString);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting", e);
        }
    }

    private Map<String, Object> getParameters(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        HashMap<String, Object> map = new HashMap<>();

        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            map.put(parameterNames[i], joinPoint.getArgs()[i]);
        }

        return map;
    }

}
