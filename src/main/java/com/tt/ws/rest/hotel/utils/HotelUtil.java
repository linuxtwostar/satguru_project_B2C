package com.tt.ws.rest.hotel.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tt.satguruportal.hotel.service.HotelBookService;
import com.tt.satguruportal.hotel.util.HotelHelperUtil;
import com.tt.satguruportal.hotel.util.HotelRestUtil;
import com.tt.satguruportal.login.bean.UserSessionBean;
import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ws.rest.hotel.bean.HotelSearchReqDataBean;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.tt.ws.rest.hotel.bean.HotelsFareRuleRespBean;
import com.tt.ws.rest.hotel.manager.HotelManager;
import com.tt.ws.rest.hotel.model.HotelSatMappingModel;
import com.tt.ws.rest.hotel.model.HotelSatguruModel;
import com.tt.ws.rest.hotel.services.HotelServices;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.flight.config.CredentialConfigModel;
import com.ws.services.flight.config.ServiceConfigModel;
import com.ws.services.flight.config.SupplierConfigModal;
import com.ws.services.hotel.bean.ChargeableRateInfoBean;
import com.ws.services.hotel.bean.Hotel;
import com.ws.services.hotel.bean.HotelCommonSearchRespBean;
import com.ws.services.hotel.bean.HotelMealBean;
import com.ws.services.hotel.bean.HotelOfferBean;
import com.ws.services.hotel.bean.HotelRoomCategoryBean;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.HotelSearchResponseBean;
import com.ws.services.hotel.bean.HotelWidgetElement;
import com.ws.services.hotel.bean.NightlyRateBean;
import com.ws.services.hotel.bean.RateInfoBean;
import com.ws.services.hotel.bean.RoomBean;
import com.ws.services.hotel.bean.RoomRateInfoBean;
import com.ws.services.hotel.bean.RoomRequestBean;
import com.ws.services.hotel.bean.SubHotel;
import com.ws.services.hotel.bean.ValueAddsBean;
import com.ws.services.hotel.bean.expedia.hotelSearch.RoomRateDetails;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;
import com.ws.services.util.ProductServiceEnum;

public class HotelUtil 
{
	static final String HOTEL_CODE = "hotelCode";
	static final String ROOM_NAME = "roomName";
	static final String ROOM_CODE = "roomCode";
	static final String HOTEL_ROOMS = "hotelRooms";
	static final String PRICE = "price";
	static final String CITY_NAME = "cityName";
	static final String CITY_CODE = "cityCode";
	static final String COUNTRY_NAME = "countryName";
	static final String HTL_NAME = "hotelName";
	static final String STAR_RATING_NAME = "starRatingName";
	static final String STAR_RATING_CODE = "starRatingCode";
	RoomRequestBean roomRequestBean = null;
	ServiceRequestBean serviceRequestBean = null;

