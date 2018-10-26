package com.test.model;

import java.util.List;
import java.util.Map;

public class ServiceResponse {  private String jsonString;
private String errorMsg;
private String errorCode;
private String responseStatus = "Success";
private List<?> resultList;
private Map<?,?> resultMap;
private Object resultObject;
private Boolean resultBoolean;
private Integer resultInteger;


public Map<?, ?> getResultMap() {
    return resultMap;
}

public void setResultMap(Map<?, ?> resultMap) {
    this.resultMap = resultMap;
}

public Object getResultObject() {
    return resultObject;
}

public void setResultObject(Object resultObject) {
    this.resultObject = resultObject;
}

public Boolean getResultBoolean() {
    return resultBoolean;
}

public void setResultBoolean(Boolean resultBoolean) {
    this.resultBoolean = resultBoolean;
}

public String getJsonString() {
    return jsonString;
}

public String getErrorCode()
{
    return errorCode;
}

public void setErrorCode(String errorCode)
{
    this.errorCode = errorCode;
}

public String getResponseStatus()
{
    return responseStatus;
}

public void setResponseStatus(String responseStatus)
{
    this.responseStatus = responseStatus;
}

public void setJsonString(String jsonString) {
    this.jsonString = jsonString;
}

public String getErrorMsg() {
    return errorMsg;
}

public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
}

public List<?> getResultList() {
    return resultList;
}

public void setResultList(List<?> resultList) {
    this.resultList = resultList;
}

public Integer getResultInteger()
{
	return resultInteger;
}

public void setResultInteger(Integer resultInteger)
{
	this.resultInteger = resultInteger;
}
}
