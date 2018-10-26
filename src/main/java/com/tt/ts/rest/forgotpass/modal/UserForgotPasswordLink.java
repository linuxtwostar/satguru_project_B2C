package com.tt.ts.rest.forgotpass.modal;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TT_TS_FORGOT_PASSWORD_LINK")
public class UserForgotPasswordLink implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "AGENCY_ID")
	private String agencyId;

	@Column(name = "AGENT_ID")
	private Integer agentId;

	@Column(name = "LINK_URL")
	private String linkUrl;

	@Column(name = "LINK_EXPIRY_DATE")
	private Date linkExpDate;

	@Column(name = "LINK_ATTEMPTED_COUNT")
	private Integer linkAttemptedCount;

	@Column(name = "LINK_STATUS")
	private Integer linkStatus;

	@Column(name = "CREATION_TIME")
	private Date creationTime;

	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "LAST_MOD_TIME")
	private Date lastModeTime;

	@Column(name = "LAST_MOD_BY")
	private Integer lastModeBy;

	@Column(name = "LINK_TOKEN")
	private String linkUniqueToken;

	
	
	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Date getLinkExpDate() {
		return linkExpDate;
	}

	public void setLinkExpDate(Date linkExpDate) {
		this.linkExpDate = linkExpDate;
	}

	public Integer getLinkAttemptedCount() {
		return linkAttemptedCount;
	}

	public void setLinkAttemptedCount(Integer linkAttemptedCount) {
		this.linkAttemptedCount = linkAttemptedCount;
	}

	public Integer getLinkStatus() {
		return linkStatus;
	}

	public void setLinkStatus(Integer linkStatus) {
		this.linkStatus = linkStatus;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModeTime() {
		return lastModeTime;
	}

	public void setLastModeTime(Date lastModeTime) {
		this.lastModeTime = lastModeTime;
	}

	public Integer getLastModeBy() {  
		return lastModeBy;
	}

	public void setLastModeBy(Integer lastModeBy) {
		this.lastModeBy = lastModeBy;
	}

	public String getLinkUniqueToken() {
		return linkUniqueToken;
	}

	public void setLinkUniqueToken(String linkUniqueToken) {
		this.linkUniqueToken = linkUniqueToken;
	}
	
	
}
