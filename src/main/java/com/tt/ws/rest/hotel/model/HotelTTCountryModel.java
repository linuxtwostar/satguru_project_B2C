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
@Table(name="TT_HOTEL_COUNTRY_TS")
public class HotelTTCountryModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_COUNTRY_ID")
	private int ttHotelCountryId;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="HOTEL_COUNTRY_CODE")
	private String countryCode;
	
	@Column(name="HOTEL_COUNTRY_NAME")
	private String countryName;
	
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

	public int getTtHotelCountryId() {
		return ttHotelCountryId;
	}

	public void setTtHotelCountryId(int ttHotelCountryId) {
		this.ttHotelCountryId = ttHotelCountryId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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
	
	
}
