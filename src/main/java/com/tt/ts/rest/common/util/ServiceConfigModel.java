package com.tt.ts.rest.common.util;

import java.util.ArrayList;
import java.util.List;

public class ServiceConfigModel {
	private String productName;
	private String currencyName;
	private String currencyCode;
	private String companyName;
	
	private List<SupplierConfigModal> supplier =new ArrayList<>();
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<SupplierConfigModal> getSupplier() {
		return supplier;
	}
	public void setSupplier(List<SupplierConfigModal> supplier) {
		this.supplier = supplier;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
