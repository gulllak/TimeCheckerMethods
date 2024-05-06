package ru.evgenii.timecheckermethods.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.evgenii.timecheckermethods.model.ClassEntity;
import ru.evgenii.timecheckermethods.model.MethodExecution;
import ru.evgenii.timecheckermethods.service.TrackTimeService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class TrackAsyncTimeAspect {
    private final TrackTimeService trackTimeService;

    @Pointcut("@annotation(ru.evgenii.timecheckermethods.annotation.TrackAsyncTime) " +
            "&& execution(!void *(..))")
    public void asyncMethodsReturning() {}

    @Pointcut("@annotation(ru.evgenii.timecheckermethods.annotation.TrackAsyncTime) " +
            "&& execution(void *(..))")
    public void asyncMethodsReturningVoid() {}

    @Around("asyncMethodsReturning()")
    public Object trackAsyncTimeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("Асинхронное выполнение trackAsyncTimeAdvice");
                long startTime = System.currentTimeMillis();
                Object result = joinPoint.proceed();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                saveTimeMeasurement(joinPoint, duration);
                log.info("Время выполнения метода {} составило {}", joinPoint.getSignature().getName(), duration);
                return result;
            } catch (Throwable throwable) {
                log.error("Исключение в методе {}: {}", joinPoint.getSignature().getName(), throwable.getMessage());
                throw new CompletionException(throwable);
            }
        });
    }

    @Around("asyncMethodsReturningVoid()")
    public Object trackAsyncTimeVoidAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("Асинхронное выполнение trackAsyncTimeVoidAdvice");
                long startTime = System.currentTimeMillis();
                joinPoint.proceed();
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                saveTimeMeasurement(joinPoint, duration);
                log.info("Время выполнения метода {} составило {}", joinPoint.getSignature().getName(), duration);
            } catch (Throwable throwable) {
                log.error("Исключение в методе {}: {}", joinPoint.getSignature().getName(), throwable.getMessage());
                throw new CompletionException(throwable);
            }
        });
    }

    private void saveTimeMeasurement(ProceedingJoinPoint joinPoint, long duration) {
        ClassEntity classEntity = ClassEntity.builder()
                .packageName(joinPoint.getSignature().getDeclaringType().getPackageName())
                .className(joinPoint.getSignature().getDeclaringType().getSimpleName())
                .build();

        MethodExecution methodExecution = MethodExecution.builder()
                .classEntity(classEntity)
                .methodName(joinPoint.getSignature().getName())
                .executionTime(duration)
                .build();

        trackTimeService.saveTimeMethod(methodExecution);
    }
}
