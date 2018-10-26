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
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name="TT_HOTEL_AMNETIES_TS",uniqueConstraints = {
	    @UniqueConstraint(columnNames = { "HOTEL_AMNETIES_NAME", "HOTEL_AMNETIES_ID", "REF_AMNETIES_ID" })})
public class HotelAmnetiesModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_AMNETIES_ID")
	private int ttHotelAmnetiesId;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="HOTEL_AMNETIES_NAME")
	private String hotelAmnetiesName;
	
	@Column(name="HOTEL_AMNETIES_ICON_PATH")
	private String hotelAmnetiesIconPath;
	
	@Column(name="REF_AMNETIES_ID")
	private int amnetiesRefId;
	
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

	@JsonBackReference
	public int getTtHotelAmnetiesId() {
		return ttHotelAmnetiesId;
	}

	public void setTtHotelAmnetiesId(int ttHotelAmnetiesId) {
		this.ttHotelAmnetiesId = ttHotelAmnetiesId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getHotelAmnetiesName() {
		return hotelAmnetiesName;
	}

	public void setHotelAmnetiesName(String hotelAmnetiesName) {
		this.hotelAmnetiesName = hotelAmnetiesName;
	}

	public String getHotelAmnetiesIconPath() {
		return hotelAmnetiesIconPath;
	}

	public void setHotelAmnetiesIconPath(String hotelAmnetiesIconPath) {
		this.hotelAmnetiesIconPath = hotelAmnetiesIconPath;
	}

	public int getAmnetiesRefId() {
		return amnetiesRefId;
	}

	public void setAmnetiesRefId(int amnetiesRefId) {
		this.amnetiesRefId = amnetiesRefId;
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
