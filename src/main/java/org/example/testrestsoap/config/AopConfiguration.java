package org.example.testrestsoap.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class AopConfiguration {

    private final Logger logger = LoggerFactory.getLogger(AopConfiguration.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* org.example.testrestsoap.controller.*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().getName();
        String clazz = joinPoint.getTarget().getClass().getSimpleName();
        String args = Arrays.toString(joinPoint.getArgs());

        String requestBodyJson = getRequestBodyAsJson(joinPoint);

        logger.info("--> [START] {}.{}() | Params: {}", clazz, method, args);
        logger.info("--> OBJECT: {}", requestBodyJson);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            String responseBodyJson = objectToResultString(result);
            // Если метод что-то возвращает, можно логировать и результат (опционально result вместо SUCCESS)
            logger.info("<-- [SUCCESS] {}.{}() | Time: {}ms", clazz, method, duration);
            logger.info("<-- OBJECT: {}", responseBodyJson);
            return result;
        } catch (Throwable e) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("X-- [ERROR] {}.{}() | Time: {}ms | Message: {}", clazz, method, duration, e.getMessage());
            logger.error("X-- EXCEPTION: {}", e.getClass().getName());
            throw e;
        }
    }

    private String getRequestBodyAsJson(ProceedingJoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof RequestBody) {
                        return objectMapper.writeValueAsString(args[i]);
                    }
                }
            }
            // Если @RequestBody нет, выводим просто все аргументы списком (для GET запросов)
            return args.length > 0 ? objectMapper.writeValueAsString(args) : "[no arguments]";
        } catch (Exception e) {
            return "[Error parsing request object]";
        }
    }

    private String objectToResultString(Object result) {
        if (result == null) {
            return "[void / null]";
        }
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            return result.toString(); // Если это обычная строка или простой тип
        }
    }

}
