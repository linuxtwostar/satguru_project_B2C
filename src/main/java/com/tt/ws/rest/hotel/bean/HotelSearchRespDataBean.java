package com.tt.ws.rest.hotel.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tt.ws.rest.hotel.model.HotelSatguruModel;

public class HotelSearchRespDataBean implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private List<HotelsFareRuleRespBean> respList = new ArrayList<>();
	
	private String countryId;

	private String branchId;
	
	private String agencyId;
	
	private String clarifiId;
	
	private StringBuilder supplierRespTime = new StringBuilder("");
	
	private StringBuilder hotelCodes;
	
	private List<HotelSatguruModel> hotelModel;

	private int credentialId;
	
	private StringBuilder timeString;
	
	private List<String> suppCurrencyFail = new ArrayList<>();
	
	public List<HotelsFareRuleRespBean> getRespList() {
		return respList;
	}

	public void setRespList(List<HotelsFareRuleRespBean> respList) {
		this.respList = respList;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getClarifiId() {
		return clarifiId;
	}

	public void setClarifiId(String clarifiId) {
		this.clarifiId = clarifiId;
	}

	public List<HotelSatguruModel> getHotelModel() {
		return hotelModel;
	}

	public void setHotelModel(List<HotelSatguruModel> hotelModel) {
		this.hotelModel = hotelModel;
	}
	public StringBuilder getSupplierRespTime() {
		return supplierRespTime;
	}

	public void setSupplierRespTime(StringBuilder supplierRespTime) {
		this.supplierRespTime = supplierRespTime;
	}

	public int getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(int credentialId) {
		this.credentialId = credentialId;
	}

	public StringBuilder getHotelCodes() {
		return hotelCodes;
	}

	public void setHotelCodes(StringBuilder hotelCodes) {
		this.hotelCodes = hotelCodes;
	}

	public StringBuilder getTimeString() {
		return timeString;
	}

	public void setTimeString(StringBuilder timeString) {
		this.timeString = timeString;
	}

	public List<String> getSuppCurrencyFail() {
		return suppCurrencyFail;
	}

	public void setSuppCurrencyFail(List<String> suppCurrencyFail) {
		this.suppCurrencyFail = suppCurrencyFail;
	}
}
