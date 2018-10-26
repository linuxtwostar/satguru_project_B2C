package com.tt.ws.rest.Activities.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.ws.services.activities.bean.ActivitiesBean;
import com.ws.services.activities.bean.ActivitiesContentBean;



@JsonIgnoreProperties(ignoreUnknown=true)
public class ActivitiesModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String operationId;
	private String serverId;
	private String environment;
	private String processTime;
	private String timestamp;
    private String errorMsg;
	private String errorCode;
	private List<ActivitiesContentBean> activities;
	
	private String itemPerPage;
	private String page;
	private String totalItems;
	private List<ActivitiesBean> activitiesList;
	private Set<String> featuresList;
	private Set<String> activityTypeSet;
	
	
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getProcessTime() {
		return processTime;
	}
	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
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
	public List<ActivitiesContentBean> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivitiesContentBean> activities) {
		this.activities = activities;
	}
	public String getItemPerPage() {
		return itemPerPage;
	}
	public void setItemPerPage(String itemPerPage) {
		this.itemPerPage = itemPerPage;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(String totalItems) {
		this.totalItems = totalItems;
	}
	public List<ActivitiesBean> getActivitiesList() {
		return activitiesList;
	}
	public void setActivitiesList(List<ActivitiesBean> activitiesList) {
		this.activitiesList = activitiesList;
	}
	public Set<String> getFeaturesList() {
		return featuresList;
	}
	public void setFeaturesList(Set<String> featuresList) {
		this.featuresList = featuresList;
	}
	public Set<String> getActivityTypeSet() {
		return activityTypeSet;
	}
	public void setActivityTypeSet(Set<String> activityTypeSet) {
		this.activityTypeSet = activityTypeSet;
	}
	
	
	
}
