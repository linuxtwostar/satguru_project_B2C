package com.tt.ws.rest.carbooking.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tt.nc.common.util.QueryConstant;
import com.tt.nc.user.model.User;
import com.tt.satguruportal.car.manager.CarManager;
import com.tt.satguruportal.car.model.SupplierInfoBean;
import com.tt.satguruportal.car.util.CarUtil;
import com.tt.satguruportal.hotel.manager.HotelManager;
import com.tt.satguruportal.hotel.util.HotelHelperUtil;
import com.tt.satguruportal.insurance.model.EmailNotificationEntity;
import com.tt.satguruportal.login.bean.UserSessionBean;
import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.jms.model.MessageBean;
import com.tt.ts.organization.model.OrganizationModel;
import com.tt.ts.organization.service.OrganizationService;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ts.rest.ruleengine.service.KieSessionService;
import com.tt.ts.rest.ruleengine.service.RuleSimulationService;
import com.tt.ts.staff.manager.StaffManager;
import com.tt.ws.rest.carbooking.utils.CarBookingUtils;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.carbooking.bean.CBAmendRequestBean;
import com.ws.services.carbooking.bean.CBAmendResponseBean;
import com.ws.services.carbooking.bean.CBBookingInfoRespBean;
import com.ws.services.carbooking.bean.CBBookingStatusRespBean;
import com.ws.services.carbooking.bean.CarBookingRequestBean;
import com.ws.services.carbooking.bean.CarBookingResponseBean;
import com.ws.services.carbooking.bean.CarRentalBookingReqBean;
import com.ws.services.carbooking.bean.CarRentalBookingRespBean;
import com.ws.services.carbooking.bean.CarRentalCancellationRespBean;
import com.ws.services.carbooking.bean.CarRentalInfoBean;
import com.ws.services.carbooking.bean.CarRentalSearchReqBean;
import com.ws.services.carbooking.bean.CarRentalSearchRespBean;
import com.ws.services.enums.ProductServiceEnum;
import com.ws.services.flight.config.ServiceConfigModel;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;

@Service
public class CarRentalBookingService {
	
	
	@Autowired
	ProductService productService;
	
	@Autowired
    RedisService redisService;
	
	@Autowired
	CarManager carManager;
	
	@Autowired
	private CarUtil carUtil;
	
	@Autowired
	transient MessageSource messageSource;
	
	@Autowired
	private StaffManager staffManager;
	
	@Autowired
	private OrganizationService organizationService;
	
	 @Autowired
	transient HotelManager hotelManager;

ServiceResolverFactory serviceResolverFactory = null;
	
