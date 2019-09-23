/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.agatsenko.todo.util.Throwables;

@Aspect
public class LogAspects {
    private static final String ARGS_DELIMITER = ", ";

    @Around("@annotation(LogInfo)")
    public Object logInfo(ProceedingJoinPoint point) throws Throwable {
        final var logger = getLogger(point);
        Object result;
        if (logger.isInfoEnabled()) {
            final var signatureLog = buildSignatureLog(point);
            logger.info("start - " + signatureLog);
            result = point.proceed();
            logger.info("finish - " + signatureLog + ": " + result);
        }
        else {
            result = point.proceed();
        }
        return result;
    }

    @Around("@annotation(LogError)")
    public Object logError(ProceedingJoinPoint point) throws Throwable {
        try {
            return point.proceed();
        }
        catch (Throwable t) {
            Throwables.whenNonFatal(t, () -> getLogger(point).error("fails - " + buildSignatureLog(point), t));
            throw t;
        }
    }

    private static Logger getLogger(JoinPoint point) {
        return LoggerFactory.getLogger(point.getSignature().getDeclaringType());
    }

    private static String buildSignatureLog(JoinPoint point) {
        final var builder = new StringBuilder();
        builder
                .append(point.getSignature().getName())
                .append('(');
        final var args = point.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                builder.append(args[i]);
                builder.append(ARGS_DELIMITER);
            }
            builder.delete(builder.length() - ARGS_DELIMITER.length(), builder.length());
        }
        builder.append(')');
        return builder.toString();
    }
}
