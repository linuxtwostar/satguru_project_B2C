package com.tt.ws.rest.hotel.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "TT_SATGURU_HOTEL_RECOMMENDATION")
public class HotelRecommendationModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SATGURU_HOTEL_RECOMMENDATION_ID")
	private Integer hotelRecommendationId;
	
	@Column(name = "SITE_ID")
	private Integer siteId;
	
	@NotEmpty
	@Column(name = "RECOMMENDED_NAME")
	private String recommendationName;
	
	@Column(name = "RECOMMENDATION_DESCRIPTION")
	private String recommendationDescription;
	
	@Column(name = "STATUS")  
	private int status;
	
	@Column(name = "RECOMMENDATION_RATING")
	private Integer recommendationRating;
	
	@Column(name = "CREATION_TIME",updatable=false)  
	private Date creationTime;
	
	@Column(name = "CREATED_BY")  
	private int createdBy;
	
	@Column(name = "LAST_UPDATED_BY")  
	private int lastUpdatedBy;
	
	@Column(name = "LAST_MOD_TIME")  
	private Date lastModTime;
	
	@Column(name = "APPROVAL_USER_ID")
	private Integer approvalUserId ;
	
	@Column(name = "APPROVAL_STATUS")
	private Integer approvalStatus = 0;
	
	@Column(name = "APPROVAL_DATE")
	private Date approvalDate;
	
	@Column(name = "APPROVAL_REMARKS")
	private String approvalRemarks;
	
	@Column(name = "APPROVAL_BY")
	private Integer approvalBy;
	
	private transient String hotelName;
	
	private transient String action;
	
	@Transient
	private String displayRights;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SATGURU_HOTEL_HOTEL_ID")
	private HotelSatguruModel satguruHotelId;
	
	public String getDisplayRights() {
		return displayRights;
	}

	public void setDisplayRights(String displayRights) {
		this.displayRights = displayRights;
	}

	public Integer getHotelRecommendationId()
	{
		return hotelRecommendationId;
	}

	public void setHotelRecommendationId(Integer hotelRecommendationId)
	{
		this.hotelRecommendationId = hotelRecommendationId;
	}

	public Integer getSiteId()
	{
		return siteId;
	}

	public void setSiteId(Integer siteId)
	{
		this.siteId = siteId;
	}

	public String getRecommendationName()
	{
		return recommendationName;
	}

	public void setRecommendationName(String recommendationName)
	{
		this.recommendationName = recommendationName;
	}

	@JsonBackReference
	public HotelSatguruModel getSatguruHotelId() {
		return satguruHotelId;
	}

	public void setSatguruHotelId(HotelSatguruModel satguruHotelId) {
		this.satguruHotelId = satguruHotelId;
	}

	public String getRecommendationDescription()
	{
		return recommendationDescription;
	}

	public void setRecommendationDescription(String recommendationDescription)
	{
		this.recommendationDescription = recommendationDescription;
	}

	

	public Integer getRecommendationRating()
	{
		return recommendationRating;
	}

	public void setRecommendationRating(Integer recommendationRating)
	{
		this.recommendationRating = recommendationRating;
	}

	public Date getCreationTime()
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime)
	{
		this.creationTime = creationTime;
	}

	public int getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	public int getLastUpdatedBy()
	{
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(int lastUpdatedBy)
	{
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastModTime()
	{
		return lastModTime;
	}

	public void setLastModTime(Date lastModTime)
	{
		this.lastModTime = lastModTime;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public String getHotelName()
	{
		return hotelName;
	}

	public void setHotelName(String hotelName)
	{
		this.hotelName = hotelName;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	
}