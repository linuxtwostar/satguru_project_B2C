package com.tt.ts.rest.ruleengine.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.QueryConstant;
import com.tt.nc.common.util.TTLog;
import com.tt.ts.airline.service.AirlineService;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.common.util.CommonUtil;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.common.util.GenericHelperUtil;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.insurance.model.InsuranceWidget;
import com.tt.ts.rest.organization.service.OrganizationService;
import com.tt.ts.rest.ruleengine.util.RuleSimulationHelper;
import com.tt.ts.ruleengine.model.activity.RulesActivityBean;
import com.tt.ts.ruleengine.model.airline.RulesCommonBean;
import com.tt.ts.ruleengine.model.car.RulesCarBean;
import com.tt.ts.ruleengine.model.hotel.RulesHotelBean;
import com.tt.ts.ruleengine.service.SimulationRuleService;
import com.tt.ts.suppliercredential.service.SupplierCredentialService;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.tt.ws.rest.hotel.bean.HotelsFareRuleRespBean;
import com.ws.services.activities.bean.ActivitiesBean;
import com.ws.services.activities.bean.ActivitiesSearchRequestBean;
import com.ws.services.activities.bean.ActivitiesSearchResponseBean;
import com.ws.services.carbooking.bean.CarRentalInfoBean;
import com.ws.services.carbooking.bean.CarRentalSearchRespBean;
import com.ws.services.flight.bean.booking.Passenger;
import com.ws.services.flight.bean.flightsearch.FlightCalendarOption;
import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.FlightSearchRequestBean;
import com.ws.services.flight.bean.flightsearch.RoundTripFlightOption;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingResponseBean;
import com.ws.services.insurance.bean.product.FProduct;
import com.ws.services.insurance.bean.product.ProductPriceRequestBean;
import com.ws.services.util.CallLog;

@Service
public class RuleSimulationService {
	private static final String ROUNDTRIP = "RoundTrip";
	private static final String MULTICITY = "MultiCity";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	@Autowired
	SimulationRuleService simuRuleService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrganizationService orgService;
	
	@Autowired
	private AirlineService airlineService;
	
	private RuleSimulationService() {

	}
	
	public static ResultBean applyRuleOnOnwardFlight(List<FlightOption> flightOptionList, FlightSearchRequestBean flightSearchReqBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,String pccId,ProductService productService) {
		ResultBean resultBean = new ResultBean();
		try {
			Long startTime = System.currentTimeMillis();
			if (flightSearchReqBean != null && flightSearchReqBean.getTripType().equalsIgnoreCase(MULTICITY)) {
				List<FlightOption> flightOptionListUpdated = RuleSimulationHelper.processMutliCityFlightoption(flightOptionList, flightSearchReqBean, geoLocationService, credentialService,resultBeanAirline,airportModals,pccId,productService);	
				resultBean.setResultList(flightOptionListUpdated);
			} else {
				List<FlightOption> flightOptionListUpdated = RuleSimulationHelper.processFlightoption(flightOptionList, flightSearchReqBean, geoLocationService, credentialService,resultBeanAirline,airportModals,pccId,productService);
				resultBean.setResultList(flightOptionListUpdated);
			}

			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(100, "Time taken in applying RULE/TagFlight(ONEWAY/MULTICITY) ---->>>" + timeTaken);

		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(101, e);
		}
		return resultBean;
	}

	public static ResultBean applyRuleOnRoundTripFlight(List<RoundTripFlightOption> flightOptionList, FlightSearchRequestBean flightSearchReqBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,String pccId,ProductService productService) {

		ResultBean resultBean = new ResultBean();
		try {
			Long startTime = System.currentTimeMillis();
			if (flightSearchReqBean != null && flightSearchReqBean.getTripType().equalsIgnoreCase(ROUNDTRIP)) {
				List<RoundTripFlightOption> flightOptionListUpdated = RuleSimulationHelper.processRoundTripFlightOption(flightOptionList, flightSearchReqBean, geoLocationService, credentialService,resultBeanAirline,airportModals,pccId,productService);
				resultBean.setResultList(flightOptionListUpdated);
			}
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(100, "Time taken in applying RULE/TagFlight(ROUNDTRIP) ---->>>" + timeTaken);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(101, e);
		}
		return resultBean;
	}

