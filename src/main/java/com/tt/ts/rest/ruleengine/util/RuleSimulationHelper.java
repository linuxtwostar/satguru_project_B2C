package com.tt.ts.rest.ruleengine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import com.tt.nc.common.util.QueryConstant;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.insurance.model.InsuranceWidget;
import com.tt.ts.rest.organization.service.OrganizationService;
import com.tt.ts.rest.ruleengine.service.KieSessionService;
import com.tt.ts.ruleengine.model.airline.RulesCommonBean;
import com.tt.ts.ruleengine.model.airline.RulesFlightBean;
import com.tt.ts.ruleengine.model.insurance.RulesInsuranceBean;
import com.tt.ts.ruleengine.service.RuleInterface;
import com.tt.ts.ruleengine.service.SimulationRuleService;
import com.tt.ts.suppliercredential.modal.SupplierCredentialModal;
import com.tt.ts.suppliercredential.service.SupplierCredentialService;
import com.ws.services.flight.bean.booking.Passenger;
import com.ws.services.flight.bean.flightsearch.FlightCalendarOption;
import com.ws.services.flight.bean.flightsearch.FlightFare;
import com.ws.services.flight.bean.flightsearch.FlightLegs;
import com.ws.services.flight.bean.flightsearch.FlightLegsStop;
import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.FlightSearchRequestBean;
import com.ws.services.flight.bean.flightsearch.OptionSegmentBean;
import com.ws.services.flight.bean.flightsearch.RoundTripFlightOption;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingResponseBean;
import com.ws.services.insurance.bean.product.FProduct;
import com.ws.services.insurance.bean.product.ProductPriceRequestBean;
import com.ws.services.util.CallLog;

public class RuleSimulationHelper {
 
	private RuleSimulationHelper() {

	}

	private static final String ONEWAY = "OneWay";
	private static final String ROUNDTRIP = "RoundTrip";
	private static final String MULTICITY = "MultiCity";
	private static final String RETAIL = "Retail";
	private static final String CORPORATE = "Corporate";
	
	public static FlightOption processT3PriceAndTagFlight(FlightOption flightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService,SupplierCredentialService credentialService,String pccId,ProductService productService) {
		return processT3FlightOption(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId,productService);
	}

	public static RoundTripFlightOption processT3PriceAndTagFlightRoundTrip(RoundTripFlightOption roundTripFlightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId,
			ProductService productService) {
		return processRoundTripT3PriceSameFareJourney(roundTripFlightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId,productService);
	}

	private static RoundTripFlightOption processRoundTripT3PriceSameFareJourney(RoundTripFlightOption roundTripFlightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId,
			ProductService productService) {
		FlightOption onwardFlightOption = roundTripFlightOption.getOnwardFlightOption();
		FlightOption returnFlightOption = roundTripFlightOption.getReturnFlightOption();

		double t3Price = roundTripFlightOption.getTotalJourneyFare();

		RoundTripFlightOption flightOptionUpdated = null;
		
		double adultMarkupPrice = 0;
		double adultServiceChargePrice = 0;
		double adultDiscountPrice = 0;

		double childMarkupPrice = 0;
		double childServiceChargePrice = 0;
		double childDiscountPrice = 0;

		double infantMarkupPrice = 0;
		double infantServiceChargePrice = 0;
		double infantDiscountPrice = 0;

		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		
		flightOptionUpdated = ruleApplyRoundTripOption(roundTripFlightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId,productService);
			
		if (flightOptionUpdated != null) {
			if(adults > 0) {
				adultMarkupPrice = flightOptionUpdated.getAdultMarkupPrice() * adults;
				adultDiscountPrice = flightOptionUpdated.getAdultDiscountPrice() * adults;
				adultServiceChargePrice = flightOptionUpdated.getAdultServiceChargePrice() * adults;			
			}
			if(childs > 0) {
				childMarkupPrice = flightOptionUpdated.getChildMarkupPrice() * childs;
				childDiscountPrice = flightOptionUpdated.getChildDiscountPrice() * childs;
				childServiceChargePrice = flightOptionUpdated.getChildServiceChargePrice() * childs;		
			} 
			if(infants > 0) {
				infantMarkupPrice = flightOptionUpdated.getInfantMarkupPrice() * infants;
				infantDiscountPrice = flightOptionUpdated.getInfantDiscountPrice() * infants;
				infantServiceChargePrice = flightOptionUpdated.getInfantServiceChargePrice() * infants;
			}	
		}
		roundTripFlightOption.setAdultMarkupPrice(adultMarkupPrice);
		roundTripFlightOption.setAdultServiceChargePrice(adultServiceChargePrice);
		roundTripFlightOption.setAdultDiscountPrice(adultDiscountPrice);

		roundTripFlightOption.setChildMarkupPrice(childMarkupPrice);
		roundTripFlightOption.setChildServiceChargePrice(childServiceChargePrice);
		roundTripFlightOption.setChildDiscountPrice(childDiscountPrice);

		roundTripFlightOption.setInfantMarkupPrice(infantMarkupPrice);
		roundTripFlightOption.setInfantServiceChargePrice(infantServiceChargePrice);
		roundTripFlightOption.setInfantDiscountPrice(infantDiscountPrice);

		roundTripFlightOption.setMarkupPrice(adultMarkupPrice + childMarkupPrice + infantMarkupPrice);
		roundTripFlightOption.setServiceChargePrice(adultServiceChargePrice + childServiceChargePrice + infantServiceChargePrice);
		roundTripFlightOption.setDiscountPrice(adultDiscountPrice + childDiscountPrice + infantDiscountPrice);

		t3Price = t3Price + roundTripFlightOption.getMarkupPrice() + roundTripFlightOption.getServiceChargePrice();

		roundTripFlightOption.setT3Price(t3Price);
		onwardFlightOption.getFlightFare().setT3Price(roundTripFlightOption.getT3Price());
		returnFlightOption.getFlightFare().setT3Price(roundTripFlightOption.getT3Price());

		onwardFlightOption.getFlightFare().setMarkupPrice(roundTripFlightOption.getMarkupPrice());
		onwardFlightOption.getFlightFare().setDiscountPrice(roundTripFlightOption.getDiscountPrice());
		onwardFlightOption.getFlightFare().setServiceChargePrice(roundTripFlightOption.getServiceChargePrice());

		onwardFlightOption.getFlightFare().setAdultMarkupPrice(roundTripFlightOption.getAdultMarkupPrice());
		onwardFlightOption.getFlightFare().setAdultDiscountPrice(roundTripFlightOption.getAdultDiscountPrice());
		onwardFlightOption.getFlightFare().setAdultServiceChargePrice(roundTripFlightOption.getAdultServiceChargePrice());
		onwardFlightOption.getFlightFare().setChildMarkupPrice(roundTripFlightOption.getChildMarkupPrice());
		onwardFlightOption.getFlightFare().setChildDiscountPrice(roundTripFlightOption.getChildDiscountPrice());
		onwardFlightOption.getFlightFare().setChildServiceChargePrice(roundTripFlightOption.getChildServiceChargePrice());
		onwardFlightOption.getFlightFare().setInfantMarkupPrice(roundTripFlightOption.getInfantMarkupPrice());
		onwardFlightOption.getFlightFare().setInfantDiscountPrice(roundTripFlightOption.getInfantDiscountPrice());
		onwardFlightOption.getFlightFare().setInfantServiceChargePrice(roundTripFlightOption.getInfantServiceChargePrice());

		returnFlightOption.getFlightFare().setMarkupPrice(roundTripFlightOption.getMarkupPrice());
		returnFlightOption.getFlightFare().setDiscountPrice(roundTripFlightOption.getDiscountPrice());
		returnFlightOption.getFlightFare().setServiceChargePrice(roundTripFlightOption.getServiceChargePrice());

		returnFlightOption.getFlightFare().setAdultMarkupPrice(roundTripFlightOption.getAdultMarkupPrice());
		returnFlightOption.getFlightFare().setAdultDiscountPrice(roundTripFlightOption.getAdultDiscountPrice());
		returnFlightOption.getFlightFare().setAdultServiceChargePrice(roundTripFlightOption.getAdultServiceChargePrice());
		returnFlightOption.getFlightFare().setChildMarkupPrice(roundTripFlightOption.getChildMarkupPrice());
		returnFlightOption.getFlightFare().setChildDiscountPrice(roundTripFlightOption.getChildDiscountPrice());
		returnFlightOption.getFlightFare().setChildServiceChargePrice(roundTripFlightOption.getChildServiceChargePrice());
		returnFlightOption.getFlightFare().setInfantMarkupPrice(roundTripFlightOption.getInfantMarkupPrice());
		returnFlightOption.getFlightFare().setInfantDiscountPrice(roundTripFlightOption.getInfantDiscountPrice());
		returnFlightOption.getFlightFare().setInfantServiceChargePrice(roundTripFlightOption.getInfantServiceChargePrice());

		roundTripFlightOption.setOnwardFlightOption(onwardFlightOption);
		roundTripFlightOption.setReturnFlightOption(returnFlightOption);
		return roundTripFlightOption;
	}

	private static FlightOption processT3FlightOption(FlightOption flightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId,ProductService productService) {
		double t3Price = flightOption.getTotalFare();

		FlightOption flightOptionUpdated = null;

		double adultMarkupPrice = 0;
		double adultServiceChargePrice = 0;
		double adultDiscountPrice = 0;

		double childMarkupPrice = 0;
		double childServiceChargePrice = 0;
		double childDiscountPrice = 0;

		double infantMarkupPrice = 0;
		double infantServiceChargePrice = 0;
		double infantDiscountPrice = 0;

		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		
		flightOptionUpdated = ruleApplyFlightOption(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, ONEWAY ,productService);
		
		if (flightOptionUpdated != null) {
			if(adults > 0) {
				adultMarkupPrice = flightOptionUpdated.getFlightFare().getAdultMarkupPrice() * adults;
				adultDiscountPrice = flightOptionUpdated.getFlightFare().getAdultDiscountPrice() * adults;
				adultServiceChargePrice = flightOptionUpdated.getFlightFare().getAdultServiceChargePrice() * adults;		
			}
			if(childs > 0) {
				childMarkupPrice = flightOptionUpdated.getFlightFare().getChildMarkupPrice() * childs;
				childDiscountPrice = flightOptionUpdated.getFlightFare().getChildDiscountPrice() * childs;
				childServiceChargePrice = flightOptionUpdated.getFlightFare().getChildServiceChargePrice() * childs;
			}
			if(infants > 0) {
				infantMarkupPrice = flightOptionUpdated.getFlightFare().getInfantMarkupPrice() * infants;
				infantDiscountPrice = flightOptionUpdated.getFlightFare().getInfantDiscountPrice() * infants;
				infantServiceChargePrice = flightOptionUpdated.getFlightFare().getInfantServiceChargePrice() * infants;
			}	
		}

		flightOption.getFlightFare().setAdultMarkupPrice(adultMarkupPrice);
		flightOption.getFlightFare().setAdultServiceChargePrice(adultServiceChargePrice);
		flightOption.getFlightFare().setAdultDiscountPrice(adultDiscountPrice);

		flightOption.getFlightFare().setChildMarkupPrice(childMarkupPrice);
		flightOption.getFlightFare().setChildServiceChargePrice(childServiceChargePrice);
		flightOption.getFlightFare().setChildDiscountPrice(childDiscountPrice);

		flightOption.getFlightFare().setInfantMarkupPrice(infantMarkupPrice);
		flightOption.getFlightFare().setInfantServiceChargePrice(infantServiceChargePrice);
		flightOption.getFlightFare().setInfantDiscountPrice(infantDiscountPrice);

		flightOption.getFlightFare().setMarkupPrice(adultMarkupPrice + childMarkupPrice + infantMarkupPrice);
		flightOption.getFlightFare().setServiceChargePrice(adultServiceChargePrice + childServiceChargePrice + infantServiceChargePrice);
		flightOption.getFlightFare().setDiscountPrice(adultDiscountPrice + childDiscountPrice + infantDiscountPrice);

		t3Price = t3Price + flightOption.getFlightFare().getMarkupPrice() + flightOption.getFlightFare().getServiceChargePrice();

		flightOption.getFlightFare().setT3Price(t3Price);
		return flightOption;
	}