	@SuppressWarnings({ "unchecked", "unused" })
	public static HotelSearchRespDataBean prepareFareRuleJson(HotelSearchRespDataBean hotelSearchRespDataBean,HotelSearchRequestBean hotelSearchRequestBean,HotelManager hotelManager,com.tt.satguruportal.hotel.manager.HotelManager hotelManagerPortal,HotelBookService hotelBookService,UserSessionBean userSessionBean,HttpServletRequest request)
	{
		String formattedJson = null;
		ObjectMapper mapper = null;
		HotelSearchRespDataBean hotels = null;
		HotelSearchResponseBean mainObj=null;
		List<HotelsFareRuleRespBean> respList = null;
		HotelsFareRuleRespBean respBean = null;
		List<Object[]> resultList = null;
		HotelServices hotelService = new HotelServices();
		String spCode = null;
		String currencyCode = null;
		String spName = null;
		List<HotelRoomCategoryBean> hotelDetailRoomList = null;
		Boolean available = false;
		Boolean roomAdded = false;
		float fareWithTax=0;
		String sessionId = null;
		int noOfRooms = 1;
		try 
		{
			mapper = new ObjectMapper();
			noOfRooms = hotelSearchRequestBean.getNoOfRooms();

			respList = new ArrayList<>();
			hotels = new HotelSearchRespDataBean();
			/////////////////////////////////////////// Private Inventory Hotels Price Set /////////////////////////////////////////////////
			String suppProductJson = userSessionBean.getProductSupplierJson();
			ServiceConfigModel configModel = HotelRestUtil.prepareServiceConf(suppProductJson);
			try {
				resultList = hotelService.getPInventoryPriceData(hotelSearchRequestBean,hotelManager);
				if(null != resultList && !resultList.isEmpty())
				{
					Map<String,Float> taxMap = new HashMap<>();
					A:for(Object[] col : resultList)
					{
						if(!"###HOTEL###".equalsIgnoreCase(col[4].toString()) && "###HOTEL###".equalsIgnoreCase(col[28].toString()))
						{
							roomAdded = false;
							available = false;
							taxMap = new HashMap<>();
							respBean = new HotelsFareRuleRespBean();
							hotelDetailRoomList = new ArrayList<>();
							fareWithTax = 0;
							if(null!=col[0])
								respBean.setHotelCode(col[0].toString());

							if(null!=col[3])
								respBean.setHotelGroup(col[3].toString());

							if(null!=col[4])
								respBean.setHotelName(col[4].toString());
							
							if(null!=col[7])
								respBean.setCurrencyCode(col[7].toString());

							respBean.setDestinationCountry(hotelSearchRequestBean.getCountryName());
							respBean.setDestinationCity(hotelSearchRequestBean.getCityName());
							respBean.setBookingStartDate(hotelSearchRequestBean.getCheckInDate());
							respBean.setBookingEndDate(hotelSearchRequestBean.getCheckOutDate());
							respBean.setNationality(hotelSearchRequestBean.getGuestNationality());
							respBean.setNoOfPax(hotelSearchRequestBean.getTotalPax());
							respBean.setNoOfRooms(hotelSearchRequestBean.getNoOfRooms());
							respBean.setDuration(hotelSearchRequestBean.getDuration());

							if(null!=col[10])
								respBean.setCurrencyCode(col[10].toString());
							//respBean.setFareFromTo(totalRoomPrice);
						}
						if(!"###TAX_MAP###".equalsIgnoreCase(col[4].toString()) && "###TAX_MAP###".equalsIgnoreCase(col[28].toString()))
						{
							if(null!=col[7])
							{
								if(col[7].toString().equalsIgnoreCase("percent"))
								{
									if(null!=col[8])
										taxMap.put("percent", Float.parseFloat(col[8].toString()));
								}
								else if(col[7].toString().equalsIgnoreCase("amount"))
								{
									if(null!=col[9])
										taxMap.put("amount", Float.parseFloat(col[9].toString()));
								}
							}
						}
						if(!"###ROOMS###".equalsIgnoreCase(col[4].toString()) && "###ROOMS###".equalsIgnoreCase(col[28].toString()))
						{
							if(!roomAdded)
							{
								HotelRoomCategoryBean roomBean =new HotelRoomCategoryBean();
								if(null!=col[4])
									roomBean.setRoomIndex(col[4].toString());
								roomBean.setRoomCode(col[4].toString());
	
								if(null!=col[12])
									roomBean.setRoomType(col[12].toString());
								roomBean.setRoomName(col[12].toString());
	
								if(null!=col[9])
									roomBean.setNoOfRooms(String.valueOf(noOfRooms));
	
								
								if(null!=col[15])
									roomBean.setMaxAdult(col[15].toString());
								
								if(null!=col[16])
									roomBean.setMaxChildren(col[16].toString());
								
								if(null!=col[9] && null!=col[18])
								{
									int roomsAvail = Integer.parseInt(col[9].toString()) - Integer.parseInt(col[18].toString());
									roomBean.setNoOfAvailRooms(String.valueOf(roomsAvail));
								}
								
								CallLog.info(11,"setMaxExtraBed============>>"+col[30]);
								CallLog.info(11,"setMaxExtraBed============>>"+col[31]);
								if(null!=col[30])
									roomBean.setMaxExtraBed(col[30].toString());
	
								if(null!=col[31])
									roomBean.setExtraBedPrice(col[31].toString());
	
								if(null!=col[19])
								{
									float taxPrice = 0;
									available = true;
									if(null!=taxMap)
									{
										for (Map.Entry<String, Float> entry : taxMap.entrySet())
										{
											if(entry.getKey().equalsIgnoreCase("percent"))
											{
												taxPrice = taxPrice + (Float.parseFloat(col[19].toString()) * entry.getValue()) / 100;
											}
											else if(entry.getKey().equalsIgnoreCase("amount"))
											{
												taxPrice = taxPrice +  entry.getValue();
											}
										}
									}
									fareWithTax = (Float.parseFloat(col[19].toString()) * noOfRooms) + taxPrice;
									//float perRoomPrice = fareWithTax /  noOfRooms;
									roomBean.setPrice(col[19].toString());
									roomBean.setTotal(col[19].toString());
									respBean.setTaxsAmount(String.valueOf(taxPrice));
									hotelDetailRoomList.add(roomBean);
									roomAdded = true;
								}
						}

						}

						if(!"###SUPP_MAP###".equalsIgnoreCase(col[4].toString()) && "###SUPP_MAP###".equalsIgnoreCase(col[28].toString()))
						{
							if(null!=col[5])
								respBean.setSupplierCode(col[5].toString());

							if(null!=col[4])
								respBean.setSupplier(col[4].toString());

							respBean.setFareFromTo(Double.parseDouble(String.valueOf(fareWithTax)));
							respBean = processPriceCurrency(respBean, configModel, request, hotelManagerPortal, hotelBookService,3.67,userSessionBean);
							respBean.setHotelListRoomDetails(hotelDetailRoomList);
							respBean.setCredentialId(9999);

							if(available)
								respList.add(respBean);
						}
					}
				}
			} catch (Exception e1) {
				CallLog.info(1, "Exception Occured into PI Procedure Condition2 and Exception::"+e1.getMessage());
				CallLog.printStackTrace(1, e1);
			}
			hotelSearchRespDataBean.getRespList().addAll(respList);
		} 
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}

