package ru.evgenii.timecheckermethods.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MethodStatistics {
    private String methodName;
    private double executionTimeAvg;
}