	public static ResultBean applyRuleCalenderFlight(FlightCalendarOption flightCalenderOption, FlightSearchRequestBean flightSearchReqBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,ProductService productService) {
		ResultBean resultBean = new ResultBean();
		
		try {
			FlightCalendarOption flightCalenderOptionUpdated = RuleSimulationHelper.processCalenderFlight(flightCalenderOption, flightSearchReqBean, geoLocationService, credentialService,resultBeanAirline,airportModals,productService);			
			resultBean.setResultObject(flightCalenderOptionUpdated);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(101, e);
		}
		return resultBean;
	}
	
	   public static ResultBean applyRuleOnInsurance(List<FProduct> fProduct, ProductPriceRequestBean requestBean, GeoLocationService geoLocationService,CorporateService corporateService,ProductService productService) {
	       
		   ResultBean resultBean = new ResultBean();
			try {
				Long startTime = System.currentTimeMillis();
				List<FProduct> insuranceOptionListUpdated = RuleSimulationHelper.processInsuranceOption(fProduct, requestBean, geoLocationService, corporateService,productService);		
				if(KieSessionService.kieSessInsurance != null) {
					KieSessionService.kieSessInsurance.dispose();
				}
				resultBean.setResultList(insuranceOptionListUpdated);

				Long endTime = System.currentTimeMillis();
				Long timeTaken = endTime - startTime;
				CallLog.info(100, "Time taken in applying RULE/ Insurance) ---->>>" + timeTaken);
				
			} catch (Exception e) {
				resultBean.setIserror(true);
				CallLog.printStackTrace(101, e);
			}
			return resultBean;
	   }
	
	public ResultBean applyRuleOnInsurance1(List<FProduct> fProduct, InsuranceWidget insuranceWidget, SimulationRuleService simulationRuleService, OrganizationService organizationService, List<Object> list, String countryId, String branchId,
			String agencyId, GeoLocationService geoLocationService,ProductService productService) {
		ResultBean resultBean = new ResultBean();
		try {
			if (insuranceWidget != null) {
				List<FProduct> flightOptionListUpdated = RuleSimulationHelper.processInsuranceOption1(fProduct, insuranceWidget, simulationRuleService, organizationService, list, countryId, branchId, agencyId, geoLocationService,productService);
				resultBean.setResultObject(flightOptionListUpdated);
			}
		} catch (Exception e) {
			resultBean.setIserror(true);
			TTLog.printStackTrace(0, e);
		}
		return resultBean;
	}

