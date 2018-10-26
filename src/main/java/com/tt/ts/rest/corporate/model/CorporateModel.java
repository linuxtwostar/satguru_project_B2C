package com.tt.ts.rest.corporate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "TT_CORPORATE")
public class CorporateModel implements Serializable {

    private static final long serialVersionUID = 2693361727584184537L;

    @Id
    @GeneratedValue(generator = "SEQUENCE_ID", strategy = GenerationType.TABLE)
    @TableGenerator(name = "SEQUENCE_ID", table = "TT_SEQUENCES", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_NEXT_HI_VALUE", pkColumnValue = "CORPORATE_UID", allocationSize = 1)
    @Column(name = "CORP_ID")
    private Integer corpId;

    @Column(name = "CORPORATE_NAME")
    private String corporateName;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "MOBILE_CODE")
    private String mobileCode;

    @NotNull
    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "FAX")
    private String fax;

    @NotEmpty
    @Size(min = 1, max = 250)
    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "CREATED_BY_AGENT_ID")
    private Integer createdByAgentId;

    @Size(min = 1, max = 20)
    @Column(name = "WEBSITE_URL")
    private String url;

    @Size(min = 1, max = 50)
    @Column(name = "CORPORATE_LOGO")
    private String logo;

    @Column(name = "CREATION_TIME")
    private Date creationTime;

    @Column(name = "LAST_UPDATION_TIME")
    private Date lastUpdationTime;

    @Column(name = "LAST_UPDATED_BY_AGENT_ID")
    private Integer lastUpdatedByAgentId;

    @Column(name = "STATUS")
    private Integer status;
    
    @Column(name = "BRANCH_ID")
    private String branchId;
    
    @Column(name = "AGENCY_ID")
    private String agencyId;
    
    @Column(name = "POS_OBJECT_ID")
    private String posObjectId ;

    @Column(name = "FACT_OBJECT_ID")
    private String factObjectId;

    @Column(name = "FACT_WS_COMM_FLAG")
    private Integer factCommFlg;

    @Column(name = "FACT_APPROVAL_USER_ID")
    private Integer factApprovalUserId;

    @Column(name = "FACT_APPROVAL_DATE", insertable = true)
    private Date factApprovalDate;

    @Column(name = "TRANSACTION_ID")
    private Long transactionId;
 
    @Column(name = "FACT_APPROVAL_STATUS")
    private Integer factApprovalStatus;
    
    //Added By Pramod for SAT-14018 on 08-08-2018
    @Transient
    int loggedInuserId;

    // @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "corporateModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CorporateTravelCordinator> corporateTravelCordinator = new ArrayList<>();

    // @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "corporateModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CorporateFinanceContact> corporateFinanceContact = new ArrayList<>();

    @Transient
    private String countryName;
    
    @Transient
    private int hiddenId;
    
    @Transient
    private String action = "";
    
    @Transient
    private String oldCorpName = "";
    
    @Transient
    private String oldEmail = "";
    
    public String getOldCorpName() {
		return oldCorpName;
	}

	public void setOldCorpName(String oldCorpName) {
		this.oldCorpName = oldCorpName;
	}

	public String getOldEmail() {
		return oldEmail;
	}

	public void setOldEmail(String oldEmail) {
		this.oldEmail = oldEmail;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	public int getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(int hiddenId) {
		this.hiddenId = hiddenId;
	}

	public Integer getCorpId() {
	return corpId;
    }

    public void setCorpId(Integer corpId) {
	this.corpId = corpId;
    }

    public String getCorporateName() {
	return corporateName;
    }

    public void setCorporateName(String corporateName) {
	this.corporateName = corporateName;
    }

    public Integer getCountryId() {
	return countryId;
    }

    public void setCountryId(Integer countryId) {
	this.countryId = countryId;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile;
    }

    public String getFax() {
	return fax;
    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public Integer getCreatedByAgentId() {
	return createdByAgentId;
    }

    public void setCreatedByAgentId(Integer createdByAgentId) {
	this.createdByAgentId = createdByAgentId;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getLogo() {
	return logo;
    }

    public void setLogo(String logo) {
	this.logo = logo;
    }

    public Date getCreationTime() {
	return creationTime;
    }

    public void setCreationTime(Date creationTime) {
	this.creationTime = creationTime;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    @JsonIgnore
    public List<CorporateFinanceContact> getCorporateFinanceContact() {
	return corporateFinanceContact;
    }

    public void setCorporateFinanceContact(List<CorporateFinanceContact> corporateFinanceContact) {
	this.corporateFinanceContact = corporateFinanceContact;
    }

    @JsonIgnore
    public List<CorporateTravelCordinator> getCorporateTravelCordinator() {
	return corporateTravelCordinator;
    }

    public void setCorporateTravelCordinator(List<CorporateTravelCordinator> corporateTravelCordinator) {
	this.corporateTravelCordinator = corporateTravelCordinator;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    public String getCountryName() {
	return countryName;
    }

    public void setCountryName(String countryName) {
	this.countryName = countryName;
    }

    public String getMobileCode() {
	return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
	this.mobileCode = mobileCode;
    }

    public Date getLastUpdationTime() {
	return lastUpdationTime;
    }

    public void setLastUpdationTime(Date lastUpdationTime) {
	this.lastUpdationTime = lastUpdationTime;
    }

    public Integer getLastUpdatedByAgentId() {
	return lastUpdatedByAgentId;
    }

    public void setLastUpdatedByAgentId(Integer lastUpdatedByAgentId) {
	this.lastUpdatedByAgentId = lastUpdatedByAgentId;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	
	public String getPosObjectId() {
		return posObjectId;
	}

	public void setPosObjectId(String posObjectId) {
		this.posObjectId = posObjectId;
	}

	public String getFactObjectId() {
		return factObjectId;
	}

	public void setFactObjectId(String factObjectId) {
		this.factObjectId = factObjectId;
	}

	public Integer getFactCommFlg() {
		return factCommFlg;
	}

	public void setFactCommFlg(Integer factCommFlg) {
		this.factCommFlg = factCommFlg;
	}

	public Integer getFactApprovalUserId() {
		return factApprovalUserId;
	}

	public void setFactApprovalUserId(Integer factApprovalUserId) {
		this.factApprovalUserId = factApprovalUserId;
	}

	public Date getFactApprovalDate() {
		return factApprovalDate;
	}

	public void setFactApprovalDate(Date factApprovalDate) {
		this.factApprovalDate = factApprovalDate;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getFactApprovalStatus() {
		return factApprovalStatus;
	}

	public void setFactApprovalStatus(Integer factApprovalStatus) {
		this.factApprovalStatus = factApprovalStatus;
	}

	public int getLoggedInuserId() {
		return loggedInuserId;
	}

	public void setLoggedInuserId(int loggedInuserId) {
		this.loggedInuserId = loggedInuserId;
	}
	
	
    
    
}
