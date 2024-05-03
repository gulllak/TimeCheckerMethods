package ru.evgenii.timecheckermethods.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgenii.timecheckermethods.dto.ClassStatistics;
import ru.evgenii.timecheckermethods.dto.MethodStatistics;
import ru.evgenii.timecheckermethods.exception.EntityNotFoundException;
import ru.evgenii.timecheckermethods.model.ClassEntity;
import ru.evgenii.timecheckermethods.repository.ClassEntityRepository;
import ru.evgenii.timecheckermethods.repository.MethodExecutionRepository;
import ru.evgenii.timecheckermethods.service.StatisticsService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

    private final MethodExecutionRepository methodRepository;

    private final ClassEntityRepository classRepository;
    @Override
    public MethodStatistics getMethodByName(String className, String methodName) {
        ClassEntity classEntity = classRepository.findByClassName(className)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Класс с именем className=%s не найден", className)));

        double averageExecutionTimes = methodRepository.findAverageExecutionTimeMethodNameAndClassEntity(classEntity, methodName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Метод с именем methodName=%s не найден", methodName)));

        return MethodStatistics.builder()
                .methodName(methodName)
                .executionTimeAvg(averageExecutionTimes)
                .build();
    }

    @Override
    public ClassStatistics getMethodsByClassName(String className) {
        ClassEntity classEntity = classRepository.findByClassName(className)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Класс с именем className=%s не найден", className)));

        List<MethodStatistics> methodExecutions = methodRepository.findAllAverageExecutionTimeMethodByClassEntity(classEntity).stream()
                .sorted(Comparator.comparingDouble(MethodStatistics::getExecutionTimeAvg))
                .collect(Collectors.toList());

        return ClassStatistics.builder()
                .packageName(classEntity.getPackageName())
                .className(classEntity.getClassName())
                .methodStatistics(methodExecutions)
                .build();
    }

    @Override
    public List<ClassStatistics> getMethodsByPackageName(String packageName) {
        List<ClassEntity> classEntities = classRepository.findByPackageNameLikeIgnoreCase(packageName + "%");

        if (classEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return classEntities.stream()
                .map(classEntity -> {
                    List<MethodStatistics> methodExecutions = methodRepository
                            .findAllAverageExecutionTimeMethodByClassEntity(classEntity)
                            .stream()
                            .sorted(Comparator.comparingDouble(MethodStatistics::getExecutionTimeAvg))
                            .collect(Collectors.toList());

                    return Optional.of(methodExecutions)
                            .filter(not(List::isEmpty))
                            .map(executions -> ClassStatistics.builder()
                                    .packageName(classEntity.getPackageName())
                                    .className(classEntity.getClassName())
                                    .methodStatistics(executions)
                                    .build())
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
