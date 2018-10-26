package com.tt.ws.rest.air.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.tt.ts.blacklist.model.BlackListFlightCityModel;
import com.tt.ts.blacklist.model.BlackListFlightCountryPosModel;
import com.tt.ts.blacklist.model.BlackOutFlightCountryModel;
import com.tt.ts.blacklist.model.BlackOutFlightModel;
import com.tt.ts.flighttag.model.FlightTagModel;
import com.tt.ts.flighttag.model.TagFlightCityModel;
import com.tt.ts.flighttag.model.TagFlightCountryModel;
import com.tt.ts.rbd.model.RbdModel;
import com.ws.interaction.beans.ServiceRequestBean;
import com.ws.interaction.beans.ServiceResponseBean;
import com.ws.interaction.factory.ServiceResolverFactory;
import com.ws.services.flight.bean.flightsearch.FlightCalendarFareSearchResponseBean;
import com.ws.services.flight.bean.flightsearch.FlightCalendarOption;
import com.ws.services.flight.bean.flightsearch.FlightLegs;
import com.ws.services.flight.bean.flightsearch.FlightOption;
import com.ws.services.flight.bean.flightsearch.FlightSearchRequestBean;
import com.ws.services.flight.bean.flightsearch.FlightSearchResponseBean;
import com.ws.services.flight.bean.flightsearch.OptionSegmentBean;
import com.ws.services.flight.bean.flightsearch.RoundTripFlightOption;
import com.ws.services.flight.connector.util.AmadeusRequestUtil;
import com.ws.services.flight.connector.util.AmadeusResponseUtil;
import com.ws.services.flight.connector.util.ParamMappingUtil;
import com.ws.services.util.CallLog;
import com.ws.services.util.ProductEnum;

public final class AirHelperUtil
{
	public AirHelperUtil()
	{

	}

