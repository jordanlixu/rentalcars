package com.example.rentalcars.common;

import java.io.Serializable;

public class ResultEntity  implements Serializable {

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


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private String code;
    private String msg;
    private Object data;

    public static ResultEntity getErrorEntity(String msg){
        return new ResultEntity(Constants.FAIL_CODE,msg);
    }

    public ResultEntity(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultEntity(Object data) {
        this.data = data;
        this.code = Constants.SUCCESS_CODE;
        this.msg = "succeed";
    }
}
