package com.example.examsys.Support;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class ResponseData {

    //00000Success,00500Failed,00999Error参数错误
    private String rspCode = "00000";
    private String rspMsg = "Success";
    private Object rspData = new Object();

    public ResponseData() {
    }

    public void setFailed() {
        this.rspCode = "00500";
        this.rspMsg = "Failed";
    }

    public void setError() {
        this.rspCode = "00999";
        this.rspData = "Error";
    }

    public String toJson() {
        return JSON.toJSONString(this.rspData);
    }

}