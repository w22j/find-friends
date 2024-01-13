package com.tu.hb.common;

/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(0, data, "ok");
    }

    /**
     * 失败
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<T>(errorCode);
    }


    /**
     * 失败
     * @param code
     * @param msg
     * @param description
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(int code, String msg, String description) {
        return new BaseResponse(code,null, msg, description);
    }

    /**
     * 失败
     * @param errorCode
     * @param msg
     * @param description
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String msg, String description) {
        return new BaseResponse(errorCode.getCode(),null, msg, description);
    }

    /**
     * 失败
     * @param errorCode
     * @param description
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode.getCode(), null, errorCode.getMsg(), description);
    }

}
