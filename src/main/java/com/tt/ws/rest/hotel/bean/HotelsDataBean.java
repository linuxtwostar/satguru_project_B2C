package com.tt.ws.rest.hotel.bean;

import java.io.Serializable;

public class HotelsDataBean  implements Serializable
{

	private static final long serialVersionUID = 1L;
	
	private String hotelName;
	private String hotelcode;
    private String hotelGroup;
    private String starRating;
    private String destinationCountry;
    private String destinationRegion;
    private String destinationCity;
    private String bookingStartDate;
    private String bookingEndDate;
    private String blackoutStartDate;
    private String blackoutEndDate;
    private Double fareFromTo;
    private String nationality;
    private Integer noOfPax;
    private Integer noOfRooms;
    private String roomType;
    private String ratePlanType;
    private String customerType;
    private String crossSellingPath;
    private String corporateId;
    private String forEx;
    private double t3Price;
    
    
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	
	public String getHotelcode() {
		return hotelcode;
	}
	public void setHotelcode(String hotelcode) {
		this.hotelcode = hotelcode;
	}
	public String getHotelGroup() {
		return hotelGroup;
	}
	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}
	public String getStarRating() {
		return starRating;
	}
	public void setStarRating(String starRating) {
		this.starRating = starRating;
	}
	public String getDestinationCountry() {
		return destinationCountry;
	}
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	public String getDestinationRegion() {
		return destinationRegion;
	}
	public void setDestinationRegion(String destinationRegion) {
		this.destinationRegion = destinationRegion;
	}
	public String getDestinationCity() {
		return destinationCity;
	}
	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}
	
	public String getBookingStartDate() {
		return bookingStartDate;
	}
	public void setBookingStartDate(String bookingStartDate) {
		this.bookingStartDate = bookingStartDate;
	}
	public String getBookingEndDate() {
		return bookingEndDate;
	}
	public void setBookingEndDate(String bookingEndDate) {
		this.bookingEndDate = bookingEndDate;
	}
	public String getBlackoutStartDate() {
		return blackoutStartDate;
	}
	public void setBlackoutStartDate(String blackoutStartDate) {
		this.blackoutStartDate = blackoutStartDate;
	}
	public String getBlackoutEndDate() {
		return blackoutEndDate;
	}
	public void setBlackoutEndDate(String blackoutEndDate) {
		this.blackoutEndDate = blackoutEndDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getFareFromTo() {
		return fareFromTo;
	}
	public void setFareFromTo(Double fareFromTo) {
		this.fareFromTo = fareFromTo;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Integer getNoOfPax() {
		return noOfPax;
	}
	public void setNoOfPax(Integer noOfPax) {
		this.noOfPax = noOfPax;
	}
	public Integer getNoOfRooms() {
		return noOfRooms;
	}
	public void setNoOfRooms(Integer noOfRooms) {
		this.noOfRooms = noOfRooms;
	}
	public String getRoomType() {
		return roomType;
	}
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	public String getRatePlanType() {
		return ratePlanType;
	}
	public void setRatePlanType(String ratePlanType) {
		this.ratePlanType = ratePlanType;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getCrossSellingPath() {
		return crossSellingPath;
	}
	public void setCrossSellingPath(String crossSellingPath) {
		this.crossSellingPath = crossSellingPath;
	}
	public String getCorporateId() {
		return corporateId;
	}
	public void setCorporateId(String corporateId) {
		this.corporateId = corporateId;
	}
	
	public String getForEx() {
		return forEx;
	}
	public void setForEx(String forEx) {
		this.forEx = forEx;
	}
	public double getT3Price() {
		return t3Price;
	}
	public void setT3Price(double t3Price) {
		this.t3Price = t3Price;
	}
	
	
}

