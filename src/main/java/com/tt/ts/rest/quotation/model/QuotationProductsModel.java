package com.tt.ts.rest.quotation.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

public class QuotationProductsModel {

	private Integer quotationProductId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private QuotationModel quotationModel;
	
	private String productName = "flight";

	private Integer noOfServices;

	private Date fromDate;

	private Date toDate;

	private Integer paxes;

	private Integer adults;

	private Integer child;

	private Integer infant;

	@OneToMany(mappedBy = "quotationProductsModel", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<QuotationFlightModel> quotationFlightModel = new ArrayList<QuotationFlightModel>();

	@OneToMany(mappedBy = "quotationProductsModel", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<QuotationHotelModel> quotationHotelModel = new ArrayList<QuotationHotelModel>();

	// Getters and Setters

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getNoOfServices() {
		return noOfServices;
	}

	public void setNoOfServices(Integer noOfServices) {
		this.noOfServices = noOfServices;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getPaxes() {
		return paxes;
	}

	public void setPaxes(Integer paxes) {
		this.paxes = paxes;
	}

	public Integer getAdults() {
		return adults;
	}

	public void setAdults(Integer adults) {
		this.adults = adults;
	}

	public Integer getChild() {
		return child;
	}

	public void setChild(Integer child) {
		this.child = child;
	}

	public Integer getInfant() {
		return infant;
	}

	public void setInfant(Integer infant) {
		this.infant = infant;
	}

	public Integer getQuotationProductId() {
		return quotationProductId;
	}

	public void setQuotationProductId(Integer quotationProductId) {
		this.quotationProductId = quotationProductId;
	}

	public QuotationModel getQuotationModel() {
		return quotationModel;
	}

	public void setQuotationModel(QuotationModel quotationModel) {
		this.quotationModel = quotationModel;
	}

	public List<QuotationFlightModel> gettTQuoteFlight() {
		return quotationFlightModel;
	}

	public void settTQuoteFlight(List<QuotationFlightModel> quotationFlightModel) {
		this.quotationFlightModel = quotationFlightModel;
	}

	public List<QuotationHotelModel> gettTQuoteHotel() {
		return quotationHotelModel;
	}

	public void settTQuoteHotel(List<QuotationHotelModel> quotationHotelModel) {
		this.quotationHotelModel = quotationHotelModel;
	}
	
}
