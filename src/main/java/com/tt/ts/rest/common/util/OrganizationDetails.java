package com.tt.ts.rest.common.util;

import java.util.ArrayList;
import java.util.List;


public class OrganizationDetails {
	private Integer companyId;
	private String companyName;
	private Integer branchId;
	private String branchName;
	private Integer currencyId;
	private String currencyName;
	private Integer countryId;
	private String countryName;
	private Integer stateId;
	private String stateName;
	private Integer cityId;
	private String cityName;
	private List<OrganizationPccModel> organizationPccModels = new ArrayList<>();
	private List<OrganizationTicketPccModal> organizationTicketPccModels = new ArrayList<>();

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<OrganizationPccModel> getOrganizationPccModels() {
		return organizationPccModels;
	}

	public void setOrganizationPccModels(List<OrganizationPccModel> organizationPccModels) {
		this.organizationPccModels = organizationPccModels;
	}

	public List<OrganizationTicketPccModal> getOrganizationTicketPccModels() {
		return organizationTicketPccModels;
	}

	public void setOrganizationTicketPccModels(
			List<OrganizationTicketPccModal> organizationTicketPccModels) {
		this.organizationTicketPccModels = organizationTicketPccModels;
	}

}
