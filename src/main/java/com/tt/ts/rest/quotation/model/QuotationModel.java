package com.tt.ts.rest.quotation.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

public class QuotationModel {

	private Integer quotationId = 5;
	
	private Integer agentId =1;

	private Integer agencyId =1;

	private Integer clientId =1;

	private Double totalPrice;

	private Double totalTax;

	private Double totalDiscount;

	private Double finalPrice;

	private Date validUpto = new Date();

	private Character quoteFlag;

	private int siteId;
	
	private int status;  
	  
	private Date creationTime;  
	  
	private int createdBy;
	
	private int lastUpdatedBy;  
	  
	private Date lastModTime;
	
	@OneToMany(mappedBy = "quotationModel", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<QuotationProductsModel> quotationProductsModel = new ArrayList<QuotationProductsModel>();

	// Getters and Setters

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public Date getValidUpto() {
		return validUpto;
	}

	public void setValidUpto(Date validUpto) {
		this.validUpto = validUpto;
	}

	public Character getQuoteFlag() {
		return quoteFlag;
	}

	public void setQuoteFlag(Character quoteFlag) {
		this.quoteFlag = quoteFlag;
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

	public Date getLastModTime() {
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime) {
		this.lastModTime = lastModTime;
	}

	public List<QuotationProductsModel> gettTQuotationProducts() {
		return quotationProductsModel;
	}

	public void settTQuotationProducts(List<QuotationProductsModel> quotationProductsModel) {
		this.quotationProductsModel = quotationProductsModel;
	}

	public Integer getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Integer quotationId) {
		this.quotationId = quotationId;
	}

	public List<QuotationProductsModel> getQuotationProductsModel() {
		return quotationProductsModel;
	}

	public void setQuotationProductsModel(List<QuotationProductsModel> quotationProductsModel) {
		this.quotationProductsModel = quotationProductsModel;
	}

}
