package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.DOTWHotelService;
import com.ws.services.hotel.bean.HotelAvailAndPricingReqBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookingDetailsReqBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/dotw")
public class DOTWHotelController 
{
	String jsonString = null;

	@Autowired
	DOTWHotelService dotwHotelService;
	/*{
	"checkInDate": "20/04/2017",
	"checkOutDate": "25/04/2017",
	"currencyCode": "USD",
	"commission": "0",
	"roomList": [{
		"roomsNo": "1",
		"roomRunNo": "0",
		"adultCode": "1",
		"children": "0",
		"rateBasis": "-1",
		"passengerCountryOfResidence":"20",
		"passengerNationality": "20",
		"roomTypeCode": "14916258",
		"selectedRateBasis":"1331",		
		"allocationDetails": "73.41"
		}],
	"hotelID": "JP147456"
    }*/
	
	@RequestMapping(value = "/searchHotelAvailAndPricing",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelAvailAndPricing(@RequestBody HotelAvailAndPricingReqBean hotelAvailAndPricingReqBean)
	{
			try {
				jsonString = dotwHotelService.searchHotelAvailAndPricing(hotelAvailAndPricingReqBean);
				} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
	
		return jsonString;	
	}
	
	
	/*{
	"checkInDate": "20/04/2017",
	"checkOutDate": "25/04/2017",
	"currencyCode": "AED",
	"agentCommission": "0",
	"hotelCode": "JP147456",
	"bookingReference": "test@test.com",
	"roomList": [{
				"roomTypeCode": "01001",
				"selectedRateBasis":"0",		
				"allocationDetails": "QzozNzoiRnJhbWV3b3JrXExpYlxBbGxvY2F0aW9uXEhvdGVsRGV0YWlscyI6MTMwODp7YTo1OntzOjM6InNpZCI7aToxMDA2O3M6NDoic2JpZCI7TjtzOjM6InRpZCI7czoxNjoiMTQ5MTM5NjczODAwMDAwMSI7czoxOiJuIjtpOjE7czozOiJzY2EiO2E6NTp7czoyOiJ0cCI7czozMjoiMmZkYTUxYzVhZDdlM2MzM2U4OGIzMWIxMTcwOTZhODIiO3M6MzoicnBjIjtzOjEzOiIxNDgwNTA5MF8xMzMxIjtzOjY6ImNydWxlcyI7YToyOntpOjA7TzoxNjoiQ2FuY2VsbGF0aW9uUnVsZSI6MTY6e3M6ODoiZnJvbURhdGUiO047czo2OiJ0b0RhdGUiO3M6MTk6IjIwMTctMDQtMDQgMDQ6MDA6MDAiO3M6ODoidGltZXpvbmUiO2k6MzAwO3M6MTg6InJldHVybmVkQnlTdXBwbGllciI7YjowO3M6MjA6ImNhbmNlbGxhdGlvblJ1bGVUZXh0IjtOO3M6MTQ6ImNhbmNlbFBvc3NpYmxlIjtiOjE7czoxMzoiYW1lbmRQb3NzaWJsZSI7YjoxO3M6Njoibm9TaG93IjtiOjA7czoxNzoiYXJyQWRkaXRpb25hbEluZm8iO047czoxNjoib2JqUHJpY2VDdXN0b21lciI7TjtzOjE1OiJwcmljZVZhbHVlQW1lbmQiO2k6MDtzOjEwOiJwcmljZVZhbHVlIjtpOjA7czo5OiJmb3JtYXR0ZWQiO047czoxMzoicHJpY2VDdXJyZW5jeSI7czozOiIzNjYiO3M6MjE6InByaWNlSW5jbHVkZXNBbGxUYXhlcyI7YjoxO3M6MTk6Im1pbmltdW1TZWxsaW5nUHJpY2UiO2k6MDt9aToxO086MTY6IkNhbmNlbGxhdGlvblJ1bGUiOjE2OntzOjg6ImZyb21EYXRlIjtzOjE5OiIyMDE3LTA0LTA0IDA0OjAwOjAxIjtzOjY6InRvRGF0ZSI7TjtzOjg6InRpbWV6b25lIjtpOjMwMDtzOjE4OiJyZXR1cm5lZEJ5U3VwcGxpZXIiO2I6MDtzOjIwOiJjYW5jZWxsYXRpb25SdWxlVGV4dCI7TjtzOjE0OiJjYW5jZWxQb3NzaWJsZSI7YjoxO3M6MTM6ImFtZW5kUG9zc2libGUiO2I6MTtzOjY6Im5vU2hvdyI7YjowO3M6MTc6ImFyckFkZGl0aW9uYWxJbmZvIjtOO3M6MTY6Im9ialByaWNlQ3VzdG9tZXIiO047czoxNToicHJpY2VWYWx1ZUFtZW5kIjtkOjM4OC42Mjg1MDc2MzY2NTIxNTkzNjI3MTEyNDUxOTQwNzc0OTE3NjAyNTM5MDYyNTtzOjEwOiJwcmljZVZhbHVlIjtkOjM4OC42Mjg1MDc2MzY2NTIxNTkzNjI3MTEyNDUxOTQwNzc0OTE3NjAyNTM5MDYyNTtzOjk6ImZvcm1hdHRlZCI7TjtzOjEzOiJwcmljZUN1cnJlbmN5IjtzOjM6IjM2NiI7czoyMToicHJpY2VJbmNsdWRlc0FsbFRheGVzIjtiOjE7czoxOToibWluaW11bVNlbGxpbmdQcmljZSI7aTowO319czo4OiJjdXJyZW5jeSI7czozOiIzNjYiO3M6MjoiZHIiO2E6MTp7czoxMDoiMjAxNy0wNC0wNSI7ZDozODguNjI4NTA3NjM2NjUyMTU5MzYyNzExMjQ1MTk0MDc3NDkxNzYwMjUzOTA2MjU7fX19fQ==",
				"adultCode": "1",
				"adult": "1",
				"children": "1",
				"childrenAge": [2],
				"actualChildren": "1",
				"actualChildrenAge":[2],
				"noOfExtraBeds":"0",
				"passengerNationality":"",
				"passengerCountryOfResidence":"",
				"mealPlanDateTime": "",
				"extraMealBean":[{
				                "applicableFor":"",
				                "childAge":"2",
				                "isPassenger":"",
				                "mealsCount":"",
				                "passengerNumber":"",
								"mealBean":[{
											"code":"1331",
											"units": "",
											"mealPrice":""
											}]
								}],		
		        "guestDetails":[{
								"guestTitle":"",
								"guestFirstName": "",
								"guestLastName":""
								}],
				"specialRequestList":[""],
				"beddingPreference":"false"
			  }]
	
	}*/

	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelBooking(@RequestBody HotelBookRequestBean requestBean)
	{
			try {
				jsonString = dotwHotelService.hotelBooking(requestBean);
				} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
	/*{
	  "bookingType" : "1",
	  "bookingId":"126916143"  
	}*/
	@RequestMapping(value = "/hotelBookingDetails",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelBookingDetails(@RequestBody HotelBookingDetailsReqBean reqBean)	
	{
		try 
			{
				jsonString = dotwHotelService.searchHotelBookingDetails(reqBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;
	}
		
	/*{
	  "bookingType" : "1",
	  "bookingCode":"126916143",
	  "isConfirm" : "",
	  "refNo" : "HTL-AE2-126916143",
	  "penality":"0"
	  }*/
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelCancel(@RequestBody HotelCancelRequestBean requestBean)
	{
		try {
			jsonString = dotwHotelService.hotelCancel(requestBean);
			} catch (Exception e) {
			CallLog.printStackTrace(1,e);
		}
	return jsonString;	
	}

}
