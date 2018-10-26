package com.tt.ws.rest.air;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airline.service.AirlineService;
import com.tt.ts.blacklist.model.BlackOutFlightModel;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.flighttag.model.FlightTagModel;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.organization.service.OrganizationService;
import com.tt.ws.rest.air.service.FlightWSService;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.flight.bean.amadeus.fareconfirm.FareConfirmRequestBean;
import com.ws.services.flight.bean.amadeus.farerule.FareRuleRequestBean;
import com.ws.services.flight.bean.booking.Address;
import com.ws.services.flight.bean.booking.ContactInfo;
import com.ws.services.flight.bean.booking.FlightBookAncillaryServiceRequestBean;
import com.ws.services.flight.bean.booking.FlightBookingRequestBean;
import com.ws.services.flight.bean.booking.FlightBookingResponseBean;
import com.ws.services.flight.bean.booking.FlightCancelRequestBean;
import com.ws.services.flight.bean.booking.FlightCancelResponseBean;
import com.ws.services.flight.bean.booking.Passenger;
import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.FlightSearchRequestBean;
import com.ws.services.flight.bean.flightsearch.FlightSearchResponseBean;
import com.ws.services.flight.bean.flydubai.booking.FlyDubaiBookingRequestBean;
import com.ws.services.flight.bean.flydubai.booking.Payment;
import com.ws.services.flight.bean.flydubai.pnrsummary.Segment;
import com.ws.services.flight.bean.flydubai.pnrsummary.SpecialService;
import com.ws.services.flight.bean.flydubai.servicequote.CarrierCode;
import com.ws.services.flight.bean.flydubai.servicequote.RetrieveServiceQuotesRequestBean;
import com.ws.services.flight.bean.flydubai.servicequote.RetrieveServiceQuotesResponseBean;
import com.ws.services.flight.bean.flydubai.servicequote.ServiceQuote;
import com.ws.services.flight.bean.importpnr.ImportPNRIssueTicketRequestBean;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingRequestBean;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingResponseBean;
import com.ws.services.flight.bean.importpnr.ImportPNRResponseBean;
import com.ws.services.flight.bean.importpnr.ImportPNRServiceRequestBean;
import com.ws.services.flight.connector.util.ParamMappingUtil;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;
//import com.tt.ts.rest.common.util.MemCacheUtil;

@RestController
@RequestMapping("/airflight")
public class FlightWSController {

	private static final String ROUNDTRIP = ParamMappingUtil.TripType.RoundTrip.toString();
	private static final String ECONOMY_SMALL = "Economy";

	@Autowired
	MessageSource messageSource;

	@Autowired
	RedisService redisService;
	
	@Autowired
	FlightWSService flightWsService;
	
	@Autowired
	AirlineService airlineService;
	
	@Autowired
	OrganizationService orgService;
	
	//method for Redis Cache
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchAirFlights(@RequestBody FlightSearchRequestBean requestBean, HttpServletRequest request) {
		AirlineModel airlineModel =new AirlineModel();
		ResultBean resultBeanAirline = airlineService.getAirlinesNew(airlineModel);
		com.tt.ts.blacklist.model.BlackOutFlightModel blac=new com.tt.ts.blacklist.model.BlackOutFlightModel();
		blac.setStatus(1);
		blac.setApprovalStatus(1);
		com.tt.ts.common.modal.ResultBean resultBean1 = orgService.searchBlackOutFlight(blac,resultBeanAirline);
		List<BlackOutFlightModel> blackOutFlightModels = (List<BlackOutFlightModel>) resultBean1.getResultList();
	
		com.tt.ts.flighttag.model.FlightTagModel flighttag =new com.tt.ts.flighttag.model.FlightTagModel();
		flighttag.setStatus(1);
		flighttag.setApprovalStatus(1);
		com.tt.ts.common.modal.ResultBean resultBeanTag = orgService.searchFlightTag(flighttag,resultBeanAirline);
		List<FlightTagModel> tagFlightModalList= (List<FlightTagModel>) resultBeanTag.getResultList();
		
		com.tt.ts.common.modal.ResultBean resultBeanRBD = orgService.getRBDListCountryWise(Integer.parseInt(requestBean.getCountryId()));
		return flightWsService.responseForFlightSearchResult(requestBean,resultBeanAirline,blackOutFlightModels,tagFlightModalList,resultBeanRBD);
	}
	
		
    /* Method added for PG payment for flight booking ends */

