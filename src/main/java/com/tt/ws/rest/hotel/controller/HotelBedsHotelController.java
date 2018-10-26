package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.HotelBedsHotelService;
import com.ws.services.hotel.bean.HotelAvailAndPricingReqBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/hotelBeds")
public class HotelBedsHotelController {
	
	String jsonString = null;
	@Autowired
	HotelBedsHotelService hbdHotelService;

	/*{
		"upselling": "true",
		"language": "ENG",
		"rateKey": "20170420|20170425|W|164|316236|DBL.DX|CG-BAR|RO||1~2~1|8|N@35F3798707BF4260872622FBA87B8518",
   }*/
   @RequestMapping(value = "/searchHotelAvailAndPrice",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
   public String searchAvailAndPricing(@RequestBody HotelAvailAndPricingReqBean reqBean)
   {
	   try {
				jsonString = hbdHotelService.searchAvailAndPricing(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
   }
	
	
	
	/*{
	"guestNationality": "IN",
	"holderName": "aabb",
	"holderSurname": "ffg",
	"paymentType": "AT_WEB",
	"rateKey": "20170420|20170425|W|164|316236|DBL.DX|CG-BAR|RO||1~2~1|8|N@35F3798707BF4260872622FBA87B8518",
	"hotelAddressLine": {
		"zipcode": "110096",
		"country": "India",
		"areaCode": "11",
		"city": "Delhi",
		"countrycode": "91",
		"addressLine1": "testadd1",
		"addressLine2": "testadd2",
		"telephone": "25869696",
		"state": "Delhi",
		"email": "test@test.com"
	},
	"guestDetailsLis": [{
		"isLeadGuest": "true",
		"guestAge": "26",
		"guestLastName": "Kumar",
		"guestFirstName": "Dinesh",
		"guestTitle": "Mr",
		"guestType": "Adult",
		"guestInRoom": "1"
	}],
	"paymentInfoMap": {
		"PaymentModeType": "Limit",
		"VoucherBooking": "true"
	},
	"resultIndex": "1",
	"sessionId": "646d42e-6811-4b24-90df-4519ec515182",
	"noOfRooms": "1",
	"hotelCode": "1199260",
	"hotelName": "Swagat Palace",
	"clientReferenceNumber": "SATHTL129",
	"roomList": [{
		"roomTypeCode": "0c4d7466-7eed-f55e-ba1e-75b4dd79fb9c|1|1|",
		"totalFare": "10.67",
		"agentMarkup": "0.00",
		"roomTypeName": "Adult",
		"currency": "USD",
		"ratePlanCode": "1|",
		"roomFare": "9.70",
		"roomIndex": "1",
		"roomTax": "0.97",
		"roomNo": "1",
		"type": "AD",
		"age": "30",
		"name": "cdgg",
		"surname": "xxyyzz"

	}, {
		"roomTypeCode": "0c4d7466-7eed-f55e-ba1e-75b4dd79fb9c|1|1|",
		"totalFare": "10.67",
		"agentMarkup": "0.00",
		"roomTypeName": "Adult",
		"currency": "USD",
		"ratePlanCode": "1|",
		"roomFare": "9.70",
		"roomIndex": "1",
		"roomTax": "0.97",
		"roomNo": "1",
		"type": "AD",
		"age": "45",
		"name": "aaa",
		"surname": "xxyyzz"

	}, {
		"roomTypeCode": "0c4d7466-7eed-f55e-ba1e-75b4dd79fb9c|1|1|",
		"totalFare": "10.67",
		"agentMarkup": "0.00",
		"roomTypeName": "Adult",
		"currency": "USD",
		"ratePlanCode": "1|",
		"roomFare": "9.70",
		"roomIndex": "1",
		"roomTax": "0.97",
		"roomNo": "1",
		"type": "CH",
		"age": "8",
		"name": "bbbb",
		"surname": "xxyyzz"

	}]
}*/
	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBooking(@RequestBody HotelBookRequestBean requestBean)  
	{		
		try
		{   jsonString = hbdHotelService.hotelBooking(requestBean);
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{

		"bookingId": "164-3179693"
	}*/
	@RequestMapping(value = "/hotelBookingDetails",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBookingDetails(@RequestBody HotelBookingDetailsReqBean requestBean)  
	{		
		try
		{
			    jsonString = hbdHotelService.hotelBookingDetails(requestBean);
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{
	"itenaryNumber": "281432331",
	"exConfirmRoom": "1234",
	"bookingReference":"164-3179693"
	}*/
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean reqBean)	
	{
			try 
			{
				jsonString = hbdHotelService.searchHotelCancellation(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
	
		return jsonString;
	}
}
