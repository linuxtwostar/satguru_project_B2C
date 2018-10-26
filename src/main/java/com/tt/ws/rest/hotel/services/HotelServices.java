package com.tt.ws.rest.hotel.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.tt.nc.common.util.QueryConstant;
import com.tt.nc.common.util.TTLog;
import com.tt.satguruportal.agent.model.AgencyMarkup;
import com.tt.satguruportal.hotel.model.HotelResultsRequestBean;
import com.tt.satguruportal.hotel.service.HotelBookService;
import com.tt.satguruportal.hotel.util.HotelHelperUtil;
import com.tt.satguruportal.login.bean.UserSessionBean;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.service.AgentService;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ts.rest.ruleengine.service.KieSessionService;
import com.tt.ts.rest.ruleengine.service.RuleSimulationService;
import com.tt.ws.rest.hotel.bean.HotelSearchReqDataBean;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.tt.ws.rest.hotel.bean.HotelsFareRuleRespBean;
import com.tt.ws.rest.hotel.manager.HotelManager;
import com.tt.ws.rest.hotel.model.HotelCityDetailsModel;
import com.tt.ws.rest.hotel.model.HotelCountryDetailsModel;
import com.tt.ws.rest.hotel.model.HotelRecommendationModel;
import com.tt.ws.rest.hotel.model.HotelSatGuruAddressModel;
import com.tt.ws.rest.hotel.model.HotelSatGuruAmnetiesModel;
import com.tt.ws.rest.hotel.model.HotelSatMappingModel;
import com.tt.ws.rest.hotel.model.HotelSatguruDescModel;
import com.tt.ws.rest.hotel.model.HotelSatguruImgModel;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.tt.ws.rest.hotel.utils.HotelUtil;
import com.tt.ws.rest.hotel.utils.SavePriceResultsThread;
import com.tt.ws.rest.hotel.utils.SaveStaticResultsThread;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.util.CallLog;

@Service
public class HotelServices 
{
	 ServiceResolverFactory serviceResolverFactory = null;
	 RoomRequestBean roomRequestBean = null;
	 ServiceRequestBean serviceRequestBean = null;
	 ServiceResponseBean serviceResponseBean = null;
	 String logStr = "[HotelServices]";
	 private boolean flag = false;
	 private boolean tvcFlag = true;
	 private boolean hbdFlag = true;
	 
	@Autowired
	private HotelManager hotelManager;
	
	@Autowired
    RedisService redisService;
	
	@Autowired
	com.tt.satguruportal.hotel.manager.HotelManager hotelManagerPortal;
	
	@Autowired
	HotelBookService hotelBookService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	HotelHelperUtil hotelHelperUtil;
	
	
	public List<HotelSatguruModel> getHotelCompleteDetails(int pageNo,String cityName,String countryName,String hotelName) throws Exception {
		return hotelManager.getHotelCompleteDetails(pageNo,cityName,countryName,hotelName);
	}
	
