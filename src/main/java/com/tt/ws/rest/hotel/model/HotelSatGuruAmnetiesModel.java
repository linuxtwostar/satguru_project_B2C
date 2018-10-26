package com.tt.ws.rest.hotel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
@Table(name="TT_SATGURU_HOTEL_AMNETIES")
public class HotelSatGuruAmnetiesModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SATGURU_HOTEL_AMNETIES_ID")
	private int satguruHotelAmnetiesId;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="STATUS")
	private int status;
	
	@Column(name="CREATION_TIME")
	@Temporal(TemporalType.DATE)
	private Date creationTime;
	
	@Column(name="CREATED_BY")
	private int createdBy;
	
	@Column(name="LAST_UPDATED_BY")
	private int lastUpdatedBy;
	
	@Transient
	private String amenitiesName;
	
	@Transient
	private int amenitiesId;
	
	@Transient
	private String amenitiesImgPath;
	
	@Transient
	private String amenitiesImgAlt;
	
	@Transient
	private String amenitiesValue;
	
	@Column(name="LAST_MOD_TIME")
	@Temporal(TemporalType.DATE)
	private Date lastModifTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SATGURU_HOTEL_HOTEL_ID")
	private HotelSatguruModel satguruHotelId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="HOTEL_AMNETIES_HOTEL_AMNETIES_ID")
	private HotelAmnetiesModel hotelAmnetiesDetails;
	
	public int getSatguruHotelAmnetiesId() {
		return satguruHotelAmnetiesId;
	}

	public void setSatguruHotelAmnetiesId(int satguruHotelAmnetiesId) {
		this.satguruHotelAmnetiesId = satguruHotelAmnetiesId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
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
	public HotelSatguruModel getSatguruHotelId() {
		return satguruHotelId;
	}

	public void setSatguruHotelId(HotelSatguruModel satguruHotelId) {
		this.satguruHotelId = satguruHotelId;
	}

	@JsonManagedReference
	public HotelAmnetiesModel getHotelAmnetiesDetails() {
		return hotelAmnetiesDetails;
	}

	public void setHotelAmnetiesDetails(HotelAmnetiesModel hotelAmnetiesDetails) {
		this.hotelAmnetiesDetails = hotelAmnetiesDetails;
	}

	public String getAmenitiesName() {
		return amenitiesName;
	}

	public void setAmenitiesName(String amenitiesName) {
		this.amenitiesName = amenitiesName;
	}

	public int getAmenitiesId() {
		return amenitiesId;
	}

	public void setAmenitiesId(int amenitiesId) {
		this.amenitiesId = amenitiesId;
	}

	public String getAmenitiesImgPath() {
		return amenitiesImgPath;
	}

	public void setAmenitiesImgPath(String amenitiesImgPath) {
		this.amenitiesImgPath = amenitiesImgPath;
	}

	public String getAmenitiesImgAlt() {
		return amenitiesImgAlt;
	}

	public void setAmenitiesImgAlt(String amenitiesImgAlt) {
		this.amenitiesImgAlt = amenitiesImgAlt;
	}

	public String getAmenitiesValue() {
		return amenitiesValue;
	}

	public void setAmenitiesValue(String amenitiesValue) {
		this.amenitiesValue = amenitiesValue;
	}
	
}
