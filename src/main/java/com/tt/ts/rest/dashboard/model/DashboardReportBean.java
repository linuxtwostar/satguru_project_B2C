package com.tt.ts.rest.dashboard.model;

public class DashboardReportBean {
	private String bookingStatus;
    private String bookingStatusName;
    private String selReportType;
    private String reportFrom;
	private Double reportAmount;
	private Integer agentId;
	private String selRepFlightType;
	private String selRepHotelType;
	private String selRepCarType;
	private String selRepInsuranceType;
	private String agencyCode;
	private String userType;
	
	public String getAgencyCode() {
		return agencyCode;
	}
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getSelRepFlightType() {
		return selRepFlightType;
	}
	public void setSelRepFlightType(String selRepFlightType) {
		this.selRepFlightType = selRepFlightType;
	}
	public String getSelRepHotelType() {
		return selRepHotelType;
	}
	public void setSelRepHotelType(String selRepHotelType) {
		this.selRepHotelType = selRepHotelType;
	}
	public String getSelRepCarType() {
		return selRepCarType;
	}
	public void setSelRepCarType(String selRepCarType) {
		this.selRepCarType = selRepCarType;
	}
	public String getSelRepInsuranceType() {
		return selRepInsuranceType;
	}
	public void setSelRepInsuranceType(String selRepInsuranceType) {
		this.selRepInsuranceType = selRepInsuranceType;
	}
    public Integer getAgentId()
	{
		return agentId;
	}
	public void setAgentId(Integer agentId)
	{
		this.agentId = agentId;
	}
	
    public Double getReportAmount() {
		return reportAmount;
	}
	public void setReportAmount(Double reportAmount) {
		this.reportAmount = reportAmount;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public String getBookingStatusName() {
		return bookingStatusName;
	}
	public void setBookingStatusName(String bookingStatusName) {
		this.bookingStatusName = bookingStatusName;
	}
	public String getSelReportType() {
		return selReportType;
	}
	public void setSelReportType(String selReportType) {
		this.selReportType = selReportType;
	}
	public String getReportFrom() {
		return reportFrom;
	}
	public void setReportFrom(String reportFrom) {
		this.reportFrom = reportFrom;
	}
	
}
