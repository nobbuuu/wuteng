package own.stromsong.myapplication.mvp.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */

public class HttpResponse<T> implements Serializable {
    private String errorMsg;
    private String errorCode;
    private boolean success;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private T results;

    public T getData() {
        return results;
    }

    public void setData(T data) {
        this.results = data;
    }
}
