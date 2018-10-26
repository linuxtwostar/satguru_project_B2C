package com.tt.ts.rest.corporate.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "TT_CORPORATE_TRAVEL_COORDINATOR")
public class CorporateTravelCordinator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORP_ID")
    private CorporateModel corporateModel;

    @Id
    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "COORDINATOR_NAME")
    private String cordinatorName;

    @NotNull
    @Column(name = "COORDINATOR_EMAIL")
    private String cordinatorEmail;

    @NotNull
    @Column(name = "COORDINATOR_PHONE")
    private String cordinatorMobile;

    @Column(name = "COORDINATOR_PHONE_CODE")
    private String cordinatorMobileCode;

    public Integer getSequence() {
	return sequence;
    }

    public void setSequence(Integer sequence) {
	this.sequence = sequence;
    }

    public String getCordinatorName() {
	return cordinatorName;
    }

    public void setCordinatorName(String cordinatorName) {
	this.cordinatorName = cordinatorName;
    }

    public String getCordinatorEmail() {
	return cordinatorEmail;
    }

    public void setCordinatorEmail(String cordinatorEmail) {
	this.cordinatorEmail = cordinatorEmail;
    }

    public String getCordinatorMobile() {
	return cordinatorMobile;
    }

    public void setCordinatorMobile(String cordinatorMobile) {
	this.cordinatorMobile = cordinatorMobile;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    @JsonIgnore
    public CorporateModel getCorporateModel() {
	return corporateModel;
    }

    public void setCorporateModel(CorporateModel corporateModel) {
	this.corporateModel = corporateModel;
    }

    public String getCordinatorMobileCode() {
	return cordinatorMobileCode;
    }

    public void setCordinatorMobileCode(String cordinatorMobileCode) {
	this.cordinatorMobileCode = cordinatorMobileCode;
    }

}
