package ru.evgenii.timecheckermethods.service;

import ru.evgenii.timecheckermethods.dto.ClassStatistics;
import ru.evgenii.timecheckermethods.dto.MethodStatistics;

import java.util.List;

public interface StatisticsService {
    MethodStatistics getMethodByName(String className, String methodName);

    ClassStatistics getMethodsByClassName(String className);

    List<ClassStatistics> getMethodsByPackageName(String packageName);
}
