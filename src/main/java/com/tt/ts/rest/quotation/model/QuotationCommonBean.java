package com.tt.ts.rest.quotation.model;

import java.util.List;

import com.tt.ts.rest.ruleengine.model.FlightWidget;
import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.RoundTripFlightOption;

public class QuotationCommonBean {

	private String requestBean;
	private String onwardFlightOptionJson;
	private FlightWidget flightWidget;
	private FlightOption flightOption;
	private RoundTripFlightOption roundTripFlightOption;
	private String name;
	private List<String> emailIdList;

	public String getRequestBean() {
		return requestBean;
	}

	public void setRequestBean(String requestBean) {
		this.requestBean = requestBean;
	}

	public String getOnwardFlightOptionJson() {
		return onwardFlightOptionJson;
	}

	public void setOnwardFlightOptionJson(String onwardFlightOptionJson) {
		this.onwardFlightOptionJson = onwardFlightOptionJson;
	}

	public FlightWidget getFlightWidget() {
		return flightWidget;
	}

	public void setFlightWidget(FlightWidget flightWidget) {
		this.flightWidget = flightWidget;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getEmailIdList() {
		return emailIdList;
	}

	public void setEmailIdList(List<String> emailIdList) {
		this.emailIdList = emailIdList;
	}

}
