/**
 * Copyright 2022 bejson.com
 */
package com.example.netnew.bean;

import java.util.List;

public class NewsTitle {

    private String code;
    private boolean charge;
    private String msg;
    private Result result;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public boolean getCharge() {
        return charge;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public class Result {

        private String status;
        private String msg;
        private List<String> result;

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }

        public List<String> getResult() {
            return result;
        }

    }
}