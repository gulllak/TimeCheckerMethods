package ru.evgenii.timecheckermethods.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ClassStatistics {
    private String packageName;
    private String className;
    List<MethodStatistics> methodStatistics;
}
