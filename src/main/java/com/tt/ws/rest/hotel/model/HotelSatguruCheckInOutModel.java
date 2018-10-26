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
@Table(name="TT_SATGURU_HOTEL_CHECKINOUT")
public class HotelSatguruCheckInOutModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_CHECKINOUT_ID")
	private int hotelCheckInOutId;
	
	@Column(name="SITE_ID")
	private int siteID;
	
	@Column(name="CHECK_TYPE")
	private String checkType;
	
	@Column(name="MON_TIME")
	private String monTime;
	
	@Column(name="TUES_TIME")
	private String tuesTime;
	
	@Column(name="WED_TIME")
	private String wedTime;
	
	@Column(name="THU_TIME")
	private String thuTime;
	
	@Column(name="FRI_TIME")
	private String friTime;
	
	@Column(name="SAT_TIME")
	private String satTime;
	
	@Column(name="SUN_TIME")
	private String sunTime;
	
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SATGURU_HOTEL_HOTEL_ID")
	private HotelSatguruModel satguruHotelModel;

	public int getHotelCheckInOutId() {
		return hotelCheckInOutId;
	}

	public void setHotelCheckInOutId(int hotelCheckInOutId) {
		this.hotelCheckInOutId = hotelCheckInOutId;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getMonTime() {
		return monTime;
	}

	public void setMonTime(String monTime) {
		this.monTime = monTime;
	}

	public String getTuesTime() {
		return tuesTime;
	}

	public void setTuesTime(String tuesTime) {
		this.tuesTime = tuesTime;
	}

	public String getWedTime() {
		return wedTime;
	}

	public void setWedTime(String wedTime) {
		this.wedTime = wedTime;
	}

	public String getThuTime() {
		return thuTime;
	}

	public void setThuTime(String thuTime) {
		this.thuTime = thuTime;
	}

	public String getFriTime() {
		return friTime;
	}

	public void setFriTime(String friTime) {
		this.friTime = friTime;
	}

	public String getSatTime() {
		return satTime;
	}

	public void setSatTime(String satTime) {
		this.satTime = satTime;
	}

	public String getSunTime() {
		return sunTime;
	}

	public void setSunTime(String sunTime) {
		this.sunTime = sunTime;
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

	@JsonBackReference
	public HotelSatguruModel getSatguruHotelModel() {
		return satguruHotelModel;
	}

	public void setSatguruHotelModel(HotelSatguruModel satguruHotelModel) {
		this.satguruHotelModel = satguruHotelModel;
	}
	
	
	
}
