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
@Table(name="TT_SATGURU_HOTEL_CONTACTS")
public class HotelSatguruContactsModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HOTEL_CONTACTS_ID")
	private int hotelContactId;
	
	@Column(name="SITE_ID")
	private int siteID;
	
	@Column(name="CONTACT_NAME")
	private String contactNAme;
	
	@Column(name="DESIGNATION")
	private String designation;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="PHONE_NUMBER")
	private String phoneNo;
	
	@Column(name="FAX_NUMBER")
	private String faxNo;
	
	@Column(name="MOBILE_NUMBER")
	private String mobileNo;
	
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

	public int getHotelContactId() {
		return hotelContactId;
	}

	public void setHotelContactId(int hotelContactId) {
		this.hotelContactId = hotelContactId;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getContactNAme() {
		return contactNAme;
	}

	public void setContactNAme(String contactNAme) {
		this.contactNAme = contactNAme;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