	@RequestMapping(value = "/booking", method = RequestMethod.POST)
	public String bookingAirFlights(@RequestBody FlightBookingRequestBean bookingRequestBean) {
		String jsonString = null;

		CallLog.info(0, ProductEnum.Flight.search);

		FlyDubaiBookingRequestBean bean = new FlyDubaiBookingRequestBean();

		bean.setDisplayCurrency("AED");

		Address address = new Address();
		address.setAddress1("FZ HQ");
		address.setAddress2("FZ HQ");
		address.setAreaCode("4");
		address.setCity("DUBAI");
		address.setCountry("AE");
		address.setCountryCode("00971");
		address.setDisplay("na");
		address.setPhoneNumber("60337954");
		address.setPostal("66677");
		address.setState("DUBAI");

		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setAreaCode("4");
		contactInfo.setContactField("0097146033954");
		contactInfo.setContactID(-2141);
		contactInfo.setContactType("HomePhone");
		contactInfo.setCountryCode("00971");
		contactInfo.setDisplay("na");
		contactInfo.setExtension("");
		contactInfo.setPersonOrgID(-214);
		contactInfo.setPhoneNumber("6033954");
		contactInfo.setPreferredContactMethod(false);
		List<ContactInfo> contactInfos = new ArrayList<>();
		contactInfos.add(contactInfo);
		contactInfos.add(contactInfo);
		List<Passenger> passengerList = bookingRequestBean.getPassengerList();
		if (passengerList != null && !passengerList.isEmpty()) {
			for (Passenger passenger : passengerList) {
				passenger.setContactInfos(contactInfos);
				passenger.setAddress(address);
			}
		}
		bookingRequestBean.setPassengerList(passengerList);

		Payment payment = new Payment();
		payment.setBillingCountry("");
		payment.setCardHolder("");
		payment.setCardNum("");
		payment.setCardType("");
		payment.setCompanyName("Desh TEST");
		payment.setCVCode("");
		payment.setExchangeRate("1");
		payment.setExchangeRateDate("2013-02-12");
		payment.setExpirationDate("2014-02-12");
		payment.setFirstName("Desh");
		payment.setGcxID("1");
		payment.setGcxOpt("1");
		payment.setISOCurrency(1);
		payment.setLastName("TEST");
		payment.setOriginalAmount("364");
		payment.setOriginalCurrency("AED");
		payment.setPaymentAmount("364");
		payment.setPaymentComment("");
		payment.setPaymentCurrency("AED");
		payment.setPaymentMethod("INVC");
		payment.setReservationPaymentID(-2147483648);
		payment.setTACreditCard(false);
		payment.setVoucherNum(-2147483648);

		bean.setPayment(payment);
		bean.setPromoCode("na");

		bookingRequestBean.setFlyDubaiBookingRequestBean(bean);

		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		CallLog.info(0, ProductEnum.Flight.book);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.book.toString());
		bookingRequestBean.setPayLaterBooking(false);
		serviceRequestBean.setRequestBean(bookingRequestBean);
		serviceRequestBean.setServiceConfigModel(bookingRequestBean.getServiceConfigModel());

		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean flightBookResponseBean = serviceResolverFactory.getServiceResponse();
		if (flightBookResponseBean != null) {
			FlightBookingResponseBean bookingResponseBean = (FlightBookingResponseBean) flightBookResponseBean.getResponseBean();
			jsonString = CommonUtil.convertIntoJson(bookingResponseBean);
		}

