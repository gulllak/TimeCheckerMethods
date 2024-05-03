package ru.evgenii.timecheckermethods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.evgenii.timecheckermethods.dto.MethodStatistics;
import ru.evgenii.timecheckermethods.model.ClassEntity;
import ru.evgenii.timecheckermethods.model.MethodExecution;

import java.util.List;
import java.util.Optional;

@Repository
public interface MethodExecutionRepository extends JpaRepository<MethodExecution, Long> {
    @Query("SELECT AVG(me.executionTime) " +
            "FROM MethodExecution me " +
            "WHERE me.classEntity = :classEntity AND me.methodName = :methodName")
    Optional<Double> findAverageExecutionTimeMethodNameAndClassEntity(ClassEntity classEntity, String methodName);
    @Query("SELECT new ru.evgenii.timecheckermethods.dto.MethodStatistics(me.methodName, AVG(me.executionTime)) " +
            "FROM MethodExecution me " +
            "WHERE me.classEntity = :classEntity " +
            "GROUP BY me.methodName")
    List<MethodStatistics> findAllAverageExecutionTimeMethodByClassEntity(ClassEntity classEntity);
}
