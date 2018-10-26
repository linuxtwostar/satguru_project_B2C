package com.tt.ts.rest.pax.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.tt.satguruportal.pax.model.PaxDocument;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class,property="@id", scope = PaxModel.class)
@Entity
@Table(name = "TT_TS_PAX")
public class PaxModel implements Serializable {

    private static final long serialVersionUID = 7490138459120534141L;

    @Id
    @GeneratedValue(generator = "SEQUENCE_ID", strategy = GenerationType.TABLE)
    @TableGenerator(name = "SEQUENCE_ID", table = "TT_SEQUENCES", pkColumnName = "SEQUENCE_NAME", valueColumnName = "SEQUENCE_NEXT_HI_VALUE", pkColumnValue = "PAX_UID", allocationSize = 1)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "DOB")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dob;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE1")
    private String phone1;

    @Column(name = "COMPANY_ID")
    private Integer companyId;

    @Column(name = "PASSPORT_NUMBER")
    private String passportNumber;

    @Column(name = "PASSPORT_ISSUE_COUNTRY")
    private Integer passportIssueCountry;

    @Column(name = "MEAL_PREFERENCE")
    private String mealPreference;

    @Column(name = "HOTEL_PREFERENCE")
    private String hotelPreference;

    @Column(name = "SPECIAL_PREFERENCE")
    private String specialPreference;

    @Column(name = "DEP_TIME_PREFERENCE")
    private String depTimePreference;

    @Column(name = "SEAT_PREFERENCE")
    private String seatPreference;

    @Column(name = "CREATION_TIME")
    private Date createdDate;

    @Column(name = "LAST_UPDATION_TIME")
    private Date updateDate;

    @Column(name = "CREATED_BY_AGENT_ID")
    private Integer createdBy;
    
    @Column(name="UPDATED_BY_AGENT_ID")
    private Integer updatedBy;

    @Column(name = "PHONE_CODE")
    private String phoneCode;

    @Column(name = "PHONE_CODE1")
    private String phoneCode1;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "PAX_TYPE")
    private String paxType;

    @Column(name = "PASSPORT_EXPIRY_DATE")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date passportExpiryDate;

    @Column(name = "TITLE")
    private Integer title;

    @Column(name = "GENDER")
    private Integer gender;
    
    @Column(name = "BRANCH_ID")
    private String branchId;
    
    @Column(name = "AGENCY_ID")
    private String agencyId;   

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "paxModel", cascade = CascadeType.ALL)
    private List<PaxAirlinePrefernce> paxAirlinePrefernce = new ArrayList<>();
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "paxModel", cascade = CascadeType.ALL)
    private List<PaxFrequentFlyer> paxFrequentFlyer = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "paxModel", cascade = CascadeType.ALL)
    private List<PaxDocumentModel> paxDocumentModel = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "paxModel", cascade = CascadeType.ALL)
    private List<PaxRelationModel> paxRelationList = new ArrayList<>();

    @Transient
    private String passportIssuedCountryName;

    @Transient
    private String companyName;

    @Transient
    private String companyLocation;

    @Transient
    private Integer mainPaxId;
    
    @Transient
    private String searchDOB;

    @Transient
    private Integer age;

    @Transient
    private String nationalityStr;
    
    @Transient
    private String nationalityCode;

    @Transient
    private boolean fromSearch;

    @Transient
    private String paxIds;

    @Transient
    private String genderName;

    @Transient
    private String titleName;
    
    @Transient
    private boolean isDateFormatChanged;
    
    @Transient
    private Integer pageNumber=1;
    
    @Transient
    private Integer maxRecordPerPage=0;
    
    @Transient
    private String hiddenId;   
    
    @Transient
    private boolean statusUpdateProcess;
    
    @Transient 
    private String oldName;
    
    @Transient 
    private String oldEmail;   
    
    @Transient 
    private String mainPaxName;
    
    @Transient
    private String branchIds;
    
    public String getMainPaxName() {
		return mainPaxName;
	}

	public void setMainPaxName(String mainPaxName) {
		this.mainPaxName = mainPaxName;
	}

	@Transient
    private List<PaxDocument> paxDocument = new ArrayList<>();

    public boolean isStatusUpdateProcess() {
		return statusUpdateProcess;
	}

	public void setStatusUpdateProcess(boolean statusUpdateProcess) {
		this.statusUpdateProcess = statusUpdateProcess;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldEmail() {
		return oldEmail;
	}

	public void setOldEmail(String oldEmail) {
		this.oldEmail = oldEmail;
	}

	public String getHiddenId() {
		return hiddenId;
	}

	public void setHiddenId(String hiddenId) {
		this.hiddenId = hiddenId;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getMaxRecordPerPage() {
		return maxRecordPerPage;
	}

	public void setMaxRecordPerPage(Integer maxRecordPerPage) {
		this.maxRecordPerPage = maxRecordPerPage;
	}

	public String getTitleName() {
	return titleName;
    }

    public void setTitleName(String titleName) {
	this.titleName = titleName;
    }

    public String getGenderName() {
	return genderName;
    }

    public void setGenderName(String genderName) {
	this.genderName = genderName;
    }

    public Integer getTitle() {
	return title;
    }

    public void setTitle(Integer title) {
	this.title = title;
    }

    public Integer getGender() {
	return gender;
    }

    public void setGender(Integer gender) {
	this.gender = gender;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getMiddleName() {
	return middleName;
    }

    public void setMiddleName(String middleName) {
	this.middleName = middleName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public Date getDob() {
	return dob;
    }

    public void setDob(Date dob) {
	this.dob = dob;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getNationality() {
	return nationality;
    }

    public void setNationality(String nationality) {
	this.nationality = nationality;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getPhone1() {
	return phone1;
    }

    public void setPhone1(String phone1) {
	this.phone1 = phone1;
    }

    public String getPassportNumber() {
	return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
	this.passportNumber = passportNumber;
    }

    public String getMealPreference() {
	return mealPreference;
    }

    public void setMealPreference(String mealPreference) {
	this.mealPreference = mealPreference;
    }

    public String getHotelPreference() {
	return hotelPreference;
    }

    public void setHotelPreference(String hotelPreference) {
	this.hotelPreference = hotelPreference;
    }

    public String getSpecialPreference() {
	return specialPreference;
    }

    public void setSpecialPreference(String specialPreference) {
	this.specialPreference = specialPreference;
    }

    public String getDepTimePreference() {
	return depTimePreference;
    }

    public void setDepTimePreference(String depTimePreference) {
	this.depTimePreference = depTimePreference;
    }

    public String getSeatPreference() {
	return seatPreference;
    }

    public void setSeatPreference(String seatPreference) {
	this.seatPreference = seatPreference;
    }

    public Integer getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
	this.createdBy = createdBy;
    }    
    
    public Integer getUpdatedBy() {
	return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
	this.updatedBy = updatedBy;
	}

	public Integer getPassportIssueCountry() {
	return passportIssueCountry;
    }

    public void setPassportIssueCountry(Integer passportIssueCountry) {
	this.passportIssueCountry = passportIssueCountry;
    }

    public Date getCreatedDate() {
	return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public String getPhoneCode() {
	return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
	this.phoneCode = phoneCode;
    }

    public String getPhoneCode1() {
	return phoneCode1;
    }

    public void setPhoneCode1(String phoneCode1) {
	this.phoneCode1 = phoneCode1;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

    @JsonIgnore
    public List<PaxAirlinePrefernce> getPaxAirlinePrefernce() {
	return paxAirlinePrefernce;
    }

    public void setPaxAirlinePrefernce(List<PaxAirlinePrefernce> paxAirlinePrefernce) {
	this.paxAirlinePrefernce = paxAirlinePrefernce;
    }

    @JsonManagedReference
    public List<PaxFrequentFlyer> getPaxFrequentFlyer() {
	return paxFrequentFlyer;
    }

    public void setPaxFrequentFlyer(List<PaxFrequentFlyer> paxFrequentFlyer) {
	this.paxFrequentFlyer = paxFrequentFlyer;
    }

    public String getPaxType() {
	return paxType;
    }

    public void setPaxType(String paxType) {
	this.paxType = paxType;
    }

    public Date getPassportExpiryDate() {
	return passportExpiryDate;
    }

    public void setPassportExpiryDate(Date passportExpiryDate) {
	this.passportExpiryDate = passportExpiryDate;
    }

    public Integer getCompanyId() {
	return companyId;
    }

    public void setCompanyId(Integer companyId) {
	this.companyId = companyId;
    }

    @JsonIgnore
    public List<PaxDocumentModel> getPaxDocumentModel() {
	return paxDocumentModel;
    }

    public void setPaxDocumentModel(List<PaxDocumentModel> paxDocumentModel) {
	this.paxDocumentModel = paxDocumentModel;
    }

    public String getPassportIssuedCountryName() {
	return passportIssuedCountryName;
    }

    public void setPassportIssuedCountryName(String passportIssuedCountryName) {
	this.passportIssuedCountryName = passportIssuedCountryName;
    }

    public String getCompanyName() {
	return companyName;
    }

    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    public String getCompanyLocation() {
	return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
	this.companyLocation = companyLocation;
    }
    
    @JsonManagedReference
    public List<PaxRelationModel> getPaxRelationList() {
	return paxRelationList;
    }

    public void setPaxRelationList(List<PaxRelationModel> paxRelationList) {
	this.paxRelationList = paxRelationList;
    }

    public Integer getMainPaxId() {
	return mainPaxId;
    }

    public void setMainPaxId(Integer mainPaxId) {
	this.mainPaxId = mainPaxId;
    }

    public Integer getAge() {
	return age;
    }

    public void setAge(Integer age) {
	this.age = age;
    }

    public String getNationalityStr() {
	return nationalityStr;
    }

    public void setNationalityStr(String nationalityStr) {
	this.nationalityStr = nationalityStr;
    }

    public boolean getFromSearch() {
	return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
	this.fromSearch = fromSearch;
    }

    public String getPaxIds() {
	return paxIds;
    }

    public void setPaxIds(String paxIds) {
	this.paxIds = paxIds;
    }

	public String getNationalityCode()
	{
		return nationalityCode;
	}

	public void setNationalityCode(String nationalityCode)
	{
		this.nationalityCode = nationalityCode;
	}

	public String getSearchDOB() {
		return searchDOB;
	}

	public void setSearchDOB(String searchDOB) {
		this.searchDOB = searchDOB;
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

	public String getBranchIds() {
		return branchIds;
	}

	public void setBranchIds(String branchIds) {
		this.branchIds = branchIds;
	}	
}
