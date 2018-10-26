package com.tt.ts.rest.quotation.model;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;

public class QuotationHotelModel {

	private Integer quotationHotelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private QuotationProductsModel quotationProductsModel;

	private String quoteHotelName;

	private String quoteHotelPlace;

	private Date checkInDate;

	private Date checkOutDate;

	private Integer rooms;

	private Double cost;

	private Double taxes;

	private Double discount;

	private Double totalPrice;

	private String hotelId;

	// Getters and Setters

	public QuotationProductsModel gettTQuotationProducts() {
		return quotationProductsModel;
	}

	public void settTQuotationProducts(QuotationProductsModel quotationProductsModel) {
		this.quotationProductsModel = quotationProductsModel;
	}

	public String getQuoteHotelName() {
		return quoteHotelName;
	}

	public void setQuoteHotelName(String quoteHotelName) {
		this.quoteHotelName = quoteHotelName;
	}

	public String getQuoteHotelPlace() {
		return quoteHotelPlace;
	}

	public void setQuoteHotelPlace(String quoteHotelPlace) {
		this.quoteHotelPlace = quoteHotelPlace;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Integer getRooms() {
		return rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
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

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getQuotationHotelId() {
		return quotationHotelId;
	}

	public void setQuotationHotelId(Integer quotationHotelId) {
		this.quotationHotelId = quotationHotelId;
	}

	public QuotationProductsModel getQuotationProductsModel() {
		return quotationProductsModel;
	}

	public void setQuotationProductsModel(QuotationProductsModel quotationProductsModel) {
		this.quotationProductsModel = quotationProductsModel;
	}

}
