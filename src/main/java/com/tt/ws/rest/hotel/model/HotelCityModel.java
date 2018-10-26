package com.tt.ws.rest.hotel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class HotelCityModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int hotelCityId;
	private int siteId;
	private String hotelCityCode;
	private String cityName;
	private int stateId;
	private int countryId;
	@Temporal(TemporalType.DATE)
	private Date creationTime;
	private int createdBy;
	private int lastUpdatedBy;
	@Temporal(TemporalType.DATE)
	private Date lastModifTime;
	public int getHotelCityId() {
		return hotelCityId;
	}
	public void setHotelCityId(int hotelCityId) {
		this.hotelCityId = hotelCityId;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getHotelCityCode() {
		return hotelCityCode;
	}
	public void setHotelCityCode(String hotelCityCode) {
		this.hotelCityCode = hotelCityCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(int lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getLastModifTime() {
		return lastModifTime;
	}
	public void setLastModifTime(Date lastModifTime) {
		this.lastModifTime = lastModifTime;
	}
	@Override
	public String toString() {
		return "HotelCityModel [hotelCityId=" + hotelCityId + ", siteId="
				+ siteId + ", hotelCityCode=" + hotelCityCode + ", cityName="
				+ cityName + ", stateId=" + stateId + ", countryId="
				+ countryId + ", creationTime=" + creationTime + ", createdBy="
				+ createdBy + ", lastUpdatedBy=" + lastUpdatedBy
				+ ", lastModifTime=" + lastModifTime + "]";
	}
	
	
	
}
