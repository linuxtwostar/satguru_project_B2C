package com.tt.ts.rest.insurance.model;

import java.util.ArrayList;
import java.util.List;

public class InsuranceWidget {
	private String residency;
	private String coverCountry;
	private String startDate;
	private String endDate;
	
	private String airlineCode;
	private String originCity;
	private String destinationCity;
	private int noOfInsuredAdult;
	private int noOfInsureChild;
	private int noOfInsureInfant;
	private int totalInsured;
	private List<Integer> age = new ArrayList<>();
	
	private String originCityName;
	private String destinationCityName;
	
	private List<String> passengerIdList = new ArrayList<>();
	private List<Integer> passengerIdAge = new ArrayList<>();
	
	
	public int getNoOfInsuredAdult() {
		return noOfInsuredAdult;
	}
	public void setNoOfInsuredAdult(int noOfInsuredAdult) {
		this.noOfInsuredAdult = noOfInsuredAdult;
	}
	public String getResidency() {
		return residency;
	}
	public void setResidency(String residency) {
		this.residency = residency;
	}
	public String getCoverCountry() {
		return coverCountry;
	}
	public void setCoverCountry(String coverCountry) {
		this.coverCountry = coverCountry;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getAirlineCode() {
		return airlineCode;
	}
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}
	public String getOriginCity() {
		return originCity;
	}
	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}
	public String getDestinationCity() {
		return destinationCity;
	}
	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}
	public List<Integer> getAge() {
		return age;
	}
	public void setAge(List<Integer> age) {
		this.age = age;
	}
	public String getOriginCityName() {
		return originCityName;
	}
	public void setOriginCityName(String originCityName) {
		this.originCityName = originCityName;
	}
	public String getDestinationCityName() {
		return destinationCityName;
	}
	public void setDestinationCityName(String destinationCityName) {
		this.destinationCityName = destinationCityName;
	}
	public int getNoOfInsureChild()
	{
		return noOfInsureChild;
	}
	public void setNoOfInsureChild(int noOfInsureChild)
	{
		this.noOfInsureChild = noOfInsureChild;
	}
	public int getNoOfInsureInfant()
	{
		return noOfInsureInfant;
	}
	public void setNoOfInsureInfant(int noOfInsureInfant)
	{
		this.noOfInsureInfant = noOfInsureInfant;
	}
	public int getTotalInsured()
	{
		return totalInsured;
	}
	public void setTotalInsured(int totalInsured)
	{
		this.totalInsured = totalInsured;
	}
	public List<String> getPassengerIdList()
	{
		return passengerIdList;
	}
	public void setPassengerIdList(List<String> passengerIdList)
	{
		this.passengerIdList = passengerIdList;
	}
	public List<Integer> getPassengerIdAge()
	{
		return passengerIdAge;
	}
	public void setPassengerIdAge(List<Integer> passengerIdAge)
	{
		this.passengerIdAge = passengerIdAge;
	}
	
}
