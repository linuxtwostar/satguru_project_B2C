package com.tt.ts.rest.common;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CommonModal
{
    private Integer groupId;
    
    private Integer productId;
    
    private Integer userId;
    
    private Integer supplierId;
    
    private Integer organizationType;

    private Integer countryId;
    
    private Integer branchId;
    
    private String jsonString;
    
    private String colName;
    
    private Integer intCode;
    
    private Integer quoteNo;
    
    private Integer domOrIntl;
    
    private String organizationId;
    
    private Integer corporateOrRetail;

    @JsonFormat(pattern ="MM/dd/yyyy")
    private Date startDate;

    @JsonFormat(pattern ="MM/dd/yyyy")
    private Date endDate;

    
    public Integer getGroupId()
    {
        return groupId;
    }

    public void setGroupId(Integer groupId)
    {
        this.groupId = groupId;
    }

    public Integer getProductId()
    {
        return productId;
    }

    public void setProductId(Integer productId)
    {
        this.productId = productId;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId)
    {
        this.supplierId = supplierId;
    }

    public Integer getOrganizationType()
    {
        return organizationType;
    }

    public void setOrganizationType(Integer organizationType)
    {
        this.organizationType = organizationType;
    }

    public Integer getCountryId()
    {
        return countryId;
    }

    public void setCountryId(Integer countryId)
    {
        this.countryId = countryId;
    }

    public Integer getBranchId()
    {
        return branchId;
    }

    public void setBranchId(Integer branchId)
    {
        this.branchId = branchId;
    }

    public String getJsonString()
    {
        return jsonString;
    }

    public void setJsonString(String jsonString)
    {
        this.jsonString = jsonString;
    }
    public Integer getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(Integer quoteNo) {
        this.quoteNo = quoteNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Integer getIntCode() {
        return intCode;
    }

    public void setIntCode(Integer intCode) {
        this.intCode = intCode;
    }

	public Integer getDomOrIntl() {
		return domOrIntl;
	}

	public void setDomOrIntl(Integer domOrIntl) {
		this.domOrIntl = domOrIntl;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getCorporateOrRetail() {
		return corporateOrRetail;
	}

	public void setCorporateOrRetail(Integer corporateOrRetail) {
		this.corporateOrRetail = corporateOrRetail;
	}

    
}