package com.tt.ws.rest.hotel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="TT_HOTEL_STATE_TS")
public class HotelStateDetailsModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_STATE_ID")
	private int ttHotelStateId;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="HOTEL_STATE_CODE")
	private String hotelStateCode;
	
	@Column(name="HOTEL_STATE_NAME")
	private String hotelStateName;
	
	@Column(name="STATUS")
	private int status;
	
	@Column(name="CREATION_TIME")
	@Temporal(TemporalType.DATE)
	private Date creationTime;
	
	@Column(name="CREATED_BY")
	private int createdBy;
	
	@Column(name="LAST_UPDATED_BY")
	private int lastUpdatedBy;
	
	@Column(name="LAST_MOD_TIME")
	@Temporal(TemporalType.DATE)
	private Date lastModifTime;
	
	/*@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="HOTEL_COUNTRY_TS_HOTEL_COUNTRY_ID")
	@JsonManagedReference
	private HotelCountryDetailsModel ttHotelCountryDetails;*/

	public int getTtHotelStateId() {
		return ttHotelStateId;
	}

	public void setTtHotelStateId(int ttHotelStateId) {
		this.ttHotelStateId = ttHotelStateId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getHotelStateCode() {
		return hotelStateCode;
	}

	public void setHotelStateCode(String hotelStateCode) {
		this.hotelStateCode = hotelStateCode;
	}

	public String getHotelStateName() {
		return hotelStateName;
	}

	public void setHotelStateName(String hotelStateName) {
		this.hotelStateName = hotelStateName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

/*	public HotelCountryDetailsModel getTtHotelCountryDetails() {
		return ttHotelCountryDetails;
	}

	public void setTtHotelCountryDetails(HotelCountryDetailsModel ttHotelCountryDetails) {
		this.ttHotelCountryDetails = ttHotelCountryDetails;
	}*/
	
	
}
