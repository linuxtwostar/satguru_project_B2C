package com.tt.ws.rest.air.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.QueryConstant;
import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ts.airline.service.AirlineService;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.blacklist.model.BlackOutFlightModel;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.flighttag.model.FlightTagModel;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rbd.model.RbdModel;
import com.tt.ts.rest.agent.manager.AgentManager;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.organization.service.OrganizationService;
import com.tt.ts.rest.ruleengine.service.KieSessionService;
import com.tt.ts.rest.ruleengine.service.RuleSimulationService;
import com.tt.ts.ruleengine.model.offer.OfferPricingModel;
import com.tt.ts.ruleengine.service.OfferPricingService;
import com.tt.ts.suppliercredential.modal.SupplierCredentialModal;
import com.tt.ts.suppliercredential.service.SupplierCredentialService;
import com.tt.ws.rest.air.util.AirHelperUtil;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.flight.bean.amadeus.fareconfirm.FareConfirmRequestBean;
import com.ws.services.flight.bean.amadeus.fareconfirm.FareConfirmResponseBean;
import com.ws.services.flight.bean.amadeus.fareconfirm.SeatConfirmRequestBean;
import com.ws.services.flight.bean.amadeus.fareconfirm.SeatConfirmResponseBean;
import com.ws.services.flight.bean.amadeus.farerule.FareRuleRequestBean;
import com.ws.services.flight.bean.amadeus.farerule.FareRuleResponseBean;
import com.ws.services.flight.bean.booking.Address;
import com.ws.services.flight.bean.booking.ContactInfo;
import com.ws.services.flight.bean.booking.FlightBookAncillaryServiceRequestBean;
import com.ws.services.flight.bean.booking.FlightBookingRequestBean;
import com.ws.services.flight.bean.booking.FlightBookingResponseBean;
import com.ws.services.flight.bean.booking.FlightCancelRequestBean;
import com.ws.services.flight.bean.booking.FlightCancelResponseBean;
import com.ws.services.flight.bean.booking.Passenger;
import com.ws.services.flight.bean.flightsearch.FlightCalendarFareSearchResponseBean;
import com.ws.services.flight.bean.flightsearch.FlightCalendarOption;
import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.FlightSearchRequestBean;
import com.ws.services.flight.bean.flightsearch.FlightSearchResponseBean;
import com.ws.services.flight.bean.flightsearch.RoundTripFlightOption;
import com.ws.services.flight.bean.flydubai.booking.FlyDubaiBookingRequestBean;
import com.ws.services.flight.bean.flydubai.booking.Payment;
import com.ws.services.flight.bean.flydubai.servicequote.CarrierCode;
import com.ws.services.flight.bean.flydubai.servicequote.RetrieveServiceQuotesRequestBean;
import com.ws.services.flight.bean.flydubai.servicequote.RetrieveServiceQuotesResponseBean;
import com.ws.services.flight.bean.flydubai.servicequote.ServiceQuote;
import com.ws.services.flight.bean.importpnr.ImportPNRResponseBean;
import com.ws.services.flight.bean.importpnr.ImportPNRServiceRequestBean;
import com.ws.services.flight.config.ServiceConfigModel;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

@Service
public class FlightWSService
{
    
    @Autowired
    RedisService redisService;
    
    @Autowired
	ProductService productService;
    
    @Autowired
    CorporateService corporateService;
    
    @Autowired
    GeoLocationService geoLocationService;
    
    @Autowired
    OrganizationService orgService;
    
    @Autowired
    AirlineService airlineService;
    
    @Autowired
    SupplierCredentialService credentialService;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    OfferPricingService offerPricingService;
    
    @Autowired
	private AgentManager agentManager;
    
    private static final String ONEWAY = "OneWay";
    private static final String ROUNDTRIP = "RoundTrip";
    private static final String MULTICITY = "MultiCity";
    private static final String FLIGHT_RESULT_EXPIRE_TIME = "FLIGHT_RESULT_EXPIRE_TIME";

    public void keepSessionAliveLcc(FlightBookingRequestBean flightBookingRequestBean){
    	ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(flightBookingRequestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.SessionkeepAlive.toString());
		serviceRequestBean.setServiceConfigModel(flightBookingRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
    }
    
	@SuppressWarnings({ "unchecked" })
	public String responseForFlightSearchResult(FlightSearchRequestBean requestBean, ResultBean resultBeanAirline, List<BlackOutFlightModel> blackList, List<FlightTagModel> flightTagList, ResultBean rbdResultBean){
		Integer expiryTimeCache = Integer.parseInt(messageSource.getMessage(FLIGHT_RESULT_EXPIRE_TIME, null ,"30", null));
		CallLog.info(102,"####### Breakup of responseForFlightSearchResult start #########");
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		String jsonString;
		CallLog.info(0, "FlexibleDates === " + requestBean.getFlexibleDates());
		Long st1= System.currentTimeMillis();
		String searchFlightKey = requestBean.getFlightSearchKey();
		CallLog.info(0, "searchFlightKey === " + requestBean.getFlightSearchKey());
		Boolean isSearchKeyFound = false;
		
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheFlight(searchFlightKey,redisService);
		if(resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		}
		Long end1= System.currentTimeMillis();
		CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:isSearchKeyInCache line 99::"+(end1-st1));
		
		Boolean isPlatingCarrierKeyFound = false;
		String platingCarrierKey = requestBean.getFlightPlatingCarrierKey();
		ResultBean resultBeanPltCarr = RedisCacheUtil.isSearchKeyInCacheFlight(platingCarrierKey,redisService);
		if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
			isPlatingCarrierKeyFound = resultBeanPltCarr.getResultBoolean();
		}
		Long startTime = System.currentTimeMillis();
		Long st2= System.currentTimeMillis();
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

		ResultBean resultBeanProduct = productService.getProducts(productModel);
		if (resultBeanProduct != null && !resultBeanProduct.isError()) {
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) {
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {

					Date ruleLastUpdated = productModal.getRuleLastUpdated();
					Date kieSessionLastUpdated = null;
					if(KieSessionService.kieSessionLastUpdatedFlightSearch!=null && !KieSessionService.kieSessionLastUpdatedFlightSearch.isEmpty() && KieSessionService.kieSessionLastUpdatedFlightSearch.containsKey(searchFlightKey)) {
						kieSessionLastUpdated = KieSessionService.kieSessionLastUpdatedFlightSearch.get(searchFlightKey);
						if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
							CallLog.info(105,"Rules for Flight Not changed.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+kieSessionLastUpdated+"(Time : "+kieSessionLastUpdated.getTime()+").");
							isRuleTextUpdated = true;
						}
					}
						
					if (KieSessionService.kieSessFlight != null && isRuleTextUpdated) {
						KieSessionService.getKieSessionFlight();
					} else {
						boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
						if (flagTextRule) {
							KieSessionService.kieSessFlight = null;
							KieSessionService.getKieSessionFlight();
							KieSessionService.kieSessionLastUpdatedFlightSearch.put(searchFlightKey, new Date());
						}			
					}

				} else {
					KieSessionService.kieSessFlight = null;
					CallLog.info(105, "No approved flight rules found.");
				}

			}
		}
		Long end2= System.currentTimeMillis();
		CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:kieSessionFlightByProduct line 99::"+(end2-st2));
		
