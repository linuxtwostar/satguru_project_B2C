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
@Table(name="TT_HOTEL_ZIP_TS")
public class HotelZipDetailsModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_ZIP_ID")
	private int ttHotelZipId;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="HOTEL_ZIP_CODE")
	private String hotelZipCode;
	
	@Column(name="HOTEL_ZIP_NAME")
	private String hotelZipName;
	
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
	@JoinColumn(name="HOTEL_CITY_TS_HOTEL_CITY_ID")
	@JsonManagedReference
	private HotelCityDetailsModel ttHotelCityDetails;*/

	public int getTtHotelZipId() {
		return ttHotelZipId;
	}

	public void setTtHotelZipId(int ttHotelZipId) {
		this.ttHotelZipId = ttHotelZipId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getHotelZipCode() {
		return hotelZipCode;
	}

	public void setHotelZipCode(String hotelZipCode) {
		this.hotelZipCode = hotelZipCode;
	}

	public String getHotelZipName() {
		return hotelZipName;
	}

	public void setHotelZipName(String hotelZipName) {
		this.hotelZipName = hotelZipName;
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

	/*public HotelCityDetailsModel getTtHotelCityDetails() {
		return ttHotelCityDetails;
	}

	public void setTtHotelCityDetails(HotelCityDetailsModel ttHotelCityDetails) {
		this.ttHotelCityDetails = ttHotelCityDetails;
	}*/
	
	
}
