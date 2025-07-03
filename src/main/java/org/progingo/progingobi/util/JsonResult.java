package org.progingo.progingobi.util;

import java.io.Serializable;

public class JsonResult implements Serializable {
    private int state;//状态码，成功就是200
    private Object data;//返回的数据
    private String info;//需要附加的信息、提示等内容

    public JsonResult(int state) {
        this.state = state;
    }

    public JsonResult(int state, Object data) {
        this.state = state;
        this.data = data;
    }

    public JsonResult(int state, Object data, String info) {
        this.state = state;
        this.data = data;
        this.info = info;
    }

    public static JsonResult ok(){
        return new JsonResult(200);
    }
    public static JsonResult ok(Object data){
        return new JsonResult(200,data);
    }
    public static JsonResult ok(Object data, String info){
        JsonResult result = new JsonResult(200, data);
        result.setInfo(info);
        return result;
    }
    public static JsonResult ok(int state,Object data, String info){
        JsonResult result = new JsonResult(state, data);
        result.setInfo(info);
        return result;
    }
    public static JsonResult fail(String data){
        return new JsonResult(500,data,null);
    }
    public static JsonResult fail(int state, String errorMsg){
        return new JsonResult(state,null,errorMsg);
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}