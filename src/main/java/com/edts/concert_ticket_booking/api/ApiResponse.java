package com.edts.concert_ticket_booking.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
    private String timestamp;
    private String requestId;

    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = java.time.LocalDateTime.now().toString();
        this.requestId = org.slf4j.MDC.get("requestId");
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, "SUCCESS", message, data);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
