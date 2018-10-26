package com.tt.ws.rest.Activities.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.tt.satguruportal.activities.model.ActivitiesWidget;
import com.tt.satguruportal.agent.model.AgencyMarkup;
import com.ws.services.activities.bean.ActivitiesSearchRequestBean;

public class ActivitiesResultRequestBean implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private ActivitiesSearchRequestBean searchRequestBean;
	
	private ActivitiesWidget activitiesWidget;
	
	private int markUpType;

	private float totalAgencyMarkUp;

	private AgencyMarkup agencyMarkup;

	private String filteredResultCacheKey;

	private String filteredTenResultCacheKey;

	private String allFiltersResult;
	
	private String tenFilteredResult;

	public ActivitiesSearchRequestBean getSearchRequestBean() {
		return searchRequestBean;
	}

	public void setSearchRequestBean(ActivitiesSearchRequestBean searchRequestBean) {
		this.searchRequestBean = searchRequestBean;
	}

	public ActivitiesWidget getActivitiesWidget() {
		return activitiesWidget;
	}

	public void setActivitiesWidget(ActivitiesWidget activitiesWidget) {
		this.activitiesWidget = activitiesWidget;
	}

	public int getMarkUpType() {
		return markUpType;
	}

	public void setMarkUpType(int markUpType) {
		this.markUpType = markUpType;
	}

	public float getTotalAgencyMarkUp() {
		return totalAgencyMarkUp;
	}

	public void setTotalAgencyMarkUp(float totalAgencyMarkUp) {
		this.totalAgencyMarkUp = totalAgencyMarkUp;
	}

	public AgencyMarkup getAgencyMarkup() {
		return agencyMarkup;
	}

	public void setAgencyMarkup(AgencyMarkup agencyMarkup) {
		this.agencyMarkup = agencyMarkup;
	}

	public String getFilteredResultCacheKey() {
		return filteredResultCacheKey;
	}

	public void setFilteredResultCacheKey(String filteredResultCacheKey) {
		this.filteredResultCacheKey = filteredResultCacheKey;
	}

	public String getFilteredTenResultCacheKey() {
		return filteredTenResultCacheKey;
	}

	public void setFilteredTenResultCacheKey(String filteredTenResultCacheKey) {
		this.filteredTenResultCacheKey = filteredTenResultCacheKey;
	}

	public String getTenFilteredResult() {
		return tenFilteredResult;
	}

	public void setTenFilteredResult(String tenFilteredResult) {
		this.tenFilteredResult = tenFilteredResult;
	}

	public String getAllFiltersResult() {
		return allFiltersResult;
	}

	public void setAllFiltersResult(String allFiltersResult) {
		this.allFiltersResult = allFiltersResult;
	}

}
