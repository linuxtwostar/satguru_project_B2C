package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.TravcoHotelService;
import com.ws.services.hotel.bean.HotelAllocationEnquiryRequestBean;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelCancelConfirmRequestBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCancellationDetailsRequestBean;
import com.ws.services.hotel.bean.HotelCityRequestBean;
import com.ws.services.hotel.bean.HotelInformationRequestBean;
import com.ws.services.hotel.bean.HotelSearchRequestBean;
import com.ws.services.hotel.bean.HotelStarRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/travco")
public class TravcoHotelController
{
  @Autowired
	TravcoHotelService travcoHotelService;
	/*
	 * {
   "countryCode": "UK",
   "countryName": "United Kingdom",
   "cityCode": "LON",
   "cityName": "London",
   "destType": "C",
   "checkInDate": "21/Jan/2017",
   "checkOutDate": "22/Jan/2017",
   "guestNationality": null,
   "starRatings": null,
   "noOfRooms": 6,
   "totalPax": 6,
   "roomList": [   {
      "adults": "2",
      "children": "1",
      "occupancyType": null,
      "occupancyCode": null,
      "noCots": 0,
      "roomCount": 2,
      "resultCount": 0,
      "childrenAge":       [
         4
      ]
   }],
   "duration": 10,
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
			    	 jsonString = travcoHotelService.searchHotel(hotelSearchRequestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			return jsonString;
	}
	/*
	 * Request json
	 * {"hotelCode":"YYL"}
	 */
	@RequestMapping(value = "/searchHotelInfo",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelInfo(@RequestBody HotelInformationRequestBean requestBean)
	{
		String jsonString  = null;		
			try {
		    	jsonString = travcoHotelService.searchHotelInfo(requestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(103,e);
			}
	return jsonString;
}
	/*
	 * {
"checkindate":"2017-02-25",
"duration":"1",
"hotelCode":"YYG3",
"hotelStar":"5",
"serviceType":"National",
"roomRequestDetails": [   {
      "adults": "1",
      "children": "0",
      "occupancyType": null,
      "occupancyCode": null,
      "noCots": 0,
      "roomCount": 1,
      "resultCount": 0,
      "childrenAge": []
   }]
}
	 */
	@RequestMapping(value = "/searchHotelAlloc",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelAllocation(@RequestBody HotelAllocationEnquiryRequestBean requestBean)
	{
		String jsonString = null;
				try {
			    	jsonString = travcoHotelService.searchHotelAllocation(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
		
	/*
	 * Request json
	 * {"cityCode":"BKK","starCode":"5"}
	 */
	@RequestMapping(value = "/hotelSingleCity", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelSingleCity(@RequestBody HotelCityRequestBean requestBean)  
	{		
			String jsonString="";
				try {
			    	 jsonString = travcoHotelService.hotelSingleCity(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			
		return jsonString;
	}
	

	@RequestMapping(value = "/hotelStarRating",produces=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelStarRating(HotelStarRequestBean requestBean)  
	{	
			String jsonString="";
			
				try {
			    	 jsonString = travcoHotelService.hotelStarRating(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			
		return jsonString;
	}
	/*
	 * {
"cchargesCode":"015307",
"hotelCode":"YYG3",
"checkInDate":"30/Dec/2016",
"duration":"1"
}
	 */
	@RequestMapping(value = "/hotelCancelDetails", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelCancelDetails(@RequestBody HotelCancellationDetailsRequestBean requestBean)  
	{		
			String jsonString="";
				try {
			    	 jsonString = travcoHotelService.hotelCancelDetails(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
			
		return jsonString;
	}
	/*
	 *{
   "hotelCode": "YYG3",
   "uniqueId": 463443112,
   "serviceProviderName": null,
   "duration": "10",
   "checkInDate": "30-Dec-2016",
   "shippingDetailMap":    {
      "adultLastName": "Kumar",
      "adultFirstName": "Dinesh",
      "hotelRequest": "Smoking Room (Request Only), Early Check-in (Request Only), Late Check-out (Request Only)",
      "comment": "",
      "title": "Mr"
   },
   "hotelRoomDetails":    [
            {
         "ROOM_CODE": "SWO",
         "SP_REF_NO": "LONDON",
         "ADULTS": "1",
         "OUR_REF_NO": "TESTREF15",
         "CHILDREN": "0"
      },
            {
         "ROOM_CODE": "SWO",
         "SP_REF_NO": "LONDON",
         "ADULTS": "1",
         "OUR_REF_NO": "TESTREF16",
         "CHILDREN": "0"
      }
   ]
}
	 */
	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public  String hotelBooking(@RequestBody HotelBookRequestBean requestBean)  
	{		
			String jsonString="";			
				try {
			    	 jsonString = travcoHotelService.hotelBooking(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	/*
	 * {
   "hotelReferenceID": "H334889",
   "hotelRoomDetails": [   {
      "ROOM_CODE": "SWO",
      "SP_REF_NO": "TG00363/01",
      "ADULTS": "1",
      "OUR_REF_NO": "R1150968",
      "CHILDREN": "0"
   }]
}
	 */
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelCancellation(@RequestBody HotelCancelRequestBean requestBean)
	{
		String jsonString = null;
				try {
			    	jsonString = travcoHotelService.searchHotelCancellation(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	//Hotel CancelConfirm Request JSON
	
	/*{
		   "hotelCode": "YYG3",
		   "uniqueId": 463443333112,
		   "duration": "10",
		   "transactionDate": "20-Jan-2017",
		   	"checkInDate": "20-Feb-2017",
		   "shippingDetailMap":    {
		      "adultLastName": "Kumar",
		      "adultFirstName": "Dinesh",
		      "hotelRequest": "Smoking Room (Request Only), Early Check-in (Request Only), Late Check-out (Request Only)",
		      "comment": "",
		      "title": "Mr"
		   },
		   "hotelRoomDetails":    [
		            {
		         "ROOM_CODE": "SWO",
			"ADULTS": "1",
			"BOOKING_REF_NO": "TG00391/01",
			"PRICE_CODE": "LONDON",
			"OUR_REF_NO": "TESTREF24",
			"CHILDREN": "0"
		      }
		   ]
		}*/
	//End
	
	@RequestMapping(value = "/hotelCancelConfirm",method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_VALUE,consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getHotelCancelConfirm(@RequestBody HotelCancelConfirmRequestBean requestBean)
	{
		String jsonString = null;
				try {
			    	jsonString = travcoHotelService.getHotelCancelConfirm(requestBean);
				} catch (Exception e) {
					CallLog.printStackTrace(103,e);
				}
		return jsonString;
	}
	
}
