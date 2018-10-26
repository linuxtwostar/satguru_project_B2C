package com.tt.ts.rest.quotation.model;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;

public class QuotationFlightModel {

	private Integer quotationFlightId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private QuotationProductsModel quotationProductsModel;

	private String quoteFlightName;

	private String tripType;

	private String flightClass;

	private String origin;

	private String destination;

	private Date departTime;

	private Date arrivalTime;

	private Double cost;

	private Double taxes;

	private Double discount;

	private Double totalPrice;

	private String onwardFlightNo;

	private String returnFlightNo;

	// Getters and Setters

	public QuotationProductsModel gettTQuotationProducts() {
		return quotationProductsModel;
	}

	public void settTQuotationProducts(QuotationProductsModel quotationProductsModel) {
		this.quotationProductsModel = quotationProductsModel;
	}

	public String getQuoteFlightName() {
		return quoteFlightName;
	}

	public void setQuoteFlightName(String quoteFlightName) {
		this.quoteFlightName = quoteFlightName;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getFlightClass() {
		return flightClass;
	}

	public void setFlightClass(String flightClass) {
		this.flightClass = flightClass;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getDepartTime() {
		return departTime;
	}

	public void setDepartTime(Date departTime) {
		this.departTime = departTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public String getOnwardFlightNo() {
		return onwardFlightNo;
	}

	public void setOnwardFlightNo(String onwardFlightNo) {
		this.onwardFlightNo = onwardFlightNo;
	}

	public String getReturnFlightNo() {
		return returnFlightNo;
	}

	public void setReturnFlightNo(String returnFlightNo) {
		this.returnFlightNo = returnFlightNo;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getQuotationFlightId() {
		return quotationFlightId;
	}

	public void setQuotationFlightId(Integer quotationFlightId) {
		this.quotationFlightId = quotationFlightId;
	}

	public QuotationProductsModel getQuotationProductsModel() {
		return quotationProductsModel;
	}

	public void setQuotationProductsModel(QuotationProductsModel quotationProductsModel) {
		this.quotationProductsModel = quotationProductsModel;
	}

	
}
