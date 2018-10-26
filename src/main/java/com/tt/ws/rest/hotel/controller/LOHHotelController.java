package com.tt.ws.rest.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ws.rest.hotel.services.LOHHotelService;
import com.ws.services.hotel.bean.HotelBookRequestBean;
import com.ws.services.hotel.bean.HotelBookingRulesReqBean;
import com.ws.services.hotel.bean.HotelCancelRequestBean;
import com.ws.services.hotel.bean.HotelCityRequestBean;
import com.ws.services.hotel.bean.HotelCountryRequestBean;
import com.ws.services.hotel.bean.HotelRoomAvailRequestBean;
import com.ws.services.util.CallLog;

@RestController
@RequestMapping("/loh")
public class LOHHotelController {
	
	String jsonString = null;
	@Autowired
	private LOHHotelService lohHotelService;
	

	/*{
	"hotelID": "JP147456",	                 
	"ratePlanCode": "YinOof79rgavkaGW9ez9Ve5LdZYh/qPo+6GKVgkwx089t5ggQCf7XcBptRxi6rc54eYy/NJtaurni+3vo/pB4L3W4v+TgAsq9+iVywl/ZhaNu3Xb56AIvK6WnJRlQQBjOb+ytQzSpMAnxVzBVjjph7nqiifVpGOzO/ebPP6oiEPHsSYecfxnB8Bq4hmmiwvxgvmmTWr9J2TxIMxxiPiBh2X7kQOIJ7gmxQXfvu4ps4wVTMC2e9yiO0x9jNOsHVpighXVQgmosrnamCRVYHoglQ==",
	"checkInDate": "20/04/2017",
	"checkOutDate": "25/04/2017"
    }*/
	
	@RequestMapping(value = "/searchHotelRoomAvail",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String searchHotelRoomAvail(@RequestBody HotelRoomAvailRequestBean hotelRoomRequestBean)
	{
			try {
				jsonString = lohHotelService.searchHotelRoomAvail(hotelRoomRequestBean);
			} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
	
	/*{
	"hotelCode": "JP147456",
	"ratePlanCode": "YinOof79rgavkaGW9ez9Ve5LdZYh/qPo+6GKVgkwx089t5ggQCf7XcBptRxi6rc54eYy/NJtaurni+3vo/pB4L3W4v+TgAsq9+iVywl/ZharOPIqhMWmHKaLT7JywL4g1ZfsdjSC93lpSsP/zj9zvalP9WQXEId0yZuVRJq9hBjQ3+otp77hFMCn6nUd2ZhRKS69ZDj0q4uQuBpDEZb18ClIDwtSS+0OhYokTC2MMA0i1hyCbN+/as6XoVHBePZFgTNIb87wlRyZ05vktpIjm+coQZickN5j1ScWSTL3+sc=",
	"checkInDate": "20/04/2017",
	"checkOutDate": "25/04/2017"
    }*/
	
	@RequestMapping(value = "/hotelBookingRules",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelBookRules(@RequestBody HotelBookingRulesReqBean requestBean)
	{
			try {
				jsonString = lohHotelService.hotelBookRules(requestBean);
				} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
	
	/*{
		"checkInDate": "20/04/2017",
		"checkOutDate": "25/04/2017",
		"paxes": [{
		    "paxId":"1",
			"paxName": "Test Three",
			"surname": "Mr",
			"age": "31",
			"gender":"M",
			"email": "test@test.com",
			"phoneNo": "111111111",
			"document": "22222222E",
			"documentType": "DNI",
			"address": "Address Holder 67",
			"city": "Palma de Mallorca",
			"country": "Spain",
			"postalCode": "5555",
			"nationality": "ES"
		}],
		"externalBookingRef": "TH122345",
		"agentName": "Test",
		"agentEmail": "test@test.com",
		"commentType": "RES",
		"comment": "Reservation comment",
		"bookingCode": "YinOof79rgavkaGW9ez9Ve5LdZYh/qPo+6GKVgkwx089t5ggQCf7XcBptRxi6rc54eYy/NJtaurni+3vo/pB4L3W4v+TgAsq9+iVywl/ZharOPIqhMWmHKaLT7JywL4g1ZfsdjSC93lpSsP/zj9zvalP9WQXEId0yZuVRJq9hBg1f2AJ71VZ1p+MQbJ7yozKMeCT41tB9wt85OU6h5iUu9eb6wad95Zn5mbdgx0Y4N2DFDFIwKa2gFe+WN6+3ZTA9YifnFmkVh32bhkpOzuQ07KrY4Fb7iOlpo+0WIIsa+eVceEq6U/7c9Vpp7yJnEPD",
		"externalItemRef": "",
		"relPaxId": ["1"],
		"elementCommentType": "ELE",
		"elementComment": "ELEMENT REMARKS",
		"minimumPrice": "301.58",
		"maximumPrice": "333.32",
		"currencyCode": "USD",
		"hotelCode": "JP147456"
	}*/
	
	@RequestMapping(value = "/hotelBooking",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelBooking(@RequestBody HotelBookRequestBean requestBean)
	{
			try {
				jsonString = lohHotelService.hotelBooking(requestBean);
				} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
	/*{
		"reservationLocator" : "44L3QR"
	      }*/
	@RequestMapping(value = "/hotelCancel",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelCancel(@RequestBody HotelCancelRequestBean requestBean)
	{
			try {
				jsonString =lohHotelService.hotelCancel(requestBean);
				} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
	
	/*{
		"productType" : "HOT"		
	}*/
	@RequestMapping(value = "/hotelCountryList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelCountryList(@RequestBody HotelCountryRequestBean countryRequestBean)
	{
			try {
				jsonString = lohHotelService.hotelCountryList(countryRequestBean);
				} catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
	
	@RequestMapping(value = "/hotelCityList",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public String hotelCityList(@RequestBody HotelCityRequestBean cityRequestBean)
	{
			try {
				jsonString = lohHotelService.hotelCityList(cityRequestBean);
				} 
			catch (Exception e) {
				CallLog.printStackTrace(1,e);
			}
		return jsonString;	
	}
	
}
