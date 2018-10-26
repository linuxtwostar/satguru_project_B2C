package com.tt.ts.rest.ruleengine.model;

import java.util.ArrayList;
import java.util.List;

public class FlightWidget {
	private String tripType;
	private String noOfChilds;
	private String noOfAdults;
	private String noOfInfants;
	private boolean isDateFlexible;
	private boolean isNonStop;
	private boolean isExcludeLcc;
	private List<String> prefferedAirline;
    private List<String> prefferedAirlineName;
	

	private List<FlightWidgetElement> flightwidgetElement = new ArrayList<FlightWidgetElement>();

	public List<FlightWidgetElement> getFlightwidgetElement() {
		return flightwidgetElement;
	}

	public void setFlightwidgetElement(List<FlightWidgetElement> flightwidgetElement) {
		this.flightwidgetElement = flightwidgetElement;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getNoOfChilds() {
		return noOfChilds;
	}

	public void setNoOfChilds(String noOfChilds) {
		this.noOfChilds = noOfChilds;
	}

	public String getNoOfAdults() {
		return noOfAdults;
	}

	public void setNoOfAdults(String noOfAdults) {
		this.noOfAdults = noOfAdults;
	}

	public String getNoOfInfants() {
		return noOfInfants;
	}

	public void setNoOfInfants(String noOfInfants) {
		this.noOfInfants = noOfInfants;
	}

	public boolean getIsDateFlexible() {
		return isDateFlexible;
	}

	public void setIsDateFlexible(boolean isDateFlexible) {
		this.isDateFlexible = isDateFlexible;
	}

	public boolean getIsNonStop() {
		return isNonStop;
	}

	public void setIsNonStop(boolean isNonStop) {
		this.isNonStop = isNonStop;
	}

	public boolean getIsExcludeLcc() {
		return isExcludeLcc;
	}

	public void setIsExcludeLcc(boolean isExcludeLcc) {
		this.isExcludeLcc = isExcludeLcc;
	}

	public List<String> getPrefferedAirline() {
		return prefferedAirline;
	}

	public void setPrefferedAirline(List<String> prefferedAirline) {
		this.prefferedAirline = prefferedAirline;
	}

	public List<String> getPrefferedAirlineName() {
		return prefferedAirlineName;
	}

	public void setPrefferedAirlineName(List<String> prefferedAirlineName) {
		this.prefferedAirlineName = prefferedAirlineName;
	}

	public void setDateFlexible(boolean isDateFlexible) {
		this.isDateFlexible = isDateFlexible;
	}

	public void setNonStop(boolean isNonStop) {
		this.isNonStop = isNonStop;
	}

	public void setExcludeLcc(boolean isExcludeLcc) {
		this.isExcludeLcc = isExcludeLcc;
	}

}