		FlightSearchResponseBean finalSearchResponseBean = null;
		FlightCalendarFareSearchResponseBean finalCalSearchResponseBean = null;
		CommonUtil<Object> commonUtil = new CommonUtil<>();
		ResultBean resultBeanRule = null;
		ResultBean resultBeanPlatingCache = null;
		String platingCarrierString = "";
		if (requestBean.getFlexibleDates()) {
			if (isSearchKeyFound && requestBean.isDataFromCacheOrNot()) {
				ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(searchFlightKey, redisService);
				if (resultBeanCache != null && !resultBeanCache.isError()) {
					jsonString = resultBeanCache.getResultString();
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) commonUtil.convertJSONIntoObject(jsonString, FlightCalendarFareSearchResponseBean.class);
					if (!isRuleTextUpdated) {
						resultBeanRule = applyRuleOnFlightCalSearchResponse(requestBean, finalCalSearchResponseBean,resultBeanAirline);
						if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
							List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
							finalCalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
						}
						if(resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList2()!= null && !resultBeanRule.getResultList2().isEmpty()) {
							List<FlightCalendarOption> updatedFlightCalenderList = (List<FlightCalendarOption>)resultBeanRule.getResultList2();
							finalCalSearchResponseBean.setCalFlightOptions(updatedFlightCalenderList);
						}
						boolean flag1 = resultBean != null && !resultBean.isError();
						boolean flag2 = finalCalSearchResponseBean.getCalFlightOptions() != null && !finalCalSearchResponseBean.getCalFlightOptions().isEmpty();
						if (flag1 && flag2) {
							jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
							RedisCacheUtil.setResponseInCacheFlight(searchFlightKey, jsonString, redisService,expiryTimeCache);
						}
					}
					if(isPlatingCarrierKeyFound) {
						resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
						if(resultBeanPlatingCache != null && !resultBeanPlatingCache.isError()) {
							platingCarrierString = resultBeanPlatingCache.getResultString();
						} else {
							List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
							platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
							if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
								RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
							}
						}
						finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
					} else {
						List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
						platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
						if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
							RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
						}
						finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
					}

				} else {
					finalCalSearchResponseBean = AirHelperUtil.processToFlightSearchResponseDateFlexible(serviceRequestBean, requestBean);
					resultBeanRule = applyRuleOnFlightCalSearchResponse(requestBean, finalCalSearchResponseBean,resultBeanAirline);

					if (finalCalSearchResponseBean!= null && resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
						List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
						finalCalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
					}
					if(finalCalSearchResponseBean!= null && resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList2()!= null && !resultBeanRule.getResultList2().isEmpty()) {
						List<FlightCalendarOption> updatedFlightCalenderList = (List<FlightCalendarOption>)resultBeanRule.getResultList2();
						finalCalSearchResponseBean.setCalFlightOptions(updatedFlightCalenderList);
					}
					if (resultBean != null && !resultBean.isError()) {
						jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
						RedisCacheUtil.setResponseInCacheFlight(searchFlightKey, jsonString, redisService,expiryTimeCache);
					}
					if(finalCalSearchResponseBean != null) {
						if(isPlatingCarrierKeyFound) {
							resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
							if(resultBeanPlatingCache != null && !resultBeanPlatingCache.isError()) {
								platingCarrierString = resultBeanPlatingCache.getResultString();
							} else {
								List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
								platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
								if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
									RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
								}
							}
							finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
						} else {
							List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
							platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
							if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
								RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
							}
							finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
						}
					}
					
				}
			} else if(!isSearchKeyFound || !requestBean.isDataFromCacheOrNot()){
				finalCalSearchResponseBean = AirHelperUtil.processToFlightSearchResponseDateFlexible(serviceRequestBean, requestBean);
				resultBeanRule = applyRuleOnFlightCalSearchResponse(requestBean, finalCalSearchResponseBean,resultBeanAirline);

				if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
					List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
					finalCalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
				}
				if(resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList2()!= null && !resultBeanRule.getResultList2().isEmpty()) {
					List<FlightCalendarOption> updatedFlightCalenderList = (List<FlightCalendarOption>)resultBeanRule.getResultList2();
					finalCalSearchResponseBean.setCalFlightOptions(updatedFlightCalenderList);
				}
				if (resultBean != null && !resultBean.isError()) {
					jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
					RedisCacheUtil.setResponseInCacheFlight(searchFlightKey, jsonString, redisService,expiryTimeCache);
				}
				if(finalCalSearchResponseBean != null) {
					if(isPlatingCarrierKeyFound) {
						resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
						if(resultBeanPlatingCache != null && !resultBeanPlatingCache.isError()) {
							platingCarrierString = resultBeanPlatingCache.getResultString();
						} else {
							List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
							platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
							if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
								RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
							}
						}
						finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
					} else {
						List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
						platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
						if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
							RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
						}
						finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
					}
				}
				
			}
			
			AirHelperUtil airHelperUtil =new AirHelperUtil();
			if(finalCalSearchResponseBean != null)
			{
				ResultBean resultBeanAgencymarkup = settingAgencyMarkupCalender(requestBean,finalCalSearchResponseBean);
				if(resultBeanAgencymarkup!=null && !resultBeanAgencymarkup.isError()) {
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean)resultBeanAgencymarkup.getResultObject();
				}
				
				ResultBean resultBeanOffer = settingOfferDetailsCalender(requestBean, finalCalSearchResponseBean);
				if (resultBeanOffer != null && !resultBeanOffer.isError()) {
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) resultBeanOffer.getResultObject();
				}
				
				Long st8= System.currentTimeMillis();
				List<RbdModel> rbdList= null;
				if(rbdResultBean.getResultList()!=null && !rbdResultBean.getResultList().isEmpty()){
					rbdList=(List<RbdModel>) rbdResultBean.getResultList();
				}
				airHelperUtil.validateBlackListAndTagRBDChecksForCalendarFare(finalCalSearchResponseBean, blackList,flightTagList,requestBean,rbdList);
				Long end8= System.currentTimeMillis();
				CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:validateBlackListAndTagRBDChecksForCalendarFare line 311::"+(end8-st8));
	
			}
			jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);			
			} else {
				Long start= System.currentTimeMillis();
				if (isSearchKeyFound && requestBean.isDataFromCacheOrNot()) {
					Long st3= System.currentTimeMillis();
					ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(searchFlightKey, redisService);
					Long end3= System.currentTimeMillis();
					CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:getResponseFromCacheFlight line 211::"+(end3-st3));
					
					if (resultBeanCache != null && !resultBeanCache.isError()) {
						jsonString = resultBeanCache.getResultString();
						finalSearchResponseBean = (FlightSearchResponseBean) commonUtil.convertJSONIntoObject(jsonString, FlightSearchResponseBean.class);
						if (!isRuleTextUpdated) {
							Long st4= System.currentTimeMillis();
							resultBeanRule = applyRuleOnFlightSearchResponse(requestBean, finalSearchResponseBean,resultBeanAirline);
	
							if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
								if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
									List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
									finalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
								} else {
									List<RoundTripFlightOption> updatedFlightOptions = (List<RoundTripFlightOption>) resultBeanRule.getResultList();
									finalSearchResponseBean.setRoundTripFlightOptions(updatedFlightOptions);
								}
							}
							Long end4= System.currentTimeMillis();
							finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[markup:"+(end4-st4)+"]");
							CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:applyRuleOnFlightSearchResponse line 220::"+(end4-st4));
				
							if (resultBean != null && !resultBean.isError()) {
								Long st5= System.currentTimeMillis();
								jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
								RedisCacheUtil.setResponseInCacheFlight(searchFlightKey, jsonString, redisService,expiryTimeCache);
								Long end5= System.currentTimeMillis();
								CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:setResponseInCacheFlight line 237::"+(end5-st5));
			
							}
						}
						
						if(finalSearchResponseBean != null) {
							if(isPlatingCarrierKeyFound) {
								resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
								if(resultBeanPlatingCache == null || resultBeanPlatingCache.isError()) {
									if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
										List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
										platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
										
									} else {
										List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
										platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);	
									}
									if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
										RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
									}		
								}
								
							} else {
								if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
									List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
									
								} else {
									List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);	
								}
								if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
									RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
								}
							}
						}
						
					} else {
						Long st6= System.currentTimeMillis();
						finalSearchResponseBean = AirHelperUtil.processToFlightSearchResponse(serviceRequestBean, requestBean);
						Long end6= System.currentTimeMillis();
						CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:processToFlightSearchResponse line 245::"+(end6-st6));
			
						Long st7= System.currentTimeMillis();
						resultBeanRule = applyRuleOnFlightSearchResponse(requestBean, finalSearchResponseBean,resultBeanAirline);
	
						if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
							if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
								List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
								finalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
							} else {
								List<RoundTripFlightOption> updatedFlightOptions = (List<RoundTripFlightOption>) resultBeanRule.getResultList();
								finalSearchResponseBean.setRoundTripFlightOptions(updatedFlightOptions);
							}
						}
						Long end7= System.currentTimeMillis();
						finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[markup:"+(end7-st7)+"]");
						CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:applyRuleOnFlightSearchResponse line 250::"+(end7-st7));
			
						if (resultBean != null && !resultBean.isError()) {
							Long st8= System.currentTimeMillis();
							jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
							RedisCacheUtil.setResponseInCacheFlight(searchFlightKey, jsonString, redisService,expiryTimeCache);
							Long end8= System.currentTimeMillis();
							CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:setResponseInCacheFlight line 267::"+(end8-st8));
				
						}
						if(finalSearchResponseBean != null) {
							if(isPlatingCarrierKeyFound) {
								resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
								if(resultBeanPlatingCache == null || resultBeanPlatingCache.isError()) {
									if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
										List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
										platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
										
									} else {
										List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
										platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);	
									}
									if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
										RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
									}		
								}
								
							} else {
								if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
									List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
									
								} else {
									List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);	
								}
								if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
									RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
								}
							}
						}
					}
				} else if(!isSearchKeyFound || !requestBean.isDataFromCacheOrNot()){
					Long st6= System.currentTimeMillis();
					finalSearchResponseBean = AirHelperUtil.processToFlightSearchResponse(serviceRequestBean, requestBean);
					Long end6= System.currentTimeMillis();
					CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:processToFlightSearchResponse line 275::"+(end6-st6));
		
					Long st7= System.currentTimeMillis();
					resultBeanRule = applyRuleOnFlightSearchResponse(requestBean, finalSearchResponseBean,resultBeanAirline);
	
					if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList()!= null && !resultBeanRule.getResultList().isEmpty()) {
						if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
							List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
							finalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
						} else {
							List<RoundTripFlightOption> updatedFlightOptions = (List<RoundTripFlightOption>) resultBeanRule.getResultList();
							finalSearchResponseBean.setRoundTripFlightOptions(updatedFlightOptions);
						}
					}
					Long end7= System.currentTimeMillis();
					finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[markup:"+(end7-st7)+"]");
					CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:applyRuleOnFlightSearchResponse line 280::"+(end7-st7));
		
					if (resultBean != null && !resultBean.isError()) {
						Long st8= System.currentTimeMillis();
						jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
						RedisCacheUtil.setResponseInCacheFlight(searchFlightKey, jsonString, redisService,expiryTimeCache);
						Long end8= System.currentTimeMillis();
						CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:setResponseInCacheFlight line 297::"+(end8-st8));
			
					}
					if(finalSearchResponseBean != null) {
						if(isPlatingCarrierKeyFound) {
							resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
							if(resultBeanPlatingCache == null || resultBeanPlatingCache.isError()) {
								if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
									List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
									
								} else {
									List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);	
								}
								if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
									RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
								}		
							}
							
						} else {
							if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
								List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
								platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
								
							} else {
								List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
								platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);	
							}
							if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
								RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
							}
						}
					}
				}
				AirHelperUtil airHelperUtil =new AirHelperUtil();
				if(finalSearchResponseBean!=null)
				{
					ResultBean resultBeanAgencymarkup = settingAgencyMarkup(requestBean,finalSearchResponseBean);
					if(resultBeanAgencymarkup!=null && !resultBeanAgencymarkup.isError()) {
						finalSearchResponseBean = (FlightSearchResponseBean)resultBeanAgencymarkup.getResultObject();
					}
					ResultBean resultBeanOffer = settingOfferDetails(requestBean, finalSearchResponseBean);
					if (resultBeanOffer != null && !resultBeanOffer.isError()) {
						finalSearchResponseBean = (FlightSearchResponseBean) resultBeanOffer.getResultObject();
					}
					Long st8= System.currentTimeMillis();
					List<RbdModel> rbdList= null;
					if(rbdResultBean.getResultList()!=null && !rbdResultBean.getResultList().isEmpty()){
						rbdList=(List<RbdModel>) rbdResultBean.getResultList();
					}
					airHelperUtil.validateBlackListAndTagRBDChecks(finalSearchResponseBean, blackList,flightTagList,requestBean,rbdList);
					Long end8= System.currentTimeMillis();
					finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[BlackListAndTagRBDChecks/AgencyMarkup:"+(end8-st8)+"]");
					CallLog.info(102,"Time Taken ::: FlightWsService:responseForFlightSearchResult:validateBlackListAndTagRBDChecks line 311::"+(end8-st8));	
				}
				Long end= System.currentTimeMillis();
				finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[totalResponse:"+(end-start)+"]");
				jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
		}
		
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - startTime;
		CallLog.info(100, "TOTAL RESPONSE time----->>>" + timeTaken);
		CallLog.info(102,"####### Breakup of responseForFlightSearchResult End #########");
		return jsonString;
	}

	@SuppressWarnings({ "unchecked" })
	public String responseForAllFlightSearchResult(FlightSearchRequestBean requestBean, ResultBean resultBeanAirline, List<BlackOutFlightModel> blackList, List<FlightTagModel> flightTagList, ResultBean rbdResultBean) {
		Integer expiryTimeCache = Integer.parseInt(messageSource.getMessage(FLIGHT_RESULT_EXPIRE_TIME, null ,"30", null));
		CallLog.info(102, "####### Breakup of responseForAllFlightSearchResult start #########");
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		String jsonString;
		CallLog.info(0, "FlexibleDates === " + requestBean.getFlexibleDates());
		Long st1 = System.currentTimeMillis();
		String searchFlightKey = requestBean.getFlightSearchKey();
		CallLog.info(0, "searchFlightKey === " + requestBean.getFlightSearchKey());
		Boolean isSearchKeyFound = false;
		String searchFlightKeyAll = requestBean.getFlightSearchKey() + "_All";
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheFlight(searchFlightKeyAll, redisService);
		if (resultBean != null && !resultBean.isError()) {
			isSearchKeyFound = resultBean.getResultBoolean();
		}
		Long end1 = System.currentTimeMillis();
		CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:isSearchKeyInCache line 99::" + (end1 - st1));

		Boolean isPlatingCarrierKeyFound = false;
		String platingCarrierKey = requestBean.getFlightPlatingCarrierKey();
		ResultBean resultBeanPltCarr = RedisCacheUtil.isSearchKeyInCacheFlight(platingCarrierKey, redisService);
		if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
			isPlatingCarrierKeyFound = resultBeanPltCarr.getResultBoolean();
		}
		Long startTime = System.currentTimeMillis();
		Long st2 = System.currentTimeMillis();
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

		ResultBean resultBeanProduct = productService.getProducts(productModel);
		if (resultBeanProduct != null && !resultBeanProduct.isError()) {
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) {
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
					Date ruleLastUpdated = productModal.getRuleLastUpdated();
					Date kieSessionLastUpdated = null;
					if(KieSessionService.kieSessionLastUpdatedFlightSearch!=null && !KieSessionService.kieSessionLastUpdatedFlightSearch.isEmpty() && KieSessionService.kieSessionLastUpdatedFlightSearch.containsKey(searchFlightKey)) {
						kieSessionLastUpdated = KieSessionService.kieSessionLastUpdatedFlightSearch.get(searchFlightKey);
						if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
							CallLog.info(105,"Rules for Flight Not changed.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+kieSessionLastUpdated+"(Time : "+kieSessionLastUpdated.getTime()+").");
							isRuleTextUpdated = true;
						}
					}
						
					if (KieSessionService.kieSessFlight != null && isRuleTextUpdated) {
						KieSessionService.getKieSessionFlight();
					} else {
						boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
						if (flagTextRule) {
							KieSessionService.kieSessFlight = null;
							KieSessionService.getKieSessionFlight();
							KieSessionService.kieSessionLastUpdatedFlightSearch.put(searchFlightKey, new Date());
						}			
					}
				} else {
					KieSessionService.kieSessFlight = null;
					CallLog.info(105, "No approved flight rules found.");
				}
			}
		}
		Long end2 = System.currentTimeMillis();
		CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:kieSessionFlightByProduct line 99::" + (end2 - st2));

		FlightSearchResponseBean finalSearchResponseBean = null;
		FlightCalendarFareSearchResponseBean finalCalSearchResponseBean = null;
		CommonUtil<Object> commonUtil = new CommonUtil<>();
		ResultBean resultBeanRule = null;
		ResultBean resultBeanPlatingCache = null;
		String platingCarrierString = "";
		if (requestBean.getFlexibleDates()) {
			if (isSearchKeyFound && requestBean.isDataFromCacheOrNot()) {
				ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(searchFlightKeyAll, redisService);
				if (resultBeanCache != null && !resultBeanCache.isError()) {
					jsonString = resultBeanCache.getResultString();
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) commonUtil.convertJSONIntoObject(jsonString, FlightCalendarFareSearchResponseBean.class);
					if(finalCalSearchResponseBean!=null) {
						if (!isRuleTextUpdated) {
							resultBeanRule = applyRuleOnFlightCalSearchResponse(requestBean, finalCalSearchResponseBean, resultBeanAirline);
							if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList() != null && !resultBeanRule.getResultList().isEmpty()) {
								List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
								finalCalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
							}
							if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList2() != null && !resultBeanRule.getResultList2().isEmpty()) {
								List<FlightCalendarOption> updatedFlightCalenderList = (List<FlightCalendarOption>) resultBeanRule.getResultList2();
								finalCalSearchResponseBean.setCalFlightOptions(updatedFlightCalenderList);
							}
							boolean flag1 = resultBean != null && !resultBean.isError();
							boolean flag2 = finalCalSearchResponseBean.getCalFlightOptions() != null && !finalCalSearchResponseBean.getCalFlightOptions().isEmpty();
							if (flag1 && flag2) {
								jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
								RedisCacheUtil.setResponseInCacheFlight(searchFlightKeyAll, jsonString, redisService,expiryTimeCache);
							}
						}
						if (isPlatingCarrierKeyFound) {
							resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
							if (resultBeanPlatingCache != null && !resultBeanPlatingCache.isError()) {
								platingCarrierString = resultBeanPlatingCache.getResultString();
							} else {
								List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
								platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
								if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
									RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
								}
							}
							finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
						} else {
							List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
							platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
							if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
								RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
							}
							finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
						}
					}			
				}
			}

			AirHelperUtil airHelperUtil = new AirHelperUtil();
			if (finalCalSearchResponseBean != null) {
				ResultBean resultBeanAgencymarkup = settingAgencyMarkupCalender(requestBean, finalCalSearchResponseBean);
				if (resultBeanAgencymarkup != null && !resultBeanAgencymarkup.isError()) {
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) resultBeanAgencymarkup.getResultObject();
				}
				ResultBean resultBeanOffer = settingOfferDetailsCalender(requestBean, finalCalSearchResponseBean);
				if (resultBeanOffer != null && !resultBeanOffer.isError()) {
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) resultBeanOffer.getResultObject();
				}
				Long st8 = System.currentTimeMillis();
				List<RbdModel> rbdList = null;
				if (rbdResultBean.getResultList() != null && !rbdResultBean.getResultList().isEmpty()) {
					rbdList = (List<RbdModel>) rbdResultBean.getResultList();
				}
				airHelperUtil.validateBlackListAndTagRBDChecksForCalendarFare(finalCalSearchResponseBean, blackList, flightTagList, requestBean, rbdList);
				Long end8 = System.currentTimeMillis();
				CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:validateBlackListAndTagRBDChecksForCalendarFare line 311::" + (end8 - st8));

			}
			jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
		} else {
			
			if (isSearchKeyFound && requestBean.isDataFromCacheOrNot()) {
				Long st3 = System.currentTimeMillis();
				ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(searchFlightKeyAll, redisService);
				Long end3 = System.currentTimeMillis();
				CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:getResponseFromCacheFlight line 211::" + (end3 - st3));

				if (resultBeanCache != null && !resultBeanCache.isError()) {
					jsonString = resultBeanCache.getResultString();
					finalSearchResponseBean = (FlightSearchResponseBean) commonUtil.convertJSONIntoObject(jsonString, FlightSearchResponseBean.class);
					if (!isRuleTextUpdated) {
						Long st4 = System.currentTimeMillis();
						resultBeanRule = applyRuleOnFlightSearchResponse(requestBean, finalSearchResponseBean, resultBeanAirline);

						if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList() != null && !resultBeanRule.getResultList().isEmpty()) {
							if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
								List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
								finalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
							} else {
								List<RoundTripFlightOption> updatedFlightOptions = (List<RoundTripFlightOption>) resultBeanRule.getResultList();
								finalSearchResponseBean.setRoundTripFlightOptions(updatedFlightOptions);
							}
						}
						Long end4 = System.currentTimeMillis();
						if(finalSearchResponseBean!=null)
							finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[ApplyRuleResultFromCache:" + (end4 - st4) + "]");
						CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:applyRuleOnFlightSearchResponse line 220::" + (end4 - st4));

						if (resultBean != null && !resultBean.isError()) {
							Long st5 = System.currentTimeMillis();
							jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
							RedisCacheUtil.setResponseInCacheFlight(searchFlightKeyAll, jsonString, redisService,expiryTimeCache);
							Long end5 = System.currentTimeMillis();
							CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:setResponseInCacheFlight line 237::" + (end5 - st5));

						}
					}

					if (finalSearchResponseBean != null) {
						if (isPlatingCarrierKeyFound) {
							resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
							if (resultBeanPlatingCache == null || resultBeanPlatingCache.isError()) {
								if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
									List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);

								} else {
									List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
									platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);
								}
								if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
									RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
								}
							}

						} else {
							if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
								List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
								platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);

							} else {
								List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
								platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);
							}
							if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
								RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
							}
						}
					}
				}
			}
			AirHelperUtil airHelperUtil = new AirHelperUtil();
			if (finalSearchResponseBean != null) {
				ResultBean resultBeanAgencymarkup = settingAgencyMarkup(requestBean, finalSearchResponseBean);
				if (resultBeanAgencymarkup != null && !resultBeanAgencymarkup.isError()) {
					finalSearchResponseBean = (FlightSearchResponseBean) resultBeanAgencymarkup.getResultObject();
				}
				ResultBean resultBeanOffer = settingOfferDetails(requestBean, finalSearchResponseBean);
				if (resultBeanOffer != null && !resultBeanOffer.isError()) {
					finalSearchResponseBean = (FlightSearchResponseBean) resultBeanOffer.getResultObject();
				}
				Long st8 = System.currentTimeMillis();
				List<RbdModel> rbdList = null;
				if (rbdResultBean.getResultList() != null && !rbdResultBean.getResultList().isEmpty()) {
					rbdList = (List<RbdModel>) rbdResultBean.getResultList();
				}
				airHelperUtil.validateBlackListAndTagRBDChecks(finalSearchResponseBean, blackList, flightTagList, requestBean, rbdList);
				Long end8 = System.currentTimeMillis();
				finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[BlackListAndTagRBDChecks/AgencyMarkup:" + (end8 - st8) + "]");
				CallLog.info(102, "Time Taken ::: FlightWsService:responseForFlightSearchResult:validateBlackListAndTagRBDChecks line 311::" + (end8 - st8));
			}
			jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
		}
		Long end = System.currentTimeMillis();
		if(finalSearchResponseBean !=null) {
			finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[totalResponse:" + (end - startTime) + "]");
		}
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - startTime;
		CallLog.info(100, "responseForAllFlightSearchResult :: TOTAL RESPONSE time----->>>" + timeTaken);
		CallLog.info(102, "####### Breakup of responseForAllFlightSearchResult End #########");
		return jsonString;
	}
	
	@SuppressWarnings({ "unchecked" })
	public String responseForFlightSearchResultFromSupplier(FlightSearchRequestBean requestBean, ResultBean resultBeanAirline, List<BlackOutFlightModel> blackList, List<FlightTagModel> flightTagList, ResultBean rbdResultBean){
		Integer expiryTimeCache = Integer.parseInt(messageSource.getMessage(FLIGHT_RESULT_EXPIRE_TIME, null ,"30", null));
		CallLog.info(102,"####### Breakup of responseForAllFlightSearchResultFromSupplier start #########");
		Long startTime = System.currentTimeMillis();
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		String jsonString = null;
		String jsonStringTopTen = null;
		String searchFlightKeyAll = requestBean.getFlightSearchKey() + "_All";
		String searchFlightKeyTopTen = requestBean.getFlightSearchKey() + "_TopTen";
		Boolean isPlatingCarrierKeyFound = false;
		String platingCarrierKey = requestBean.getFlightPlatingCarrierKey();
		ResultBean resultBeanPltCarr = RedisCacheUtil.isSearchKeyInCacheFlight(platingCarrierKey,redisService);
		if(resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
			isPlatingCarrierKeyFound = resultBeanPltCarr.getResultBoolean();
		}		
		Long st2= System.currentTimeMillis();
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_ONE);

		ResultBean resultBeanProduct = productService.getProducts(productModel);
		Long en2 = System.currentTimeMillis();
		Long timeRuleReadDB = en2 - st2;
		CallLog.info(102,"Time Taken ::: FlightWsService:responseForAllFlightSearchResultFromSupplier:ReadRuleFromDB ::"+timeRuleReadDB);
		if (resultBeanProduct != null && !resultBeanProduct.isError()) {
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) {
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {
					Date ruleLastUpdated = productModal.getRuleLastUpdated();
					Date kieSessionLastUpdated = KieSessionService.kieSessionLastUpdated;
					if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
						CallLog.info(105,"Rules for Flight Not changed.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+kieSessionLastUpdated+"(Time : "+kieSessionLastUpdated.getTime()+").");
						isRuleTextUpdated = true;
					}
								
					if (KieSessionService.kieSessFlight != null && isRuleTextUpdated) {
						KieSessionService.getKieSessionFlight();
						KieSessionService.kieSessionLastUpdatedFlightSearch.put(requestBean.getFlightSearchKey(),new Date());
					} else {
						boolean flagTextRule = KieSessionService.writeRuleTextFlight(productModal.getRuleText());
						if (flagTextRule) {
							KieSessionService.kieSessFlight = null;
							KieSessionService.getKieSessionFlight();
							KieSessionService.kieSessionLastUpdatedFlightSearch.put(requestBean.getFlightSearchKey(), new Date());
						}			
					}
				} else {
					KieSessionService.kieSessFlight = null;
					CallLog.info(105, "No approved flight rules found.");
				}

			}
		}
		Long end2= System.currentTimeMillis();
		Long timeKieSession = end2 - en2;
		CallLog.info(102,"Time Taken ::: FlightWsService:responseForAllFlightSearchResultFromSupplier:kieSessionFlight ::"+timeKieSession);
		CallLog.info(102,"Time Taken ::: FlightWsService:responseForAllFlightSearchResultFromSupplier: Total(ReadRuleFromDB + kieSessionFlight) ::"+(end2-st2));
		
		FlightSearchResponseBean finalSearchResponseBean = null;
		FlightCalendarFareSearchResponseBean finalCalSearchResponseBean = null;
		
		ResultBean resultBeanRule = null;
		ResultBean resultBeanPlatingCache = null;
		String platingCarrierString = "";
		if (requestBean.getFlexibleDates()) {

			finalCalSearchResponseBean = AirHelperUtil.processToFlightSearchResponseDateFlexible(serviceRequestBean, requestBean);
			resultBeanRule = applyRuleOnFlightCalSearchResponse(requestBean, finalCalSearchResponseBean, resultBeanAirline);

			if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList() != null && !resultBeanRule.getResultList().isEmpty()) {
				List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
				finalCalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
			}
			if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList2() != null && !resultBeanRule.getResultList2().isEmpty()) {
				List<FlightCalendarOption> updatedFlightCalenderList = (List<FlightCalendarOption>) resultBeanRule.getResultList2();
				finalCalSearchResponseBean.setCalFlightOptions(updatedFlightCalenderList);
			}
			jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
			if(finalCalSearchResponseBean!=null && finalCalSearchResponseBean.getCalFlightOptions()!=null && !finalCalSearchResponseBean.getCalFlightOptions().isEmpty()) {
				RedisCacheUtil.setResponseInCacheFlight(searchFlightKeyAll, jsonString, redisService,expiryTimeCache);
			}
			if (finalCalSearchResponseBean != null) {
				if (isPlatingCarrierKeyFound) {
					resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
					if (resultBeanPlatingCache != null && !resultBeanPlatingCache.isError()) {
						platingCarrierString = resultBeanPlatingCache.getResultString();
					} else {
						List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
						platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
						if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
							RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
						}
					}
					finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
				} else {
					List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
					platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);
					if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
						RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
					}
					finalCalSearchResponseBean.setPlatingcarrierString(platingCarrierString);
				}
			}

			AirHelperUtil airHelperUtil = new AirHelperUtil();
			if (finalCalSearchResponseBean != null) {
				ResultBean resultBeanAgencymarkup = settingAgencyMarkupCalender(requestBean, finalCalSearchResponseBean);
				if (resultBeanAgencymarkup != null && !resultBeanAgencymarkup.isError()) {
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) resultBeanAgencymarkup.getResultObject();
				}
				ResultBean resultBeanOffer = settingOfferDetailsCalender(requestBean, finalCalSearchResponseBean);
				if (resultBeanOffer != null && !resultBeanOffer.isError()) {
					finalCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) resultBeanOffer.getResultObject();
				}
				Long st8 = System.currentTimeMillis();
				List<RbdModel> rbdList = null;
				if (rbdResultBean.getResultList() != null && !rbdResultBean.getResultList().isEmpty()) {
					rbdList = (List<RbdModel>) rbdResultBean.getResultList();
				}
				airHelperUtil.validateBlackListAndTagRBDChecksForCalendarFare(finalCalSearchResponseBean, blackList, flightTagList, requestBean, rbdList);
				Long end8 = System.currentTimeMillis();
				CallLog.info(102, "Time Taken ::: FlightWsService:responseForAllFlightSearchResultFromSupplier:validateBlackListAndTagRBDChecksForCalendarFare line 311::" + (end8 - st8));

			}
			jsonString = CommonUtil.convertIntoJson(finalCalSearchResponseBean);
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(100, "TOTAL RESPONSE time----->>>" + timeTaken);
			CallLog.info(102,"####### Breakup of responseForAllFlightSearchResultFromSupplier End #########");
			return jsonString;
		} else {
			Long st6 = System.currentTimeMillis();
			finalSearchResponseBean = AirHelperUtil.processToFlightSearchResponse(serviceRequestBean, requestBean);
			Long end6 = System.currentTimeMillis();
			CallLog.info(102, "Time Taken ::: FlightWsService:responseForAllFlightSearchResultFromSupplier:processToFlightSearchResponse line 275::" + (end6 - st6));

			Long st7 = System.currentTimeMillis();
			resultBeanRule = applyRuleOnFlightSearchResponse(requestBean, finalSearchResponseBean, resultBeanAirline);

			if (resultBeanRule != null && !resultBeanRule.isError() && resultBeanRule.getResultList() != null && !resultBeanRule.getResultList().isEmpty()) {
				if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
					List<FlightOption> updatedFlightOptions = (List<FlightOption>) resultBeanRule.getResultList();
					finalSearchResponseBean.setOnwardFlightOptions(updatedFlightOptions);
				} else {
					List<RoundTripFlightOption> updatedFlightOptions = (List<RoundTripFlightOption>) resultBeanRule.getResultList();
					finalSearchResponseBean.setRoundTripFlightOptions(updatedFlightOptions);
				}
			}
			Long end7 = System.currentTimeMillis();
			if(finalSearchResponseBean!=null)
				finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[ruleReadFromDB:" + timeRuleReadDB + ",kieSessionCreate:" + timeKieSession +  ",ApplyRuleResultFromSupplier:" + (end7 - st7) + "]");
			CallLog.info(102, "Time Taken ::: FlightWsService:responseForAllFlightSearchResultFromSupplier:applyRuleOnFlightSearchResponse line 280::" + (end7 - st7));

			
			jsonString = CommonUtil.convertIntoJson(finalSearchResponseBean);
			boolean flagOneWayMultiCity = (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) && finalSearchResponseBean!=null && finalSearchResponseBean.getOnwardFlightOptions()!=null && !finalSearchResponseBean.getOnwardFlightOptions().isEmpty();
			boolean flagRoundTrip = ROUNDTRIP.equalsIgnoreCase(requestBean.getTripType()) && finalSearchResponseBean!=null && finalSearchResponseBean.getRoundTripFlightOptions()!=null && !finalSearchResponseBean.getRoundTripFlightOptions().isEmpty();
			if (flagOneWayMultiCity || flagRoundTrip) {
				RedisCacheUtil.setResponseInCacheFlight(searchFlightKeyAll, jsonString, redisService,expiryTimeCache);
			}
				
			//Top Ten Result Caching
			if(finalSearchResponseBean !=null) {
				FlightSearchResponseBean flightSearchResponseTopTen = new FlightSearchResponseBean();
				flightSearchResponseTopTen.setAirArabiaCurrConversionFound(finalSearchResponseBean.isAirArabiaCurrConversionFound());
				flightSearchResponseTopTen.setAmadeusCurrConversionFound(finalSearchResponseBean.isAmadeusCurrConversionFound());
				flightSearchResponseTopTen.setFlyDubaiCurrConversionFound(finalSearchResponseBean.isFlyDubaiCurrConversionFound());
				flightSearchResponseTopTen.setIndigoCurrConversionFound(finalSearchResponseBean.isIndigoCurrConversionFound());
				flightSearchResponseTopTen.setSpiceCurrConversionFound(finalSearchResponseBean.isSpiceCurrConversionFound());
				flightSearchResponseTopTen.setTripType(finalSearchResponseBean.getTripType());
				flightSearchResponseTopTen.setTimeLogs(finalSearchResponseBean.getTimeLogs());
				
				if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
					List<FlightOption> updatedFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
					List<FlightOption> flightOptionListTopTen;
					if (!updatedFlightOptions.isEmpty() && updatedFlightOptions.size() > 10)
					{
						flightOptionListTopTen = updatedFlightOptions.subList(0, 10);
					}
					else
					{
						flightOptionListTopTen = updatedFlightOptions;
					}
					flightSearchResponseTopTen.setOnwardFlightOptions(flightOptionListTopTen);		
					flightSearchResponseTopTen.setTotalResult(finalSearchResponseBean.getTotalResult());
					jsonStringTopTen = CommonUtil.convertIntoJson(flightSearchResponseTopTen);
					if(flightSearchResponseTopTen!=null && flightSearchResponseTopTen.getOnwardFlightOptions()!=null && !flightSearchResponseTopTen.getOnwardFlightOptions().isEmpty()) {
						RedisCacheUtil.setResponseInCacheFlight(searchFlightKeyTopTen, jsonStringTopTen, redisService,expiryTimeCache);
					}
				} else if(ROUNDTRIP.equalsIgnoreCase(requestBean.getTripType())) {			
					List<RoundTripFlightOption> updatedFlightOptions = finalSearchResponseBean.getRoundTripFlightOptions();
					List<RoundTripFlightOption> roundTripFlightOptionListTopTen;
					if (!updatedFlightOptions.isEmpty() && updatedFlightOptions.size() > 10)
					{
						roundTripFlightOptionListTopTen = updatedFlightOptions.subList(0, 10);
					}
					else
					{
						roundTripFlightOptionListTopTen = updatedFlightOptions;
					}
					flightSearchResponseTopTen.setRoundTripFlightOptions(roundTripFlightOptionListTopTen);
					flightSearchResponseTopTen.setTotalResult(finalSearchResponseBean.getTotalResult());
					jsonStringTopTen = CommonUtil.convertIntoJson(flightSearchResponseTopTen);		
					if(flightSearchResponseTopTen!=null && flightSearchResponseTopTen.getRoundTripFlightOptions()!=null && !flightSearchResponseTopTen.getRoundTripFlightOptions().isEmpty()) {
						RedisCacheUtil.setResponseInCacheFlight(searchFlightKeyTopTen, jsonStringTopTen, redisService,expiryTimeCache);
					}
				}
			
			}
			
			if (finalSearchResponseBean != null) {
				if (isPlatingCarrierKeyFound) {
					resultBeanPlatingCache = RedisCacheUtil.getResponseFromCacheFlight(platingCarrierKey, redisService);
					if (resultBeanPlatingCache == null || resultBeanPlatingCache.isError()) {
						if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
							List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
							platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);

						} else {
							List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
							platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);
						}
						if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
							RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
						}
					}

				} else {
					if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
						List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
						platingCarrierString = fetchUniquePlatingCarriers(onwardFlightOptions);

					} else {
						List<RoundTripFlightOption> roundTripOptions = finalSearchResponseBean.getRoundTripFlightOptions();
						platingCarrierString = fetchUniquePlatingCarriersRoundTrip(roundTripOptions);
					}
					if (resultBeanPltCarr != null && !resultBeanPltCarr.isError()) {
						RedisCacheUtil.setResponseInCacheFlight(platingCarrierKey, platingCarrierString, redisService,expiryTimeCache);
					}
				}
			}
			Long end = System.currentTimeMillis();
			if(finalSearchResponseBean !=null) {
				finalSearchResponseBean.setTimeLogs(finalSearchResponseBean.getTimeLogs() + "[totalResponse:" + (end - startTime) + "]");
			}

			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(100, "responseForAllFlightSearchResultFromSupplier :: TOTAL RESPONSE time----->>>" + timeTaken);
			CallLog.info(102,"####### Breakup of responseForAllFlightSearchResultFromSupplier End #########");
			return jsonStringTopTen;
		}		
	}
	public String responseForTopTenFlightSearchResult(FlightSearchRequestBean requestBean){		
		Long startTime = System.currentTimeMillis();
		String jsonString = null;
		String searchFlightKeyTopTen = requestBean.getFlightSearchKey() + "_TopTen";
		Boolean isSearchKeyFound = false;
		
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheFlight(searchFlightKeyTopTen,redisService);
		if(resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		}	
		if(isSearchKeyFound && requestBean.isDataFromCacheOrNot()) {
			ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(searchFlightKeyTopTen, redisService);
			if (resultBeanCache != null && !resultBeanCache.isError()) {
				jsonString = resultBeanCache.getResultString();
			}
		}
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - startTime;
		CallLog.info(100, "responseForTopTenFlightSearchResult :: TOTAL RESPONSE time----->>>" + timeTaken);
		return jsonString;
	}
	public String responseForTopTenPopularSectorFlightSearchResult(FlightSearchRequestBean requestBean){		
		Long startTime = System.currentTimeMillis();
		String jsonString = null;
		String flightSearchKeyTopTen = requestBean.getFlightSearchKey() + "_PopularSector_TopTen";
		Boolean isSearchKeyFound = false;
		
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheFlight(flightSearchKeyTopTen,redisService);
		if(resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		}	
		if(isSearchKeyFound) {
			ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(flightSearchKeyTopTen, redisService);
			if (resultBeanCache != null && !resultBeanCache.isError()) {
				jsonString = resultBeanCache.getResultString();
			}
		}
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - startTime;
		CallLog.info(100, "responseForTopTenFlightSearchResult :: TOTAL RESPONSE time----->>>" + timeTaken);
		return jsonString;
	}
	
    public ResultBean applyRuleOnFlightCalSearchResponse(FlightSearchRequestBean requestBean,FlightCalendarFareSearchResponseBean finalCalSearchResponseBean, ResultBean resultBeanAirline) {
    	ResultBean resultBean = new ResultBean();
		
    	if(finalCalSearchResponseBean!=null){
    		if(ONEWAY.equalsIgnoreCase(requestBean.getTripType())) {
        		List<FlightOption> onwardFlightOptions = finalCalSearchResponseBean.getOnwardFlightOptions();
        		if(onwardFlightOptions!=null && !onwardFlightOptions.isEmpty()) {
        			Long st1= System.currentTimeMillis();
        	    	List<AirportModal> airportModals = new ArrayList<>();
        	    	ResultBean resultBeanAirport = orgService.searchAllAirportNew(null);
        	    	if(resultBeanAirport!=null && !resultBeanAirport.isError() && resultBeanAirport.getResultList()!=null) {
        	    		airportModals = (List<AirportModal>)resultBeanAirport.getResultList();
        	    	}
        	    	Long end1= System.currentTimeMillis();
        			CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse:searchAllAirport line 311::"+(end1-st1));

        			Long st2= System.currentTimeMillis();
        			Map<Integer, String> credentialMap = new HashMap<>();
        			ServiceConfigModel serviceConfigModel = requestBean.getServiceConfigModel();
        			if(serviceConfigModel!=null) {
        				List<com.ws.services.flight.config.SupplierConfigModal> supplierConfigList = serviceConfigModel.getSupplier();
        				if(supplierConfigList!=null && !supplierConfigList.isEmpty()) {
        					for(com.ws.services.flight.config.SupplierConfigModal supplierConfig : supplierConfigList ) {
        						ResultBean resultBeanPcc = geoLocationService.fetchObjectById(supplierConfig.getPccId(),SupplierCredentialModal.class);
        						if(resultBeanPcc!=null && !resultBeanPcc.isError() && resultBeanPcc.getResultObject()!=null) {
        							SupplierCredentialModal supplierCredentialModal = (SupplierCredentialModal)resultBeanPcc.getResultObject();
        							credentialMap.put(supplierCredentialModal.getCredentialId(), supplierCredentialModal.getCredentialName());
        						}
        					}
        				}
        			}
        			CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse: credential Map prepration::"+(System.currentTimeMillis()-st2));
        			requestBean.setCredentialMap(credentialMap);
        			resultBean = RuleSimulationService.applyRuleOnOnwardFlight(onwardFlightOptions, requestBean, geoLocationService, credentialService,resultBeanAirline,airportModals,null,productService);
        		}
        		
        	} 
        	
        	ResultBean resultBeanCal = new ResultBean();
        	List<FlightCalendarOption> flightCalenderOptionList = finalCalSearchResponseBean.getCalFlightOptions();
        	List<FlightCalendarOption> updatedFlightCalList = new ArrayList<>();
        	if(flightCalenderOptionList != null && !flightCalenderOptionList.isEmpty()) {
        		Long st1= System.currentTimeMillis();
    	    	List<AirportModal> airportModals = new ArrayList<>();
    	    	ResultBean resultBeanAirport = orgService.searchAllAirportNew(null);
    	    	if(resultBeanAirport!=null && !resultBeanAirport.isError() && resultBeanAirport.getResultList()!=null) {
    	    		airportModals = (List<AirportModal>)resultBeanAirport.getResultList();
    	    	}
    	    	Long end1= System.currentTimeMillis();
    			CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse:searchAllAirport line 311::"+(end1-st1));

    			Long st2= System.currentTimeMillis();
    			Map<Integer, String> credentialMap = new HashMap<>();
    			ServiceConfigModel serviceConfigModel = requestBean.getServiceConfigModel();
    			if(serviceConfigModel!=null) {
    				List<com.ws.services.flight.config.SupplierConfigModal> supplierConfigList = serviceConfigModel.getSupplier();
    				if(supplierConfigList!=null && !supplierConfigList.isEmpty()) {
    					for(com.ws.services.flight.config.SupplierConfigModal supplierConfig : supplierConfigList ) {
    						ResultBean resultBeanPcc = geoLocationService.fetchObjectById(supplierConfig.getPccId(),SupplierCredentialModal.class);
    						if(resultBeanPcc!=null && !resultBeanPcc.isError() && resultBeanPcc.getResultObject()!=null) {
    							SupplierCredentialModal supplierCredentialModal = (SupplierCredentialModal)resultBeanPcc.getResultObject();
    							credentialMap.put(supplierCredentialModal.getCredentialId(), supplierCredentialModal.getCredentialName());
    						}
    					}
    				}
    			}
    			CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse: credential Map prepration::"+(System.currentTimeMillis()-st2));
    			requestBean.setCredentialMap(credentialMap);
        		for(FlightCalendarOption flightCalenderOption : flightCalenderOptionList) {
        			if(flightCalenderOption != null) {
        				resultBeanCal = RuleSimulationService.applyRuleCalenderFlight(flightCalenderOption, requestBean, geoLocationService, credentialService,resultBeanAirline,airportModals,productService);
            			if(resultBeanCal.getResultObject() != null) {
            				flightCalenderOption = (FlightCalendarOption)resultBeanCal.getResultObject();
            			}
        			}
        			updatedFlightCalList.add(flightCalenderOption);
        		}
        	}
        	resultBean.setResultList2(updatedFlightCalList);
        	if(KieSessionService.kieSessFlight!=null) {
				KieSessionService.kieSessFlight.dispose();
			}
    	}
    	return resultBean;
    }
    
    @SuppressWarnings("unchecked")
	public ResultBean applyRuleOnFlightSearchResponse(FlightSearchRequestBean requestBean,FlightSearchResponseBean finalSearchResponseBean, ResultBean resultBeanAirline) {
    	
    	ResultBean resultBean =new ResultBean();
    	CallLog.info(102,"\n####### Breakup of applyRuleOnFlightSearchResponse start #########");
    	if(ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
    		if(finalSearchResponseBean!=null){
    			List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
    			if(onwardFlightOptions!=null && !onwardFlightOptions.isEmpty()) {
    		    	Long st1= System.currentTimeMillis();
    		    	List<AirportModal> airportModals = new ArrayList<>();
    		    	ResultBean resultBeanAirport = orgService.searchAllAirportNew(null);
    		    	if(resultBeanAirport!=null && !resultBeanAirport.isError() && resultBeanAirport.getResultList()!=null) {
    		    		airportModals = (List<AirportModal>)resultBeanAirport.getResultList();
    		    	}
    		    	Long end1= System.currentTimeMillis();
    				CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse:searchAllAirport line 311::"+(end1-st1));

    				Long st2= System.currentTimeMillis();
    				Map<Integer, String> credentialMap = new HashMap<>();
    				ServiceConfigModel serviceConfigModel = requestBean.getServiceConfigModel();
    				if(serviceConfigModel!=null) {
    					List<com.ws.services.flight.config.SupplierConfigModal> supplierConfigList = serviceConfigModel.getSupplier();
    					if(supplierConfigList!=null && !supplierConfigList.isEmpty()) {
    						for(com.ws.services.flight.config.SupplierConfigModal supplierConfig : supplierConfigList ) {
    							ResultBean resultBeanPcc = geoLocationService.fetchObjectById(supplierConfig.getPccId(),SupplierCredentialModal.class);
    							if(resultBeanPcc!=null && !resultBeanPcc.isError() && resultBeanPcc.getResultObject()!=null) {
    								SupplierCredentialModal supplierCredentialModal = (SupplierCredentialModal)resultBeanPcc.getResultObject();
    								credentialMap.put(supplierCredentialModal.getCredentialId(), supplierCredentialModal.getCredentialName());
    							}
    						}
    					}
    				}
    				CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse: credential Map prepration::"+(System.currentTimeMillis()-st2));
    				requestBean.setCredentialMap(credentialMap);
    				resultBean = RuleSimulationService.applyRuleOnOnwardFlight(onwardFlightOptions, requestBean, geoLocationService, credentialService,resultBeanAirline,airportModals,null,productService);
    				if(KieSessionService.kieSessFlight!=null) {
    					KieSessionService.kieSessFlight.dispose();
    				}
    			}
        		
        	}
    	} else {
    		if(finalSearchResponseBean!=null){
    			List<RoundTripFlightOption> flightOptions = finalSearchResponseBean.getRoundTripFlightOptions();
        		if(flightOptions!=null && !flightOptions.isEmpty()) {
    		    	Long st1= System.currentTimeMillis();
    		    	List<AirportModal> airportModals = new ArrayList<>();
    		    	ResultBean resultBeanAirport = orgService.searchAllAirportNew(null);
    		    	if(resultBeanAirport!=null && !resultBeanAirport.isError() && resultBeanAirport.getResultList()!=null) {
    		    		airportModals = (List<AirportModal>)resultBeanAirport.getResultList();
    		    	}
    		    	Long end1= System.currentTimeMillis();
    				CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse:searchAllAirport line 311::"+(end1-st1));

    				Long st2= System.currentTimeMillis();
    				Map<Integer, String> credentialMap = new HashMap<>();
    				ServiceConfigModel serviceConfigModel = requestBean.getServiceConfigModel();
    				if(serviceConfigModel!=null) {
    					List<com.ws.services.flight.config.SupplierConfigModal> supplierConfigList = serviceConfigModel.getSupplier();
    					if(supplierConfigList!=null && !supplierConfigList.isEmpty()) {
    						for(com.ws.services.flight.config.SupplierConfigModal supplierConfig : supplierConfigList ) {
    							ResultBean resultBeanPcc = geoLocationService.fetchObjectById(supplierConfig.getPccId(),SupplierCredentialModal.class);
    							if(resultBeanPcc!=null && !resultBeanPcc.isError() && resultBeanPcc.getResultObject()!=null) {
    								SupplierCredentialModal supplierCredentialModal = (SupplierCredentialModal)resultBeanPcc.getResultObject();
    								credentialMap.put(supplierCredentialModal.getCredentialId(), supplierCredentialModal.getCredentialName());
    							}
    						}
    					}
    				}
    				CallLog.info(102,"Time Taken ::: applyRuleOnFlightSearchResponse: credential Map prepration::"+(System.currentTimeMillis()-st2));
    				requestBean.setCredentialMap(credentialMap);
    				resultBean = RuleSimulationService.applyRuleOnRoundTripFlight(flightOptions, requestBean, geoLocationService, credentialService,resultBeanAirline,airportModals,null,productService);
    				if(KieSessionService.kieSessFlight!=null) {
    					KieSessionService.kieSessFlight.dispose();
    				}
        		}
    			
    		}
    	}
    	CallLog.info(102,"####### Breakup of applyRuleOnFlightSearchResponse end #########");
    	return resultBean;
    }
	
    private String fetchUniquePlatingCarriers(List<FlightOption> flightOptionList) {
    	
    	String uniquePlatingCarrierString = null;
    	if(null != flightOptionList && !flightOptionList.isEmpty()) {
    		List<String> uniquePlatingCarrier = new ArrayList<>();
    		for(FlightOption flightOption : flightOptionList) {
    			if(flightOption.getPlatingCarrier()!= null && !uniquePlatingCarrier.contains(flightOption.getPlatingCarrier())) {
    				uniquePlatingCarrier.add(flightOption.getPlatingCarrier());
    			}			
    		}
    		uniquePlatingCarrierString = CommonUtil.convertIntoJson(uniquePlatingCarrier);
    	}
    	
    	return uniquePlatingCarrierString;
    }
    
	private String fetchUniquePlatingCarriersRoundTrip(List<RoundTripFlightOption> flightOptionList) {

		String uniquePlatingCarrierString = null;
		if (null != flightOptionList && !flightOptionList.isEmpty()) {
			List<String> uniquePlatingCarrier = new ArrayList<>();
			for (RoundTripFlightOption flightOption : flightOptionList) {
				if (flightOption.getOnwardFlightOption() != null && flightOption.getOnwardFlightOption().getPlatingCarrier()!= null && !uniquePlatingCarrier.contains(flightOption.getOnwardFlightOption().getPlatingCarrier())) {
					uniquePlatingCarrier.add(flightOption.getOnwardFlightOption().getPlatingCarrier());
				}
			}
			uniquePlatingCarrierString = CommonUtil.convertIntoJson(uniquePlatingCarrier);
		}

		return uniquePlatingCarrierString;
	}

	public String fareConfirmation(FareConfirmRequestBean fareConfirmRequestBean)
	{
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.fareConfirm.toString());

		serviceRequestBean.setRequestBean(fareConfirmRequestBean);
		serviceRequestBean.setServiceConfigModel(fareConfirmRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		/*
		 * //** Will return the bean containing corresponding service response
		 * bean, response status and error message
		 */
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			FareConfirmResponseBean responseBean = (FareConfirmResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}
		return jsonString;
	}

	public String fareRuleCall(FareRuleRequestBean fareRuleRequestBean)
	{
		String jsonString=null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.fareRule.toString());
		serviceRequestBean.setRequestBean(fareRuleRequestBean);
		serviceRequestBean.setServiceConfigModel(fareRuleRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		/*
		 * //** Will return the bean containing corresponding service response
		 * bean, response status and error message
		 */
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			FareRuleResponseBean responseBean = (FareRuleResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}
		return jsonString;
	}
	public String doBookingForFlight(FlightBookingRequestBean bookingRequestBean){
		String jsonString=null;
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
		
		if(!bookingRequestBean.getOnwardFlightOption().isImportPnr())
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
	public String bookAncillary(FlightBookAncillaryServiceRequestBean requestBean)
	{
		String jsonString = null;
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
	  public String doTicketingForFlight(FlightBookingRequestBean bookingRequestBean, FlightBookingResponseBean bookingResponseBean){
	        String jsonString=null;
	        

	        ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
	        CallLog.info(0, ProductEnum.Flight.issueTicket);
	        serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
	        ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
	        serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
	       
	        serviceRequestBean.setRequestBean(bookingRequestBean);
	        serviceRequestBean.setServiceConfigModel(bookingRequestBean.getServiceConfigModel());
	        ServiceResponseBean flightBookResponseBean = null;
	        if(!bookingRequestBean.getOnwardFlightOption().isImportPnr()){
	        	serviceRequestBean.setServiceName(ProductEnum.Flight.issueTicket.toString());
	        	flightBookResponseBean = serviceResolverFactory.getServiceResponse(bookingResponseBean);
	        }
			else{
				serviceRequestBean.setServiceName(ProductEnum.Flight.issueTicketImportPNR.toString());
				flightBookResponseBean = serviceResolverFactory.getServiceResponse();
			}
	        
	        if (flightBookResponseBean != null) {
	            bookingResponseBean = (FlightBookingResponseBean) flightBookResponseBean.getResponseBean();
	            jsonString = CommonUtil.convertIntoJson(bookingResponseBean);
	        }
	        return jsonString;
	    }
	public String getSSRService(RetrieveServiceQuotesRequestBean retrieveServiceQuotesRequestBean)
	{
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
	public String pnrTicketCancel(FlightCancelRequestBean flightCancelRequestBean) {
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
	
	public String payLaterLcc(FlightBookingRequestBean flightBookingRequestBean)
	{
		String jsonString = null;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();

		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.paylaterbooking.toString());
		serviceRequestBean.setServiceConfigModel(flightBookingRequestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(flightBookingRequestBean);

		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		
		if (serviceResponseBean != null) {
			FlightBookingResponseBean flightBookResponseBean = (FlightBookingResponseBean) serviceResponseBean.getResponseBean();
			jsonString = CommonUtil.convertIntoJson(flightBookResponseBean);
		}
		CallLog.info(0, "payLaterLcc Response Json ::::: " + jsonString);
		return jsonString;
	}
	
	public String issueTicketForAmadeous(FlightBookingRequestBean flightBookingRequestBean)
	{
		String jsonString = null;
		
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.payAndTicketHoldPNR.toString());
		serviceRequestBean.setServiceConfigModel(flightBookingRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceRequestBean.setRequestBean(flightBookingRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean != null) {
			FlightBookingResponseBean flightBookResponseBean = (FlightBookingResponseBean) serviceResponseBean.getResponseBean();
			jsonString = CommonUtil.convertIntoJson(flightBookResponseBean);
		}
		CallLog.info(0, "issueTicketForAmadeous Response Json ::::: " + jsonString);
		return jsonString;
	}
	public String fareReCheckOnPaymentConfirmation(FareConfirmRequestBean fareConfirmRequestBean)
	{
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.fareConfirmAfterPayClick.toString());
		serviceRequestBean.setRequestBean(fareConfirmRequestBean);
		serviceRequestBean.setServiceConfigModel(fareConfirmRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			FareConfirmResponseBean responseBean = (FareConfirmResponseBean) serviceResponseBean.getResponseBean();
			if (responseBean != null) {
				jsonString = CommonUtil.convertIntoJson(responseBean);
			}
		}
		return jsonString;
	}
	 public String validateGdsTicketPccExistForHold(FlightBookingRequestBean bookingRequestBean,boolean lccFlag){
	        String jsonString;
	        ResultBean resultBean = new ResultBean();
	        
	        if(!lccFlag){
	        	 ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
	 	        serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
	 	        ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
	 	        serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
	 	       
	 	        serviceRequestBean.setRequestBean(bookingRequestBean);
	 	        serviceRequestBean.setServiceConfigModel(bookingRequestBean.getServiceConfigModel());
	 	        ServiceResponseBean serviceResponseBean;
	 	        
	 			serviceRequestBean.setServiceName(ProductEnum.Flight.checkGDSTicketPCCHoldPNR.toString());
	 			serviceResponseBean = serviceResolverFactory.getServiceResponse();
	 			  
	 	        if (serviceResponseBean != null) {
	 	        	String responseStatus = serviceResponseBean.getResponseStatus().toString();
	 	        	if(responseStatus!=null && responseStatus.equalsIgnoreCase("Success")) {
	 	        		resultBean.setResultBoolean(true);
	 	        	} else {
	 	        		resultBean.setResultBoolean(false);
	 	        		resultBean.setResultString(serviceResponseBean.getErrorMsg());
	 	        	}
	 	        }
			} else {
				resultBean.setResultBoolean(true);
			}
	       
	        jsonString = CommonUtil.convertIntoJson(resultBean);
	        return jsonString;
	    }
	 
	 public String checkSeatAvailability(SeatConfirmRequestBean seatConfirmRequestBean)
	 {
		String jsonString = null;
		ServiceResponseBean serviceResponseBean;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.seatAvailability.toString());
		serviceRequestBean.setRequestBean(seatConfirmRequestBean);
		serviceRequestBean.setServiceConfigModel(seatConfirmRequestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			SeatConfirmResponseBean seatConfirmResponseBean = (SeatConfirmResponseBean)serviceResponseBean.getResponseBean();
			if (seatConfirmResponseBean != null) {
				jsonString = CommonUtil.convertIntoJson(seatConfirmResponseBean);
			}
		}
		return jsonString;
	 }
	 
	 public String releasePNR(ImportPNRServiceRequestBean requestBean)
	 {
		String jsonString = null;
		ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());
		serviceRequestBean.setServiceName(ProductEnum.Flight.releasePNR.toString());
		serviceRequestBean.setRequestBean(requestBean);
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		if (serviceResponseBean.getResponseBean() != null) {
			ImportPNRResponseBean importPNRResponseBean= (ImportPNRResponseBean)serviceResponseBean.getResponseBean();
			if (importPNRResponseBean != null) {
				jsonString = CommonUtil.convertIntoJson(importPNRResponseBean);
			}
		}
		return jsonString;
	 }

	private ResultBean settingAgencyMarkup(FlightSearchRequestBean requestBean, FlightSearchResponseBean finalSearchResponseBean) {

		ResultBean resultBean = new ResultBean();
		try {
			int totalPax = requestBean.getNoOfAdults() + requestBean.getNoOfChilds() + requestBean.getNoOfInfants();
			
			
			AgencyMarkupModel agencyMarkupModel = new AgencyMarkupModel();
			agencyMarkupModel.setStatus(1);
			agencyMarkupModel.setProductRefId(1);
			int domOrInt = 1;
			if (requestBean.getOriginCountryId().equalsIgnoreCase(requestBean.getDestinationCountryId()))
			{
				domOrInt = 0;
			}
			agencyMarkupModel.setDomOrInternational(domOrInt);
			agencyMarkupModel.setAgencyId(requestBean.getAgentId() != null ? Integer.parseInt(requestBean.getAgentId()) : 0);
			agencyMarkupModel.setCorporateId(requestBean.getCorporateId());
			List<Object> listAgencyMarkup = agentManager.fetchAgencyMarkup(agencyMarkupModel);
			
			////////////////////
		//	List<Object> listAgencyMarkup = requestBean.getListAgencyMarkup();
			if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
				if (finalSearchResponseBean != null) {
					List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
					if (onwardFlightOptions != null && !onwardFlightOptions.isEmpty()) {
						for (FlightOption flightOption : onwardFlightOptions) {
							boolean fareFlag = flightOption != null && flightOption.getFlightFare() != null;
							if (fareFlag) {
								double t3SellingPrice = flightOption.getTotalFare() + flightOption.getFlightFare().getMarkupPrice() + flightOption.getFlightFare().getServiceChargePrice() - flightOption.getFlightFare().getDiscountPrice();
								double agencyMarkup = 0;
								if (listAgencyMarkup != null && !listAgencyMarkup.isEmpty()) {
									for (int i = 0; i < listAgencyMarkup.size(); i++) {
										Object[] agencyList = (Object[]) listAgencyMarkup.get(i);
										if (agencyList != null && agencyList.length > 4) {
											String airline = agencyList[3].toString();
											if (airline != null && !"null".equalsIgnoreCase(airline) && airline.trim().equalsIgnoreCase(flightOption.getPlatingCarrier().trim())) {
												boolean condition = agencyList[2].toString() != null && !"null".equalsIgnoreCase(agencyList[2].toString()) && !"".equalsIgnoreCase(agencyList[2].toString());
												if(t3SellingPrice > 0 && condition) {
													if(agencyList[1]!=null && agencyList[1].toString().equals("1")) {// For %
														agencyMarkup = t3SellingPrice * (condition ? Double.parseDouble(agencyList[2].toString()) : 0)/100;
														break;
													} else if(agencyList[1]!=null && agencyList[1].toString().equals("0")) {// For value
														agencyMarkup = condition ? Double.parseDouble(agencyList[2].toString()) : 0;
														break;
													}
												}	
											}
										}
									}
								}
								flightOption.getFlightFare().setAgencyMarkup(agencyMarkup * totalPax);
							}
						}
						finalSearchResponseBean.setOnwardFlightOptions(onwardFlightOptions);
					}
				}
			} else {
				if (finalSearchResponseBean != null) {
					List<RoundTripFlightOption> roundTripFlightOptions = finalSearchResponseBean.getRoundTripFlightOptions();
					if (roundTripFlightOptions != null && !roundTripFlightOptions.isEmpty()) {
						for (RoundTripFlightOption roundTripFlightOption : roundTripFlightOptions) {
							FlightOption onwardFlightOption = roundTripFlightOption.getOnwardFlightOption();
							FlightOption returnFlightOption = roundTripFlightOption.getReturnFlightOption();
							boolean fareFlag = onwardFlightOption != null && onwardFlightOption.getFlightFare() != null && returnFlightOption != null && returnFlightOption.getFlightFare() != null;
							if (fareFlag) {
								double t3SellingPrice = roundTripFlightOption.getTotalJourneyFare() + roundTripFlightOption.getMarkupPrice() + roundTripFlightOption.getServiceChargePrice() - roundTripFlightOption.getDiscountPrice();
								double agencyMarkup = 0;
								if (listAgencyMarkup != null && !listAgencyMarkup.isEmpty()) {
									for (int i = 0; i < listAgencyMarkup.size(); i++) {
										Object[] agencyList = (Object[]) listAgencyMarkup.get(i);
										if (agencyList != null && agencyList.length > 4) {
											String airline = agencyList[3].toString();
											if (airline != null && !"null".equalsIgnoreCase(airline) && airline.trim().equalsIgnoreCase(onwardFlightOption.getPlatingCarrier().trim())) {
												boolean condition = agencyList[2].toString() != null && !"null".equalsIgnoreCase(agencyList[2].toString()) && !"".equalsIgnoreCase(agencyList[2].toString());
												if(t3SellingPrice > 0 && condition) {
													if(agencyList[1]!=null && agencyList[1].toString().equals("1")) {// For %
														agencyMarkup = t3SellingPrice * (condition ? Double.parseDouble(agencyList[2].toString()) : 0)/100;
														break;
													} else if(agencyList[1]!=null && agencyList[1].toString().equals("0")) {// For value
														agencyMarkup = condition ? Double.parseDouble(agencyList[2].toString()) : 0;
														break;
													}
												}
											}
										}
									}
								}
								onwardFlightOption.getFlightFare().setAgencyMarkup(agencyMarkup * totalPax);
								
							}
						}
					}

				}
			}
			resultBean.setResultObject(finalSearchResponseBean);
		} catch (Exception e) {
			resultBean.setIserror(true);
		}

		return resultBean;
	}
	
	private ResultBean settingAgencyMarkupCalender(FlightSearchRequestBean requestBean, FlightCalendarFareSearchResponseBean finalSearchResponseBean) {

		ResultBean resultBean = new ResultBean();
		try {
			
			
			AgencyMarkupModel agencyMarkupModel = new AgencyMarkupModel();
			agencyMarkupModel.setStatus(1);
			agencyMarkupModel.setProductRefId(1);
			int domOrInt = 1;
			if (requestBean.getOriginCountryId().equalsIgnoreCase(requestBean.getDestinationCountryId()))
			{
				domOrInt = 0;
			}
			agencyMarkupModel.setDomOrInternational(domOrInt);
			agencyMarkupModel.setAgencyId(requestBean.getAgentId() != null ? Integer.parseInt(requestBean.getAgentId()) : 0);
			agencyMarkupModel.setCorporateId(requestBean.getCorporateId());
			List<Object> listAgencyMarkup = agentManager.fetchAgencyMarkup(agencyMarkupModel);
			
			
			
			int totalPax = requestBean.getNoOfAdults() + requestBean.getNoOfChilds() + requestBean.getNoOfInfants();
			//List<Object> listAgencyMarkup = requestBean.getListAgencyMarkup();
			if (finalSearchResponseBean != null) {
				if (ONEWAY.equalsIgnoreCase(requestBean.getTripType())) {
					List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
					if (onwardFlightOptions != null && !onwardFlightOptions.isEmpty()) {
						for (FlightOption flightOption : onwardFlightOptions) {
							boolean fareFlag = flightOption != null && flightOption.getFlightFare() != null;
							if (fareFlag) {
								double t3SellingPrice = flightOption.getTotalFare() + flightOption.getFlightFare().getMarkupPrice() + flightOption.getFlightFare().getServiceChargePrice() - flightOption.getFlightFare().getDiscountPrice();
								double agencyMarkup = 0;
								if (listAgencyMarkup != null && !listAgencyMarkup.isEmpty()) {
									for (int i = 0; i < listAgencyMarkup.size(); i++) {
										Object[] agencyList = (Object[]) listAgencyMarkup.get(i);
										if (agencyList != null && agencyList.length > 4) {
											String airline = agencyList[3].toString();
											if (airline != null && !"null".equalsIgnoreCase(airline) && airline.trim().equalsIgnoreCase(flightOption.getPlatingCarrier().trim())) {
												boolean condition = agencyList[2].toString() != null && !"null".equalsIgnoreCase(agencyList[2].toString()) && !"".equalsIgnoreCase(agencyList[2].toString());
												if(agencyList[1]!=null && agencyList[1].toString().equals("1")) {// For %
													agencyMarkup = t3SellingPrice * (condition ? Double.parseDouble(agencyList[2].toString()) : 0)/100;
													break;
												} else if(agencyList[1]!=null && agencyList[1].toString().equals("0")) {// For value
													agencyMarkup = condition ? Double.parseDouble(agencyList[2].toString()) : 0;
													break;
												}
											}
										}
									}
								}
								flightOption.getFlightFare().setAgencyMarkup(agencyMarkup * totalPax);
							}
						}
						finalSearchResponseBean.setOnwardFlightOptions(onwardFlightOptions);
					}
				}
				List<FlightCalendarOption> flightCalOptions = finalSearchResponseBean.getCalFlightOptions();
				if (flightCalOptions != null && !flightCalOptions.isEmpty()) {
					for (FlightCalendarOption flightCalOption : flightCalOptions) {
						boolean fareFlag = flightCalOption != null && flightCalOption.getFlightFare() != null;
						if (fareFlag) {
							double t3SellingPrice = flightCalOption.getTotalFare() + flightCalOption.getFlightFare().getMarkupPrice() + flightCalOption.getFlightFare().getServiceChargePrice() - flightCalOption.getFlightFare().getDiscountPrice();
							double agencyMarkup = 0;
							if (listAgencyMarkup != null && !listAgencyMarkup.isEmpty()) {
								for (int i = 0; i < listAgencyMarkup.size(); i++) {
									Object[] agencyList = (Object[]) listAgencyMarkup.get(i);
									if (agencyList != null && agencyList.length > 4) {
										String airline = agencyList[3].toString();
										if (airline != null && !"null".equalsIgnoreCase(airline) && airline.trim().equalsIgnoreCase(flightCalOption.getPlatingCarrier().trim())) {
											boolean condition = agencyList[2].toString() != null && !"null".equalsIgnoreCase(agencyList[2].toString()) && !"".equalsIgnoreCase(agencyList[2].toString());
											if(agencyList[1]!=null && agencyList[1].toString().equals("1")) {// For %
												agencyMarkup = t3SellingPrice * (condition ? Double.parseDouble(agencyList[2].toString()) : 0)/100;
												break;
											} else if(agencyList[1]!=null && agencyList[1].toString().equals("0")) {// For value
												agencyMarkup = condition ? Double.parseDouble(agencyList[2].toString()) : 0;
												break;
											}
										}
									}
								}
							}
							flightCalOption.getFlightFare().setAgencyMarkup(agencyMarkup * totalPax);
						}
					}
					finalSearchResponseBean.setCalFlightOptions(flightCalOptions);
				}
			}	
			resultBean.setResultObject(finalSearchResponseBean);
		} catch (Exception e) {
			TTPortalLog.printStackTrace(107, e);
			resultBean.setIserror(true);
		}

		return resultBean;
	}
	public ResultBean flightCancelTicket(FlightCancelRequestBean cancelRequestBean) {
		ResultBean resultBean =new ResultBean();
		try {
			ServiceRequestBean serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.Flight.toString());		
			serviceRequestBean.setServiceName(ProductEnum.Flight.cancelTicket.toString());
			serviceRequestBean.setServiceConfigModel(cancelRequestBean.getServiceConfigModel());
			serviceRequestBean.setRequestBean(cancelRequestBean);		
			ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResolverFactory.setServiceRequestBean(serviceRequestBean);
			ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
			FlightCancelResponseBean ticketCancelResponseBean=(FlightCancelResponseBean)serviceResponseBean.getResponseBean();
			resultBean.setResultObject(ticketCancelResponseBean);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			TTPortalLog.printStackTrace(107, e);
		}
		return resultBean;
	}
	
	private ResultBean settingOfferDetails(FlightSearchRequestBean requestBean, FlightSearchResponseBean finalSearchResponseBean) {

		ResultBean resultBean = new ResultBean();
		try {
			OfferPricingModel offerPricingModel = new OfferPricingModel();
			offerPricingModel.setProductId(1);
			offerPricingModel.setStatus(1);
			offerPricingModel.setApprovalStatus(1);
			ResultBean resultBeanOffers = offerPricingService.fetchAllActiveOffers(offerPricingModel);
			List<OfferPricingModel> offerList = null;
			if(resultBeanOffers!=null && !resultBeanOffers.isError()) {
				offerList = (List<OfferPricingModel>)resultBeanOffers.getResultList();
			}
			
			if (ONEWAY.equalsIgnoreCase(requestBean.getTripType()) || MULTICITY.equalsIgnoreCase(requestBean.getTripType())) {
				if (finalSearchResponseBean != null) {
					List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
					if (onwardFlightOptions != null && !onwardFlightOptions.isEmpty()) {
						for (FlightOption flightOption : onwardFlightOptions) {
							 if(offerList!=null && !offerList.isEmpty()) {
								 for(OfferPricingModel offerModel : offerList) {
									 
									 boolean adultOfferFlag = flightOption.getDiscountIdAdult() != null && flightOption.getDiscountIdAdult() == offerModel.getDiscountId();
									 boolean childOfferFlag = flightOption.getDiscountIdChild() != null && flightOption.getDiscountIdAdult() == offerModel.getDiscountId();
									 boolean infantOfferFlag = flightOption.getDiscountIdInfant() != null && flightOption.getDiscountIdInfant() == offerModel.getDiscountId();
									 	 
									 if(adultOfferFlag || childOfferFlag || infantOfferFlag) {
										 flightOption.setOfferId(offerModel.getOfferId());
										 flightOption.setOfferImgPath(offerModel.getImagePath());
										 flightOption.setOfferDesc(offerModel.getDesc());
										 break;
									 } 
								 }
							 }
						}
						finalSearchResponseBean.setOnwardFlightOptions(onwardFlightOptions);
					}
				}
			} else {
				if (finalSearchResponseBean != null) {
					List<RoundTripFlightOption> roundTripFlightOptions = finalSearchResponseBean.getRoundTripFlightOptions();
					if (roundTripFlightOptions != null && !roundTripFlightOptions.isEmpty()) {
						for (RoundTripFlightOption roundTripFlightOption : roundTripFlightOptions) {
							 for(OfferPricingModel offerModel : offerList) {
								 
								 boolean adultOfferFlag = roundTripFlightOption.getDiscountIdAdult() != null && roundTripFlightOption.getDiscountIdAdult() == offerModel.getDiscountId();
								 boolean childOfferFlag = roundTripFlightOption.getDiscountIdChild() != null && roundTripFlightOption.getDiscountIdAdult() == offerModel.getDiscountId();
								 boolean infantOfferFlag = roundTripFlightOption.getDiscountIdInfant() != null && roundTripFlightOption.getDiscountIdInfant() == offerModel.getDiscountId();
								 	 
								 if(adultOfferFlag || childOfferFlag || infantOfferFlag) {
									 roundTripFlightOption.setOfferId(offerModel.getOfferId());
									 roundTripFlightOption.setOfferImgPath(offerModel.getImagePath());
									 roundTripFlightOption.setOfferDesc(offerModel.getDesc());
									 break;
								 } 
							 }
						}
					}

				}
			}
			resultBean.setResultObject(finalSearchResponseBean);
		} catch (Exception e) {
			CallLog.printStackTrace(102, e);
			resultBean.setIserror(true);
		}

		return resultBean;
	}
	

	@SuppressWarnings("unchecked")
	private ResultBean settingOfferDetailsCalender(FlightSearchRequestBean requestBean, FlightCalendarFareSearchResponseBean finalSearchResponseBean) {

		ResultBean resultBean = new ResultBean();
		try {
			OfferPricingModel offerPricingModel = new OfferPricingModel();
			offerPricingModel.setProductId(1);
			offerPricingModel.setStatus(1);
			offerPricingModel.setApprovalStatus(1);
			ResultBean resultBeanOffers = offerPricingService.fetchAllActiveOffers(offerPricingModel);
			List<OfferPricingModel> offerList = null;
			if(resultBeanOffers!=null && !resultBeanOffers.isError()) {
				offerList = (List<OfferPricingModel>)resultBeanOffers.getResultList();
			}
			if (finalSearchResponseBean != null && ONEWAY.equalsIgnoreCase(requestBean.getTripType())) {

				List<FlightOption> onwardFlightOptions = finalSearchResponseBean.getOnwardFlightOptions();
				if (onwardFlightOptions != null && !onwardFlightOptions.isEmpty()) {
					for (FlightOption flightOption : onwardFlightOptions) {
						if (offerList != null && !offerList.isEmpty()) {
							for (OfferPricingModel offerModel : offerList) {

								boolean adultOfferFlag = flightOption.getDiscountIdAdult() != null && flightOption.getDiscountIdAdult() == offerModel.getDiscountId();
								boolean childOfferFlag = flightOption.getDiscountIdChild() != null && flightOption.getDiscountIdAdult() == offerModel.getDiscountId();
								boolean infantOfferFlag = flightOption.getDiscountIdInfant() != null && flightOption.getDiscountIdInfant() == offerModel.getDiscountId();

								if (adultOfferFlag || childOfferFlag || infantOfferFlag) {
									flightOption.setOfferId(offerModel.getOfferId());
									flightOption.setOfferImgPath(offerModel.getImagePath());
									flightOption.setOfferDesc(offerModel.getDesc());
									break;
								}
							}
						}
					}
					finalSearchResponseBean.setOnwardFlightOptions(onwardFlightOptions);
				}

			}
			resultBean.setResultObject(finalSearchResponseBean);
		} catch (Exception e) {
			CallLog.printStackTrace(102, e);
			resultBean.setIserror(true);
		}

		return resultBean;
	}
}
