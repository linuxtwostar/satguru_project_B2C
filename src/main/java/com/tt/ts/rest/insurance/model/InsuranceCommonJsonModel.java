package com.tt.ts.rest.insurance.model;

import java.util.List;

import com.ws.services.insurance.bean.product.FProduct;
import com.ws.services.insurance.bean.product.ProductFamilyPriceResponseBean;

public class InsuranceCommonJsonModel
{
	private InsuranceWidget insuranceWidget;
	private ProductFamilyPriceResponseBean productFamilyPriceResponseBean;
	// private FlightOption flightOption;
	// private RoundTripFlightOption roundTripFlightOption;
	private List<Object> agencyMarkUp;
	private String nameQuote;
	private String companyQuote;
	private String[] emailQuote;
	private String sendQuoteMailDivContent;

	private List<FProduct> fProduct;

	private String countryId;
	private String branchId;
	private String agencyId;

	public InsuranceWidget getInsuranceWidget()
	{
		return insuranceWidget;
	}

	public void setInsuranceWidget(InsuranceWidget insuranceWidget)
	{
		this.insuranceWidget = insuranceWidget;
	}

	public ProductFamilyPriceResponseBean getProductFamilyPriceResponseBean()
	{
		return productFamilyPriceResponseBean;
	}

	public void setProductFamilyPriceResponseBean(ProductFamilyPriceResponseBean productFamilyPriceResponseBean)
	{
		this.productFamilyPriceResponseBean = productFamilyPriceResponseBean;
	}

	public List<Object> getAgencyMarkUp()
	{
		return agencyMarkUp;
	}

	public void setAgencyMarkUp(List<Object> agencyMarkUp)
	{
		this.agencyMarkUp = agencyMarkUp;
	}

	public String getNameQuote()
	{
		return nameQuote;
	}

	public void setNameQuote(String nameQuote)
	{
		this.nameQuote = nameQuote;
	}

	public String getCompanyQuote()
	{
		return companyQuote;
	}

	public void setCompanyQuote(String companyQuote)
	{
		this.companyQuote = companyQuote;
	}

	public String[] getEmailQuote()
	{
		return emailQuote;
	}

	public void setEmailQuote(String[] emailQuote)
	{
		this.emailQuote = emailQuote;
	}

	public String getSendQuoteMailDivContent()
	{
		return sendQuoteMailDivContent;
	}

	public void setSendQuoteMailDivContent(String sendQuoteMailDivContent)
	{
		this.sendQuoteMailDivContent = sendQuoteMailDivContent;
	}

	public String getCountryId()
	{
		return countryId;
	}

	public void setCountryId(String countryId)
	{
		this.countryId = countryId;
	}

	public String getBranchId()
	{
		return branchId;
	}

	public void setBranchId(String branchId)
	{
		this.branchId = branchId;
	}

	public String getAgencyId()
	{
		return agencyId;
	}

	public void setAgencyId(String agencyId)
	{
		this.agencyId = agencyId;
	}

	public List<FProduct> getfProduct()
	{
		return fProduct;
	}

	public void setfProduct(List<FProduct> fProduct)
	{
		this.fProduct = fProduct;
	}
}