	private static RulesFlightBean processRulesFlightBeanRoundTrip(RoundTripFlightOption roundTripFlightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId) {
		FlightOption onwardFlightOption = roundTripFlightOption.getOnwardFlightOption();
		FlightOption returnFlightOption = roundTripFlightOption.getReturnFlightOption();
		boolean conditionFlag = onwardFlightOption != null && onwardFlightOption.getFlightlegs()!=null && !onwardFlightOption.getFlightlegs().isEmpty();
		boolean conditionFlag2 = returnFlightOption != null && returnFlightOption.getFlightlegs()!=null && !returnFlightOption.getFlightlegs().isEmpty();
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		if(roundTripFlightOption.isMultiCarrierForApplyRule()){
			rulesFlightBean.setExcludeMultiCarrier("Yes");
		} else {
			rulesFlightBean.setExcludeMultiCarrier("");
		}
		rulesFlightBean.setAirlineName(onwardFlightOption.getPlatingCarrierName());
		rulesFlightBean.setAirlineCode(onwardFlightOption.getPlatingCarrier());
		
		List<String> airlineType = new ArrayList<>();
		if(onwardFlightOption.getPlatingAirlineType() == 0){
			airlineType.add("LCC");
		} else if(onwardFlightOption.getPlatingAirlineType() == 1){
			airlineType.add("GDS");
		} else {
			airlineType.add("GDS");
			airlineType.add("LCC");
		}
		rulesFlightBean.setAirlineType(airlineType);
		rulesFlightBean.setBaseFare(roundTripFlightOption.getTotalBaseFare());
		List<String> flightNo = new ArrayList<>();
		if(conditionFlag) {
			for(FlightLegs flightLeg : onwardFlightOption.getFlightlegs()) {
				flightNo.add(flightLeg.getFlightNumber().trim());
			}
		}
		if(conditionFlag2) {
			for(FlightLegs flightLeg : returnFlightOption.getFlightlegs()) {
				flightNo.add(flightLeg.getFlightNumber().trim());
			}
		}
		rulesFlightBean.setFlightNo(flightNo);

		rulesFlightBean.setCabinClass(onwardFlightOption.getFlightFare().getCabinClass());
		rulesFlightBean.setDealCode(onwardFlightOption.getFlightFare().getCorporateDealCode());
		
		rulesFlightBean.setItFares("No");
		rulesFlightBean.setImportPnr("No");
		rulesFlightBean.setCrossSellingPath("No");
		
		Integer originCountryId = flightSearchRequestBean.getOriginCountryId()!=null && !flightSearchRequestBean.getOriginCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getOriginCountryId()) : 0;
		Integer destCountryId = flightSearchRequestBean.getDestinationCountryId()!=null && !flightSearchRequestBean.getDestinationCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getDestinationCountryId()) : 0;String originCountry = geoLocationService.getCountryNameById(originCountryId).getResultString();
		String destCountry = geoLocationService.getCountryNameById(destCountryId).getResultString();
		rulesFlightBean.setOriginCountry(originCountry);
		rulesFlightBean.setDestinationCountry(destCountry);
		
		List<String> journeyType = new ArrayList<>();
		if(originCountry!=null && destCountry!=null && originCountry.equalsIgnoreCase(destCountry)){
			journeyType.add("Domestic");
		} else if(originCountry!=null && destCountry!=null && !originCountry.equalsIgnoreCase(destCountry)){
			journeyType.add("International");
		} else {
			journeyType.add("Domestic");
			journeyType.add("International");
		}
		rulesFlightBean.setJourneyType(journeyType);
		
		rulesFlightBean.setOriginCity(onwardFlightOption.getFlightlegs().get(0).getOriginCityName());
		rulesFlightBean.setDestinationCity(onwardFlightOption.getFlightlegs().get(onwardFlightOption.getFlightlegs().size() - 1).getDestinationCityName());

		rulesFlightBean.setOriginAirport(onwardFlightOption.getFlightlegs().get(0).getOrigin());
		rulesFlightBean.setDestinationAirport(onwardFlightOption.getFlightlegs().get(onwardFlightOption.getFlightlegs().size() - 1).getDestination());

		rulesFlightBean.setNoOfPaxAdult(flightSearchRequestBean.getNoOfAdults());
		rulesFlightBean.setNoOfPaxChild(flightSearchRequestBean.getNoOfChilds());
		rulesFlightBean.setNoOfPaxInfant(flightSearchRequestBean.getNoOfInfants());
		rulesFlightBean.setTripType(flightSearchRequestBean.getTripType());
		rulesFlightBean.setSupplier(onwardFlightOption.getServiceVendor());

		String travelStrtDateStr = onwardFlightOption.getFlightlegs().get(0).getDepDate();
		Date travelStrtDate = CommonUtil.convertStringToDateIndianFormat(travelStrtDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelStartDate(travelStrtDate);

		String travelEndDateStr = returnFlightOption.getFlightlegs().get(returnFlightOption.getFlightlegs().size() - 1).getArrDate();
		Date travelEndDate = CommonUtil.convertStringToDateIndianFormat(travelEndDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelEndDate(travelEndDate);

		List<String> blackoutoutboundStrList = new ArrayList<>();
		Date blackoutOutboundToDate = CommonUtil.convertStringToDateIndianFormat(onwardFlightOption.getFlightlegs().get(onwardFlightOption.getFlightlegs().size()-1).getArrDate(), "yyyy-MM-dd");
		List<Date> blackoutoutboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(travelStrtDate, blackoutOutboundToDate);
		if(blackoutoutboundDateList!=null && !blackoutoutboundDateList.isEmpty()) {
			for(Date blackoutOutboundDate : blackoutoutboundDateList) {
				blackoutoutboundStrList.add(CommonUtil.convertDatetoString(blackoutOutboundDate, "yyyy-MM-dd"));
			}
		}
		rulesFlightBean.setBlackoutOutboundDateList(blackoutoutboundStrList);
		
		List<String> blackoutInboundStrList = new ArrayList<>();
		Date blackoutInboundFromDate = CommonUtil.convertStringToDateIndianFormat(returnFlightOption.getFlightlegs().get(0).getDepDate(), "yyyy-MM-dd");
		List<Date> blackoutInboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(blackoutInboundFromDate, travelEndDate);
		if(blackoutInboundDateList!=null && !blackoutInboundDateList.isEmpty()) {
			for(Date blackoutInboundDate : blackoutInboundDateList) {
				blackoutInboundStrList.add(CommonUtil.convertDatetoString(blackoutInboundDate, "yyyy-MM-dd"));
			}
		}
		rulesFlightBean.setBlackoutInboundDateList(blackoutInboundStrList);
		
		int noOfDays = CommonUtil.daysBetweenTwoDate(travelStrtDate, travelEndDate) + 1;

		rulesFlightBean.setNoOfDays(noOfDays);

		String validDay = com.tt.ts.common.util.CommonUtil.changeDateToStrByFormat(travelStrtDate, "EEEE");
		rulesFlightBean.setValidDays(validDay);

		Integer credentialId = 0;
		if(pccId != null && !pccId.isEmpty() && !"null".equalsIgnoreCase(pccId)) {
			credentialId = Integer.parseInt(pccId);
		} else {
			credentialId = onwardFlightOption.getSearchPCCId()!=null && !onwardFlightOption.getSearchPCCId().isEmpty() ? Integer.parseInt(onwardFlightOption.getSearchPCCId()) : 0;
		}

		Map<Integer,String> credentialMap = flightSearchRequestBean.getCredentialMap();
		String credentialName = "";
		if(credentialMap!=null && !credentialMap.isEmpty()) {
			credentialName = credentialMap.get(credentialId);
		}
		rulesFlightBean.setPcc(credentialName);
		
		String nationality = "";
		List<String> passengerIdList = flightSearchRequestBean.getPassengerIdList();
		if (passengerIdList != null && !passengerIdList.isEmpty() && passengerIdList.get(0).split("-").length > 2) {				
			nationality = passengerIdList.get(0).split("-")[2];		
		} 
		rulesFlightBean.setNationality(nationality);
		
		if(flightSearchRequestBean.getAgentId() == null || flightSearchRequestBean.getAgentId().isEmpty()) {
			String customerType = "";
			String corporateName = "";
			boolean condition = flightSearchRequestBean.getCorporateId()!= null && flightSearchRequestBean.getCorporateId() > 0;
				if(condition) {
					customerType = CORPORATE;
					ResultBean resultBean = geoLocationService.fetchObjectById(flightSearchRequestBean.getCorporateId(), CorporateModel.class);
					if(resultBean!=null && !resultBean.isError() && resultBean.getResultObject()!=null) {
						CorporateModel corpModel = (CorporateModel)resultBean.getResultObject();
						corporateName = corpModel.getCorporateName();
					}
				} else {
					customerType = RETAIL;
				}
			 
			rulesFlightBean.setCorporateId(corporateName);
			rulesFlightBean.setCustomerType(customerType);
		}

		String rbd = "";
		for (FlightLegs flightLeg : onwardFlightOption.getFlightlegs()) {
			rbd += flightLeg.getBookingClass() + ",";
		}
		for (FlightLegs flightLeg : returnFlightOption.getFlightlegs()) {
			rbd += flightLeg.getBookingClass() + ",";
		}
		rulesFlightBean.setRbd(rbd.length()>0 ?rbd.substring(0, rbd.length() - 1):rbd);

		rulesFlightBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		rulesFlightBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		if(onwardFlightOption.getFlightFare()!=null && onwardFlightOption.getFlightFare().getCurrency()!=null && onwardFlightOption.getSupplierCurrency()!=null && !onwardFlightOption.getFlightFare().getCurrency().equalsIgnoreCase(onwardFlightOption.getSupplierCurrency())) {
			rulesFlightBean.setForEx("Yes");
		}else {
			rulesFlightBean.setForEx("No");
		}
		
		
		return rulesFlightBean;
	}

	private static RulesFlightBean processRulesFlightBeanMulticity(FlightOption flightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId) {
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		if(flightOption.isMultiCarrierForApplyRule()){
			rulesFlightBean.setExcludeMultiCarrier("Yes");
		} else {
			rulesFlightBean.setExcludeMultiCarrier("");
		}
		rulesFlightBean.setBaseFare(flightOption.getFlightFare().getTotalBaseFare());
		rulesFlightBean.setAirlineName(flightOption.getPlatingCarrierName());
		rulesFlightBean.setAirlineCode(flightOption.getPlatingCarrier());
	
		
		List<String> airlineType = new ArrayList<>();
		if(flightOption.getPlatingAirlineType() == 1){
			airlineType.add("GDS");
		} else if(flightOption.getPlatingAirlineType() == 0){
			airlineType.add("LCC");
		} else {
			airlineType.add("GDS");
			airlineType.add("LCC");
		}
		rulesFlightBean.setAirlineType(airlineType);
		boolean conditionFlag = flightOption.getOptionSegmentBean() != null && !flightOption.getOptionSegmentBean().isEmpty();
		List<String> flightNo = new ArrayList<>();
		if(conditionFlag) {
			for(OptionSegmentBean optSegmentBean : flightOption.getOptionSegmentBean()) {
				boolean flag = optSegmentBean.getFlightlegs() != null && !optSegmentBean.getFlightlegs().isEmpty();
				if(flag) {
					for(FlightLegs flightLeg : optSegmentBean.getFlightlegs()) {
						flightNo.add(flightLeg.getFlightNumber().trim());
					}
				}			
			}	
		}
		
		rulesFlightBean.setFlightNo(flightNo);
		
		rulesFlightBean.setCabinClass(flightOption.getFlightFare().getCabinClass());
		rulesFlightBean.setDealCode(flightOption.getFlightFare().getCorporateDealCode());
		
		rulesFlightBean.setItFares("No");
		rulesFlightBean.setImportPnr("No");
		rulesFlightBean.setCrossSellingPath("No");
		
		Integer originCountryId = flightSearchRequestBean.getOriginCountryId()!=null && !flightSearchRequestBean.getOriginCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getOriginCountryId()) : 0;
		Integer destCountryId = flightSearchRequestBean.getDestinationCountryId()!=null && !flightSearchRequestBean.getDestinationCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getDestinationCountryId()) : 0;
		String originCountry = geoLocationService.getCountryNameById(originCountryId).getResultString();
		String destCountry = geoLocationService.getCountryNameById(destCountryId).getResultString();
		
		rulesFlightBean.setOriginCountry(originCountry);
		rulesFlightBean.setDestinationCountry(destCountry);
		
		List<String> journeyType = new ArrayList<>();
		if(originCountry!=null && destCountry!=null && originCountry.equalsIgnoreCase(destCountry)){
			journeyType.add("Domestic");
		} else if(originCountry!=null && destCountry!=null && !originCountry.equalsIgnoreCase(destCountry)){
			journeyType.add("International");
		} else {
			journeyType.add("Domestic");
			journeyType.add("International");
		}
		rulesFlightBean.setJourneyType(journeyType);
		
		rulesFlightBean.setOriginCity(flightOption.getOptionSegmentBean().get(0).getFlightlegs().get(0).getOriginCityName());
		rulesFlightBean.setDestinationCity(flightOption.getOptionSegmentBean().get(flightOption.getOptionSegmentBean().size() - 1).getFlightlegs()
				.get(flightOption.getOptionSegmentBean().get(flightOption.getOptionSegmentBean().size() - 1).getFlightlegs().size() - 1).getDestinationCityName());

		rulesFlightBean.setOriginAirport(flightOption.getOptionSegmentBean().get(0).getFlightlegs().get(0).getOrigin());
		rulesFlightBean.setDestinationAirport(flightOption.getOptionSegmentBean().get(flightOption.getOptionSegmentBean().size() - 1).getFlightlegs()
				.get(flightOption.getOptionSegmentBean().get(flightOption.getOptionSegmentBean().size() - 1).getFlightlegs().size() - 1).getDestination());

		rulesFlightBean.setNoOfPaxAdult(flightSearchRequestBean.getNoOfAdults());
		rulesFlightBean.setNoOfPaxChild(flightSearchRequestBean.getNoOfChilds());
		rulesFlightBean.setNoOfPaxInfant(flightSearchRequestBean.getNoOfInfants());
		rulesFlightBean.setTripType(flightSearchRequestBean.getTripType());

		rulesFlightBean.setSupplier(flightOption.getServiceVendor());

		String travelStrtDateStr = flightOption.getOptionSegmentBean().get(0).getFlightlegs().get(0).getDepDate();
		Date travelStrtDate = CommonUtil.convertStringToDateIndianFormat(travelStrtDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelStartDate(travelStrtDate);

		String travelEndDateStr = flightOption.getOptionSegmentBean().get(flightOption.getOptionSegmentBean().size() - 1).getFlightlegs()
				.get(flightOption.getOptionSegmentBean().get(flightOption.getOptionSegmentBean().size() - 1).getFlightlegs().size() - 1).getArrDate();
		Date travelEndDate = CommonUtil.convertStringToDateIndianFormat(travelEndDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelEndDate(travelEndDate);

		List<String> blackoutoutboundStrList = new ArrayList<>();
		int noOfDays = 0;
		
		if(flightOption.getOptionSegmentBean() != null && !flightOption.getOptionSegmentBean().isEmpty()) {
			for(OptionSegmentBean optionSegment : flightOption.getOptionSegmentBean()) {
				String deptDateStr = optionSegment.getFlightlegs().get(0).getDepDate();
				Date deptDate = CommonUtil.convertStringToDateIndianFormat(deptDateStr, "yyyy-MM-dd");
				
				String arrDateStr = optionSegment.getFlightlegs().get(optionSegment.getFlightlegs().size() - 1).getArrDate();
				Date arrDate = CommonUtil.convertStringToDateIndianFormat(arrDateStr, "yyyy-MM-dd");
				
				noOfDays+= CommonUtil.daysBetweenTwoDate(deptDate, arrDate) + 1;
				List<Date> blackoutoutboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(deptDate, arrDate);
				if(blackoutoutboundDateList!=null && !blackoutoutboundDateList.isEmpty()) {
					for(Date blackoutOutboundDate : blackoutoutboundDateList) {
						blackoutoutboundStrList.add(CommonUtil.convertDatetoString(blackoutOutboundDate, "yyyy-MM-dd"));
					}
				}
			}
		}
		rulesFlightBean.setBlackoutOutboundDateList(blackoutoutboundStrList);
		
		rulesFlightBean.setNoOfDays(noOfDays);

		String validDay = com.tt.ts.common.util.CommonUtil.changeDateToStrByFormat(travelStrtDate, "EEEE");
		rulesFlightBean.setValidDays(validDay);

		Integer credentialId = 0;
		if(pccId != null && !pccId.isEmpty() && !"null".equalsIgnoreCase(pccId)) {
			credentialId = Integer.parseInt(pccId);
		} else {
			credentialId = flightOption.getSearchPCCId()!=null && !flightOption.getSearchPCCId().isEmpty() ? Integer.parseInt(flightOption.getSearchPCCId()) : 0;
		}

		Map<Integer,String> credentialMap = flightSearchRequestBean.getCredentialMap();
		String credentialName = "";
		if(credentialMap!=null && !credentialMap.isEmpty()) {
			credentialName = credentialMap.get(credentialId);
		}
		rulesFlightBean.setPcc(credentialName);
		
		String nationality = "";
		List<String> passengerIdList = flightSearchRequestBean.getPassengerIdList();
		if (passengerIdList != null && !passengerIdList.isEmpty() && passengerIdList.get(0).split("-").length > 2) {					
			nationality = passengerIdList.get(0).split("-")[2];		
		} 

		rulesFlightBean.setNationality(nationality);
		
		if(flightSearchRequestBean.getAgentId() == null || flightSearchRequestBean.getAgentId().isEmpty()) {
			String customerType = "";
			String corporateName = "";
			boolean condition = flightSearchRequestBean.getCorporateId()!= null && flightSearchRequestBean.getCorporateId() > 0;
				if(condition) {
					customerType = CORPORATE;
					ResultBean resultBean = geoLocationService.fetchObjectById(flightSearchRequestBean.getCorporateId(), CorporateModel.class);
					if(resultBean!=null && !resultBean.isError() && resultBean.getResultObject()!=null) {
						CorporateModel corpModel = (CorporateModel)resultBean.getResultObject();
						corporateName = corpModel.getCorporateName();
					}
				} else {
					customerType = RETAIL;
				}
			 
			rulesFlightBean.setCorporateId(corporateName);
			rulesFlightBean.setCustomerType(customerType);
		}
		String rbd = "";

		for (OptionSegmentBean optionSegment : flightOption.getOptionSegmentBean()) {
			for (FlightLegs flightLeg : optionSegment.getFlightlegs()) {
				rbd += flightLeg.getBookingClass() + ",";
			}
		}

		rulesFlightBean.setRbd(rbd.length()>0 ?rbd.substring(0, rbd.length() - 1):rbd);

		rulesFlightBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		rulesFlightBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		
		if(flightOption.getFlightFare()!=null && flightOption.getFlightFare().getCurrency()!=null && flightOption.getSupplierCurrency()!=null && !flightOption.getFlightFare().getCurrency().equalsIgnoreCase(flightOption.getSupplierCurrency())) {
			rulesFlightBean.setForEx("Yes");
		}else {
			rulesFlightBean.setForEx("No");
		}
		
		return rulesFlightBean;

	}

	@SuppressWarnings("unchecked")
	private static RulesFlightBean processRulesFlightBean(FlightOption flightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId) {

		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		if(flightOption.isMultiCarrierForApplyRule()){
			rulesFlightBean.setExcludeMultiCarrier("Yes");
		} else {
			rulesFlightBean.setExcludeMultiCarrier("");
		}
		rulesFlightBean.setBaseFare(flightOption.getFlightFare().getTotalBaseFare());
		rulesFlightBean.setAirlineName(flightOption.getPlatingCarrierName());
		rulesFlightBean.setAirlineCode(flightOption.getPlatingCarrier());
		
		List<String> airlineType = new ArrayList<>();
		if(flightOption.getPlatingAirlineType() == 1){
			airlineType.add("GDS");
		} else if(flightOption.getPlatingAirlineType() == 0){
			airlineType.add("LCC");
		} else {
			airlineType.add("GDS");
			airlineType.add("LCC");
		}
		rulesFlightBean.setAirlineType(airlineType);
		boolean conditionFlag = flightOption.getFlightlegs() != null && !flightOption.getFlightlegs().isEmpty();
		List<String> flightNo = new ArrayList<>();
		
		if(conditionFlag) {
			for(FlightLegs flightLeg : flightOption.getFlightlegs()) {
				flightNo.add(flightLeg.getFlightNumber().trim());
			}
		}
		rulesFlightBean.setFlightNo(flightNo);
		rulesFlightBean.setCabinClass(flightOption.getFlightFare().getCabinClass());
		rulesFlightBean.setDealCode(flightOption.getFlightFare().getCorporateDealCode());
		
		rulesFlightBean.setItFares("No");
		rulesFlightBean.setImportPnr("No");
		rulesFlightBean.setCrossSellingPath("No");
		
		Integer originCountryId = flightSearchRequestBean.getOriginCountryId()!=null && !flightSearchRequestBean.getOriginCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getOriginCountryId()) : 0;
		Integer destCountryId = flightSearchRequestBean.getDestinationCountryId()!=null && !flightSearchRequestBean.getDestinationCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getDestinationCountryId()) : 0;String originCountry = geoLocationService.getCountryNameById(originCountryId).getResultString();
		String destCountry = geoLocationService.getCountryNameById(destCountryId).getResultString();
		

		rulesFlightBean.setOriginCountry(originCountry);
		rulesFlightBean.setDestinationCountry(destCountry);
		
		List<String> journeyType = new ArrayList<>();
		if(originCountry!=null && destCountry!=null && originCountry.equalsIgnoreCase(destCountry)){
			journeyType.add("Domestic");
		} else if(originCountry!=null && destCountry!=null && !originCountry.equalsIgnoreCase(destCountry)){
			journeyType.add("International");
		} else {
			journeyType.add("Domestic");
			journeyType.add("International");
		}
		rulesFlightBean.setJourneyType(journeyType);
		
		rulesFlightBean.setOriginCity(flightOption.getFlightlegs().get(0).getOriginCityName());
		rulesFlightBean.setDestinationCity(flightOption.getFlightlegs().get(flightOption.getFlightlegs().size() - 1).getDestinationCityName());

		rulesFlightBean.setOriginAirport(flightOption.getFlightlegs().get(0).getOrigin());
		rulesFlightBean.setDestinationAirport(flightOption.getFlightlegs().get(flightOption.getFlightlegs().size() - 1).getDestination());

		rulesFlightBean.setNoOfPaxAdult(flightSearchRequestBean.getNoOfAdults());
		rulesFlightBean.setNoOfPaxChild(flightSearchRequestBean.getNoOfChilds());
		rulesFlightBean.setNoOfPaxInfant(flightSearchRequestBean.getNoOfInfants());

		rulesFlightBean.setTripType(flightSearchRequestBean.getTripType());
		rulesFlightBean.setSupplier(flightOption.getServiceVendor());

		String travelStrtDateStr = flightOption.getFlightlegs().get(0).getDepDate();
		Date travelStrtDate = CommonUtil.convertStringToDateIndianFormat(travelStrtDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelStartDate(travelStrtDate);

		String travelEndDateStr = flightOption.getFlightlegs().get(flightOption.getFlightlegs().size() - 1).getArrDate();
		Date travelEndDate = CommonUtil.convertStringToDateIndianFormat(travelEndDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelEndDate(travelEndDate);

		List<String> blackoutoutboundStrList = new ArrayList<>();
		Date blackoutOutboundToDate = CommonUtil.convertStringToDateIndianFormat(flightOption.getFlightlegs().get(flightOption.getFlightlegs().size()-1).getArrDate(), "yyyy-MM-dd");
		List<Date> blackoutoutboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(travelStrtDate, blackoutOutboundToDate);
		if(blackoutoutboundDateList!=null && !blackoutoutboundDateList.isEmpty()) {
			for(Date blackoutOutboundDate : blackoutoutboundDateList) {
				blackoutoutboundStrList.add(CommonUtil.convertDatetoString(blackoutOutboundDate, "yyyy-MM-dd"));
			}
		}
		rulesFlightBean.setBlackoutOutboundDateList(blackoutoutboundStrList);
		
		int noOfDays = CommonUtil.daysBetweenTwoDate(travelStrtDate, travelEndDate) + 1;
		
		rulesFlightBean.setNoOfDays(noOfDays);

		String validDay = com.tt.ts.common.util.CommonUtil.changeDateToStrByFormat(travelStrtDate, "EEEE");
		rulesFlightBean.setValidDays(validDay);

		Integer credentialId = 0;
		if(pccId != null && !pccId.isEmpty() && !"null".equalsIgnoreCase(pccId)) {
			credentialId = Integer.parseInt(pccId);
		} else {
			credentialId = flightOption.getSearchPCCId()!=null && !flightOption.getSearchPCCId().isEmpty() ? Integer.parseInt(flightOption.getSearchPCCId()) : 0;
		}

		Map<Integer,String> credentialMap = flightSearchRequestBean.getCredentialMap();
		String credentialName = "";
		if(credentialMap!=null && !credentialMap.isEmpty()) {
			credentialName = credentialMap.get(credentialId);
		}
		rulesFlightBean.setPcc(credentialName);
		String nationality = "";
		List<String> passengerIdList = flightSearchRequestBean.getPassengerIdList();
		if (passengerIdList != null && !passengerIdList.isEmpty() && passengerIdList.get(0).split("-").length > 2) {					
			nationality = passengerIdList.get(0).split("-")[2];		
		} 

		rulesFlightBean.setNationality(nationality);

		if(flightSearchRequestBean.getAgentId() == null || flightSearchRequestBean.getAgentId().isEmpty()) {
			String customerType = "";
			String corporateName = "";
			boolean condition = flightSearchRequestBean.getCorporateId()!= null && flightSearchRequestBean.getCorporateId() > 0;
				if(condition) {
					customerType = CORPORATE;
					ResultBean resultBean = geoLocationService.fetchObjectById(flightSearchRequestBean.getCorporateId(), CorporateModel.class);
					if(resultBean!=null && !resultBean.isError() && resultBean.getResultObject()!=null) {
						CorporateModel corpModel = (CorporateModel)resultBean.getResultObject();
						corporateName = corpModel.getCorporateName();
					}
				} else {
					customerType = RETAIL;
				}
			 
			rulesFlightBean.setCorporateId(corporateName);
			rulesFlightBean.setCustomerType(customerType);
		}
		
		String rbd = "";
		for (FlightLegs flightLeg : flightOption.getFlightlegs()) {
			rbd += flightLeg.getBookingClass() + ",";
		}

		rulesFlightBean.setRbd(rbd.length()>0 ?rbd.substring(0, rbd.length() - 1):rbd);

		Date bookingDate = new Date();
		
		rulesFlightBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
		rulesFlightBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
		
		if(flightOption.getFlightFare()!=null && flightOption.getFlightFare().getCurrency()!=null && flightOption.getSupplierCurrency()!=null && !flightOption.getFlightFare().getCurrency().equalsIgnoreCase(flightOption.getSupplierCurrency())) {
			rulesFlightBean.setForEx("Yes");
		}else {
			rulesFlightBean.setForEx("No");
		}
		
		return rulesFlightBean;
	}
	
	@SuppressWarnings("unchecked")
	private static RulesFlightBean processRulesFlightBeanCalender(FlightCalendarOption flightOpt, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals) {

		FlightCalendarOption flightOption = setAirlineAirportDataFlightCal(flightOpt,resultBeanAirline,airportModals);
		boolean flagLeg = flightOption.getFlightlegs() != null && !flightOption.getFlightlegs().isEmpty();
		boolean flagFare = flightOption.getFlightFare()!=null;
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		rulesFlightBean.setAirlineName(flightOption.getPlatingCarrierName());
		rulesFlightBean.setAirlineCode(flightOption.getPlatingCarrier());
		List<String> journeyType = new ArrayList<>();
		if(flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(flightSearchRequestBean.getDestinationCountryId())){
			journeyType.add("Domestic");
		} else if(!flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(flightSearchRequestBean.getDestinationCountryId())){
			journeyType.add("International");
		} else {
			journeyType.add("Domestic");
			journeyType.add("International");
		}
		rulesFlightBean.setJourneyType(journeyType);
		
		List<String> airlineType = new ArrayList<>();
		if(flightOption.getPlatingAirlineType() == 1){
			airlineType.add("GDS");
		} else if(flightOption.getPlatingAirlineType() == 0){
			airlineType.add("LCC");
		} else {
			airlineType.add("GDS");
			airlineType.add("LCC");
		}
		rulesFlightBean.setAirlineType(airlineType);
		List<String> flightNo = new ArrayList<>();
		
		rulesFlightBean.setFlightNo(flightNo);
		if(flightOpt.isMultiCarrierForApplyRule()) {
			rulesFlightBean.setExcludeMultiCarrier("Yes");
		} else {
			rulesFlightBean.setExcludeMultiCarrier("");
		}
		rulesFlightBean.setCabinClass(flagFare ? flightOption.getFlightFare().getCabinClass() :"");
		rulesFlightBean.setDealCode(flagFare ? flightOption.getFlightFare().getCorporateDealCode() :"");
		
		rulesFlightBean.setItFares("No");
		rulesFlightBean.setImportPnr("No");
		rulesFlightBean.setCrossSellingPath("No");
		
		rulesFlightBean.setBaseFare(flagFare?flightOption.getFlightFare().getTotalBaseFare():0);
		Integer originCountryId = flightSearchRequestBean.getOriginCountryId()!=null && !flightSearchRequestBean.getOriginCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getOriginCountryId()) : 0;
		Integer destCountryId = flightSearchRequestBean.getDestinationCountryId()!=null && !flightSearchRequestBean.getDestinationCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getDestinationCountryId()) : 0;String originCountry = geoLocationService.getCountryNameById(originCountryId).getResultString();
		String destCountry = geoLocationService.getCountryNameById(destCountryId).getResultString();
		

		rulesFlightBean.setOriginCountry(originCountry);
		rulesFlightBean.setDestinationCountry(destCountry);
		rulesFlightBean.setOriginCity(flagLeg ? flightOption.getFlightlegs().get(0).getOriginCityName() :"");
		rulesFlightBean.setDestinationCity(flagLeg ? flightOption.getFlightlegs().get(flightOption.getFlightlegs().size() - 1).getDestinationCityName() : "");

		rulesFlightBean.setOriginAirport(flagLeg ? flightOption.getFlightlegs().get(0).getOrigin() :"");
		rulesFlightBean.setDestinationAirport(flagLeg ? flightOption.getFlightlegs().get(flightOption.getFlightlegs().size() - 1).getDestination() : "");

		rulesFlightBean.setNoOfPaxAdult(flightSearchRequestBean.getNoOfAdults());
		rulesFlightBean.setNoOfPaxChild(flightSearchRequestBean.getNoOfChilds());
		rulesFlightBean.setNoOfPaxInfant(flightSearchRequestBean.getNoOfInfants());

		rulesFlightBean.setTripType(flightSearchRequestBean.getTripType());
		rulesFlightBean.setSupplier(flightOption.getServiceVendor());
		Date travelStrtDate;
		Date travelEndDate;
		if(flagLeg) {
			String travelStrtDateStr = flightOption.getFlightlegs().get(0).getDepDate();
			travelStrtDate = CommonUtil.convertStringToDateIndianFormat(travelStrtDateStr, "yyyy-MM-dd");
			
			String travelEndDateStr = flightOption.getFlightlegs().get(flightOption.getFlightlegs().size() - 1).getArrDate();
			travelEndDate = CommonUtil.convertStringToDateIndianFormat(travelEndDateStr, "yyyy-MM-dd");
			
			List<String> blackoutoutboundStrList = new ArrayList<>();
			Date blackoutOutboundToDate = CommonUtil.convertStringToDateIndianFormat(flightOption.getFlightlegs().get(flightOption.getFlightlegs().size()-1).getArrDate(), "yyyy-MM-dd");
			List<Date> blackoutoutboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(travelStrtDate, blackoutOutboundToDate);
			if(blackoutoutboundDateList!=null && !blackoutoutboundDateList.isEmpty()) {
				for(Date blackoutOutboundDate : blackoutoutboundDateList) {
					blackoutoutboundStrList.add(CommonUtil.convertDatetoString(blackoutOutboundDate, "yyyy-MM-dd"));
				}
			}
			rulesFlightBean.setBlackoutOutboundDateList(blackoutoutboundStrList);
			
		} else {
			travelStrtDate = new Date();
			travelEndDate = new Date();
		}
		
		rulesFlightBean.setTravelStartDate(travelStrtDate);
		rulesFlightBean.setTravelEndDate(travelEndDate);
		int noOfDays = CommonUtil.daysBetweenTwoDate(travelStrtDate, travelEndDate) + 1;
		
		rulesFlightBean.setNoOfDays(noOfDays);

		String validDay = com.tt.ts.common.util.CommonUtil.changeDateToStrByFormat(travelStrtDate, "EEEE");
		rulesFlightBean.setValidDays(validDay);

		Integer credentialId = flightOption.getPccId()!=null && !flightOption.getPccId().isEmpty() ? Integer.parseInt(flightOption.getPccId()) : 0;
		
		Map<Integer,String> credentialMap = flightSearchRequestBean.getCredentialMap();
		String credentialName = "";
		if(credentialMap!=null && !credentialMap.isEmpty()) {
			credentialName = credentialMap.get(credentialId);
		}
		rulesFlightBean.setPcc(credentialName);

		String nationality = "";
		List<String> passengerIdList = flightSearchRequestBean.getPassengerIdList();
		if (passengerIdList != null && !passengerIdList.isEmpty() && passengerIdList.get(0).split("-").length > 2) {					
			nationality = passengerIdList.get(0).split("-")[2];		
		} 

		rulesFlightBean.setNationality(nationality);

		if(flightSearchRequestBean.getAgentId() == null || flightSearchRequestBean.getAgentId().isEmpty()) {
			String customerType = "";
			String corporateName = "";
			boolean condition = flightSearchRequestBean.getCorporateId()!= null && flightSearchRequestBean.getCorporateId() > 0;
				if(condition) {
					customerType = CORPORATE;
					ResultBean resultBean = geoLocationService.fetchObjectById(flightSearchRequestBean.getCorporateId(), CorporateModel.class);
					if(resultBean!=null && !resultBean.isError() && resultBean.getResultObject()!=null) {
						CorporateModel corpModel = (CorporateModel)resultBean.getResultObject();
						corporateName = corpModel.getCorporateName();
					}
				} else {
					customerType = RETAIL;
				}
			 
			rulesFlightBean.setCorporateId(corporateName);
			rulesFlightBean.setCustomerType(customerType);
		}
		
		String rbd = "";
		if(flagLeg) {
			for (FlightLegs flightLeg : flightOption.getFlightlegs()) {
				rbd += flightLeg.getBookingClass() + ",";
			}

		}
		
		rulesFlightBean.setRbd(rbd.length()>0 ?rbd.substring(0, rbd.length() - 1):rbd);
		rulesFlightBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		rulesFlightBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		
		if(flightOption.getFlightFare()!=null && flightOption.getFlightFare().getCurrency()!=null && flightOption.getSupplierCurrency()!=null && !flightOption.getFlightFare().getCurrency().equalsIgnoreCase(flightOption.getSupplierCurrency())) {
			rulesFlightBean.setForEx("Yes");
		}else {
			rulesFlightBean.setForEx("No");
		}
		
		return rulesFlightBean;
	}
	
	private static RulesFlightBean processRulesFlightBeanRoundTripCalender(FlightCalendarOption flightCalenderOpt, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals) {
		FlightCalendarOption flightCalenderOption = setAirlineAirportDataFlightCal(flightCalenderOpt,resultBeanAirline,airportModals);
		List<FlightLegs> onwardFlightLegs = flightCalenderOption.getFlightlegs();
		List<FlightLegs> returnFlightLegs = flightCalenderOption.getReturnFlightlegs();
		
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		boolean conditionFlag = onwardFlightLegs != null && !onwardFlightLegs.isEmpty();
		boolean conditionFlag2 = returnFlightLegs != null && !returnFlightLegs.isEmpty();
		rulesFlightBean.setAirlineName(flightCalenderOption.getPlatingCarrierName());
		rulesFlightBean.setAirlineCode(flightCalenderOption.getPlatingCarrier());
	
		List<String> journeyType = new ArrayList<>();
		if(flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(flightSearchRequestBean.getDestinationCountryId())){
			journeyType.add("Domestic");
		} else if(!flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(flightSearchRequestBean.getDestinationCountryId())){
			journeyType.add("International");
		} else {
			journeyType.add("Domestic");
			journeyType.add("International");
		}
		rulesFlightBean.setJourneyType(journeyType);
		
		List<String> airlineType = new ArrayList<>();
		if(flightCalenderOption.getPlatingAirlineType() == 1){
			airlineType.add("GDS");
		} else if(flightCalenderOption.getPlatingAirlineType() == 0){
			airlineType.add("LCC");
		} else {
			airlineType.add("GDS");
			airlineType.add("LCC");
		}
		rulesFlightBean.setAirlineType(airlineType);
		
		List<String> flightNo = new ArrayList<>();
		
		rulesFlightBean.setFlightNo(flightNo);
		if(flightCalenderOpt.isMultiCarrierForApplyRule()) {
			rulesFlightBean.setExcludeMultiCarrier("Yes");
		} else {
			rulesFlightBean.setExcludeMultiCarrier("");
		}
		rulesFlightBean.setCabinClass(flightCalenderOption.getFlightFare()!= null ? flightCalenderOption.getFlightFare().getCabinClass():"");
		rulesFlightBean.setDealCode(flightCalenderOption.getFlightFare()!= null ? flightCalenderOption.getFlightFare().getCorporateDealCode():"");
		
		rulesFlightBean.setItFares("No");
		rulesFlightBean.setImportPnr("No");
		rulesFlightBean.setCrossSellingPath("No");
		
		rulesFlightBean.setBaseFare(flightCalenderOption.getFlightFare()!= null ? flightCalenderOption.getFlightFare().getTotalBaseFare() : 0);
		Integer originCountryId = flightSearchRequestBean.getOriginCountryId()!=null && !flightSearchRequestBean.getOriginCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getOriginCountryId()) : 0;
		Integer destCountryId = flightSearchRequestBean.getDestinationCountryId()!=null && !flightSearchRequestBean.getDestinationCountryId().isEmpty() ?Integer.parseInt(flightSearchRequestBean.getDestinationCountryId()) : 0;String originCountry = geoLocationService.getCountryNameById(originCountryId).getResultString();
		String destCountry = geoLocationService.getCountryNameById(destCountryId).getResultString();
		rulesFlightBean.setOriginCountry(originCountry);
		rulesFlightBean.setDestinationCountry(destCountry);
		
		rulesFlightBean.setOriginCity(conditionFlag?onwardFlightLegs.get(0).getOriginCityName():"");
		rulesFlightBean.setDestinationCity(conditionFlag?onwardFlightLegs.get(onwardFlightLegs.size() - 1).getDestinationCityName():"");

		rulesFlightBean.setOriginAirport(conditionFlag?onwardFlightLegs.get(0).getOrigin():"");
		rulesFlightBean.setDestinationAirport(conditionFlag?onwardFlightLegs.get(onwardFlightLegs.size() - 1).getDestination():"");

		rulesFlightBean.setNoOfPaxAdult(flightSearchRequestBean.getNoOfAdults());
		rulesFlightBean.setNoOfPaxChild(flightSearchRequestBean.getNoOfChilds());
		rulesFlightBean.setNoOfPaxInfant(flightSearchRequestBean.getNoOfInfants());
		rulesFlightBean.setTripType(flightSearchRequestBean.getTripType());
		rulesFlightBean.setSupplier(flightCalenderOption.getServiceVendor());

		Date travelStrtDate;
		if(conditionFlag) {
			String travelStrtDateStr = onwardFlightLegs.get(0).getDepDate();
			travelStrtDate = CommonUtil.convertStringToDateIndianFormat(travelStrtDateStr, "yyyy-MM-dd");
			List<String> blackoutoutboundStrList = new ArrayList<>();
			Date blackoutOutboundToDate = CommonUtil.convertStringToDateIndianFormat(onwardFlightLegs.get(onwardFlightLegs.size()-1).getArrDate(), "yyyy-MM-dd");
			List<Date> blackoutoutboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(travelStrtDate, blackoutOutboundToDate);
			if(blackoutoutboundDateList!=null && !blackoutoutboundDateList.isEmpty()) {
				for(Date blackoutOutboundDate : blackoutoutboundDateList) {
					blackoutoutboundStrList.add(CommonUtil.convertDatetoString(blackoutOutboundDate, "yyyy-MM-dd"));
				}
			}
			rulesFlightBean.setBlackoutOutboundDateList(blackoutoutboundStrList);
		} else {
			travelStrtDate = new Date();
			
		}
		rulesFlightBean.setTravelStartDate(travelStrtDate);
		Date travelEndDate;
		if(conditionFlag2) {
			String travelEndDateStr = returnFlightLegs.get(returnFlightLegs.size() - 1).getArrDate();
			travelEndDate = CommonUtil.convertStringToDateIndianFormat(travelEndDateStr, "yyyy-MM-dd");	
			List<String> blackoutInboundStrList = new ArrayList<>();
			Date blackoutInboundFromDate = CommonUtil.convertStringToDateIndianFormat(returnFlightLegs.get(0).getDepDate(), "yyyy-MM-dd");
			List<Date> blackoutInboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(blackoutInboundFromDate, travelEndDate);
			if(blackoutInboundDateList!=null && !blackoutInboundDateList.isEmpty()) {
				for(Date blackoutInboundDate : blackoutInboundDateList) {
					blackoutInboundStrList.add(CommonUtil.convertDatetoString(blackoutInboundDate, "yyyy-MM-dd"));
				}
			}
			rulesFlightBean.setBlackoutInboundDateList(blackoutInboundStrList);
		} else {
			travelEndDate = new Date();
		}
		rulesFlightBean.setTravelEndDate(travelEndDate);
		
		int noOfDays = CommonUtil.daysBetweenTwoDate(travelStrtDate, travelEndDate) + 1;

		rulesFlightBean.setNoOfDays(noOfDays);

		String validDay = com.tt.ts.common.util.CommonUtil.changeDateToStrByFormat(travelStrtDate, "EEEE");
		rulesFlightBean.setValidDays(validDay);

		Integer credentialId = flightCalenderOption.getPccId()!=null && !flightCalenderOption.getPccId().isEmpty() ? Integer.parseInt(flightCalenderOption.getPccId()) : 0;
		
		Map<Integer,String> credentialMap = flightSearchRequestBean.getCredentialMap();
		String credentialName = "";
		if(credentialMap!=null && !credentialMap.isEmpty()) {
			credentialName = credentialMap.get(credentialId);
		}
		rulesFlightBean.setPcc(credentialName);

		String nationality = "";
		List<String> passengerIdList = flightSearchRequestBean.getPassengerIdList();
		if (passengerIdList != null && !passengerIdList.isEmpty() && passengerIdList.get(0).split("-").length > 2) {					
			nationality = passengerIdList.get(0).split("-")[2];		
		} 

		rulesFlightBean.setNationality(nationality);

		if(flightSearchRequestBean.getAgentId() == null || flightSearchRequestBean.getAgentId().isEmpty()) {
			String customerType = "";
			String corporateName = "";
			boolean condition = flightSearchRequestBean.getCorporateId()!= null && flightSearchRequestBean.getCorporateId() > 0;
				if(condition) {
					customerType = CORPORATE;
					ResultBean resultBean = geoLocationService.fetchObjectById(flightSearchRequestBean.getCorporateId(), CorporateModel.class);
					if(resultBean!=null && !resultBean.isError() && resultBean.getResultObject()!=null) {
						CorporateModel corpModel = (CorporateModel)resultBean.getResultObject();
						corporateName = corpModel.getCorporateName();
					}
				} else {
					customerType = RETAIL;
				}
			 
			rulesFlightBean.setCorporateId(corporateName);
			rulesFlightBean.setCustomerType(customerType);
		}
		
		String rbd = "";
		if(conditionFlag) {
			for (FlightLegs flightLeg : onwardFlightLegs) {
				rbd += flightLeg.getBookingClass() + ",";
			}
		}
		if(conditionFlag2) {
			for (FlightLegs flightLeg : returnFlightLegs) {
				rbd += flightLeg.getBookingClass() + ",";
			}
		}
		
		rulesFlightBean.setRbd(rbd.length() > 0 ? rbd.substring(0, rbd.length() - 1) : rbd);

		rulesFlightBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		rulesFlightBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		
		if(flightCalenderOption.getFlightFare()!=null && flightCalenderOption.getFlightFare().getCurrency()!=null && flightCalenderOption.getSupplierCurrency()!=null && !flightCalenderOption.getFlightFare().getCurrency().equalsIgnoreCase(flightCalenderOption.getSupplierCurrency())) {
			rulesFlightBean.setForEx("Yes");
		}else {
			rulesFlightBean.setForEx("No");
		}
		return rulesFlightBean;
	}
	
	private static FlightOption ruleApplyFlightOption(FlightOption flightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId,
			String tripType,ProductService productService) {

		RulesCommonBean rulesCommonBean = new RulesCommonBean();
		
		//rulesCommonBean.setCountryId(flightSearchRequestBean.getCountryId());
		if(flightSearchRequestBean.getAgentId()!=null && !flightSearchRequestBean.getAgentId().isEmpty()) {
			rulesCommonBean.setAgencyId(flightSearchRequestBean.getAgentId());
		} else {
			rulesCommonBean.setBranchId(flightSearchRequestBean.getBranchId());
		}
		
		rulesCommonBean.setTicketingPeriod(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		if (MULTICITY.equals(tripType)) {
			rulesFlightBean = processRulesFlightBeanMulticity(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId);
		} else if (ONEWAY.equals(tripType)) {
			rulesFlightBean = processRulesFlightBean(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId);
		}

		rulesCommonBean.setRulesFlightBean(rulesFlightBean);

		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		
		double adultTotalFare = flightOption.getFlightFare().getAdultBaseFare() + flightOption.getFlightFare().getAdultFees() + flightOption.getFlightFare().getAdultTax();
		if(adults > 0){
			rulesCommonBean.setTotalFare(adultTotalFare/adults);
			rulesCommonBean.setBaseFareT(flightOption.getFlightFare().getAdultBaseFare()/adults);
		}
		double childTotalFare = flightOption.getFlightFare().getChildBaseFare() + flightOption.getFlightFare().getChildFees() + flightOption.getFlightFare().getChildTax();
		if(childs > 0){
			rulesCommonBean.setTotalFareChild(childTotalFare/childs);
			rulesCommonBean.setBaseFareTChild(flightOption.getFlightFare().getChildBaseFare()/childs);
		}
		double infantTotalFare = flightOption.getFlightFare().getInfantBaseFare() + flightOption.getFlightFare().getInfantFees() + flightOption.getFlightFare().getInfantTax();
		if(infants > 0) {
			rulesCommonBean.setTotalFareInfant(infantTotalFare/infants);
			rulesCommonBean.setBaseFareTInfant(flightOption.getFlightFare().getInfantBaseFare()/infants);
		}
		
		RulesCommonBean commonBean = null;
		
		if(flightOption.getFlightFare() != null) {
			flightOption.getFlightFare().setAdultMarkupPrice(0);
			flightOption.getFlightFare().setAdultDiscountPrice(0);
			flightOption.getFlightFare().setAdultServiceChargePrice(0);
			flightOption.getFlightFare().setChildMarkupPrice(0);
			flightOption.getFlightFare().setChildDiscountPrice(0);
			flightOption.getFlightFare().setChildServiceChargePrice(0);
			flightOption.getFlightFare().setInfantMarkupPrice(0);
			flightOption.getFlightFare().setInfantDiscountPrice(0);
			flightOption.getFlightFare().setInfantServiceChargePrice(0);
		}
		
		rulesCommonBean.setRuleType("markup");
		if(KieSessionService.kieSessFlight != null) {
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessFlight);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessFlight = null;
										KieSessionService.getKieSessionFlight();
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessFlight);
									}
							} else {
								KieSessionService.kieSessFlight = null;
								CallLog.info(105, "No approved flight rules found.");
							}
						}
					}					
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}
				
		}
		
		if (commonBean != null) {
			if (commonBean.getMarkupAdultList()!= null && !commonBean.getMarkupAdultList().isEmpty()) {
				double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
				flightOption.getFlightFare().setAdultMarkupPrice(maxAdultMarkup);
				rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
			}		
			if (commonBean.getMarkupChildList()!= null && !commonBean.getMarkupChildList().isEmpty()) {
				double maxChildMarkup = Collections.max(commonBean.getMarkupChildList());
				flightOption.getFlightFare().setChildMarkupPrice(maxChildMarkup);
				rulesCommonBean.setUpdatedPriceChild(maxChildMarkup);
			}
			if (commonBean.getMarkupInfantList()!= null && !commonBean.getMarkupInfantList().isEmpty()) {
				double maxInfantMarkup = Collections.max(commonBean.getMarkupInfantList());
				flightOption.getFlightFare().setInfantMarkupPrice(maxInfantMarkup);
				rulesCommonBean.setUpdatedPriceInfant(maxInfantMarkup);
			}		
		}
		rulesCommonBean.setRuleType("discOrSc");
		if(KieSessionService.kieSessFlight != null) {
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessFlight);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessFlight = null;
										KieSessionService.getKieSessionFlight();	
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessFlight);
									}

							} else {
								KieSessionService.kieSessFlight = null;
								CallLog.info(105, "No approved flight rules found.");
							}
						}
					}				
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}	
		}
		if (commonBean != null) {
			
			if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
				double maxAdultDiscount =  Collections.max(commonBean.getDiscountAdultList());
				flightOption.setDiscountIdAdult(commonBean.getDiscIdAdult().get(maxAdultDiscount));
				flightOption.getFlightFare().setAdultDiscountPrice(maxAdultDiscount);
			}
			if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
				double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
				flightOption.getFlightFare().setAdultServiceChargePrice(maxAdultServiceCharge);
			}     

			if (commonBean.getDiscountChildList() != null && !commonBean.getDiscountChildList().isEmpty()) {
				double maxChildDiscount =  Collections.max(commonBean.getDiscountChildList());
				flightOption.setDiscountIdChild(commonBean.getDiscIdChild().get(maxChildDiscount));
				flightOption.getFlightFare().setChildDiscountPrice(maxChildDiscount);
			}
			if (commonBean.getServiceChargeChildList() != null && !commonBean.getServiceChargeChildList().isEmpty()) {
				double maxChildServiceCharge = Collections.max(commonBean.getServiceChargeChildList());
				flightOption.getFlightFare().setChildServiceChargePrice(maxChildServiceCharge);
			}
			
			if (commonBean.getDiscountInfantList() != null && !commonBean.getDiscountInfantList().isEmpty()) {
				double maxInfantDiscount =  Collections.max(commonBean.getDiscountInfantList());
				flightOption.setDiscountIdInfant(commonBean.getDiscIdInfant().get(maxInfantDiscount));
				flightOption.getFlightFare().setInfantDiscountPrice(maxInfantDiscount);
			}
			if (commonBean.getServiceChargeInfantList() != null && !commonBean.getServiceChargeInfantList().isEmpty()) {
				double maxInfantServiceCharge = Collections.max(commonBean.getServiceChargeInfantList());
				flightOption.getFlightFare().setInfantServiceChargePrice(maxInfantServiceCharge);
			}

		}
		return flightOption;
	}

	private static RoundTripFlightOption ruleApplyRoundTripOption(RoundTripFlightOption roundTripFlightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId,
			ProductService productService) {

		RulesCommonBean rulesCommonBean = new RulesCommonBean();

		//rulesCommonBean.setCountryId(flightSearchRequestBean.getCountryId());
		if(flightSearchRequestBean.getAgentId()!=null && !flightSearchRequestBean.getAgentId().isEmpty()) {
			rulesCommonBean.setAgencyId(flightSearchRequestBean.getAgentId());
		} else {
			rulesCommonBean.setBranchId(flightSearchRequestBean.getBranchId());
		}
		rulesCommonBean.setTicketingPeriod(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		RulesFlightBean rulesFlightBean;

		rulesFlightBean = processRulesFlightBeanRoundTrip(roundTripFlightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId);

		rulesCommonBean.setRulesFlightBean(rulesFlightBean);
		
		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		
		double adultTotalFare = roundTripFlightOption.getAdultBaseFare() + roundTripFlightOption.getAdultTax();
		if(adults > 0){
			rulesCommonBean.setTotalFare(adultTotalFare/adults);
			rulesCommonBean.setBaseFareT(roundTripFlightOption.getAdultBaseFare()/adults);
		}
		
		double childTotalFare = roundTripFlightOption.getChildBaseFare() + roundTripFlightOption.getChildTax();
		if(childs > 0){
			rulesCommonBean.setTotalFareChild(childTotalFare/childs);
			rulesCommonBean.setBaseFareTChild(roundTripFlightOption.getChildBaseFare()/childs);
		}

		double infantTotalFare = roundTripFlightOption.getInfantBaseFare() + roundTripFlightOption.getInfantTax();
		if(infants > 0) {
			rulesCommonBean.setTotalFareInfant(infantTotalFare/infants);
			rulesCommonBean.setBaseFareTInfant(roundTripFlightOption.getInfantBaseFare()/infants);
		}
		
		RulesCommonBean commonBean = null;
		
		roundTripFlightOption.setAdultMarkupPrice(0);
		roundTripFlightOption.setAdultDiscountPrice(0);
		roundTripFlightOption.setAdultServiceChargePrice(0);
		roundTripFlightOption.setChildMarkupPrice(0);
		roundTripFlightOption.setChildDiscountPrice(0);
		roundTripFlightOption.setChildServiceChargePrice(0);
		roundTripFlightOption.setInfantMarkupPrice(0);
		roundTripFlightOption.setInfantDiscountPrice(0);
		roundTripFlightOption.setInfantServiceChargePrice(0);
		
		rulesCommonBean.setRuleType("markup");
		if(KieSessionService.kieSessFlight != null) {
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessFlight);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessFlight = null;
										KieSessionService.getKieSessionFlight();	
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessFlight);
									}

							} else {
								KieSessionService.kieSessFlight = null;
								CallLog.info(105, "No approved flight rules found.");
							}
						}
					}					
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}
		}
		
		if (commonBean != null) {
			if (commonBean.getMarkupAdultList()!= null && !commonBean.getMarkupAdultList().isEmpty()) {
				double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
				roundTripFlightOption.setAdultMarkupPrice(maxAdultMarkup);
				rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
			}
			if (commonBean.getMarkupChildList()!= null && !commonBean.getMarkupChildList().isEmpty()) {
				double maxChildMarkup = Collections.max(commonBean.getMarkupChildList());
				roundTripFlightOption.setChildMarkupPrice(maxChildMarkup);
				rulesCommonBean.setUpdatedPriceChild(maxChildMarkup);
			}	
			if (commonBean.getMarkupInfantList()!= null && !commonBean.getMarkupInfantList().isEmpty()) {
				double maxInfantMarkup = Collections.max(commonBean.getMarkupInfantList());
				roundTripFlightOption.setInfantMarkupPrice(maxInfantMarkup);
				rulesCommonBean.setUpdatedPriceInfant(maxInfantMarkup);
			}
		}
		
		rulesCommonBean.setRuleType("discOrSc");
		if(KieSessionService.kieSessFlight != null) {
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessFlight);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessFlight = null;
										KieSessionService.getKieSessionFlight();	
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessFlight);
									}

							} else {
								KieSessionService.kieSessFlight = null;
								CallLog.info(105, "No approved flight rules found.");
							}
						}
					}	
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}
		}
		if (commonBean != null) {			
			if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
				double maxAdultDiscount =  Collections.max(commonBean.getDiscountAdultList());
				roundTripFlightOption.setDiscountIdAdult(commonBean.getDiscIdAdult().get(maxAdultDiscount));
				roundTripFlightOption.setAdultDiscountPrice(maxAdultDiscount);
			}
			if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
				double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
				roundTripFlightOption.setAdultServiceChargePrice(maxAdultServiceCharge);
			}		
			if (commonBean.getDiscountChildList() != null && !commonBean.getDiscountChildList().isEmpty()) {
				double maxChildDiscount =  Collections.max(commonBean.getDiscountChildList());
				roundTripFlightOption.setDiscountIdChild(commonBean.getDiscIdChild().get(maxChildDiscount));
				roundTripFlightOption.setChildDiscountPrice(maxChildDiscount);
			}
			if (commonBean.getServiceChargeChildList() != null && !commonBean.getServiceChargeChildList().isEmpty()) {
				double maxChildServiceCharge = Collections.max(commonBean.getServiceChargeChildList());
				roundTripFlightOption.setChildServiceChargePrice(maxChildServiceCharge);
			}
			if (commonBean.getDiscountInfantList() != null && !commonBean.getDiscountInfantList().isEmpty()) {
				double maxInfantDiscount =  Collections.max(commonBean.getDiscountInfantList());
				roundTripFlightOption.setDiscountIdInfant(commonBean.getDiscIdInfant().get(maxInfantDiscount));
				roundTripFlightOption.setInfantDiscountPrice(maxInfantDiscount);
			}
			if (commonBean.getServiceChargeInfantList() != null && !commonBean.getServiceChargeInfantList().isEmpty()) {
				double maxInfantServiceCharge = Collections.max(commonBean.getServiceChargeInfantList());
				roundTripFlightOption.setInfantServiceChargePrice(maxInfantServiceCharge);
			}
		}
		return roundTripFlightOption;
	}

	private static FlightCalendarOption ruleApplyCalenderFlight(FlightCalendarOption flightCalenderOption,FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,
			ResultBean resultBeanAirline,List<AirportModal> airportModals,String tripType,ProductService productService) {
		RulesCommonBean rulesCommonBean = new RulesCommonBean();

		//rulesCommonBean.setCountryId(flightSearchRequestBean.getCountryId());
		if(flightSearchRequestBean.getAgentId()!=null && !flightSearchRequestBean.getAgentId().isEmpty()) {
			rulesCommonBean.setAgencyId(flightSearchRequestBean.getAgentId());
		} else {
			rulesCommonBean.setBranchId(flightSearchRequestBean.getBranchId());
		}
		rulesCommonBean.setTicketingPeriod(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		if (ROUNDTRIP.equalsIgnoreCase(tripType)) {
			rulesFlightBean = processRulesFlightBeanRoundTripCalender(flightCalenderOption, flightSearchRequestBean, geoLocationService, credentialService,resultBeanAirline,airportModals);
		} else if (ONEWAY.equalsIgnoreCase(tripType)) {
			rulesFlightBean = processRulesFlightBeanCalender(flightCalenderOption, flightSearchRequestBean, geoLocationService, credentialService,resultBeanAirline,airportModals);
		}

		rulesCommonBean.setRulesFlightBean(rulesFlightBean);

		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		
		boolean flagFare = flightCalenderOption.getFlightFare()!=null;
		double adultTotalFare = flagFare ? (flightCalenderOption.getFlightFare().getAdultBaseFare() + flightCalenderOption.getFlightFare().getAdultFees() + flightCalenderOption.getFlightFare().getAdultTax()):0;
		if(adults > 0) {
			rulesCommonBean.setTotalFare(adultTotalFare/adults);
			rulesCommonBean.setBaseFareT(flagFare ? flightCalenderOption.getFlightFare().getAdultBaseFare()/adults : 0);
		}

		double childTotalFare = flagFare ? (flightCalenderOption.getFlightFare().getChildBaseFare() + flightCalenderOption.getFlightFare().getChildFees() + flightCalenderOption.getFlightFare().getChildTax()):0;
		if(childs > 0) {
			rulesCommonBean.setTotalFareChild(childTotalFare/childs);
			rulesCommonBean.setBaseFareTChild(flagFare ? flightCalenderOption.getFlightFare().getChildBaseFare()/childs : 0);
		}

		double infantTotalFare = flagFare ? (flightCalenderOption.getFlightFare().getInfantBaseFare() + flightCalenderOption.getFlightFare().getInfantFees() + flightCalenderOption.getFlightFare().getInfantTax()) :0;
		if(infants > 0) {
			rulesCommonBean.setTotalFareInfant(infantTotalFare/infants);
			rulesCommonBean.setBaseFareTInfant(flagFare ? flightCalenderOption.getFlightFare().getInfantBaseFare()/infants : 0);
		}
		
		RulesCommonBean commonBean = null;
		
		if(flagFare) {
			flightCalenderOption.getFlightFare().setAdultMarkupPrice(0);
			flightCalenderOption.getFlightFare().setAdultDiscountPrice(0);
			flightCalenderOption.getFlightFare().setAdultServiceChargePrice(0);
			flightCalenderOption.getFlightFare().setChildMarkupPrice(0);
			flightCalenderOption.getFlightFare().setChildDiscountPrice(0);
			flightCalenderOption.getFlightFare().setChildServiceChargePrice(0);
			flightCalenderOption.getFlightFare().setInfantMarkupPrice(0);
			flightCalenderOption.getFlightFare().setInfantDiscountPrice(0);
			flightCalenderOption.getFlightFare().setInfantServiceChargePrice(0);		
		}
		
		rulesCommonBean.setRuleType("markup");
		if(KieSessionService.kieSessFlight != null && flagFare){
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessFlight);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessFlight = null;
										KieSessionService.getKieSessionFlight();	
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessFlight);
									}
							} else {
								KieSessionService.kieSessFlight = null;
								CallLog.info(105, "No approved flight rules found.");
							}
						}
					}	
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}
		}
		
		if (commonBean != null) {
			if (commonBean.getMarkupAdultList()!= null && !commonBean.getMarkupAdultList().isEmpty()) {
				double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
				flightCalenderOption.getFlightFare().setAdultMarkupPrice(maxAdultMarkup);
				rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
			}
			if (commonBean.getMarkupChildList()!= null && !commonBean.getMarkupChildList().isEmpty()) {
				double maxChildMarkup = Collections.max(commonBean.getMarkupChildList());
				flightCalenderOption.getFlightFare().setChildMarkupPrice(maxChildMarkup);
				rulesCommonBean.setUpdatedPriceChild(maxChildMarkup);
			}	
			if (commonBean.getMarkupInfantList()!= null && !commonBean.getMarkupInfantList().isEmpty()) {
				double maxInfantMarkup = Collections.max(commonBean.getMarkupInfantList());
				flightCalenderOption.getFlightFare().setInfantMarkupPrice(maxInfantMarkup);
				rulesCommonBean.setUpdatedPriceInfant(maxInfantMarkup);
			}		
		}
		rulesCommonBean.setRuleType("discOrSc");
		if(KieSessionService.kieSessFlight != null && flagFare){
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessFlight);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessFlight = null;
										KieSessionService.getKieSessionFlight();	
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessFlight);
									}

							} else {
								KieSessionService.kieSessFlight = null;
								CallLog.info(105, "No approved flight rules found.");
							}
						}
					}	
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}
		}
		
		if (commonBean != null) {
			if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
				double maxAdultDiscount =  Collections.max(commonBean.getDiscountAdultList());
				flightCalenderOption.getFlightFare().setAdultDiscountPrice(maxAdultDiscount);
			}
			if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
				double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
				flightCalenderOption.getFlightFare().setAdultServiceChargePrice(maxAdultServiceCharge);
			}
			if (commonBean.getDiscountChildList() != null && !commonBean.getDiscountChildList().isEmpty()) {
				double maxChildDiscount =  Collections.max(commonBean.getDiscountChildList());
				flightCalenderOption.getFlightFare().setChildDiscountPrice(maxChildDiscount);
			}
			if (commonBean.getServiceChargeChildList() != null && !commonBean.getServiceChargeChildList().isEmpty()) {
				double maxChildServiceCharge = Collections.max(commonBean.getServiceChargeChildList());
				flightCalenderOption.getFlightFare().setChildServiceChargePrice(maxChildServiceCharge);
			}
			if (commonBean.getDiscountInfantList() != null && !commonBean.getDiscountInfantList().isEmpty()) {
				double maxInfantDiscount =  Collections.max(commonBean.getDiscountInfantList());
				flightCalenderOption.getFlightFare().setInfantDiscountPrice(maxInfantDiscount);
			}
			if (commonBean.getServiceChargeInfantList() != null && !commonBean.getServiceChargeInfantList().isEmpty()) {
				double maxInfantServiceCharge = Collections.max(commonBean.getServiceChargeInfantList());
				flightCalenderOption.getFlightFare().setInfantServiceChargePrice(maxInfantServiceCharge);
			}
		}
		return flightCalenderOption;
	}
	public static List<FlightOption> processFlightoption(List<FlightOption> flightOptionList, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,String pccId,ProductService productService) {
		List<FlightOption> flightOptionsList = new ArrayList<>();
		if (flightOptionList != null && !flightOptionList.isEmpty()) {
			
			Long time1 = 0l;
			Long time2 = 0l;
			for (FlightOption flightOpt : flightOptionList) {
				Long st1 = System.currentTimeMillis();
				FlightOption flightOption = setAirlineAirportFlightOpt(flightOpt,resultBeanAirline,airportModals);
				Long end1 = System.currentTimeMillis();
				time1 = time1 + (end1-st1);
				Long st2= System.currentTimeMillis();
				flightOption = processT3PriceAndTagFlight(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, productService);
				Long end2= System.currentTimeMillis();
				time2 = time2 + (end2-st2);
				List<FlightOption> moreFlightOptionList = flightOption.getMoreOptions();
				if (moreFlightOptionList != null && !moreFlightOptionList.isEmpty()) {
					for (FlightOption moreFlightOption : moreFlightOptionList) {
						processT3PriceAndTagFlight(moreFlightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, productService);
					}
				}
				flightOption.setMoreOptions(moreFlightOptionList);
				flightOptionsList.add(flightOption);
			}
			CallLog.info(102,"Time Taken ::: RuleSimulationHelper:processFlightOption:setAirlineAirportFlightOpt ::"+time1);
			CallLog.info(102,"Time Taken ::: RuleSimulationHelper:processFlightOption::ruleinvokeTime+agencyMarkup ::"+time2);
		}
		return flightOptionsList;
	}

	public static List<RoundTripFlightOption> processRoundTripFlightOption(List<RoundTripFlightOption> reFlightOptions, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,
			ResultBean resultBeanAirline,List<AirportModal> airportModals,String pccId,ProductService productService) {
		List<RoundTripFlightOption> roundFlightOptions = new ArrayList<>();
		if (reFlightOptions != null && !reFlightOptions.isEmpty()) {
			Long time1 = 0l;
			Long time2 = 0l;
			for (RoundTripFlightOption roundTripFlightOpt : reFlightOptions) {
				Long st1= System.currentTimeMillis();
				RoundTripFlightOption roundTripFlightOption = setAirlineAirportFlightRoundOpt(roundTripFlightOpt,resultBeanAirline,airportModals);
				Long end1= System.currentTimeMillis();
				time1 = time1 + (end1-st1);
				
				if (roundTripFlightOption != null ) {
					Long st2= System.currentTimeMillis();
					RuleSimulationHelper.processT3PriceAndTagFlightRoundTrip(roundTripFlightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, productService);
					Long end2= System.currentTimeMillis();
					time2 = time2 + (end2-st2);
					List<RoundTripFlightOption> moreFlightOptionList = roundTripFlightOption.getMoreOptions();
					if (moreFlightOptionList != null && !moreFlightOptionList.isEmpty()) {
						for (RoundTripFlightOption moreFlightOption : moreFlightOptionList) {
							RuleSimulationHelper.processT3PriceAndTagFlightRoundTrip(moreFlightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, productService);
						}
					}
					roundTripFlightOption.setMoreOptions(moreFlightOptionList);
					roundFlightOptions.add(roundTripFlightOption);
				}
			}
			CallLog.info(102,"Time Taken ::: RuleSimulationHelper:processFlightOption:setAirlineAirportFlightOpt ::"+time1);
			CallLog.info(102,"Time Taken ::: RuleSimulationHelper:processFlightOption::ruleinvokeTime+agencyMarkup ::"+time2);
		}
		return roundFlightOptions;
	}

	public static List<FlightOption> processMutliCityFlightoption(List<FlightOption> flightOptions, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,
			ResultBean resultBeanAirline,List<AirportModal> airportModals,String pccId,ProductService productService) {
		List<FlightOption> flightOptionList = new ArrayList<>();
		if (flightOptions != null && !flightOptions.isEmpty()) {
			for (FlightOption flightOpt : flightOptions) {
				FlightOption flightOption = setAirlineAirportFlightOptMulti(flightOpt,resultBeanAirline,airportModals);
				processT3PriceAndTagFlightMultiCity(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, productService);
				flightOptionList.add(flightOption);
			}
		}
		return flightOptionList;
	}

	private static void processT3PriceAndTagFlightMultiCity(FlightOption flightOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String pccId,ProductService productService) {

		double t3Price = flightOption.getTotalFare();

		FlightOption flightOptionUpdated = null;
		

		double adultMarkupPrice = 0;
		double adultServiceChargePrice = 0;
		double adultDiscountPrice = 0;

		double childMarkupPrice = 0;
		double childServiceChargePrice = 0;
		double childDiscountPrice = 0;

		double infantMarkupPrice = 0;
		double infantServiceChargePrice = 0;
		double infantDiscountPrice = 0;

		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		
		flightOptionUpdated = ruleApplyFlightOption(flightOption, flightSearchRequestBean, geoLocationService, credentialService,pccId, MULTICITY, productService);
		
			
		if (flightOptionUpdated != null) {
			if(adults > 0) {
				adultMarkupPrice = flightOptionUpdated.getFlightFare().getAdultMarkupPrice() * adults;
				adultDiscountPrice = flightOptionUpdated.getFlightFare().getAdultDiscountPrice() * adults;
				adultServiceChargePrice = flightOptionUpdated.getFlightFare().getAdultServiceChargePrice() * adults;				
			}
			if(childs > 0) {
				childMarkupPrice = flightOptionUpdated.getFlightFare().getChildMarkupPrice() * childs;
				childDiscountPrice = flightOptionUpdated.getFlightFare().getChildDiscountPrice() * childs;
				childServiceChargePrice = flightOptionUpdated.getFlightFare().getChildServiceChargePrice() * childs;
			}
			if(infants > 0) {
				infantMarkupPrice = flightOptionUpdated.getFlightFare().getInfantMarkupPrice() * infants;
				infantDiscountPrice = flightOptionUpdated.getFlightFare().getInfantDiscountPrice() * infants;
				infantServiceChargePrice = flightOptionUpdated.getFlightFare().getInfantServiceChargePrice() * infants;
			}
			
		}

		flightOption.getFlightFare().setAdultMarkupPrice(adultMarkupPrice);
		flightOption.getFlightFare().setAdultServiceChargePrice(adultServiceChargePrice);
		flightOption.getFlightFare().setAdultDiscountPrice(adultDiscountPrice);

		flightOption.getFlightFare().setChildMarkupPrice(childMarkupPrice);
		flightOption.getFlightFare().setChildServiceChargePrice(childServiceChargePrice);
		flightOption.getFlightFare().setChildDiscountPrice(childDiscountPrice);

		flightOption.getFlightFare().setInfantMarkupPrice(infantMarkupPrice);
		flightOption.getFlightFare().setInfantServiceChargePrice(infantServiceChargePrice);
		flightOption.getFlightFare().setInfantDiscountPrice(infantDiscountPrice);

		flightOption.getFlightFare().setMarkupPrice(adultMarkupPrice + childMarkupPrice + infantMarkupPrice);
		flightOption.getFlightFare().setServiceChargePrice(adultServiceChargePrice + childServiceChargePrice + infantServiceChargePrice);
		flightOption.getFlightFare().setDiscountPrice(adultDiscountPrice + childDiscountPrice + infantDiscountPrice);

		t3Price = t3Price + flightOption.getFlightFare().getMarkupPrice() + flightOption.getFlightFare().getServiceChargePrice();

		flightOption.getFlightFare().setT3Price(t3Price);
	}

	public static FlightCalendarOption processCalenderFlight(FlightCalendarOption flightCalenderOption, FlightSearchRequestBean flightSearchRequestBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,ProductService productService) {
		
		double t3Price = flightCalenderOption.getTotalFare();
		FlightCalendarOption flightCalenderOptionUpdated = null;
		double adultMarkupPrice = 0;
		double adultServiceChargePrice = 0;
		double adultDiscountPrice = 0;

		double childMarkupPrice = 0;
		double childServiceChargePrice = 0;
		double childDiscountPrice = 0;

		double infantMarkupPrice = 0;
		double infantServiceChargePrice = 0;
		double infantDiscountPrice = 0;

		int adults = flightSearchRequestBean.getNoOfAdults();
		int childs = flightSearchRequestBean.getNoOfChilds();
		int infants = flightSearchRequestBean.getNoOfInfants();
		flightCalenderOptionUpdated = ruleApplyCalenderFlight(flightCalenderOption, flightSearchRequestBean, geoLocationService, credentialService,resultBeanAirline,airportModals ,flightSearchRequestBean.getTripType(), productService);
		boolean flagFare = flightCalenderOptionUpdated.getFlightFare()!= null;
		
		if (flagFare && flightCalenderOptionUpdated != null) {
			if(adults > 0) {
				adultMarkupPrice = flightCalenderOptionUpdated.getFlightFare().getAdultMarkupPrice() * adults;
				adultDiscountPrice = flightCalenderOptionUpdated.getFlightFare().getAdultDiscountPrice() * adults;
				adultServiceChargePrice = flightCalenderOptionUpdated.getFlightFare().getAdultServiceChargePrice() * adults;				
			}
			if(childs > 0) {
				childMarkupPrice = flightCalenderOptionUpdated.getFlightFare().getChildMarkupPrice() * childs;
				childDiscountPrice = flightCalenderOptionUpdated.getFlightFare().getChildDiscountPrice() * childs;
				childServiceChargePrice = flightCalenderOptionUpdated.getFlightFare().getChildServiceChargePrice() * childs;
			}
			if(infants > 0) {
				infantMarkupPrice = flightCalenderOptionUpdated.getFlightFare().getInfantMarkupPrice() * infants;
				infantDiscountPrice = flightCalenderOptionUpdated.getFlightFare().getInfantDiscountPrice() * infants;
				infantServiceChargePrice = flightCalenderOptionUpdated.getFlightFare().getInfantServiceChargePrice() * infants;
			}
			
		}

		if(flagFare) {
			flightCalenderOption.getFlightFare().setAdultMarkupPrice(adultMarkupPrice);
			flightCalenderOption.getFlightFare().setAdultServiceChargePrice(adultServiceChargePrice);
			flightCalenderOption.getFlightFare().setAdultDiscountPrice(adultDiscountPrice);

			flightCalenderOption.getFlightFare().setChildMarkupPrice(childMarkupPrice);
			flightCalenderOption.getFlightFare().setChildServiceChargePrice(childServiceChargePrice);
			flightCalenderOption.getFlightFare().setChildDiscountPrice(childDiscountPrice);

			flightCalenderOption.getFlightFare().setInfantMarkupPrice(infantMarkupPrice);
			flightCalenderOption.getFlightFare().setInfantServiceChargePrice(infantServiceChargePrice);
			flightCalenderOption.getFlightFare().setInfantDiscountPrice(infantDiscountPrice);

			flightCalenderOption.getFlightFare().setMarkupPrice(adultMarkupPrice + childMarkupPrice + infantMarkupPrice);
			flightCalenderOption.getFlightFare().setServiceChargePrice(adultServiceChargePrice + childServiceChargePrice + infantServiceChargePrice);
			flightCalenderOption.getFlightFare().setDiscountPrice(adultDiscountPrice + childDiscountPrice + infantDiscountPrice);

			t3Price = t3Price + flightCalenderOption.getFlightFare().getMarkupPrice() + flightCalenderOption.getFlightFare().getServiceChargePrice();
			flightCalenderOption.getFlightFare().setT3Price(t3Price);
		}
		
		return flightCalenderOption;
	}
	
	public static List<FProduct> processInsuranceOption(List<FProduct> fProduct, ProductPriceRequestBean requestBean, GeoLocationService geoLocationService,CorporateService corporateService,ProductService productService)
	{
		if (fProduct != null)
		{
			for (FProduct f : fProduct)
			{
				if (f != null)
				{
						RuleSimulationHelper.processT3PriceInsurance(f, requestBean, geoLocationService, corporateService,productService);
				}
			}
		}
		return fProduct;
	}
	
	private static void processT3PriceInsurance(FProduct f, ProductPriceRequestBean requestBean, GeoLocationService geoLocationService, CorporateService corporateService,ProductService productService) {
		f = ruleApplyForInsurance(f, requestBean,geoLocationService,corporateService,productService);
	}
	
	private static FProduct ruleApplyForInsurance(FProduct f, ProductPriceRequestBean requestBean,GeoLocationService geoLocationService,CorporateService corporateService,ProductService productService) {
		
		try{
			RulesCommonBean rulesCommonBean = new RulesCommonBean();
			int index = getIndexOfAmount(f);
			double t3Price = f.getConsolidatedPriceDetail().get(index).getBaseAmount();	
			RulesInsuranceBean rulesInsuranceBean = new RulesInsuranceBean();
			if(requestBean.isCrossSell())
				rulesInsuranceBean.setCrossSellingPath("Yes");
			else
				rulesInsuranceBean.setCrossSellingPath("No");
			
			if(requestBean.isForEx())
				rulesInsuranceBean.setForEx("Yes");
			else
				rulesInsuranceBean.setForEx("No");
			rulesInsuranceBean.setSupplier("HEPSTAR");
			
			rulesInsuranceBean.setFareFromTo(t3Price);
			rulesCommonBean.setRulesInsuranceBean(rulesInsuranceBean);
			
			//rulesCommonBean.setCountryId(requestBean.getCountryId());
			if(requestBean.getAgencyId()!=null && !requestBean.getAgencyId().isEmpty()) {
				rulesCommonBean.setAgencyId(requestBean.getAgencyId());
			} else {
				rulesCommonBean.setBranchId(requestBean.getBranchId());
			}
			rulesCommonBean.setTicketingPeriod(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
			
			
			rulesCommonBean.setTotalFare(t3Price);
			rulesCommonBean.setBaseFareT(t3Price);
			
			f.getConsolidatedPriceDetail().get(index).setMarkupPrice(0);
			f.getConsolidatedPriceDetail().get(index).setDiscountPrice(0);
			f.getConsolidatedPriceDetail().get(index).setServiceChargePrice(0);
			
			RulesCommonBean commonBean = null;
			rulesCommonBean.setRuleType("markup");
			if(KieSessionService.kieSessInsurance != null) {
				try{
					commonBean = fireRulesInsurance(rulesCommonBean,KieSessionService.kieSessInsurance);	
				} catch ( IllegalStateException ex) {
					try {
						ProductModel productModel = new ProductModel();
						productModel.setStatus(1);
						productModel.setProductCode(QueryConstant.RULE_GROUP_THREE);
						
						ResultBean resultBeanProduct = productService.getProducts(productModel);
						if (resultBeanProduct != null && !resultBeanProduct.isError()) {
							List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
							if (productList != null && !productList.isEmpty()) {
								ProductModel productModal = productList.get(0);
								if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
									boolean flagTextRule = KieSessionService.writeRuleTextInsurance(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessInsurance = null;
										KieSessionService.getKieSessionInsurance();
										commonBean = fireRulesInsurance(rulesCommonBean,KieSessionService.kieSessInsurance);	
									}
								}else{
									KieSessionService.kieSessInsurance = null;
								}

							}
						}
					} catch(Exception e) {
						CallLog.printStackTrace(105, e);
					}
					
				}catch(Exception ex) {
					CallLog.printStackTrace(105, ex);
				}
				
			}
						
			if(commonBean != null) {
				if (commonBean.getMarkupAdultList()!= null && !commonBean.getMarkupAdultList().isEmpty()) {
					double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
					if(f.getType().equals("Travel")){
						f.getConsolidatedPriceDetail().get(index).setMarkupPrice(maxAdultMarkup);
						t3Price = t3Price + maxAdultMarkup;
					}
					rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
				}							
			}
			
			rulesCommonBean.setRuleType("discOrSc");
			if(KieSessionService.kieSessInsurance != null) {

				try{
					commonBean = fireRulesInsurance(rulesCommonBean,KieSessionService.kieSessInsurance);	
				} catch ( IllegalStateException ex) {
					try {
						ProductModel productModel = new ProductModel();
						productModel.setStatus(1);
						productModel.setProductCode(QueryConstant.RULE_GROUP_THREE);
						
						ResultBean resultBeanProduct = productService.getProducts(productModel);
						if (resultBeanProduct != null && !resultBeanProduct.isError()) {
							List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
							if (productList != null && !productList.isEmpty()) {
								ProductModel productModal = productList.get(0);
								if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
									boolean flagTextRule = KieSessionService.writeRuleTextInsurance(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessInsurance = null;
										KieSessionService.getKieSessionInsurance();
										commonBean = fireRulesInsurance(rulesCommonBean,KieSessionService.kieSessInsurance);
									}
								}else{
									KieSessionService.kieSessInsurance = null;
								}
							}
						}
					} catch(Exception e) {
						CallLog.printStackTrace(105, e);
					}
					
				}catch(Exception ex) {
					CallLog.printStackTrace(105, ex);
				}
			}
						
			if(commonBean != null) {
				
				if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
					double maxAdultDiscount =  Collections.max(commonBean.getDiscountAdultList());
					if(f.getType().equals("Travel"))
						f.getConsolidatedPriceDetail().get(index).setDiscountPrice((maxAdultDiscount));
				}
				if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
					double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
					if(f.getType().equals("Travel")){
						f.getConsolidatedPriceDetail().get(index).setServiceChargePrice(maxAdultServiceCharge);
						t3Price = t3Price + maxAdultServiceCharge;
					}
				}				
			}
			
			f.getConsolidatedPriceDetail().get(index).setT3Price(t3Price);
			}catch(Exception e){
				CallLog.printStackTrace(12,e);
			}
			
			return f;
		}
	
	private static RulesCommonBean fireRulesInsurance(
		RulesCommonBean rulesCommonBean, KieSession kieSessInsurance) {
		List<RuleInterface> list = new ArrayList<>();
		list.add(rulesCommonBean);
		list.add(rulesCommonBean.getRulesInsuranceBean());

		List<FactHandle> factHandleList = new ArrayList<>();
		kieSessInsurance.halt();
		for (RuleInterface RulesCommonBean : list) {
			factHandleList.add(kieSessInsurance.insert(RulesCommonBean));
		}
		kieSessInsurance.fireAllRules();

		if (!factHandleList.isEmpty()) {
			for (FactHandle factHandle : factHandleList) {
				kieSessInsurance.retract(factHandle);
			}
		}

		return rulesCommonBean;
	}
	
	public static List<FProduct> processInsuranceOption1(List<FProduct> fProduct, InsuranceWidget insuranceWidget, SimulationRuleService simulationRuleService, OrganizationService organizationService, List<Object> list, String countryId,
			String branchId, String agencyId, GeoLocationService geoLocationService,ProductService productService) {
		if (fProduct != null) {
			for (FProduct f : fProduct) {
				if (f != null) {
					RuleSimulationHelper.processT3PriceInsurance(f, insuranceWidget, simulationRuleService, organizationService, list, countryId, branchId, agencyId, geoLocationService,productService);
				}
			}
		}
		return fProduct;
	}

	public static FProduct processT3PriceInsurance(FProduct f, InsuranceWidget insuranceWidget, SimulationRuleService simulationRuleService, OrganizationService organizationService, List<Object> list, String countryId, String branchId,
			String agencyId, GeoLocationService geoLocationService,ProductService productService) {
		RulesCommonBean rulesCommonBean = new RulesCommonBean();
		//rulesCommonBean.setCountryId(countryId);
		if(agencyId != null && !agencyId.isEmpty()) {
			rulesCommonBean.setAgencyId(agencyId);
		} else {
			rulesCommonBean.setBranchId(branchId);
		}
		rulesCommonBean.setTicketingPeriod(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		RulesInsuranceBean rulesInsuranceBean = new RulesInsuranceBean();
		rulesInsuranceBean.setCorporateId("");
		rulesInsuranceBean.setCrossSellingPath("");
		rulesInsuranceBean.setCustomerType("");
		rulesInsuranceBean.setFareFromTo(00.00);
		rulesInsuranceBean.setForEx("");
		rulesInsuranceBean.setSupplier("HEPSTAR");

		/*
		 * rulesFlightBean.setSupplier(flightOption.getServiceVendor()); String
		 * travelStrtDateStr =
		 * flightOption.getOptionSegmentBean().get(0).getFlightlegs
		 * ().get(0).getDepDate(); Date travelStrtDate =
		 * CommonUtil.convertStringToDateIndianFormat
		 * (travelStrtDateStr,"yyyy-MM-dd");
		 * rulesFlightBean.setTravelStartDate(travelStrtDate);
		 */

		rulesCommonBean.setRulesInsuranceBean(rulesInsuranceBean);

		rulesCommonBean.setTotalFare(getTotalAmount(f)); // Check original and
															// converted
															// currency
		rulesCommonBean.setBaseFareT(getTotalAmount(f));
		ResultBean resultBean = simulationRuleService.getT3PriceByApplyRules(rulesCommonBean, QueryConstant.RULE_GROUP_THREE);
		RulesCommonBean commonBean = (RulesCommonBean) resultBean.getResultObject();
		double t3Price = f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).getBaseAmount();
		if (commonBean != null && commonBean.getUpdatedPrice() > 0) {
			f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).setMarkupPrice(commonBean.getUpdatedPrice());
			t3Price = t3Price + commonBean.getUpdatedPrice();
		}
		if (commonBean != null && commonBean.getDiscountPrice() > 0) {
			f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).setDiscountPrice(commonBean.getDiscountPrice());
		}
		if (commonBean != null && commonBean.getServiceChargePrice() > 0) {
			f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).setServiceChargePrice(commonBean.getServiceChargePrice());
			t3Price = t3Price + commonBean.getServiceChargePrice();
		}
		f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).setT3Price(t3Price);

		/*if (list != null && !list.isEmpty()) {
			boolean flag = true;
			for (int i = 0; i < list.size(); i++) {
				Object[] agencyList = (Object[]) list.get(i);
				if(agencyList != null && agencyList.length > 4) {
					String airline = agencyList[3].toString();
					if (airline != null) {
						boolean condition = agencyList[2].toString() != null && !"null".equalsIgnoreCase(agencyList[2].toString()) && !"".equalsIgnoreCase(agencyList[2].toString());
						f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).setAgencyMarkup(condition?Double.parseDouble(agencyList[2].toString()):0);
						flag = false;
					}
				}
			}
			if (flag)
				f.getConsolidatedPriceDetail().get(getIndexOfAmount(f)).setAgencyMarkup(0);
		}*/

		return f;
	}

	public static Double getTotalAmount(FProduct i) {
		Double totalAmount = 00.00;
		try {
			for (int b = 0; b < i.getConsolidatedPriceDetail().size(); b++) {
				if (i.getConsolidatedPriceDetail().get(b).getType().equalsIgnoreCase("ConvertedCurrency")) {
					totalAmount = (double) i.getConsolidatedPriceDetail().get(b).getBaseAmount();
					break;
				} else
					totalAmount = (double) i.getConsolidatedPriceDetail().get(b).getBaseAmount();
			}

			totalAmount = (double) Math.round(totalAmount);
		} catch (Exception e) {
			 CallLog.printStackTrace(12, e);
		}

		return totalAmount;
	}

	public static int getIndexOfAmount(FProduct i) {
		int index = 0;
		try {
			for (int b = 0; b < i.getConsolidatedPriceDetail().size(); b++) {
				if (i.getConsolidatedPriceDetail().get(b).getType().equalsIgnoreCase("ConvertedCurrency")) {
					index = b;
					break;
				} else
					index = b;
			}
		} catch (Exception e) {
			 CallLog.printStackTrace(12, e);
		}

		return index;
	}


	public static String dateFormat(String dateInString, String format) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedDate = null;
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = dateformat.parse(dateInString);
		} catch (ParseException e) {
			//CallLog.printStackTrace(109, e);
		}
		dateformat = new SimpleDateFormat("yyyy-MM-dd");
		String newdate = dateformat.format(date);
		return newdate;
	}

	private static RulesCommonBean fireRulesFlight(RulesCommonBean rulesCommonBean, KieSession kieSessFlight) {
		List<RuleInterface> list = new ArrayList<>();
		list.add(rulesCommonBean);
		list.add(rulesCommonBean.getRulesFlightBean());

		List<FactHandle> factHandleList = new ArrayList<>();
		kieSessFlight.halt();
		for (RuleInterface RulesCommonBean : list) {
			factHandleList.add(kieSessFlight.insert(RulesCommonBean));
		}
		kieSessFlight.fireAllRules();

		if (!factHandleList.isEmpty()) {
			for (FactHandle factHandle : factHandleList) {
				kieSessFlight.retract(factHandle);
			}
		}
		return rulesCommonBean;
	}
	public static RulesCommonBean fireRulesHotel(RulesCommonBean rulesCommonBean, KieSession kieSessHotel) {
		try {
			List<RuleInterface> list = new ArrayList<>();
			list.add(rulesCommonBean);
			list.add(rulesCommonBean.getRulesHotelBean());
	
			List<FactHandle> factHandleList = new ArrayList<>();
			kieSessHotel.halt();
			for (RuleInterface RulesCommonBean : list) {
				factHandleList.add(kieSessHotel.insert(RulesCommonBean));
			}
			kieSessHotel.fireAllRules();
	
			if (!factHandleList.isEmpty()) {
				for (FactHandle factHandle : factHandleList) {
					kieSessHotel.retract(factHandle);
				}
			}
		}catch (Exception e) {
			CallLog.printStackTrace(105, e);
		}
		return rulesCommonBean;
	}

	private static FlightCalendarOption setAirlineAirportDataFlightCal(FlightCalendarOption flightCalOption,ResultBean resultBeanAirline,List<AirportModal> airportModals) { 	
		flightCalOption  = setAirlineAirportCalender(flightCalOption,resultBeanAirline,airportModals);
    	return flightCalOption;
    }
    
    @SuppressWarnings("unchecked")
	private static FlightCalendarOption setAirlineAirportCalender(FlightCalendarOption flightCalOption, ResultBean resultBeanAirline,List<AirportModal> airportModals) {

    	if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
			for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {	
				if(flightCalOption.getPlatingCarrier() != null && airlineModel.getAirlineCode().equalsIgnoreCase(flightCalOption.getPlatingCarrier())) {
					flightCalOption.setPlatingCarrierName(airlineModel.getAirlineName());
					flightCalOption.setPlatingAirlineType(airlineModel.getAirlineType()!=null?airlineModel.getAirlineType():0);
					break;
				}
			}
		}
    	List<FlightLegs> flightLegs = flightCalOption.getFlightlegs();
		
		if(flightLegs != null && !flightLegs.isEmpty()) {
    		for(FlightLegs flightLeg:flightLegs) {
    			if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
    				boolean flagCarrier = false;
    				boolean flagOpAirline = false;
    				boolean flag1 = false;
					boolean flag2 = false;
    				for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {
    					
    					if(!flagCarrier && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getCarrier())) {
    						flightLeg.setCarrierName(airlineModel.getAirlineName());
    						flagCarrier = true;
    					}
    					if(!flagOpAirline && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getOperatedByAirline())) {
    						flightLeg.setOperatedByAirlineName(airlineModel.getAirlineName());
    						flagOpAirline = true;
    					}
    					if(flightCalOption.getOnwardAirline()!= null && flightCalOption.getReturnAirline()!=null) {
							if(!flag1 && flightCalOption.getOnwardAirline().equalsIgnoreCase(airlineModel.getAirlineCode())) {
								flightCalOption.setOnwardAirlineName(airlineModel.getAirlineName());
								flag1 = true;
							}
							if(!flag2 && flightCalOption.getReturnAirline().equalsIgnoreCase(airlineModel.getAirlineCode())) {
								flightCalOption.setReturnAirlineName(airlineModel.getAirlineName());
								flag2 = true;
							}
    			    	}	
    					if(flagCarrier && flagOpAirline && flag1 && flag2) {
    						break;
    					}
    				}
    			}
    			
    			if(airportModals != null && !airportModals.isEmpty()) {
    				for(AirportModal airortModel : airportModals) {
    					boolean flag1 = false;
    					boolean flag2 = false;
    					if(!flag1 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getOrigin())) {
    						flightLeg.setOriginName(airortModel.getAirportName());
    						flightLeg.setOriginCityName(airortModel.getCityName());
    						flightLeg.setOriginCountry(String.valueOf(airortModel.getCountryID()));
    						flag1 = true;
    					}
    					if(!flag2 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getDestination())) {
    						flightLeg.setDestinationName(airortModel.getAirportName());
    						flightLeg.setDestinationCityName(airortModel.getCityName());
    						flightLeg.setDestinationCountry(String.valueOf(airortModel.getCountryID()));
    						flag2 = true;
    					}
    					List<FlightLegsStop> techStopList = flightLeg.getTechnicalStopList();
        				if(techStopList != null && !techStopList.isEmpty()) {
        					for(FlightLegsStop legStop : techStopList ) {
        						if(legStop.getOrigin().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setOriginName(airortModel.getAirportName());
        							legStop.setOriginCityName(airortModel.getCityName());
        						}
        						if(legStop.getDestination().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setDestinationName(airortModel.getAirportName());
        							legStop.setDestinationCityName(airortModel.getCityName());
        						}
        					}
        				}
        				if(flag1 && flag2) {
        					break;
        				}
    				}
    				
    			}
    			
    		}
    	}
		flightCalOption.setFlightlegs(flightLegs);
		List<FlightLegs> retFlightLegs =flightCalOption.getReturnFlightlegs();
		if(retFlightLegs != null && !retFlightLegs.isEmpty()) {
    		for(FlightLegs flightLeg:retFlightLegs) {
    			if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
    				boolean flagCarrier = false;
    				boolean flagOpAirline = false;
    				boolean flag1 = false;
					boolean flag2 = false;
    				for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {
    					
    					if(!flagCarrier && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getCarrier())) {
    						flightLeg.setCarrierName(airlineModel.getAirlineName());
    						flagCarrier = true;
    					}
    					if(!flagOpAirline && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getOperatedByAirline())) {
    						flightLeg.setOperatedByAirlineName(airlineModel.getAirlineName());
    						flagOpAirline = true;
    					}
    					if(flightCalOption.getOnwardAirline()!= null && flightCalOption.getReturnAirline()!=null) {
							if(!flag1 && flightCalOption.getOnwardAirline().equalsIgnoreCase(airlineModel.getAirlineCode())) {
								flightCalOption.setOnwardAirlineName(airlineModel.getAirlineName());
								flag1 = true;
							}
							if(!flag2 && flightCalOption.getReturnAirline().equalsIgnoreCase(airlineModel.getAirlineCode())) {
								flightCalOption.setReturnAirlineName(airlineModel.getAirlineName());
								flag2 = true;
							}
    			    	}	
    					if(flagCarrier && flagOpAirline && flag1 && flag2) {
    						break;
    					}
    				}
    			}
    			
    			if(airportModals != null && !airportModals.isEmpty()) {
    				for(AirportModal airortModel : airportModals) {
    					boolean flag1 = false;
    					boolean flag2 = false;
    					if(!flag1 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getOrigin())) {
    						flightLeg.setOriginName(airortModel.getAirportName());
    						flightLeg.setOriginCityName(airortModel.getCityName());
    						flag1 = true;
    					}
    					if(!flag2 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getDestination())) {
    						flightLeg.setDestinationName(airortModel.getAirportName());
    						flightLeg.setDestinationCityName(airortModel.getCityName());
    						flag2 = true;
    					}
    					List<FlightLegsStop> techStopList = flightLeg.getTechnicalStopList();
        				if(techStopList != null && !techStopList.isEmpty()) {
        					for(FlightLegsStop legStop : techStopList ) {
        						if(legStop.getOrigin().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setOriginName(airortModel.getAirportName());
        							legStop.setOriginCityName(airortModel.getCityName());
        						}
        						if(legStop.getDestination().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setDestinationName(airortModel.getAirportName());
        							legStop.setDestinationCityName(airortModel.getCityName());
        						}
        					}
        				}
        				if(flag1 && flag2) {
        					break;
        				}
    				}
    				
    			}
    			
    		}
    	}
		flightCalOption.setReturnFlightlegs(retFlightLegs);
		return flightCalOption;
    
	}

	private static FlightOption setAirlineAirportFlightOpt(FlightOption flightOption,ResultBean resultBeanAirline,List<AirportModal> airportModals) {
    	List<FlightOption> moreFlightOptions = flightOption.getMoreOptions();
    	if(moreFlightOptions != null && !moreFlightOptions.isEmpty()) {
    		for(FlightOption moreOption : moreFlightOptions) {
    			setAirlineAirportInFlightLegs(moreOption,resultBeanAirline,airportModals);
    		}
    	}
    	flightOption.setMoreOptions(moreFlightOptions);
    	setAirlineAirportInFlightLegs(flightOption,resultBeanAirline,airportModals);
    	return flightOption;
    }
    
    private static FlightOption setAirlineAirportFlightOptMulti(FlightOption flightOption,ResultBean resultBeanAirline,List<AirportModal> airportModals) {
    	if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
			for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {	
				if(flightOption.getPlatingCarrier() != null && airlineModel.getAirlineCode().equalsIgnoreCase(flightOption.getPlatingCarrier())) {
					flightOption.setPlatingCarrierName(airlineModel.getAirlineName());
					flightOption.setPlatingAirlineType(airlineModel.getAirlineType()!=null?airlineModel.getAirlineType():0);
					break;
				}
			}
		}
    	List<OptionSegmentBean> optionSegmentList = flightOption.getOptionSegmentBean();
    	if(optionSegmentList != null && !optionSegmentList.isEmpty()) {
    		for(OptionSegmentBean optionSegment:optionSegmentList) {
    			setAirlineAirportInFlightLegs(optionSegment,resultBeanAirline,airportModals);
    		}
    	}
    	flightOption.setOptionSegmentBean(optionSegmentList);
    	return flightOption;
    }
    
    private static void setAirlineAirportInFlightLegs(OptionSegmentBean optionSegment, ResultBean resultBeanAirline,List<AirportModal> airportModals) {
        List<FlightLegs> flightLegs = optionSegment.getFlightlegs();
    	if(flightLegs != null && !flightLegs.isEmpty()) {
    		for(FlightLegs flightLeg:flightLegs) {
    			if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
    				boolean flagCarrier = false;
    				boolean flagOpAirline = false;
    				for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {
    					
    					if(!flagCarrier && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getCarrier())) {
    						flightLeg.setCarrierName(airlineModel.getAirlineName());
    						flagCarrier = true;
    					}
    					if(!flagOpAirline && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getOperatedByAirline())) {
    						flightLeg.setOperatedByAirlineName(airlineModel.getAirlineName());
    						flagOpAirline = true;
    					}
    					if(flagCarrier && flagOpAirline) {
    						break;
    					}
    				}
    			}
    			
    			if(airportModals != null && !airportModals.isEmpty()) {
    								
    				for(AirportModal airortModel : airportModals) {
    					boolean flag1 = false;
    					boolean flag2 = false;
    					boolean flag3 = false;
    					boolean flag4 = false;
    					if(!flag1 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getOrigin())) {
    						flightLeg.setOriginName(airortModel.getAirportName());
    						flightLeg.setOriginCityName(airortModel.getCityName());
    						flightLeg.setOriginCountry(String.valueOf(airortModel.getCountryID()));
    						flag1 = true;
    					}
    					if(!flag2 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getDestination())) {
    						flightLeg.setDestinationName(airortModel.getAirportName());
    						flightLeg.setDestinationCityName(airortModel.getCityName());
    						flightLeg.setDestinationCountry(String.valueOf(airortModel.getCountryID()));
    						flag2 = true;
    					}
    					if(!flag3 && airortModel.getAirportCode().equalsIgnoreCase(optionSegment.getDestination())){
    						optionSegment.setDestinationCity(String.valueOf(airortModel.getCityId()));
    						optionSegment.setDestinationCountry(String.valueOf(airortModel.getCountryID()));
    						flag3 = true;
    					}
    					if(!flag4 && airortModel.getAirportCode().equalsIgnoreCase(optionSegment.getOrigin())){
    						optionSegment.setOriginCity(String.valueOf(airortModel.getCityId()));
    						optionSegment.setOriginCountry(String.valueOf(airortModel.getCountryID()));
    						flag4 = true;
    					}
    					List<FlightLegsStop> techStopList = flightLeg.getTechnicalStopList();
        				if(techStopList != null && !techStopList.isEmpty()) {
        					for(FlightLegsStop legStop : techStopList ) {
        						if(legStop.getOrigin().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setOriginName(airortModel.getAirportName());
        							legStop.setOriginCityName(airortModel.getCityName());
        						}
        						if(legStop.getDestination().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setDestinationName(airortModel.getAirportName());
        							legStop.setDestinationCityName(airortModel.getCityName());
        						}
        					}
        				}
        				if(flag1 && flag2 && flag3 && flag4) {
        					break;
        				}
    				}
    				
    			}
    			
    		}
    	}
	}

	private static RoundTripFlightOption setAirlineAirportFlightRoundOpt(RoundTripFlightOption roundTripOption,ResultBean resultBeanAirline,List<AirportModal> airportModals) {
		FlightOption onwardFlightOption = setAirlineAirportInFlightLegs(roundTripOption.getOnwardFlightOption(),resultBeanAirline,airportModals);
		FlightOption returnFlightOption = setAirlineAirportInFlightLegs(roundTripOption.getReturnFlightOption(),resultBeanAirline,airportModals);
    	roundTripOption.setOnwardFlightOption(onwardFlightOption);
    	roundTripOption.setReturnFlightOption(returnFlightOption);
    	List<RoundTripFlightOption> moreRoundTripOptions = roundTripOption.getMoreOptions();
    	if(moreRoundTripOptions != null && !moreRoundTripOptions.isEmpty()) {
    		for(RoundTripFlightOption roundTripOpt: moreRoundTripOptions) {
    			onwardFlightOption = setAirlineAirportInFlightLegs(roundTripOpt.getOnwardFlightOption(),resultBeanAirline,airportModals);
    			returnFlightOption = setAirlineAirportInFlightLegs(roundTripOpt.getReturnFlightOption(),resultBeanAirline,airportModals);
    			roundTripOpt.setOnwardFlightOption(onwardFlightOption);
    			roundTripOpt.setReturnFlightOption(returnFlightOption);
    		}
    	}
    	roundTripOption.setMoreOptions(moreRoundTripOptions);
    	return roundTripOption;
    }
    
	@SuppressWarnings("unchecked")
	private static FlightOption setAirlineAirportInFlightLegs(FlightOption flightOption,ResultBean resultBeanAirline,List<AirportModal> airportModals) {
		if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
			for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {	
				if(flightOption.getPlatingCarrier() != null && airlineModel.getAirlineCode().equalsIgnoreCase(flightOption.getPlatingCarrier())) {
					flightOption.setPlatingCarrierName(airlineModel.getAirlineName());
					flightOption.setPlatingAirlineType(airlineModel.getAirlineType()!=null?airlineModel.getAirlineType():0);
					break;
				}
			}
		}
		List<FlightLegs> flightLegs = flightOption.getFlightlegs();
		
		if(flightLegs != null && !flightLegs.isEmpty()) {
    		for(FlightLegs flightLeg:flightLegs) {
    			if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
    				boolean flagCarrier = false;
    				boolean flagOpAirline = false;
    				for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {
    					
    					if(!flagCarrier && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getCarrier())) {
    						flightLeg.setCarrierName(airlineModel.getAirlineName());
    						flagCarrier = true;
    					}
    					if(!flagOpAirline && airlineModel.getAirlineCode().equalsIgnoreCase(flightLeg.getOperatedByAirline())) {
    						flightLeg.setOperatedByAirlineName(airlineModel.getAirlineName());
    						flagOpAirline = true;
    					}
    					if(flagCarrier && flagOpAirline) {
    						break;
    					}
    				}
    			}
    			
    			if(airportModals != null && !airportModals.isEmpty()) {
    				for(AirportModal airortModel : airportModals) {
    					boolean flag1 = false;
    					boolean flag2 = false;
    					boolean flag3 = false;
    					boolean flag4 = false;
    					if(!flag1 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getOrigin())) {
    						flightLeg.setOriginName(airortModel.getAirportName());
    						flightLeg.setOriginCityName(airortModel.getCityName());
    						flightLeg.setOriginCountry(String.valueOf(airortModel.getCountryID()));
    						flag1 = true;
    					}
    					if(!flag2 && airortModel.getAirportCode().equalsIgnoreCase(flightLeg.getDestination())) {
    						flightLeg.setDestinationName(airortModel.getAirportName());
    						flightLeg.setDestinationCityName(airortModel.getCityName());
    						flightLeg.setDestinationCountry(String.valueOf(airortModel.getCountryID()));
    						flag2 = true;
    					}
    					if(!flag3 && airortModel.getAirportCode().equalsIgnoreCase(flightOption.getOrigin())) {
    						flightOption.setOriginCity(String.valueOf(airortModel.getCityId()));
    						flightOption.setOriginCountry(String.valueOf(airortModel.getCountryID()));
    						
    						flag3 = true;
    					}
    					if(!flag4 && airortModel.getAirportCode().equalsIgnoreCase(flightOption.getDestination())) {
    						flightOption.setDestinationCity(String.valueOf(airortModel.getCityId()));
    						flightOption.setDestinationCountry(String.valueOf(airortModel.getCountryID()));
    						
    						flag4 = true;
    					}
    					List<FlightLegsStop> techStopList = flightLeg.getTechnicalStopList();
        				if(techStopList != null && !techStopList.isEmpty()) {
        					for(FlightLegsStop legStop : techStopList ) {
        						if(legStop.getOrigin().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setOriginName(airortModel.getAirportName());
        							legStop.setOriginCityName(airortModel.getCityName());
        						}
        						if(legStop.getDestination().equalsIgnoreCase(airortModel.getAirportCode())) {
        							legStop.setDestinationName(airortModel.getAirportName());
        							legStop.setDestinationCityName(airortModel.getCityName());
        						}
        					}
        				}
        				if(flag1 && flag2 && flag3 && flag4) {
        					break;
        				}
    				}
    				
    			}
    			
    		}
    	}
		return flightOption;
    }
	
	public static ImportPNRPricingResponseBean processFlightoptionImportPnr(ImportPNRPricingResponseBean importPNRPricingResponseBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,String country, String branch, String agency, List<Object> agencyMarkupList,ProductService productService) {
		if (importPNRPricingResponseBean != null ) {
				importPNRPricingResponseBean = setAirlineAirportFlightImportPnr(importPNRPricingResponseBean,resultBeanAirline,airportModals);
				importPNRPricingResponseBean = processT3PriceAndTagFlightImportPnr(importPNRPricingResponseBean, geoLocationService, credentialService, country, branch, agency, agencyMarkupList, productService);
		}
		return importPNRPricingResponseBean;
	}
	
	public static ImportPNRPricingResponseBean processT3PriceAndTagFlightImportPnr(ImportPNRPricingResponseBean importPNRPricingResponseBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService, String country, String branch, String agency, List<Object> agencyMarkupList,ProductService productService) {
		return processT3FlightOptionImportPnr(importPNRPricingResponseBean, geoLocationService, credentialService, country, branch, agency, agencyMarkupList, productService);
	}
	
	private static ImportPNRPricingResponseBean processT3FlightOptionImportPnr(ImportPNRPricingResponseBean importPNRPricingResponseBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,String country, String branch, String agency, List<Object> agencyMarkupList,ProductService productService) {
		double t3Price = 0.0;
		if(importPNRPricingResponseBean.getFlightFare() != null)
			t3Price = importPNRPricingResponseBean.getFlightFare().getTotalNet();

		double adultMarkupPrice = 0;
		double adultServiceChargePrice = 0;
		double adultDiscountPrice = 0;

		double childMarkupPrice = 0;
		double childServiceChargePrice = 0;
		double childDiscountPrice = 0;

		double infantMarkupPrice = 0;
		double infantServiceChargePrice = 0;
		double infantDiscountPrice = 0;
		List<Passenger> passengerList = importPNRPricingResponseBean.getPassengerList();
		
		int adults =0;
		int childs = 0;
		int infants = 0;
		
		for(Passenger p: passengerList){
			if(p.getType().equals("ADT"))
				adults++;
			if(p.getType().equals("CHD"))
				childs++;
			if(p.getType().equals("INF"))
				infants++;
		}
		int totalPax = adults + childs + infants;
		importPNRPricingResponseBean = ruleApplyFlightOptionImportPnr(importPNRPricingResponseBean, geoLocationService, credentialService, country, branch, agency, productService);
		FlightFare flightFare = importPNRPricingResponseBean.getFlightFare();
		
		if (importPNRPricingResponseBean != null) {
			if(adults > 0) {
				adultMarkupPrice = flightFare.getAdultMarkupPrice() * adults;
				adultDiscountPrice = flightFare.getAdultDiscountPrice() * adults;
				adultServiceChargePrice = flightFare.getAdultServiceChargePrice() * adults;		
			}
			if(childs > 0) {
				childMarkupPrice = flightFare.getChildMarkupPrice() * childs;
				childDiscountPrice = flightFare.getChildDiscountPrice() * childs;
				childServiceChargePrice = flightFare.getChildServiceChargePrice() * childs;
			}
			if(infants > 0) {
				infantMarkupPrice = flightFare.getInfantMarkupPrice() * infants;
				infantDiscountPrice = flightFare.getInfantDiscountPrice() * infants;
				infantServiceChargePrice = flightFare.getInfantServiceChargePrice() * infants;
			}	
		}

		
		
		flightFare.setAdultMarkupPrice(adultMarkupPrice);
		flightFare.setAdultServiceChargePrice(adultServiceChargePrice);
		flightFare.setAdultDiscountPrice(adultDiscountPrice);

		flightFare.setChildMarkupPrice(childMarkupPrice);
		flightFare.setChildServiceChargePrice(childServiceChargePrice);
		flightFare.setChildDiscountPrice(childDiscountPrice);

		flightFare.setInfantMarkupPrice(infantMarkupPrice);
		flightFare.setInfantServiceChargePrice(infantServiceChargePrice);
		flightFare.setInfantDiscountPrice(infantDiscountPrice);

		flightFare.setMarkupPrice(adultMarkupPrice + childMarkupPrice + infantMarkupPrice);
		flightFare.setServiceChargePrice(adultServiceChargePrice + childServiceChargePrice + infantServiceChargePrice);
		flightFare.setDiscountPrice(adultDiscountPrice + childDiscountPrice + infantDiscountPrice);

		t3Price = t3Price + flightFare.getMarkupPrice() + flightFare.getServiceChargePrice();
		flightFare.setT3Price(t3Price);	
		return importPNRPricingResponseBean;
	}
	
	private static RulesFlightBean processRulesFlightBeanImportPnr(ImportPNRPricingResponseBean importPNRPricingResponseBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService) {
		FlightLegs fLegFirst = importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getFlightlegs().get(0);
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
		rulesFlightBean.setBaseFare(importPNRPricingResponseBean.getFlightFare().getTotalBaseFare());
		rulesFlightBean.setAirlineName(importPNRPricingResponseBean.getPlatingCarrierName());
		rulesFlightBean.setAirlineCode(importPNRPricingResponseBean.getPlatingCarrier());
		List<String> journeyType = new ArrayList<>();
		if(importPNRPricingResponseBean.getOptionSegmentBeanList().size()<=2){
			if(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry().equalsIgnoreCase(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getDestinationCountry())){
				journeyType.add("Domestic");
			} else if(!importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry().equalsIgnoreCase(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getDestinationCountry())){
				journeyType.add("International");
			} else {
				journeyType.add("Domestic");
				journeyType.add("International");
			}
		}else{
			if(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry().equalsIgnoreCase(importPNRPricingResponseBean.getOptionSegmentBeanList().get(importPNRPricingResponseBean.getOptionSegmentBeanList().size()-1).getDestinationCountry())){
				journeyType.add("Domestic");
			} else if(!importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry().equalsIgnoreCase(importPNRPricingResponseBean.getOptionSegmentBeanList().get(importPNRPricingResponseBean.getOptionSegmentBeanList().size()-1).getDestinationCountry())){
				journeyType.add("International");
			} else {
				journeyType.add("Domestic");
				journeyType.add("International");
			}
		}
		rulesFlightBean.setJourneyType(journeyType);
		
		List<String> airlineType = new ArrayList<>();
		if(importPNRPricingResponseBean.getPlatingAirlineType() == 1){
			airlineType.add("GDS");
		} else if(importPNRPricingResponseBean.getPlatingAirlineType() == 0){
			airlineType.add("LCC");
		} else {
			airlineType.add("GDS");
			airlineType.add("LCC");
		}
		rulesFlightBean.setAirlineType(airlineType);
		List<String> flightNo = new ArrayList<>();
		if(importPNRPricingResponseBean.isMultiCarrierForApplyRule()) {
			rulesFlightBean.setExcludeMultiCarrier("Yes");
		} else {
			rulesFlightBean.setExcludeMultiCarrier("");
		}
		rulesFlightBean.setFlightNo(flightNo);
		if(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getCabinClass()!=null && !("").equalsIgnoreCase(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getCabinClass()))
			rulesFlightBean.setCabinClass(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getCabinClass());//.toUpperCase()
		rulesFlightBean.setDealCode(importPNRPricingResponseBean.getFlightFare().getCorporateDealCode());
		
		rulesFlightBean.setItFares("No");
		rulesFlightBean.setImportPnr("Yes");
		rulesFlightBean.setCrossSellingPath("No");
		
		Integer originCountryId = importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry() !=null &&  !importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry().equals("") ? Integer.parseInt( importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getOriginCountry()) : 0;
        Integer destCountryId = importPNRPricingResponseBean.getOptionSegmentBeanList().get(importPNRPricingResponseBean.getOptionSegmentBeanList().size()-1).getDestinationCountry() !=null && !importPNRPricingResponseBean.getOptionSegmentBeanList().get(importPNRPricingResponseBean.getOptionSegmentBeanList().size()-1).getDestinationCountry().equals("") ? Integer.parseInt(importPNRPricingResponseBean.getOptionSegmentBeanList().get(importPNRPricingResponseBean.getOptionSegmentBeanList().size()-1).getDestinationCountry()) : 0;
        String originCountry = geoLocationService.getCountryNameById(originCountryId).getResultString();
        String destCountry = geoLocationService.getCountryNameById(destCountryId).getResultString();
        rulesFlightBean.setOriginCountry(originCountry);
        rulesFlightBean.setDestinationCountry(destCountry);
	
		int segSize = importPNRPricingResponseBean.getOptionSegmentBeanList().size();
		int legSize = importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getFlightlegs().size();
		
		rulesFlightBean.setOriginCity(fLegFirst.getOriginCityName());
		rulesFlightBean.setDestinationCity(importPNRPricingResponseBean.getOptionSegmentBeanList().get(segSize-1).getFlightlegs().get(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getFlightlegs().size() - 1).getDestinationCityName());

		rulesFlightBean.setOriginAirport(fLegFirst.getOrigin());
		rulesFlightBean.setDestinationAirport(importPNRPricingResponseBean.getOptionSegmentBeanList().get(segSize-1).getFlightlegs().get(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getFlightlegs().size() - 1).getDestination());

		
	
		List<Passenger> passengerList = importPNRPricingResponseBean.getPassengerList();
		int adults =0;
		int childs = 0;
		int infants = 0;
		for(Passenger p: passengerList){
			if(p.getType().equals("ADT"))
				adults++;
			if(p.getType().equals("CHD"))
				childs++;
			if(p.getType().equals("INF"))
				infants++;
		}
		
		rulesFlightBean.setNoOfPaxAdult(adults);
		rulesFlightBean.setNoOfPaxChild(childs);
		rulesFlightBean.setNoOfPaxInfant(infants);
		rulesFlightBean.setTripType(importPNRPricingResponseBean.getTripType());
		
		rulesFlightBean.setSupplier(importPNRPricingResponseBean.getServiceProvider());

		String travelStrtDateStr = importPNRPricingResponseBean.getOptionSegmentBeanList().get(0).getFlightlegs().get(0).getDepDate();
		Date travelStrtDate = CommonUtil.convertStringToDateIndianFormat(travelStrtDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelStartDate(travelStrtDate);

		String travelEndDateStr = importPNRPricingResponseBean.getOptionSegmentBeanList().get(segSize-1).getFlightlegs().get(legSize - 1).getArrDate();
		Date travelEndDate = CommonUtil.convertStringToDateIndianFormat(travelEndDateStr, "yyyy-MM-dd");
		rulesFlightBean.setTravelEndDate(travelEndDate);

		List<String> blackoutoutboundStrList = new ArrayList<>();
		
		int noOfDays = 0;
		if(importPNRPricingResponseBean.getOptionSegmentBeanList() != null && !importPNRPricingResponseBean.getOptionSegmentBeanList().isEmpty()) {
			for(OptionSegmentBean optionSegment : importPNRPricingResponseBean.getOptionSegmentBeanList()) {
				String deptDateStr = optionSegment.getFlightlegs().get(0).getDepDate();
				Date deptDate = CommonUtil.convertStringToDateIndianFormat(deptDateStr, "yyyy-MM-dd");
				
				String arrDateStr = optionSegment.getFlightlegs().get(optionSegment.getFlightlegs().size() - 1).getArrDate();
				Date arrDate = CommonUtil.convertStringToDateIndianFormat(arrDateStr, "yyyy-MM-dd");
				
				noOfDays+= CommonUtil.daysBetweenTwoDate(deptDate, arrDate) + 1;
				List<Date> blackoutoutboundDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(deptDate, arrDate);
				if(blackoutoutboundDateList!=null && !blackoutoutboundDateList.isEmpty()) {
					for(Date blackoutOutboundDate : blackoutoutboundDateList) {
						blackoutoutboundStrList.add(CommonUtil.convertDatetoString(blackoutOutboundDate, "yyyy-MM-dd"));
					}
				}
			}
		}
		rulesFlightBean.setBlackoutOutboundDateList(blackoutoutboundStrList);
		rulesFlightBean.setNoOfDays(noOfDays);
		
		String validDay = com.tt.ts.common.util.CommonUtil.changeDateToStrByFormat(travelStrtDate, "EEEE");
		rulesFlightBean.setValidDays(validDay);

		String credentialName = "";
		Integer credentialId = importPNRPricingResponseBean.getTicketingPCCId()!=null && !importPNRPricingResponseBean.getTicketingPCCId().isEmpty() ? Integer.parseInt(importPNRPricingResponseBean.getTicketingPCCId()) : 0;
		
		ResultBean resultBeanPcc = geoLocationService.fetchObjectById(credentialId, SupplierCredentialModal.class);
		if(resultBeanPcc!=null && !resultBeanPcc.isError() && resultBeanPcc.getResultObject()!=null){
			SupplierCredentialModal supplierCredentialModal = (SupplierCredentialModal)resultBeanPcc.getResultObject();
			credentialName = supplierCredentialModal.getCredentialName();
		}
		rulesFlightBean.setPcc(credentialName);

		String nationality = "";
		if (passengerList != null && !passengerList.isEmpty()) {
			nationality = passengerList.get(0).getNationality();
		}
		rulesFlightBean.setNationality(nationality);
	
		String rbd = "";
		for (OptionSegmentBean optionSegment : importPNRPricingResponseBean.getOptionSegmentBeanList()) {
			for (FlightLegs flightLeg : optionSegment.getFlightlegs()) {
				rbd += flightLeg.getBookingClass() + ",";
			}
		}
		rulesFlightBean.setRbd(rbd.length()>0 ?rbd.substring(0, rbd.length() - 1):rbd);

		Date bookingDate = new Date();
		
		rulesFlightBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
		rulesFlightBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
		if(importPNRPricingResponseBean.getFlightFare()!=null && importPNRPricingResponseBean.getFlightFare().getCurrency()!=null && importPNRPricingResponseBean.getSupplierCurrency()!=null && !importPNRPricingResponseBean.getFlightFare().getCurrency().equalsIgnoreCase(importPNRPricingResponseBean.getSupplierCurrency())) {
			rulesFlightBean.setForEx("Yes");
		}else {
			rulesFlightBean.setForEx("No");
		}
		return rulesFlightBean;
	}
	
	
	private static ImportPNRPricingResponseBean ruleApplyFlightOptionImportPnr(ImportPNRPricingResponseBean importPNRPricingResponseBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,
			String country, String branch, String agency,ProductService productService) {

		FlightFare flightFare = importPNRPricingResponseBean.getFlightFare();
		RulesCommonBean rulesCommonBean = new RulesCommonBean();

		//rulesCommonBean.setCountryId(country);
		if(agency!=null && !agency.isEmpty()) {
			rulesCommonBean.setAgencyId(agency);
		} else {
			rulesCommonBean.setBranchId(branch);
		}
		rulesCommonBean.setTicketingPeriod(com.tt.ts.common.util.CommonUtil.changeTime(new Date(), 0, 0, 0));
		RulesFlightBean rulesFlightBean = new RulesFlightBean();
	
		rulesFlightBean = processRulesFlightBeanImportPnr(importPNRPricingResponseBean, geoLocationService, credentialService);

		rulesCommonBean.setRulesFlightBean(rulesFlightBean);

		List<Passenger> passengerList = importPNRPricingResponseBean.getPassengerList();
		
		int adults = 0;
		int childs = 0;
		int infants = 0;
		
		for(Passenger p: passengerList){
			if(p.getType().equals("ADT"))
				adults++;
			if(p.getType().equals("CHD"))
				childs++;
			if(p.getType().equals("INF"))
				infants++;
		}
		
		double adultTotalFare = flightFare.getAdultBaseFare() +flightFare.getAdultFees() + flightFare.getAdultTax();
		if(adults > 0) {
			rulesCommonBean.setTotalFare(adultTotalFare/adults);
			rulesCommonBean.setBaseFareT(flightFare.getAdultBaseFare()/adults);
		}
		
		double childTotalFare = flightFare.getChildBaseFare() + flightFare.getChildFees() + flightFare.getChildTax();
		if(childs > 0) {
			rulesCommonBean.setTotalFareChild(childTotalFare/childs);
			rulesCommonBean.setBaseFareTChild(flightFare.getChildBaseFare()/childs);
		}
		
		double infantTotalFare = flightFare.getInfantBaseFare() + flightFare.getInfantFees() + flightFare.getInfantTax();
		if(infants > 0) {
			rulesCommonBean.setTotalFareInfant(infantTotalFare/infants);
			rulesCommonBean.setBaseFareTInfant(flightFare.getInfantBaseFare()/infants);
		}
		
		RulesCommonBean commonBean = null;
		rulesCommonBean.setRuleType("markup");
		if(KieSessionService.kieSessImportPnr != null) {
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessImportPnr);	
			}catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessImportPnr = null;
										KieSessionService.getKieSessionImportPnr();			
									}
									commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessImportPnr);
							} else {
								KieSessionService.kieSessImportPnr = null;
								CallLog.info(105, "No approved flight rules found.");
							}

						}
					}			
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}				
		}
		
		if (commonBean != null) {
			if (commonBean.getMarkupAdultList()!= null && !commonBean.getMarkupAdultList().isEmpty()) {
				double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
				flightFare.setAdultMarkupPrice(maxAdultMarkup);
				rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
			}
			if (commonBean.getMarkupChildList()!= null && !commonBean.getMarkupChildList().isEmpty()) {
				double maxChildMarkup = Collections.max(commonBean.getMarkupChildList());
				flightFare.setChildMarkupPrice(maxChildMarkup);
				rulesCommonBean.setUpdatedPriceChild(maxChildMarkup);
			}		
			if (commonBean.getMarkupInfantList()!= null && !commonBean.getMarkupInfantList().isEmpty()) {
				double maxInfantMarkup = Collections.max(commonBean.getMarkupInfantList());
				flightFare.setInfantMarkupPrice(maxInfantMarkup);
				rulesCommonBean.setUpdatedPriceInfant(maxInfantMarkup);
			}
		}
		rulesCommonBean.setRuleType("discOrSc");
		if(KieSessionService.kieSessImportPnr != null) {
			try {
				commonBean = fireRulesFlight(rulesCommonBean, KieSessionService.kieSessImportPnr);	
			} catch(IllegalStateException ex) {
				try{
					ProductModel productModel = new ProductModel();
					productModel.setStatus(1);
					productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

					com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
					if (resultBeanProduct != null && !resultBeanProduct.isError()) {
						List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
						if (productList != null && !productList.isEmpty()) {
							ProductModel productModal = productList.get(0);
							if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
									boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
									if (flagTextRule) {
										KieSessionService.kieSessImportPnr = null;
										KieSessionService.getKieSessionFlight();
										commonBean = fireRulesFlight(rulesCommonBean,KieSessionService.kieSessImportPnr);
									}
									
							} else {
								KieSessionService.kieSessImportPnr = null;
								CallLog.info(105, "No approved flight rules found.");
							}

						}
					}	
					
				}catch(Exception e) {
					CallLog.printStackTrace(105, e);
				}
			} catch(Exception ex) {
				CallLog.printStackTrace(105, ex);
			}	
		}
		if (commonBean != null) {		
			if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
				double maxAdultDiscount =  Collections.max(commonBean.getDiscountAdultList());
				flightFare.setAdultDiscountPrice(maxAdultDiscount);
			}
			if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
				double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
				flightFare.setAdultServiceChargePrice(maxAdultServiceCharge);
			}		
			if (commonBean.getDiscountChildList() != null && !commonBean.getDiscountChildList().isEmpty()) {
				double maxChildDiscount =  Collections.max(commonBean.getDiscountChildList());
				flightFare.setChildDiscountPrice(maxChildDiscount);
			}
			if (commonBean.getServiceChargeChildList() != null && !commonBean.getServiceChargeChildList().isEmpty()) {
				double maxChildServiceCharge = Collections.max(commonBean.getServiceChargeChildList());
				flightFare.setChildServiceChargePrice(maxChildServiceCharge);
			}
				
			if (commonBean.getDiscountInfantList() != null && !commonBean.getDiscountInfantList().isEmpty()) {
				double maxInfantDiscount =  Collections.max(commonBean.getDiscountInfantList());
				flightFare.setInfantDiscountPrice(maxInfantDiscount);
			}
			if (commonBean.getServiceChargeInfantList() != null && !commonBean.getServiceChargeInfantList().isEmpty()) {
				double maxInfantServiceCharge = Collections.max(commonBean.getServiceChargeInfantList());
				flightFare.setInfantServiceChargePrice(maxInfantServiceCharge);
			}
		}
		importPNRPricingResponseBean.setFlightFare(flightFare);
		return importPNRPricingResponseBean;
	}
	
	 private static ImportPNRPricingResponseBean setAirlineAirportFlightImportPnr(ImportPNRPricingResponseBean importPNRPricingResponseBean ,ResultBean resultBeanAirline,List<AirportModal> airportModals) {
		 if(resultBeanAirline != null && !resultBeanAirline.isError() && resultBeanAirline.getResultList()!= null && !resultBeanAirline.getResultList().isEmpty()) {
				for(AirlineModel airlineModel : (List<AirlineModel>)resultBeanAirline.getResultList()) {	
					if(importPNRPricingResponseBean.getPlatingCarrier() != null && airlineModel.getAirlineCode().equalsIgnoreCase(importPNRPricingResponseBean.getPlatingCarrier())) {
						importPNRPricingResponseBean.setPlatingCarrierName(airlineModel.getAirlineName());
						importPNRPricingResponseBean.setPlatingAirlineType(airlineModel.getAirlineType()!=null?airlineModel.getAirlineType():0);
						break;
					}
				}
			}
		 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(0),resultBeanAirline,airportModals);
		 
		 if(importPNRPricingResponseBean.getOptionSegmentBeanList().size()==2)
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(1),resultBeanAirline,airportModals);
		 if(importPNRPricingResponseBean.getOptionSegmentBeanList().size()==3){
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(1),resultBeanAirline,airportModals);
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(2),resultBeanAirline,airportModals);
		 }
		 if(importPNRPricingResponseBean.getOptionSegmentBeanList().size()==4){
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(1),resultBeanAirline,airportModals);
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(2),resultBeanAirline,airportModals);
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(3),resultBeanAirline,airportModals);
		 }
		 if(importPNRPricingResponseBean.getOptionSegmentBeanList().size()==5){
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(1),resultBeanAirline,airportModals);
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(2),resultBeanAirline,airportModals);
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(3),resultBeanAirline,airportModals);
			 setAirlineAirportInFlightLegs(importPNRPricingResponseBean.getOptionSegmentBeanList().get(4),resultBeanAirline,airportModals);
		 }
			 
	    return importPNRPricingResponseBean;
	}
	 public static RulesCommonBean fireRulesCar(RulesCommonBean rulesCommonBean,
				KieSession kieSessCar) {
		 try {
				List<RuleInterface> list = new ArrayList<>();
				list.add(rulesCommonBean);
				list.add(rulesCommonBean.getRulesCarBean());
	
				List<FactHandle> factHandleList = new ArrayList<>();
				kieSessCar.halt();
				for (RuleInterface RulesCommonBean : list) {
					factHandleList.add(kieSessCar.insert(RulesCommonBean));
				}
				kieSessCar.fireAllRules();
	
				if (!factHandleList.isEmpty()) {
					for (FactHandle factHandle : factHandleList) {
						kieSessCar.retract(factHandle);
					}
				}
		 }catch (Exception e) {
				CallLog.printStackTrace(105, e);
			}	
			return rulesCommonBean;
		}
	 public static RulesCommonBean fireRulesActivity(RulesCommonBean rulesCommonBean, KieSession kieSessActivity) {
			try {
			 	List<RuleInterface> list = new ArrayList<>();
				list.add(rulesCommonBean);
				list.add(rulesCommonBean.getRulesActivityBean());
	
				List<FactHandle> factHandleList = new ArrayList<>();
				kieSessActivity.halt();
				for (RuleInterface RulesCommonBean : list) {
					factHandleList.add(kieSessActivity.insert(RulesCommonBean));
				}
				kieSessActivity.fireAllRules();
	
				if (!factHandleList.isEmpty()) {
					for (FactHandle factHandle : factHandleList) {
						kieSessActivity.retract(factHandle);
					}
				}
			}catch (Exception e) {
				CallLog.printStackTrace(105, e);
			}
			return rulesCommonBean;
		}
}