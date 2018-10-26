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
@Table(name="TT_SATGURU_HOTEL_IMAGES")
public class HotelSatguruImgModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SATGURU_HOTEL_IMAGES_ID")
	private int hotelImgID;
	
	@Column(name="SITE_ID")
	private int siteId;
	
	@Column(name="HOTEL_IMAGE_NAME")
	private String hotelImgName;
	
	@Column(name="HOTEL_IMAGE_WIDTH_PX")
	private String imgWithPx;
	
	@Column(name="HOTEL_IMAGE_HEIGHT_PX")
	private String imgHeightPx;
	
	@Column(name="HOTEL_IMAGE_DIMENSION")
	private String imgDimension;
	
	@Column(name="HOTEL_IMAGE_SUPPLIER_URL")
	private String imgSupplierUrl;
	
	@Column(name="IMAGE_CAPTION")
	private String imgCaption;
	
	@Column(name="IMAGE_TYPE")
	private String imgType;
	
	@Column(name="IS_ENABLED")
	private boolean isEnabled;
	
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

	public int getHotelImgID() {
		return hotelImgID;
	}

	public void setHotelImgID(int hotelImgID) {
		this.hotelImgID = hotelImgID;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	

	public String getHotelImgName() {
		return hotelImgName;
	}

	public void setHotelImgName(String hotelImgName) {
		this.hotelImgName = hotelImgName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getImgWithPx() {
		return imgWithPx;
	}

	public void setImgWithPx(String imgWithPx) {
		this.imgWithPx = imgWithPx;
	}

	public String getImgHeightPx() {
		return imgHeightPx;
	}

	public void setImgHeightPx(String imgHeightPx) {
		this.imgHeightPx = imgHeightPx;
	}

	public String getImgDimension() {
		return imgDimension;
	}

	public void setImgDimension(String imgDimension) {
		this.imgDimension = imgDimension;
	}

	public String getImgSupplierUrl() {
		return imgSupplierUrl;
	}

	public void setImgSupplierUrl(String imgSupplierUrl) {
		this.imgSupplierUrl = imgSupplierUrl;
	}

	public String getImgCaption() {
		return imgCaption;
	}

	public void setImgCaption(String imgCaption) {
		this.imgCaption = imgCaption;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
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

	@Override
	public String toString() {
		return "HotelSatguruImgModel [hotelImgID=" + hotelImgID + ", siteId="
				+ siteId + ", hotelImgName=" + hotelImgName + ", imgWithPx="
				+ imgWithPx + ", imgHeightPx=" + imgHeightPx
				+ ", imgDimension=" + imgDimension + ", imgSupplierUrl="
				+ imgSupplierUrl + ", imgCaption=" + imgCaption + ", imgType="
				+ imgType + ", isEnabled=" + isEnabled + ", status=" + status
				+ ", creationTime=" + creationTime + ", createdBy=" + createdBy
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", lastModifTime="
				+ lastModifTime + ", satguruHotelId=" + satguruHotelId
				+ ", getHotelImgID()=" + getHotelImgID() + ", getSiteId()="
				+ getSiteId() + ", getHotelImgName()=" + getHotelImgName()
				+ ", getImgWithPx()=" + getImgWithPx() + ", getImgHeightPx()="
				+ getImgHeightPx() + ", getImgDimension()=" + getImgDimension()
				+ ", getImgSupplierUrl()=" + getImgSupplierUrl()
				+ ", getImgCaption()=" + getImgCaption() + ", getImgType()="
				+ getImgType() + ", isEnabled()=" + isEnabled()
				+ ", getStatus()=" + getStatus() + ", getCreationTime()="
				+ getCreationTime() + ", getCreatedBy()=" + getCreatedBy()
				+ ", getLastUpdatedBy()=" + getLastUpdatedBy()
				+ ", getLastModifTime()=" + getLastModifTime()
				+ ", getSatguruHotelId()=" + getSatguruHotelId()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	
	
	
}
