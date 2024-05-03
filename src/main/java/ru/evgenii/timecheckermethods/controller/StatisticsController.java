package ru.evgenii.timecheckermethods.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.evgenii.timecheckermethods.dto.ApiError;
import ru.evgenii.timecheckermethods.dto.ClassStatistics;
import ru.evgenii.timecheckermethods.dto.MethodStatistics;
import ru.evgenii.timecheckermethods.service.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Validated
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/average/method")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить среднее время выполнения определенного метода",
            description = "Возвращает статистические данные о среднем времени выполнения указанного метода.",
    responses = {
            @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = MethodStatistics.class))),
            @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public MethodStatistics getAverageMethodExecutionTimes(@RequestParam @NotBlank(message = "Имя класса не может быть пустым") @Parameter(description = "Имя класса содержащего метод") String className,
                                                           @RequestParam @NotBlank(message = "Имя метода не может быть пустым") @Parameter(description = "Имя метода") String methodName) {
        return statisticsService.getMethodByName(className, methodName);
    }

    @GetMapping("/average/class")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить среднее время выполнения методов определенного класса",
            description = "Возвращает статистические данные о среднем времени выполнения методов в классе.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное выполнение", content = @Content(schema = @Schema(implementation = ClassStatistics.class))),
                    @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content(schema = @Schema(implementation = ApiError.class)))
            })
    public ClassStatistics getAverageMethodsExecutionTimesByClass(@RequestParam @NotBlank(message = "Имя класса не может быть пустым") @Parameter(description = "Имя класса содержащего методы") String className) {
        return statisticsService.getMethodsByClassName(className);
    }

    @GetMapping("/average/package")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить среднее время выполнения методов в классах определенного пакета",
            description = "Возвращает список статистических данных о среднем времени выполнения методов в классах пакета.",
    responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ClassStatistics.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public List<ClassStatistics> getAverageMethodsExecutionTimesByPackageName(
            @RequestParam @NotBlank(message = "Имя пакета не может быть пустым") @Parameter(description = "Имя пакета содержащего классы") String packageName) {
        return statisticsService.getMethodsByPackageName(packageName);
    }
}
