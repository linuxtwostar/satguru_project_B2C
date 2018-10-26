package com.tt.ts.rest.register.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "TT_AGENCY_REGISTER")
public class RegisterModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "REGISTRATION_ID")
	private Integer id ;

	@NotEmpty(message="your name can't be blank")
	@Pattern(regexp="^([\\s\\.]?[a-zA-Z]+)+$", message="Name only alphabets with spaces and single dot are permitted")
	@Column(name = "NAME")
	private String name;

	@NotEmpty(message="Email can't be empty")
	@Pattern(regexp="(^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message=" Please type correct email")
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "LANGUAGE")
	private String language;

	@NotEmpty(message="Contact Number can't be empty")
	@Pattern(regexp="^[0-9\\+\\-\\(\\)]+", message="Please enter numeric values (hash and comma are allowed only once) with maximum length 15")
	@Size(min=10, max=15, message="Length should be between 10 to 15")
	@Column(name = "CONTACT_NO")
	private String contactNumber;

	@Column(name = "COUNTRY_ID")
	private String countryId;

	@Column(name = "SITE_ID")
	private int siteID;

	@Column(name = "STATUS")
	private int status;

	@Column(name = "CREATION_TIME")
	private Date creationTime = new Date();

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Column(name = "LAST_UPDATED_BY")
	private int updatedBy;

	@Column(name = "LAST_MOD_TIME")
	private Date updatedTime = new Date();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

}
