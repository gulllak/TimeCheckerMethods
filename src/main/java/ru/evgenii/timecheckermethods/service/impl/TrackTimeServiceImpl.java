package ru.evgenii.timecheckermethods.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.evgenii.timecheckermethods.model.ClassEntity;
import ru.evgenii.timecheckermethods.model.MethodExecution;
import ru.evgenii.timecheckermethods.repository.ClassEntityRepository;
import ru.evgenii.timecheckermethods.repository.MethodExecutionRepository;
import ru.evgenii.timecheckermethods.service.TrackTimeService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackTimeServiceImpl implements TrackTimeService {

    private final MethodExecutionRepository methodExecutionRepository;

    private final ClassEntityRepository classEntityRepository;

    @Override
    @Async
    public void saveTimeMethod(MethodExecution methodExecution) {
        ClassEntity classEntity = getClassEntity(methodExecution.getClassEntity());
        methodExecution.setClassEntity(classEntity);
        log.info("Сохранили время выполнения метода {}", classEntity.getClassName());
        methodExecutionRepository.save(methodExecution);
    }

    private ClassEntity getClassEntity(ClassEntity classEntity) {
        Optional<ClassEntity> existingClass = classEntityRepository.findByClassNameAndPackageName(classEntity.getClassName(), classEntity.getPackageName());

        return existingClass.orElseGet(() -> saveClassEntity(classEntity));
    }

    private ClassEntity saveClassEntity(ClassEntity classEntity) {
        log.info("Сохранили новый класс {}", classEntity.getClassName());
        return classEntityRepository.save(classEntity);
    }
}