	public String getApplyRuleHotelResult(HotelSearchRespDataBean hotelCommonJsonModel) {
		String jsonString;
		List<HotelsFareRuleRespBean> respList;
		GenericHelperUtil<HotelSearchRespDataBean> genHelper;
		String countryId = hotelCommonJsonModel.getCountryId();
		String agencyId = hotelCommonJsonModel.getAgencyId();
		String branchId = hotelCommonJsonModel.getBranchId();
		genHelper = new GenericHelperUtil<>();
		respList = hotelCommonJsonModel.getRespList();
		if (null != respList && !respList.isEmpty()) {
			for (int i = 0; i < respList.size(); i++) {
				RulesCommonBean rulesCommonBean = new RulesCommonBean();
				//rulesCommonBean.setCountryId(countryId);
				if(agencyId!=null && !agencyId.isEmpty()) {
					rulesCommonBean.setAgencyId(agencyId);
				} else {
					rulesCommonBean.setBranchId(branchId);
				}
				rulesCommonBean.setTicketingPeriod(new Date());
				
				RulesHotelBean rulesHotelBean = new RulesHotelBean();
				rulesHotelBean.setHotelName(respList.get(i).getHotelName());
				rulesHotelBean.setHotelcode(respList.get(i).getHotelCode());
				rulesHotelBean.setNoOfPax(respList.get(i).getNoOfPax());
				rulesHotelBean.setNoOfRooms(respList.get(i).getNoOfRooms());
				if (null != respList.get(i).getHotelGroup() && !"".equalsIgnoreCase(respList.get(i).getHotelGroup()))
					rulesHotelBean.setHotelGroup(respList.get(i).getHotelGroup());
				else
					rulesHotelBean.setHotelGroup("");
				rulesHotelBean.setStarRating(Integer.parseInt(respList.get(i).getStarRatingCode()));
				rulesHotelBean.setDestinationCountry(respList.get(i).getDestinationCountry());
				rulesHotelBean.setDestinationCity(respList.get(i).getDestinationCity());
				rulesHotelBean.setBookingStartDate(CommonUtil.convertStringToDate(respList.get(i).getBookingStartDate(), DATE_FORMAT));
				rulesHotelBean.setBookingEndDate(CommonUtil.convertStringToDate(respList.get(i).getBookingEndDate(), DATE_FORMAT));
				if (null != respList.get(i).getBlackoutStartDate() && !"".equalsIgnoreCase(respList.get(i).getBlackoutStartDate()))
					rulesHotelBean.setBlackoutStartDate(CommonUtil.convertStringToDate(respList.get(i).getBlackoutStartDate(), DATE_FORMAT));
				if (null != respList.get(i).getBlackoutEndDate() && !"".equalsIgnoreCase(respList.get(i).getBlackoutEndDate()))
					rulesHotelBean.setBlackoutEndDate(CommonUtil.convertStringToDate(respList.get(i).getBlackoutEndDate(), DATE_FORMAT));

				rulesHotelBean.setNationality(respList.get(i).getNationality());
				if (null != respList.get(i).getRoomType() && !"".equalsIgnoreCase(respList.get(i).getRoomType()))
					rulesHotelBean.setRoomType(respList.get(i).getRoomType());
				if (null != respList.get(i).getRatePlanType() && !"".equalsIgnoreCase(respList.get(i).getRatePlanType()))
					rulesHotelBean.setRatePlanType(respList.get(i).getRatePlanType());

				rulesCommonBean.setRulesHotelBean(rulesHotelBean);

				rulesCommonBean.setTotalFare(respList.get(i).getFareFromTo());
				rulesCommonBean.setBaseFareT(0.00);
				ResultBean resultBean = simuRuleService.getT3PriceByApplyRules(rulesCommonBean, QueryConstant.RULE_GROUP_TWO);
				RulesCommonBean commonBean = (RulesCommonBean) resultBean.getResultObject();
				if (commonBean != null && commonBean.getUpdatedPrice() > 0) {
					respList.get(i).setT3Price(commonBean.getUpdatedPrice());
				}
				if (commonBean != null && commonBean.getDiscountPrice() > 0) {
					respList.get(i).setDiscountPrice(commonBean.getDiscountPrice());
				}
				if (commonBean != null && commonBean.getServiceChargePrice() > 0) {
					respList.get(i).setServiceChargePrice(commonBean.getServiceChargePrice());
				}
				hotelCommonJsonModel.setRespList(respList);
			}
		}
		jsonString = genHelper.getJsonStringByEntity(hotelCommonJsonModel);

		return jsonString;

	}

