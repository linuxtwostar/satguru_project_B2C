package com.tt.ts.rest.ruleengine.model;

public class FlightWidgetElement {
	private String startingFrom;
	private String goingTo;
	private String dateOfJourney;
	private String returnDateOfJourney;
	private String cabinClass;
	private String rbd;
	private Integer destCountryId;
	private Integer originCountryId;

	private String startingFromCity;
	private String goingToCity;
	
	public String getStartingFromCity() {
		return startingFromCity;
	}

	public void setStartingFromCity(String startingFromCity) {
		this.startingFromCity = startingFromCity;
	}

	public String getGoingToCity() {
		return goingToCity;
	}

	public void setGoingToCity(String goingToCity) {
		this.goingToCity = goingToCity;
	}

	public Integer getOriginCountryId() {
		return originCountryId;
	}

	public void setOriginCountryId(Integer originCountryId) {
		this.originCountryId = originCountryId;
	}

	public String getStartingFrom() {
		return startingFrom;
	}

	public void setStartingFrom(String startingFrom) {
		this.startingFrom = startingFrom;
	}

	public String getGoingTo() {
		return goingTo;
	}

	public void setGoingTo(String goingTo) {
		this.goingTo = goingTo;
	}

	public String getDateOfJourney() {
		return dateOfJourney;
	}

	public void setDateOfJourney(String dateOfJourney) {
		this.dateOfJourney = dateOfJourney;
	}

	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	public String getRbd() {
		return rbd;
	}

	public void setRbd(String rbd) {
		this.rbd = rbd;
	}

	public String getReturnDateOfJourney() {
		return returnDateOfJourney;
	}

	public void setReturnDateOfJourney(String returnDateOfJourney) {
		this.returnDateOfJourney = returnDateOfJourney;
	}

	public Integer getDestCountryId() {
		return destCountryId;
	}

	public void setDestCountryId(Integer destCountryId) {
		this.destCountryId = destCountryId;
	}

}
