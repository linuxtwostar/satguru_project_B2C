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
@Table(name = "TT_CORPORATE_FINANCE_CONTACT")
public class CorporateFinanceContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORP_ID")
    private CorporateModel corporateModel;

    @Id
    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @NotNull
    @Column(name = "PHONE")
    private String mobile;

    @Column(name = "PHONE_CODE")
    private String mobileCode;

    public Integer getSequence() {
	return sequence;
    }

    public void setSequence(Integer sequence) {
	this.sequence = sequence;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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

    public String getMobileCode() {
	return mobileCode;
    }

    public void setMobileCode(String mobileCode) {
	this.mobileCode = mobileCode;
    }

}