		return hotelSearchRespDataBean;

	}
	
	@SuppressWarnings("unchecked")
	public static HotelsFareRuleRespBean processPriceCurrency(HotelsFareRuleRespBean respBean, ServiceConfigModel configModel, HttpServletRequest request,com.tt.satguruportal.hotel.manager.HotelManager hotelManagerPortal,HotelBookService hotelBookService,double convCurrRate,UserSessionBean userSessionBean) throws ParseException
	{
		Double convertedFare = 0.0;
		String currencyCode = configModel.getCurrencyCode();
		String supplierCurrCode = respBean.getCurrencyCode();
		
		if(null!=supplierCurrCode)
		{
			if(!currencyCode.equalsIgnoreCase(supplierCurrCode))
			{
					if(null!=respBean.getFareFromTo())
					{
						Double fare = respBean.getFareFromTo();
						//Double currRate = getCurrencyRate(currencyCode,supplierCurrCode,hotelManagerPortal);
						Double currRate = convCurrRate;
						if(currRate!=0.0)
						{
							convertedFare = fare * currRate;
							respBean.setFareFromTo(convertedFare);
							respBean.setCurrencyCode(currencyCode);
						}
						else
						{
							respBean.setCurrencyCode(supplierCurrCode);
							//if(null!=request)
								//hotelBookService.sendCurrencyNotification(respBean.getSupplierCode(), supplierCurrCode, currencyCode, request,userSessionBean);
						}
						respBean.setSupplierCurrency(supplierCurrCode);
						respBean.setAgencyCurrency(currencyCode);
						respBean.setSupplierPrice(fare);
						respBean.setCurrConvertRate(currRate);
					}
					else
					{
						respBean.setSupplierCurrency(currencyCode);
						respBean.setAgencyCurrency(currencyCode);
						respBean.setSupplierPrice(respBean.getFareFromTo());
						respBean.setCurrConvertRate(1.0);
						respBean.setCurrencyCode(currencyCode);
					}
				}
			else
			{
				respBean.setSupplierCurrency(supplierCurrCode);
				respBean.setAgencyCurrency(currencyCode);
				respBean.setCurrConvertRate(1.0);
				respBean.setCurrencyCode(currencyCode);
				respBean.setSupplierPrice(respBean.getFareFromTo());
			}
		}
		return respBean;
	}
	public static Double getCurrencyRate(String agencyCurrCode,String suppCurrCode,com.tt.satguruportal.hotel.manager.HotelManager hotelManagerPortal) throws ParseException
	{
		Double currencyRate = 0.0;
		try
		{
			String fromCurrency =getCurrencyId(suppCurrCode,hotelManagerPortal);
			String toCurrency = getCurrencyId(agencyCurrCode,hotelManagerPortal);
			List<Object[]> currRate =hotelManagerPortal.getFactCurrencyRate(fromCurrency,toCurrency);
			if(currRate!=null && !currRate.isEmpty()){
				currencyRate= Double.parseDouble(String.valueOf(currRate.get(0)));
			}
		}
		catch (Exception e)
		{
			TTPortalLog.info(2, e);
		}
		
		return currencyRate;
	}
	public static String getCurrencyId(String currencyCode,com.tt.satguruportal.hotel.manager.HotelManager hotelManagerPortal)
	{
		List<Object> currencyIdList = new ArrayList<>();
		String currencyId = null;
		try
		{
			currencyIdList = hotelManagerPortal.getCurrencyId(currencyCode);
			if (currencyIdList != null && !currencyIdList.isEmpty())
			{
				Iterator<Object> it = currencyIdList.iterator();
				while(it.hasNext())
				{
					Object o = it.next();
					if(null != o)
						currencyId = o.toString();
				}
			}
		}
		catch (Exception e)
		{
			TTPortalLog.printStackTrace(103,e);
		}
		return currencyId;
	}
	
	public static HotelSearchRequestBean keyForTenFilteredResults(HotelSearchRequestBean requestBean,String agencyid) {
		String hotelName = requestBean.getHotelName()!=null?requestBean.getHotelName():"";
		String geodistance =requestBean.getGeoDistance()!=null?requestBean.getGeoDistance():"";
		String latitude =requestBean.getLatitude()!=null?String.valueOf(requestBean.getLatitude()):"";
		String longitude =requestBean.getLongitude()!=null?String.valueOf(requestBean.getLongitude()):"";
		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append(requestBean.getCityName().replaceAll("\\s", "") + "_" +  requestBean.getCountryName().replaceAll("\\s", "") + "_" + requestBean.getCheckInDate() + "_" + requestBean.getCheckOutDate() + "_"
				+ hotelName.replaceAll("\\s", "") + "_"	+ requestBean.getGuestNationality().replaceAll("\\s", "") + "_" + requestBean.getNoOfChildren() + "_" + requestBean.getNoOfRooms() + "_" + requestBean.getNumberOfAdults() + "_"  + requestBean.getTotalPax() + "_" + requestBean.getTotalResultCount() + "_" + latitude.replaceAll("\\s", "") + "_" + longitude.replaceAll("\\s", "") + "_" + geodistance.replaceAll("\\s", "") + "_" + agencyid + "_Filtered_10");
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			requestBean.setHotelSearchKey(cacheKey);
			
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return requestBean;
	}
	
	public static HotelSearchRequestBean keyForFilteredResults(HotelSearchRequestBean requestBean,String agencyid) {
		String hotelName = requestBean.getHotelName()!=null?requestBean.getHotelName():"";
		String geodistance =requestBean.getGeoDistance()!=null?requestBean.getGeoDistance():"";
		String latitude =requestBean.getLatitude()!=null?String.valueOf(requestBean.getLatitude()):"";
		String longitude =requestBean.getLongitude()!=null?String.valueOf(requestBean.getLongitude()):"";

		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append(requestBean.getCityName().replaceAll("\\s", "") + "_" +  requestBean.getCountryName().replaceAll("\\s", "") + "_" + requestBean.getCheckInDate() + "_" + requestBean.getCheckOutDate() + "_"
				+ hotelName.replaceAll("\\s", "") + "_"	+ requestBean.getGuestNationality().replaceAll("\\s", "") + "_" + requestBean.getNoOfChildren() + "_" + requestBean.getNoOfRooms() + "_" + requestBean.getNumberOfAdults() + "_"  + requestBean.getTotalPax() + "_" + requestBean.getTotalResultCount() + latitude.replaceAll("\\s", "") + "_" + longitude.replaceAll("\\s", "") + "_" + geodistance.replaceAll("\\s", "") + "_" + agencyid + "_Filtered");
			
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			requestBean.setHotelSearchKey(cacheKey);
			
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return requestBean;
	}
	public static List<HotelSatguruModel> getSelectHotelFiltered(String hotelName,List<HotelSatguruModel> filteredHotels)
	{
		
		List<HotelSatguruModel> satList = filteredHotels.stream().filter(model -> model.getHotelName().equalsIgnoreCase(hotelName)).collect(Collectors.toList());
		return satList;
	}

	public static HotelSearchRequestBean prepareSearchKeyForPrice(HotelSearchRequestBean requestBean,String agencyid) {
		
		//String geodistance =requestBean.getGeoDistance()!=null?requestBean.getGeoDistance():"";
		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append(requestBean.getCityName().replaceAll("\\s", "") + "_" +  requestBean.getCountryName().replaceAll("\\s", "") + "_" + requestBean.getCheckInDate() + "_" + requestBean.getCheckOutDate() + "_"
					+ requestBean.getGuestNationality().replaceAll("\\s", "") + "_" + requestBean.getNoOfChildren() + "_" + requestBean.getNoOfRooms() + "_" + requestBean.getNumberOfAdults() + "_"  + requestBean.getTotalPax() + "_" + requestBean.getTotalResultCount() +"_"+ agencyid);
			
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			requestBean.setHotelSearchKey(cacheKey);
			
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return requestBean;
	}
	
	public static HotelSearchReqDataBean prepareKeyLimitData(HotelSearchReqDataBean hotelSearchReqDataBean) {

		String cityName = hotelSearchReqDataBean.getCityName();
		String countryName = hotelSearchReqDataBean.getCountryName();
		String hotelName = hotelSearchReqDataBean.getHotelName()!=null?hotelSearchReqDataBean.getHotelName():"";
		String latitude =hotelSearchReqDataBean.getLatitude()!=null?hotelSearchReqDataBean.getLatitude():"";
		String longitude =hotelSearchReqDataBean.getLongitude()!=null?hotelSearchReqDataBean.getLongitude():"";
		String geodistance =hotelSearchReqDataBean.getGeoDistance()!=null?hotelSearchReqDataBean.getGeoDistance():"";
		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append(geodistance.replaceAll("\\s", "") +latitude.replaceAll("\\s", "") + longitude.replaceAll("\\s", "") +hotelName.replaceAll("\\s", "") + cityName.replaceAll("\\s", "") + "_" + countryName.replaceAll("\\s", "") + "_limit");
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			hotelSearchReqDataBean.setHotelSearchKey(cacheKey);
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return hotelSearchReqDataBean;
	}

	public static HotelSearchReqDataBean prepareKeyStaticData(HotelSearchReqDataBean hotelSearchReqDataBean) {

		String cityName = hotelSearchReqDataBean.getCityName();
		String countryName = hotelSearchReqDataBean.getCountryName();
		String hotelName = hotelSearchReqDataBean.getHotelName()!=null?hotelSearchReqDataBean.getHotelName():"";
		String latitude =hotelSearchReqDataBean.getLatitude()!=null?hotelSearchReqDataBean.getLatitude():"";
		String longitude =hotelSearchReqDataBean.getLongitude()!=null?hotelSearchReqDataBean.getLongitude():"";
		String geodistance =hotelSearchReqDataBean.getGeoDistance()!=null?hotelSearchReqDataBean.getGeoDistance():"";
		try 
		{
			StringBuilder searchKeyBuilder = new StringBuilder("");
			searchKeyBuilder.append(geodistance.replaceAll("\\s", "") +latitude.replaceAll("\\s", "") + longitude.replaceAll("\\s", "") +hotelName.replaceAll("\\s", "") + cityName.replaceAll("\\s", "") + "_" + countryName.replaceAll("\\s", ""));
			String cacheKey = searchKeyBuilder.toString();
			if(null != cacheKey && !cacheKey.isEmpty())
				cacheKey = cacheKey.toUpperCase();
			hotelSearchReqDataBean.setHotelSearchKey(cacheKey);
		} catch (Exception e) {
			CallLog.printStackTrace(1, e);
		}
		return hotelSearchReqDataBean;
	}
	/**
	 * This method is used to send search request for price and get commonResponse from various suppliers
	 * @param serviceRequestBean
	 * @param requestBean
	 * @return
	 */
	public static HotelSearchRespDataBean getHotelSearchResponse(ServiceRequestBean serviceRequestBean, HotelSearchRequestBean requestBean,HotelManager hotelManager,com.tt.satguruportal.hotel.manager.HotelManager hotelManagerPortal,HotelBookService hotelBookService,UserSessionBean userSessionBean,HttpServletRequest request) {
		long suppStartTime = System.currentTimeMillis();
		HotelSearchRespDataBean hotelSearchRespDataBean = null;
		ServiceResolverFactory serviceResolverFactory;
		ServiceResponseBean serviceResponseBean;
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.search.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelCommonSearchRespBean searchResBean = (HotelCommonSearchRespBean) serviceResponseBean.getResponseBean();
		hotelSearchRespDataBean = new HotelSearchRespDataBean();
		if(null!=searchResBean && null!=searchResBean.getFareRuleRespList() && !searchResBean.getFareRuleRespList().isEmpty())
		{
			CallLog.info(103, "[HotelUtil(REST)] TOTAL TIME TO RECEIVE ALL SUPP RESP FROM TTESB>> "+(System.currentTimeMillis() - suppStartTime)+" MILLISECS");
			try 
			{
				hotelSearchRespDataBean.setHotelCodes(searchResBean.getHotelCodes());
				hotelSearchRespDataBean.setRespList(searchResBean.getFareRuleRespList());
				hotelSearchRespDataBean.setSupplierRespTime(searchResBean.getSupplierRespTime());
				hotelSearchRespDataBean = HotelUtil.prepareFareRuleJson(hotelSearchRespDataBean, requestBean,hotelManager,hotelManagerPortal,hotelBookService,userSessionBean,request);
			} 
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		}
		hotelSearchRespDataBean.setSuppCurrencyFail(searchResBean.getSuppCurrencyFail());
		return hotelSearchRespDataBean;
	}

	/**
	 * This method is used to set clarifiId for hotels which we get from supplier
	 * @param suppResp
	 * @param hotelServices
	 * @return
	 */
	public static HotelSearchRespDataBean getMinPriceResp(HotelSearchRespDataBean suppResp,HotelServices hotelServices)
	{
		List<HotelsFareRuleRespBean> respList = null;
		List<Object[]> modelList = null;

		Map<String,String> hotelCodeMap = new HashMap<>();
		try
		{
			if(null != suppResp.getRespList() && !suppResp.getRespList().isEmpty())
			{
				respList = new CopyOnWriteArrayList<>(suppResp.getRespList());
				if(null != suppResp && null != suppResp.getHotelCodes() && suppResp.getHotelCodes().length()>0)
				{ 
					long startTime = System.currentTimeMillis();
					StringBuilder hotelCodesStr = new StringBuilder("");
					hotelCodesStr.append("'");
					hotelCodesStr.append(String.valueOf(suppResp.getHotelCodes().deleteCharAt(suppResp.getHotelCodes().length() - 1)).replaceAll(",", "','"));
					hotelCodesStr.append("'");
					suppResp.setHotelCodes(null);
					modelList = hotelServices.getHotelMapInfo(String.valueOf(hotelCodesStr));
					//CallLog.info(103, "hotelCodesStr:::::>>" +String.valueOf(hotelCodesStr));
					CallLog.info(103, "Time taken to fetch clarifiID:::::>>" +(System.currentTimeMillis() - startTime)+" milisecs");
					hotelCodesStr = null;
					if(null != modelList && !modelList.isEmpty())
					{
						for(Object[] rows : modelList)
						{
							hotelCodeMap.put(String.valueOf(rows[1]), String.valueOf(rows[0]));
						}
					}
					
					for(HotelsFareRuleRespBean respListObj : respList)
					{
						StringBuilder hotelCode = new StringBuilder("");
						if(null != respListObj.getSupplierCode() && !respListObj.getSupplierCode().isEmpty() && respListObj.getSupplierCode().contains("GTA,GDI"))
						{
							hotelCode.append(respListObj.getCityCode());
							hotelCode.append(";");
							hotelCode.append(respListObj.getHotelCode());
						}
						else
							hotelCode.append(respListObj.getHotelCode());
						
						if(hotelCodeMap.containsKey(hotelCode.toString()))
						    respListObj.setClarifiId(hotelCodeMap.get(respListObj.getHotelCode()));
						else
							respList.remove(respListObj);
					}
				}
			}
			suppResp.setRespList(respList);
		}
		catch(Exception e)
		{
			CallLog.printStackTrace(1, e);
		}
		return suppResp;
	}
	public static String getJsonAsString(HotelSearchRespDataBean searchRespDataBean)
	{
		ObjectMapper mapper = null;
		String jsonStr = null;
		JsonNode parsedJson = null;
	//	ObjectNode outerObject = null;
		try {
			mapper = new ObjectMapper();
			parsedJson = mapper.readTree(mapper.writeValueAsString(searchRespDataBean));
			//outerObject = mapper.createObjectNode(); 
			//outerObject.putPOJO("hotels",parsedJson); 
			jsonStr = parsedJson.toString();
		} catch (IOException e) {
			CallLog.printStackTrace(1, e);
		} 
		return jsonStr;
	}
	public static String getJsonAsString(List<HotelSatguruModel> resultList)
	{
		ObjectMapper mapper = null;
		String jsonStr = null;
		JsonNode parsedJson = null;
		ObjectNode outerObject = null;
		try {
			mapper = new ObjectMapper();
			parsedJson = mapper.readTree(mapper.writeValueAsString(resultList));
			outerObject = mapper.createObjectNode(); 
			outerObject.putPOJO("hotels",parsedJson); 
			jsonStr = outerObject.toString();
		} catch (IOException e) {
			CallLog.printStackTrace(1, e);
		} 
		return jsonStr;
	}
	public static ServiceConfigModel setServiceConfigModel()
	{
		ServiceConfigModel serviceConfigModel=new ServiceConfigModel();

		SupplierConfigModal supplierConfigExpModel = new SupplierConfigModal();
		SupplierConfigModal supplierConfigGtaModel = new SupplierConfigModal();
		SupplierConfigModal supplierConfigTboModel = new SupplierConfigModal();
		SupplierConfigModal supplierConfigTravcoModel = new SupplierConfigModal();
		SupplierConfigModal supplierConfigLohModel = new SupplierConfigModal();
		SupplierConfigModal supplierConfigMikiModel = new SupplierConfigModal();
		SupplierConfigModal supplierConfigHotelBedsModel = new SupplierConfigModal();
		List <SupplierConfigModal> supplierConfigModelL=new ArrayList<SupplierConfigModal>();

		CredentialConfigModel credentialsC=new CredentialConfigModel();
		List <CredentialConfigModel> credentialsExpL=new ArrayList<CredentialConfigModel>();


		// ************** setting Expedia Credentials ********************		

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("ExpSearchUrl");
		credentialsC.setCredentialValue("http://api.ean.com/ean-services/rs/hotel/v3/");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("ExpResvUrl");
		credentialsC.setCredentialValue("https://book.api.ean.com/ean-services/rs/hotel/v3/");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("minorRev");
		credentialsC.setCredentialValue("30");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("cid");
		credentialsC.setCredentialValue("501570");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("apiKey");
		credentialsC.setCredentialValue("58v5m81dmei2gueopptoe9mgev");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("sharedSecret");
		credentialsC.setCredentialValue("f33l4rd9uahg0");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("locale");
		credentialsC.setCredentialValue("en_US");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("expCurrencyCode");
		credentialsC.setCredentialValue("USD");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("cardHolderEmail");
		credentialsC.setCredentialValue("hotels.dubai@satgurutravel.com");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("cardHolderFirstName");
		credentialsC.setCredentialValue("tester");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("cardHolderLastName");
		credentialsC.setCredentialValue("testers");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("cardHolderHomePhone");
		credentialsC.setCredentialValue("2145370159");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardType");
		credentialsC.setCredentialValue("CA");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardNumber");
		credentialsC.setCredentialValue("5401999999999999");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardIdentifier");
		credentialsC.setCredentialValue("123");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardExpirationMonth");
		credentialsC.setCredentialValue("11");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardExpirationYear");
		credentialsC.setCredentialValue("2017");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardHolderAddress");
		credentialsC.setCredentialValue("travelnow");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardHolderCity");
		credentialsC.setCredentialValue("Bellevue");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardHolderState");
		credentialsC.setCredentialValue("WA");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardHolderCountry");
		credentialsC.setCredentialValue("US");
		credentialsExpL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("creditCardHolderPostalCode");
		credentialsC.setCredentialValue("98004");
		credentialsExpL.add(credentialsC);


		supplierConfigExpModel.setSupplierName("Expedia");
		supplierConfigExpModel.setCredential(credentialsExpL);
		supplierConfigModelL.add(supplierConfigExpModel);


		// ************** setting GTA Credentials ********************		

		List <CredentialConfigModel> credentialGtaL=new ArrayList<CredentialConfigModel>();

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("gtaRequestUrl");
		credentialsC.setCredentialValue("https://interface.demo.gta-travel.com/gtaapi/RequestListenerServlet");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("clientId");
		credentialsC.setCredentialValue("545");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("emailId");
		credentialsC.setCredentialValue("rahul@satgurutravel.com");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("gtaPassword");
		credentialsC.setCredentialValue("PASS");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("language");
		credentialsC.setCredentialValue("en");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("currency");
		credentialsC.setCredentialValue("INR");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("country");
		credentialsC.setCredentialValue("IN");
		credentialGtaL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("requestMode");
		credentialsC.setCredentialValue("SYNCHRONOUS");
		credentialGtaL.add(credentialsC);

		supplierConfigGtaModel.setSupplierName("GTA");
		supplierConfigGtaModel.setCredential(credentialGtaL);
		supplierConfigModelL.add(supplierConfigGtaModel);



		// ************** setting TBO Credentials ********************		

		List <CredentialConfigModel> credentialTboL=new ArrayList<CredentialConfigModel>();


		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("TboRequestUrl");
		credentialsC.setCredentialValue("http://api.tbotechnology.in/HotelAPI_V7/HotelService.svc");
		credentialTboL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("soapenv");
		credentialsC.setCredentialValue("");
		credentialTboL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("TboUserName");
		credentialsC.setCredentialValue("satguru");
		credentialTboL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("TboPassword");
		credentialsC.setCredentialValue("satguru@1");
		credentialTboL.add(credentialsC);

		supplierConfigTboModel.setSupplierName("TBO");
		supplierConfigTboModel.setCredential(credentialTboL);
		supplierConfigModelL.add(supplierConfigTboModel);

		// ************** setting Travco Credentials ********************	

		List <CredentialConfigModel> credentialTravcoL=new ArrayList<CredentialConfigModel>();

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("travcoRequestUrl");
		credentialsC.setCredentialValue("http://xmlv5test.travco.co.uk/trlink/link1/trlink");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("agentCode");
		credentialsC.setCredentialValue("163ABC");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("agentPassword");
		credentialsC.setCredentialValue("260513AB66");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("availabilityRequest");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv6test.travco.co.uk/trlink/schema/HotelAvailabilityV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("hotelDetailsRequest");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/HotelDetailRequestV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("allocationEnquery");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/AllocEnquiryV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("hotelBooking");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/HBookingV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("hotelCancelDetails");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/HotelCancellationDetailsV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("hotelBookCancel");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/CancellationDetailsV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("cityRequest");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/CityRequestV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("countryRequest");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/CountryRequestV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("hotelStarRequest");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/HotelStarRequestV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("hotelCityRequest");
		credentialsC.setCredentialValue("xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='http://xmlv5test.travco.co.uk/trlink/schema/HotelsForACityRequestV6Snd.xsd'");
		credentialTravcoL.add(credentialsC);

		supplierConfigTravcoModel.setSupplierName("Travco");
		supplierConfigTravcoModel.setCredential(credentialTravcoL);
		supplierConfigModelL.add(supplierConfigTravcoModel);

		// ************************** setting LOH Credentials **********************************************		

		List <CredentialConfigModel> credentialLohL=new ArrayList<CredentialConfigModel>();

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("LohUserName");
		credentialsC.setCredentialValue("XMLSatG");
		credentialLohL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("LohPassword");
		credentialsC.setCredentialValue("w52PWbAbvd");
		credentialLohL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("LohRequestUrl");
		credentialsC.setCredentialValue("http://xml2.bookingengine.es/webservice/jp/operations/");
		credentialLohL.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("LohVersion");
		credentialsC.setCredentialValue("1.1");
		credentialLohL.add(credentialsC);

		supplierConfigLohModel.setSupplierName("LOH");
		supplierConfigLohModel.setCredential(credentialLohL);
		supplierConfigModelL.add(supplierConfigLohModel);


		/*========================setting Miki Travel Credentials===================================== */

		List <CredentialConfigModel> credentialMiki=new ArrayList<CredentialConfigModel>();

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("mikiAgentCode");
		credentialsC.setCredentialValue("SGV001");
		credentialMiki.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("mikiPassword");
		credentialsC.setCredentialValue("PASSWORDPASSWORD1234");
		credentialMiki.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("mikiCurrencyCode");
		credentialsC.setCredentialValue("EUR");
		credentialMiki.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("mikiLangCode");
		credentialsC.setCredentialValue("en");
		credentialMiki.add(credentialsC);


		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("mikiRequestUrl");
		credentialsC.setCredentialValue("https://test.miki.co.uk/interfaceWL/ws/7.0/");
		credentialMiki.add(credentialsC);


		supplierConfigMikiModel.setSupplierName("MIKI");
		supplierConfigMikiModel.setCredential(credentialMiki);
		supplierConfigModelL.add(supplierConfigMikiModel);

		//==================Setting HotelBeds Credentials ==============================================
		List <CredentialConfigModel> credentialHotelBeds=new ArrayList<CredentialConfigModel>();

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("HotelBedsRequestUrl");
		credentialsC.setCredentialValue("https://api.test.`.com/hotel-api/1.0/");
		credentialHotelBeds.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("HotelBedsAPIKey");
		credentialsC.setCredentialValue("ur9f73ysancszajgncvzc7bw");
		credentialHotelBeds.add(credentialsC);

		credentialsC=new CredentialConfigModel();
		credentialsC.setCredentialName("HotelBedsSecretKey");  //sha256Hex : x-signature 
		credentialsC.setCredentialValue("h5qBVWWsyM");
		credentialHotelBeds.add(credentialsC);

		supplierConfigHotelBedsModel.setSupplierName("HotelBeds");
		supplierConfigHotelBedsModel.setCredential(credentialHotelBeds);
		supplierConfigModelL.add(supplierConfigHotelBedsModel);


		serviceConfigModel.setProductName("Hotel");
		serviceConfigModel.setSupplier(supplierConfigModelL);


		return serviceConfigModel;
	}


}
