package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.TBOHotelService;
import com.ws.services.hotel.bean.HotelAmendRequestBean;
import com.ws.services.hotel.bean.HotelAvailAndPricingReqBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelCancelConfirmRequestBean;
import com.ws.services.hotel.bean.HotelCancellationDetailsRequestBean;
import com.ws.services.hotel.bean.HotelCityRequestBean;
import com.ws.services.hotel.bean.HotelCountryRequestBean;
import com.ws.services.hotel.bean.HotelDetailsRequestBean;
import com.ws.services.hotel.bean.HotelGenerateInvoiceReqBean;
import com.ws.services.hotel.bean.HotelRoomAvailRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/hotel/tbo")
public class TBOHotelController 
{
	String jsonString = null;
	
	@Autowired
	TBOHotelService tboHotelService;
	
	/*{
	"sessionId": "5646d42e-6811-4b24-90df-4519ec515182",
	"hotelcode": "1199260",
	"resultIndex": "1"
	}*/
	@RequestMapping(value = "/hotelDetails", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelDetails(@RequestBody HotelDetailsRequestBean requestBean)  
	{		
			try
			{
			jsonString = tboHotelService.hotelDetails(requestBean);
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		return jsonString;
	}
		
	/*{
	"resultIndex": "1",
	"hotelID": "1297786",
	"sessionId": "ac8a0ddf-61bc-4366-8b52-5e2c1e8c4cb0"
	}*/
	
	@RequestMapping(value = "/searchHotelRoomAvail",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelRoomAvail(@RequestBody HotelRoomAvailRequestBean hotelSearchRequestBean)
	{
		try {
			jsonString=	tboHotelService.searchHotelRoomAvail(hotelSearchRequestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	/*{
		"roomCombinationIndex": "1",
		"resultIndex": "1",
		"fixedFormat": "true",
		"sessionId": "5646d42e-6811-4b24-90df-4519ec515182"
	}*/
	@RequestMapping(value = "/hotelCancelDetails", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelCancelDetails(@RequestBody HotelCancellationDetailsRequestBean requestBean)  
	{		
			try
			{
			jsonString = tboHotelService.hotelCancelDetails(requestBean);
					
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		return jsonString;
	}
	
	/*{
	"roomCombinationIndex": "1",
	"resultIndex": "1",
	"fixedFormat": "true",
	"sessionId": "5646d42e-6811-4b24-90df-4519ec515182"
}*/
	@RequestMapping(value = "/hotelAvailAndPricing", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelAvailAndPricing(@RequestBody HotelAvailAndPricingReqBean requestBean)  
	{		
			try
			{
			jsonString = tboHotelService.hotelAvailAndPricing(requestBean);
				
			}
			catch (Exception e) 
			{
				CallLog.printStackTrace(1, e);
			}
		return jsonString;
	}
	/*{
		"guestNationality": "IN",
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
		"clientReferenceNumber": "2003141223423400#kuld",
		"roomList": [{
			"roomTypeCode": "0c4d7466-7eed-f55e-ba1e-75b4dd79fb9c|1|1|",
			"totalFare": "10.67",
			"agentMarkup": "0.00",
			"roomTypeName": "Adult",
			"currency": "USD",
			"ratePlanCode": "1|",
			"roomFare": "9.70",
			"roomIndex": "1",
			"roomTax": "0..97"
		}]
	}*/
	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBooking(@RequestBody HotelBookRequestBean requestBean)  
	{		
		try
		{			
		jsonString = tboHotelService.hotelBooking(requestBean);			   
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	//for Request Type : OfflineAmendment
	/*{
		"roomReqList": [{
			"amendSeqName": "FirstRoom",
			"guestList": [{
				"Action": "Add",
				"GuestType": "Adult",
				"FirstName": "Amar",
				"ExistingName": "",
				"Title": "Mr",
				"LastName": "Saxena",
				"Age": "24"
			}, {
				"Action": "Rename",
				"GuestType": "Adult",
				"FirstName": "Shyam",
				"ExistingName": "Mr Kuldap testhb",
				"Title": "Mr",
				"LastName": "Srivastav",
				"Age": "25"
			}, {
				"Action": "Delete",
				"GuestType": "Adult",
				"FirstName": "",
				"ExistingName": "Mr Kuld adulttwo",
				"Title": "Mr",
				"LastName": "",
				"Age": "0"
			}]
		}],
		"requestType": "OfflineAmendment",
		"checkOutDate": "2017-02-26",
		"checkInDate": "2017-02-25",
		"priceChangeType": "Approved",
		"remarks": "guest name amendment requested",
		"bookingId": "1729"
	}*/
	//for Request Type : CheckStatus
	
	/*{
		"requestType": "CheckStatus",
		"priceChangeType": "InformationRequired",
		"remarks": "checking status amendment request",
		"bookingId": "1729"
	}*/
	//for Request Type : PriceApproved
	
	/*{
		"requestType": "PriceApproved",
		"priceChangeType": "InformationRequired",
		"remarks": "price information required if there is any price change",
		"bookingId": "1729"
	}*/
	
	@RequestMapping(value = "/hotelBookingAmendment",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBookingAmendment(@RequestBody HotelAmendRequestBean requestBean)  
	{		
		try
		{
		jsonString = tboHotelService.hotelBookingAmendment(requestBean);
			    
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{
		"bookingId":"1728"
	}
	OR
	{
		"confirmationNo":"LL8F233737"
	}
	OR
	{
		"clientReferenceNumber":"200314125855789#kuld"
	}
	*
	*/
	@RequestMapping(value = "/hotelBookingDetails",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBookingDetails(@RequestBody HotelBookingDetailsReqBean requestBean)  
	{		
		try
		{
		jsonString = tboHotelService.hotelBookingDetails(requestBean);
			
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{
		"payModeType": "Limit",
		"isVoucherBooking": "true",
		"bookingId": "1728"
	}*/
	@RequestMapping(value = "/hotelGenerateInvc",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelGenerateInvc(@RequestBody HotelGenerateInvoiceReqBean requestBean)  
	{		
		try{
			
		 jsonString = tboHotelService.hotelGenerateInvc(requestBean);
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{
		"hotelBookRefId": "46832",
		"requestType": "HotelCancel",
		"remarks": "test cancel"
	}*/
	@RequestMapping(value = "/hotelCancelConfirm",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelCancelConfirm(@RequestBody HotelCancelConfirmRequestBean requestBean)  
	{		
		try
		{
			jsonString = tboHotelService.hotelCancelConfirm(requestBean);
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{
	"hotelBookRefId": "46832",
	"requestType": "CheckStatus",
	"remarks": "test cancel"
	}*/
	@RequestMapping(value = "/hotelCheckStatus",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelCheckStatus(@RequestBody HotelCancelConfirmRequestBean requestBean)  
	{		
		try
		{
		 jsonString = tboHotelService.hotelCheckStatus(requestBean);
			   
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	//Nothing to send in request.It will return all country list.
	@RequestMapping(value = "/hotelCountryList",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelCountryList(@RequestBody HotelCountryRequestBean requestBean)  
	{		
		try
		{
			jsonString = tboHotelService.hotelCountryList(requestBean);
			
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	/*{
		"countryCode": "IN"
	}*/
	@RequestMapping(value = "/hotelCityList",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelCityList(@RequestBody HotelCityRequestBean requestBean)  
	{		
		try
		{
		  jsonString = tboHotelService.hotelCityList(requestBean);
			 
		}
		catch (Exception e) 
		{
			CallLog.printStackTrace(1, e);
		}
		return jsonString;
	}
	
}