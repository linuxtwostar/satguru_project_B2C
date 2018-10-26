package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.GTAHotelService;
import com.ws.services.hotel.bean.HotelAmendRequestBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelChargeConditionRequestBean;
import com.ws.services.hotel.bean.HotelEmergencyNumberRequestBean;
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelPriceBreakDownRequestBean;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/gta")
public class GTAHotelController 
{
	String jsonString = null;
	
	@Autowired
	GTAHotelService gtaHotelService;
	
	/*
	 * {
   "countryCode": "IN",
   "countryName": "India",
   "cityCode": "DEL",
   "cityName": "Delhi",
   "destType": "C",
   "checkInDate": "2017-01-20",
   "checkOutDate": "2017-01-21",
   "guestNationality": null,
   "starRatings": null,
   "noOfRooms": 2,
   "totalPax": 2,
   "roomList": [   {
      "adults": "2T",
      "children": "1",
      "occupancyType": null,
      "occupancyCode": null,
      "noCots": 0,
      "roomCount": 2,
      "resultCount": 0,
      "childrenAge":       [
         10
      ]
   }],
   "duration": 1,
   "hotelCode": null,
   "hotelStar": 0,
   "sortBy": null,
   "serviceType": null,
   "sortingType": null,
   "requestForMail": null,
   "priceOrder": null,
   "startOrder": null,
   "nameOrder": null,
   "packageName": null,
   "searchType": null,
   "locationCode": null,
   "currencyCode": "INR",
   "languageCode": "en",
   "rooms": null,
   "specialService": false,
   "dom": false,
   "pack": false,
   "nearBySearchAllowed": false
}
	 */
	@RequestMapping(value = "/searchHotel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotel(@RequestBody HotelSearchRequestBean hotelSearchRequestBean)
	{
			String jsonString = null;
				try {
			    	 jsonString = gtaHotelService.searchHotel(hotelSearchRequestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			return jsonString;
	}
	
	/*
	 * Request json
	  {
	 "hotelCode":"CHI",
	   "cityCode": "DEL"
	  }
	 */
	@RequestMapping(value = "/searchHotelInfo",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelInfo(@RequestBody HotelInformationRequestBean requestBean)
	{
		String jsonString = null;		
			try {
		    	jsonString = gtaHotelService.searchHotelInfo(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
	return jsonString;
	}

	/*
	 * Request json
	 {
   "checkInDate": "20-01-2017",
   "duration": "1",
   "cityCode": "DEL",
   "hotelCode": "CHI",
   "gtaDynamicProperty": "001:CHI:20080:S19681:28826:109941",
   "roomCodes": ["TB_C"]
}
	 */
	@RequestMapping(value = "/pricebreakdown",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String pricebreakdown(@RequestBody HotelPriceBreakDownRequestBean requestBean)
	{
		String jsonString  = null;		
			try {
		    	jsonString = gtaHotelService.pricebreakdown(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
	return jsonString;
	}
	
	/*
	 * Request json
	{
   "cityCode": "DEL",
   "hotelCode": "CHI",
   "checkInDate": "2017-01-20",
   "checkOutDate": "2017-01-21",
   "roomCodesMap": {"TB_C": 1},
   "gtaDynamicProperty": "001:CHI:20080:S19681:28826:109941",
   "noOfRooms": 0
}
	 */
	@RequestMapping(value = "/amendhotel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String amendhotel(@RequestBody HotelAmendRequestBean requestBean)
	{		
		String jsonString  = null;		
			try {
		    	jsonString = gtaHotelService.amendhotel(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
	return jsonString;
	}
	
	/*
	 * Request json
	  {
	 "countryCode":"IN",
	   "nationality": "IN"
	  }
	 */
	@RequestMapping(value = "/emergencynumber",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String emergencynumber(@RequestBody HotelEmergencyNumberRequestBean requestBean)
	{
		String jsonString  = null;		
			try {
		    	jsonString = gtaHotelService.emergencynumber(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
	return jsonString;
	}
	/*
	 * Request json
	 {
   "checkInDate": "2017-01-20",
   "checkOutDate": "2017-01-21",
   "cityCode": "DEL",
   "hotelCode": "CHI",
   "numberOfRooms": 1,
   "gtaDynamicProperty": "001:CHI:20080:S19681:28826:109941",
   "roomCodes": ["TB_C"]
}
	 */
	@RequestMapping(value = "/chargeCondition",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String chargeCondition(@RequestBody HotelChargeConditionRequestBean requestBean)
	{
		String jsonString  = null;
			try {
		    	jsonString = gtaHotelService.chargeCondition(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
	return jsonString;
	}
/*
 * 
 {
   "bookingReference": "HTL556657",
   "duration": "1",
   "hotelCode": "CHI",
   "cityCode": "DEL",
   "checkInDate": "2017-01-20",
   "checkOutDate": "2017-01-21",
   "gtaDynamicProperty": "001:CHI:20080:S19681:28826:109941",
   "currencyCode": "AED",
   "serviceProviderName": "GTA",
   "serviceProviderCode": "1",
   "roomList": [   {
      "adults": "2T",
      "children": "1",
      "occupancyType": null,
      "occupancyCode": null,
      "noCots": 0,
      "roomCount": 1,
      "resultCount": 0,
      "childrenAge": [10]
   }],
   "requestedRemarkMap":    {
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
   "shippingDetailsMap":    {
      "adultLastName":       [
         "Kumar",
         "Singh"
      ],
      "childLastName": ["Kumar"],
      "adultFirstName":       [
         "Dinesh",
         "Rohit"
      ],
      "childFirstName": ["Amit"],
      "childAge": ["10"],
      "title":       [
         "Mr",
         "Mr"
      ]
   }
}
 */
	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBooking(@RequestBody HotelBookRequestBean requestBean)  
	{	
			try {
				jsonString = gtaHotelService.hotelBooking(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
		return jsonString;
	}

	/*
	 * {
   "hotelReferenceID": "HTL556657"
   }
	 */
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean requestBean)
	{
		String jsonString = null;
				try {
			    	jsonString = gtaHotelService.searchHotelCancellation(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
}
