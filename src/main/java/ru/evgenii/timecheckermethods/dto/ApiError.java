package ru.evgenii.timecheckermethods.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ApiError {
    @Schema(description = "HTTP статус ошибки", example = "Код ошибки")
    private HttpStatus status;
    @Schema(description = "Сообщение об ошибке", example = "Класс с именем className=Example не найден")
    private String message;
    @Schema(description = "Время возникновения ошибки", example = "2024-05-01 12:00:00")
    private String timestamp;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
