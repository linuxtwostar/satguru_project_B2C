package com.tt.ts.rest.agent.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.web.multipart.MultipartFile;

import com.tt.nc.group.model.GroupModal;

@Entity
@Table(name="TT_USER")
public class UserModal implements Serializable {

	private static final long serialVersionUID = -4512024255285053267L;

	@NotNull
	@Column(name="USER_ALIAS")
	private String userAlias;
	@NotNull
	@Column(name="PASSWORD")
	private String password;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="USER_ID", unique = true, nullable = false)
	private Integer userId;
	
	@Column(name="TITLE")
	private int title;
	transient private String displayTitle;//TITLE USED TO DISPLAY AT VIEW PAGE.
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="MOBILE_NUMBER")
	private String mobileNo;
	
	@Column(name="MOBILE_NUMBER1")
	private String mobileNo1;
	
	@Column(name="DATE_OF_BIRTH")
	private Date dob;
	
	@Column(name="PROFILE_IMAGE")
	private String profileImage;
    transient MultipartFile uploadProfileImage;
	
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="FAX")
	private String fax;
	
	@Column(name="GENDER")
	private int gender;
	
	@Column(name="MARITAL_STATUS")
	private int marritalStatus;
	
	@Column(name="USER_TYPE")
	private int userType;
	
	@Column(name="APPROVAL_ID")
	private Integer approvalId;
	
	transient private String displayUserType;//USER TYPE TO DISPLAY AT VIEW PAGE.
	
	@Column(name="LAST_LOGIN_DATE")
	private Date lastLoginDate;
	
	@Column(name="CREATION_TIME")
	private Date creationTime;
	
	@Column(name="LAST_MOD_TIME")
	private Date lastModifiedDate;
	
	@Column(name="USER_STATUS")
	private int userStatus;
	
	@Column(name="DISABLE_SIGNIN")
	private int disableSignIn;
	
	@Transient
	private String selectedSite;
	
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private AgentModel agentModel;
	
	/*@OneToMany(mappedBy = "userModal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)*/
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany( mappedBy = "userModal", cascade = CascadeType.ALL)
	private List<AgentCreditLimitModel> agentCreditLimit = new ArrayList<>();
	
	/*@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)*/
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany( mappedBy = "user", cascade = CascadeType.ALL)
	private List<AgentDocumentModel> agentDocumentModel = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.EAGER)
	@ElementCollection(targetClass=GroupModal.class)
	@JoinTable(name = "TT_GROUP_USER_MAPPING", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID") })
	private Set<GroupModal> groupBeanList = new HashSet<GroupModal>();
	
	public String getUserAlias() {
		return userAlias;
	}
	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
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
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getMarritalStatus() {
		return marritalStatus;
	}
	public void setMarritalStatus(int marritalStatus) {
		this.marritalStatus = marritalStatus;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public int getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	

	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
	public int getDisableSignIn() {
		return disableSignIn;
	}
	public void setDisableSignIn(int disableSignIn) {
		this.disableSignIn = disableSignIn;
	}
	
	public String getDisplayUserType() {
		return displayUserType;
	}
	public void setDisplayUserType(String displayUserType) {
		this.displayUserType = displayUserType;
	}
	public String getDisplayTitle() {
		return displayTitle;
	}
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	public MultipartFile getUploadImage() {
		return uploadProfileImage;
	}
	public void setUploadImage(MultipartFile uploadProfileImage) {
		this.uploadProfileImage = uploadProfileImage;
	}
	
	public String getSelectedSite() {
		return selectedSite;
	}
	public void setSelectedSite(String selectedSite) {
		this.selectedSite = selectedSite;
	}
	public String getMobileNo1()
	{
		return mobileNo1;
	}
	public void setMobileNo1(String mobileNo1)
	{
		this.mobileNo1 = mobileNo1;
	}
	public Integer getApprovalId()
	{
		return approvalId;
	}
	public void setApprovalId(Integer approvalId)
	{
		this.approvalId = approvalId;
	}
	@JsonIgnore
	public AgentModel getAgentModel() {
		return agentModel;
	}
	public void setAgentModel(AgentModel agentModel) {
		this.agentModel = agentModel;
	}
	@JsonIgnore
	public List<AgentCreditLimitModel> getAgentCreditLimit() {
		return agentCreditLimit;
	}
	public void setAgentCreditLimit(List<AgentCreditLimitModel> agentCreditLimit) {
		this.agentCreditLimit = agentCreditLimit;
	}
	@JsonIgnore
	public List<AgentDocumentModel> getAgentDocumentModel() {
		return agentDocumentModel;
	}
	public void setAgentDocumentModel(List<AgentDocumentModel> agentDocumentModel) {
		this.agentDocumentModel = agentDocumentModel;
	}
	@JsonIgnore
	public Set<GroupModal> getGroupBeanList() {
		return groupBeanList;
	}
	public void setGroupBeanList(Set<GroupModal> groupBeanList) {
		this.groupBeanList = groupBeanList;
	}
	
}