	public HotelSearchRespDataBean applyRuleOnHotelResult(HotelSearchRespDataBean hotelSearchResp,KieSession kieSessHotel,String countryId,String agencyId,String branchId,String corpId,String custNationality) 
	{
		List<HotelsFareRuleRespBean> respList;
		respList = hotelSearchResp.getRespList();
		
		if (null != respList && !respList.isEmpty()) 
		{
			for (int i = 0; i < respList.size(); i++) 
			{
				try 
				{
					if(null != respList.get(i).getFareFromTo())
					{
						float starRating=0;
						RulesCommonBean rulesCommonBean = new RulesCommonBean();
						if(agencyId!=null && !agencyId.isEmpty()) {
							rulesCommonBean.setAgencyId(agencyId);
						} else {
							rulesCommonBean.setBranchId(branchId);
						}
						rulesCommonBean.setTicketingPeriod(new Date());
						RulesHotelBean rulesHotelBean = new RulesHotelBean();
						if(agencyId == null || agencyId.isEmpty()) {
							String customerType = "";
							boolean condition = corpId!= null && !"null".equals(corpId) && Integer.valueOf(corpId) > 0;
								if(condition) {
									customerType = "Corporate";
									} else {
									customerType = "Retail";
								}
								
								rulesHotelBean.setCustomerType(customerType); 
						}
						
						
						if(null != corpId && !"".equalsIgnoreCase(corpId)){
							rulesHotelBean.setCorporateId(corpId);
						}
						rulesHotelBean.setHotelName(respList.get(i).getHotelName());
						rulesHotelBean.setHotelcode(respList.get(i).getHotelCode());
						rulesHotelBean.setNoOfPax(respList.get(i).getNoOfPax());
						rulesHotelBean.setNoOfRooms(respList.get(i).getNoOfRooms());
						rulesHotelBean.setNoOfNights(respList.get(i).getDuration());
						if (null != respList.get(i).getHotelGroup() && !"".equalsIgnoreCase(respList.get(i).getHotelGroup()))
							rulesHotelBean.setHotelGroup(respList.get(i).getHotelGroup());
						else
							rulesHotelBean.setHotelGroup("");
						if(null!=respList.get(i).getStarRatingCode())
						{
							if(respList.get(i).getStarRatingCode().matches("\\d*\\.?\\d+")) 
								starRating=Float.parseFloat(respList.get(i).getStarRatingCode());
							else
							{
								if(respList.get(i).getStarRatingCode().toLowerCase().contains("one"))
									starRating=1;
								else if(respList.get(i).getStarRatingCode().toLowerCase().contains("two"))
									starRating=2;
								else if(respList.get(i).getStarRatingCode().toLowerCase().contains("three"))
									starRating=3;
								else if(respList.get(i).getStarRatingCode().toLowerCase().contains("four"))
									starRating=4;
								else if(respList.get(i).getStarRatingCode().toLowerCase().contains("five"))
									starRating=5;
								else if(respList.get(i).getStarRatingCode().toLowerCase().contains("six"))
									starRating=6;
								else if(respList.get(i).getStarRatingCode().toLowerCase().contains("seven"))
									starRating=7;
								else if(respList.get(i).getStarRatingCode().equalsIgnoreCase("APARTHOTEL")) //APARTHOTEL
									starRating=0;
								else
									starRating=0;
							}
						}
						rulesHotelBean.setStarRating(starRating);
						rulesHotelBean.setDestinationCountry(respList.get(i).getDestinationCountry());
						rulesHotelBean.setDestinationCity(respList.get(i).getDestinationCity());
						rulesHotelBean.setFareFromTo(respList.get(i).getFareFromTo());
						if(null != respList.get(i).getSupplierCode() && !respList.get(i).getSupplierCode().isEmpty() && "GTA,GDI".equalsIgnoreCase(respList.get(i).getSupplierCode()))
							rulesHotelBean.setSupplier("GDI");
						else
							rulesHotelBean.setSupplier(respList.get(i).getSupplierCode());
						Date bookingDate = new Date();
						rulesHotelBean.setBookingStartDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
						rulesHotelBean.setBookingEndDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
						
						if (null != respList.get(i).getBookingStartDate() && !"".equalsIgnoreCase(respList.get(i).getBookingStartDate()) && null != respList.get(i).getBookingEndDate() && !"".equalsIgnoreCase(respList.get(i).getBookingEndDate()))
						{
							List<String> blackOutHotelStrList = new ArrayList<>();
							Date blackOutHotelFromDate = CommonUtil.convertStringToDateIndianFormat(respList.get(i).getBookingStartDate(), "dd/MM/yyyy");
							Date blackOutHotelEndDate = CommonUtil.convertStringToDateIndianFormat(respList.get(i).getBookingEndDate(), "dd/MM/yyyy");
							List<Date> blackOutHotelDateList = com.tt.ts.common.util.CommonUtil.getListOfDaysBetweenTwoDates(blackOutHotelFromDate, blackOutHotelEndDate);
							if(blackOutHotelDateList!=null && !blackOutHotelDateList.isEmpty()) {
								for(Date blackOutDate : blackOutHotelDateList) {
									blackOutHotelStrList.add(CommonUtil.convertDatetoString(blackOutDate, "yyyy-MM-dd"));
								}
							}
							rulesHotelBean.setBlackOutHotelDateList(blackOutHotelStrList);
					     }
						rulesHotelBean.setNationality(custNationality);
						if (null != respList.get(i).getRoomType() && !"".equalsIgnoreCase(respList.get(i).getRoomType()))
							rulesHotelBean.setRoomType(respList.get(i).getRoomType());
						if (null != respList.get(i).getRatePlanType() && !"".equalsIgnoreCase(respList.get(i).getRatePlanType()))
							rulesHotelBean.setRatePlanType(respList.get(i).getRatePlanType());

						rulesCommonBean.setRulesHotelBean(rulesHotelBean);
						rulesCommonBean.setTotalFare(respList.get(i).getFareFromTo());
						if(null!=respList.get(i).getTaxsAmount() && !respList.get(i).getTaxsAmount().isEmpty())
						{
							String tax = respList.get(i).getTaxsAmount();
							rulesCommonBean.setBaseFareT(respList.get(i).getFareFromTo()-Double.parseDouble(tax));
						}
						else
							rulesCommonBean.setBaseFareT(respList.get(i).getFareFromTo());
						RulesCommonBean commonBean = null;
						rulesCommonBean.setRuleType("markup");
						if(KieSessionService.kieSessHotel != null) {
							try{
								commonBean = RuleSimulationHelper.fireRulesHotel(rulesCommonBean,KieSessionService.kieSessHotel);	
							} catch ( IllegalStateException ex) {
								try {
									ProductModel productModel = new ProductModel();
									productModel.setStatus(1);
									productModel.setProductCode(QueryConstant.RULE_GROUP_TWO);
									
									ResultBean resultBeanProduct = productService.getProducts(productModel);
									if (resultBeanProduct != null && !resultBeanProduct.isError()) {
										List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
										if (productList != null && !productList.isEmpty()) {
											ProductModel productModal = productList.get(0);
											if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
												boolean flagTextRule = KieSessionService.writeRuleTextHotel(productModal.getRuleText());
												if (flagTextRule) {
													KieSessionService.kieSessHotel = null;
													KieSessionService.getKieSessionHotel();
													commonBean = RuleSimulationHelper.fireRulesHotel(rulesCommonBean,KieSessionService.kieSessHotel);	
												}
											}else{
												KieSessionService.kieSessHotel = null;
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
						if (commonBean != null) {
							if (commonBean.getMarkupAdultList() != null && !commonBean.getMarkupAdultList().isEmpty()) {
								double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
								respList.get(i).setT3Price(maxAdultMarkup);
								rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
							}
						}

						rulesCommonBean.setRuleType("discOrSc");
						if(KieSessionService.kieSessHotel != null) {
							try{
								commonBean = RuleSimulationHelper.fireRulesHotel(rulesCommonBean,KieSessionService.kieSessHotel);	
							} catch ( IllegalStateException ex) {
								try {
									ProductModel productModel = new ProductModel();
									productModel.setStatus(1);
									productModel.setProductCode(QueryConstant.RULE_GROUP_TWO);
									
									ResultBean resultBeanProduct = productService.getProducts(productModel);
									if (resultBeanProduct != null && !resultBeanProduct.isError()) {
										List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
										if (productList != null && !productList.isEmpty()) {
											ProductModel productModal = productList.get(0);
											if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
												boolean flagTextRule = KieSessionService.writeRuleTextHotel(productModal.getRuleText());
												if (flagTextRule) {
													KieSessionService.kieSessHotel = null;
													KieSessionService.getKieSessionHotel();
													commonBean = RuleSimulationHelper.fireRulesHotel(rulesCommonBean,KieSessionService.kieSessHotel);	
												}
											}else{
												KieSessionService.kieSessHotel = null;
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
						if (commonBean != null) {
							if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
								double maxAdultDiscount = Collections.max(commonBean.getDiscountAdultList());
								respList.get(i).setDiscountPrice(maxAdultDiscount);
							}
							if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
								double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
								respList.get(i).setServiceChargePrice(maxAdultServiceCharge);
							}
						}
						
						hotelSearchResp.setRespList(respList);
						if(null!=hotelSearchResp.getSupplierRespTime())
						    hotelSearchResp.setSupplierRespTime(hotelSearchResp.getSupplierRespTime());
					}
				} catch (Exception e) {
					CallLog.printStackTrace(103, e);
					CallLog.info(103, "Exception occured to process rule engine on resp");
				}
				
			}
		}
		return hotelSearchResp;

	}
	
	
	@SuppressWarnings("unchecked")
	public ResultBean applyRuleOnImportPnrFlight(ImportPNRPricingResponseBean importPNRPricingResponseBean, GeoLocationService geoLocationService, SupplierCredentialService credentialService,ResultBean resultBeanAirline,List<AirportModal> airportModals,String country, String branch, String agency, List<Object> agencyMarkupList) {
		ResultBean resultBean = new ResultBean();
		try {
			Long startTime = System.currentTimeMillis();
			List<Passenger> passengers = importPNRPricingResponseBean.getPassengerList();
			if(passengers!=null && !passengers.isEmpty()){
				String nationalityCode=passengers.get(0).getNationality();
				ResultBean nationalityList = orgService.fetchNationalityName(nationalityCode);
				if(nationalityList!=null &&  !nationalityList.isError()){
					List<Object> nationality = (List<Object>) nationalityList.getResultList();
					if(nationality!=null && !nationality.isEmpty()){
						nationalityCode = (String) nationality.get(0);
						passengers.get(0).setNationality(nationalityCode);
					}
				}
			}
			
			importPNRPricingResponseBean = RuleSimulationHelper.processFlightoptionImportPnr(importPNRPricingResponseBean, geoLocationService, credentialService,resultBeanAirline,airportModals, country, branch, agency, agencyMarkupList, productService);
			if(KieSessionService.kieSessImportPnr != null) {
				KieSessionService.kieSessImportPnr.dispose();
			}
			resultBean.setResultObject(importPNRPricingResponseBean);

			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(100, "Time taken in applying RULE/applyRuleOnImportPnrFlight(IMPORT PNR) ---->>>" + timeTaken);

		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(101, e);
		}
		return resultBean;
	}

	public String applyRuleOnCarResult(
			CarRentalSearchRespBean carSearchResp, KieSession kieSessCar,
			String countryId, String agencyId, String branchId) {
		String jsonString = null;
		List<CarRentalInfoBean> respList;
		respList = carSearchResp.getCarRentalInfo();
		if (null != respList && !respList.isEmpty()) 
		{
			for (int i = 0; i < respList.size(); i++) 
			{
				if(null != respList.get(i).getBasePrice())
				{
					RulesCommonBean rulesCommonBean = new RulesCommonBean();
					if(agencyId!=null && !agencyId.isEmpty()) {
						rulesCommonBean.setAgencyId(agencyId);
					} else {
						rulesCommonBean.setBranchId(branchId);
					}
					rulesCommonBean.setTicketingPeriod(new Date());
					
					RulesCarBean rulescarBean = new RulesCarBean();
					rulescarBean.setCarType(respList.get(i).getName());
					double fare = respList.get(i).getConvertedPrice();
					rulescarBean.setFareFromTo(fare);
					rulescarBean.setDestinationCity(respList.get(i).getDropOffLocName());
					rulescarBean.setOriginCity(respList.get(i).getPickupLocName());
					rulescarBean.setSupplier("RentalCars");
					rulescarBean.setTransmissionType(respList.get(i).getAutomatic());
					String carType="";
					String code =Character.toString(respList.get(i).getGroup().charAt(0));
					if(code.equalsIgnoreCase("C"))  
						carType= "Compact";
			            else if(code.equalsIgnoreCase("D"))  
			            	carType= "Compact Elite";
			            else if(code.equalsIgnoreCase("E"))
			            	carType= "Economy";
			            else if(code.equalsIgnoreCase("F"))
			            	carType= "Fullsize";
			            else if(code.equalsIgnoreCase("G"))
			            	carType= "Fullsize Elite";
			            else if(code.equalsIgnoreCase("H"))
			            	carType= "Economy Elite";
			            else if(code.equalsIgnoreCase("I"))
			            	carType= "Intermediate";
			            else if(code.equalsIgnoreCase("J")) 
			            	carType= "Intermediate Elite";
			            else if(code.equalsIgnoreCase("M"))
			            	carType= "Mini";
			            else if(code.equalsIgnoreCase("N"))
			            	carType= "Mini Elite";
			            else if(code.equalsIgnoreCase("O")) 
			            	carType= "Oversize";
			            else if(code.equalsIgnoreCase("P")) 
			            	carType= "Premium";
			            else if(code.equalsIgnoreCase("R"))  
			            	carType= "Standard Elite";
			            else if(code.equalsIgnoreCase("S"))  
			            	carType= "Standard";
			            else if(code.equalsIgnoreCase("U")) 
			            	carType= "Premium Elite";
			            else if(code.equalsIgnoreCase("W"))  
			            	carType= "Luxury Elite";
			            else if(code.equalsIgnoreCase("X"))  
			            	carType= "Special";					
					rulescarBean.setCarType(carType);
					Date bookingDate = new Date();
					rulescarBean.setBookingFromDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
					rulescarBean.setBookingToDate(com.tt.ts.common.util.CommonUtil.changeTime(bookingDate, 0, 0, 0));
					rulesCommonBean.setTotalFare(fare);
					rulesCommonBean.setBaseFareT(fare);
					
					if(null!=respList.get(i).getDepositTaxInc())
					{
						String tax = respList.get(i).getDepositTaxInc();
						//rulesCommonBean.setBaseFareT(respList.get(i).getFareFromTo()-Double.parseDouble(tax));
					}
					//else
						//rulesCommonBean.setBaseFareT(respList.get(i).getFareFromTo());
					rulesCommonBean.setRulesCarBean(rulescarBean);
					RulesCommonBean commonBean = null;
					rulesCommonBean.setRuleType("markup");
					double maxAdultMarkup = 0;
					
					if(KieSessionService.kieSessCar != null) {
						try{
							commonBean = RuleSimulationHelper.fireRulesCar(rulesCommonBean,KieSessionService.kieSessCar);	
						} catch ( IllegalStateException ex) {
							try {
								ProductModel productModel = new ProductModel();
								productModel.setStatus(1);
								productModel.setProductCode(QueryConstant.RULE_GROUP_FOUR);
								
								com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
								if (resultBeanProduct != null && !resultBeanProduct.isError()) {
									List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
									if (productList != null && !productList.isEmpty()) {
										ProductModel productModal = productList.get(0);
										if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
											boolean flagTextRule = KieSessionService.writeRuleTextCar(productModal.getRuleText());
											if (flagTextRule) {
												KieSessionService.kieSessCar = null;
												KieSessionService.getKieSessionCar();
												commonBean = RuleSimulationHelper.fireRulesCar(rulesCommonBean,KieSessionService.kieSessCar);	
											}
										}else{
											KieSessionService.kieSessCar = null;
										}

									}
								}
							} catch(Exception e) {
								CallLog.printStackTrace(122, e);
							}
							
						}catch(Exception ex) {
							CallLog.printStackTrace(122, ex);
						}
					}
					
					if (commonBean != null) {
						if (commonBean.getMarkupAdultList() != null && !commonBean.getMarkupAdultList().isEmpty()) {
							maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());

							rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
						}
					}
					
					respList.get(i).setT3Price(maxAdultMarkup);
					rulesCommonBean.setRuleType("discOrSc");
					double maxAdultDiscount = 0;
					double maxAdultServiceCharge = 0;
					
					if(KieSessionService.kieSessCar != null) {
						try{
							commonBean = RuleSimulationHelper.fireRulesCar(rulesCommonBean,KieSessionService.kieSessCar);	
						} catch ( IllegalStateException ex) {
							try {
								ProductModel productModel = new ProductModel();
								productModel.setStatus(1);
								productModel.setProductCode(QueryConstant.RULE_GROUP_FOUR);
								
								com.tt.ts.common.modal.ResultBean resultBeanProduct = productService.getProducts(productModel);
								if (resultBeanProduct != null && !resultBeanProduct.isError()) {
									List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
									if (productList != null && !productList.isEmpty()) {
										ProductModel productModal = productList.get(0);
										if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {		
											boolean flagTextRule = KieSessionService.writeRuleTextCar(productModal.getRuleText());
											if (flagTextRule) {
												KieSessionService.kieSessCar = null;
												KieSessionService.getKieSessionCar();
												commonBean = RuleSimulationHelper.fireRulesCar(rulesCommonBean,KieSessionService.kieSessCar);	
											}
										}else{
											KieSessionService.kieSessCar = null;
										}

									}
								}
							} catch(Exception e) {
								CallLog.printStackTrace(122, e);
							}
							
						}catch(Exception ex) {
							CallLog.printStackTrace(122, ex);
						}
					}
					if (commonBean != null) {
						if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
							maxAdultDiscount = Collections.max(commonBean.getDiscountAdultList());

						}
						if (commonBean.getServiceChargeAdultList() != null
								&& !commonBean.getServiceChargeAdultList().isEmpty()) {
							maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
						}
					}
					respList.get(i).setDiscountPrice(maxAdultDiscount);
					respList.get(i).setServiceChargePrice(maxAdultServiceCharge);
					carSearchResp.setCarRentalInfo(respList);
				}
			}
		}
		if(null!=carSearchResp)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(carSearchResp);
			} catch (Exception e) {
				CallLog.printStackTrace(122,e);
			}
		}

		return jsonString;

	}
	
	public String applyRuleOnActivitiesResult(ActivitiesSearchResponseBean activitiesSearchResp,KieSession kieSessActivity,String agencyId,String branchId,ActivitiesSearchRequestBean searchRequestBean) 
	{
		String jsonString="";
		List<ActivitiesBean> respList;
		GenericHelperUtil<ActivitiesSearchResponseBean> genHelper;
		genHelper = new GenericHelperUtil<>();
		if(activitiesSearchResp.getActivitiesList().size()>0) 
		{
			respList = activitiesSearchResp.getActivitiesList();
			
			if (null != respList && !respList.isEmpty()) 
			{
				for (int i = 0; i < respList.size(); i++) 
				{
					if(null != respList.get(i).getPrice())
					{
						RulesCommonBean rulesCommonBean = new RulesCommonBean();
						if(agencyId!=null && !agencyId.isEmpty()) {
							rulesCommonBean.setAgencyId(agencyId);
						} else {
							rulesCommonBean.setBranchId(branchId);
						}
						rulesCommonBean.setTicketingPeriod(new Date());
						
						RulesActivityBean rulesActivityBean=new RulesActivityBean();
						/*if(null != corpId && !"".equalsIgnoreCase(corpId))
						{
							rulesActivityBean.setCorporateId(corpId);
							//rulesHotelBean.setCustomerType("Corporate");
						}*/
						/*else
							rulesHotelBean.setCustomerType("Retail");*/
						rulesActivityBean.setActivityName(respList.get(i).getActivityName());
						//rulesActivityBean.setActivityType(respList.get(i).getCatagory());
						rulesActivityBean.setDestinationCountry(respList.get(i).getCountryName());
						rulesActivityBean.setDestinationCity(respList.get(i).getDestinationName());
						rulesActivityBean.setFareFromTo(Double.parseDouble(respList.get(i).getPrice()));
						/*rulesHotelBean.setHotelName(respList.get(i).getHotelName());
						rulesHotelBean.setHotelcode(respList.get(i).getHotelCode());
						rulesHotelBean.setNoOfPax(respList.get(i).getNoOfPax());
						rulesHotelBean.setNoOfRooms(respList.get(i).getNoOfRooms());
						rulesHotelBean.setNoOfNights(respList.get(i).getDuration());*/
						rulesActivityBean.setBookingFromDate(CommonUtil.convertStringToDate(searchRequestBean.getFromDate(),DATE_FORMAT));
						rulesActivityBean.setBookingToDate(CommonUtil.convertStringToDate(searchRequestBean.getToDate(),DATE_FORMAT));
			
						/*rulesHotelBean.setBookingStartDate(CommonUtil.convertStringToDate(respList.get(i).getBookingStartDate(), DATE_FORMAT));
						rulesHotelBean.setBookingEndDate(CommonUtil.convertStringToDate(respList.get(i).getBookingEndDate(), DATE_FORMAT));*/
	
						rulesCommonBean.setRulesActivityBean(rulesActivityBean);
						rulesCommonBean.setTotalFare(Double.parseDouble(respList.get(i).getPrice()));

					rulesCommonBean.setBaseFareT(Double.parseDouble(respList.get(i).getPrice()));
					
					RulesCommonBean commonBean = null;
					rulesCommonBean.setRuleType("markup");
					if(null!=kieSessActivity)
					{
						 commonBean = RuleSimulationHelper.fireRulesActivity(rulesCommonBean, kieSessActivity);
						 if(commonBean != null)
						 {
							if (commonBean.getMarkupAdultList()!= null && !commonBean.getMarkupAdultList().isEmpty()) {
								double maxAdultMarkup = Collections.max(commonBean.getMarkupAdultList());
								respList.get(i).setT3Markup(maxAdultMarkup);
								rulesCommonBean.setUpdatedPrice(maxAdultMarkup);
							}		
						}
					}
					rulesCommonBean.setRuleType("discOrSc");
					if(null!=kieSessActivity)
					{
						 commonBean = RuleSimulationHelper.fireRulesActivity(rulesCommonBean, kieSessActivity);
						 if(commonBean != null)
						 {				
							if (commonBean.getDiscountAdultList() != null && !commonBean.getDiscountAdultList().isEmpty()) {
								double maxAdultDiscount =  Collections.max(commonBean.getDiscountAdultList());
								respList.get(i).setDiscountPrice(maxAdultDiscount);
							}
							if (commonBean.getServiceChargeAdultList() != null && !commonBean.getServiceChargeAdultList().isEmpty()) {
								double maxAdultServiceCharge = Collections.max(commonBean.getServiceChargeAdultList());
								respList.get(i).setServiceChargePrice(maxAdultServiceCharge);
							}
						}
						activitiesSearchResp.setActivitiesList(respList);

					}
				}
			}
		}
		jsonString = genHelper.getJsonStringByEntity(activitiesSearchResp);
		}
		return jsonString;
		}
		}