		return jsonString;
	}

	@RequestMapping(value = "/bookAncillary", method = RequestMethod.POST)
	public String bookAncillary(@RequestBody FlightBookAncillaryServiceRequestBean requestBean) {
		String jsonString = "";
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();

		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.bookAncillary.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);

		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean != null) {
			FlightBookingResponseBean flightBookResponseBean = (FlightBookingResponseBean) serviceResponseBean.getResponseBean();
			jsonString = CommonUtil.convertIntoJson(flightBookResponseBean);
		}
		CallLog.info(0, "bookAncillary Response Json ::::: " + jsonString);
		return jsonString;
	}

	@RequestMapping(value = "/getSSRService", method = RequestMethod.POST)
	public String getSsrService(@RequestBody RetrieveServiceQuotesRequestBean retrieveServiceQuotesRequestBean) {
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;

		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		CallLog.info(4, "\nTime taken by [TestFlight] getServiceResponse() " + (endTime - startTime) + " MiliSeconds");

		FlightOption onwardFlightOption = retrieveServiceQuotesRequestBean.getOnwardFlightOption();
		FlightOption returnFlightOption = retrieveServiceQuotesRequestBean.getReturnFlightOption();

		List<ServiceQuote> serviceQuotes = new ArrayList<>();

		if (null != onwardFlightOption && onwardFlightOption.getServiceVendor().equalsIgnoreCase("FlyDubai")) {
			ServiceQuote serviceQuote = new ServiceQuote();

			CallLog.info(4, "Onward Case =  " + onwardFlightOption.getFlyDubaiFlightOptionDetails().getSecurityToken());

			retrieveServiceQuotesRequestBean.setSecurityGUID(onwardFlightOption.getFlyDubaiFlightOptionDetails().getSecurityToken());
			serviceQuote.setAirportCode(onwardFlightOption.getOrigin());
			serviceQuote.setCabin(onwardFlightOption.getFlightFare().getCabinClass());
			serviceQuote.setCategory("");
			serviceQuote.setCurrency(onwardFlightOption.getFlightFare().getCurrency());
			serviceQuote.setDepartureDate(retrieveServiceQuotesRequestBean.getOnwardDate());
			serviceQuote.setDestinationAirportCode(onwardFlightOption.getDestination());
			serviceQuote.setFareBasisCode("");
			serviceQuote.setFareClass("");
			serviceQuote.setLogicalFlightID(Integer.parseInt(onwardFlightOption.getFlyDubaiFlightOptionDetails().getLfid()));
			serviceQuote.setMarketingCarrierCode("FZ");
			serviceQuote.setOperatingCarrierCode("FZ");
			serviceQuote.setReservationChannel("TPAPI");
			serviceQuote.setServiceCode("");
			serviceQuote.setUTCOffset(0);
			serviceQuotes.add(serviceQuote);
		}

		if (null != returnFlightOption && returnFlightOption.getServiceVendor().equalsIgnoreCase("FlyDubai")) {
			ServiceQuote serviceQuote1 = new ServiceQuote();

			retrieveServiceQuotesRequestBean.setSecurityGUID(returnFlightOption.getFlyDubaiFlightOptionDetails().getSecurityToken());
			serviceQuote1.setAirportCode(returnFlightOption.getOrigin());
			serviceQuote1.setCabin(returnFlightOption.getFlightFare().getCabinClass());
			serviceQuote1.setCategory("");
			serviceQuote1.setCurrency(returnFlightOption.getFlightFare().getCurrency());
			serviceQuote1.setDepartureDate(retrieveServiceQuotesRequestBean.getReturnDate());
			serviceQuote1.setDestinationAirportCode(returnFlightOption.getDestination());
			serviceQuote1.setFareBasisCode("");
			serviceQuote1.setFareClass("");
			serviceQuote1.setLogicalFlightID(Integer.parseInt(returnFlightOption.getFlyDubaiFlightOptionDetails().getLfid()));
			serviceQuote1.setMarketingCarrierCode("FZ");
			serviceQuote1.setOperatingCarrierCode("FZ");
			serviceQuote1.setReservationChannel("TPAPI");
			serviceQuote1.setServiceCode("");
			serviceQuote1.setUTCOffset(0);
			serviceQuotes.add(serviceQuote1);
		}
		CarrierCode carrierCode = new CarrierCode();
		carrierCode.setAccessibleCarrierCode("FZ");
		List<CarrierCode> carrieList = new ArrayList<>();
		carrieList.add(carrierCode);
		retrieveServiceQuotesRequestBean.setCarrierCodes(carrieList);

		retrieveServiceQuotesRequestBean.setRetrieveServiceQuotes(serviceQuotes);

		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();

		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.srvicequote.toString());

		serviceRequestBean.setRequestBean(retrieveServiceQuotesRequestBean);
		serviceRequestBean.setServiceConfigModel(retrieveServiceQuotesRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);

		/**
		 * Will return the bean containing corresponding service response bean,
		 * response status and error message
		 *
		 */
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			RetrieveServiceQuotesResponseBean responseBean = (RetrieveServiceQuotesResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}
		return jsonString;
	}

	@RequestMapping(value = "/fareConfirmation", method = RequestMethod.POST)
	public String fareConfirmation(@RequestBody FareConfirmRequestBean fareConfirmRequestBean) {
		return flightWsService.fareConfirmation(fareConfirmRequestBean);
	}

	@RequestMapping(value = "/fareRuleCall", method = RequestMethod.POST)
	public String fareRuleCall(@RequestBody FareRuleRequestBean fareRuleRequestBean) {
		return flightWsService.fareRuleCall(fareRuleRequestBean);
	}

	@RequestMapping(value = "/pnrTicketCancel", method = RequestMethod.POST)
	public String pnrTicketCancel(@RequestBody FlightCancelRequestBean flightCancelRequestBean) {
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.cancelTicket.toString());
		serviceRequestBean.setRequestBean(flightCancelRequestBean);
		serviceRequestBean.setServiceConfigModel(flightCancelRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		/*
		 * //** Will return the bean containing corresponding service response
		 * bean, response status and error message
		 */
		serviceResponseBean = serviceResolverFactory.getServiceResponse();

		if (serviceResponseBean.getResponseBean() != null) {
			FlightCancelResponseBean responseBean = (FlightCancelResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}

		return jsonString;
	}

	@RequestMapping(value = "/holdandPayLaterbooking")
	public String holdandPayLaterbookingAirFlights() {
		ObjectMapper mapper;
		String jsonString = null;

		FlightSearchRequestBean requestBean = new FlightSearchRequestBean();
		List<String> prefAirLineList = new ArrayList<>();
		prefAirLineList.add("All");// for spice
		requestBean.setOrigin("DEL");
		requestBean.setDestination("BOM");
		requestBean.setCabinClass(ECONOMY_SMALL); // Economy - E
		requestBean.setOnwardDate("2017-01-25");
		requestBean.setReturnDate("2017-01-27");
		requestBean.setTripType(ROUNDTRIP); // OneWay // RoundTrip //
		// SpecialRoundtrip : BL
		// onward // return // onward-return : SPICE
		requestBean.setDomOrInt("Domestic");
		requestBean.setNoOfAdults(1);
		requestBean.setNoOfChilds(0);
		requestBean.setNoOfInfants(0);
		requestBean.setPrefAirlines(prefAirLineList);
		requestBean.setClientTxnId("10000255");
		// requestBean.setLccSearch(false);
		// requestBean.setGDSSearch(false);
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.search.toString());

		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);

		long startTime = System.currentTimeMillis();
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		long endTime = System.currentTimeMillis();
		CallLog.info(0, "\nTime taken by [TestFlight] getServiceResponse() " + (endTime - startTime) + " MiliSeconds");

		CallLog.info(0, ">>>>>>>>>>>> Response Status = " + serviceResponseBean.getResponseStatus().toString());
		CallLog.info(0, ">>>>>>>>>>>> Error Message = " + serviceResponseBean.getErrorMsg());
		if (serviceResponseBean.getResponseBean() != null) {
			try {
				FlightSearchResponseBean flightSearchResponseBean = (FlightSearchResponseBean) serviceResponseBean.getResponseBean();
				if (flightSearchResponseBean != null) {
					CallLog.info(0, "OnwardFlightOption:::::: " + flightSearchResponseBean.toString());
					CallLog.info(0, "size of list of available onward flight options === " + flightSearchResponseBean.getOnwardFlightOptions().size());
					CallLog.info(0, "size of list of available return flight options === " + flightSearchResponseBean.getReturnFlightOptions().size());
					List<FlightOption> onwardFlightOptionList = flightSearchResponseBean.getOnwardFlightOptions();
					List<FlightOption> returnFlightOptionList = flightSearchResponseBean.getReturnFlightOptions();

					FlightOption flyDubaiOnwardFlightOption = null;
					FlightOption flyDubaiRetrunFlightOption = null;
					if (onwardFlightOptionList != null && !onwardFlightOptionList.isEmpty()) {
						flyDubaiOnwardFlightOption = onwardFlightOptionList.get(0);
					}
					if (returnFlightOptionList != null && !returnFlightOptionList.isEmpty()) {
						flyDubaiRetrunFlightOption = returnFlightOptionList.get(0);
					}
					FlightBookingRequestBean bookingRequestBean = new FlightBookingRequestBean();

					bookingRequestBean.setOrigin("DEL");
					bookingRequestBean.setDestination("BOM");
					bookingRequestBean.setCabinClass(ECONOMY_SMALL); // Economy
					// - E
					bookingRequestBean.setOnwardDate("2017-01-25");
					bookingRequestBean.setReturnDate("2017-01-27");
					bookingRequestBean.setTripType(ROUNDTRIP); // OneWay //
					// RoundTrip //
					// SpecialRoundtrip
					// : BL
					// onward // return // onward-return : SPICE
					bookingRequestBean.setDomOrInt("Domestic");
					bookingRequestBean.setNoOfAdults(1);
					bookingRequestBean.setNoOfChilds(0);
					bookingRequestBean.setNoOfInfants(0);
					bookingRequestBean.setOnwardFlightOption(flyDubaiOnwardFlightOption);
					bookingRequestBean.setReturnFlightOption(flyDubaiRetrunFlightOption);

					// FlyDubai specific request bean

					FlyDubaiBookingRequestBean bean = new FlyDubaiBookingRequestBean();

					bean.setDisplayCurrency("AED");

					Address address = new Address();
					address.setAddress1("FZ HQ");
					address.setAddress2("FZ HQ");
					address.setAreaCode("4");
					address.setCity("DUBAI");
					address.setCountry("AE");
					address.setCountryCode("00971");
					address.setDisplay("na");
					address.setPhoneNumber("60337954");
					address.setPostal("66677");
					address.setState("DUBAI");

					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setAreaCode("4");
					contactInfo.setContactField("0097146033954");
					contactInfo.setContactID(-2141);
					contactInfo.setContactType("HomePhone");
					contactInfo.setCountryCode("00971");
					contactInfo.setDisplay("na");
					contactInfo.setExtension("");
					contactInfo.setPersonOrgID(-214);
					contactInfo.setPhoneNumber("6033954");
					contactInfo.setPreferredContactMethod(false);

					List<ContactInfo> contactInfos = new ArrayList<>();

					// Passenger
					Passenger passenger = new Passenger();
					passenger.setAge(26);
					passenger.setDob("1989-09-16");
					passenger.setTitle("MR");
					passenger.setFirstName("Desh");
					passenger.setLastName("TEST");
					passenger.setMiddleName("");
					passenger.setGender("Male");
					passenger.setType("ADT");
					passenger.setLeadPassenger(true);

					contactInfos.add(contactInfo);
					passenger.setContactInfos(contactInfos);
					passenger.setAddress(address);

					Payment payment = new Payment();
					payment.setBillingCountry("");
					payment.setCardHolder("");
					payment.setCardNum("");
					payment.setCardType("");
					payment.setCompanyName("Desh TEST");
					payment.setCVCode("");
					payment.setExchangeRate("1");
					payment.setExchangeRateDate("2013-02-12");
					payment.setExpirationDate("2014-02-12");
					payment.setFirstName("Desh");
					payment.setGcxID("1");
					payment.setGcxOpt("1");
					payment.setISOCurrency(1);
					payment.setLastName("TEST");
					payment.setOriginalAmount("364");
					payment.setOriginalCurrency("AED");
					payment.setPaymentAmount("364");
					payment.setPaymentComment("");
					payment.setPaymentCurrency("AED");
					payment.setPaymentMethod("INVC");
					payment.setReservationPaymentID(-2147483648);
					payment.setTACreditCard(false);
					payment.setVoucherNum(-2147483648);

					bean.setPayment(payment);
					bean.setPromoCode("na");

					SpecialService specialServices = new SpecialService();
					specialServices.setAmount("40");
					specialServices.setChargeComment("BAGB");
					specialServices.setCodeType("BAGB");
					specialServices.setCommissionable(false);
					specialServices.setCurrencyCode("AED");
					specialServices.setDepartureDate("2016-11-16T00:00:00");
					specialServices.setExtIsRePriceFixed("");
					specialServices.setExtPenaltyRule("");
					specialServices.setExtRePriceSourceName("");
					specialServices.setExtRePriceValue("");
					specialServices.setExtRePriceValueReason("");
					specialServices.setLogicalFlightID(9570626);
					specialServices.setOverrideAmount(true);
					specialServices.setOverrideAmtReason("");
					specialServices.setPersonOrgID(-214);
					specialServices.setPhysicalFlightID(-214);
					specialServices.setRefundable(true);
					specialServices.setServiceID(0);
					specialServices.setSSRCategory(99);

					List<SpecialService> speServices = new ArrayList<>();
					speServices.add(specialServices);
					Segment segement = new Segment();
					segement.setSpecialServices(speServices);

					// Passenger List
					List<Passenger> passengerList = new ArrayList<>();
					passengerList.add(passenger);
					// set passenger list into bean
					bookingRequestBean.setPassengerList(passengerList);
					bookingRequestBean.setFlyDubaiBookingRequestBean(bean);
					CallLog.info(0, ProductEnum.Flight.book);
					serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
					serviceRequestBean.setServiceName(ProductEnum.Flight.book.toString());
					bookingRequestBean.setPayLaterBooking(true);
					serviceRequestBean.setRequestBean(bookingRequestBean);
					serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
					ServiceResponseBean flightBookResponseBean = serviceResolverFactory.getServiceResponse();
					if (flightBookResponseBean != null) {
						FlightBookingResponseBean bookingResponseBean = (FlightBookingResponseBean) flightBookResponseBean.getResponseBean();
						if (bookingRequestBean.isPayLaterBooking()) {
							CallLog.info(0, "Inside block to process payment for hold booking");
							serviceRequestBean.setRequestBean(bookingRequestBean);
							serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
							serviceRequestBean.setServiceName(ProductEnum.Flight.paylaterbooking.toString());
							bookingRequestBean.setPayLaterBooking(true);
							flyDubaiOnwardFlightOption.setAirlinePNR(bookingResponseBean.getOnwardFlightBooking().getAirlinePNR());
							bookingRequestBean.setOnwardFlightOption(flyDubaiOnwardFlightOption);
							bookingRequestBean.setReturnFlightOption(flyDubaiRetrunFlightOption);
							serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
							flightBookResponseBean = serviceResolverFactory.getServiceResponse();
							bookingResponseBean = (FlightBookingResponseBean) flightBookResponseBean.getResponseBean();

						}
						CallLog.info(0, "Flight Airline PNR ===>" + bookingResponseBean.getOnwardFlightBooking().getAirlinePNR());
						mapper = new ObjectMapper();
						jsonString = mapper.writeValueAsString(bookingResponseBean);

					}
				}
			} catch (Exception e) {
				TTLog.printStackTrace(0, e);
			}
		}
		return jsonString;
	}

	@RequestMapping(value = "/importPnrSearch", method = RequestMethod.POST)
	public String importPnrSearch(@RequestBody ImportPNRServiceRequestBean importPNRServiceRequestBean) {
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.importPNR.toString());

		serviceRequestBean.setRequestBean(importPNRServiceRequestBean);
		serviceRequestBean.setServiceConfigModel(importPNRServiceRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		/*
		 * //** Will return the bean containing corresponding service response
		 * bean, response status and error message
		 */
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			ImportPNRResponseBean responseBean = (ImportPNRResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}
		return jsonString;
	}
	
	@RequestMapping(value = "/importPnrPrice", method = RequestMethod.POST)
	public String importPnrPrice(@RequestBody ImportPNRPricingRequestBean importPNRPricingRequestBean) {
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.importPNRPricing.toString());

		serviceRequestBean.setRequestBean(importPNRPricingRequestBean);
		serviceRequestBean.setServiceConfigModel(importPNRPricingRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		/*
		 * //** Will return the bean containing corresponding service response
		 * bean, response status and error message
		 */
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			ImportPNRPricingResponseBean responseBean = (ImportPNRPricingResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}
		return jsonString;
	}
	
	
	@RequestMapping(value = "/bookingImportPnr", method = RequestMethod.POST)
	public String bookingImportPnr(@RequestBody ImportPNRIssueTicketRequestBean importPNRIssueTicketRequestBean) {
		String jsonString = null;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		CallLog.info(0, ProductEnum.Flight.issueTicketImportPNR);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.issueTicketImportPNR.toString());
		serviceRequestBean.setServiceConfigModel(importPNRIssueTicketRequestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(importPNRIssueTicketRequestBean);
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean flightBookResponseBean = serviceResolverFactory.getServiceResponse();
		if (flightBookResponseBean != null) {
			FlightBookingResponseBean bookingResponseBean = (FlightBookingResponseBean) flightBookResponseBean.getResponseBean();
			jsonString = CommonUtil.convertIntoJson(bookingResponseBean);
		}

		return jsonString;
	}

}
