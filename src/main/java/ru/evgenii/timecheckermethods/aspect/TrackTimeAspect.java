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

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class TrackTimeAspect {
    private final TrackTimeService trackTimeService;

    @Pointcut("@annotation(ru.evgenii.timecheckermethods.annotation.TrackTime)")
    public void methodsMarkAsTrackTime(){}

    @Around("methodsMarkAsTrackTime()")
    public Object trackTimeAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Синхронное выполнение trackTimeAdvice");
        long startTime = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Исключение в методе {}: {}", joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        saveTimeMeasurement(joinPoint, duration);
        log.info("Время выполнения метода {} составило {}", joinPoint.getSignature().getName(), duration);

        return result;
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
