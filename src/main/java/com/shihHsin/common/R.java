package com.shihHsin.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>
 * 用戶信息
 * </p>
 *
 * @author madhu
 * @since 2024-4-12
 */
@Data
public class R<T> {

    private Integer code; //編碼：1成功，0和其它數字為失敗

    private String msg; //錯誤訊息

    private T data; //數據

    private Map map = new HashMap(); //動態數據

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
