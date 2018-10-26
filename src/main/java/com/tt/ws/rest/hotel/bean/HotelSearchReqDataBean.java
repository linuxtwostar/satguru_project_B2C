package com.tt.ws.rest.hotel.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HotelSearchReqDataBean implements Serializable
{
	private String searchInputParam;
	
	private int pageNumber;
	
	private String hotelSearchKey;
	
	private String hotelId;
	
	private String cityName;
	
	private String countryName;
	
	private String hotelName;
	
	private String latitude;
	
	private String longitude;
	
	private String geoDistance;

	private String branchId;
	
	private String hotelCheckIn;

	private String hotelCheckOut;
	
	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getSearchInputParam() {
		return searchInputParam;
	}

	public void setSearchInputParam(String searchInputParam) {
		this.searchInputParam = searchInputParam;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getHotelSearchKey() {
		return hotelSearchKey;
	}

	public void setHotelSearchKey(String hotelSearchKey) {
		this.hotelSearchKey = hotelSearchKey;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getGeoDistance() {
		return geoDistance;
	}

	public void setGeoDistance(String geoDistance) {
		this.geoDistance = geoDistance;
	}

	public String getHotelCheckIn() {
		return hotelCheckIn;
	}

	public void setHotelCheckIn(String hotelCheckIn) {
		this.hotelCheckIn = hotelCheckIn;
	}

	public String getHotelCheckOut() {
		return hotelCheckOut;
	}

	public void setHotelCheckOut(String hotelCheckOut) {
		this.hotelCheckOut = hotelCheckOut;
	}

}
