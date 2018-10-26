package com.tt.ts.rest.error.modal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TT_TS_ERROR_CODES")
public class ErrorCodeModal
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ERROR_ID")
	private Integer errorId;
	
	@Column(name="ERROR_CODE")
	private String errorCode = "";
	
	@Column(name="ERROR_DESC")
	private String errorDesc = "";
	
	@Column(name="VENDOR")
	private String vendorName = "";
	
	@Column(name="STATUS")
	private int status ;
	
	@Column(name="CREATED_BY")
	private int createdBy;
	
	@Column(name="UPDATED_BY")
	private int updatedBy;
	
	@Column(name="CREATED_DATE_TIME")
	private Date createdDateTime;
	
	@Column(name="UPDATED_DATE_TIME")
	private Date updatedDateTime ;
	
	@Column(name="DISPLAY_MESSAGE")
	private String displayMessage = "";
	
	@Column(name="PRODUCT")
	private String product = "";
	
	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorDesc()
	{
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc)
	{
		this.errorDesc = errorDesc;
	}

	public String getVendorName()
	{
		return vendorName;
	}

	public void setVendorName(String vendorName)
	{
		this.vendorName = vendorName;
	}
	public Integer getErrorId()
	{
		return errorId;
	}

	public void setErrorId(Integer errorId)
	{
		this.errorId = errorId;
	}

	public String getDisplayMessage()
	{
		return displayMessage;
	}

	public void setDisplayMessage(String displayMessage)
	{
		this.displayMessage = displayMessage;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	public int getUpdatedBy()
	{
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy)
	{
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDateTime()
	{
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime)
	{
		this.createdDateTime = createdDateTime;
	}

	public Date getUpdatedDateTime()
	{
		return updatedDateTime;
	}

	public void setUpdatedDateTime(Date updatedDateTime)
	{
		this.updatedDateTime = updatedDateTime;
	}

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	

}
