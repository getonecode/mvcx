package guda.mvcx.core.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by well on 2017/3/24.
 */
public class BizResult {

    public boolean success;

    public String code;

    public String msg;

    public Map<String,Object> data = new HashMap<String,Object>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
