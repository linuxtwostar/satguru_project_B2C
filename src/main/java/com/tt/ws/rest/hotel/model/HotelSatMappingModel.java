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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;

@Entity
@Table(name="TT_SATGURU_HOTEL_SUPP_MAP")
public class HotelSatMappingModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="MAPPING_ID")
	private int mappingId;
	
	@Column(name="SATGURU_ID")
	private String satguruId;
	
	@Column(name="CLARIFI_ID")
	private int clarifiId;
	
	@Column(name="SOURCESUPPLIER")
	private String sourceSupp;
	
	@Column(name="SUPPLIER_CODE")
	private String suppCode;
	
	@Column(name="SUPPLIER_HOTEL_CODE")
	private String suppHotelCode;
	
	@Temporal(TemporalType.DATE)
	@Column(name="CREATION_TIME")
	private Date creationTime;
	
	@Column(name="CREATED_BY")
	private int createdBy;
	
	@Column(name="LAST_UPDATED_BY")
	private int lastUpdatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_MOD_TIME")
	private Date lastModifTime;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="HOTEL_ID",unique=true)
	private HotelSatguruModel satguruHotelModel;

	public int getMappingId() {
		return mappingId;
	}

	public void setMappingId(int mappingId) {
		this.mappingId = mappingId;
	}

	public String getSatguruId() {
		return satguruId;
	}

	public void setSatguruId(String satguruId) {
		this.satguruId = satguruId;
	}

	public int getClarifiId() {
		return clarifiId;
	}

	public void setClarifiId(int clarifiId) {
		this.clarifiId = clarifiId;
	}

	public String getSourceSupp() {
		return sourceSupp;
	}

	public void setSourceSupp(String sourceSupp) {
		this.sourceSupp = sourceSupp;
	}

	public String getSuppCode() {
		return suppCode;
	}

	public void setSuppCode(String suppCode) {
		this.suppCode = suppCode;
	}

	public String getSuppHotelCode() {
		return suppHotelCode;
	}

	public void setSuppHotelCode(String suppHotelCode) {
		this.suppHotelCode = suppHotelCode;
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
