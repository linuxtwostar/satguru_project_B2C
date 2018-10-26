package com.tt.ws.rest.hotel.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.ws.services.hotel.bean.HotelRoomCategoryBean;

public class HotelsFareRuleRespBean implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	private String hotelName;
	private String hotelCode;
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
    private double discountPrice;
    private double serviceChargePrice;
    private String supplier;
    private String supplierCode;
    private String roomCode;
    private String currencyCode;
    private String sessionId;
    private String resultIndex;
    private String totalRoomsAvail;
    private String rateKey;
    private transient List<HotelRoomCategoryBean> roomList;
    private transient List<HotelRoomCategoryBean> hotelListRoomDetails;
    private String taxsAmount;
    private String cityCode;
    private String hotelDesc;
    private List<String> inclusions;
    private Set<String> offers;
    private String roomAvailStatus;
    private String hotelOption;
    private String starRatingCode;
    private List<String> gtaRoomCode;
    private List<String> gtaNoOfRooms;
    private String clarifiId;
    private String supplierCurrency;
    private String agencyCurrency;
    private Double currConvertRate;
    private Double supplierPrice;
    private String highligtDetails;
    private int duration;
    private int credentialId;
    private boolean isCurrConvExist =true;
    
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	
	public String getHotelCode() {
		return hotelCode;
	}
	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
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
	public double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}
	public double getServiceChargePrice() {
		return serviceChargePrice;
	}
	public void setServiceChargePrice(double serviceChargePrice) {
		this.serviceChargePrice = serviceChargePrice;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getRoomCode() {
		return roomCode;
	}
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getResultIndex() {
		return resultIndex;
	}
	public void setResultIndex(String resultIndex) {
		this.resultIndex = resultIndex;
	}
	
	public List<HotelRoomCategoryBean> getRoomList() {
		return roomList;
	}
	public void setRoomList(List<HotelRoomCategoryBean> roomList) {
		this.roomList = roomList;
	}
	
	
	public String getTotalRoomsAvail() {
		return totalRoomsAvail;
	}
	public void setTotalRoomsAvail(String totalRoomsAvail) {
		this.totalRoomsAvail = totalRoomsAvail;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	public String getRateKey() {
		return rateKey;
	}
	public void setRateKey(String rateKey) {
		this.rateKey = rateKey;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public String getTaxsAmount() {
		return taxsAmount;
	}
	public void setTaxsAmount(String taxsAmount) {
		this.taxsAmount = taxsAmount;
	}
	public List<HotelRoomCategoryBean> getHotelListRoomDetails() {
		return hotelListRoomDetails;
	}
	public void setHotelListRoomDetails(
			List<HotelRoomCategoryBean> hotelListRoomDetails) {
		this.hotelListRoomDetails = hotelListRoomDetails;
	}
	
	public String getHotelDesc() {
		return hotelDesc;
	}
	public void setHotelDesc(String hotelDesc) {
		this.hotelDesc = hotelDesc;
	}
	
	public List<String> getInclusions() {
		return inclusions;
	}
	public void setInclusions(List<String> inclusions) {
		this.inclusions = inclusions;
	}
	public String getRoomAvailStatus() {
		return roomAvailStatus;
	}
	public void setRoomAvailStatus(String roomAvailStatus) {
		this.roomAvailStatus = roomAvailStatus;
	}
	public Set<String> getOffers() {
		return offers;
	}
	public void setOffers(Set<String> offers) {
		this.offers = offers;
	}
	public String getHotelOption() {
		return hotelOption;
	}
	public void setHotelOption(String hotelOption) {
		this.hotelOption = hotelOption;
	}
	public String getStarRatingCode() {
		return starRatingCode;
	}
	public void setStarRatingCode(String starRatingCode) {
		this.starRatingCode = starRatingCode;
	}
	public List<String> getGtaRoomCode() {
		return gtaRoomCode;
	}
	public void setGtaRoomCode(List<String> gtaRoomCode) {
		this.gtaRoomCode = gtaRoomCode;
	}
	public List<String> getGtaNoOfRooms() {
		return gtaNoOfRooms;
	}
	public void setGtaNoOfRooms(List<String> gtaNoOfRooms) {
		this.gtaNoOfRooms = gtaNoOfRooms;
	}
	public Double getCurrConvertRate() {
		return currConvertRate;
	}
	public void setCurrConvertRate(Double currConvertRate) {
		this.currConvertRate = currConvertRate;
	}
	public String getSupplierCurrency() {
		return supplierCurrency;
	}
	public void setSupplierCurrency(String supplierCurrency) {
		this.supplierCurrency = supplierCurrency;
	}
	public String getAgencyCurrency() {
		return agencyCurrency;
	}
	public void setAgencyCurrency(String agencyCurrency) {
		this.agencyCurrency = agencyCurrency;
	}
	public Double getSupplierPrice() {
		return supplierPrice;
	}
	public void setSupplierPrice(Double supplierPrice) {
		this.supplierPrice = supplierPrice;
	}
	public String getClarifiId() {
		return clarifiId;
	}
	public void setClarifiId(String clarifiId) {
		this.clarifiId = clarifiId;
	}
	public int getCredentialId() {
		return credentialId;
	}
	public void setCredentialId(int credentialId) {
		this.credentialId = credentialId;
	}
	public String getHighligtDetails() {
		return highligtDetails;
	}
	public void setHighligtDetails(String highligtDetails) {
		this.highligtDetails = highligtDetails;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public boolean isCurrConvExist() {
		return isCurrConvExist;
	}
	public void setCurrConvExist(boolean isCurrConvExist) {
		this.isCurrConvExist = isCurrConvExist;
	}
}