	ServiceRequestBean serviceRequestBean = null;
	ServiceResponseBean serviceResponseBean=null;
	String logStr = "[CarRentalBookingService]";
	String jsonString = null;
	
	
	/*{ 
	   	"pickupLocationId":"LON",
	   	"pickupDate":"05/11/2017" ,
	    "dropOffLocationId" :"LON",  
		"dropOffDate" :"10/11/2017",
		"driverAge":"32"

	}*/

	
	public String searchCars(CarRentalSearchReqBean requestBean)
	{
		 	serviceRequestBean = new ServiceRequestBean();
			serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
			serviceRequestBean.setServiceName(ProductEnum.CarBooking.search.toString());
			serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
			//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
			serviceRequestBean.setRequestBean(requestBean);
			serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
			serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
			CarRentalSearchRespBean responseBean = (CarRentalSearchRespBean) serviceResponseBean.getResponseBean();
			if(null!=responseBean)
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
					jsonString = mapper.writeValueAsString(responseBean);
					CallLog.info(1,logStr+" Response bean values::: "+jsonString);
				} catch (Exception e) {
					CallLog.printStackTrace(1,e);
				}
			}
			return jsonString;	
	}


	/*{
		"pickupLocationId": "3372771",
		"pickupDate": "05/11/2017",
		"pickupHour": "11",
		"pickupMinutes": "30",
		"dropOffLocationId": "3372771",
		"dropOffDate": "10/11/2017",
		"dropOffHour": "2",
		"dropOffMinutes": "30",
		"extraListId": "607304703",
		"extraListAmount": "1",
		"vehicleId": "548494063",
		"driverTitle": "Mr",
		"driverFirstName": "Test",
		"driverLastName": "Abc",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "34567",
		"driverEmail": "abc@gmail.com",
		"driverTelephone": "4567890123",
		"driverAge": "32",
		"depositPayment": "false",
		"creditCardType": "4",
		"cardNumber": "5111111111111111",
		"ccv2": "123",
		"expirationYear": "2019",
		"expirationMonth": "08",
		"cardHolder": "Test Abc",
		"comments": "hfdjkfikkkl",
		"pickUpService": "test",
		"dropOffService": "test123",
		"flightNo": "abc 123",
		"AcceptedPrice": "574.94",
		"AcceptedCurrency": "GBP",
		"Adcamp": "example",
		"Adplat": "example"

	}*/
	
	public String getMakeBookingResp(CarRentalBookingReqBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.makeBooking.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalBookingRespBean responseBean = (CarRentalBookingRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}

	/*{
		"pickupLocationId": "3372771",
		"pickupDate": "05/11/2017",
		"pickupHour": "11",
		"pickupMinutes": "30",
		"dropOffLocationId": "3372771",
		"dropOffDate": "10/11/2017",
		"dropOffHour": "2",
		"dropOffMinutes": "30",
		"extraListId": "607304703",
		"extraListAmount": "1",
		"vehicleId": "548494063",
		"driverTitle": "Mr",
		"driverFirstName": "Test",
		"driverLastName": "Abc",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "34567",
		"driverEmail": "abc@gmail.com",
		"driverTelephone": "4567890123",
		"driverAge": "32",
		"comments": "hfdjkfikkkl",
		"pickUpService": "test",
		"dropOffService": "test123",
		"flightNo": "abc 123",
		"AcceptedPrice": "574.94",
		"AcceptedCurrency": "GBP",
		"Adcamp": "example",
		"Adplat": "example"
	
	}*/
	//322915834
	
	public String getSaveQuoteResp(CarRentalBookingReqBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.saveQuote.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalBookingRespBean responseBean = (CarRentalBookingRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}

	/*{
		"pickupLocationId": "3372771",
		"pickupDate": "05/11/2017",
		"pickupHour": "11",
		"pickupMinutes": "30",
		"dropOffLocationId": "3372771",
		"dropOffDate": "10/11/2017",
		"dropOffHour": "2",
		"dropOffMinutes": "30",
		"extraListId": "607304703",
		"extraListAmount": "1",
		"vehicleId": "548494063",
		"driverTitle": "Mr",
		"driverFirstName": "Test",
		"driverLastName": "Abc",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "34567",
		"driverEmail": "abc@gmail.com",
		"driverTelephone": "4567890123",
		"driverAge": "32",
		"depositPayment": "false",
		"creditCardType": "4",
		"cardNumber": "5111111111111111",
		"ccv2": "123",
		"expirationYear": "2019",
		"expirationMonth": "08",
		"cardHolder": "Test Abc"
		

	}*/
	
	public String getConvertQuoteResp(CarRentalBookingReqBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.convertQuote.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalBookingRespBean responseBean = (CarRentalBookingRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}

	/*{
		"bookingId": "322915834",
		
		"email": "abc@gmail.com"
		
	
	}*/
	
	public String getBookingInfo(CarBookingRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.bookingInfo.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBBookingInfoRespBean responseBean = (CBBookingInfoRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	/*{
		"bookingId": "322915834",
		
		"email": "abc@gmail.com"
	

	}*/
	
	public String getBookingStatus(CarBookingRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.bookingStatus.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBBookingStatusRespBean responseBean = (CBBookingStatusRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	/*{
		"bookingId": "322915834",
		"email": "abc@gmail.com",
		"pickUpYear": "2017",
		"pickUpMonth": "11",
		"pickUpDay": "5",
		"pickUpHour": "11",
		"pickUpMinute": "30",
		"dropOffYear": "2017",
		"dropOffMonth": "11",
		"dropOffDay": "10",
		"dropOffHour": "2",
		"dropOffMinute": "30",
		"driverCountry": "UK",
		"driverCity": "London",
		"driverStreet": "hjkk",
		"driverPostCode": "345678",
		"driverTelephone": "9988776644",
		"addlInfoComments": "jdfkgdkljgjogt",
		"airlineInfo": "ABC12"

	}*/
	
	
	public String getBookingAmend(CBAmendRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.bookingAmend.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CBAmendResponseBean responseBean = (CBAmendResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String getCancelFees(CarBookingRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.cancelFees.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarRentalCancellationRespBean responseBean = (CarRentalCancellationRespBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
	
	public String getCancelBooking(CarBookingRequestBean requestBean)
	{
		serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.CarBooking.toString());
		serviceRequestBean.setServiceName(ProductEnum.CarBooking.cancelBooking.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		//serviceRequestBean.setServiceConfigModel(CarBookingUtils.setServiceConfigModel()); 
		serviceRequestBean.setRequestBean(requestBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		CarBookingResponseBean responseBean = (CarBookingResponseBean) serviceResponseBean.getResponseBean();
		if(null!=responseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(responseBean);
				CallLog.info(1,logStr+" Response bean values::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}

	public String searchCarsPrice(CarRentalSearchReqBean searchRequestBean,UserSessionBean userSessionBean,ServiceConfigModel configModel,RuleSimulationService ruleSimulationService,HttpServletRequest request,int mut,float tam)
	{
		
		
		long startingTime = 0;
		long endingTime = 0;
		long totalTime = 0;
		long totalTimeInMillis = 0;	
		String jsonString = "";
		String appliedRuleResp = null;
		String agencyId=userSessionBean.getAgentId();
		String branchId=userSessionBean.getBranchId();
		String carSearchKey = CarUtil.prepareSearchKeyForPrice(searchRequestBean,agencyId,branchId);
		TTPortalLog.info(122, "carSearchKey for Price Call === " + carSearchKey);
		Boolean isSearchKeyFound = false;
		ResultBean resultBean = RedisCacheUtil.isSearchKeyInCacheCar(carSearchKey,redisService);
		if (resultBean != null && !resultBean.isError()) {
		    isSearchKeyFound = resultBean.getResultBoolean();
		}
				String countryId=userSessionBean.getCountryId();
				Long startTime = System.currentTimeMillis();
				boolean isRuleTextUpdated = false;
				ProductModel productModel = new ProductModel();
				productModel.setStatus(1);
				productModel.setProductCode(QueryConstant.RULE_GROUP_FOUR);
				ResultBean resultBeanProduct = productService.getProducts(productModel);
				if (resultBeanProduct != null && !resultBeanProduct.isError()) {
					List<ProductModel> productList = (List<ProductModel>) resultBeanProduct.getResultList();
					if (productList != null && !productList.isEmpty()) {
						ProductModel productModal = productList.get(0);
						if (productModal != null && productModal.getRuleText() != null && !productModal.getRuleText().isEmpty()) {

							Date ruleLastUpdated = productModal.getRuleLastUpdated();
							Date kieSessionLastUpdated = KieSessionService.kieCarSessionLastUpdated;
							if (ruleLastUpdated != null && kieSessionLastUpdated != null && ruleLastUpdated.getTime() < kieSessionLastUpdated.getTime()) {
								isRuleTextUpdated = true;
							}
							if (KieSessionService.kieSessCar != null && isRuleTextUpdated) {
								KieSessionService.getKieSessionCar();
							} else {
								startingTime = System.currentTimeMillis(); 
								boolean flagTextRule = KieSessionService.writeRuleTextCar(productModal.getRuleText());
								if (flagTextRule) {
									KieSessionService.kieSessCar = null;
									KieSessionService.getKieSessionCar();
									KieSessionService.kieCarSessionLastUpdated = new Date();
								}
								endingTime = System.currentTimeMillis();
								totalTime = (endingTime-startTime)/1000;
								totalTimeInMillis = endingTime-startingTime;
								TTPortalLog.info(122, "TOTAL TIME TO WRITE RULE TEXT ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
							}
							
						} else {
							KieSessionService.kieSessCar = null;
							TTPortalLog.info(122, "No approved Car rules found.");
						}
					}
				}
				String currencyCode = configModel.getCurrencyCode();
				CarRentalSearchRespBean carSearchRespBean = null;
				CommonUtil<Object> commonUtil = new CommonUtil<>();
				if(isSearchKeyFound){
					startingTime = System.currentTimeMillis(); 
					ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheCar(carSearchKey,redisService);
					endingTime = System.currentTimeMillis();
					totalTime = (endingTime-startTime)/1000;
					totalTimeInMillis = endingTime-startingTime;
					TTPortalLog.info(122, "TOTAL TIME TO FECTH PRICE RESPONSE FROM CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
				  
					if (resultBeanCache != null && !resultBeanCache.isError()) 
				    {
						jsonString = resultBeanCache.getResultString();
						startingTime = System.currentTimeMillis(); 
						carSearchRespBean = (CarRentalSearchRespBean) commonUtil.convertJSONIntoObject(jsonString, CarRentalSearchRespBean.class);
						carSearchRespBean.setCarSearchKey(carSearchKey);
						endingTime = System.currentTimeMillis();
						totalTime = (endingTime-startTime)/1000;
						totalTimeInMillis = endingTime-startingTime;
						TTPortalLog.info(122, "TOTAL TIME TO DESERIALIZE PRICE RESPONSE FROM SUPPLIER ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
						if (!isRuleTextUpdated) {
						startingTime = System.currentTimeMillis(); 
						jsonString = ruleSimulationService.applyRuleOnCarResult(carSearchRespBean,KieSessionService.kieSessCar,countryId,agencyId,branchId);
						endingTime = System.currentTimeMillis();
						totalTime = (endingTime-startTime)/1000;
						totalTimeInMillis = endingTime-startingTime;
						TTPortalLog.info(122, "TOTAL TIME TO APPLY RULE ON RESPONSE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
						if(!resultBean.isError()) {
							startingTime = System.currentTimeMillis(); 
					    	RedisCacheUtil.setResponseInCacheCar(carSearchKey, jsonString,redisService);
					    	endingTime = System.currentTimeMillis();
							totalTime = (endingTime-startTime)/1000;
							totalTimeInMillis = endingTime-startingTime;
							TTPortalLog.info(122, "TOTAL TIME TO SET RESPONSE IN CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
					    }
						}
						
					}else{
						startingTime = System.currentTimeMillis(); 
						jsonString = searchCars(searchRequestBean);
						carSearchRespBean = (CarRentalSearchRespBean) commonUtil.convertJSONIntoObject(jsonString, CarRentalSearchRespBean.class);
						carSearchRespBean.setCarSearchKey(carSearchKey);
						if (null != carSearchRespBean.getCarRentalInfo() && carSearchRespBean.getCarRentalInfo().size() != 0) 
						{
						jsonString = processPriceCurrency(searchRequestBean,jsonString,carSearchRespBean,userSessionBean,currencyCode,request);
						}
						endingTime = System.currentTimeMillis();
						totalTime = (endingTime-startingTime)/1000;
						totalTimeInMillis = endingTime-startingTime;
						TTPortalLog.info(122, "ALL SUPPLIERS TOTAL RESPONSE TIME[SECS] ::: "+totalTime +" SECONDS || TIME[MILLISECONDS]::" +totalTimeInMillis+" MILLISECONDS");
						if (null != jsonString && !jsonString.isEmpty()) 
						{
							startingTime = System.currentTimeMillis(); 
							carSearchRespBean = (CarRentalSearchRespBean) commonUtil.convertJSONIntoObject(jsonString, CarRentalSearchRespBean.class);
							endingTime = System.currentTimeMillis();
							totalTime = (endingTime-startTime)/1000;
							totalTimeInMillis = endingTime-startingTime;
							TTPortalLog.info(122, "TOTAL TIME TO DESERIALIZE PRICE RESPONSE FROM SUPPLIER ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
							startingTime = System.currentTimeMillis(); 
							appliedRuleResp = ruleSimulationService.applyRuleOnCarResult(carSearchRespBean,KieSessionService.kieSessCar,countryId,agencyId,branchId);
							endingTime = System.currentTimeMillis();
							totalTime = (endingTime-startTime)/1000;
							totalTimeInMillis = endingTime-startingTime;
							TTPortalLog.info(122, "TOTAL TIME TO APPLY RULE ON RESPONSE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
							//JSONObject updatedObj = HotelUtil.getMinPriceResp(appliedRuleResp, this);
							 if (null != appliedRuleResp)
						    {
								jsonString = appliedRuleResp.toString();
							    	startingTime = System.currentTimeMillis(); 
							    	RedisCacheUtil.setResponseInCacheCar(carSearchKey, jsonString,redisService);
							    	endingTime = System.currentTimeMillis();
									totalTime = (endingTime-startTime)/1000;
									totalTimeInMillis = endingTime-startingTime;
									TTPortalLog.info(122, "TOTAL TIME TO SET RESPONSE IN CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
							    
						    }
						}
					}
					
					
				}else
				{
				    	startingTime = System.currentTimeMillis(); 
				    	jsonString = searchCars(searchRequestBean);
				    	carSearchRespBean = (CarRentalSearchRespBean) commonUtil.convertJSONIntoObject(jsonString, CarRentalSearchRespBean.class);
						if (null != carSearchRespBean.getCarRentalInfo() && carSearchRespBean.getCarRentalInfo().size() != 0) 
						{
						carSearchRespBean.setCarSearchKey(carSearchKey);	
						jsonString = processPriceCurrency(searchRequestBean,jsonString,carSearchRespBean,userSessionBean,currencyCode,request);
						}
				    	endingTime = System.currentTimeMillis();
						totalTime = (endingTime-startingTime)/1000;
						totalTimeInMillis = endingTime-startingTime;
						TTPortalLog.info(122, "ALL SUPPLIERS TOTAL RESPONSE TIME[SECS] ::: "+totalTime +" SECONDS || TIME[MILLISECONDS]::" +totalTimeInMillis+" MILLISECONDS");
						if (null != jsonString && !jsonString.isEmpty()) 
						{
							startingTime = System.currentTimeMillis(); 
							carSearchRespBean = (CarRentalSearchRespBean) commonUtil.convertJSONIntoObject(jsonString, CarRentalSearchRespBean.class);
							endingTime = System.currentTimeMillis();
							totalTime = (endingTime-startTime)/1000;
							totalTimeInMillis = endingTime-startingTime;
							TTPortalLog.info(122, "TOTAL TIME TO DESERIALIZE PRICE RESPONSE FROM SUPPLIER ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
							startingTime = System.currentTimeMillis(); 
							appliedRuleResp = ruleSimulationService.applyRuleOnCarResult(carSearchRespBean,KieSessionService.kieSessCar,countryId,agencyId,branchId);
							endingTime = System.currentTimeMillis();
							totalTime = (endingTime-startTime)/1000;
							totalTimeInMillis = endingTime-startingTime;
							TTPortalLog.info(122, "TOTAL TIME TO APPLY RULE ON RESPONSE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
							 if (null != appliedRuleResp && null != carSearchRespBean && null != carSearchRespBean.getCarRentalInfo() && carSearchRespBean.getCarRentalInfo().size() != 0)
							    {
									jsonString = appliedRuleResp.toString();
								    	startingTime = System.currentTimeMillis(); 
								    	RedisCacheUtil.setResponseInCacheCar(carSearchKey, jsonString,redisService);
								    	endingTime = System.currentTimeMillis();
										totalTime = (endingTime-startTime)/1000;
										totalTimeInMillis = endingTime-startingTime;
										TTPortalLog.info(122, "TOTAL TIME TO SET RESPONSE IN CACHE ::: "+totalTime +" SECONDS || TIME(millisecs)::" +totalTimeInMillis+" milliseconds");
								    
							    }
						    
						}
				}
				Long endTime = System.currentTimeMillis();
				Long timeTaken = endTime - startTime;
				TTPortalLog.info(122, "TOTAL RESPONSE TIME(SECS)----->>>" + timeTaken/1000+" || TIME(MILLISECS)::"+timeTaken);
				
				carSearchRespBean = (CarRentalSearchRespBean) commonUtil.convertJSONIntoObject(jsonString, CarRentalSearchRespBean.class);
				List<CarRentalInfoBean> respList;
				respList = carSearchRespBean.getCarRentalInfo();
				
				double totalAgencyMarkup = 0f;
				
				for(int i=0;i<respList.size();i++)
				{
				
				double orgFare = (respList.get(i).getConvertedPrice());
				Float discPrice = Float.parseFloat(String.valueOf(respList.get(i).getDiscountPrice()));
				Float t3MarkUp = Float.parseFloat(String.valueOf(respList.get(i).getT3Price()));
				Float SrCharge = Float.parseFloat(String.valueOf(respList.get(i).getServiceChargePrice()));
				if (mut == 1)
				{
					totalAgencyMarkup = (orgFare + t3MarkUp + SrCharge - discPrice) * (tam / 100);
				}
				else
				totalAgencyMarkup = tam;
				
				respList.get(i).setTotalT3Price(t3MarkUp + orgFare + totalAgencyMarkup);
				respList.get(i).setT3pricewithoutDiscount(t3MarkUp + orgFare + totalAgencyMarkup - discPrice);
				respList.get(i).setGrossPrice(t3MarkUp + orgFare + totalAgencyMarkup + SrCharge - discPrice);
				respList.get(i).setGrossPriceForMarkUp(t3MarkUp + orgFare + totalAgencyMarkup + SrCharge - discPrice);
				respList.get(i).setTotalAgencyMarkUp(totalAgencyMarkup);
				respList.get(i).setDbT3Price(orgFare + t3MarkUp + SrCharge - discPrice);
				}
				carSearchRespBean.setCarRentalInfo(respList);
				if(null!=carSearchRespBean)
				{
					ObjectMapper mapper = new ObjectMapper();
					try {
						jsonString = mapper.writeValueAsString(carSearchRespBean);
					} catch (Exception e) {
						CallLog.printStackTrace(1,e);
					}
				}
				ArrayList<SupplierInfoBean> suppList=CarUtil.getSupplierList(jsonString);
				if(!suppList.isEmpty())
					try {
						addSupplier(suppList, userSessionBean.getUserId());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				TTPortalLog.info(122, "Car Search Response ::::" + jsonString);
		
		return jsonString;
		
		
	}


	public int addSupplier(ArrayList<SupplierInfoBean> suppList, int userid) throws Exception
	{
		
			return carManager.addSupplier(suppList, userid);
	}


	private String processPriceCurrency(
			CarRentalSearchReqBean searchRequestBean, String jsonString2,
			CarRentalSearchRespBean carSearchRespBean,
			UserSessionBean userSessionBean, String currencyCode,
			HttpServletRequest request) {
		String priceCurrResponse = null;
		List<CarRentalInfoBean> respList = null;
		Double convertedFare = 0.0;
		boolean currConvFound = false;
		String supplierCurrCode="";
		respList = carSearchRespBean.getCarRentalInfo();
		if(null != respList && !respList.isEmpty())
		{
		A :	for (int i = 0; i < respList.size(); i++)
			{
				supplierCurrCode = respList.get(i).getBaseCurrency(); 
				if(!currencyCode.equalsIgnoreCase(respList.get(i).getBaseCurrency()))
				{
					if(null!=supplierCurrCode)
					{
						if(null!=respList.get(i).getBasePrice())
						{
							Double fare =  Double.parseDouble(respList.get(i).getBasePrice());
							Double currRate = getCurrencyRate(currencyCode,supplierCurrCode);
							if(currRate!=0.0)
							{
								currConvFound = true;
								convertedFare = fare * currRate;
								respList.get(i).setConvertedPrice(convertedFare);
								respList.get(i).setAgencyCurrency(currencyCode);
								respList.get(i).setCurrConversionRate(currRate);
							}
							else
							{
								break A;
							}
						}
					}
				}
				else{
				currConvFound = true;
				respList.get(i).setAgencyCurrency(currencyCode);
				respList.get(i).setConvertedPrice(Float.parseFloat(respList.get(i).getBasePrice()));
				respList.get(i).setCurrConversionRate(0.0);
				}
			}
		}
		if(currConvFound){
			carSearchRespBean.setCarRentalInfo(respList);
			if(null!=carSearchRespBean)
				priceCurrResponse = HotelHelperUtil.convertObjectIntoJson(carSearchRespBean);
			}else{
				priceCurrResponse = "{\"errorMsg\":\"no result found.\"}";
				sendCurrencyNotification("RentalCars" , supplierCurrCode, currencyCode, request,userSessionBean);
		}
		return priceCurrResponse;
	}


	private void sendCurrencyNotification(String supplier,
			String supplierCurrency, String agencyCurrency,
			HttpServletRequest request, UserSessionBean userSessionBean) {
		String fromAddr = messageSource.getMessage("smtp.userName", null, null, null);
		User bookingUser = null;
		User superUser = null;
		String agencyEmail = null;
		List<Object> orgList = new ArrayList<>();
		Integer id = 0;
		OrganizationModel organizationModel = new OrganizationModel();
		
			try
			{
				bookingUser = staffManager.getUserById(userSessionBean.getUserId());
				organizationModel.setGroupId(Integer.parseInt(userSessionBean.getBranchId()));
				com.tt.ts.common.modal.ResultBean resultBean = organizationService.fetchBranchById(organizationModel);
				if(resultBean != null && resultBean.getResultObject() != null)
					organizationModel = (OrganizationModel) resultBean.getResultObject();
				
				superUser = staffManager.getUserById(1);
				String orgCode="";
				if(null!=userSessionBean){
					if(null!=userSessionBean.getAgencyCode())
					    orgCode = userSessionBean.getAgencyCode();
					else
						orgCode = userSessionBean.getBranchCode();
				}
				OrganizationModel orgModel = carManager.getOrgnizationFromId(orgCode);
				agencyEmail = orgModel.getOrganizationContactModel().get(0).getEmail();
				String branchEmail = organizationModel.getOrganizationContactModel() != null ?organizationModel.getOrganizationContactModel().get(0).getEmail(): null;
				String satguruAdminEmail = superUser.getEmail();
				
				String ccEmailString = agencyEmail + ", " + branchEmail + ", "+ satguruAdminEmail;
				EmailNotificationEntity mailNotificationEntity=null;
				if(userSessionBean.getAgencyCode()!=null)
					mailNotificationEntity = carManager.getEmailNotification(userSessionBean.getAgencyCode(), supplier + ", " + supplierCurrency + " to " + agencyCurrency + " Currency Conversion not found!", 16);
				else
					mailNotificationEntity = carManager.getEmailNotification(userSessionBean.getBranchCode(), supplier + ", " + supplierCurrency + " to " + agencyCurrency + " Currency Conversion not found!", 16);
				if(mailNotificationEntity == null){
					mailNotificationEntity = carUtil.prepareEmailNotificationEntity(supplier,supplierCurrency,agencyCurrency,mailNotificationEntity, userSessionBean, ccEmailString, fromAddr);
					mailNotificationEntity.setMailToAddress(messageSource.getMessage("fact.emailid", null, null, null));
					MessageBean beanMessage =carUtil.prepareMessageBeanForCurrencyMail(supplier,request, fromAddr, agencyEmail, supplierCurrency,userSessionBean,messageSource);
					mailNotificationEntity.setMailText(beanMessage.getMsgText());
					id = carManager.saveEmailNotification(mailNotificationEntity);
					TTPortalLog.info(122, "Currency Conversion not found ID for email for supplier ' "+supplier+" ' is == " +id);
               }
			}
			catch (NumberFormatException e)
			{
				TTPortalLog.printStackTrace(122,e);
			}
			catch (Exception e)
			{
				TTPortalLog.printStackTrace(122,e);
			}
	}


	public Double getCurrencyRate(String currencyCode, String supplierCurrCode) {
		Double currencyRate = 0.0;
		try
		{
			String fromCurrency =getCurrencyId(supplierCurrCode);
			String toCurrency = getCurrencyId(currencyCode);
			List<Object[]> currRate =hotelManager.getFactCurrencyRate(fromCurrency,toCurrency);
			if(currRate!=null && !currRate.isEmpty()){
				currencyRate= Double.parseDouble(String.valueOf(currRate.get(0)));
			}
		}
		catch (Exception e)
		{
			TTPortalLog.info(122, e);
		}
		
		return currencyRate;
	}


	private String getCurrencyId(String supplierCurrCode) {
		List<Object> currencyIdList = new ArrayList<>();
		String currencyId = null;
		try
		{
			currencyIdList = hotelManager.getCurrencyId(supplierCurrCode);
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
			TTPortalLog.printStackTrace(122,e);
		}
		return currencyId;
	}
}