	public static FlightCalendarFareSearchResponseBean processToFlightSearchResponseDateFlexible(ServiceRequestBean serviceRequestBean, FlightSearchRequestBean requestBean)
	{
		serviceRequestBean.setServiceName(ProductEnum.Flight.calenderfare.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		FlightCalendarFareSearchResponseBean flightCalSearchResponseBean = null; 
		if(serviceResponseBean!=null && serviceResponseBean.getResponseBean() != null) {
			flightCalSearchResponseBean = (FlightCalendarFareSearchResponseBean) serviceResponseBean.getResponseBean();
		}	
		try
		{		
		//CallLog.info(101,">>>>>>>>>>flightCalSearchResponseBean::"+flightCalSearchResponseBean);
		
		
		//CallLog.info(101,">>>>>>>>>>flightCalenderMaflightCalenderOptionListtCalenderMap.size()>>>>"+flightCalenderOptionList);
		//CallLog.info(101,">>>>>>>>>>"+flightCalenderOptionList.get(0).getOnwardDate());
		Map<Date, FlightCalendarOption> flightCalenderMap = new TreeMap<>();
		DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy"); //23-10-2017
		//CallLog.info(101,">>>>>>>>>"+requestBean.getOnwardDate());
		
			if(flightCalSearchResponseBean!=null) {
				List<FlightCalendarOption> flightCalenderOptionList = flightCalSearchResponseBean.getCalFlightOptions();
				if ("oneway".equalsIgnoreCase(requestBean.getTripType()))
				{
					for (int i = 0; i < flightCalenderOptionList.size(); i++)
					{
						if (flightCalenderMap.size() == 0){
							if (flightCalenderOptionList.get(i).getTotalFare() != 0)
								flightCalenderMap.put(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate()), flightCalenderOptionList.get(i));
						}
						else
						{
							if (flightCalenderMap.containsKey(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate())))
							{
								if (flightCalenderOptionList.get(i).getTotalFare() != 0){
									if (flightCalenderOptionList.get(i).getTotalFare() < flightCalenderMap.get(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate())).getTotalFare())
									{
										flightCalenderMap.put(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate()), flightCalenderOptionList.get(i));
									}
									if(flightCalenderOptionList.get(i).getTotalFare() > flightCalenderMap.get(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate())).getTotalFare()){
										if (flightCalenderMap.get(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate())).getTotalFare()==0){
											flightCalenderMap.put(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate()),flightCalenderOptionList.get(i));
										}
									}
								}
							}
							else
							{
								flightCalenderMap.put(dateFormat1.parse(flightCalenderOptionList.get(i).getOnwardDate()), flightCalenderOptionList.get(i));
							}
						}
						//CallLog.info(101,">>>>>>>>>["+i+"]-----"+flightCalenderOptionList.get(i).getOnwardDate()+"------->"+flightCalenderOptionList.get(i).getServiceVendor());
						//CallLog.info(101,">>>>>>>>>["+i+"]-----"+flightCalenderMap);
					}
					//CallLog.info(101,">>>>>>>>>>flightCalenderMap----size->"+flightCalenderMap.size()+"----     requestBean.getOnwardDate()>>>"+requestBean.getOnwardDate());
					//CallLog.info(101,">>>>>>>>>>flightCalenderMap----->"+flightCalenderMap);
					List<Date> onwrdDates = getPlusMinusDates(requestBean.getOnwardDate());
					//CallLog.info(101,">>>>>>>>>>onwrdDates----->"+onwrdDates);
					for (int i = 0; i < onwrdDates.size(); i++)
					{
						if (flightCalenderMap.containsKey(onwrdDates.get(i)))
						{
							// CallLog.info(4,"Date match");
						}
						else
						{
							FlightCalendarOption flightCal = new FlightCalendarOption();
							// flightCal.set
							flightCal.setFareFound(false);
							flightCal.setOnwardDate(dateFormat1.format(onwrdDates.get(i)));
							flightCalenderMap.put(onwrdDates.get(i), flightCal);
						}
					}
					List<FlightCalendarOption> onwardCalFlightOptions = new ArrayList<FlightCalendarOption>();
					for (int i = 0; i < onwrdDates.size(); i++)
					{
						onwardCalFlightOptions.add(flightCalenderMap.get(onwrdDates.get(i)));
					}
					/*for (Map.Entry entry : flightCalenderMap.entrySet())
					{
						onwardCalFlightOptions.add((FlightCalendarOption) entry.getValue());
					}*/
					flightCalSearchResponseBean.setCalFlightOptions(null);
					flightCalSearchResponseBean.setCalFlightOptions(onwardCalFlightOptions);
				}
				if ("RoundTrip".equalsIgnoreCase(requestBean.getTripType()))
				{
					Map<String, FlightCalendarOption> flightRoundCalenderMap = new HashMap<String, FlightCalendarOption>();
					// CallLog.info(101,"flightCalenderOptionList.size()====>"+flightCalenderOptionList.size());
					for (int i = 0; i < flightCalenderOptionList.size(); i++)
					{
						CallLog.info(101, "" + flightCalenderOptionList.get(i).getOnwardDate() + " and " + flightCalenderOptionList.get(i).getReturnDate() + "-Service vendor->" + flightCalenderOptionList.get(i).getServiceVendor() + " --TotalFare->>" + flightCalenderOptionList.get(i).getTotalFare());
						if (flightRoundCalenderMap.size() == 0)
						{
							if (flightCalenderOptionList.get(i).getTotalFare() != 0)
								flightRoundCalenderMap.put(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate(), flightCalenderOptionList.get(i));
						}
						else
						{
							if (flightRoundCalenderMap.containsKey(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate()))
							{
								// CallLog.info(101,"after match==>List Total Fare:"+flightCalenderOptionList.get(i).getTotalFare()+"    Map total Fare:"+flightRoundCalenderMap.get(flightCalenderOptionList.get(i).getOnwardDate()+"_"+flightCalenderOptionList.get(i).getReturnDate()).getTotalFare());
								if (flightCalenderOptionList.get(i).getTotalFare() != 0)
									if (flightCalenderOptionList.get(i).getTotalFare() < flightRoundCalenderMap.get(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate()).getTotalFare())
									{
										// CallLog.info(101,"Interchange");
										flightRoundCalenderMap.put(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate(), flightCalenderOptionList.get(i));
										// CallLog.info(101,"New Fare on Map:"+flightRoundCalenderMap.get(flightCalenderOptionList.get(i).getOnwardDate()+"_"+flightCalenderOptionList.get(i).getReturnDate()).getTotalFare());
									}
								if (flightCalenderOptionList.get(i).getTotalFare() > flightRoundCalenderMap.get(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate()).getTotalFare())
								{
									if (flightRoundCalenderMap.get(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate()).getTotalFare() == 0)
									{
										flightRoundCalenderMap.put(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate(), flightCalenderOptionList.get(i));
									}
								}
							}
							else
							{
								flightRoundCalenderMap.put(flightCalenderOptionList.get(i).getOnwardDate() + "_" + flightCalenderOptionList.get(i).getReturnDate(), flightCalenderOptionList.get(i));
							}
						}
					}
					// CallLog.info(101,"Calender flightRoundCalenderMap size===>"+flightRoundCalenderMap.size());
					// CallLog.info(4,"Calender flightRoundCalenderMap "+flightRoundCalenderMap);
					List<FlightCalendarOption> roundTripCalFlightOptions = new ArrayList<FlightCalendarOption>();

					List<Date> onwDate = getPlusMinusDates(requestBean.getOnwardDate());
					List<Date> retDate = getPlusMinusDates(requestBean.getReturnDate()); // df.format(today);
					SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
					List<String> onwStringDate = new ArrayList<String>();
					// CallLog.info(101,"----------onwDate::"+onwDate.toString());
					// CallLog.info(101,"----------retDate::"+retDate.toString());
					for (int i = 0; i < retDate.size(); i++)
					{
						for (int j = 0; j < onwDate.size(); j++)
						{
							// CallLog.info(101,"----------"+sdf1.format(onwDate.get(j))+"_"+sdf1.format(retDate.get(i))+"------->"+flightRoundCalenderMap.get(sdf1.format(onwDate.get(j))+"_"+sdf1.format(retDate.get(i))));
							roundTripCalFlightOptions.add(flightRoundCalenderMap.get(sdf1.format(onwDate.get(j)) + "_" + sdf1.format(retDate.get(i))));
							flightRoundCalenderMap.remove(flightRoundCalenderMap.get(sdf1.format(onwDate.get(j)) + "_" + sdf1.format(retDate.get(i))));
						}
					}
					// CallLog.info(101,"----------Calender roundTripCalFlightOptions  ===>"+roundTripCalFlightOptions.size());
					flightCalSearchResponseBean.setCalFlightOptions(roundTripCalFlightOptions);
				}
			}
			
		}
		catch (Exception e)
		{
			CallLog.info(101, ">>>>>>>>>>>> Exception in  calender filter records = " + e.getMessage());
		}

		return flightCalSearchResponseBean;

	}

	public static FlightSearchResponseBean processToFlightSearchResponse(ServiceRequestBean serviceRequestBean, FlightSearchRequestBean requestBean)
	{
		serviceRequestBean.setServiceName(ProductEnum.Flight.search.toString());
		serviceRequestBean.setServiceConfigModel(requestBean.getServiceConfigModel());
		ServiceResolverFactory serviceResolverFactory = new ServiceResolverFactory(serviceRequestBean);
		ServiceResponseBean serviceResponseBean = serviceResolverFactory.getServiceResponse();
		FlightSearchResponseBean flightSearchResponseBean = null;
		if(serviceResponseBean!=null && serviceResponseBean.getResponseBean()!=null) {
			flightSearchResponseBean = (FlightSearchResponseBean) serviceResponseBean.getResponseBean();
			if (flightSearchResponseBean != null)
			{
				CallLog.info(0, "size onwardFilteredList after sorting ===>>> " + flightSearchResponseBean.getOnwardFlightOptions().size());
				CallLog.info(0, "size roundTripFlightOptionList after adding more options sorting ===>>> " + flightSearchResponseBean.getRoundTripFlightOptions().size());
			}
		}	
		return flightSearchResponseBean;
	}

	
	
	public void validateBlackListAndTagRBDChecks(FlightSearchResponseBean searchResponseBean, List<BlackOutFlightModel> blackList, List<FlightTagModel> tagFlightModelList, FlightSearchRequestBean requestBean, List<RbdModel> rbdList)
	{
		CallLog.info(102,"###### Break validateBlackListAndTagRBDChecks ######");
		try
		{
			boolean isBlackListDataFound = (blackList != null && blackList.size() > 0) ? true : false;
			
			Map<String, List<RbdModel>> rdbModelMap = null;
			List<RbdModel> airLineWiseRBDList = null;
			boolean isRBDDataFound = false;
			
				if(rbdList != null && rbdList.size() > 0){
					
					isRBDDataFound = true;
					
					/* Preparing a Map, to reduce the iteration for every time of rbdList */
					rdbModelMap = new HashMap<String, List<RbdModel>>();
					
					Long st1= System.currentTimeMillis();
					for(RbdModel eachRBDModel : rbdList){
						
						if(rdbModelMap.containsKey(eachRBDModel.getAirlineName())){
							rdbModelMap.get(eachRBDModel.getAirlineName()).add(eachRBDModel);
						}
						else{
							airLineWiseRBDList = new ArrayList<RbdModel>();
							airLineWiseRBDList.add(eachRBDModel);
							rdbModelMap.put(eachRBDModel.getAirlineName(), airLineWiseRBDList);
						}
					}
					Long end1= System.currentTimeMillis();
					CallLog.info(102,"Time Taken :::rdbModelMap prepare ::"+(end1-st1));
				}
				
				CallLog.info(0, "finally rdbModelMap = "+rdbModelMap);
				

			if (requestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.OneWay.toString()))
			{
				List<FlightOption> optionList = searchResponseBean.getOnwardFlightOptions();

				if (optionList != null && optionList.size() > 0)
				{
					List<FlightOption> filteredOptionList = new ArrayList<FlightOption>();
					boolean isFlightBlackListed = false;
					boolean isFlightIsTagged = false;
					List<String> blackListFlightNo = null;
					List<FlightLegs> legList = null;
					String displayCabin = null;
					Long st3= System.currentTimeMillis();
					Set<String> operatingCarrierSet = null;
					
					for (FlightOption eachFlightOption : optionList)
					{
						try
						{
							isFlightBlackListed = false;
							isFlightIsTagged = false;
						
							if (isBlackListDataFound)
							{
								operatingCarrierSet = new HashSet<String>();

								for (FlightLegs eachSegmentLeg : eachFlightOption.getFlightlegs())
								{
									if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
									{
										operatingCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
									}
									else{
										operatingCarrierSet.add(eachSegmentLeg.getCarrier().trim());
										eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
									}
								}
								
								for (FlightLegs eachSegmentLeg : eachFlightOption.getFlightlegs())
								{
								for (BlackOutFlightModel eachBlackOutModel : blackList)
								{
									if (operatingCarrierSet.contains(eachBlackOutModel.getAirlineName()))
									{
										if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
										{
											blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, false, null, null);

											if (blackListFlightNo != null && blackListFlightNo.size() > 0)
											{
												if(eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
													isFlightBlackListed = true;
											}

										}
										else
										{
											isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, requestBean, false, null, null);
										}
									}

									if (isFlightBlackListed)
									{
										break;
									}

								 } // End of black List model loop
								
								if (isFlightBlackListed)
								{
									break;
								}
								
								} //End of flight legs loop
							}

							if(!isFlightBlackListed)
							{
									/* Checking for RBD mastered Cabin Classes starts by vishal */
									if(isRBDDataFound)
									{
										legList = eachFlightOption.getFlightlegs();
										displayCabin = checkRBDMasteredCabinClass(legList, rdbModelMap);
									
										if(displayCabin!= null && !displayCabin.equals("")){
											
											if(!displayCabin.equalsIgnoreCase(eachFlightOption.getFlightFare().getCabinClass())){
												eachFlightOption.getFlightFare().setDisplayOnlycabinClass(displayCabin);
											}else{
												eachFlightOption.getFlightFare().setDisplayOnlycabinClass(eachFlightOption.getFlightFare().getCabinClass());
											}
											
										}else{
											eachFlightOption.getFlightFare().setDisplayOnlycabinClass(eachFlightOption.getFlightFare().getCabinClass());
										}
									} //End of isRBDDataFound
									else{
										eachFlightOption.getFlightFare().setDisplayOnlycabinClass(eachFlightOption.getFlightFare().getCabinClass());
									}
												
								/* Checking for RBD mastered Cabin Classes ends by vishal */
								
								if (tagFlightModelList != null && tagFlightModelList.size() > 0)
								{
									for (FlightTagModel eachFlightTagModel : tagFlightModelList)
									{
										/* * Vishal - Now Tag Flight will be checked for every Leg Operating Carrier instead of Segment Marketing Carrier*/
										 
										legList = eachFlightOption.getFlightlegs();

										if (legList != null && legList.size() > 0)
										{
											for (FlightLegs eachLeg : legList)
											{
												if (eachFlightTagModel.getAirlineName().equalsIgnoreCase(eachLeg.getOperatedByAirline()))
												{
													isFlightIsTagged = isFlightIsTagged(eachFlightTagModel, requestBean, eachLeg.getFlightNumber().trim(), false, eachLeg.getOrigin(), eachLeg.getDestination(), eachLeg.getOriginCountry(), eachLeg.getDestinationCountry());
												}

												if (isFlightIsTagged)
												{
													eachFlightOption.setTagFlight(eachFlightTagModel.getTagName());
													eachFlightOption.setTagFlightDesc(eachFlightTagModel.getTagDesc());
													//break;
												} // Breaking legList loop
											}
										}

										if (isFlightIsTagged)
										{
											break;
										} // Breaking tagFlightModelList loop
									}
									
								}

								filteredOptionList.add(eachFlightOption);
							}
						}
						catch (Exception ex)
						{
							CallLog.error(0, "Inside Oneway Filtering Black List & Tag Flight error cause = " + ex);
							CallLog.printStackTrace(0, ex);
						}

					} // End of flight option loop
					Long end3= System.currentTimeMillis();
					CallLog.info(102,"Time Taken ::: RBDData && blackList model && tagFlightModelList ::"+(end3-st3));
					CallLog.error(0, "After Oneway filtering filteredOptionList size = " + filteredOptionList.size());
					searchResponseBean.setOnwardFlightOptions(filteredOptionList);
				}
				
			} // End of oneway case
			else if (requestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.RoundTrip.toString()))
			{

				List<RoundTripFlightOption> roundTripOptionList = searchResponseBean.getRoundTripFlightOptions();

				if (roundTripOptionList != null && roundTripOptionList.size() > 0)
				{
					List<RoundTripFlightOption> filteredRoundTripOptionList = new ArrayList<RoundTripFlightOption>();

					boolean isFlightBlackListed = false;
					boolean isFlightIsTagged = false;
					boolean isReturnFlightIsTagged = false;
					List<String> blackListFlightNo = null;
					List<FlightLegs> legList = null;
					String displayCabin = null;
					Set<String> operatingCarrierSet = null;
					Set<String> operatingReturnCarrierSet = null;
					
					for (RoundTripFlightOption eachRoundTripFlightOption : roundTripOptionList)
					{
						try
						{
							isFlightBlackListed = false;
							isFlightIsTagged = false;
							isReturnFlightIsTagged = false;
							
							if (isBlackListDataFound)
							{
								operatingCarrierSet = new HashSet<String>();
								
								for (FlightLegs eachSegmentLeg : eachRoundTripFlightOption.getOnwardFlightOption().getFlightlegs())
								{
									if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
									{
										operatingCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
									}
									else{
										operatingCarrierSet.add(eachSegmentLeg.getCarrier().trim());
										eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
									}
								}
								
								for (FlightLegs eachSegmentLeg : eachRoundTripFlightOption.getOnwardFlightOption().getFlightlegs())
								{
									for (BlackOutFlightModel eachBlackOutModel : blackList)
									{
										if (operatingCarrierSet.contains(eachBlackOutModel.getAirlineName()))
										{
											if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
											{
												blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, false, null, null);
	
												if (blackListFlightNo != null && blackListFlightNo.size() > 0)
												{
														if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
															isFlightBlackListed = true;
												}
	
											}
											else
											{
												isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg,requestBean, false, null, null);
											}
										}
	
										if (isFlightBlackListed)
										{
											break;
										}
									} // End of black List model loop
									
									if (isFlightBlackListed)
									{
										break;
									}
									
									} // End of flight legs loop
								
									if(!isFlightBlackListed){
										operatingReturnCarrierSet = new HashSet<String>();
										
										for (FlightLegs eachSegmentLeg : eachRoundTripFlightOption.getReturnFlightOption().getFlightlegs())
										{
											if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
											{
												operatingReturnCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
											}
											else{
												operatingReturnCarrierSet.add(eachSegmentLeg.getCarrier().trim());
												eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
											}
										}
										
										for (FlightLegs eachSegmentLeg : eachRoundTripFlightOption.getReturnFlightOption().getFlightlegs())
										{
											for (BlackOutFlightModel eachBlackOutModel : blackList)
											{
												if (operatingReturnCarrierSet.contains(eachBlackOutModel.getAirlineName()))
												{
													if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
													{
														blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, true, null, null);
		
														if (blackListFlightNo != null && blackListFlightNo.size() > 0)
														{
																if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
																	isFlightBlackListed = true;
														}
		
													}
													else
													{
														isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, requestBean, true, null, null);
													}
												}
												if (isFlightBlackListed)
												{
													break;
												}
											} // End of black List model loop
											
											if (isFlightBlackListed)
											{
												break;
											}
											
											} // End of flight legs loop
									} //End of if(!isFlightBlackListed)
							} //End of if (isBlackListDataFound)

							if (!isFlightBlackListed)
							{
								
								/* Checking for RBD mastered Cabin Classes starts by vishal */
								
								if(isRBDDataFound)
								{
									//For Onward Segment
									legList = eachRoundTripFlightOption.getOnwardFlightOption().getFlightlegs();
									displayCabin = checkRBDMasteredCabinClass(legList, rdbModelMap);
								
									if(displayCabin!= null && !displayCabin.equals("")){
										
										if(!displayCabin.equalsIgnoreCase(eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().getCabinClass())){
											eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().setDisplayOnlycabinClass(displayCabin);
										}else{
											eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().setDisplayOnlycabinClass(eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().getCabinClass());
										}
										
									}else{
										eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().setDisplayOnlycabinClass(eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().getCabinClass());
									}
									
									//For Return Segment
									legList = eachRoundTripFlightOption.getReturnFlightOption().getFlightlegs();
									displayCabin = checkRBDMasteredCabinClass(legList, rdbModelMap);
								
									if(displayCabin!= null && !displayCabin.equals("")){
										
										if(!displayCabin.equalsIgnoreCase(eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().getCabinClass())){
											eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().setDisplayOnlycabinClass(displayCabin);
										}else{
											eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().setDisplayOnlycabinClass(eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().getCabinClass());
										}
										
									}else{
										eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().setDisplayOnlycabinClass(eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().getCabinClass());
									}
									
									
								} //End of isRBDDataFound
								else{
									eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().setDisplayOnlycabinClass(eachRoundTripFlightOption.getOnwardFlightOption().getFlightFare().getCabinClass());
									eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().setDisplayOnlycabinClass(eachRoundTripFlightOption.getReturnFlightOption().getFlightFare().getCabinClass());
								}
							
							/* Checking for RBD mastered Cabin Classes ends by vishal */
								
								if (tagFlightModelList != null && tagFlightModelList.size() > 0)
								{
									for (FlightTagModel eachFlightTagModel : tagFlightModelList)
									{

										legList = eachRoundTripFlightOption.getOnwardFlightOption().getFlightlegs();
										
										if(!isFlightIsTagged)
										{
										if (legList != null && legList.size() > 0)
										{
											for (FlightLegs eachLeg : legList)
											{
												if (eachFlightTagModel.getAirlineName().equalsIgnoreCase(eachLeg.getOperatedByAirline()))
												{
													isFlightIsTagged = isFlightIsTagged(eachFlightTagModel, requestBean, eachLeg.getFlightNumber().trim(), false, eachLeg.getOrigin(), eachLeg.getDestination(), eachLeg.getOriginCountry(), eachLeg.getDestinationCountry());
												}

												if (isFlightIsTagged)
												{
													eachRoundTripFlightOption.setTagFlight(eachFlightTagModel.getTagName());
													eachRoundTripFlightOption.setTagFlightDesc(eachFlightTagModel.getTagDesc());
													eachRoundTripFlightOption.getOnwardFlightOption().setTagFlight(eachFlightTagModel.getTagName());
													eachRoundTripFlightOption.getOnwardFlightOption().setTagFlightDesc(eachFlightTagModel.getTagDesc());
												} // Breaking legList loop
											}
										}
										}

										if(!isReturnFlightIsTagged)
										{
											legList = eachRoundTripFlightOption.getReturnFlightOption().getFlightlegs();

											if (legList != null && legList.size() > 0)
											{
												for (FlightLegs eachLeg : legList)
												{
													if (eachFlightTagModel.getAirlineName().equalsIgnoreCase(eachLeg.getOperatedByAirline()))
													{
														isReturnFlightIsTagged = isFlightIsTagged(eachFlightTagModel, requestBean, eachLeg.getFlightNumber().trim(), true, eachLeg.getOrigin(), eachLeg.getDestination(), eachLeg.getDestinationCountry(), eachLeg.getOriginCountry());
													}

													if (isReturnFlightIsTagged)
													{
														if(!isFlightIsTagged){
															eachRoundTripFlightOption.setTagFlight(eachFlightTagModel.getTagName());
															eachRoundTripFlightOption.setTagFlightDesc(eachFlightTagModel.getTagDesc());
															eachRoundTripFlightOption.getReturnFlightOption().setTagFlight(eachFlightTagModel.getTagName());
															eachRoundTripFlightOption.getReturnFlightOption().setTagFlightDesc(eachFlightTagModel.getTagDesc());
														}
														else
														{
															eachRoundTripFlightOption.getReturnFlightOption().setTagFlight(eachFlightTagModel.getTagName());
															eachRoundTripFlightOption.getReturnFlightOption().setTagFlightDesc(eachFlightTagModel.getTagDesc());
														}
														//break;
													} // Breaking legList loop
												}
											}
										}

										if (isFlightIsTagged && isReturnFlightIsTagged)
										{
											break;
										}
									} // Breaking tagFlightModelList loop
								}

								filteredRoundTripOptionList.add(eachRoundTripFlightOption);

							}
						}
						catch (Exception ex)
						{
							CallLog.error(0, "Inside RoundTrip Filtering Black List & Tag Flight error cause = " + ex);
							CallLog.printStackTrace(0, ex);
						}

					} // End of flight option loop

					CallLog.error(0, "After RoundTrip filtering filteredRoundTripOptionList size = " + filteredRoundTripOptionList.size());
					searchResponseBean.setRoundTripFlightOptions(filteredRoundTripOptionList);
				}

			}// End of RoundTrip case
			else if (requestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.MultiCity.toString()))
			{
				List<FlightOption> optionList = searchResponseBean.getOnwardFlightOptions();

				if (optionList != null && optionList.size() > 0)
				{
					List<FlightOption> filteredOptionList = new ArrayList<FlightOption>();
					boolean isFlightBlackListed = false;
					List<String> blackListFlightNo = null;
					boolean isFlightIsTagged = false;
					List<FlightLegs> legList = null;
					String displayCabin = null;
					Set<String> operatingCarrierSet = null;
					
					for (FlightOption eachFlightOption : optionList)
					{

						try
						{
							isFlightBlackListed = false;
							isFlightIsTagged = false;
							int count = 0;
							
							List<FlightSearchRequestBean> flightSearchRequestBeanList = requestBean.getFlightSearchRequestList();
							
							if (isBlackListDataFound)
							{
								CallLog.info(0, "multicity size of flightSearchRequestBeanList = " + flightSearchRequestBeanList.size() + " || OptionSegmentBeanList Size = " + eachFlightOption.getOptionSegmentBean().size());
								
								if(flightSearchRequestBeanList.size() == eachFlightOption.getOptionSegmentBean().size())
								{
									for (OptionSegmentBean eachOptionSegment : eachFlightOption.getOptionSegmentBean())
									{
										operatingCarrierSet = new HashSet<String>();
						
										for (FlightLegs eachSegmentLeg : eachOptionSegment.getFlightlegs())
										{
											if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
											{
												operatingCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
											}
											else{
												operatingCarrierSet.add(eachSegmentLeg.getCarrier().trim());
												eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
											}
										}
										
										for (FlightLegs eachSegmentLeg : eachOptionSegment.getFlightlegs())
										{
											for (BlackOutFlightModel eachBlackOutModel : blackList)
											{
												if (operatingCarrierSet.contains(eachBlackOutModel.getAirlineName()))
												{
													if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
													{
														blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, (FlightSearchRequestBean)flightSearchRequestBeanList.get(count), false, null, null);
		
														if (blackListFlightNo != null && blackListFlightNo.size() > 0)
														{
															if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
																	isFlightBlackListed = true;
														}
		
													}
													else
													{
														isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, (FlightSearchRequestBean)flightSearchRequestBeanList.get(count), false, null, null);
													}
												}
		
												if (isFlightBlackListed)
												{
													break;
												}
											} // End of black List model loop
											
											if (isFlightBlackListed)
											{
												break;
											}
											
										} // End of flight legs loop
		
										if (isFlightBlackListed)
										{
											break; // Break segment loop no need to
													// check black list for other
													// segment as already one
													// segment found
										}
										
										count ++;
										
									} // End of multicity segment loop
							 }
							}

							if (!isFlightBlackListed)
							{
								/* Checking for RBD mastered Cabin Classes starts by vishal */
							
									for(OptionSegmentBean segmentBean : eachFlightOption.getOptionSegmentBean())
									{
										if(isRBDDataFound)
										{
											legList = segmentBean.getFlightlegs();
											displayCabin = checkRBDMasteredCabinClass(legList, rdbModelMap);
										
											if(displayCabin!= null && !displayCabin.equals("")){
												
												if(!displayCabin.equalsIgnoreCase(eachFlightOption.getFlightFare().getCabinClass())){
													segmentBean.setDisplayOnlycabinClass(displayCabin);
												}else{
													segmentBean.setDisplayOnlycabinClass(segmentBean.getCabinClass());
												}
												
											}else{
												segmentBean.setDisplayOnlycabinClass(segmentBean.getCabinClass());
											}
										} //End of isRBDDataFound
										else{
											segmentBean.setDisplayOnlycabinClass(segmentBean.getCabinClass());
										}
									}
							
							/* Checking for RBD mastered Cabin Classes ends by vishal */
									
								
									int segmentCount = 0; 
								if(flightSearchRequestBeanList.size() == eachFlightOption.getOptionSegmentBean().size())
								{
									if (tagFlightModelList != null && tagFlightModelList.size() > 0)
									{
										for (FlightTagModel eachFlightTagModel : tagFlightModelList)
										{
											segmentCount = 0; 
											
											for (OptionSegmentBean segmentBean : eachFlightOption.getOptionSegmentBean())
											{
												legList = segmentBean.getFlightlegs();
	
												if (legList != null && legList.size() > 0)
												{
													for (FlightLegs eachLeg : legList)
													{
														if (eachFlightTagModel.getAirlineName().equalsIgnoreCase(eachLeg.getOperatedByAirline()))
														{
															isFlightIsTagged = isFlightIsTagged(eachFlightTagModel, (FlightSearchRequestBean)flightSearchRequestBeanList.get(segmentCount), eachLeg.getFlightNumber().trim(), false, eachLeg.getOrigin(), eachLeg.getDestination(), eachLeg.getOriginCountry(), eachLeg.getDestinationCountry());
															
														}
														if (isFlightIsTagged)
														{
															eachFlightOption.setTagFlight(eachFlightTagModel.getTagName());
															eachFlightOption.setTagFlightDesc(eachFlightTagModel.getTagDesc());
															//break;
														} // Breaking legList loop
													}
												}
	
												if (isFlightIsTagged)
												{
													break;
												}
												
												segmentCount ++;
											} // End of OptionSegmentBeanList
	
											if (isFlightIsTagged)
											{
												break;
											}
										}
									}
								}

								filteredOptionList.add(eachFlightOption);
							}
						}
						catch (Exception ex)
						{
							CallLog.error(0, "Inside Multicity Filtering Black List & Tag Flight error cause = " + ex);
							CallLog.printStackTrace(0, ex);
						}

					} // End of flight option loop

					CallLog.error(0, "After Multicity filtering filteredOptionList size = " + filteredOptionList.size());
					searchResponseBean.setOnwardFlightOptions(filteredOptionList);
				}

			}// End of Multicity case

		}
		catch (Exception ex)
		{
			CallLog.error(0, "Exception while validateBlackListAndTagChecks :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}
	}
	
	
	
	public void validateBlackListAndTagRBDChecksForCalendarFare(FlightCalendarFareSearchResponseBean finalCalSearchResponse, List<BlackOutFlightModel> blackList, List<FlightTagModel> tagFlightModelList, FlightSearchRequestBean requestBean, List<RbdModel> rbdList)
	{
		CallLog.info(102,"###### validateBlackListAndTagRBDChecksForCalendarFare ######");
		try
		{
			boolean isBlackListDataFound = (blackList != null && blackList.size() > 0) ? true : false;
			
			Map<String, List<RbdModel>> rdbModelMap = null;
			List<RbdModel> airLineWiseRBDList = null;
			boolean isRBDDataFound = false;
			
				if(rbdList != null && rbdList.size() > 0){
					
					isRBDDataFound = true;
					
					/* Preparing a Map, to reduce the iteration for every time of rbdList */
					rdbModelMap = new HashMap<String, List<RbdModel>>();
					
					Long st1= System.currentTimeMillis();
					for(RbdModel eachRBDModel : rbdList){
						
						if(rdbModelMap.containsKey(eachRBDModel.getAirlineName())){
							rdbModelMap.get(eachRBDModel.getAirlineName()).add(eachRBDModel);
						}
						else{
							airLineWiseRBDList = new ArrayList<RbdModel>();
							airLineWiseRBDList.add(eachRBDModel);
							rdbModelMap.put(eachRBDModel.getAirlineName(), airLineWiseRBDList);
						}
					}
					Long end1= System.currentTimeMillis();
					CallLog.info(102,"Time Taken :::rdbModelMap prepare ::"+(end1-st1));
				}
				
				CallLog.info(0, "finally rdbModelMap = "+rdbModelMap);
				
			if(requestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.OneWay.toString()))
			{
				List<FlightOption> optionList = finalCalSearchResponse.getOnwardFlightOptions();
				List<FlightCalendarOption> optionCalList = finalCalSearchResponse.getCalFlightOptions();
				
				boolean isFlightBlackListed = false;
				boolean isFlightIsTagged = false;
				List<String> blackListFlightNo = null;
				List<FlightLegs> legList = null;
				String displayCabin = null;
				Long st3= System.currentTimeMillis();
				Set<String> operatingCarrierSet = null;
				
				if (optionList != null && optionList.size() > 0)
				{
					List<FlightOption> filteredOptionList = new ArrayList<FlightOption>();
		
					for (FlightOption eachFlightOption : optionList)
					{
						try
						{
							isFlightBlackListed = false;
							isFlightIsTagged = false;
							
							if (isBlackListDataFound)
							{
								operatingCarrierSet = new HashSet<String>();
								
								for (FlightLegs eachSegmentLeg : eachFlightOption.getFlightlegs())
								{
									if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
									{
										operatingCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
									}
									else{
										operatingCarrierSet.add(eachSegmentLeg.getCarrier().trim());
										eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
									}
								}
								
								for (FlightLegs eachSegmentLeg : eachFlightOption.getFlightlegs())
								{
									for (BlackOutFlightModel eachBlackOutModel : blackList)
									{
										if (operatingCarrierSet.contains(eachBlackOutModel.getAirlineName()))
										{
											if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
											{
												blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, false, null, null);
	
												if (blackListFlightNo != null && blackListFlightNo.size() > 0)
												{
													if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
															isFlightBlackListed = true;
												}
	
											}
											else
											{
												isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, requestBean, false, null, null);
											}
										}
	
										if (isFlightBlackListed)
										{
											break;
										}
										
										} // End of black List model loop
									
										if (isFlightBlackListed)
										{
											break;
										}

									} // End of flight legs loop
								}

							if(!isFlightBlackListed)
							{
									/* Checking for RBD mastered Cabin Classes starts by vishal */
									if(isRBDDataFound)
									{
										legList = eachFlightOption.getFlightlegs();
										displayCabin = checkRBDMasteredCabinClass(legList, rdbModelMap);
									
										if(displayCabin!= null && !displayCabin.equals("")){
											
											if(!displayCabin.equalsIgnoreCase(eachFlightOption.getFlightFare().getCabinClass())){
												eachFlightOption.getFlightFare().setDisplayOnlycabinClass(displayCabin);
											}else{
												eachFlightOption.getFlightFare().setDisplayOnlycabinClass(eachFlightOption.getFlightFare().getCabinClass());
											}
											
										}else{
											eachFlightOption.getFlightFare().setDisplayOnlycabinClass(eachFlightOption.getFlightFare().getCabinClass());
										}
									} //End of isRBDDataFound
									else{
										eachFlightOption.getFlightFare().setDisplayOnlycabinClass(eachFlightOption.getFlightFare().getCabinClass());
									}
												
								/* Checking for RBD mastered Cabin Classes ends by vishal */
								
								if (tagFlightModelList != null && tagFlightModelList.size() > 0)
								{
									for (FlightTagModel eachFlightTagModel : tagFlightModelList)
									{
										/* * Vishal - Now Tag Flight will be checked for every Leg Operating Carrier instead of Segment Marketing Carrier*/
										 
										legList = eachFlightOption.getFlightlegs();

										if (legList != null && legList.size() > 0)
										{
											for (FlightLegs eachLeg : legList)
											{
												if (eachFlightTagModel.getAirlineName().equalsIgnoreCase(eachLeg.getOperatedByAirline()))
												{
													isFlightIsTagged = isFlightIsTagged(eachFlightTagModel, requestBean, eachLeg.getFlightNumber().trim(), false, eachLeg.getOrigin(), eachLeg.getDestination(), eachLeg.getOriginCountry(), eachLeg.getDestinationCountry());
												}

												if (isFlightIsTagged)
												{
													eachFlightOption.setTagFlight(eachFlightTagModel.getTagName());
													eachFlightOption.setTagFlightDesc(eachFlightTagModel.getTagDesc());
													//break;
												} // Breaking legList loop
											}
										}

										if (isFlightIsTagged)
										{
											break;
										} // Breaking tagFlightModelList loop
									}
									
								}

								filteredOptionList.add(eachFlightOption);
							}
						}
						catch (Exception ex)
						{
							CallLog.error(0, "Inside Oneway Filtering Black List & Tag Flight error cause = " + ex);
							CallLog.printStackTrace(0, ex);
						}

					} // End of flight option loop
					
					
					Long end3= System.currentTimeMillis();
					CallLog.info(102,"Calendar Fare Time Taken ::: RBDData && blackList model && tagFlightModelList ::"+(end3-st3));
					CallLog.error(0, "Calendar Fare After Oneway filtering filteredOptionList size = " + filteredOptionList.size());
					finalCalSearchResponse.setOnwardFlightOptions(filteredOptionList);
				}
				
				if(optionCalList != null && optionCalList.size() > 0)
				{
					List<FlightCalendarOption> filteredCalOptionList = new ArrayList<FlightCalendarOption>();
					
					for (FlightCalendarOption eachCalFlightOption : optionCalList)
					{
						isFlightBlackListed = false;
						
						try
						{
							if(isBlackListDataFound)
							{
								operatingCarrierSet = new HashSet<String>();
								if(eachCalFlightOption.getFlightlegs()!=null) {
									for (FlightLegs eachSegmentLeg : eachCalFlightOption.getFlightlegs())
									{
										if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
										{
											operatingCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
										}
										else{
											operatingCarrierSet.add(eachSegmentLeg.getCarrier().trim());
											eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
										}
									}
									
									for (FlightLegs eachSegmentLeg : eachCalFlightOption.getFlightlegs())
									{
										for (BlackOutFlightModel eachBlackOutModel : blackList)
										{
											if(operatingCarrierSet.contains(eachBlackOutModel.getAirlineName()))
											{
												if(eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
												{
													blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, false, eachCalFlightOption.getOnwardDate(), null);
		
													if (blackListFlightNo != null && blackListFlightNo.size() > 0)
													{
															if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
															{
																isFlightBlackListed = true;
																break;
															}
														
													}
		
												}
												else
												{
													isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, requestBean, false, eachCalFlightOption.getOnwardDate(), null);
												}
											}
		
											if (isFlightBlackListed)
											{
												break;
											}
		
											} // End of black List model loop
									
										if (isFlightBlackListed)
										{
											break;
										}
										
									  } // End of flights legs loop
								}
								
								}

								if(!isFlightBlackListed)
								{
									filteredCalOptionList.add(eachCalFlightOption);
								}else{
									eachCalFlightOption.setFareFound(false);
									eachCalFlightOption.setCheapestOption(false);
									eachCalFlightOption.setOnwardAirline(null);
									eachCalFlightOption.setOnwardCabinClass(null);
									eachCalFlightOption.setFlightFare(null);
									eachCalFlightOption.setFlightlegs(null);
									eachCalFlightOption.setTotalFare(0.0);
									filteredCalOptionList.add(eachCalFlightOption);
								}
						}
						catch (Exception ex)
						{
							CallLog.error(0, "Inside Oneway Caldendar Filtering Black List & Tag Flight error cause = " + ex);
							CallLog.printStackTrace(0, ex);
						}

					} // End of flight calendar option loop
					
					finalCalSearchResponse.setCalFlightOptions(filteredCalOptionList);
				}
				
			} // End of oneway case
			else if (requestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.RoundTrip.toString()))
			{
				List<FlightCalendarOption> optionCalList = finalCalSearchResponse.getCalFlightOptions();
				
				boolean isFlightBlackListed = false;
				List<String> blackListFlightNo = null;
				
				Set<String> operatingCarrierSet = null;
				Set<String> operatingReturnCarrierSet = null;
				
				if(optionCalList != null && optionCalList.size() > 0)
				{
					List<FlightCalendarOption> filteredCalOptionList = new ArrayList<FlightCalendarOption>();
					
					for (FlightCalendarOption eachCalFlightOption : optionCalList)
					{
						try
						{
							isFlightBlackListed = false;
							
							if(isBlackListDataFound)
							{
									operatingCarrierSet = new HashSet<String>();
									if(eachCalFlightOption.getFlightlegs() != null) {
										for (FlightLegs eachSegmentLeg : eachCalFlightOption.getFlightlegs())
										{
											if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
											{
												operatingCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
											}
											else{
												operatingCarrierSet.add(eachSegmentLeg.getCarrier().trim());
												eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
											}
										}
									
										for (FlightLegs eachSegmentLeg : eachCalFlightOption.getFlightlegs())
										{
											for (BlackOutFlightModel eachBlackOutModel : blackList)
											{
												if (operatingCarrierSet.contains(eachBlackOutModel.getAirlineName()))
												{
													if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
													{
														blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, false, eachCalFlightOption.getOnwardDate(), null);
		
														if (blackListFlightNo != null && blackListFlightNo.size() > 0)
														{
																if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
																{
																	isFlightBlackListed = true;
																	break;
																}
														}
		
													}
													else
													{
														isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, requestBean, false, eachCalFlightOption.getOnwardDate(), null);
													}
												}
												if (isFlightBlackListed)
												{
													break;
												}
		
											} // End of black List model loop
										
											if (isFlightBlackListed)
											{
												break;
											}
											
									   } // End of onward flights legs loop
									}
									
									
								 if(!isFlightBlackListed)	
								 {
									 operatingReturnCarrierSet = new HashSet<String>();
										for (FlightLegs eachSegmentLeg : eachCalFlightOption.getReturnFlightlegs())
										{
											if(eachSegmentLeg.getOperatedByAirline() != null && !eachSegmentLeg.getOperatedByAirline().equals(""))
											{
												operatingReturnCarrierSet.add(eachSegmentLeg.getOperatedByAirline().trim());
											}
											else{
												operatingReturnCarrierSet.add(eachSegmentLeg.getCarrier().trim());
												eachSegmentLeg.setOperatedByAirline(eachSegmentLeg.getCarrier().trim());
											}
										}
										
										for (FlightLegs eachSegmentLeg : eachCalFlightOption.getReturnFlightlegs())
										{
											for (BlackOutFlightModel eachBlackOutModel : blackList)
											{
												if (operatingReturnCarrierSet.contains(eachBlackOutModel.getAirlineName()))
												{
													if (eachBlackOutModel.getFlightNo() != null && !eachBlackOutModel.getFlightNo().equals(""))
													{
														blackListFlightNo = getBlackListedAirlineFlightNo(eachBlackOutModel, eachSegmentLeg, requestBean, true, null, eachCalFlightOption.getReturnDate());
		
														if (blackListFlightNo != null && blackListFlightNo.size() > 0)
														{
																if (eachSegmentLeg.getOperatedByAirline().equalsIgnoreCase(eachBlackOutModel.getAirlineName()) && blackListFlightNo.contains(eachSegmentLeg.getFlightNumber()))
																{
																	isFlightBlackListed = true;
																	break;
																}
														}
													}
													else
													{
														isFlightBlackListed = isFlightIsBlackListed(eachBlackOutModel, eachSegmentLeg, requestBean, true, null, eachCalFlightOption.getReturnDate());
													}
												}
												
											if (isFlightBlackListed)
											{
												break;
											}
			
												} // End of black List model loop
											
												if (isFlightBlackListed)
												{
													break;
												}
												
										   } // End of return flights legs loop
									} //End of if(!isFlightBlackListed)	
							 }
							
							if(!isFlightBlackListed)
							{
								filteredCalOptionList.add(eachCalFlightOption);
							}else{
								eachCalFlightOption.setFareFound(false);
								eachCalFlightOption.setCheapestOption(false);
								eachCalFlightOption.setOnwardAirline(null);
								eachCalFlightOption.setOnwardCabinClass(null);
								eachCalFlightOption.setFlightFare(null);
								eachCalFlightOption.setFlightlegs(null);
								eachCalFlightOption.setTotalFare(0.0);
								filteredCalOptionList.add(eachCalFlightOption);
							}
						}
						catch (Exception ex)
						{
							CallLog.error(0, "Inside RoundTrip Caldendar Filtering Black List & Tag Flight error cause = " + ex);
							CallLog.printStackTrace(0, ex);
						}

					} // End of flight calendar option loop
					
					finalCalSearchResponse.setCalFlightOptions(filteredCalOptionList);
				}
			}// End of RoundTrip case
		}
		catch (Exception ex)
		{
			CallLog.error(0, "Exception while validateBlackListAndTagRBDChecksForCalendarFare :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}
	}

	public boolean isFlightIsBlackListed(BlackOutFlightModel blackListModel, FlightLegs eachLeg, FlightSearchRequestBean searchRequestBean, boolean returnFlight, String calendarOnwardJourneyDate, String calendarReturnJourneyDate)
	{

		boolean isBlackListed = false;

		CallLog.info(0, "isFlightIsBlackListed For Airline = " + blackListModel.getAirlineName() + " || FlightNo >>" + blackListModel.getFlightNo() + "<<");
		
		try
		{
			if (blackListModel.getFlightNo() == null || blackListModel.getFlightNo().equals(""))
			{
					String journeyDateStr = "";
					
					if(returnFlight)
						journeyDateStr = searchRequestBean.getReturnDate();
					else
						journeyDateStr = searchRequestBean.getOnwardDate();
					
					
					CallLog.info(0, "isFlightIsBlackListed journeyDateStr = " + journeyDateStr);
					
					Date journeyDate = null; 
					
					if(returnFlight){
						if(calendarReturnJourneyDate != null && !calendarReturnJourneyDate.equals("")){
							CallLog.info(0, "getBlackListedAirlineFlightNo calendarReturnJourneyDate = " + calendarReturnJourneyDate);
							journeyDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarReturnJourneyDate);
						}
						else{
							journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
						}
					}
					else
					{
						if(calendarOnwardJourneyDate != null && !calendarOnwardJourneyDate.equals("")){
							CallLog.info(0, "getBlackListedAirlineFlightNo calendarOnwardJourneyDate = " + calendarOnwardJourneyDate);
							journeyDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarOnwardJourneyDate);
						}
						else{
							journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
						}
					}
					
					CallLog.info(0, "isFlightIsBlackListed journeyDate = " + journeyDate);

					Date validFromDate = blackListModel.getValidFrom();
					Date validToDate = blackListModel.getValidTo();
					CallLog.info(0, "eachLeg origin = " + eachLeg.getOrigin());
					CallLog.info(0, "eachLeg destination = " + eachLeg.getDestination());
					CallLog.info(0, "eachLeg origin Country = " + eachLeg.getOriginCountry());
					CallLog.info(0, "eachLeg destination Country = " + eachLeg.getDestinationCountry());

					boolean isOriginFound = false;
					boolean isDestinationFound = false;

					if (checkDateValidity(validFromDate, validToDate, journeyDate))
					{
						List<BlackListFlightCountryPosModel> blackListCountryPosList = blackListModel.getBlackCountryPosList();

						if (((blackListCountryPosList == null || blackListCountryPosList.isEmpty())) && ((blackListModel.getBlackCityList() == null || blackListModel.getBlackCityList().isEmpty())) && ((blackListModel.getBlackOutFlightList() == null || blackListModel.getBlackOutFlightList().isEmpty())))
						{
							isBlackListed = true;
							isOriginFound = true;
							isDestinationFound = true;
						}

						if (blackListCountryPosList != null && blackListCountryPosList.size() > 0)
						{

							for (BlackListFlightCountryPosModel eachCountryPosModel : blackListCountryPosList)
							{
								if (eachLeg.getOriginCountry().equalsIgnoreCase(eachCountryPosModel.getPosId()))
								{
									isOriginFound = true;
								}
								if (eachLeg.getDestinationCountry().equalsIgnoreCase(eachCountryPosModel.getPosId()))
								{
									isDestinationFound = true;
								}
							}
						}

						if (isOriginFound && isDestinationFound)
						{ // Domestic
							// Journey
							isBlackListed = true;
						}

						if (!isBlackListed)
						{
							if (blackListModel.getIsCountrySelected() == 0)
							{ // Sector

								List<BlackListFlightCityModel> blackListAirportList = blackListModel.getBlackCityList();
								if (blackListAirportList != null && blackListAirportList.size() > 0)
								{
									for (BlackListFlightCityModel eachAirportModel : blackListAirportList)
									{

										if (eachAirportModel.getAirportCode().equals(eachLeg.getOrigin()) && eachAirportModel.getIsSource() == 0)
										{ // Origin
											isOriginFound = true;
										}

										if (eachAirportModel.getAirportCode().equals(eachLeg.getDestination()) && eachAirportModel.getIsSource() == 1)
										{ // Destination
											isDestinationFound = true;
										}
									}
								}

								if (isOriginFound && isDestinationFound)
								{
									isBlackListed = true;
								}

							}
							else if (blackListModel.getIsCountrySelected() == 1)
							{ // Country

								List<BlackOutFlightCountryModel> blackListCountryList = blackListModel.getBlackOutFlightList();
								if (blackListCountryList != null && blackListCountryList.size() > 0)
								{

									for (BlackOutFlightCountryModel eachCountryModel : blackListCountryList)
									{

										if (String.valueOf(eachCountryModel.getCountryId()).equals(eachLeg.getOriginCountry()) && eachCountryModel.getIsSource() == 0)
										{ // Origin
											isOriginFound = true;
										}

										if (String.valueOf(eachCountryModel.getCountryId()).equals(eachLeg.getDestinationCountry()) && eachCountryModel.getIsSource() == 1)
										{ // Destination
											isDestinationFound = true;
										}
									}
								}

								if (isOriginFound && isDestinationFound)
								{
									isBlackListed = true;
								}

							}
						}
					}/*
				} // Ends Oneway
				else if (searchRequestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.RoundTrip.toString()))
				{
					CallLog.info(0, "inside roundtrip case");

					String journeyDateStr = searchRequestBean.getOnwardDate();
					String journeyRetDateStr = searchRequestBean.getReturnDate();

					CallLog.info(0, "RoundTrip isFlightIsBlackListed journeyDateStr = " + journeyDateStr);
					CallLog.info(0, "RoundTrip isFlightIsBlackListed journeyRetDateStr = " + journeyRetDateStr);
					
					Date journeyDate = null;
					Date journeyReturnDate = null;
					
					if(calendarOnwardJourneyDate != null && !calendarOnwardJourneyDate.equals("")){
						CallLog.info(0, "isFlightIsBlackListed calendarOnwardJourneyDate = " + calendarOnwardJourneyDate);
						
						journeyDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarOnwardJourneyDate);
					}
					else
					{
						journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
					}
					
					if(calendarReturnJourneyDate != null && !calendarReturnJourneyDate.equals("")){
						CallLog.info(0, "isFlightIsBlackListed calendarReturnJourneyDate = " + calendarReturnJourneyDate);
						
						journeyReturnDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarReturnJourneyDate);
					}
					else
					{
						journeyReturnDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyRetDateStr);
					}

					CallLog.info(0, "RoundTrip isFlightIsBlackListed journeyDate = " + journeyDate);
					CallLog.info(0, "RoundTrip isFlightIsBlackListed journeyReturnDate = " + journeyReturnDate);

					Date validFromDate = blackListModel.getValidFrom();
					Date validToDate = blackListModel.getValidTo();
					CallLog.info(0, "RoundTrip validFromDate = " + validFromDate);
					CallLog.info(0, "RoundTrip validToDate = " + validToDate);
					CallLog.info(0, "RoundTrip origin = " + searchRequestBean.getOrigin());
					CallLog.info(0, "RoundTrip destination = " + searchRequestBean.getDestination());

					boolean isOriginFound = false;
					boolean isDestinationFound = false;

					if (checkDateValidity(validFromDate, validToDate, journeyDate) && checkDateValidity(validFromDate, validToDate, journeyReturnDate))
					{

						CallLog.info(0, "roundtrip inside date valid");

						List<BlackListFlightCountryPosModel> blackListCountryPosList = blackListModel.getBlackCountryPosList();
						
						if (((blackListCountryPosList == null || blackListCountryPosList.isEmpty())) && ((blackListModel.getBlackCityList() == null || blackListModel.getBlackCityList().isEmpty())) && ((blackListModel.getBlackOutFlightList() == null || blackListModel.getBlackOutFlightList().isEmpty())))
						{
							isBlackListed = true;
							isOriginFound = true;
							isDestinationFound = true;
						}

						if (blackListCountryPosList != null && blackListCountryPosList.size() > 0)
						{

							for (BlackListFlightCountryPosModel eachCountryPosModel : blackListCountryPosList)
							{

								if (searchRequestBean.getOriginCountryId().equalsIgnoreCase(eachCountryPosModel.getPosId()))
								{
									isOriginFound = true;
								}
								if (searchRequestBean.getDestinationCountryId().equalsIgnoreCase(eachCountryPosModel.getPosId()))
								{
									isDestinationFound = true;
								}
							}
						}

						if (isOriginFound && isDestinationFound)
						{ // Domestic
							// Journey
							isBlackListed = true;
						}

						if (!isBlackListed)
						{
							if (blackListModel.getIsCountrySelected() == 0)
							{ // Sector

								List<BlackListFlightCityModel> blackListAirportList = blackListModel.getBlackCityList();
								if (blackListAirportList != null && blackListAirportList.size() > 0)
								{
									for (BlackListFlightCityModel eachAirportModel : blackListAirportList)
									{

										if (eachAirportModel.getAirportCode().equals(searchRequestBean.getOrigin()) && eachAirportModel.getIsSource() == 0)
										{ // Origin
											isOriginFound = true;
										}

										if (eachAirportModel.getAirportCode().equals(searchRequestBean.getDestination()) && eachAirportModel.getIsSource() == 1)
										{ // Destination
											isDestinationFound = true;
										}

										if (eachAirportModel.getAirportCode().equals(searchRequestBean.getDestination()) && eachAirportModel.getIsSource() == 0)
										{ // Origin
											isOriginFound = true;
										}

										if (eachAirportModel.getAirportCode().equals(searchRequestBean.getOrigin()) && eachAirportModel.getIsSource() == 1)
										{ // Destination
											isDestinationFound = true;
										}
									}
								}

								if (isOriginFound && isDestinationFound)
								{
									isBlackListed = true;
								}

							}
							else if (blackListModel.getIsCountrySelected() == 1)
							{ // Country

								List<BlackOutFlightCountryModel> blackListCountryList = blackListModel.getBlackOutFlightList();
								if (blackListCountryList != null && blackListCountryList.size() > 0)
								{

									for (BlackOutFlightCountryModel eachCountryModel : blackListCountryList)
									{

										if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getOriginCountryId()) && eachCountryModel.getIsSource() == 0)
										{ // Origin
											isOriginFound = true;
										}

										if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getDestinationCountryId()) && eachCountryModel.getIsSource() == 1)
										{ // Destination
											isDestinationFound = true;
										}

										if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getDestinationCountryId()) && eachCountryModel.getIsSource() == 1)
										{ // Origin
											isOriginFound = true;
										}

										if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getOriginCountryId()) && eachCountryModel.getIsSource() == 0)
										{ // Destination
											isDestinationFound = true;
										}
									}
								}

								if (isOriginFound && isDestinationFound)
								{
									isBlackListed = true;
								}

							}
						}
					}
				} // Ends Roundtrip

				else if (searchRequestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.MultiCity.toString()))
				{
					CallLog.info(0, "inside multicity case");

					List<FlightSearchRequestBean> flightSearchRequestBeanList = searchRequestBean.getFlightSearchRequestList();

					for (FlightSearchRequestBean eachRequestBean : flightSearchRequestBeanList)
					{
						String journeyDateStr = eachRequestBean.getOnwardDate();
						CallLog.info(0, "Multicity isFlightIsBlackListed journeyDateStr = " + journeyDateStr);
						Date journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
						CallLog.info(0, "Multicity isFlightIsBlackListed journeyDate = " + journeyDate);

						Date validFromDate = blackListModel.getValidFrom();
						Date validToDate = blackListModel.getValidTo();
						CallLog.info(0, "Multicity validFromDate = " + validFromDate);
						CallLog.info(0, "Multicity validToDate = " + validToDate);
						CallLog.info(0, "Multicity origin = " + eachRequestBean.getOrigin());
						CallLog.info(0, "Multicity destination = " + eachRequestBean.getDestination());

						boolean isOriginFound = false;
						boolean isDestinationFound = false;

						if (checkDateValidity(validFromDate, validToDate, journeyDate))
						{

							CallLog.info(0, "multicity inside date valid");

							List<BlackListFlightCountryPosModel> blackListCountryPosList = blackListModel.getBlackCountryPosList();

							if (((blackListCountryPosList == null || blackListCountryPosList.isEmpty())) && ((blackListModel.getBlackCityList() == null || blackListModel.getBlackCityList().isEmpty())) && ((blackListModel.getBlackOutFlightList() == null || blackListModel.getBlackOutFlightList().isEmpty())))
							{
								isBlackListed = true;
								isOriginFound = true;
								isDestinationFound = true;
							}

							if (blackListCountryPosList != null && blackListCountryPosList.size() > 0)
							{

								for (BlackListFlightCountryPosModel eachCountryPosModel : blackListCountryPosList)
								{

									if (searchRequestBean.getOriginCountryId().equalsIgnoreCase(eachCountryPosModel.getPosId()))
									{
										isOriginFound = true;
									}
									if (searchRequestBean.getDestinationCountryId().equalsIgnoreCase(eachCountryPosModel.getPosId()))
									{
										isDestinationFound = true;
									}
								}
							}

							if (isOriginFound && isDestinationFound)
							{ // Domestic
								// Journey
								isBlackListed = true;
							}

							if (!isBlackListed)
							{

								if (blackListModel.getIsCountrySelected() == 0)
								{ // Sector

									List<BlackListFlightCityModel> blackListAirportList = blackListModel.getBlackCityList();
									if (blackListAirportList != null && blackListAirportList.size() > 0)
									{
										for (BlackListFlightCityModel eachAirportModel : blackListAirportList)
										{

											if (eachAirportModel.getAirportCode().equals(eachRequestBean.getOrigin()) && eachAirportModel.getIsSource() == 0)
											{ // Origin
												isOriginFound = true;
											}

											if (eachAirportModel.getAirportCode().equals(eachRequestBean.getDestination()) && eachAirportModel.getIsSource() == 1)
											{ // Destination
												isDestinationFound = true;
											}
										}
									}

									if (isOriginFound && isDestinationFound)
									{
										isBlackListed = true;
									}

								}
								else if (blackListModel.getIsCountrySelected() == 1)
								{ // Country

									List<BlackOutFlightCountryModel> blackListCountryList = blackListModel.getBlackOutFlightList();
									if (blackListCountryList != null && blackListCountryList.size() > 0)
									{

										for (BlackOutFlightCountryModel eachCountryModel : blackListCountryList)
										{

											if (String.valueOf(eachCountryModel.getCountryId()).equals(eachRequestBean.getOriginCountryId()) && eachCountryModel.getIsSource() == 0)
											{ // Origin
												isOriginFound = true;
											}

											if (String.valueOf(eachCountryModel.getCountryId()).equals(eachRequestBean.getDestinationCountryId()) && eachCountryModel.getIsSource() == 1)
											{ // Destination
												isDestinationFound = true;
											}
										}
									}

									if (isOriginFound && isDestinationFound)
									{
										isBlackListed = true;
									}

								}
							}
						}

						if (isBlackListed)
						{
							break; // Don't need to check other segments as one
									// segment found matched with blacklist
									// criteria
						}
					}

				} // Ends Multicity
*/			}

			CallLog.info(0, "For Airline = " + blackListModel.getAirlineName() + " || isBlackListed = " + isBlackListed);

		}
		catch (Exception ex)
		{
			CallLog.error(0, "Exception while validating black listed flight :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}
		return isBlackListed;

	}

	public List<String> getBlackListedAirlineFlightNo(BlackOutFlightModel blackListModel, FlightLegs eachLeg, FlightSearchRequestBean searchRequestBean, boolean returnFlight, String calendarOnwardJourneyDate, String calendarReturnJourneyDate)
	{

		boolean isBlackListed = false;

		CallLog.info(0, "getBlackListedAirlineFlightNo returnFlight = " + returnFlight);

		List<String> blackListedFlightNo = null;

		try
		{
				String journeyDateStr = "";
				
				if(returnFlight)
					journeyDateStr = searchRequestBean.getReturnDate();
				else
					journeyDateStr = searchRequestBean.getOnwardDate();
					
				CallLog.info(0, "getBlackListedAirlineFlightNo journeyDateStr = " + journeyDateStr);
				Date journeyDate = null;
				
				if(returnFlight){
					if(calendarReturnJourneyDate != null && !calendarReturnJourneyDate.equals("")){
						CallLog.info(0, "getBlackListedAirlineFlightNo calendarReturnJourneyDate = " + calendarReturnJourneyDate);
						journeyDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarReturnJourneyDate);
					}
					else{
						journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
					}
				}
				else
				{
					if(calendarOnwardJourneyDate != null && !calendarOnwardJourneyDate.equals("")){
						CallLog.info(0, "getBlackListedAirlineFlightNo calendarOnwardJourneyDate = " + calendarOnwardJourneyDate);
						journeyDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarOnwardJourneyDate);
					}
					else{
						journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
					}
				}

				Date validFromDate = blackListModel.getValidFrom();
				Date validToDate = blackListModel.getValidTo();
				//CallLog.info(0, "validFromDate = " + validFromDate);
				//CallLog.info(0, "validToDate = " + validToDate);
				CallLog.info(0, "eachLeg origin = " + eachLeg.getOrigin());
				CallLog.info(0, "eachLeg destination = " + eachLeg.getDestination());
				CallLog.info(0, "eachLeg origin Country = " + eachLeg.getOriginCountry());
				CallLog.info(0, "eachLeg destination Country = " + eachLeg.getDestinationCountry());

				boolean isOriginFound = false;
				boolean isDestinationFound = false;

				if (checkDateValidity(validFromDate, validToDate, journeyDate))
				{
					List<BlackListFlightCountryPosModel> blackListCountryPosList = blackListModel.getBlackCountryPosList();

					if ((blackListCountryPosList == null || blackListCountryPosList.isEmpty()) && (blackListModel.getBlackCityList() == null || blackListModel.getBlackCityList().isEmpty()) && (blackListModel.getBlackOutFlightList() == null || blackListModel.getBlackOutFlightList().isEmpty()))
					{
						isBlackListed = true;
						isOriginFound = true;
						isDestinationFound = true;
					}

					if (blackListCountryPosList != null && blackListCountryPosList.size() > 0)
					{

						for (BlackListFlightCountryPosModel eachCountryPosModel : blackListCountryPosList)
						{
							if (eachLeg.getOriginCountry().equalsIgnoreCase(eachCountryPosModel.getPosId()))
							{
								isOriginFound = true;
							}
							if (eachLeg.getDestinationCountry().equalsIgnoreCase(eachCountryPosModel.getPosId()))
							{
								isDestinationFound = true;
							}
						}
					}

					if (isOriginFound && isDestinationFound)
					{ // Domestic
						// Journey
						isBlackListed = true;
					}

					if (!isBlackListed)
					{
						if (blackListModel.getIsCountrySelected() == 0)
						{ // Sector

							List<BlackListFlightCityModel> blackListAirportList = blackListModel.getBlackCityList();
							if (blackListAirportList != null && blackListAirportList.size() > 0)
							{
								for (BlackListFlightCityModel eachAirportModel : blackListAirportList)
								{

									if (eachAirportModel.getAirportCode().equals(eachLeg.getOrigin()) && eachAirportModel.getIsSource() == 0)
									{ // Origin
										isOriginFound = true;
									}

									if (eachAirportModel.getAirportCode().equals(eachLeg.getDestination()) && eachAirportModel.getIsSource() == 1)
									{ // Destination
										isDestinationFound = true;
									}
								}
							}

							if (isOriginFound && isDestinationFound)
							{
								isBlackListed = true;
							}

						}
						else if (blackListModel.getIsCountrySelected() == 1)
						{ // Country

							List<BlackOutFlightCountryModel> blackListCountryList = blackListModel.getBlackOutFlightList();
							if (blackListCountryList != null && blackListCountryList.size() > 0)
							{

								for (BlackOutFlightCountryModel eachCountryModel : blackListCountryList)
								{

									if (String.valueOf(eachCountryModel.getCountryId()).equals(eachLeg.getOriginCountry()) && eachCountryModel.getIsSource() == 0)
									{ // Origin
										isOriginFound = true;
									}

									if (String.valueOf(eachCountryModel.getCountryId()).equals(eachLeg.getDestinationCountry()) && eachCountryModel.getIsSource() == 1)
									{ // Destination
										isDestinationFound = true;
									}
								}
							}

							if (isOriginFound && isDestinationFound)
							{
								isBlackListed = true;
							}

						}
					}
				}
			/*} // Ends Oneway
*/			/*else if (searchRequestBean.getTripType().equalsIgnoreCase(ParamMappingUtil.TripType.RoundTrip.toString()))
			{
				String journeyDateStr = searchRequestBean.getOnwardDate();
				String journeyRetDateStr = searchRequestBean.getReturnDate();

				CallLog.info(0, "RoundTrip getBlackListedAirlineFlightNo journeyDateStr = " + journeyDateStr);
				CallLog.info(0, "RoundTrip getBlackListedAirlineFlightNo journeyRetDateStr = " + journeyRetDateStr);
		
				Date journeyDate = null;
				Date journeyReturnDate = null;
				
				if(calendarOnwardJourneyDate != null && !calendarOnwardJourneyDate.equals("")){
					CallLog.info(0, "getBlackListedAirlineFlightNo calendarOnwardJourneyDate = " + calendarOnwardJourneyDate);
					journeyDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarOnwardJourneyDate);
				}
				else{
					journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
				}
				
				if(calendarReturnJourneyDate != null && !calendarReturnJourneyDate.equals("")){
					CallLog.info(0, "getBlackListedAirlineFlightNo calendarReturnJourneyDate = " + calendarReturnJourneyDate);
					journeyReturnDate = AmadeusResponseUtil.getDateObject("dd-MM-yyyy", calendarReturnJourneyDate);
				}
				else{
					journeyReturnDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyRetDateStr);
				}
				
				// CallLog.info(0,"RoundTrip getBlackListedAirlineFlightNo journeyDate = "+journeyDate);
				// CallLog.info(0,"RoundTrip getBlackListedAirlineFlightNo journeyReturnDate = "+journeyReturnDate);

				Date validFromDate = blackListModel.getValidFrom();
				Date validToDate = blackListModel.getValidTo();
				CallLog.info(0, "RoundTrip validFromDate = " + validFromDate);
				CallLog.info(0, "RoundTrip validToDate = " + validToDate);
				CallLog.info(0, "RoundTrip origin = " + searchRequestBean.getOrigin());
				CallLog.info(0, "RoundTrip destination = " + searchRequestBean.getDestination());

				boolean isOriginFound = false;
				boolean isDestinationFound = false;

				if (checkDateValidity(validFromDate, validToDate, journeyDate) && checkDateValidity(validFromDate, validToDate, journeyReturnDate))
				{

					List<BlackListFlightCountryPosModel> blackListCountryPosList = blackListModel.getBlackCountryPosList();

					if ((blackListCountryPosList == null || blackListCountryPosList.isEmpty()) && (blackListModel.getBlackCityList() == null || blackListModel.getBlackCityList().isEmpty()) && (blackListModel.getBlackOutFlightList() == null || blackListModel.getBlackOutFlightList().isEmpty()))
					{
						isBlackListed = true;
						isOriginFound = true;
						isDestinationFound = true;
					}

					if (blackListCountryPosList != null && blackListCountryPosList.size() > 0)
					{

						for (BlackListFlightCountryPosModel eachCountryPosModel : blackListCountryPosList)
						{

							if (searchRequestBean.getOriginCountryId().equalsIgnoreCase(eachCountryPosModel.getPosId()))
							{
								isOriginFound = true;
							}
							if (searchRequestBean.getDestinationCountryId().equalsIgnoreCase(eachCountryPosModel.getPosId()))
							{
								isDestinationFound = true;
							}
						}
					}

					if (isOriginFound && isDestinationFound)
					{ // Domestic
						// Journey
						isBlackListed = true;
					}

					if (!isBlackListed)
					{
						if (blackListModel.getIsCountrySelected() == 0)
						{ // Sector

							List<BlackListFlightCityModel> blackListAirportList = blackListModel.getBlackCityList();
							if (blackListAirportList != null && blackListAirportList.size() > 0)
							{
								for (BlackListFlightCityModel eachAirportModel : blackListAirportList)
								{

									if (eachAirportModel.getAirportCode().equals(searchRequestBean.getOrigin()) && eachAirportModel.getIsSource() == 0)
									{ // Origin
										isOriginFound = true;
									}

									if (eachAirportModel.getAirportCode().equals(searchRequestBean.getDestination()) && eachAirportModel.getIsSource() == 1)
									{ // Destination
										isDestinationFound = true;
									}

									if (eachAirportModel.getAirportCode().equals(searchRequestBean.getDestination()) && eachAirportModel.getIsSource() == 0)
									{ // Origin
										isOriginFound = true;
									}

									if (eachAirportModel.getAirportCode().equals(searchRequestBean.getOrigin()) && eachAirportModel.getIsSource() == 1)
									{ // Destination
										isDestinationFound = true;
									}
								}
							}

							if (isOriginFound && isDestinationFound)
							{
								isBlackListed = true;
							}

						}
						else if (blackListModel.getIsCountrySelected() == 1)
						{ // Country

							List<BlackOutFlightCountryModel> blackListCountryList = blackListModel.getBlackOutFlightList();
							if (blackListCountryList != null && blackListCountryList.size() > 0)
							{

								for (BlackOutFlightCountryModel eachCountryModel : blackListCountryList)
								{

									if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getOriginCountryId()) && eachCountryModel.getIsSource() == 0)
									{ // Origin
										isOriginFound = true;
									}

									if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getDestinationCountryId()) && eachCountryModel.getIsSource() == 1)
									{ // Destination
										isDestinationFound = true;
									}

									if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getDestinationCountryId()) && eachCountryModel.getIsSource() == 0)
									{ // Origin
										isOriginFound = true;
									}

									if (String.valueOf(eachCountryModel.getCountryId()).equals(searchRequestBean.getOriginCountryId()) && eachCountryModel.getIsSource() == 1)
									{ // Destination
										isDestinationFound = true;
									}
								}
							}

							if (isOriginFound && isDestinationFound)
							{
								isBlackListed = true;
							}

						}
					}
				}
			} // Ends Roundtrip
			 */
			CallLog.info(0, "value of getBlackListedAirlineFlightNo isBlackListed = " + isBlackListed);

			if (isBlackListed)
			{
				blackListedFlightNo = new ArrayList();

				String flightNumberStr = blackListModel.getFlightNo();

				if (flightNumberStr.contains(","))
				{

					String[] seperateFlightNo = flightNumberStr.split(",");

					if (seperateFlightNo != null && seperateFlightNo.length > 0)
					{

						for (int v = 0; v < seperateFlightNo.length; v++)
						{

							if (!seperateFlightNo[v].equals(""))
							{
								blackListedFlightNo.add(seperateFlightNo[v].trim());
							}
						}
					}

				}
				else
				{
					blackListedFlightNo.add(flightNumberStr);
				}
			}

		}
		catch (Exception ex)
		{
			CallLog.error(0, "Exception while validating black listed flight :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}

		return blackListedFlightNo;
	}

	public boolean isFlightIsTagged(FlightTagModel eachFlightTagModel, FlightSearchRequestBean searchRequestBean, String flightNo, boolean returnFlight, String origin, String destination, String originCountryId, String destCountryId)
	{

		boolean isFlightTagged = false;
		boolean isAirlineAndFlightNoExist = true;

		AmadeusRequestUtil amadeusRequestUtil = null;
		ArrayList<String> flightNoList = null;

		try
		{
			amadeusRequestUtil = new AmadeusRequestUtil();

			if (eachFlightTagModel.getFlightId() != null && !eachFlightTagModel.getFlightId().equals(""))
			{

				flightNoList = new ArrayList<String>();

				String flightNumberStr = eachFlightTagModel.getFlightId();
				flightNumberStr = flightNumberStr.trim();

				if (flightNumberStr.contains(","))
				{

					String[] seperateFlightNo = flightNumberStr.split(",");

					if (seperateFlightNo != null && seperateFlightNo.length > 0)
					{

						for (int v = 0; v < seperateFlightNo.length; v++)
						{

							if (!seperateFlightNo[v].equals(""))
							{
								flightNoList.add(seperateFlightNo[v].trim());
							}
						}
					}
				}
				else
				{
					flightNoList.add(flightNumberStr);
				}
				/* Check for carrier names as well as for flight Nos */
				if (!flightNoList.contains(flightNo))
				{
					isAirlineAndFlightNoExist = false;
				}
			}

			//CallLog.info(0, "isAirlineAndFlightNoExist = " + isAirlineAndFlightNoExist);

			if (isAirlineAndFlightNoExist)
			{
				String journeyDateStr = "";
				
				if(returnFlight){
					journeyDateStr = searchRequestBean.getReturnDate();
				}
				else{
					journeyDateStr = searchRequestBean.getOnwardDate();
				}
				CallLog.info(0, "isFlightIsTagged journeyDateStr = " + journeyDateStr);
				Date journeyDate = AmadeusResponseUtil.getDateObject("yyyy-MM-dd", journeyDateStr);
				CallLog.info(0, "isFlightIsTagged journeyDate = " + journeyDate);

				Date validFromDate = eachFlightTagModel.getValidFrom();
				Date validToDate = eachFlightTagModel.getValidTo();
				//CallLog.info(0, "isFlightIsTagged validFromDate = " + validFromDate);
				//CallLog.info(0, "isFlightIsTagged validToDate = " + validToDate);
				CallLog.info(0, "leg origin = " + origin);
				CallLog.info(0, "leg destination = " + destination);
				CallLog.info(0, "leg origin country = " + originCountryId);
				CallLog.info(0, "leg destination country = " + destCountryId);

				boolean isOriginFound = false;
				boolean isDestinationFound = false;

				if (amadeusRequestUtil.checkDateValidity(validFromDate, validToDate, journeyDate))
				{
					if (((eachFlightTagModel.getFlightCityList() == null || eachFlightTagModel.getFlightCityList().size() == 0)) && ((eachFlightTagModel.getFlightCountryList() == null || eachFlightTagModel.getFlightCountryList().size() == 0)))
					{
						isFlightTagged = true;
						isOriginFound = true;
						isDestinationFound = true;
					}

					if (eachFlightTagModel.getIsCountrySelected() == 0)
					{ // Sector

						List<TagFlightCityModel> taggedAirportList = eachFlightTagModel.getFlightCityList();

						if (taggedAirportList != null && !taggedAirportList.isEmpty())
						{

							for (TagFlightCityModel eachTagFlightCityModel : taggedAirportList)
							{

								if (eachTagFlightCityModel.getAirportCode().equals(origin) && eachTagFlightCityModel.getIsSource() == 0)
								{ // Origin
									isOriginFound = true;
								}

								if (eachTagFlightCityModel.getAirportCode().equals(destination) && eachTagFlightCityModel.getIsSource() == 1)
								{ // Destination
									isDestinationFound = true;
								}
							}
						}

						if (isOriginFound && isDestinationFound)
						{
							isFlightTagged = true;
						}

					} // End Airport Check
					else if (eachFlightTagModel.getIsCountrySelected() == 1)
					{ // Country

						List<TagFlightCountryModel> tagCountryModelList = eachFlightTagModel.getFlightCountryList();

						if (tagCountryModelList != null && !tagCountryModelList.isEmpty())
						{

							for (TagFlightCountryModel eachTagCountryModel : tagCountryModelList)
							{

								if (String.valueOf(eachTagCountryModel.getCountryId()).equals(originCountryId) && eachTagCountryModel.getIsSource() == 0)
								{ // Origin
									isOriginFound = true;
								}

								if (String.valueOf(eachTagCountryModel.getCountryId()).equals(destCountryId) && eachTagCountryModel.getIsSource() == 1)
								{ // Destination
									isDestinationFound = true;
								}
							}
						}

						if (isOriginFound && isDestinationFound)
						{
							isFlightTagged = true;
						}

					} // End Country Check
				}

			} // isAirlineAndFlightNoExist

			//CallLog.info(0, "isFlightIsTagged For Airline = " + eachFlightTagModel.getAirlineName() + " isFlightTagged = " + isFlightTagged);

			amadeusRequestUtil = null;

		}
		catch (Exception ex)
		{
			CallLog.error(0, "Exception while validating tagged flight :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}

		return isFlightTagged;

	}
	
	
	
	
	public String checkRBDMasteredCabinClass(List<FlightLegs> flightLegs, Map<String, List<RbdModel>> rbdMap)
	{

		String displayCabinClass = "";
		List<RbdModel> rbdModelList = null;	
		boolean isRBDFound = false;
		try
		{
			for(FlightLegs eachLeg : flightLegs)
			{
				rbdModelList = rbdMap.get(eachLeg.getOperatedByAirline());
				
				if(rbdModelList != null && rbdModelList.size() > 0){
					
					for(RbdModel eachRBDModel : rbdModelList)
					{
						if(eachRBDModel.getRbdType().trim().length() == 1)
						{
							if(eachRBDModel.getRbdType().equalsIgnoreCase(eachLeg.getBookingClass())){
								isRBDFound = true;	
								
								if(eachRBDModel.getCabinType() == 1){
									displayCabinClass = "ECONOMY";
								}
								else if(eachRBDModel.getCabinType() == 2){
									displayCabinClass = "PREMIUM ECONOMY";
								}
								else if(eachRBDModel.getCabinType() == 3){
									displayCabinClass = "BUSINESS";
								}
								else if(eachRBDModel.getCabinType() == 4){
									displayCabinClass = "FIRST CLASS";
								}
							}
						}else{
							if(eachRBDModel.getRbdType().trim().contains(eachLeg.getBookingClass())){
								isRBDFound = true;	
								
								if(eachRBDModel.getCabinType() == 1){
									displayCabinClass = "ECONOMY";
								}
								else if(eachRBDModel.getCabinType() == 2){
									displayCabinClass = "PREMIUM ECONOMY";
								}
								else if(eachRBDModel.getCabinType() == 3){
									displayCabinClass = "BUSINESS";
								}
								else if(eachRBDModel.getCabinType() == 4){
									displayCabinClass = "FIRST CLASS";
								}
							}
						}
						
						if(isRBDFound)break;
					}
					
				}
				
				if(isRBDFound)break;
			}

		}
		catch (Exception ex)
		{
			CallLog.error(0, "Exception while getting checkRBDMasteredCabinClass :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}

		return displayCabinClass;

	}
	

	public boolean checkDateValidity(Date fromDate, Date toDate, Date dateTocheck)
	{
		boolean isDateValid = false;
		try
		{
			if (dateTocheck.after(fromDate) && dateTocheck.before(toDate))
			{
				isDateValid = true;
			}
			else if (dateTocheck.equals(fromDate))
			{
				isDateValid = true;
			}
			else if (dateTocheck.equals(toDate))
			{
				isDateValid = true;
			}

		}
		catch (Exception ex)
		{
			CallLog.error(0, "AirHelperUtil Exception checkDateValidity while comparing dates :: error cause = " + ex);
			CallLog.printStackTrace(0, ex);
		}

		// CallLog.info(0,"fromDate = "+fromDate+" || toDate = "+toDate+" || dateTocheck = "+dateTocheck+" || isDateValid = "+isDateValid);

		return isDateValid;

	}

	static List<Date> getPlusMinusDates(String onwardDate)
	{
		List<Date> dateList = null;
		try
		{

			DateFormat datefr = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = datefr.parse(onwardDate);
			Calendar c = Calendar.getInstance();
			dateList = new ArrayList<Date>();
			for (int i = -3; i <= 3; i++)
			{
				c.setTime(date1);
				c.add(Calendar.DATE, i);
				Date dt = c.getTime();
				dateList.add(dt);
			}

		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			CallLog.info(101,">>>>>>>>>>dateList exception----->"+e.getMessage());
			
		}
		CallLog.info(101,">>>>>>>>>>dateList----->"+dateList);
		return dateList;
	}

}