	public List<HotelSatguruModel> getHotelInfo(int hotelId) throws Exception {
		return hotelManager.getHotelInfo(hotelId);
	}
	public ResultBean fetchHotelCities()
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<HotelCityDetailsModel> hotelCityModals;
			resultBean.setIserror(false);
			hotelCityModals = hotelManager.fetchHotelCities();
			resultBean.setResultList(hotelCityModals);
		}
		catch (Exception e)
		{
			TTLog.printStackTrace(12, e);
		}
		return resultBean;
	}
	public ResultBean fetchHotelCountries()
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<HotelCountryDetailsModel> hotelCountryModals;
			resultBean.setIserror(false);
			hotelCountryModals = hotelManager.fetchHotelCountries();
			resultBean.setResultList(hotelCountryModals);
		}
		catch (Exception e)
		{
			TTLog.printStackTrace(12, e);
		}
		return resultBean;
	}
	public List<Object[]> getHotelMapInfo(String hotelId) throws Exception
	{
		return hotelManager.getHotelMapInfo(hotelId);
	}
	public List<HotelSatMappingModel> getHotelAllDetailsList(int clarifiId) throws Exception
	{
		return hotelManager.getHotelAllDetailsList(clarifiId);
	}
	
	public List<Object[]> getPInventoryPriceData(HotelSearchRequestBean hotelSearchRequestBean,HotelManager hotelManager) throws Exception
	{
		List<Object[]> resultList = null;
		resultList = hotelManager.getPInventoryPriceData(hotelSearchRequestBean);
		return resultList;
	}
	
	public List<HotelSatguruModel> getHotelStaticInfo(String cityName, String countyName,String hotelName,Double lat, Double lon,Double distance,String limit,String hotelCheckIn, String hotelCheckOut)throws Exception 
	{
		logStr = logStr + " getHotelStaticInfoNew() ";
		List<HotelSatguruModel> satModelList = null;
		HashMap<String,HotelSatguruModel> satModelMap = null;
		Set<HotelSatguruDescModel> satDescList = null;
		Set<HotelSatGuruAmnetiesModel> satAmnetiesList = null;
		Set<HotelSatguruImgModel> satImgList = null;
		Set<HotelSatGuruAddressModel> satAddressList = null;
		Set<HotelSatMappingModel> satMappingList = null;
		
		HotelSatguruModel satModel = null;
		HotelSatguruDescModel descModel = null;
		HotelSatGuruAmnetiesModel amnetiesModel = null;
		HotelSatguruImgModel imgModel = null;
		HotelSatGuruAddressModel addressModel = null;
		HotelSatMappingModel mappingModel = null;
		HotelRecommendationModel hotelRecommendation = null;
		List<Object[]> resultList = null;
		long startTime = 0;
		long endTime = 0;
		long timeTaken = 0;
		try
		{
			startTime = System.currentTimeMillis();
			long startTime1 = System.currentTimeMillis();
			resultList = hotelManager.getHotelStaticInfo(cityName, countyName,hotelName,lat,lon,distance,limit,hotelCheckIn,hotelCheckOut);
			endTime = System.currentTimeMillis();
	  		timeTaken = endTime - startTime;
	  		CallLog.info(103, "TOTAL TIME TO FETCH HOTEL STATIC DATA FROM DATABASE:::" + timeTaken/1000+" SECONDS || TIME[MILISECONDS]::"+timeTaken);
			if(null != resultList && !resultList.isEmpty())
			{
				satModelMap = new HashMap<>();
				Object[] col=null;
				int size=resultList.size();
				startTime = System.currentTimeMillis();
				for(int i=0;i<size;i++)
				{
					col= resultList.get(i);
					if(null != col[2] && null != col[11])
					{
						if(!"###HOTEL###".equalsIgnoreCase(col[2].toString()) && "###HOTEL###".equalsIgnoreCase(col[11].toString()))
						{
							satDescList = new HashSet<>();
							satAmnetiesList = new HashSet<>();
							satMappingList = new HashSet<>();
							satAddressList = new HashSet<>();
							satImgList = new  HashSet<>();							

							satModel = new HotelSatguruModel();
							satModel.setHotelDescription(satDescList);
							satModel.setHotelAmneties(satAmnetiesList);
							satModel.setHotelSatSuppModel(satMappingList);
							satModel.setSatguruHotelAddress(satAddressList);
							satModel.setHotelImages(satImgList);						
							
							
							satModel.setHotelId(Integer.parseInt(col[0].toString()));
							if(col[1]!=null)
							  satModel.setClarifiId(Integer.parseInt(col[1].toString()));
							
							satModel.setHotelName(col[2].toString());
							
							if(null != col[3])
								satModel.setHotelStarRating(col[3].toString());
							if(null != col[4] && Integer.parseInt(col[4].toString())==1)
								satModel.setEnabled(true);
							else
								satModel.setEnabled(false);
							if(null!=col[5])
								satModel.setLatitude(col[5].toString());
							if(null!=col[6])
								satModel.setLongitude(col[6].toString());
							if(null!=col[7])
								satModel.setSatguruHotelId(col[7].toString());
							if(null != col[8])
								satModel.setStatus(Integer.parseInt(col[8].toString()));
							if(null != col[9])
								satModel.setCityName(col[9].toString());
							if(null != col[10])
								satModel.setCityId(col[10].toString());							
							if(col.length>12 && null != col[12]){
								satModel.setDistanceFromGeoLocation((col[12].toString()));
							}
							if(null != col[13]){
								satModel.setHotelFaxNo(col[13].toString());
							}
							if(null != col[14]){
								satModel.setHotelPhoneNo(col[14].toString());
							}
							if(null != col[15]){
								satModel.setHotelWebsiteUrl((col[15].toString()));
							}
							
							if(col[16]!=null)
								  satModel.setHotelHighlights(col[16].toString());
							satModelMap.put(col[0].toString(), satModel);
							
						}
						if(!"###DESCRIPTIONS###".equalsIgnoreCase(col[2].toString()) && "###DESCRIPTIONS###".equalsIgnoreCase(col[11].toString()))
						{
							descModel = new HotelSatguruDescModel();
							
							if(null != col[2])
								descModel.setDescType(col[2].toString());
							if(null != col[3])
								descModel.setDescValue(col[3].toString());
							satModelMap.get(col[0].toString()).getHotelDescription().add(descModel);
							
						}
						if(!"###AMENITIES###".equalsIgnoreCase(col[2].toString()) && "###AMENITIES###".equalsIgnoreCase(col[11].toString()))
						{
							amnetiesModel = new HotelSatGuruAmnetiesModel();
							if(null != col[2])
							{
								String amenityName = col[2].toString().trim();
								amnetiesModel.setAmenitiesName(amenityName);
								
								//1
								if("Airport Shuttle".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/Airport_shuttle.png");
									amnetiesModel.setAmenitiesImgAlt("airport_shuttle_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//2
								else if("Fitness Facility".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/Fitness Facility.png");
									amnetiesModel.setAmenitiesImgAlt("fitness_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//3
								else if("Internet".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/internet.png");
									amnetiesModel.setAmenitiesImgAlt("internet_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//4
								else if("Parking".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/parking.png");
									amnetiesModel.setAmenitiesImgAlt("parking_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//5
								else if("Pets Allowed".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/pet_allowed.png");
									amnetiesModel.setAmenitiesImgAlt("pets_allowed_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//6
								else if("Restaurant".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/restaurant.png");
									amnetiesModel.setAmenitiesImgAlt("restaurant_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//7
								else if("Swimming Pool".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/swimming_icon.png");
									amnetiesModel.setAmenitiesImgAlt("swimming_pool_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//8
								else if("Laundry Services".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/laundry_service.png");
									amnetiesModel.setAmenitiesImgAlt("laundry_services_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//9
								else if("Television".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/television.png");
									amnetiesModel.setAmenitiesImgAlt("television_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//10
								else if("Non Smoking".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/no-smoking.png");
									amnetiesModel.setAmenitiesImgAlt("spa_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//11
								else if("Spa".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/spa_icon.png");
									amnetiesModel.setAmenitiesImgAlt("spa_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//12
								else if("Bar".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/bar.png");
									amnetiesModel.setAmenitiesImgAlt("bar_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//13
								else if("Currency Exchange".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/currency_exchange.png");
									amnetiesModel.setAmenitiesImgAlt("currency_exchange_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//14
								else if("Business Center".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/bussiness_center.png");
									amnetiesModel.setAmenitiesImgAlt("business_center_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//15
								else if("Breakfast".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/breakfast.png");
									amnetiesModel.setAmenitiesImgAlt("breakfast_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//16
								else if("Casino".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/casino.png");
									amnetiesModel.setAmenitiesImgAlt("casino_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//17
								else if("Childcare Service".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/Childcare_activities.png");
									amnetiesModel.setAmenitiesImgAlt("childcare_service_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//18
								else if("Lounge".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/lounge.png");
									amnetiesModel.setAmenitiesImgAlt("lounge_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								//19
								else if("Valet Parking".equalsIgnoreCase(amenityName))
								{
									amnetiesModel.setAmenitiesImgPath("../static/images/icons/valet_parking.png");
									amnetiesModel.setAmenitiesImgAlt("valet_parking_icon");
									amnetiesModel.setAmenitiesValue(amenityName);
								}
								
							}
							if(null != col[3])
								amnetiesModel.setAmenitiesId(Integer.parseInt(col[3].toString()));
							satModelMap.get(col[0].toString()).getHotelAmneties().add(amnetiesModel);							
						}
						if(!"###SUPP_MAP###".equalsIgnoreCase(col[2].toString()) && "###SUPP_MAP###".equalsIgnoreCase(col[11].toString()))
						{
							mappingModel = new HotelSatMappingModel();
							if(null != col[2])
								mappingModel.setSourceSupp(col[2].toString());
							if(null != col[3])
								mappingModel.setSuppCode(col[3].toString());
							if(null != col[4])
								mappingModel.setSuppHotelCode(col[4].toString());
							satModelMap.get(col[0].toString()).getHotelSatSuppModel().add(mappingModel);							
						}
						if(!"###ADDRESSES###".equalsIgnoreCase(col[2].toString()) && "###ADDRESSES###".equalsIgnoreCase(col[11].toString()))
						{
							addressModel = new HotelSatGuruAddressModel();
							if(null != col[2])
								addressModel.setAddress1(col[2].toString());
							
							satModelMap.get(col[0].toString()).getSatguruHotelAddress().add(addressModel);
						}
						if(!"###RECOMMENDATIONS###".equalsIgnoreCase(col[2].toString()) && "###RECOMMENDATIONS###".equalsIgnoreCase(col[11].toString()))
						{
							hotelRecommendation = new HotelRecommendationModel();
							if(null != col[2])
								hotelRecommendation.setRecommendationName(col[2].toString());
							if(null != col[3])
								hotelRecommendation.setStatus(Integer.parseInt(col[3].toString()));
							
							satModelMap.get(col[0].toString()).setHotelRecommendation(hotelRecommendation);
						}
						if(!"###IMAGES###".equalsIgnoreCase(col[2].toString()) && "###IMAGES###".equalsIgnoreCase(col[11].toString()))
						{
							imgModel = new HotelSatguruImgModel();
							if(null != col[2])
								imgModel.setImgCaption(col[2].toString());
							if(null != col[3])
								imgModel.setImgSupplierUrl(col[3].toString());
							
							satModelMap.get(col[0].toString()).getHotelImages().add(imgModel);
						}
						if("CHECK-IN".equalsIgnoreCase(col[2].toString()) && "###CHECKINOUT###".equalsIgnoreCase(col[11].toString()))
						{
							satModelMap.get(col[0].toString()).setHotelCheckInTime(col[3].toString());;
						}
						if("CHECK-OUT".equalsIgnoreCase(col[2].toString()) && "###CHECKINOUT###".equalsIgnoreCase(col[11].toString()))
						{
							satModelMap.get(col[0].toString()).setHotelCheckOutTime(col[3].toString());
						}
					}
					
				}
				satModelList = new ArrayList<>(satModelMap.values());
				CallLog.info(1, logStr +  "==== satModelList::================================"+satModelList.size());
				endTime = System.currentTimeMillis();
		  		timeTaken = endTime - startTime;
				CallLog.info(103, "TOTAL TIME TO SET PROCEDURE DATA INTO MODEL:::" + timeTaken/1000+" SECONDS || TIME[MILISECONDS]::"+timeTaken);
				resultList=null;
			}
		  	logStr = null;
		  	long endTime1 = System.currentTimeMillis();
		  	long timeTaken1 = endTime1 - startTime1;
		  	CallLog.info(103, "FINAL TOTAL TIME TO GET DATABASE RESULT:::" + timeTaken1/1000+" SECONDS || TIME[MILISECONDS]::"+timeTaken1);
		}
		catch(Exception e)
		{
			CallLog.printStackTrace(1, e);
		}
		return satModelList;
	}

	
///////////////////Search Ten Filtered Result Starts//////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public List<HotelSatguruModel> getTenFilteredResult(HotelResultsRequestBean resultsReqBean,HotelSearchRequestBean reqBean,UserSessionBean userSessionBean,RuleSimulationService ruleSimulationService,AgentService agentServices) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;
		serviceRequestBean = new ServiceRequestBean();
		if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
			reqBean = HotelUtil.keyForTenFilteredResults(reqBean, userSessionBean.getAgentId());
		else
			reqBean = HotelUtil.keyForTenFilteredResults(reqBean, userSessionBean.getBranchId());
		String corpId = reqBean.getCorporateId();
		String custNationality = reqBean.getCustNationality();
		String hotelSearchKey = reqBean.getHotelSearchKey();
		int totalPax = reqBean.getTotalPax();
		int totalRooms = reqBean.getNoOfRooms();
		int noOfNights = reqBean.getDuration();
		String destinationCity = reqBean.getCityName();
		String destinationCountry = reqBean.getCountryName();
		String hotelName = reqBean.getHotelName();
		List<HotelSatguruModel> filteredJsonArr = null;
		CallLog.info(1, "hotelSearchKey for getFilteredResult Call === " + hotelSearchKey);
		Boolean isSearchKeyFound = false;
		Boolean hotelFound = false; 
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey, redisService);
		HotelSearchRespDataBean hotelSearchRespBean = null;
		HotelSearchRespDataBean ruleAppliedRespBean = null;

		if (null != userSessionBean.getAgencyCode())
			resultsReqBean = fetchAgencyMarkup(resultsReqBean, agentServices,userSessionBean);

		if (resultBean != null && !resultBean.isError()) {
			isSearchKeyFound = resultBean.getResultBoolean();
			if(!isSearchKeyFound)
			{
				reqBean.setHotelName("");
				if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
					reqBean = HotelUtil.keyForTenFilteredResults(reqBean, userSessionBean.getAgentId());
				else
					reqBean = HotelUtil.keyForTenFilteredResults(reqBean, userSessionBean.getBranchId());	
				hotelSearchKey = reqBean.getHotelSearchKey();
				resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey, redisService);
				if (resultBean != null && !resultBean.isError())
				{
					isSearchKeyFound = resultBean.getResultBoolean();
			    }
			}
			reqBean.setHotelName(hotelName);
		}
		if (isSearchKeyFound) {
			/* Start Code to apply rule engine */
			Long startTime = System.currentTimeMillis();
			boolean isRuleTextUpdated = false;
			/*ProductModel productModel = new ProductModel();
			productModel.setStatus(1);
			productModel.setProductCode(QueryConstant.RULE_GROUP_TWO);
			ResultBean resultBeanProduct = productService.getProducts(productModel);
			if (resultBeanProduct != null && !resultBeanProduct.isError()) {
				List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
				if (productList != null && !productList.isEmpty()) {
					ProductModel productModal = productList.get(0);
					if (productModal != null && productModal.getRuleText() != null
							&& !productModal.getRuleText().isEmpty()) {

						Date ruleLastUpdated = productModal.getRuleLastUpdated();
						Date kieSessionLastUpdated = KieSessionService.kieHotelSessionLastUpdated;
						if (ruleLastUpdated != null && kieSessionLastUpdated != null
								&& ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
							CallLog.info(105,
									"Rules for Hotel Not changed.\nRule Last Updated Time :: " + ruleLastUpdated
											+ "(Time : " + ruleLastUpdated.getTime()
											+ " ). KissSession Last Updated Time :: " + kieSessionLastUpdated
											+ "(Time : " + kieSessionLastUpdated.getTime() + ").");
							isRuleTextUpdated = true;
						}
						if (KieSessionService.kieSessHotel != null && isRuleTextUpdated) {
							KieSessionService.getKieSessionHotel();

						} else {
							startingTime = System.currentTimeMillis();
							boolean flagTextRule = KieSessionService.writeRuleTextHotel(productModal.getRuleText());
							if (flagTextRule) {
								KieSessionService.kieSessHotel = null;
								KieSessionService.getKieSessionHotel();
							}
							CallLog.info(105, "Rules for Hotel Modified.\nRule Last Updated Time :: " + ruleLastUpdated
									+ "(Time : " + ruleLastUpdated.getTime() + " ). KissSession Last Updated Time :: "
									+ KieSessionService.kieHotelSessionLastUpdated + "(Time : "
									+ KieSessionService.kieHotelSessionLastUpdated.getTime() + ").");
						}
					}
					else {
						KieSessionService.kieSessHotel = null;
						CallLog.info(105, "No approved hotel rules found.");
					}
				}
			}*/
			startingTime = System.currentTimeMillis();
			resultBean = RedisCacheUtil.getResponseCacheHotelObject(hotelSearchKey, redisService);
			List<HotelSatguruModel> hotelModelList = (List<HotelSatguruModel>)resultBean.getResultObject();
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime - startTime) / 1000;
			totalTimeInMillis = endingTime - startingTime;
			CallLog.info(103, "TOTAL TIME TO FECTH FILTERED HOTELS RESPONSE FROM CACHE ::: " + totalTime+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
			if (null != hotelModelList && !hotelModelList.isEmpty()) {
				if (!isRuleTextUpdated) {
					startingTime = System.currentTimeMillis();
					
					if(null!=reqBean.getHotelName() && ""!=reqBean.getHotelName())
					{
						List<HotelSatguruModel> selectedHotel = HotelUtil.getSelectHotelFiltered(reqBean.getHotelName(),hotelModelList);
						if(null!=selectedHotel && !selectedHotel.isEmpty())
						{
						  filteredJsonArr = selectedHotel;
						  hotelFound = true;
						}
						else
							 hotelFound = false;
					}
					else
					{
						filteredJsonArr = hotelModelList;
						hotelFound = true;
					}
				if(hotelFound)
				 {
					 int count = 0;
					 for (HotelSatguruModel hotelJsonObj : filteredJsonArr) {
						Float grossPrice;
						Float totalAgencyMarkup = 0f;
						Float totalPrice;
						Float priceWithoutDiscPrice;
						HotelSearchRespDataBean hotelSearchResp = new HotelSearchRespDataBean();
						List<HotelsFareRuleRespBean> respList = new ArrayList<>();
						HotelsFareRuleRespBean fareRuleBean = new HotelsFareRuleRespBean();
						fareRuleBean.setFareFromTo(Double.parseDouble(hotelJsonObj.getOrgFare()));
						fareRuleBean.setHotelName(hotelJsonObj.getHotelName());
						Set<HotelSatMappingModel> satSuppArr = hotelJsonObj.getHotelSatSuppModel();
						Iterator<HotelSatMappingModel> itr = satSuppArr.iterator();
						String suppHotelCode = null;
						String suppCode = null;
						while(itr.hasNext())
						{
							HotelSatMappingModel model = itr.next();
							suppHotelCode = model.getSuppHotelCode();
							suppCode = model.getSuppCode();
						}
						
						if("DOT".equalsIgnoreCase(suppCode))
							suppHotelCode = suppHotelCode.split("\\|")[0];
						else if("GDI".equalsIgnoreCase(suppCode))
							suppHotelCode = suppHotelCode.split(";")[1];
						else if("HBD".equalsIgnoreCase(suppCode))
							suppHotelCode = suppHotelCode.split("-")[1];
						fareRuleBean.setHotelCode(suppHotelCode);
						fareRuleBean.setNoOfPax(totalPax);
						fareRuleBean.setNoOfRooms(totalRooms); 
						fareRuleBean.setDuration(noOfNights);
						fareRuleBean.setHotelGroup("");
						fareRuleBean.setStarRatingCode(hotelJsonObj.getHotelStarRating());
						fareRuleBean.setDestinationCountry(destinationCountry);
						fareRuleBean.setDestinationCity(destinationCity);
						fareRuleBean.setSupplierCode(suppCode);
						fareRuleBean.setBlackoutStartDate("");
						fareRuleBean.setBlackoutEndDate("");
						fareRuleBean.setRoomType("");
						fareRuleBean.setRatePlanType("");
						if(null != hotelJsonObj.getTaxsAmount() && !hotelJsonObj.getTaxsAmount().isEmpty())
							fareRuleBean.setTaxsAmount(hotelJsonObj.getTaxsAmount());
						else
							fareRuleBean.setTaxsAmount("0");
						respList.add(fareRuleBean);
						hotelSearchResp.setRespList(respList);
						hotelSearchRespBean = ruleSimulationService.applyRuleOnHotelResult(hotelSearchResp,KieSessionService.kieSessHotel,userSessionBean.getCountryId(),userSessionBean.getAgentId(),userSessionBean.getBranchId(),corpId,custNationality);
						HotelsFareRuleRespBean fareRuleResp = hotelSearchRespBean.getRespList().get(0);
						double orgFare = Double.parseDouble(hotelJsonObj.getOrgFare());
						Float discPrice = Float.parseFloat(String.valueOf(fareRuleResp.getDiscountPrice()));
						
						totalPrice = Float.parseFloat(String.valueOf(orgFare)) + Float.parseFloat(String.valueOf(fareRuleResp.getT3Price())) + Float.parseFloat(String.valueOf(fareRuleResp.getServiceChargePrice()));
						
						
						if (resultsReqBean.getTotalAgencyMarkUp() > 0)
							totalAgencyMarkup = resultsReqBean.getTotalAgencyMarkUp();

						if (resultsReqBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice- discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						
						priceWithoutDiscPrice = Float.parseFloat(String.valueOf(orgFare)) + Float.parseFloat(String.valueOf(fareRuleResp.getT3Price())) + Float.parseFloat(String.valueOf(fareRuleResp.getServiceChargePrice())) + totalAgencyMarkup;
						
						double priceWithoutServiceCharge = priceWithoutDiscPrice - Float.parseFloat(String.valueOf(fareRuleResp.getServiceChargePrice())) - discPrice;
						
						Float amountToPay = totalPrice - discPrice;
						filteredJsonArr.get(count).setT3Markup(String.valueOf(fareRuleResp.getT3Price()));
						filteredJsonArr.get(count).setServiceChargePrice(String.valueOf(fareRuleResp.getServiceChargePrice()));
						filteredJsonArr.get(count).setDiscountPrice(String.valueOf(fareRuleResp.getDiscountPrice()));
						filteredJsonArr.get(count).setPriceWithoutServiceCharge(Float.parseFloat(String.valueOf(priceWithoutServiceCharge)));
						filteredJsonArr.get(count).setPriceWithoutDiscount(priceWithoutDiscPrice);
						filteredJsonArr.get(count).setTotalAmtToPay(amountToPay);
						filteredJsonArr.get(count).setGrossPrice(grossPrice);
						filteredJsonArr.get(count).setTotalAgencyMarkup(totalAgencyMarkup);
						filteredJsonArr.get(count).setGrossPriceWithoutMarkup(String.valueOf(grossPrice - fareRuleResp.getT3Price()));
						filteredJsonArr.get(count).setT3Price(String.valueOf(amountToPay));
						
						if(discPrice>=grossPrice || discPrice>=priceWithoutServiceCharge)
							filteredJsonArr.remove(hotelJsonObj);
						
						count++;
					}
					jsonString = filteredJsonArr.toString();
					RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, jsonString, redisService);
					endingTime = System.currentTimeMillis();
					totalTime = (endingTime - startTime) / 1000;
					totalTimeInMillis = endingTime - startingTime;
					CallLog.info(103, "TOTAL TIME TO APPLY RULE ON CACHE RESPONSE ::: " + totalTime
							+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
				}
				else
					jsonString = "No Results";
				} 
				else 
				{
					if(null!=reqBean.getHotelName() && ""!=reqBean.getHotelName())
					{
						List<HotelSatguruModel> selectedHotel = HotelUtil.getSelectHotelFiltered(reqBean.getHotelName(),hotelModelList);
						if(null!=selectedHotel && !selectedHotel.isEmpty())
						{
						  filteredJsonArr = selectedHotel;
						  hotelFound = true;
						}
						else
							 hotelFound = false;
					}
					else
					{
						filteredJsonArr = hotelModelList;
						hotelFound = true;
					}
				if(hotelFound)
				 {
					int count = 0;
					for(HotelSatguruModel hotelJsonObj : filteredJsonArr)
					{
						Float grossPrice;
						Float totalPrice;
						Float totalAgencyMarkup = 0f;
						if (resultsReqBean.getTotalAgencyMarkUp() > 0)
						    totalAgencyMarkup = resultsReqBean.getTotalAgencyMarkUp();
						
						double orgFare = Double.parseDouble(hotelJsonObj.getOrgFare());
						Float discPrice = Float.parseFloat(String.valueOf(hotelJsonObj.getDiscountPrice()));
						
						totalPrice = Float.parseFloat(String.valueOf(orgFare)) + Float.parseFloat(String.valueOf(hotelJsonObj.getT3Markup())) + Float.parseFloat(String.valueOf(hotelJsonObj.getServiceChargePrice()));
						
						if (resultsReqBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice- discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						
						Float amountToPay = totalPrice - discPrice;
						filteredJsonArr.get(count).setGrossPrice(grossPrice);
						filteredJsonArr.get(count).setTotalAgencyMarkup(totalAgencyMarkup);
						filteredJsonArr.get(count).setGrossPriceWithoutMarkup(String.valueOf(grossPrice - Float.parseFloat(String.valueOf(hotelJsonObj.getT3Markup()))));
						filteredJsonArr.get(count).setT3Price(String.valueOf(amountToPay));
						
						count++;
					}
				}
					else
						return null;
				}
			} else
				return null;
		} else
			return null;

		return filteredJsonArr;
	}

///////////////////Get Ten Filtered Result Ends//////////////////////////////////////////	


	///////////////////Search Filtered Result Starts//////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public List<HotelSatguruModel> getFilteredResult(HotelResultsRequestBean resultsReqBean,HotelSearchRequestBean reqBean,UserSessionBean userSessionBean,RuleSimulationService ruleSimulationService,AgentService agentServices) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;	
		serviceRequestBean = new ServiceRequestBean();
		if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
			reqBean = HotelUtil.keyForFilteredResults(reqBean,userSessionBean.getAgentId());
		else
			reqBean = HotelUtil.keyForFilteredResults(reqBean,userSessionBean.getBranchId());	
		String corpId = reqBean.getCorporateId(); 
		String custNationality = reqBean.getCustNationality(); 
		String hotelSearchKey = reqBean.getHotelSearchKey();
		int totalPax = reqBean.getTotalPax();
		int totalRooms = reqBean.getNoOfRooms();
		int noOfNights = reqBean.getDuration();
		String destinationCity = reqBean.getCityName();
		String destinationCountry = reqBean.getCountryName();
		String hotelName = reqBean.getHotelName();
		CallLog.info(1, "hotelSearchKey for getFilteredResult Call === " + hotelSearchKey);
		Boolean isSearchKeyFound = false;
		Boolean hotelFound = false; 
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey,redisService);
		HotelSearchRespDataBean hotelSearchRespBean = null;
		if(null!=userSessionBean.getAgencyCode())
			resultsReqBean = fetchAgencyMarkup(resultsReqBean, agentServices,userSessionBean);
		
		List<HotelSatguruModel> filteredJsonArr = null;
		
		if (resultBean != null && !resultBean.isError()) {
			isSearchKeyFound = resultBean.getResultBoolean();
			if(!isSearchKeyFound)
			{
				reqBean.setHotelName("");
				if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
					reqBean = HotelUtil.keyForFilteredResults(reqBean, userSessionBean.getAgentId());
				else
					reqBean = HotelUtil.keyForFilteredResults(reqBean, userSessionBean.getBranchId());
				hotelSearchKey = reqBean.getHotelSearchKey();
				resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey, redisService);
				if (resultBean != null && !resultBean.isError())
				{
					isSearchKeyFound = resultBean.getResultBoolean();
			    }
			}
			reqBean.setHotelName(hotelName);
		}
		
		if(isSearchKeyFound)
		{
			/*Start Code to apply rule engine*/
			Long startTime = System.currentTimeMillis();
			boolean isRuleTextUpdated = false;
			ProductModel productModel = new ProductModel();
			productModel.setStatus(1);
			productModel.setProductCode(QueryConstant.RULE_GROUP_TWO);
			ResultBean resultBeanProduct = productService.getProducts(productModel);
			if (resultBeanProduct != null && !resultBeanProduct.isError()) {
				List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
				if (productList != null && !productList.isEmpty()) {
					ProductModel productModal = productList.get(0);
					if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {

						Date ruleLastUpdated = productModal.getRuleLastUpdated();
						Date kieSessionLastUpdated = KieSessionService.kieHotelSessionLastUpdated;
						if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime())
						{
							CallLog.info(105,"Rules for Hotel Not changed.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+kieSessionLastUpdated+"(Time : "+kieSessionLastUpdated.getTime()+").");
							isRuleTextUpdated = true;
						}
						if (KieSessionService.kieSessHotel != null && isRuleTextUpdated) {
							KieSessionService.getKieSessionHotel();
							
						} else {
							startingTime = System.currentTimeMillis(); 
							boolean flagTextRule = KieSessionService.writeRuleTextHotel(productModal.getRuleText());
							if (flagTextRule) {
								KieSessionService.kieSessHotel = null;
								KieSessionService.getKieSessionHotel();
								KieSessionService.kieHotelSessionLastUpdated = new Date();
							}
							CallLog.info(105,"Rules for Hotel Modified.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+KieSessionService.kieHotelSessionLastUpdated+"(Time : "+KieSessionService.kieHotelSessionLastUpdated.getTime()+").");
						}
					}
					else {
						KieSessionService.kieSessHotel = null;
						CallLog.info(105, "No approved hotel rules found.");
					}
				}
			}
			startingTime = System.currentTimeMillis(); 
			resultBean = RedisCacheUtil.getResponseCacheHotelObject(hotelSearchKey,redisService);
			List<HotelSatguruModel> hotelSatModelList =(List<HotelSatguruModel>)resultBean.getResultObject();
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime-startTime)/1000;
			totalTimeInMillis = endingTime-startingTime;
			CallLog.info(103, "TOTAL TIME TO FECTH FILTERED HOTELS RESPONSE FROM CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
			if (null != hotelSatModelList && !hotelSatModelList.isEmpty()) 
			{
				if(!isRuleTextUpdated)
				{
					startingTime = System.currentTimeMillis(); 
					if(null!=reqBean.getHotelName() && !reqBean.getHotelName().isEmpty())
					{
						List<HotelSatguruModel> selectedHotel = HotelUtil.getSelectHotelFiltered(reqBean.getHotelName(),hotelSatModelList);
						if(null!=selectedHotel && !selectedHotel.isEmpty())
						{
							filteredJsonArr = selectedHotel;
							hotelFound = true;
						}
						else
                            hotelFound = false;						
					}
					else
					{
						filteredJsonArr = hotelSatModelList;
						hotelFound = true;
					}
				if(hotelFound)
				 {
					for(int i=0;i<filteredJsonArr.size();i++)
					{
						Float grossPrice;
						Float totalAgencyMarkup = 0f;
						Float totalPrice;
						Float priceWithoutDiscPrice;
						HotelSatguruModel hotelObj = filteredJsonArr.get(i);
						HotelSearchRespDataBean hotelSearchResp = new HotelSearchRespDataBean();
						List<HotelsFareRuleRespBean> respList = new ArrayList<>();
						HotelsFareRuleRespBean fareRuleBean = new HotelsFareRuleRespBean();
						fareRuleBean.setFareFromTo(Double.parseDouble(hotelObj.getOrgFare()));
						fareRuleBean.setHotelName(hotelObj.getHotelName());
						Set<HotelSatMappingModel> satSuppSet = hotelObj.getHotelSatSuppModel();
						Iterator<HotelSatMappingModel> itr = satSuppSet.iterator();
						String suppHotelCode = null;
						String suppCode = null;
						while(itr.hasNext())
						{
							HotelSatMappingModel model = itr.next();
							suppHotelCode =  model.getSuppHotelCode();
							suppCode = model.getSuppCode();
						}
						if("DOT".equalsIgnoreCase(suppCode))
							suppHotelCode = suppHotelCode.split("\\|")[0];
						else if("GDI".equalsIgnoreCase(suppCode))
							suppHotelCode = suppHotelCode.split(";")[1];
						else if("HBD".equalsIgnoreCase(suppCode))
							suppHotelCode = suppHotelCode.split("-")[1];
						fareRuleBean.setHotelCode(suppHotelCode);
						fareRuleBean.setNoOfPax(totalPax);
						fareRuleBean.setNoOfRooms(totalRooms); 
						fareRuleBean.setDuration(noOfNights);
						fareRuleBean.setHotelGroup("");
						fareRuleBean.setStarRatingCode(hotelObj.getHotelStarRating());
						fareRuleBean.setDestinationCountry(destinationCountry);
						fareRuleBean.setDestinationCity(destinationCity);
						fareRuleBean.setSupplierCode(suppCode);
						fareRuleBean.setBlackoutStartDate("");
						fareRuleBean.setBlackoutEndDate("");
						fareRuleBean.setRoomType("");
						fareRuleBean.setRatePlanType("");
						if(null != hotelObj.getTaxsAmount() && !hotelObj.getTaxsAmount().isEmpty())
							fareRuleBean.setTaxsAmount(hotelObj.getTaxsAmount());
						else
							fareRuleBean.setTaxsAmount("0");
						respList.add(fareRuleBean);
						hotelSearchResp.setRespList(respList);
						hotelSearchRespBean = ruleSimulationService.applyRuleOnHotelResult(hotelSearchResp,KieSessionService.kieSessHotel,userSessionBean.getCountryId(),userSessionBean.getAgentId(),userSessionBean.getBranchId(),corpId,custNationality);
						HotelsFareRuleRespBean fareRuleResp = hotelSearchRespBean.getRespList().get(0);
						double orgFare = Double.parseDouble(hotelObj.getOrgFare());
						Float discPrice = Float.parseFloat(String.valueOf(fareRuleResp.getDiscountPrice()));
						
						totalPrice = Float.parseFloat(String.valueOf(orgFare)) + Float.parseFloat(String.valueOf(fareRuleResp.getT3Price())) + Float.parseFloat(String.valueOf(fareRuleResp.getServiceChargePrice()));
						
						
						if (resultsReqBean.getTotalAgencyMarkUp() > 0)
							totalAgencyMarkup = resultsReqBean.getTotalAgencyMarkUp();

						if (resultsReqBean.getMarkUpType() == 1)
						{
							totalAgencyMarkup = (totalPrice- discPrice) * (totalAgencyMarkup / 100);
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						
                        priceWithoutDiscPrice = Float.parseFloat(String.valueOf(orgFare)) + Float.parseFloat(String.valueOf(fareRuleResp.getT3Price())) + Float.parseFloat(String.valueOf(fareRuleResp.getServiceChargePrice())) + totalAgencyMarkup;
						
						double priceWithoutServiceCharge = priceWithoutDiscPrice - Float.parseFloat(String.valueOf(fareRuleResp.getServiceChargePrice())) - discPrice;
						
						Float amountToPay = totalPrice - discPrice;
						filteredJsonArr.get(i).setT3Markup(String.valueOf(fareRuleResp.getT3Price()));
						filteredJsonArr.get(i).setServiceChargePrice(String.valueOf(fareRuleResp.getServiceChargePrice()));
						filteredJsonArr.get(i).setDiscountPrice(String.valueOf(fareRuleResp.getDiscountPrice()));
						filteredJsonArr.get(i).setPriceWithoutServiceCharge(Float.parseFloat(String.valueOf(priceWithoutServiceCharge)));
						filteredJsonArr.get(i).setPriceWithoutDiscount(priceWithoutDiscPrice);
						filteredJsonArr.get(i).setTotalAmtToPay(amountToPay);
						filteredJsonArr.get(i).setGrossPrice(grossPrice);
						filteredJsonArr.get(i).setTotalAgencyMarkup(totalAgencyMarkup);
						filteredJsonArr.get(i).setGrossPriceWithoutMarkup(String.valueOf(grossPrice - fareRuleResp.getT3Price()));
						filteredJsonArr.get(i).setT3Price(String.valueOf(amountToPay));
						
						/*if(discPrice>=grossPrice || discPrice>=priceWithoutServiceCharge)
							filteredJsonArr.remove(i);*/
					}
					filteredJsonArr = (List<HotelSatguruModel>)filteredJsonArr.stream().filter(obj-> obj.getPriceWithoutServiceCharge()>0.0 && obj.getGrossPrice()>0).collect(Collectors.toList());
					RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, filteredJsonArr,redisService);
					endingTime = System.currentTimeMillis();
					totalTime = (endingTime-startTime)/1000;
					totalTimeInMillis = endingTime-startingTime;
					CallLog.info(103, "TOTAL TIME TO APPLY RULE ON CACHE RESPONSE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
				}
				else
					filteredJsonArr = new ArrayList<>();
				}
				else
				{
					if(null!=reqBean.getHotelName() && !reqBean.getHotelName().isEmpty())
					{
						List<HotelSatguruModel> selectedHotel = HotelUtil.getSelectHotelFiltered(reqBean.getHotelName(),hotelSatModelList);
						if(null!=selectedHotel && !selectedHotel.isEmpty())
						{
							filteredJsonArr = selectedHotel;
							hotelFound = true;
						}
						else
							hotelFound = false;
					}
					else
					{
						filteredJsonArr = hotelSatModelList;
				        hotelFound = true;
					}
				if(hotelFound)
				{
					for(int i=0;i<filteredJsonArr.size();i++)
					{
						Float grossPrice;
						Float totalPrice;
						Float totalAgencyMarkup = 0f;
						if (resultsReqBean.getTotalAgencyMarkUp() > 0)
						    totalAgencyMarkup = resultsReqBean.getTotalAgencyMarkUp();
						
						HotelSatguruModel hotelJsonObj = filteredJsonArr.get(i);
						
						double orgFare = Double.parseDouble(hotelJsonObj.getOrgFare());
						Float discPrice = Float.parseFloat(String.valueOf(hotelJsonObj.getDiscountPrice()));
						
						totalPrice = Float.parseFloat(String.valueOf(orgFare)) + Float.parseFloat(String.valueOf(hotelJsonObj.getT3Markup())) + Float.parseFloat(String.valueOf(hotelJsonObj.getServiceChargePrice()));
						
						
						if (resultsReqBean.getMarkUpType() == 1)
						{
							grossPrice = totalPrice + ((totalPrice- discPrice) * (totalAgencyMarkup / 100)) - discPrice;
							totalAgencyMarkup = (totalPrice- discPrice) * (totalAgencyMarkup / 100);
						}
						else
							grossPrice = totalPrice + totalAgencyMarkup - discPrice;
						
						Float amountToPay = totalPrice - discPrice;
						
						filteredJsonArr.get(i).setGrossPrice(grossPrice);
						filteredJsonArr.get(i).setTotalAgencyMarkup(totalAgencyMarkup);
						filteredJsonArr.get(i).setGrossPriceWithoutMarkup(String.valueOf(grossPrice - Float.parseFloat(String.valueOf(hotelJsonObj.getT3Markup()))));
						filteredJsonArr.get(i).setT3Price(String.valueOf(amountToPay));
					}
				}
				else
					return null;
				}
			}
			else
				return null;
		}
		else
			return null;

		return filteredJsonArr;
	}

	///////////////////Get Filtered Result Ends//////////////////////////////////////////	

	
	
///////////////////Set Filtered Result Starts//////////////////////////////////////////

	public String setTenFilteredResult(HotelSearchRequestBean reqBean,String countryId,String agencyId,String branchId,RuleSimulationService ruleSimulationService,List<HotelSatguruModel> hotelRecommList) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;
		serviceRequestBean = new ServiceRequestBean();
		try 
		{
			if(null!=agencyId && !agencyId.isEmpty())
			reqBean = HotelUtil.keyForTenFilteredResults(reqBean, agencyId);
			else
			reqBean = HotelUtil.keyForTenFilteredResults(reqBean, branchId);	
			String hotelSearchKey = reqBean.getHotelSearchKey();
			CallLog.info(1, "hotelSearchKey for setFilteredResult Call === " + hotelSearchKey);
			Long startTime = System.currentTimeMillis();
			RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, hotelRecommList, redisService);
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime - startTime) / 1000;
			totalTimeInMillis = endingTime - startingTime;
			CallLog.info(103, "TOTAL TIME TO SET FILTERED HOTEL RESULT RESPONSE IN CACHE ::: " + totalTime
					+ " SECONDS || TIME(millisecs)::" + totalTimeInMillis + " milliseconds");
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(103,"TOTAL RESPONSE TIME(SECS)----->>>" + timeTaken / 1000 + " || TIME(MILLISECS)::" + timeTaken);
			jsonString = "success";
		} 
		catch (Exception e) {
			jsonString = "failure";
			CallLog.printStackTrace(1, e);
		}

		return jsonString;
	}

///////////////////Set Filtered Result Ends//////////////////////////////////////////	
	
	
	
///////////////////Set Filtered Result Starts//////////////////////////////////////////

	public String setFilteredResult(HotelSearchRequestBean reqBean,String countryId,String agencyId,String branchId,RuleSimulationService ruleSimulationService,List<HotelSatguruModel> hotelRecommList) 
	{
		String jsonString;
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;	
		serviceRequestBean = new ServiceRequestBean();
		try 
		{
			if(null!=agencyId && !agencyId.isEmpty())
				reqBean = HotelUtil.keyForFilteredResults(reqBean,agencyId);
			else
				reqBean = HotelUtil.keyForFilteredResults(reqBean,branchId);	
			String hotelSearchKey = reqBean.getHotelSearchKey();
			CallLog.info(1, "hotelSearchKey for setFilteredResult Call === " + hotelSearchKey);
			Long startTime = System.currentTimeMillis();
			RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, hotelRecommList,redisService);
			endingTime = System.currentTimeMillis();
			totalTime = (endingTime-startTime)/1000;
			totalTimeInMillis = endingTime-startingTime;
			CallLog.info(103, "TOTAL TIME TO SET FILTERED HOTEL RESULT RESPONSE IN CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
			Long endTime = System.currentTimeMillis();
			Long timeTaken = endTime - startTime;
			CallLog.info(103, "TOTAL RESPONSE TIME(SECS)----->>>" + timeTaken/1000+" || TIME(MILLISECS)::"+timeTaken);
			jsonString = "success";
		} 
		catch (Exception e) {
			 jsonString = "failure";
			 CallLog.printStackTrace(1, e);
		}
		
		return jsonString;
	}

///////////////////Set Filtered Result Ends//////////////////////////////////////////	


	@SuppressWarnings("unchecked")
	public HotelSearchRespDataBean searchHotel(HotelSearchRequestBean reqBean,RuleSimulationService ruleSimulationService,UserSessionBean userSessionBean,HttpServletRequest request) 
	{
		StringBuilder timeString = new StringBuilder();
		long totalTimeInMillis = 0;	
		serviceRequestBean = new ServiceRequestBean();
		Long searchKeyStartTime = System.currentTimeMillis();
		if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
			reqBean = HotelUtil.prepareSearchKeyForPrice(reqBean,userSessionBean.getAgentId());
		else
			reqBean = HotelUtil.prepareSearchKeyForPrice(reqBean,userSessionBean.getBranchId());
		Long searchKeyEndTime = System.currentTimeMillis();
		timeString.append("[PrepareSearchKey : ").append(String.valueOf(searchKeyEndTime-searchKeyStartTime)).append("]");
		String hotelSearchKey = reqBean.getHotelSearchKey();
		CallLog.info(1, "hotelSearchKey for Price Call === " + hotelSearchKey);
		Boolean isSearchKeyFound = false;
		long checkSearchKeyStart = System.currentTimeMillis();
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey,redisService);
		if (resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		    if(!reqBean.isCacheAllSupp())
		    	isSearchKeyFound = false;
		}
		/*Start Code to apply rule engine*/
		long checkSearchKeyEnd = System.currentTimeMillis();
		timeString.append("[FoundSearchKey : ").append(String.valueOf(checkSearchKeyEnd-checkSearchKeyStart)).append("]");
		Long checkRuleUpdationStart = System.currentTimeMillis();
		String corpId = reqBean.getCorporateId(); 
		String custNationality = reqBean.getCustNationality(); 
		boolean isRuleTextUpdated = false;
		ProductModel productModel = new ProductModel();
		productModel.setStatus(1);
		productModel.setProductCode(QueryConstant.RULE_GROUP_TWO);
		ResultBean resultBeanProduct = productService.getProducts(productModel);
		HotelSearchRespDataBean ruleAppliedRespBean = null;
		if (resultBeanProduct != null && !resultBeanProduct.isError()) {
			List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
			if (productList != null && !productList.isEmpty()) {
				ProductModel productModal = productList.get(0);
				if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {

					Date ruleLastUpdated = productModal.getRuleLastUpdated();
					Date kieSessionLastUpdated = KieSessionService.kieHotelSessionLastUpdated;
					if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime())
					{
						CallLog.info(105,"Rules for Hotel Not changed.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+kieSessionLastUpdated+"(Time : "+kieSessionLastUpdated.getTime()+").");
						isRuleTextUpdated = true;
					}
					if (KieSessionService.kieSessHotel != null && isRuleTextUpdated) {
						KieSessionService.getKieSessionHotel();
						
					} else {
						boolean flagTextRule = KieSessionService.writeRuleTextHotel(productModal.getRuleText());
						if (flagTextRule) {
							KieSessionService.kieSessHotel = null;
							KieSessionService.getKieSessionHotel();
							KieSessionService.kieHotelSessionLastUpdated = new Date();
						}
						CallLog.info(105,"Rules for Hotel Modified.\nRule Last Updated Time :: "+ruleLastUpdated+"(Time : "+ruleLastUpdated.getTime()+" ). KissSession Last Updated Time :: "+KieSessionService.kieHotelSessionLastUpdated+"(Time : "+KieSessionService.kieHotelSessionLastUpdated.getTime()+").");
					}
					
				}
				else {
					KieSessionService.kieSessHotel = null;
					CallLog.info(105, "No approved hotel rules found.");
				}
			}
		}
		long checkRuleUpdationEnd = System.currentTimeMillis();
		timeString.append("[CheckRuleUpdation : ").append(String.valueOf(checkRuleUpdationEnd-checkRuleUpdationStart)).append("]");
		HotelSearchRespDataBean hotelSearchRespBean = null;
		if(isSearchKeyFound)
		{
			Long cacheRespStartTime = System.currentTimeMillis(); 
			resultBean = RedisCacheUtil.getResponseCacheHotelObject(hotelSearchKey,redisService);
			Long cacheRespEndTime = System.currentTimeMillis();
			timeString.append("[RespFromCache : ").append(String.valueOf(cacheRespEndTime-cacheRespStartTime)).append("]");
			hotelSearchRespBean = (HotelSearchRespDataBean)resultBean.getResultObject();
			totalTimeInMillis = cacheRespEndTime-cacheRespStartTime;
			CallLog.info(103, "TOTAL TIME TO FECTH PRICE RESPONSE FROM CACHE ::: "+totalTimeInMillis/1000 +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
		    if (null != hotelSearchRespBean.getRespList() && !hotelSearchRespBean.getRespList().isEmpty()) 
		    {
				if (!isRuleTextUpdated) {
					Long applyRuleStartTime = System.currentTimeMillis(); 
					ruleAppliedRespBean = ruleSimulationService.applyRuleOnHotelResult(hotelSearchRespBean,KieSessionService.kieSessHotel,userSessionBean.getCountryId(),userSessionBean.getAgentId(),userSessionBean.getBranchId(),corpId,custNationality);
					Long applyRuleEndTime = System.currentTimeMillis();
					timeString.append("[ApplyRuleTime : ").append(String.valueOf(applyRuleEndTime-applyRuleStartTime)).append("]");
					totalTimeInMillis = applyRuleEndTime-applyRuleStartTime;
					CallLog.info(103, "TOTAL TIME TO APPLY RULE ON RESPONSE ::: "+totalTimeInMillis/1000 +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
					if(!resultBean.isError() && null != ruleAppliedRespBean && !ruleAppliedRespBean.getRespList().isEmpty() && reqBean.isCacheAllSupp()) {
						SavePriceResultsThread savePriceRes = new SavePriceResultsThread(hotelSearchKey, ruleAppliedRespBean, redisService);
						Thread priceThread = new Thread(savePriceRes);
						priceThread.start();
						//RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, ruleAppliedRespBean,redisService);
				    }
				}
				else
				{
					ruleAppliedRespBean = hotelSearchRespBean;
				}
		    } else {
		    	Long priceRespStartTime = System.currentTimeMillis();
		    	hotelSearchRespBean = HotelUtil.getHotelSearchResponse(serviceRequestBean, reqBean,hotelManager,hotelManagerPortal,hotelBookService,userSessionBean,request);
		    	if(null != hotelSearchRespBean && null!= hotelSearchRespBean.getRespList() && !hotelSearchRespBean.getRespList().isEmpty())
		    	{
		    		List<HotelsFareRuleRespBean> respList = hotelSearchRespBean.getRespList();
					respList = respList.stream().map(obj->{
						if(obj.getSupplierCode().equalsIgnoreCase("TVC") || obj.getSupplierCode().equalsIgnoreCase("HBD"))
						{
							if(!obj.getAgencyCurrency().equalsIgnoreCase(obj.getSupplierCurrency()))
							{

								if(obj.getSupplierCode().equalsIgnoreCase("TVC"))
								{
									Double currRate;
									try {
										currRate = hotelHelperUtil.getCurrencyRate(obj.getAgencyCurrency(),obj.getSupplierCurrency());
									
									if(Double.compare(currRate, 0)==0)
									{
										obj.setCurrConvExist(false);
										if(tvcFlag)
										  hotelBookService.sendCurrencyNotification(obj.getSupplierCode() , obj.getSupplierCurrency(), obj.getAgencyCurrency(), request,userSessionBean);
									}
									else
									{
										obj.setCurrConvertRate(currRate);
										Double fareFromTo = obj.getFareFromTo();
										obj.setFareFromTo(fareFromTo*currRate);
									}
									} catch (ParseException e) {
										CallLog.printStackTrace(1, e);
									}
									tvcFlag = false;
								}
								else if(obj.getSupplierCode().equalsIgnoreCase("HBD"))
								{
									Double currRate;
									try {
										currRate = hotelHelperUtil.getCurrencyRate(obj.getAgencyCurrency(),obj.getSupplierCurrency());
									
									if(Double.compare(currRate, 0)==0)
									{
										obj.setCurrConvExist(false);
										if(hbdFlag)
										  hotelBookService.sendCurrencyNotification(obj.getSupplierCode() , obj.getSupplierCurrency(), obj.getAgencyCurrency(), request,userSessionBean);
									}
									else
									{
										obj.setCurrConvertRate(currRate);
										Double fareFromTo = obj.getFareFromTo();
										obj.setFareFromTo(fareFromTo*currRate);
									}
									} catch (ParseException e) {
										CallLog.printStackTrace(1, e);
									}
									hbdFlag = false;
								}
							}
							else
							{
								obj.setCurrConvExist(true);
								obj.setCurrConvertRate(1.0);
							}
						}
						return obj;
					}).collect(Collectors.toList());
					respList = respList.stream().filter(obj->obj.isCurrConvExist()).collect(Collectors.toList());
					hotelSearchRespBean.setRespList(respList);
		    	}
		    	
		    	Long priceRespEndTime = System.currentTimeMillis();
		    	timeString.append("[SuppRespTime : ").append(String.valueOf(priceRespEndTime-priceRespStartTime)).append("]");
				totalTimeInMillis = priceRespEndTime-priceRespStartTime;
				CallLog.info(103, "ALL SUPPLIERS TOTAL RESPONSE TIME[SECS] ::: "+totalTimeInMillis/1000 +" SECONDS || TIME[MILLISECONDS]::" +totalTimeInMillis+" MILLISECONDS");
				if (null != hotelSearchRespBean.getRespList() && !hotelSearchRespBean.getRespList().isEmpty()) 
				{
					Long applyRuleStartTime = System.currentTimeMillis();
					ruleAppliedRespBean = ruleSimulationService.applyRuleOnHotelResult(hotelSearchRespBean,KieSessionService.kieSessHotel,userSessionBean.getCountryId(),userSessionBean.getAgentId(),userSessionBean.getBranchId(),corpId,custNationality);
					Long applyRuleEndTime = System.currentTimeMillis();
					timeString.append("[ApplyRuleTime : ").append(String.valueOf(applyRuleEndTime-applyRuleStartTime)).append("]");
					totalTimeInMillis = applyRuleEndTime-applyRuleStartTime;
					CallLog.info(103, "TOTAL TIME TO APPLY RULE ON RESPONSE ::: "+totalTimeInMillis/1000 +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
					ruleAppliedRespBean = HotelUtil.getMinPriceResp(ruleAppliedRespBean, this);
					if (null != ruleAppliedRespBean && !ruleAppliedRespBean.getRespList().isEmpty() && reqBean.isCacheAllSupp())
				    {
				    	SavePriceResultsThread savePriceRes = new SavePriceResultsThread(hotelSearchKey, ruleAppliedRespBean, redisService);
						Thread priceThread = new Thread(savePriceRes);
						priceThread.start();
				    }
				    
				}
		    }
		}
		else 
		{
			Long priceRespStartTime = System.currentTimeMillis(); 
			hotelSearchRespBean = HotelUtil.getHotelSearchResponse(serviceRequestBean, reqBean,hotelManager,hotelManagerPortal,hotelBookService,userSessionBean,request);
			if(null != hotelSearchRespBean && null!= hotelSearchRespBean.getRespList() && !hotelSearchRespBean.getRespList().isEmpty())
	    	{
				List<HotelsFareRuleRespBean> respList = hotelSearchRespBean.getRespList();
				respList = respList.stream().map(obj->{
					if(obj.getSupplierCode().equalsIgnoreCase("TVC") || obj.getSupplierCode().equalsIgnoreCase("HBD"))
					{
						if(!obj.getAgencyCurrency().equalsIgnoreCase(obj.getSupplierCurrency()))
						{
							if(obj.getSupplierCode().equalsIgnoreCase("TVC"))
							{
								Double currRate;
								try {
									currRate = hotelHelperUtil.getCurrencyRate(obj.getAgencyCurrency(),obj.getSupplierCurrency());
								
								if(Double.compare(currRate, 0)==0)
								{
									obj.setCurrConvExist(false);
									if(tvcFlag)
									   hotelBookService.sendCurrencyNotification(obj.getSupplierCode() , obj.getSupplierCurrency(), obj.getAgencyCurrency(), request,userSessionBean);
								}
								else
								{
									obj.setCurrConvertRate(currRate);
									Double fareFromTo = obj.getFareFromTo();
									obj.setFareFromTo(fareFromTo*currRate);
								}
								} catch (ParseException e) {
									CallLog.printStackTrace(1, e);
								}
								tvcFlag = false;
							}
							else if(obj.getSupplierCode().equalsIgnoreCase("HBD"))
							{
								Double currRate;
								try {
									currRate = hotelHelperUtil.getCurrencyRate(obj.getAgencyCurrency(),obj.getSupplierCurrency());
								
								if(Double.compare(currRate, 0)==0)
								{
									obj.setCurrConvExist(false);
									if(hbdFlag)
									  hotelBookService.sendCurrencyNotification(obj.getSupplierCode() , obj.getSupplierCurrency(), obj.getAgencyCurrency(), request,userSessionBean);
								}
								else
								{
									obj.setCurrConvertRate(currRate);
									Double fareFromTo = obj.getFareFromTo();
									obj.setFareFromTo(fareFromTo*currRate);
								}
								} catch (ParseException e) {
									CallLog.printStackTrace(1, e);
								}
								hbdFlag = false;
							}
						}
						else
						{
							obj.setCurrConvExist(true);
							obj.setCurrConvertRate(1.0);
						}
					}
					return obj;
				}).collect(Collectors.toList());
				respList = respList.stream().filter(obj->obj.isCurrConvExist()).collect(Collectors.toList());
				hotelSearchRespBean.setRespList(respList);
	    	}
			
			Long priceRespEndTime = System.currentTimeMillis();
	    	timeString.append("[SuppRespTime : ").append(String.valueOf(priceRespEndTime-priceRespStartTime)).append("]");
			totalTimeInMillis = priceRespEndTime-priceRespStartTime;
			CallLog.info(103, "ALL SUPPLIERS TOTAL RESPONSE TIME[SECS] ::: "+totalTimeInMillis/1000 +" SECONDS || TIME[MILLISECONDS]::" +totalTimeInMillis+" MILLISECONDS");
			if (null != hotelSearchRespBean.getRespList() && !hotelSearchRespBean.getRespList().isEmpty()) 
			{
				Long applyRuleStartTime = System.currentTimeMillis();  
				ruleAppliedRespBean = ruleSimulationService.applyRuleOnHotelResult(hotelSearchRespBean,KieSessionService.kieSessHotel,userSessionBean.getCountryId(),userSessionBean.getAgentId(),userSessionBean.getBranchId(),corpId,custNationality);
				Long applyRuleEndTime = System.currentTimeMillis();
				timeString.append("[ApplyRuleTime : ").append(String.valueOf(applyRuleEndTime-applyRuleStartTime)).append("]");
				totalTimeInMillis = applyRuleEndTime-applyRuleStartTime;
				CallLog.info(103, "TOTAL TIME TO APPLY RULE ON RESPONSE ::: "+totalTimeInMillis/1000 +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
				ruleAppliedRespBean = HotelUtil.getMinPriceResp(ruleAppliedRespBean, this);
				if (null != ruleAppliedRespBean && !ruleAppliedRespBean.getRespList().isEmpty() && reqBean.isCacheAllSupp())
			    {
			    	SavePriceResultsThread savePriceRes = new SavePriceResultsThread(hotelSearchKey, ruleAppliedRespBean, redisService);
					Thread priceThread = new Thread(savePriceRes);
					priceThread.start();
			    }
			}
			else
			{
				ruleAppliedRespBean = new HotelSearchRespDataBean(); 
				ruleAppliedRespBean.setSuppCurrencyFail(hotelSearchRespBean.getSuppCurrencyFail());
			}
	    }
		Long endTime = System.currentTimeMillis();
		Long timeTaken = endTime - searchKeyStartTime;
		CallLog.info(103, "TOTAL RESPONSE TIME(SECS)----->>>" + timeTaken/1000+" || TIME(MILLISECS)::"+timeTaken);
		reqBean.setTimeString(timeString);
		return ruleAppliedRespBean;
		/*End*/
	}
	 
	///////////////////Limit Result Starts//////////////////////////////////////////
	
	
    /////////////////Search Raw Results Ends//////////////////////////////////////////
	
	public List<HotelSatguruModel> searchLimitResult(@RequestBody HotelSearchReqDataBean hotelSearchReqDataBean) 
	{
		logStr = logStr + "inside searchHotelStaticInfo() ";
	    int pageNo = hotelSearchReqDataBean.getPageNumber();
	    String cityName = hotelSearchReqDataBean.getCityName();
	    String countryName = hotelSearchReqDataBean.getCountryName();
	    String hotelName = hotelSearchReqDataBean.getHotelName();
	    String hotelCheckIn = hotelSearchReqDataBean.getHotelCheckIn();
	    String hotelCheckOut = hotelSearchReqDataBean.getHotelCheckOut();
	    Double lat=hotelSearchReqDataBean.getLatitude()!=null?Double.parseDouble(hotelSearchReqDataBean.getLatitude()):null;
	    Double lon=hotelSearchReqDataBean.getLongitude()!=null?Double.parseDouble(hotelSearchReqDataBean.getLongitude()):null;
	    Double dis=hotelSearchReqDataBean.getGeoDistance()!=null?Double.parseDouble(hotelSearchReqDataBean.getGeoDistance()):null;
	    long startTime = 0;
		long endTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;
		String limit= "limit";
		List<HotelSatguruModel> resultList = null;
		HotelSearchRespDataBean searchRespData = null;
		try {
		    /////////////////////////////cache implementation////////////////////////////////////
			long startTime1 = System.currentTimeMillis();
		    hotelSearchReqDataBean = HotelUtil.prepareKeyLimitData(hotelSearchReqDataBean);
			String hotelSearchKey = hotelSearchReqDataBean.getHotelSearchKey();
			CallLog.info(1,logStr + hotelSearchKey);
			Boolean isSearchKeyFound = false;
			ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey,redisService);
			if (resultBean != null && !resultBean.isError())
			{
			    isSearchKeyFound = resultBean.getResultBoolean();
			}
			if (isSearchKeyFound)
	        {
				startTime = System.currentTimeMillis(); 
				resultBean = RedisCacheUtil.getResponseCacheHotelObject(hotelSearchKey, redisService);
				searchRespData = (HotelSearchRespDataBean)resultBean.getResultObject();
			    endTime = System.currentTimeMillis();
				totalTime = (endTime-startTime)/1000;
				totalTimeInMillis = endTime-startTime;
				CallLog.info(103, "TOTAL TIME TO FETCH HOTEL LIMIT DATA RESPONSE FROM CACHE ::: "+totalTime +" seconds || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
			    if (null != searchRespData) 
			    	resultList = searchRespData.getHotelModel();
			    else 
				{
				   if ((null != cityName && !"".equalsIgnoreCase(cityName)) && pageNo > 0)
			       {
			    	   if (null != countryName && !"".equalsIgnoreCase(countryName))
			    	   {
			    		 resultList = getHotelStaticInfo(cityName,countryName,hotelName,lat,lon,dis,limit,hotelCheckIn,hotelCheckOut);
	                      if (null != resultList && !resultList.isEmpty())
                          {
	                    	  	HotelSearchRespDataBean respBeanData = new HotelSearchRespDataBean();
	                    	  	respBeanData.setHotelModel(resultList);
	                        	startTime = System.currentTimeMillis(); 
	                        	RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, respBeanData,redisService);
	                        	endTime = System.currentTimeMillis();
	         					totalTime = (endTime-startTime)/1000;
	         					totalTimeInMillis = endTime-startTime;
	         					CallLog.info(103, "TOTAL TIME TO SET HOTEL STATIC DATA RESPONSE INTO CACHE ::: "+totalTime +" seconds || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
                          }
			    	   }
			       }
				} 	
			}
			else 
			{
				resultList = getHotelStaticInfo(cityName,countryName,hotelName,lat,lon,dis,limit,hotelCheckIn,hotelCheckOut);
				if (null != resultList && !resultList.isEmpty())
				{
					startTime = System.currentTimeMillis(); 
					HotelSearchRespDataBean respBeanData = new HotelSearchRespDataBean();
            	  	respBeanData.setHotelModel(resultList);
				    RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, respBeanData,redisService);
				    endTime = System.currentTimeMillis();
 					totalTime = (endTime-startTime)/1000;
 					totalTimeInMillis = endTime-startTime;
 					CallLog.info(103, "TOTAL TIME TO SET HOTEL STATIC DATA RESPONSE INTO CACHE ::: "+totalTime +" seconds || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
			    }
			}
		    ////////////////////////////cache implementation ends///////////////////////////////
			long endTime1 = System.currentTimeMillis();
			long totalTime1 = (endTime1-startTime1);
			CallLog.info(103, "TOTAL TIME TO PROCE HOTEL STATIC DATA RESPONSE ::: "+totalTime1/1000 +" seconds || TIME(millisecs)::" +totalTime1+" milliseconds");
			
		} catch (Exception e) {
		    CallLog.printStackTrace(1, e);
		}
		return resultList;
	}
	
///////////////////Limit Result Ends//////////////////////////////////////////
	
	
	
	 public HotelSearchRespDataBean searchHotelStaticInfo(@RequestBody HotelSearchReqDataBean hotelSearchReqDataBean) {
		 	Long startTime2 = System.currentTimeMillis();
		 	StringBuilder timeString = new StringBuilder();
			logStr = logStr + "inside searchHotelStaticInfo() ";
		    int pageNo = hotelSearchReqDataBean.getPageNumber();
		    String cityName = hotelSearchReqDataBean.getCityName();
		    String countryName = hotelSearchReqDataBean.getCountryName();
		    String hotelName = hotelSearchReqDataBean.getHotelName();
		    String hotelCheckIn = hotelSearchReqDataBean.getHotelCheckIn();
		    String hotelCheckOut = hotelSearchReqDataBean.getHotelCheckOut();
		    HotelSearchRespDataBean searchDataBean = new HotelSearchRespDataBean();
		    Double lat=hotelSearchReqDataBean.getLatitude()!=null?Double.parseDouble(hotelSearchReqDataBean.getLatitude()):null;
		    Double lon=hotelSearchReqDataBean.getLongitude()!=null?Double.parseDouble(hotelSearchReqDataBean.getLongitude()):null;
		    Double dis=hotelSearchReqDataBean.getGeoDistance()!=null?Double.parseDouble(hotelSearchReqDataBean.getGeoDistance()):null;
		    long startTime = 0;
			long endTime = 0;
			long totalTime = 0;
			long totalTimeInMillis = 0;
			String limit = "full";
			List<HotelSatguruModel> resultList = null;
			try 
			{
			    /////////////////////////////cache implementation////////////////////////////////////
				long startTime1 = System.currentTimeMillis();
			    hotelSearchReqDataBean = HotelUtil.prepareKeyStaticData(hotelSearchReqDataBean);
				String hotelSearchKey = hotelSearchReqDataBean.getHotelSearchKey();
				CallLog.info(1,logStr + hotelSearchKey);
				Boolean isSearchKeyFound = false;
				ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheHotel(hotelSearchKey,redisService);
				if (resultBean != null && !resultBean.isError())
				{
				    isSearchKeyFound = resultBean.getResultBoolean();
				}
				if (isSearchKeyFound)
		        {
					startTime = System.currentTimeMillis(); 
					resultBean = RedisCacheUtil.getResponseCacheHotelObject(hotelSearchKey, redisService);
					searchDataBean = (HotelSearchRespDataBean)resultBean.getResultObject();
				    endTime = System.currentTimeMillis();
					totalTime = (endTime-startTime)/1000;
					totalTimeInMillis = endTime-startTime;
					CallLog.info(103, "TOTAL TIME TO FETCH HOTEL STATIC DATA RESPONSE FROM CACHE ::: "+totalTime +" seconds || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
				    if (null != searchDataBean && !searchDataBean.getHotelModel().isEmpty()){
				    	Long endTime2 = System.currentTimeMillis();
						timeString.append("[HotelStaticInfo : ").append(String.valueOf(endTime2-startTime2)).append("]");
						searchDataBean.setTimeString(timeString);
				    	return searchDataBean;
				    }
				    else 
					{
					    if ((null != cityName && !"".equalsIgnoreCase(cityName)) && pageNo > 0)
				         {
				    	   if (null != countryName && !"".equalsIgnoreCase(countryName))
				    	   {
				    		 resultList = getHotelStaticInfo(cityName,countryName,hotelName,lat,lon,dis,limit,hotelCheckIn,hotelCheckOut);
				    		 if(resultList!=null && !resultList.isEmpty())
				    		 {
				    			 searchDataBean = new HotelSearchRespDataBean();
					    		 searchDataBean.setHotelModel(resultList);
					    		 SaveStaticResultsThread saveStaticRes = new SaveStaticResultsThread(hotelSearchKey, searchDataBean, redisService);
					    		 Thread staticThread = new Thread(saveStaticRes);
					    		 staticThread.start();
								//RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, searchDataBean,redisService);
				    		 }
				    	   }
				         }
					   } 	
				    }
				else 
				{
					 resultList = getHotelStaticInfo(cityName,countryName,hotelName,lat,lon,dis,limit,hotelCheckIn,hotelCheckOut);
					 if(resultList!=null && !resultList.isEmpty())
		    		 {
		    			 searchDataBean = new HotelSearchRespDataBean();
			    		 searchDataBean.setHotelModel(resultList);
			    		 SaveStaticResultsThread saveStaticRes = new SaveStaticResultsThread(hotelSearchKey, searchDataBean, redisService);
			    		 Thread staticThread = new Thread(saveStaticRes);
			    		 staticThread.start();
						//RedisCacheUtil.setResponseInCacheHotel(hotelSearchKey, searchDataBean,redisService);
		    		 }
				}
			    ////////////////////////////cache implementation ends///////////////////////////////
				long endTime1 = System.currentTimeMillis();
				long totalTime1 = (endTime1-startTime1);
				CallLog.info(103, "TOTAL TIME TO PROCE HOTEL STATIC DATA RESPONSE ::: "+totalTime1/1000 +" seconds || TIME(millisecs)::" +totalTime1+" milliseconds");
			} catch (Exception e) {
			    CallLog.printStackTrace(1, e);
			}
			Long endTime2 = System.currentTimeMillis();
			timeString.append("[HotelStaticInfo : ").append(String.valueOf(endTime2-startTime2)).append("]");
			searchDataBean.setTimeString(timeString);
			return searchDataBean;
		    }

	 
	 /**
	 * @param hotelResultBean
	 * @param agentServices
	 * @param userSessionBean
	 * @return
	 */
	public static HotelResultsRequestBean fetchAgencyMarkup(HotelResultsRequestBean hotelResultBean, AgentService agentServices,UserSessionBean userSessionBean)
	 {
			AgencyMarkup agencyMarkup = hotelResultBean.getAgencyMarkup();
			AgencyMarkupModel agencyMarkupModel = new AgencyMarkupModel();
			agencyMarkupModel.setStatus(1);
			agencyMarkupModel.setProductRefId(1);
			int corporateId = agencyMarkup.getCorporateId();
			String agencyType = agencyMarkup.getPassengerType();
			if (agencyType!="" && agencyType!=null && ("c-passenger").equalsIgnoreCase(agencyType))
			{
				List<String> passengerIdList = agencyMarkup.getPassengerIdList();
				if (passengerIdList != null && !passengerIdList.isEmpty())
				{
					boolean condition = passengerIdList.get(0).split("-")[1] != null && !("").equalsIgnoreCase(passengerIdList.get(0).split("-")[1]);
					corporateId = condition ? Integer.parseInt(passengerIdList.get(0).split("-")[1]) : 0;
				}
			}
		
			else if (agencyType!=null && ("corporate").equalsIgnoreCase(agencyType))
			{
				List<String> corporateList = agencyMarkup.getCorporateIdList();
				corporateId = corporateList != null && corporateList.get(0) != null ? Integer.parseInt(corporateList.get(0)) : 0;
			}
			if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
				agencyMarkup.setAgencyId(Integer.parseInt(userSessionBean.getAgentId()));
			agencyMarkupModel.setCorporateId(corporateId);
			agencyMarkupModel.setDomOrInternational(Integer.valueOf(agencyMarkup.getDomInt()));
			agencyMarkupModel.setStatus(agencyMarkup.getStatus());
			
			agencyMarkupModel.setProductRefId(agencyMarkup.getProductRefId());
			if(null != userSessionBean.getAgentId() && !userSessionBean.getAgentId().isEmpty())
				agencyMarkupModel.setAgencyId(Integer.parseInt(userSessionBean.getAgentId()));
			com.tt.ts.common.modal.ResultBean resultBean = agentServices.fetchAgencyMarkup(agencyMarkupModel);
			if(null!=resultBean.getResultList() && !resultBean.getResultList().isEmpty())
			{
			  Object[] markUpArr =  (Object [])resultBean.getResultList().get(0);
			  hotelResultBean.setMarkUpType(Integer.parseInt(String.valueOf(markUpArr[1])));
			  hotelResultBean.setTotalAgencyMarkUp(Integer.parseInt(String.valueOf(markUpArr[2])));
			}
			return hotelResultBean;
	}
}
