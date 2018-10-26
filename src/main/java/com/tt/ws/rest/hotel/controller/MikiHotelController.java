package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.MikiHotelService;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelCancelCostReqBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.util.CallLog;


@RestController
@RequestMapping("/miki")
public class MikiHotelController 
{
	
	String jsonString = null;
	
	@Autowired
	MikiHotelService mikiHotelService;
	

	/*{
	"checkInDate": "2017-06-18",
	"requestId": "4453",
	"paxNationality": "GB",
	"mustBeUnique": "false",
	"clientReferenceNumber": "SATHTL553111",
	"itemNumber": "1",
	"productCode": "CAF536500",
	"firstName": "TestF",
	"lastName": "TestL",
	"numberOfNights": "1",	
	"roomList": [{
		"roomTypeCode": "01001",
		"roomNo": "1",
		"rateIdentifier": "bff44949-3601-4a77-8235-342289eea857|MbHRrOtYlDtp6lqSns9dzK2WJ2An7pZtt_xd2ATU2dHpeSS27AgKfbf97iRouCpbb10uRGl782YlVV8ls3GUpqlsk6nkVmTBaKePsK4aFnfnrGUsFG2DjeGjT7nKg8y9REwm_i1gmcQXOTUV1CL2Rel51GPCZBm3osXrk3Hzqf5O18QYvG3ZgPajwCLo-kloTIa1O03iZQAX-VhbwPjys3o76yK6Ri9wXOWond4ZC8LV4s28U2DDShilr0SGKGve1rT6Gcd-D5VOe8l9MD980hzaXOU-ejz5",
		"totalFare": "73.41",
		"guestDetailsList": [{
			"guestType": "ADT",
			"guestFirstName": "TestF",
			"guestLastName": "TestL"
							},{
			"guestType": "ADT",
			"guestFirstName": "TestFFF",
			"guestLastName": "TestLLL"
							}]
							
				}]

}*/	
	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBooking(@RequestBody HotelBookRequestBean reqBean)  
	{		
			try {
				jsonString =mikiHotelService.hotelBooking(reqBean);
			} 
			catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}

		return jsonString;
	}
	

	/*{
		  "requestID" : "44566",
		  "requestDateTime" : "2017-04-20T15:55:34",
		  "bookingReference" : "3361362"	  
		}*/
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean reqBean)	
	{
			try 
			{
				jsonString = mikiHotelService.searchHotelCancellation(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;
	}
	


		/*{
		  "requestId" : "85434",
		  "requestDateTime" : "2017-04-05T15:55:34",
		  "bookingReference" : "3339408"	  
		}*/
		@RequestMapping(value = "/hotelCostCancellation",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
		public String searchHotelCostCancellation(@RequestBody HotelCancelCostReqBean reqBean)	
		{
				try 
				{
					jsonString = mikiHotelService.searchHotelCostCancellation(reqBean);
				} catch (Exception e) {
					CallLog.printStackTrace(1,e);
				}
			return jsonString;
		}
		
		
		
				/*{
				  "requestId" : "85434",
				  "requestDateTime" : "2017-04-05T15:55:34",
				  "bookingId":"3339408"
				}*/
				@RequestMapping(value = "/hotelBookingDetails",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
				public String searchHotelBookingDetails(@RequestBody HotelBookingDetailsReqBean reqBean)	
				{
						try 
						{
							jsonString = mikiHotelService.searchHotelBookingDetails(reqBean);
						} catch (Exception e) {
							CallLog.printStackTrace(1,e);
						}
					return jsonString;
				}

}
