package com.tt.ts.rest.agent.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "TT_TS_AGENT_STAFF")
public class AgentModel {

    @Transient
    private String firstName;

    @Transient
    private Integer userType;

    @Transient
    private Integer siteId = 1;

    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "USER_ID", unique = true, nullable = false)
    private int id;

    @NotNull
    @Column(name = "CREDIT_LIMIT")
    private String creditLimit;	//weekly = 0, monthly=1, daily = 2

    @Column(name = "STATUS")
    private Integer status;
    
    @Column(name = "ORGANIZATION_ID")
    private Integer orgId;

    @Column(name = "APPROVAL_USER_ID")
    private Integer approvalUserId;

    @Column(name = "APPROVAL_STATUS")
    private Integer approvalStatus;

    @Column(name = "APPROVAL_DATE")
    private Date approvalDate;

    @Column(name = "APPROVAL_REMARKS")
    private String approvalRemarks;

    @Column(name = "APPROVAL_BY")
    private Integer approvalBy;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "JOINING_DATE")
    @DateTimeFormat(pattern = "yyyy-dd-MM")
    private Date joiningDate;

    @Column(name = "PRIMARY_EMAIL")
    private String email;

    @Column(name = "SECONDARY_EMAIL")
    private String email1;

    @Column(name = "PRIMARY_PHONE")
    private String mobileNo;

    @Column(name = "SECONDARY_PHONE")
    private String mobileNo1;

    @Transient
    private Integer sessionUser;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserModal user;

    @Transient
    private String orgCode;
    
    @Transient
    private Integer hiddenId = 0;
    
    public Integer getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(Integer hiddenId) {
		this.hiddenId = hiddenId;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getCreditLimit() {
	return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
	this.creditLimit = creditLimit;
    }

    public String getEmail1() {
	return email1;
    }

    public void setEmail1(String email1) {
	this.email1 = email1;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Integer getApprovalUserId() {
	return approvalUserId;
    }

    public void setApprovalUserId(Integer approvalUserId) {
	this.approvalUserId = approvalUserId;
    }

    public Integer getApprovalStatus() {
	return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
	this.approvalStatus = approvalStatus;
    }

    public Date getApprovalDate() {
	return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
	this.approvalDate = approvalDate;
    }

    public String getApprovalRemarks() {
	return approvalRemarks;
    }

    public void setApprovalRemarks(String approvalRemarks) {
	this.approvalRemarks = approvalRemarks;
    }

    public Integer getApprovalBy() {
	return approvalBy;
    }

    public void setApprovalBy(Integer approvalBy) {
	this.approvalBy = approvalBy;
    }

    public Integer getSessionUser() {
	return sessionUser;
    }

    public void setSessionUser(Integer sessionUser) {
	this.sessionUser = sessionUser;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Integer getUserType() {
	return userType;
    }

    public void setUserType(Integer userType) {
	this.userType = userType;
    }

    public String getMobileNo() {
	return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
	this.mobileNo = mobileNo;
    }

    public Integer getSiteId() {
	return siteId;
    }

    public void setSiteId(Integer siteId) {
	this.siteId = siteId;
    }

    public String getPosition() {
	return position;
    }

    public void setPosition(String position) {
	this.position = position;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public Date getJoiningDate() {
	return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
	this.joiningDate = joiningDate;
    }

    @JsonIgnore
    public UserModal getUser() {
	return user;
    }

    public void setUser(UserModal user) {
	this.user = user;
    }

    public String getMobileNo1() {
	return mobileNo1;
    }

    public void setMobileNo1(String mobileNo1) {
	this.mobileNo1 = mobileNo1;
    }

}
