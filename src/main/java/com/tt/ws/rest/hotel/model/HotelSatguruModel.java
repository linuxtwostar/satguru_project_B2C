package com.tt.ws.rest.hotel.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.ws.services.hotel.bean.HotelRoomCategoryBean;



@Entity
@Table(name="TT_SATGURU_HOTEL")
@JsonIgnoreProperties(ignoreUnknown=true)
public class HotelSatguruModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_ID")
	private int hotelId;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="HOTEL_NAME")
	private String hotelName;
	
	@Column(name="HOTEL_CLARIFYID")
	private int clarifiId;
	
	@Transient
	private double grossPrice;
	
	@Transient
	private String grossPriceWithoutMarkup;
	
	@Transient
	private String discountPrice;
	
	@Transient
	private String serviceChargePrice;
	
	@Transient
	private String t3Price;
	
	@Transient
	private String supplierCurrency;
	
	@Transient
	private String currencyCode;
	
	@Transient
	private String agencyCurrency;
	
	@Transient
	private Double currConvertRate;
	
	@Transient
	private Double supplierPrice;
	
	@Transient
	private String gtaRoomCatId;
	
	@Transient
	private List<String> gtaNoOfRooms;
	
	@Transient
	private List<String> gtaRoomCode;
	
	@Transient
	private String sessionId;
	
	@Transient
	private String totalRoomsAvail;
	
	@Transient
	private String resultIndex;
	
	@Transient
	private String roomCode;
	
	@Transient
	private String rateKey;
	
	@Transient
	private String bookThrough;
	
	@Transient
	private String serviceProvider;
	
	@Transient
	private String serviceProviderCode;
	
	@Transient
	private String quoteSavedFrom;
	
	@Transient
	private String retrivedFromSaveQuote;
	
	@Transient
	private List<HotelRoomCategoryBean> roomDetails ;
	
	@Transient
	private List<String> roomCombinationIndexList ;
	
	
	@Transient
	private List<HotelRoomCategoryBean> hotelListRoomDetails ;
	
	@Transient
	private String orgFare;
	
	@Transient
	private String t3Markup;
	
	@Transient
	private String taxsAmount;
	
	@Transient
	private String cityCode;
	
	@Transient
	private String cityName;
	
	@Transient
	private String countryName;
	
	@Transient
	private String hotelDesc;
	
	@Transient
	private String roomAvailStatus;
	
	@Transient
	private String hotelOption;
	
	@Transient
	private float markUpValue;
	
	@Transient
	private String cityId;
	
	@Transient
	private String distanceFromGeoLocation;
	
	@Transient
	private String hotelInfo;
	
	@Transient
    private List<String> inclusions;
	
	@Transient
	private Set<String> offers;
	
	@Transient
	private String highlightDetails;
	
	@Transient
	private int requestedRoomsCount;
	
	@Transient
	private float priceWithoutDiscount;
	
	@Transient
	private float priceWithoutServiceCharge;
	
	@Transient
	private StringBuilder supplierRespTime = new StringBuilder("");
	
	@Transient
	private int count;
	
	@Transient
	private double minPrice;
	
	@Transient
	private double maxPrice;
	
	@Transient
	private String hotelStaticCode;
	
	@Transient
	private boolean isRoomValidated; 
	
	@Column(name="HOTEL_SATGURU_ID")
	private String satguruHotelId;
	
	@Column(name="HOTEL_STAR_RATING")
	private String hotelStarRating;
	
	@Column(name="HOTEL_WEBSITE_URL")
	private String hotelWebsiteUrl;

	@Column(name="HOTEL_PHONE_NUMMBER")
	private String hotelPhoneNo;
	
	@Column(name="HOTEL_FAX_NUMBER")
	private String hotelFaxNo;
	
	@Column(name="HOTEL_LANGUAGE_TS_HOTEL_LANGUAGE_ID")
	private int tsHotelLangId;
	
	@Column(name="HOTEL_ENABLED")
	private boolean isEnabled;
	
	@Column(name="HOTEL_DESTINATION_CODE")
	private String hotelDestCode;
	
	@Column(name="STATUS")
	private int status;
	
	@Column(name="HOTEL_SUPPLIER_TS_HOTEL_SUPPLIER_ID")
	private int supplierHotelCode;
	
	@Column(name="HOTEL_HIGHLIGHTS")
	private String hotelHighlights;
	
	@Transient
	private double totalAmtToPay;
	
	@Transient
	private double totalAgencyMarkup;
	
	@Transient
	private int credentialId;
	
	@Transient
	private String hotelCheckInTime;
	
	@Transient
	private String hotelCheckOutTime;
	
	@OneToMany(mappedBy="satguruHotelId")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatguruDescModel> hotelDescription;
	
	@OneToMany(mappedBy="satguruHotelId")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatGuruAmnetiesModel> hotelAmneties;
	
	@OneToOne(mappedBy="satguruHotelId")
	@LazyCollection(LazyCollectionOption.FALSE)
	private HotelRecommendationModel hotelRecommendation;
	
	@OneToMany(mappedBy="satguruHotelId")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatguruImgModel> hotelImages;
	
	@OneToMany(mappedBy="satguruHotelModel")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatguruContactsModel> satguruHotelContactDetails;
	
	@OneToMany(mappedBy="satguruHotelModel")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatguruCheckInOutModel> satguruHotelCheckInOut;
	
	@OneToMany(mappedBy="satguruHotelId")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatGuruAddressModel> satguruHotelAddress;
	
	@OneToMany(mappedBy="satguruHotelModel")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<HotelSatMappingModel> hotelSatSuppModel;

		
	@Column(name="HOTEL_CITY_TS_HOTEL_CITY_ID")
	private int tsHotelCityId;
	
	@Column(name="CREATION_TIME")
	@Temporal(TemporalType.DATE)
	private Date creationTime;
	
	@Column(name="CREATED_BY")
	private int createdBy;
	
	@Column(name="LAST_UPDATED_BY")
	private int lastUpdatedBy;
	
	@Column(name="LAST_MOD_TIME")
	@Temporal(TemporalType.DATE)
	private Date modifTime;
	
	@Column(name="IS_HOTEL_BASE_NET_PRICE")
	private String hotelBaseNetPrice;
	
	@Column(name="NEGOTIATED_RATE")
	private String negotitationRate;
	
	@Column(name="LATITUDE")
	private String latitude;
	
	@Column(name="LONGITUDE")
	private String longitude;
	
	@Transient
	private String FILTERED_RESULT_CACHE_KEY;
	
	@Transient
	private String FILTERED_TEN_RESULT_CACHE_KEY;
	
	@Transient
	private String SUPP_PRICE_RESULT_CACHE_KEY;

	@Transient
	private String timeString;
	
	@Transient
	private Set<String> uniqueStarRating;
	
	@Transient
	private boolean isRsultFromCache;
	
	@Transient
	private List<String> highlights;
	
	@Transient
	private Double updatedTaxsAmount;
	
	@Transient
	private Double totalVariableAmount;
	

	@Transient
	private Double perRoomVariableAmount;
	
	@Transient
	private String finalSelectedRoomIndex;
	
	@Transient
	private String optForBookingFixedFormat;
	
	
	
	public int getHotelId() {
		return hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public int getClarifiId() {
		return clarifiId;
	}

	public void setClarifiId(int clarifiId) {
		this.clarifiId = clarifiId;
	}


	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getT3Price() {
		return t3Price;
	}

	public void setT3Price(String t3Price) {
		this.t3Price = t3Price;
	}

	public String getGtaRoomCatId() {
		return gtaRoomCatId;
	}

	public void setGtaRoomCatId(String gtaRoomCatId) {
		this.gtaRoomCatId = gtaRoomCatId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTotalRoomsAvail() {
		return totalRoomsAvail;
	}

	public void setTotalRoomsAvail(String totalRoomsAvail) {
		this.totalRoomsAvail = totalRoomsAvail;
	}

	public String getResultIndex() {
		return resultIndex;
	}

	public void setResultIndex(String resultIndex) {
		this.resultIndex = resultIndex;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRateKey() {
		return rateKey;
	}

	public void setRateKey(String rateKey) {
		this.rateKey = rateKey;
	}

	public String getBookThrough() {
		return bookThrough;
	}

	public void setBookThrough(String bookThrough) {
		this.bookThrough = bookThrough;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getServiceProviderCode() {
		return serviceProviderCode;
	}

	public void setServiceProviderCode(String serviceProviderCode) {
		this.serviceProviderCode = serviceProviderCode;
	}

	
	



	public String getOrgFare() {
		return orgFare;
	}

	public void setOrgFare(String orgFare) {
		this.orgFare = orgFare;
	}

	public String getTaxsAmount() {
		return taxsAmount;
	}

	public void setTaxsAmount(String taxsAmount) {
		this.taxsAmount = taxsAmount;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getSatguruHotelId() {
		return satguruHotelId;
	}

	public void setSatguruHotelId(String satguruHotelId) {
		this.satguruHotelId = satguruHotelId;
	}

	public String getHotelStarRating() {
		return hotelStarRating;
	}

	public void setHotelStarRating(String hotelStarRating) {
		this.hotelStarRating = hotelStarRating;
	}

	public String getHotelWebsiteUrl() {
		return hotelWebsiteUrl;
	}

	public void setHotelWebsiteUrl(String hotelWebsiteUrl) {
		this.hotelWebsiteUrl = hotelWebsiteUrl;
	}

	public String getHotelPhoneNo() {
		return hotelPhoneNo;
	}

	public void setHotelPhoneNo(String hotelPhoneNo) {
		this.hotelPhoneNo = hotelPhoneNo;
	}

	public String getHotelFaxNo() {
		return hotelFaxNo;
	}

	public void setHotelFaxNo(String hotelFaxNo) {
		this.hotelFaxNo = hotelFaxNo;
	}

	public int getTsHotelLangId() {
		return tsHotelLangId;
	}

	public void setTsHotelLangId(int tsHotelLangId) {
		this.tsHotelLangId = tsHotelLangId;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getHotelDestCode() {
		return hotelDestCode;
	}

	public void setHotelDestCode(String hotelDestCode) {
		this.hotelDestCode = hotelDestCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSupplierHotelCode() {
		return supplierHotelCode;
	}

	public void setSupplierHotelCode(int supplierHotelCode) {
		this.supplierHotelCode = supplierHotelCode;
	}

	@JsonManagedReference
	public Set<HotelSatguruDescModel> getHotelDescription() {
		return hotelDescription;
	}

	public void setHotelDescription(Set<HotelSatguruDescModel> hotelDescription) {
		this.hotelDescription = hotelDescription;
	}

	@JsonManagedReference
	public Set<HotelSatGuruAmnetiesModel> getHotelAmneties() {
		return hotelAmneties;
	}

	public void setHotelAmneties(Set<HotelSatGuruAmnetiesModel> hotelAmneties) {
		this.hotelAmneties = hotelAmneties;
	}

	@JsonManagedReference
	public HotelRecommendationModel getHotelRecommendation() {
		return hotelRecommendation;
	}

	public void setHotelRecommendation(HotelRecommendationModel hotelRecommendation) {
		this.hotelRecommendation = hotelRecommendation;
	}

	@JsonManagedReference
	public Set<HotelSatguruImgModel> getHotelImages() {
		return hotelImages;
	}

	public void setHotelImages(Set<HotelSatguruImgModel> hotelImages) {
		this.hotelImages = hotelImages;
	}

	@JsonManagedReference
	public Set<HotelSatguruContactsModel> getSatguruHotelContactDetails() {
		return satguruHotelContactDetails;
	}

	public void setSatguruHotelContactDetails(
			Set<HotelSatguruContactsModel> satguruHotelContactDetails) {
		this.satguruHotelContactDetails = satguruHotelContactDetails;
	}

	@JsonManagedReference
	public Set<HotelSatguruCheckInOutModel> getSatguruHotelCheckInOut() {
		return satguruHotelCheckInOut;
	}

	public void setSatguruHotelCheckInOut(
			Set<HotelSatguruCheckInOutModel> satguruHotelCheckInOut) {
		this.satguruHotelCheckInOut = satguruHotelCheckInOut;
	}

	@JsonManagedReference
	public Set<HotelSatGuruAddressModel> getSatguruHotelAddress() {
		return satguruHotelAddress;
	}

	public void setSatguruHotelAddress(
			Set<HotelSatGuruAddressModel> satguruHotelAddress) {
		this.satguruHotelAddress = satguruHotelAddress;
	}

	@JsonManagedReference
	public Set<HotelSatMappingModel> getHotelSatSuppModel() {
		return hotelSatSuppModel;
	}

	public void setHotelSatSuppModel(Set<HotelSatMappingModel> hotelSatSuppModel) {
		this.hotelSatSuppModel = hotelSatSuppModel;
	}

	public int getTsHotelCityId() {
		return tsHotelCityId;
	}

	public void setTsHotelCityId(int tsHotelCityId) {
		this.tsHotelCityId = tsHotelCityId;
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

	public Date getModifTime() {
		return modifTime;
	}

	public void setModifTime(Date modifTime) {
		this.modifTime = modifTime;
	}

	public String getHotelBaseNetPrice() {
		return hotelBaseNetPrice;
	}

	public void setHotelBaseNetPrice(String hotelBaseNetPrice) {
		this.hotelBaseNetPrice = hotelBaseNetPrice;
	}

	public String getNegotitationRate() {
		return negotitationRate;
	}

	public void setNegotitationRate(String negotitationRate) {
		this.negotitationRate = negotitationRate;
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

	public String getHotelDesc() {
		return hotelDesc;
	}

	public void setHotelDesc(String hotelDesc) {
		this.hotelDesc = hotelDesc;
	}

	public String getRoomAvailStatus() {
		return roomAvailStatus;
	}

	public void setRoomAvailStatus(String roomAvailStatus) {
		this.roomAvailStatus = roomAvailStatus;
	}
	
	public float getMarkUpValue() {
		return markUpValue;
	}

	public void setMarkUpValue(float markUpValue) {
		this.markUpValue = markUpValue;
	}

	public String getHotelOption() {
		return hotelOption;
	}

	public void setHotelOption(String hotelOption) {
		this.hotelOption = hotelOption;
	}

	public String getGrossPriceWithoutMarkup() {
		return grossPriceWithoutMarkup;
	}

	public void setGrossPriceWithoutMarkup(String grossPriceWithoutMarkup) {
		this.grossPriceWithoutMarkup = grossPriceWithoutMarkup;
	}

	public String getServiceChargePrice() {
		return serviceChargePrice;
	}

	public void setServiceChargePrice(String serviceChargePrice) {
		this.serviceChargePrice = serviceChargePrice;
	}

	public String getT3Markup() {
		return t3Markup;
	}

	public void setT3Markup(String t3Markup) {
		this.t3Markup = t3Markup;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistanceFromGeoLocation() {
		return distanceFromGeoLocation;
	}

	public void setDistanceFromGeoLocation(String distanceFromGeoLocation) {
		this.distanceFromGeoLocation = distanceFromGeoLocation;
	}

	public String getHotelInfo() {
		return hotelInfo;
	}

	public void setHotelInfo(String hotelInfo) {
		this.hotelInfo = hotelInfo;
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

	public Double getCurrConvertRate() {
		return currConvertRate;
	}

	public void setCurrConvertRate(Double currConvertRate) {
		this.currConvertRate = currConvertRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public List<HotelRoomCategoryBean> getRoomDetails() {
		return roomDetails;
	}
//@JsonIgnore
	public void setRoomDetails(List<HotelRoomCategoryBean> roomDetails) {
		this.roomDetails = roomDetails;
	}

	public List<HotelRoomCategoryBean> getHotelListRoomDetails() {
		return hotelListRoomDetails;
	}
	//@JsonIgnore
	public void setHotelListRoomDetails(List<HotelRoomCategoryBean> hotelListRoomDetails) {
		this.hotelListRoomDetails = hotelListRoomDetails;
	}

	public double getGrossPrice() {
		return grossPrice;
	}

	public void setGrossPrice(double grossPrice) {
		this.grossPrice = grossPrice;
	}

	public List<String> getRoomCombinationIndexList() {
		return roomCombinationIndexList;
	}

	public void setRoomCombinationIndexList(List<String> roomCombinationIndexList) {
		this.roomCombinationIndexList = roomCombinationIndexList;
	}

	public List<String> getGtaNoOfRooms() {
		return gtaNoOfRooms;
	}

	public void setGtaNoOfRooms(List<String> gtaNoOfRooms) {
		this.gtaNoOfRooms = gtaNoOfRooms;
	}

	public List<String> getGtaRoomCode() {
		return gtaRoomCode;
	}

	public void setGtaRoomCode(List<String> gtaRoomCode) {
		this.gtaRoomCode = gtaRoomCode;
	}

	public double getTotalAmtToPay() {
		return totalAmtToPay;
	}

	public void setTotalAmtToPay(double totalAmtToPay) {
		this.totalAmtToPay = totalAmtToPay;
	}

	public double getTotalAgencyMarkup() {
		return totalAgencyMarkup;
	}

	public void setTotalAgencyMarkup(double totalAgencyMarkup) {
		this.totalAgencyMarkup = totalAgencyMarkup;
	}

	public String getHotelHighlights() {
		return hotelHighlights;
	}

	public void setHotelHighlights(String hotelHighlights) {
		this.hotelHighlights = hotelHighlights;
	}

	public List<String> getInclusions() {
		return inclusions;
	}

	public void setInclusions(List<String> inclusions) {
		this.inclusions = inclusions;
	}

	public Set<String> getOffers() {
		return offers;
	}

	public void setOffers(Set<String> offers) {
		this.offers = offers;
	}

	public String getHighlightDetails() {
		return highlightDetails;
	}

	public void setHighlightDetails(String highlightDetails) {
		this.highlightDetails = highlightDetails;
	}

	public int getRequestedRoomsCount() {
		return requestedRoomsCount;
	}

	public void setRequestedRoomsCount(int requestedRoomsCount) {
		this.requestedRoomsCount = requestedRoomsCount;
	}

	public float getPriceWithoutDiscount() {
		return priceWithoutDiscount;
	}

	public void setPriceWithoutDiscount(float priceWithoutDiscount) {
		this.priceWithoutDiscount = priceWithoutDiscount;
	}

	public float getPriceWithoutServiceCharge() {
		return priceWithoutServiceCharge;
	}

	public void setPriceWithoutServiceCharge(float priceWithoutServiceCharge) {
		this.priceWithoutServiceCharge = priceWithoutServiceCharge;
	}

	public boolean isRoomValidated() {
		return isRoomValidated;
	}

	public void setRoomValidated(boolean isRoomValidated) {
		this.isRoomValidated = isRoomValidated;
	}

	public StringBuilder getSupplierRespTime() {
		return supplierRespTime;
	}

	public void setSupplierRespTime(StringBuilder supplierRespTime) {
		this.supplierRespTime = supplierRespTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(int credentialId) {
		this.credentialId = credentialId;
	}

	public String getHotelStaticCode() {
		return hotelStaticCode;
	}

	public void setHotelStaticCode(String hotelStaticCode) {
		this.hotelStaticCode = hotelStaticCode;
	}

	public String getFILTERED_RESULT_CACHE_KEY() {
		return FILTERED_RESULT_CACHE_KEY;
	}

	public void setFILTERED_RESULT_CACHE_KEY(String fILTERED_RESULT_CACHE_KEY) {
		FILTERED_RESULT_CACHE_KEY = fILTERED_RESULT_CACHE_KEY;
	}

	public String getFILTERED_TEN_RESULT_CACHE_KEY() {
		return FILTERED_TEN_RESULT_CACHE_KEY;
	}

	public void setFILTERED_TEN_RESULT_CACHE_KEY(
			String fILTERED_TEN_RESULT_CACHE_KEY) {
		FILTERED_TEN_RESULT_CACHE_KEY = fILTERED_TEN_RESULT_CACHE_KEY;
	}

	public String getSUPP_PRICE_RESULT_CACHE_KEY() {
		return SUPP_PRICE_RESULT_CACHE_KEY;
	}

	public void setSUPP_PRICE_RESULT_CACHE_KEY(String sUPP_PRICE_RESULT_CACHE_KEY) {
		SUPP_PRICE_RESULT_CACHE_KEY = sUPP_PRICE_RESULT_CACHE_KEY;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public boolean isRsultFromCache() {
		return isRsultFromCache;
	}

	public void setRsultFromCache(boolean isRsultFromCache) {
		this.isRsultFromCache = isRsultFromCache;
	}

	public double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Set<String> getUniqueStarRating() {
		return uniqueStarRating;
	}

	public void setUniqueStarRating(Set<String> uniqueStarRating) {
		this.uniqueStarRating = uniqueStarRating;
	}

	public Double getUpdatedTaxsAmount() {
		return updatedTaxsAmount;
	}

	public void setUpdatedTaxsAmount(Double updatedTaxsAmount) {
		this.updatedTaxsAmount = updatedTaxsAmount;
	}

	public Double getTotalVariableAmount() {
		return totalVariableAmount;
	}

	public void setTotalVariableAmount(Double totalVariableAmount) {
		this.totalVariableAmount = totalVariableAmount;
	}

	public Double getPerRoomVariableAmount() {
		return perRoomVariableAmount;
	}

	public void setPerRoomVariableAmount(Double perRoomVariableAmount) {
		this.perRoomVariableAmount = perRoomVariableAmount;
	}

	public List<String> getHighlights() {
		return highlights;
	}

	public void setHighlights(List<String> highlights) {
		this.highlights = highlights;
	}

	public String getHotelCheckInTime() {
		return hotelCheckInTime;
	}

	public void setHotelCheckInTime(String hotelCheckInTime) {
		this.hotelCheckInTime = hotelCheckInTime;
	}

	public String getHotelCheckOutTime() {
		return hotelCheckOutTime;
	}

	public void setHotelCheckOutTime(String hotelCheckOutTime) {
		this.hotelCheckOutTime = hotelCheckOutTime;
	}

	public String getFinalSelectedRoomIndex() {
		return finalSelectedRoomIndex;
	}

	public void setFinalSelectedRoomIndex(String finalSelectedRoomIndex) {
		this.finalSelectedRoomIndex = finalSelectedRoomIndex;
	}

	public String getOptForBookingFixedFormat() {
		return optForBookingFixedFormat;
	}

	public void setOptForBookingFixedFormat(String optForBookingFixedFormat) {
		this.optForBookingFixedFormat = optForBookingFixedFormat;
	}

	public String getQuoteSavedFrom() {
		return quoteSavedFrom;
	}

	public void setQuoteSavedFrom(String quoteSavedFrom) {
		this.quoteSavedFrom = quoteSavedFrom;
	}

	public String getRetrivedFromSaveQuote() {
		return retrivedFromSaveQuote;
	}

	public void setRetrivedFromSaveQuote(String retrivedFromSaveQuote) {
		this.retrivedFromSaveQuote = retrivedFromSaveQuote;
	}
	
	
}
