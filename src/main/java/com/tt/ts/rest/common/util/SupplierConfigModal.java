package com.tt.ts.rest.common.util;

import java.util.List;
import java.util.Set;

import com.ws.services.flight.config.GDSDealCodeModel;


public class SupplierConfigModal {
	private String pccType;
	private String supplierName;
	private String credentialName;
	private List<CredentialConfigModel> credential;
	private List<String> restrictedAirlines;
	private List<OrganizationTicketPccModal> organizationTicketPccModels;
	private List<GDSDealCodeModel> dealCodes;
	private int supplierId;
	private Integer pccId;
	private String supplierCurrency;
	
	private double currencyConversionRate;
	private boolean isCurrencyRateFound;
	
	private String credentialType;
	
	private String countriesName;
	
	private Set<String> countriesNameSet;
	
	private Set<String> citiesName;
	 
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public List<CredentialConfigModel> getCredential() {
		return credential;
	}
	public void setCredential(List<CredentialConfigModel> credential) {
		this.credential = credential;
	}
	public List<String> getRestrictedAirlines()
	{
		return restrictedAirlines;
	}
	public void setRestrictedAirlines(List<String> restrictedAirlines)
	{
		this.restrictedAirlines = restrictedAirlines;
	}
	public String getPccType() {
		return pccType;
	}
	public void setPccType(String pccType) {
		this.pccType = pccType;
	}
	public List<OrganizationTicketPccModal> getOrganizationTicketPccModels() {
		return organizationTicketPccModels;
	}
	public void setOrganizationTicketPccModels(
			List<OrganizationTicketPccModal> organizationTicketPccModels) {
		this.organizationTicketPccModels = organizationTicketPccModels;
	}
	public List<GDSDealCodeModel> getDealCodes()
	{
		return dealCodes;
	}
	public void setDealCodes(List<GDSDealCodeModel> dealCodes)
	{
		this.dealCodes = dealCodes;
	}
	public Integer getPccId() {
		return pccId;
	}
	public void setPccId(Integer pccId) {
		this.pccId = pccId;
	}
	public int getSupplierId()
	{
		return supplierId;
	}
	public void setSupplierId(int supplierId)
	{
		this.supplierId = supplierId;
	}
	public String getSupplierCurrency() {
		return supplierCurrency;
	}
	public void setSupplierCurrency(String supplierCurrency) {
		this.supplierCurrency = supplierCurrency;
	}
	public double getCurrencyConversionRate() {
		return currencyConversionRate;
	}
	public void setCurrencyConversionRate(double currencyConversionRate) {
		this.currencyConversionRate = currencyConversionRate;
	}
	public boolean isCurrencyRateFound() {
		return isCurrencyRateFound;
	}
	public void setCurrencyRateFound(boolean isCurrencyRateFound) {
		this.isCurrencyRateFound = isCurrencyRateFound;
	}
	public String getCredentialType() {
		return credentialType;
	}
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}
	
	public String getCountriesName() {
		return countriesName;
	}
	public void setCountriesName(String countriesName) {
		this.countriesName = countriesName;
	}
	public Set<String> getCountriesNameSet() {
		return countriesNameSet;
	}
	public void setCountriesNameSet(Set<String> countriesNameSet) {
		this.countriesNameSet = countriesNameSet;
	}
	public Set<String> getCitiesName() {
		return citiesName;
	}
	public void setCitiesName(Set<String> citiesName) {
		this.citiesName = citiesName;
	}
	public String getCredentialName()
	{
		return credentialName;
	}
	public void setCredentialName(String credentialName)
	{
		this.credentialName = credentialName;
	}
	
}
