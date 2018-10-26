package com.tt.ws.rest.insurance.modal;

public class InsuranceValidation {

	private String insuranceOptionKey;
	private double markupPrice;
	private double t3Price;
	private double discountPrice;
	private double serviceChargePrice;
	private double agencyMarkup;
	private String currency;
	private String supplierCurrency;
	private double totalBaseFare;
	private double baseAmountBySupplier;
	
	public String getInsuranceOptionKey()
	{
		return insuranceOptionKey;
	}
	public void setInsuranceOptionKey(String insuranceOptionKey)
	{
		this.insuranceOptionKey = insuranceOptionKey;
	}
	public double getMarkupPrice()
	{
		return markupPrice;
	}
	public void setMarkupPrice(double markupPrice)
	{
		this.markupPrice = markupPrice;
	}
	public double getT3Price()
	{
		return t3Price;
	}
	public void setT3Price(double t3Price)
	{
		this.t3Price = t3Price;
	}
	public double getDiscountPrice()
	{
		return discountPrice;
	}
	public void setDiscountPrice(double discountPrice)
	{
		this.discountPrice = discountPrice;
	}
	public double getServiceChargePrice()
	{
		return serviceChargePrice;
	}
	public void setServiceChargePrice(double serviceChargePrice)
	{
		this.serviceChargePrice = serviceChargePrice;
	}
	public double getAgencyMarkup()
	{
		return agencyMarkup;
	}
	public void setAgencyMarkup(double agencyMarkup)
	{
		this.agencyMarkup = agencyMarkup;
	}
	public String getCurrency()
	{
		return currency;
	}
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	public String getSupplierCurrency()
	{
		return supplierCurrency;
	}
	public void setSupplierCurrency(String supplierCurrency)
	{
		this.supplierCurrency = supplierCurrency;
	}
	public double getTotalBaseFare()
	{
		return totalBaseFare;
	}
	public void setTotalBaseFare(double totalBaseFare)
	{
		this.totalBaseFare = totalBaseFare;
	}
	public double getBaseAmountBySupplier() {
		return baseAmountBySupplier;
	}
	public void setBaseAmountBySupplier(double baseAmountBySupplier) {
		this.baseAmountBySupplier = baseAmountBySupplier;
	}
	
	@Override
	public String toString() {
		return "InsuranceValidation [insuranceOptionKey=" + insuranceOptionKey
				+ ", markupPrice=" + markupPrice + ", t3Price=" + t3Price
				+ ", discountPrice=" + discountPrice + ", serviceChargePrice="
				+ serviceChargePrice + ", agencyMarkup=" + agencyMarkup
				+ ", currency=" + currency + ", supplierCurrency="
				+ supplierCurrency + ", totalBaseFare=" + totalBaseFare
				+ ", baseAmountBySupplier=" + baseAmountBySupplier +"]";
	}
	
}
