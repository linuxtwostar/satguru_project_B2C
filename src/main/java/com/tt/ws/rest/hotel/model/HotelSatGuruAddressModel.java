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

import org.codehaus.jackson.annotate.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="TT_SATGURU_HOTEL_ADDRESS")
public class HotelSatGuruAddressModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_ADDRESS_ID")
	private int hotelAddressId;
	
	@Column(name="SITE_ID")
	private int siteID;
	
	@Column(name="ADDRESS_TYPE")
	private String addressTpe;
	
	@Column(name="ADDRESS1")
	private String address1;
	
	@Column(name="ADDRESS2")
	private String address2;
	
	@Column(name="HOTEL_ZIP_LONGITUDE")
	private String longitude;
	
	@Column(name="HOTEL_ZIP_LATTITUDE")
	private String latitude;
	
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
	private HotelSatguruModel satguruHotelId;
	
	/*@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="HOTEL_ZIP_TS_HOTEL_ZIP_ID")
	@JsonManagedReference
	private HotelZipDetailsModel ttHotelZipDetails;*/

	public int getHotelAddressId() {
		return hotelAddressId;
	}

	public void setHotelAddressId(int hotelAddressId) {
		this.hotelAddressId = hotelAddressId;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getAddressTpe() {
		return addressTpe;
	}

	public void setAddressTpe(String addressTpe) {
		this.addressTpe = addressTpe;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
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

	/*public HotelZipDetailsModel getTtHotelZipDetails() {
		return ttHotelZipDetails;
	}

	public void setTtHotelZipDetails(HotelZipDetailsModel ttHotelZipDetails) {
		this.ttHotelZipDetails = ttHotelZipDetails;
	}*/
	
	
}
