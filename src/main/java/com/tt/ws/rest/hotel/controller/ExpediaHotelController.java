package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.ExpediaHotelService;
import com.ws.services.hotel.bean.HotelAvailAndPricingReqBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelDealsRequestBean;
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelRoomAvailRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/expedia")
public class ExpediaHotelController 
{
	String jsonString = null;
	
	@Autowired
	ExpediaHotelService expediaHotelService;
	
	/*
	 * Request json
	 * {"hotelCode":"239894"}
	 */
	@RequestMapping(value = "/searchHotelInfo",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelInfo(@RequestBody HotelInformationRequestBean reqBean)
	{
		try {
				
				jsonString =expediaHotelService.searchHotelInfo(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;
	}
	/*{
		"customerSessionId": "0ABAAC0D-307F-F5C9-9152-A89C278E197D",
		"hotelID": "130719",
		"checkInDate": "25/03/2017",
		"checkOutDate": "26/03/2017",
		"roomList": [{
			"adults": "1",
			"children": "0",
			"childrenAge": []
		}]
	}*/
	@RequestMapping(value = "/searchHotelRoomAvail",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchAvailAndPricing(@RequestBody HotelAvailAndPricingReqBean reqBean)
	{
		try {
				jsonString=	expediaHotelService.searchAvailAndPricing(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	/*{
		"customerSessionId": "0ABAAC0D-307F-F5C9-9152-A89C278E197D",
		"hotelID": "129095",
		"checkInDate": "25/03/2017",
		"checkOutDate": "26/03/2017",
        "rateCode":"200938226",
        "roomTypeCode":"200165719",
		"roomList": [{
			"adults": "1",
			"children": "0",
			"childrenAge": []
		}]
	}*/
	@RequestMapping(value = "/searchHotelRoomAvailbility",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelRoomAvailbility(@RequestBody HotelRoomAvailRequestBean reqBean)
	{
			try {
				jsonString = expediaHotelService.searchHotelRoomAvailbility(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
		}
		return jsonString;	
	}
	/*
		{
			"bookingReference": "H3348sad89",
			"duration": "1",
			"hotelCode": "129095",
			"countryCode": "UK",
			"checkInDate": "03/25/2017",
			"checkOutDate": "03/26/2017",
			"rateKey": "1c5e546d-fc50-4eeb-8592-208664c9fdd6-5001",
			"roomCodeNos": "200165719",
			"rateCode": "200938226",
			"currencyCode": "USD",
			"serviceProviderName": "Expedia",
			"serviceProviderCode": "1",
			"isPack": false,
			"totalAmount": "113.34",
			"roomList": [{
				"adults": "1",
				"children": "0",
				"occupancyType": null,
				"occupancyCode": null,
				"noCots": 0,
				"roomCount": 1,
				"resultCount": 0,
				"childrenAge": [],
				"roomIndex": null,
				"roomTypeName": null,
				"roomTypeCode": null,
				"ratePlanCode": null,
				"roomFare": null,
				"currency": null,
				"agentMarkup": null,
				"roomTax": null,
				"totalFare": null
			}],
			"requestedRemarkMap": {
				"RC": "Please provide inter-connecting rooms",
				"LO": "If possible please provide room on lowest floor available",
				"QUI": "If possible please provide quiet room",
				"LA": "Please note late arrival (after 7 pm)",
				"HM": "Please note passengers are honeymooners",
				"LD": "Please note late check out",
				"EA": "Please note early arrival",
				"BT1": "If possible please provide room with bathtub",
				"RA": "If possible, please provide adjoining rooms",
				"SRM": "If possible please provide smoking room"
			},
			"shippingDetailsMap": {
				"adultLastName": ["TestOneL"],
				"childLastName": [],
				"adultFirstName": ["TestOneF"],
				"childFirstName": [],
				"childAge": [],
				"title": ["Mr"]
			},
			"adultFirstNames": ["TestOneF"],
			"adultLastNames": ["TestOneL"],
			"titles": ["Mr"]
		}
	 */

	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBooking(@RequestBody HotelBookRequestBean reqBean)  
	{	
			try {
				jsonString = expediaHotelService.hotelBooking(reqBean);
			} 
			catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;
	}
	
	/*{
		"itenaryNumber": "281432331",
		"exConfirmRoom": "1234"
	}*/
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean reqBean)	
	{
			try 
			{
				jsonString = expediaHotelService.searchHotelCancellation(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;
	}
	/*{
		"hotelIdList": "576491",
		"travelStartDate": "2017-03-25",
		"travelEndDate": "2017-03-26",
		"fenced": false
	}*/
	@RequestMapping(value = "/hotelDealsDetails",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelDealsDetails(@RequestBody HotelDealsRequestBean reqBean)	
	{
			try 
			{
				jsonString = expediaHotelService.searchHotelDealsDetails(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;
	}
	
// 	
	/*
	{
	  "hotelID": "239894",
	    "checkInDate": "1/12/2017",
	    "checkOutDate": "1/13/2017",
	      "roomList":[ {
	        "adults": "2",
	        "children": "1",
	        "childrenAge": ["3"]
	      } ]
	}
	 */
	/*@RequestMapping(value = "/searchHotelRoomAvail",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelRoomAvail(@RequestBody HotelRoomAvailRequestBean reqBean)
	{
		logStr = logStr+" searchHotelRoomAvail() ";
	    serviceRequestBean = new ServiceRequestBean();
		serviceRequestBean.setProductType(ProductServiceEnum.Hotel.toString());
		serviceRequestBean.setServiceName(ProductEnum.Hotel.roomavail.toString());
		serviceRequestBean.setServiceConfigModel(reqBean.getServiceConfigModel());
		serviceRequestBean.setRequestBean(reqBean);
		serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		serviceResponseBean = serviceResolverFactory.getServiceResponse();//USING TTESB APIS
		HotelRoomAvailResponseBean hotelSearchResponseBean = (HotelRoomAvailResponseBean) serviceResponseBean.getResponseBean();
		if(null!=hotelSearchResponseBean)
		{
			ObjectMapper mapper = new ObjectMapper();
			try {
				jsonString = mapper.writeValueAsString(hotelSearchResponseBean);
				CallLog.info(1,logStr+" Response Json::: "+jsonString);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		}
		return jsonString;	
	}
*/
	
	

}
