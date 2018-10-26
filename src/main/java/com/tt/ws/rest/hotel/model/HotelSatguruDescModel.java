package com.tt.ws.rest.hotel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name="TT_SATGURU_HOTEL_DESCRIPTION")
public class HotelSatguruDescModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SATGURU_HOTEL_DESCRIPTION_ID")
	private int hotelDescID;
	
	@Column(name="SITE_ID")
	private int siteID;
	
	@Column(name="HOTEL_DESCRIPTION_TYPE")
	private String descType;
	
	@Column(name="HOTEL_DESCRIPTION_VALUE")
	private String descValue;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SATGURU_HOTEL_HOTEL_ID")
	@JsonBackReference
	private HotelSatguruModel satguruHotelId;
	
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
	

	public int getHotelDescID() {
		return hotelDescID;
	}

	public void setHotelDescID(int hotelDescID) {
		this.hotelDescID = hotelDescID;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getDescType() {
		return descType;
	}

	public void setDescType(String descType) {
		this.descType = descType;
	}

	public String getDescValue() {
		return descValue;
	}

	public void setDescValue(String descValue) {
		this.descValue = descValue;
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

	public HotelSatguruModel getSatguruHotelId() {
		return satguruHotelId;
	}

	public void setSatguruHotelId(HotelSatguruModel satguruHotelId) {
		this.satguruHotelId = satguruHotelId;
	}

	
	
}
