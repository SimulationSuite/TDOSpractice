package org.tdos.tdospractice.type;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * rest api 全局返回格式
 * <p>
 * {
 * "code": 500,
 * "data": "data",
 * "message": "success"
 * }
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {
    @JsonProperty("code")
    private int code;

    @JsonProperty("data")
    private T data;

    @JsonProperty("message")
    private String message;

    public static <T> Response<T> success(T data) {
        return new Response<>(Code.SUCCESS.code, data, Code.SUCCESS.message);
    }

    public static <T> Response<T> success() {
        return new Response<>(Code.SUCCESS.code, null, Code.SUCCESS.message);
    }

    public static <T> Response<T> success(Code code, T data, String message) {
        return new Response<>(code.code, data, message + code.message);
    }

    public static <T> Response<T> error(Code code) {
        return new Response<>(code.code, null, code.message);
    }


    public static <T> Response<T> error(Code code, String reason) {
        return new Response<>(code.code, null, reason);
    }

    public static <T> Response<T> error() {
        return new Response<>(Code.INTERNAL_ERROR.code, null, Code.INTERNAL_ERROR.message);
    }

    public static <T> Response<T> error(String reason) {
        return new Response<>(Code.INTERNAL_ERROR.code, null, reason);
    }

    public static <T> Response<T> error(Code code, String reason, T data) {
        return new Response<>(code.code, data, reason);
    }

    public enum Code {
        SUCCESS(200, "success"),

        JWT_INVALID(400, "jwt is invalid"),

        INTERNAL_ERROR(500, "internal error");


        public final int code;

        public final String message;

        Code(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}

