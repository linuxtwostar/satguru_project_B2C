package com.tt.ts.rest.common.util;

import java.util.ArrayList;
import java.util.List;

import com.ws.services.flight.config.GDSDealCodeModel;

public class OrganizationTicketPccModal {
	
	private String pccType;
	private String supplierName;
	private List<GDSDealCodeModel> dealCodes;
	private int paymentGatewayId;
	private String paymentGatewayName;
	private int supplierId;
	private int pccId;
	
	private String airlineCode;

	private Integer credentialId;

	private String origin;

	private String destination;
	
	private String credentialName;
	
	private List<CredentialConfigModel> credentialModal= new ArrayList<>();
	
	private double currencyConversionRate;
	private String supplierCurrency;
	private boolean isCurrencyRateFound;

	public String getPccType() {
		return pccType;
	}

	public void setPccType(String pccType) {
		this.pccType = pccType;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public List<GDSDealCodeModel> getDealCodes() {
		return dealCodes;
	}

	public void setDealCodes(List<GDSDealCodeModel> dealCodes) {
		this.dealCodes = dealCodes;
	}

	public int getPaymentGatewayId() {
		return paymentGatewayId;
	}

	public void setPaymentGatewayId(int paymentGatewayId) {
		this.paymentGatewayId = paymentGatewayId;
	}

	public String getPaymentGatewayName() {
		return paymentGatewayName;
	}

	public void setPaymentGatewayName(String paymentGatewayName) {
		this.paymentGatewayName = paymentGatewayName;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getPccId() {
		return pccId;
	}

	public void setPccId(int pccId) {
		this.pccId = pccId;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public Integer getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(Integer credentialId) {
		this.credentialId = credentialId;
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

	public String getCredentialName() {
		return credentialName;
	}

	public void setCredentialName(String credentialName) {
		this.credentialName = credentialName;
	}

	public List<CredentialConfigModel> getCredentialModal() {
		return credentialModal;
	}

	public void setCredentialModal(List<CredentialConfigModel> credentialModal) {
		this.credentialModal = credentialModal;
	}



	public String getSupplierCurrency() {
		return supplierCurrency;
	}

	public void setSupplierCurrency(String supplierCurrency) {
		this.supplierCurrency = supplierCurrency;
	}

	public boolean isCurrencyRateFound() {
		return isCurrencyRateFound;
	}

	public void setCurrencyRateFound(boolean isCurrencyRateFound) {
		this.isCurrencyRateFound = isCurrencyRateFound;
	}

	public double getCurrencyConversionRate() {
		return currencyConversionRate;
	}

	public void setCurrencyConversionRate(double currencyConversionRate) {
		this.currencyConversionRate = currencyConversionRate;
	}
	
	
}
