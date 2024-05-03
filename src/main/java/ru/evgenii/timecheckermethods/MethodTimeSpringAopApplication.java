package ru.evgenii.timecheckermethods;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import ru.evgenii.timecheckermethods.service.TestMethods;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class MethodTimeSpringAopApplication {
    private final TestMethods testMethods;

    public static void main(String[] args) {
        SpringApplication.run(MethodTimeSpringAopApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("Запуск выполнения методов @TrackTime.");
        for (int i = 1; i <= 6; i++) {
            testMethods.testTrackTimeAnnotation(i);
        }

        log.info("Запуск выполнения void методов @TrackAsyncTime.");
        for (int i = 1; i <= 6; i++) {
            testMethods.testTrackTimeAsyncAnnotationVoid(i);
        }

        log.info("Запуск выполнения returnType методов @TrackAsyncTime.");
        for (int i = 1; i <= 6; i++) {
            testMethods.testTrackTimeAsyncAnnotationReturn(i);
        }
    }
}
