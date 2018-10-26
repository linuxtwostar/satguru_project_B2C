package com.tt.ts.rest.common.util;

import java.util.ArrayList;
import java.util.List;

public class OrganizationPccModel {

	private Integer pccCodeType;

	private Integer credentialId;

	private String credentialName;

	private String managerName;

	private String description;
	
	private List<CredentialConfigModel> credentialModal= new ArrayList<>();

	public Integer getPccCodeType() {
		return pccCodeType;
	}

	public void setPccCodeType(Integer pccCodeType) {
		this.pccCodeType = pccCodeType;
	}

	public Integer getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(Integer credentialId) {
		this.credentialId = credentialId;
	}

	public String getCredentialName() {
		return credentialName;
	}

	public void setCredentialName(String credentialName) {
		this.credentialName = credentialName;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<CredentialConfigModel> getCredentialModal() {
		return credentialModal;
	}

	public void setCredentialModal(List<CredentialConfigModel> credentialModal) {
		this.credentialModal = credentialModal;
	}

}
