package com.tt.ts.rest.ruleengine.model;

import java.util.List;

import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.FlightSearchResponseBean;
import com.ws.services.flight.bean.flightsearch.RoundTripFlightOption;

public class FlightCommonJsonModel {
	private FlightWidget flightWidgetModel;
	private FlightSearchResponseBean flightBean;
	private FlightOption flightOption;
	private RoundTripFlightOption roundTripFlightOption;
	private List<Object> agencyMarkUp;

	private List<FlightOption> onwardFlightOptions;

	private List<RoundTripFlightOption> roundTripFlightOptions;

	private String countryId;
	private String branchId;
	private String agencyId;
	private String corporateId;
	    
    public String getCorporateId()
	{
		return corporateId;
	}

	public void setCorporateId(String corporateId)
	{
		this.corporateId = corporateId;
	}
	
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
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

	public List<RoundTripFlightOption> getRoundTripFlightOptions() {
		return roundTripFlightOptions;
	}

	public void setRoundTripFlightOptions(
			List<RoundTripFlightOption> roundTripFlightOptions) {
		this.roundTripFlightOptions = roundTripFlightOptions;
	}

	public List<FlightOption> getOnwardFlightOptions() {
		return onwardFlightOptions;
	}

	public void setOnwardFlightOptions(List<FlightOption> onwardFlightOptions) {
		this.onwardFlightOptions = onwardFlightOptions;
	}

	public FlightWidget getFlightWidgetModel() {
		return flightWidgetModel;
	}

	public void setFlightWidgetModel(FlightWidget flightWidgetModel) {
		this.flightWidgetModel = flightWidgetModel;
	}

	public FlightSearchResponseBean getFlightBean() {
		return flightBean;
	}

	public void setFlightBean(FlightSearchResponseBean flightBean) {
		this.flightBean = flightBean;
	}

	public FlightOption getFlightOption() {
		return flightOption;
	}

	public void setFlightOption(FlightOption flightOption) {
		this.flightOption = flightOption;
	}

	public RoundTripFlightOption getRoundTripFlightOption() {
		return roundTripFlightOption;
	}

	public void setRoundTripFlightOption(
			RoundTripFlightOption roundTripFlightOption) {
		this.roundTripFlightOption = roundTripFlightOption;
	}

	public List<Object> getAgencyMarkUp() {
		return agencyMarkUp;
	}

	public void setAgencyMarkUp(List<Object> agencyMarkUp) {
		this.agencyMarkUp = agencyMarkUp;
	}

}
