package com.tt.ts.rest.agent.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TT_AGENCY_MARKUP")
public class AgencyMarkupModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "MARKUP_ID", unique = true, nullable = false)
	private Integer markupId;

	@Column(name = "USER_ID")
	private int userId;
	
	@Column(name = "CORPORATE_ID")
	private int corporateId;
	
	@Column(name = "AGENCY_ID")
	private int agencyId;
	
	@Column(name = "PRODUCT_REF_ID")
	private int productRefId;
	
	@Column(name = "PRODUCT_ID")
	private Integer prodId ;

	@Column(name = "MARKING_FOR")
	private int markingFor; // corporate 0  or retail 1 
	
	@Column(name = "MARKUP_TYPE")
	private int markupType ;	// % 0 or value 1
	
	@Column(name = "MARKUP_VALUE")
	private int markupValue ;

	@Column(name = "DOM_INTERNATIONAL")
	private Integer domOrInternational=0;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	@Column(name = "STATUS")
	private int status;
	
	@Transient
	private transient String airlineName;
	
	@Transient
	private transient String productName;
	
	@Transient
	private transient String airlineCode;

	public Integer getMarkupId() {
		return markupId;
	}

	public void setMarkupId(Integer markupId) {
		this.markupId = markupId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(int corporateId) {
		this.corporateId = corporateId;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public int getProductRefId() {
		return productRefId;
	}

	public void setProductRefId(int productRefId) {
		this.productRefId = productRefId;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public int getMarkingFor() {
		return markingFor;
	}

	public void setMarkingFor(int markingFor) {
		this.markingFor = markingFor;
	}

	public int getMarkupType() {
		return markupType;
	}

	public void setMarkupType(int markupType) {
		this.markupType = markupType;
	}

	public int getMarkupValue() {
		return markupValue;
	}

	public void setMarkupValue(int markupValue) {
		this.markupValue = markupValue;
	}

	public Integer getDomOrInternational() {
		return domOrInternational;
	}

	public void setDomOrInternational(Integer domOrInternational) {
		this.domOrInternational = domOrInternational;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
