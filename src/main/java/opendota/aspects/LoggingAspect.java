package opendota.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* opendota.cache.EntityCache.get*(..))")
    public void logCacheGet(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Cache get method invoked: {}", methodName);
    }

    @Before("execution(* opendota.cache.EntityCache.put*(..))")
    public void logCachePut(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Cache put method invoked: {}", methodName);
    }

    @Before("execution(* opendota.cache.EntityCache.remove*(..))")
    public void logCacheRemove(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Cache remove method invoked: {}", methodName);
    }

    @Pointcut("execution(* opendota.service.*.*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logServiceMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Service method invoked - Class: {}, Method: {}, Args: {}", className, methodName, args);
    }
}