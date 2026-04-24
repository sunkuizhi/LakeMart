package org.lzx.lakemart.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }

    // 成功（无数据）
    public static Result success() {
        return new Result<>(0, "操作成功", null);
    }

    // 失败（默认提示）
    public static Result error() {
        return new Result<>(1, "操作失败", null);
    }

    // 成功（自定义消息 + 数据）
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(0, message, data);
    }

    // 成功（自定义消息）
    public static Result success(String message) {
        return new Result<>(0, message, null);
    }

    // 失败（自定义消息）
    public static Result error(String message) {
        return new Result<>(1, message, null);
    }
    // 专门用于返回数据（避免与 success(String message) 歧义）
    public static <T> Result<T> successData(T data) {
        return new Result<>(0, "操作成功", data);
    }
}