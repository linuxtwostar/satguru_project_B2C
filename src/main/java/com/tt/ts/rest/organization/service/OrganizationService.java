package com.tt.ts.rest.organization.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.test.model.AutoSuggestCountry;
import com.tt.nc.common.util.TTLog;
import com.tt.satguruportal.flight.service.FlightRestService;
import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airline.service.AirlineService;
import com.tt.ts.airport.manager.AirportManager;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.airport.service.AirportService;
import com.tt.ts.blacklist.model.BlackListFlightCityModel;
import com.tt.ts.blacklist.model.BlackOutFlightModel;
import com.tt.ts.bsp.model.BspAirportModel;
import com.tt.ts.bsp.model.BspCityModel;
import com.tt.ts.bsp.model.BspCommissionModel;
import com.tt.ts.bsp.model.BspCountryModel;
import com.tt.ts.common.enums.manager.EnumManager;
import com.tt.ts.common.enums.model.EnumModel;
import com.tt.ts.common.errorConstant.ErrorCodeContant;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.common.util.CommonUtil;
import com.tt.ts.currency.model.CurrencyBean;
import com.tt.ts.flighttag.model.FlightTagModel;
import com.tt.ts.gdsdealcode.model.GDSDealCodeAgencyModel;
import com.tt.ts.gdsdealcode.model.GDSDealCodeAirportModel;
import com.tt.ts.gdsdealcode.model.GDSDealCodeCountryModel;
import com.tt.ts.gdsdealcode.model.GDSDealCodeModel;
import com.tt.ts.geolocations.manager.GeoLocationManager;
import com.tt.ts.geolocations.model.CityBean;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.hotel.model.HotelModel;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterModel;
import com.tt.ts.organization.model.OrganizationAirlineCodeModal;
import com.tt.ts.organization.model.OrganizationDocumentModel;
import com.tt.ts.organization.model.OrganizationFinancialModel;
import com.tt.ts.payment.manager.PaymentGatewayManager;
import com.tt.ts.payment.model.FraudulentModel;
import com.tt.ts.payment.model.PaymentMode;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.rbd.model.RbdModel;
import com.tt.ts.rest.common.CommonModal;
import com.tt.ts.rest.common.RedisService;
import com.tt.ts.rest.common.util.CredentialConfigModel;
import com.tt.ts.rest.common.util.OrganizationTicketPccModal;
import com.tt.ts.rest.common.util.RedisCacheUtil;
import com.tt.ts.rest.common.util.ServiceConfigModel;
import com.tt.ts.rest.common.util.SupplierConfigModal;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.organization.manager.OrganizationManager;
import com.tt.ts.supplier.modal.SupplierModal;
import com.tt.ts.suppliercredential.modal.SupplierCredentialCountryModal;
import com.tt.ts.suppliercredential.modal.SupplierCredentialModal;
import com.tt.ts.uccf.manager.UCCFManager;
import com.tt.ts.uccf.modal.UCCFCityMapping;
import com.tt.ts.uccf.modal.UCCFModal;
import com.ws.payment.bean.PaymentRequest;
import com.ws.services.flight.bean.booking.FlightBookingRequestBean;
import com.ws.services.flight.bean.flightsearch.FlightLegs;
import com.ws.services.flight.bean.flightsearch.FlightSearchRequestBean;
import com.ws.services.flight.bean.flightsearch.OptionSegmentBean;
import com.ws.services.flight.bean.importpnr.ImportPNRPricingRequestBean;
import com.ws.services.util.CallLog;

@Service(value = "orgServiceRest")
public class OrganizationService
{
	@Autowired
	private OrganizationManager orgManager;

	@Autowired
	private GeoLocationManager geoLocationManager;

	@Autowired
	private AirlineService airlineService;

	@Autowired
	private EnumManager enumManager;

	@Autowired
	private UCCFManager uccfManager;

	@Autowired
	private AirportManager airportManager;

	@Autowired
	RedisService redisService;
	
	@Autowired
	MessageSource messageSource; 

	@Autowired
	private PaymentGatewayManager paymentGatewayManager;
	
	@Autowired
    CorporateService corporateService;

	@Autowired
	AirportService airportService;
	
	@Autowired
	GeoLocationService geoLocationService;
	
	private static final String APPLICATIONRESOURCE = "ApplicationResources";
	private static final String FLIGHT = "Flight";
	private static final String HOTEL = "Hotel";
	private static final String AMADEUS = "Amadeus";
	private static final String ONEWAY = "OneWay";
	private static final String MULTICITY = "MultiCity";
	private static final String ROUNDTRIP = "RoundTrip";
	private static final String TICKETING= "TICKETING";
	private static final String AIRPORT_AUTOSUGGEST_EXPIRE_TIME = "AIRPORT_AUTOSUGGEST_EXPIRE_TIME";
	private static final String ALL_AIRPORT_EXPIRE_TIME = "ALL_AIRPORT_EXPIRE_TIME";
	private static final String BRANCH = "Branch";
	
	public ResultBean getProductById(Integer groupId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<ProductModel> productBean = orgManager.getProductById(groupId, null);
			if(productBean != null && !productBean.isEmpty()) {
				for(ProductModel productModel : productBean) {
					productModel.setRuleText(null);		
				}
			}
			resultBean.setResultList(productBean);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean getSupplierById(Integer groupId, Integer productId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<SupplierModal> productBean = orgManager.getSupplierById(groupId, productId);
			resultBean.setResultList(productBean);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean getCredentialById(Integer groupId, Integer productId, Integer supplierId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<SupplierCredentialModal> supplierCredentialModals = orgManager.getCredentialById(groupId, productId, supplierId);
			resultBean.setResultList(supplierCredentialModals);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean getSupplierCredentialById(Integer groupId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<SupplierModal> supplierModal = orgManager.getSupplierCredentialById(groupId);
			resultBean.setResultList(supplierModal);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean getCompanyDetailsById(Integer userId, Integer organizationType)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object> objectData = orgManager.getCompanyDetailsById(userId, organizationType);
			resultBean.setResultList(objectData);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}
	
	public ResultBean getCompanyDetailsByAgentId(Integer agentId, Integer organizationType)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object> objectData = orgManager.getCompanyDetailsByAgentId(agentId, organizationType);
			resultBean.setResultList(objectData);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean fetchAgencyCreditLimit(Integer groupId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			if (groupId != null)
			{
				List<OrganizationFinancialModel> objectData = orgManager.fetchAgencyCreditLimit(groupId);
				resultBean.setResultList(objectData);
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;

	}

	public ResultBean getProductSupplierCredentialById(Integer groupId, Integer productId, Integer supplierId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object[]> supplierModal = orgManager.getProductSupplierCredentialById(groupId, productId, supplierId);
			resultBean.setResultList(supplierModal);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultBean getServiceConfigCredential(Integer groupId, String agencyCode, Integer countryId,String orgType)
	{
		List<Object> currencyList = null;
		String currencyCode = null;
		List<Object> objectData=null;
		ResultBean resultBean = new ResultBean();
		List<ServiceConfigModel> productConfigList = new ArrayList<>();
		try
		{
			List<AirlineModel> airlineModels = null;
			objectData = orgManager.fetchOrganizationDetails(groupId,agencyCode);
			if(objectData!=null && !objectData.isEmpty()){
				Object[] object = (Object[]) objectData.get(0);
				List<ProductModel> productBean = orgManager.getProductById(groupId, null);
				{
					for (ProductModel p : productBean)
					{
						if (p.getProductName().equalsIgnoreCase(FLIGHT))
						{
							AirlineModel am = new AirlineModel();
							am.setApprovalStatus(1);
							am.setStatus(1);
							ResultBean resultBean2 = airlineService.getAirlinesNew(am);
							airlineModels = (List<AirlineModel>)resultBean2.getResultList() ;
						}
						ServiceConfigModel productConfigModel = new ServiceConfigModel();
						productConfigModel.setProductName(p.getProductName());
						productConfigModel.setCurrencyName(object[11].toString());
						productConfigModel.setCurrencyCode(object[12].toString());
						productConfigModel.setCompanyName(object[9].toString());
						String agencyCurrCode=object[12].toString();
						String currId=object[10].toString();
						Integer branchId = (Integer)object[0];
						String branchCode = object[13].toString();
						List<SupplierModal> supplierBean;
						if(orgType.equalsIgnoreCase(BRANCH)){
							supplierBean = orgManager.getSupplierById(groupId, p.getProductId());		
						}
						else{
							List<SupplierModal> supplierBeanAgency = orgManager.getSupplierById(groupId, p.getProductId());
							List<SupplierModal> supplierBeanBranch = orgManager.getSupplierById(branchId, p.getProductId());
							supplierBean = new ArrayList<>();
							for(SupplierModal supplierModalAgn : supplierBeanAgency) {
								for(SupplierModal supplierModalBrn : supplierBeanBranch) {
									if(supplierModalAgn.getSupplierId() == supplierModalBrn.getSupplierId()) {
										supplierBean.add(supplierModalAgn);
										break;
									}
								}
							}				
						}	
						List<SupplierConfigModal> suppConfigModelList = new ArrayList<>();
						for (int i = 0; i < supplierBean.size(); i++)
						{
							if (supplierBean.get(i).getSupplierName().equalsIgnoreCase(AMADEUS))
							{
								
								List<Object[]>  supplierCredentialModals = new ArrayList<>();
								
								List<Object> gdsSearchSupplier = new ArrayList<>();
								if(orgType.equalsIgnoreCase(BRANCH)){
									gdsSearchSupplier = orgManager.gdsSearchSupplierCredential(groupId,orgType);
								} else {
									List<Object> gdsSearchSupplierAgn = orgManager.gdsSearchSupplierCredential(groupId,orgType);
									List<Object> gdsSearchSupplierBrn = orgManager.gdsSearchSupplierCredential(branchId,BRANCH);
									for (Iterator iteratorAgn = gdsSearchSupplierAgn.iterator(); iteratorAgn.hasNext();)
									{
										Object[] objectAgn = (Object[]) iteratorAgn.next();
										Integer credentailIdAgn = Integer.parseInt(objectAgn[1].toString());
										for (Iterator iteratorBrn = gdsSearchSupplierBrn.iterator(); iteratorBrn.hasNext();)
										{
											Object[] objectBrn = (Object[]) iteratorBrn.next();
											Integer credentailIdBrn = Integer.parseInt(objectBrn[1].toString());
											if(credentailIdAgn.equals(credentailIdBrn)) {
												gdsSearchSupplier.add(objectAgn);
											}
										}	
									}	
								}
								
								 
								for (Iterator iterator = gdsSearchSupplier.iterator(); iterator.hasNext();)
								{
									Object[] object2 = (Object[]) iterator.next();
									SupplierConfigModal sConfigModel = new SupplierConfigModal();
									sConfigModel.setSupplierName(supplierBean.get(i).getSupplierName());
									sConfigModel.setSupplierId(supplierBean.get(i).getSupplierId());
									sConfigModel.setPccId(Integer.parseInt(object2[1].toString()));
									sConfigModel.setCredentialName(object2[2].toString());
									Integer credentailId = Integer.parseInt(object2[1].toString());
									if(object2[5]!=null){
										int currencyId=(Integer)object2[5];
										List<CurrencyBean> currencyModelList = orgManager.fetchCurrencyById(currencyId);
										if(currencyModelList!=null && !currencyModelList.isEmpty()){
											String supplierCurrency=currencyModelList.get(0).getCurrencyCode();
											if(supplierCurrency!=null && !supplierCurrency.isEmpty()){
												//sConfigModel.setSupplierCurrency(supplierCurrency);
												sConfigModel.setSupplierCurrency(supplierCurrency);
													String fromCurrency=corporateService.getCurrencyId(supplierCurrency).getResultString();
													String toCurrency=corporateService.getCurrencyId(agencyCurrCode).getResultString();
													String supplierName=sConfigModel.getSupplierName();
													//CallLog.info(102,"####### Agency CurrencyId "+toCurrency+"####### Agency CurrencyCode "+agencyCurrCode+"#########  supplierName"+supplierName+"  ####### Supplier CurrencyId "+fromCurrency+"####### Supplier CurrencyCode "+supplierCurrency);
													if(fromCurrency!=null && !fromCurrency.isEmpty() && toCurrency!=null && !toCurrency.isEmpty()){
														if(Integer.parseInt(fromCurrency)==Integer.parseInt(toCurrency)){
															//CallLog.info(102,"####### Both currency are same i. e. "+currId+" id : "+fromCurrency);
															sConfigModel.setCurrencyConversionRate(1.0);
															sConfigModel.setCurrencyRateFound(true);
														}else{
															String currencyRate=corporateService.getCurrencyRate(fromCurrency,toCurrency).getResultString();
															if(currencyRate!=null && !currencyRate.isEmpty()){
																sConfigModel.setCurrencyConversionRate(Double.parseDouble(currencyRate));
																sConfigModel.setCurrencyRateFound(true);
																//CallLog.info(102,"####### Set Currency Rate : "+currencyRate+" For supplier "+supplierName);
															}
															else{
																//supplierConfigModal.setCurrencyRate(Integer.toString(1));
																sConfigModel.setCurrencyRateFound(false);
																//CallLog.info(102,"####### currency rate not found. ");
															}
													}
												}
											}
										}
									}
									supplierCredentialModals = orgManager.getSupplierCredentialById(groupId, p.getProductId(), supplierBean.get(i).getSupplierId(),String.valueOf(credentailId));
									if(null != supplierCredentialModals && !supplierCredentialModals.isEmpty()){
										if(supplierCredentialModals.get(0).length>4 && null != supplierCredentialModals.get(0)[4])
											sConfigModel.setCredentialType((String) supplierCredentialModals.get(0)[4]);
										else
											sConfigModel.setCredentialType("STAGING");
									}
									if (Integer.parseInt(object2[0].toString()) == 0)
									{
										sConfigModel.setPccType("SEARCH");
									}
									else
									{
										sConfigModel.setPccType(TICKETING);
										List<OrganizationTicketPccModal> organizationTicketPccModals = new ArrayList<>();
										OrganizationTicketPccModal tcm;
										List<Object> gdsTicketSupplier = new ArrayList<>();
										if(orgType.equalsIgnoreCase(BRANCH)){
											gdsTicketSupplier = orgManager.gdsTicketingSupplierCredential(groupId,orgType);
										} else {
											List<Object> gdsTicketSupplierAgn = orgManager.gdsTicketingSupplierCredential(groupId,orgType);
											List<Object> gdsTicketSupplierBrn = orgManager.gdsTicketingSupplierCredential(branchId,BRANCH);
											for (Iterator iteratorAgn = gdsTicketSupplierAgn.iterator(); iteratorAgn.hasNext();)
											{
												Object[] objectAgn = (Object[]) iteratorAgn.next();
												Integer credentailIdAgn = Integer.parseInt(objectAgn[0].toString());
												for (Iterator iteratorBrn = gdsTicketSupplierBrn.iterator(); iteratorBrn.hasNext();)
												{
													Object[] objectBrn = (Object[]) iteratorBrn.next();
													Integer credentailIdBrn = Integer.parseInt(objectBrn[0].toString());
													if(credentailIdAgn.equals(credentailIdBrn)) {
														gdsTicketSupplier.add(objectAgn);
													}
												}	
											}	
										}
															
										if (gdsTicketSupplier != null && !gdsTicketSupplier.isEmpty())
										{
											for (Iterator iterator1 = gdsTicketSupplier.iterator(); iterator1.hasNext();)
											{
												Object[] object1 = (Object[]) iterator1.next();
												tcm = new OrganizationTicketPccModal();
												List<CredentialConfigModel> credConfigList1 = new ArrayList<>();
												List<Object> credentials1 = orgManager.fetchCredential(Integer.parseInt(object1[0].toString()));
												if (credentials1 != null && !credentials1.isEmpty())
												{
													CredentialConfigModel cm;
													for (Iterator iterator2 = credentials1.iterator(); iterator2.hasNext();)
													{
														Object[] object21 = (Object[]) iterator2.next();
														cm = new CredentialConfigModel();
														cm.setCredentialName(object21[0].toString());
														cm.setCredentialValue(object21[1].toString());
														credConfigList1.add(cm);
													}
												}
												if(object1[5]!=null){
													int currencyId=(Integer)object1[5];
													List<CurrencyBean> currencyModelList = orgManager.fetchCurrencyById(currencyId);
													if(currencyModelList!=null && !currencyModelList.isEmpty()){
														String supplierCurrency=currencyModelList.get(0).getCurrencyCode();
														if(supplierCurrency!=null && !supplierCurrency.isEmpty()){
															//sConfigModel.setSupplierCurrency(supplierCurrency);
															tcm.setSupplierCurrency(supplierCurrency);
															String fromCurrency=corporateService.getCurrencyId(supplierCurrency).getResultString();
															String toCurrency=corporateService.getCurrencyId(agencyCurrCode).getResultString();
															String supplierName=tcm.getSupplierName();
															//CallLog.info(102,"####### Agency CurrencyId "+toCurrency+"####### Agency CurrencyCode "+agencyCurrCode+"#########  supplierName"+supplierName+"  ####### Supplier CurrencyId "+fromCurrency+"####### Supplier CurrencyCode "+supplierCurrency);
															if(fromCurrency!=null && !fromCurrency.isEmpty() && toCurrency!=null && !toCurrency.isEmpty()){
																if(Integer.parseInt(fromCurrency)==Integer.parseInt(toCurrency)){
																	//CallLog.info(102,"####### Both currency are same i. e. "+currId+" id : "+fromCurrency);
																	tcm.setCurrencyConversionRate(1.0);
																	tcm.setCurrencyRateFound(true);
																}else{
																	String currencyRate=corporateService.getCurrencyRate(fromCurrency,toCurrency).getResultString();
																	if(currencyRate!=null && !currencyRate.isEmpty()){
																		tcm.setCurrencyConversionRate(Double.parseDouble(currencyRate));
																		tcm.setCurrencyRateFound(true);
																		//CallLog.info(102,"####### Set Currency Rate : "+currencyRate+" For supplier "+supplierName);
																	}
																	else{
																		//supplierConfigModal.setCurrencyRate(Integer.toString(1));
																		tcm.setCurrencyRateFound(false);
																		//CallLog.info(102,"####### currency rate not found. ");
																	}
															}
														}
														}
													}
												}
												tcm.setPccType(TICKETING);
												tcm.setPccId(Integer.parseInt(object1[0].toString()));
												tcm.setPaymentGatewayId(0);
												tcm.setPaymentGatewayName("");
												tcm.setSupplierId(supplierBean.get(i).getSupplierId());
												tcm.setSupplierName(supplierBean.get(i).getSupplierName());
												tcm.setCredentialModal(credConfigList1);
												tcm.setAirlineCode(object1[2].toString());
												tcm.setCredentialId(Integer.parseInt(object1[0].toString()));
												tcm.setCredentialName(object1[1].toString());
												tcm.setDestination(object1[4].toString());
												tcm.setOrigin(object1[3].toString());
												organizationTicketPccModals.add(tcm);
											}
										}
										sConfigModel.setOrganizationTicketPccModels(organizationTicketPccModals);
									}
									
									List<CredentialConfigModel> credConfigList = new ArrayList<>();
									
									List<Object> credentials = orgManager.fetchCredential(credentailId);
									if (credentials != null && !credentials.isEmpty())
									{
										CredentialConfigModel cm;
										for (Iterator iterator2 = credentials.iterator(); iterator2.hasNext();)
										{
											Object[] object21 = (Object[]) iterator2.next();
											cm = new CredentialConfigModel();
											cm.setCredentialName(object21[0].toString());
											cm.setCredentialValue(object21[1].toString());
											credConfigList.add(cm);
										}
										
									}
									List<Object> restrictAirline = orgManager.getRestrictedAirlineCredential(Integer.parseInt(object2[1].toString()));
									List<String> confList = new ArrayList<>();
									if (restrictAirline != null && !restrictAirline.isEmpty())
									{
										for (int k = 0; k < restrictAirline.size(); k++)
										{
											if (airlineModels != null)
											{
												for (AirlineModel am : airlineModels)
												{
													if (am.getContentId() == (Integer) restrictAirline.get(k))
													{
														confList.add(am.getAirlineCode());
													}
												}
											}
										}
									}
									sConfigModel.setCredential(credConfigList);
									sConfigModel.setRestrictedAirlines(confList);
									//TTPortalLog.info(102, "SupplierName "+sConfigModel.getSupplierName());
									suppConfigModelList.add(sConfigModel);
								}
							}
							else
							{
								Set<String> crdnCountryNamesSet = new HashSet<>();
								Set<String> crdnCityNames = new HashSet<>();
								  List<Object[]> supplierCredentialModals = new ArrayList<>();
								  List<Object> credentialIdList = null;
								  String credentialId = null;
								  if(orgType.equalsIgnoreCase(BRANCH)){
									  credentialIdList= orgManager.getProductSupplierCredentialId(agencyCode,p.getProductId(),supplierBean.get(i).getSupplierId(),orgType);
									  credentialId  = credentialIdList!=null && !credentialIdList.isEmpty() ? String.valueOf((Integer)credentialIdList.get(0)) : null;
								  } else {
									  List<Object> credentialIdListAgn= orgManager.getProductSupplierCredentialId(agencyCode,p.getProductId(),supplierBean.get(i).getSupplierId(),orgType);
									  List<Object> credentialIdListBrn= orgManager.getProductSupplierCredentialId(branchCode,p.getProductId(),supplierBean.get(i).getSupplierId(),BRANCH);
									  String credentialIdAgn  = credentialIdListAgn!=null && !credentialIdListAgn.isEmpty() ? String.valueOf((Integer)credentialIdListAgn.get(0)) : null;
									  String credentialIdBrn  = credentialIdListBrn!=null && !credentialIdListBrn.isEmpty() ? String.valueOf((Integer)credentialIdListBrn.get(0)) : null;
									  if(credentialIdAgn!=null && credentialIdBrn!=null && credentialIdAgn.equals(credentialIdBrn)) {
										  credentialId = credentialIdAgn;
									  }
								  }
								 
								  
								  if(credentialId!=null){
									  supplierCredentialModals = orgManager.getSupplierCredentialById(groupId, p.getProductId(), supplierBean.get(i).getSupplierId(),credentialId);
									 
									  currencyList= orgManager.getCurrencyCode(credentialId);
									  if(p.getProductName().equalsIgnoreCase(HOTEL) && null != supplierCredentialModals && !supplierCredentialModals.isEmpty())
									  {
										  if(currencyList!=null && !currencyList.isEmpty()) {
											  currencyCode  = String.valueOf(currencyList.get(0));
											  if(currencyCode!=null) {
												  Object[] currObject = currObject = new Object[3];
												  currObject[0] = "currencyCode";
												  currObject[1] = currencyCode;
												  currObject [2] = Integer.parseInt(credentialId);
												  supplierCredentialModals.add(currObject);
											  }
										  }
									  }
								  }
								if(null != supplierCredentialModals && !supplierCredentialModals.isEmpty() && supplierCredentialModals.get(0).length>4)
								{
									SupplierCredentialModal suppCrednModel = (SupplierCredentialModal)supplierCredentialModals.get(0)[5];
									List<HotelModel> hotelModelList = suppCrednModel.getHotelModel();
									if(null != hotelModelList && !hotelModelList.isEmpty())
									{
										for(HotelModel hotelModel : hotelModelList)
										{
											if(null != hotelModel.getCommonCityName() && !hotelModel.getCommonCityName().isEmpty())
												crdnCityNames.add(hotelModel.getCommonCityName());
											if(null != hotelModel.getCountryName() && !hotelModel.getCountryName().isEmpty())
												crdnCountryNamesSet.add(hotelModel.getCountryName());
										}
									}
								}
								SupplierConfigModal sConfigModel = new SupplierConfigModal();
								sConfigModel.setSupplierName(supplierBean.get(i).getSupplierName());
								sConfigModel.setSupplierId(supplierBean.get(i).getSupplierId());
								List<CredentialConfigModel> credConfigList = new ArrayList<>();
								List<Object> restrictAirline = null;
								String credentialCountries = null;
								for (int j = 0; j < supplierCredentialModals.size(); j++)
								{
									Object[] supplierCredentialModelArr = supplierCredentialModals.get(j);
									StringBuilder countriesId = new StringBuilder("");
									StringBuilder countriesName = new StringBuilder("");
									if(supplierCredentialModelArr.length>5 && null != supplierCredentialModelArr[5])
									{
										SupplierCredentialModal suppCrednModel = (SupplierCredentialModal)supplierCredentialModals.get(j)[5];
										if(null != suppCrednModel.getSupplierCredentialCountryModals() && !suppCrednModel.getSupplierCredentialCountryModals().isEmpty())
										{
											List<SupplierCredentialCountryModal> countryList = suppCrednModel.getSupplierCredentialCountryModals();
											int count = 0;
											for(SupplierCredentialCountryModal countryObj : countryList)
											{
												if(count==countryList.size()-1)
													countriesId.append(countryObj.getCountryId());
												else
													countriesId.append(countryObj.getCountryId()+",");
												count++;
											}
											List<CountryBean> countryBeanArr = orgManager.fetchCountryById(countriesId.toString());
											if(null != countryBeanArr && !countryBeanArr.isEmpty())
											{
												for(int x = 0;x<countryBeanArr.size();x++)
												{
													if(x==countryBeanArr.size()-1)
														countriesName.append("'"+countryBeanArr.get(x).getCountryName()+"'");
													else
														countriesName.append("'"+countryBeanArr.get(x).getCountryName()+"',");
												}
											}
										}
									}
									if(null == credentialCountries)
										credentialCountries = countriesName.toString();
									
									
									CredentialConfigModel credConfigModel = new CredentialConfigModel();
									sConfigModel.setPccId((Integer) supplierCredentialModals.get(j)[2]);
									if(supplierCredentialModelArr.length>5 && null != supplierCredentialModelArr[5])
									{
										SupplierCredentialModal suppCrednModel = (SupplierCredentialModal)supplierCredentialModals.get(j)[5];
										sConfigModel.setCredentialName(suppCrednModel.getCredentialName());
									}
									credConfigModel.setCredentialName((String) supplierCredentialModelArr[0]);
									credConfigModel.setCredentialValue((String) supplierCredentialModelArr[1]);
									if(j==0)
									{
										if(supplierCredentialModelArr.length>4 && null != supplierCredentialModelArr[4])
											sConfigModel.setCredentialType((String) supplierCredentialModals.get(j)[4]);
										else
											sConfigModel.setCredentialType("STAGING");
									}
									
									if(!p.getProductName().equalsIgnoreCase(HOTEL) && supplierCredentialModals.get(j)[3]!=null){
										int currencyId=(Integer) supplierCredentialModals.get(j)[3];
										List<CurrencyBean> currencyModelList = orgManager.fetchCurrencyById(currencyId);
										if(currencyModelList!=null && !currencyModelList.isEmpty()){
											String supplierCurrency=currencyModelList.get(0).getCurrencyCode();
											if(supplierCurrency!=null && !supplierCurrency.isEmpty()){
												sConfigModel.setSupplierCurrency(supplierCurrency);
												String fromCurrency=corporateService.getCurrencyId(supplierCurrency).getResultString();
												String toCurrency=corporateService.getCurrencyId(agencyCurrCode).getResultString();
												String supplierName=sConfigModel.getSupplierName();
												//CallLog.info(102,"####### Agency CurrencyId "+toCurrency+"####### Agency CurrencyCode "+agencyCurrCode+"#########  supplierName"+supplierName+"  ####### Supplier CurrencyId "+fromCurrency+"####### Supplier CurrencyCode "+supplierCurrency);
												if(fromCurrency!=null && !fromCurrency.isEmpty() && toCurrency!=null && !toCurrency.isEmpty()){
													if(Integer.parseInt(fromCurrency)==Integer.parseInt(toCurrency)){
														//CallLog.info(102,"####### Both currency are same i. e. "+currId+" id : "+fromCurrency);
														sConfigModel.setCurrencyConversionRate(1.0);
														sConfigModel.setCurrencyRateFound(true);
													}else{
														String currencyRate=corporateService.getCurrencyRate(fromCurrency,toCurrency).getResultString();
														if(currencyRate!=null && !currencyRate.isEmpty()){
															sConfigModel.setCurrencyConversionRate(Double.parseDouble(currencyRate));
															sConfigModel.setCurrencyRateFound(true);
															//CallLog.info(102,"####### Set Currency Rate : "+currencyRate+" For supplier "+supplierName);
														}
														else{
															sConfigModel.setCurrencyRateFound(false);
															//CallLog.info(102,"####### currency rate not found. ");
														}
												}
											}
											}
										}
									}
									
									if (j == 0 && p.getProductName().equalsIgnoreCase(FLIGHT))
									{
										restrictAirline = orgManager.getRestrictedAirlineCredential((Integer) supplierCredentialModals.get(j)[2]);
									}
									credConfigList.add(credConfigModel);
								}
								sConfigModel.setCountriesName(credentialCountries);
								sConfigModel.setCountriesNameSet(crdnCountryNamesSet);
								sConfigModel.setCitiesName(crdnCityNames);
								if (p.getProductName().equalsIgnoreCase(FLIGHT))
								{
									List<String> confList = new ArrayList<>();
									if (restrictAirline != null && !restrictAirline.isEmpty())
									{
										for (int k = 0; k < restrictAirline.size(); k++)
										{
											if (airlineModels != null)
											{
												for (AirlineModel am : airlineModels)
												{
													if (am.getContentId() == (Integer) restrictAirline.get(k))
													{
														confList.add(am.getAirlineCode());
													}
												}
											}
										}
									}
									sConfigModel.setRestrictedAirlines(confList);
								}
								sConfigModel.setCredential(credConfigList);
								TTPortalLog.info(102, "SupplierName "+sConfigModel.getSupplierName());
								suppConfigModelList.add(sConfigModel);
							}
						}
						productConfigModel.setSupplier(suppConfigModelList);
						productConfigList.add(productConfigModel);
					}
				}
				resultBean.setResultList(productConfigList);
			}
		}
		catch (Exception e)
		{
			TTLog.printStackTrace(15,e);
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}
	

	@SuppressWarnings("rawtypes")
	public ResultBean getRBDListCountryWise(int countryId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<RbdModel> rbdModel = new ArrayList<>();
			RbdModel rbd;
			List<Object> oRbd = orgManager.getRBDListCountryWise(countryId);
			if (oRbd != null && !oRbd.isEmpty())
			{
				for (Iterator iterator = oRbd.iterator(); iterator.hasNext();)
				{
					Object[] object = (Object[]) iterator.next();
					rbd = new RbdModel();
					rbd.setCabinType(object[0]!=null? Integer.parseInt(object[0].toString()) : 0);
					rbd.setRbdType(object[1]!=null? object[1].toString() : "");
					rbd.setAirlineId(object[2]!=null? Integer.parseInt(object[2].toString()) : 0);
					rbd.setAirlineName(object[3]!=null? object[3].toString() : "");
					rbdModel.add(rbd);
				}
			}
			resultBean.setResultList(rbdModel);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}

	public ResultBean searchParticularAirport(AirportModal airportModel)
	{
		ResultBean resultBean = new ResultBean();
		String responseJson = null;
		try
		{
			String flightSearchKey = com.tt.ts.rest.common.util.CommonUtil.prepareKeyForAutoSuggest(airportModel.getAirportCode());
			Boolean isSearchKeyFound = false;
			ResultBean resultBeanCacheKey = RedisCacheUtil.isSearchKeyInCacheFlight(flightSearchKey, redisService);
			if (resultBeanCacheKey != null && !resultBeanCacheKey.isError())
			{
				isSearchKeyFound = resultBeanCacheKey.getResultBoolean();
			}
			if (isSearchKeyFound)
			{
				ResultBean resultBeanCache = RedisCacheUtil.getResponseFromCacheFlight(flightSearchKey, redisService);
				if (resultBeanCache != null && !resultBeanCache.isError())
				{
					responseJson = resultBeanCache.getResultString();
				}
				else if (null != airportModel.getAirportCode() && !"".equalsIgnoreCase(airportModel.getAirportCode()))
				{
					responseJson = airportDataForAutoSuggest(airportModel, flightSearchKey);
				}
			}
			else
			{
				responseJson = airportDataForAutoSuggest(airportModel, flightSearchKey);
			}
			resultBean.setIserror(false);
			resultBean.setResultString(responseJson);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}

	public String airportDataForAutoSuggest(AirportModal airportModel, String flightSearchKey) throws Exception
	{
		String responseJson = null;
		List<AirportModal> airportModal = new ArrayList<>();
		List<Object[]> airportData = orgManager.searchAirportData(airportModel);
		if (airportData != null && !airportData.isEmpty())
		{
			AirportModal am;
			for (Object[] airmodal : airportData)
			{

				am = new AirportModal();
				am.setAirportId(Integer.parseInt(airmodal[0].toString()));
				am.setAirportCode(airmodal[1].toString());
				am.setAirportName(airmodal[2].toString());
				am.setCountryName(airmodal[3].toString());
				am.setCityName(airmodal[4].toString());
				am.setCountryID(Integer.parseInt(airmodal[5].toString()));
				am.setCityId(Integer.parseInt(airmodal[6].toString()));
				am.setUpdatedCountryName(airmodal[7].toString());
				am.setStationType(airmodal[8].toString());
				airportModal.add(am);
			}
		}
		if (!airportModal.isEmpty())
		{
			responseJson = CommonUtil.convertObjectIntoJson(airportModal);
		}
		if (null != responseJson && !responseJson.isEmpty())
		{
			Integer expiryTimeCache = Integer.parseInt(messageSource.getMessage(AIRPORT_AUTOSUGGEST_EXPIRE_TIME, null ,"30", null));
			RedisCacheUtil.setResponseInCacheFlight(flightSearchKey, responseJson, redisService,expiryTimeCache);
		}
		return responseJson;
	}

	public ResultBean getAirlinesByAgencyId(Integer groupId, Integer domOrIntl)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<AirlineModel> airlineModal = orgManager.getAirlinesByAgencyId(groupId, domOrIntl);

			resultBean.setResultList(airlineModal);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}

	public ResultBean getProfileImage(Integer groupId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<OrganizationDocumentModel> objectData = orgManager.getProfileImage(groupId);
			resultBean.setResultList(objectData);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean getMarkupRecordByUser(Integer organizationId, int corpOrRetail, Integer corpId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object> objectData = orgManager.getMarkupRecordByUser(organizationId, corpOrRetail, corpId);
			resultBean.setResultList(objectData);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}

	/*public ResultBean gdsSearchSupplierCredential(Integer groupId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object> objectData = orgManager.gdsSearchSupplierCredential(groupId);
			resultBean.setResultList(objectData);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}*/

	/*public ResultBean gdsTicketingSupplierCredential(Integer groupId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object> objectData = orgManager.gdsTicketingSupplierCredential(groupId);
			resultBean.setResultList(objectData);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_ORG;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(15, errorCode, e);
		}
		return resultBean;
	}*/

	public Date convertStringIntoDate(String d)
	{
		Date startDate = null;
		try
		{
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			startDate = formatter.parse(d);
		}
		catch (Exception e)
		{
			TTLog.printStackTrace(0, e);
		}

		return startDate;
	}
	public ResultBean fetchDealCode(String countryId, int pccId){
		ResultBean resultBean=new ResultBean();
		try {
			List<Object> objectDataa = orgManager.fetchDealDetails(Integer.parseInt(countryId), pccId);
			resultBean.setResultList(objectDataa);
		} catch (Exception e) {
			TTLog.printStackTrace(0, e);
		}
		return resultBean;
	}
	@SuppressWarnings("rawtypes")
	public ResultBean getGdsSupplierList(String agencyId)
	{
		ResultBean resultBean = new ResultBean();
		if (agencyId != null)
		{
			List<SupplierModal> supplierList = new ArrayList<>();
			List<SupplierModal> gdsSupplier = new ArrayList<>();
			SupplierModal supplierModel = null;
			try
			{
				supplierList = orgManager.getGdsSupplierList(agencyId);
				if (supplierList != null && !supplierList.isEmpty())
				{
					for (Iterator iteratorr = supplierList.iterator(); iteratorr.hasNext();)
					{
						Object[] objectt = (Object[]) iteratorr.next();
						supplierModel = new SupplierModal();
						supplierModel.setSupplierId((Integer) objectt[0]);
						supplierModel.setSupplierName((String) objectt[1]);
						gdsSupplier.add(supplierModel);
					}
					resultBean.setResultList(gdsSupplier);
				}
			}
			catch (Exception e)
			{
				resultBean.setIserror(true);
				TTLog.error(0, e);
			}
		}
		return resultBean;
	}

	public ResultBean getRestrictedAirlineListByAgengy(String agencyId)
	{
		ResultBean resultBean = new ResultBean();
		if (agencyId != null)
		{
			List<OrganizationAirlineCodeModal> organizationAirlineCodeModal;
			try
			{
				organizationAirlineCodeModal = orgManager.getRestrictedAirlineListByAgengy(agencyId);
				resultBean.setResultList(organizationAirlineCodeModal);
			}
			catch (Exception e)
			{
				resultBean.setIserror(true);
				TTLog.error(0, e);
			}
		}
		return resultBean;
	}

	public ResultBean getAgentType(Integer userId)
	{
		ResultBean resultBean = new ResultBean();
		if (userId != null)
		{
			List<Object> o = new ArrayList<>();
			try
			{
				o = orgManager.getAgentType(userId);
				resultBean.setResultInteger(Integer.parseInt(!o.isEmpty() ? o.get(0).toString() : "0"));
			}
			catch (Exception e)
			{
				resultBean.setIserror(true);
				TTLog.error(0, e);
			}
		}
		return resultBean;
	}

	@SuppressWarnings("unchecked")
	public ResultBean searchFlightTag(FlightTagModel flightTagModel, ResultBean airlineResultBean)
	{
		List<FlightTagModel> searchFlightTag = null;
		ResultBean resultBean = new ResultBean();
		try
		{
			List<AirportModal> airportModals = airportManager.fetchAirportConstructorWise();
			searchFlightTag = orgManager.searchFlightTag(flightTagModel);
			if (searchFlightTag != null && !searchFlightTag.isEmpty())
			{
				for (FlightTagModel fliModel : searchFlightTag)
				{
					List<AirlineModel> airlineModels = (List<AirlineModel>) airlineResultBean.getResultList();
					fliModel.setAirlineName(CommonUtil.fetchAirlineCodeById(airlineModels, fliModel.getAirLineId()));
					List<com.tt.ts.flighttag.model.TagFlightCityModel> flightCityList = fliModel.getFlightCityList();
					if (flightCityList != null && !flightCityList.isEmpty())
					{
						for (com.tt.ts.flighttag.model.TagFlightCityModel tagFlightCityModel : flightCityList)
						{
							tagFlightCityModel.setAirportCode(com.tt.ts.rest.common.util.CommonUtil.fetchAirportCodeById(airportModals, tagFlightCityModel.getAirportId()));		
						}
					}
				}
				resultBean.setIserror(false);
				resultBean.setResultList((List<FlightTagModel>) searchFlightTag);
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_SEARCH_FLIGHTTAG;
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode));
			TTLog.printStackTrace(17, errorCode, e);
		}
		return resultBean;
	}

	@SuppressWarnings("unchecked")
	public ResultBean searchBlackOutFlight(BlackOutFlightModel blackOutFlightModel, ResultBean airlineResultBean)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<BlackOutFlightModel> blackOutFlightModelList;
			blackOutFlightModelList = orgManager.searchBlackOutFlight(blackOutFlightModel);
			ResultBean airBean = airlineResultBean;
			List<AirlineModel> airlineModels = (List<AirlineModel>) airBean.getResultList();
			if (blackOutFlightModelList != null && !blackOutFlightModelList.isEmpty())
			{
				List<AirportModal> airportModal = airportManager.fetchAirportConstructorWise();
				for (BlackOutFlightModel blackOutFlight : blackOutFlightModelList)
				{
					blackOutFlight.setAirlineName(CommonUtil.fetchAirlineCodeById(airlineModels, blackOutFlight.getAirlineId()));
					List<BlackListFlightCityModel> blackListFlightCityModels = blackOutFlight.getBlackCityList();
					if (blackListFlightCityModels != null && !blackListFlightCityModels.isEmpty())
					{
						for (BlackListFlightCityModel blackListFlightCityModel : blackListFlightCityModels)
						{
							blackListFlightCityModel.setAirportCode(com.tt.ts.rest.common.util.CommonUtil.fetchAirportCodeById(airportModal, blackListFlightCityModel.getAirportId()));
						}
					}
				}
				resultBean.setIserror(false);
				resultBean.setResultList((List<BlackOutFlightModel>) blackOutFlightModelList);
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_SEARCH_BLACKOUTFLT;
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode));
			TTLog.printStackTrace(17, errorCode, e);
		}
		return resultBean;
	}

	@SuppressWarnings("unchecked")
	public ResultBean getBspList(FlightBookingRequestBean flightBookingRequestBean,int pccId,FlightRestService flightRestService)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<BspCommissionModel> bspModelList;
			ResultBean resultBean2 = airlineService.getAirlinesNew(new AirlineModel());
			List<AirlineModel> airlineModels = (List<AirlineModel>) resultBean2.getResultList();
			int airlineId = com.tt.ts.rest.common.util.CommonUtil.fetchAirlineIdByCode(airlineModels, flightBookingRequestBean.getOnwardFlightOption().getPlatingCarrier());

			BspCommissionModel bspCommissionModel = new BspCommissionModel();
			bspCommissionModel.setTicketPccId(pccId);
			bspCommissionModel.setAirlineId(airlineId);
			bspCommissionModel.setStatus(1);
			bspCommissionModel.setApprovalStatus(1);
			String tripType = flightBookingRequestBean.getTripType()!=null && !flightBookingRequestBean.getTripType().isEmpty()?flightBookingRequestBean.getTripType():"";
			Integer journeyType=-1;
			if(!tripType.isEmpty())
				journeyType=tripType.equalsIgnoreCase(ONEWAY)?1:tripType.equalsIgnoreCase(ROUNDTRIP)?2:tripType.equalsIgnoreCase(MULTICITY)?3:-1;
			bspCommissionModel.setJourneyType(journeyType);
			if(flightBookingRequestBean.getNoOfAdults()>0 && flightBookingRequestBean.getNoOfChilds()>0){
				bspCommissionModel.setHasAdult(true);
				bspCommissionModel.setHasChild(true);
			}
			else if(flightBookingRequestBean.getNoOfAdults()>0){
				bspCommissionModel.setHasAdult(true);
			}
			else if(flightBookingRequestBean.getNoOfChilds()>0){
				bspCommissionModel.setHasChild(true);
			}
			List<BspCommissionModel> newBspCommissionModal=new ArrayList<>();
			bspModelList = orgManager.getBspCommissionList(bspCommissionModel);
			TTLog.info(18, "Booking REf:: "+flightBookingRequestBean.getFbBookingReference()+" >>> TripType for BSP is::::::::::::"+flightBookingRequestBean.getTripType()+":::::::::::Origin::::::"+flightBookingRequestBean.getOrigin()+":::::Destination is::::::"+flightBookingRequestBean.getDestination());
			if (bspModelList != null && !bspModelList.isEmpty())
			{
				TTLog.info(18, "bspModelList size is:::::::"+bspModelList.size());
				for (BspCommissionModel bsp : bspModelList)
				{
					bsp.setAirlineName(CommonUtil.fetchAirlineCodeById(airlineModels, bsp.getAirlineId()));
				}
			}
			if(tripType.equalsIgnoreCase(ONEWAY) || tripType.equalsIgnoreCase(ROUNDTRIP)){
				
				String originAirportCode = flightBookingRequestBean.getOnwardFlightOption().getOrigin();
				List<AirportModal> airportModalListOrigin = flightRestService.getAirportModelListByAirportCode(originAirportCode);
				if (airportModalListOrigin != null && !airportModalListOrigin.isEmpty())
				{
					AirportModal airportModal = airportModalListOrigin.get(0);
					flightBookingRequestBean.getOnwardFlightOption().setOriginId(String.valueOf(airportModal.getAirportId()));
				}
				String destinationAirportCode = flightBookingRequestBean.getOnwardFlightOption().getDestination();
				List<AirportModal> airportModalListDestination = flightRestService.getAirportModelListByAirportCode(destinationAirportCode);
				if (airportModalListDestination != null && !airportModalListDestination.isEmpty())
				{
					AirportModal airportModal = airportModalListDestination.get(0);
					flightBookingRequestBean.getOnwardFlightOption().setDestinationId(String.valueOf(airportModal.getAirportId()));
				}
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;
				
				boolean isOriginCityFound = false;
				boolean isDestinationCityFound = false;
				
				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				
				if (bspModelList != null && !bspModelList.isEmpty())
				{
					for (BspCommissionModel eachBSPModel : bspModelList)
					{
						 isOriginAirportFound = false;
						 isDestinationAirportFound = false;
						
						 isOriginCityFound = false;
						 isDestinationCityFound = false;
						
						 isOriginCountryFound = false;
						 isDestinationCountryFound = false;
				
						 List<BspAirportModel> bspAirPortList = eachBSPModel.getBspAirportList();
						 List<BspCityModel> bspCityModelList = eachBSPModel.getBspCityList();
						 List<BspCountryModel> bspCountryModelList = eachBSPModel.getBspCountryList();
						 
						 if(bspAirPortList != null && !bspAirPortList.isEmpty()){
								for(BspAirportModel eachAirportModal : bspAirPortList){
									
									if(flightBookingRequestBean.getOnwardFlightOption().getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && !eachAirportModal.getIsSource())
										isOriginAirportFound = true;
									
									if(flightBookingRequestBean.getOnwardFlightOption().getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource())
										isDestinationAirportFound = true;
									
								}
								
								TTLog.info(18,"Booking REf:: "+flightBookingRequestBean.getFbBookingReference()+" >>>isOriginAirportFound = "+isOriginAirportFound);
								TTLog.info(18,"isDestinationAirportFound = "+isDestinationAirportFound);
								
								if(isOriginAirportFound && isDestinationAirportFound) {
									eachBSPModel.setAirportBspModal(true);
								}
						 }
						 if(bspCityModelList != null && !bspCityModelList.isEmpty()){
								
							 TTLog.info(18,"origin city = "+flightBookingRequestBean.getOnwardFlightOption().getOriginCity());
							 TTLog.info(18,"destination city = "+flightBookingRequestBean.getOnwardFlightOption().getDestinationCity());
								
								for(BspCityModel eachCityModal : bspCityModelList){
									
									if(flightBookingRequestBean.getOnwardFlightOption().getOriginCity().equalsIgnoreCase(String.valueOf(eachCityModal.getCityId())) && !eachCityModal.getIsSource())
										isOriginCityFound = true;
									
									if(flightBookingRequestBean.getOnwardFlightOption().getDestinationCity().equalsIgnoreCase(String.valueOf(eachCityModal.getCityId())) && eachCityModal.getIsSource())
										isDestinationCityFound = true;
									
								}
								TTLog.info(18,"isOriginCityFound = "+isOriginCityFound);
								TTLog.info(18,"isDestinationCityFound = "+isDestinationCityFound);
								
								if(isOriginCityFound && isDestinationCityFound){
									eachBSPModel.setCityBspModal(true);
								}
						}
						 if(bspCountryModelList != null && !bspCountryModelList.isEmpty()){
								
							 TTLog.info(18,"origin country = "+flightBookingRequestBean.getOnwardFlightOption().getOriginCountry());
							 TTLog.info(18,"destination country = "+flightBookingRequestBean.getOnwardFlightOption().getDestinationCountry());
								for(BspCountryModel eachCountryModal : bspCountryModelList){
									if(flightBookingRequestBean.getOnwardFlightOption().getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && !eachCountryModal.getIsSource())
										isOriginCountryFound = true;
									
									if(flightBookingRequestBean.getOnwardFlightOption().getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource())
										isDestinationCountryFound = true;
								}
								
								TTLog.info(18,"isOriginCountryFound = "+isOriginCountryFound);
								TTLog.info(18,"isDestinationCountryFound = "+isDestinationCountryFound);
								
								if(isOriginCountryFound && isDestinationCountryFound){
									eachBSPModel.setCountryBspModal(true);
								}
						 }
						 
						 if(eachBSPModel.isAirportBspModal() || eachBSPModel.isCityBspModal() || eachBSPModel.isCountryBspModal()){
							 newBspCommissionModal.add(eachBSPModel);
						 }
					}
				}
			}
			else if(tripType.equalsIgnoreCase(MULTICITY)){
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;
				boolean isOriginCityFound = false;
				boolean isDestinationCityFound = false;
				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				if (bspModelList != null && !bspModelList.isEmpty())
				{
					for (BspCommissionModel eachBSPModel : bspModelList)
					{
						 List<BspAirportModel> bspAirPortList = eachBSPModel.getBspAirportList();
						 List<BspCityModel> bspCityModelList = eachBSPModel.getBspCityList();
						 List<BspCountryModel> bspCountryModelList = eachBSPModel.getBspCountryList();
						 
						 if(bspAirPortList != null && !bspAirPortList.isEmpty()){
								for(OptionSegmentBean eachOptionSegment : flightBookingRequestBean.getOnwardFlightOption().getOptionSegmentBean()){
									String originAirportCode = eachOptionSegment.getOrigin();
									List<AirportModal> airportModalListOrigin = flightRestService.getAirportModelListByAirportCode(originAirportCode);
									if (airportModalListOrigin != null && !airportModalListOrigin.isEmpty())
									{
										AirportModal airportModal = airportModalListOrigin.get(0);
										eachOptionSegment.setOriginId(String.valueOf(airportModal.getAirportId()));
									}
									String destinationAirportCode = eachOptionSegment.getDestination();
									List<AirportModal> airportModalListDestination = flightRestService.getAirportModelListByAirportCode(destinationAirportCode);
									if (airportModalListDestination != null && !airportModalListDestination.isEmpty())
									{
										AirportModal airportModal = airportModalListDestination.get(0);
										eachOptionSegment.setDestinationId(String.valueOf(airportModal.getAirportId()));
									}
									
									for(BspAirportModel eachAirportModal : bspAirPortList){
										 isOriginAirportFound = false;
										 isDestinationAirportFound = false;
										 if(eachOptionSegment.getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && !eachAirportModal.getIsSource())
											 isOriginAirportFound = true;
										
										 if(eachOptionSegment.getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource())
											 isDestinationAirportFound = true;
										 
									}
								 	TTLog.info(18,"isOriginAirportFound = "+isOriginAirportFound);
									TTLog.info(18,"isDestinationAirportFound = "+isDestinationAirportFound);
									if(isOriginAirportFound && isDestinationAirportFound) {
										eachBSPModel.setAirportBspModal(true);
										break;
									}
								}
						 }
						 if(bspCityModelList != null && !bspCityModelList.isEmpty()){
							 TTLog.info(18,"origin city = "+flightBookingRequestBean.getOnwardFlightOption().getOriginCity());
							 TTLog.info(18,"destination city = "+flightBookingRequestBean.getOnwardFlightOption().getDestinationCity());
								for(OptionSegmentBean eachOptionSegment : flightBookingRequestBean.getOnwardFlightOption().getOptionSegmentBean()){
									isOriginCityFound = false;
									isDestinationCityFound = false;
									for(BspCityModel eachCityModal : bspCityModelList){
										if(eachOptionSegment.getOriginCity().equalsIgnoreCase(String.valueOf(eachCityModal.getCityId())) && !eachCityModal.getIsSource())
											isOriginCityFound = true;
										
										if(eachOptionSegment.getDestinationCity().equalsIgnoreCase(String.valueOf(eachCityModal.getCityId())) && eachCityModal.getIsSource())
											isDestinationCityFound = true;
									}
									TTLog.info(18,"isOriginCityFound = "+isOriginCityFound);
									TTLog.info(18,"isDestinationCityFound = "+isDestinationCityFound);
									
									if(isOriginCityFound && isDestinationCityFound){
										eachBSPModel.setCityBspModal(true);
										break;
									}
								}
						}
						 if(bspCountryModelList != null && !bspCountryModelList.isEmpty()){
								
							 TTLog.info(18,"origin country = "+flightBookingRequestBean.getOnwardFlightOption().getOriginCountry());
							 TTLog.info(18,"destination country = "+flightBookingRequestBean.getOnwardFlightOption().getDestinationCountry());
								for(OptionSegmentBean eachOptionSegment : flightBookingRequestBean.getOnwardFlightOption().getOptionSegmentBean()){
									isOriginCountryFound = false;
									isDestinationCountryFound = false;
									
									for(BspCountryModel eachCountryModal : bspCountryModelList){
										if(eachOptionSegment.getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && !eachCountryModal.getIsSource())
											isOriginCountryFound = true;
										
										if(eachOptionSegment.getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource())
											isDestinationCountryFound = true;
									}
									TTLog.info(18,"isOriginCountryFound = "+isOriginCountryFound);
									TTLog.info(18,"isDestinationCountryFound = "+isDestinationCountryFound);
									
									if(isOriginCountryFound && isDestinationCountryFound){
										eachBSPModel.setCountryBspModal(true);
										break;
									}
								}
						 }
						 if(eachBSPModel.isAirportBspModal() || eachBSPModel.isCityBspModal() || eachBSPModel.isCountryBspModal()){
							 newBspCommissionModal.add(eachBSPModel);
						 }
					}
				}
			}
			resultBean.setIserror(false);
			resultBean.setResultList((List<BspCommissionModel>) newBspCommissionModal);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_BSP_COMMISSION;
			TTLog.printStackTrace(18, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean searchKnowledgeCentersData(KnowledgeCenterModel kcModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			resultBean.setIserror(false);
			List<KnowledgeCenterModel> kcCenterModels = orgManager.searchKnowledgeCenters(kcModel);

			resultBean.setResultList(kcCenterModels);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_KC;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(22, errorCode, e);
		}
		return resultBean;
	}

	@SuppressWarnings("unchecked")
	public ResultBean getUccfData(UCCFModal uccfModal, int sourceCityId, int destinationCityId, String marketingCarrier, String bookingReference)
	{
		ResultBean resultBean = new ResultBean();
		String bookingRefId = "[" + bookingReference + " getUccfData()]";
		try
		{
			resultBean.setIserror(true);
			ResultBean resultBean2 = airlineService.getAirlinesNew(new AirlineModel());
			List<AirlineModel> airlineModels = (List<AirlineModel>) resultBean2.getResultList();
			uccfModal.setAirlineId(com.tt.ts.rest.common.util.CommonUtil.fetchAirlineIdByCode(airlineModels, marketingCarrier));
			CallLog.info(15, bookingRefId + "AirlineId= " + uccfModal.getAirlineId());
			if (uccfModal.getAirlineId() != 0)
			{
				uccfModal.setStatus(1);
				uccfModal.setApprovalStatus(1);
				List<UCCFModal> uccfModals = uccfManager.searchUccf(uccfModal);
				if (!uccfModals.isEmpty())
				{
					CallLog.info(15, bookingRefId + "uccfModalData size=" + uccfModals.size());
					for (UCCFModal uccfModalData : uccfModals)
					{
						List<UCCFCityMapping> uccfSourceCityMappings = uccfManager.fetchUCCFSourceCityMapping(uccfModalData.getUccfId(), 0);
						List<UCCFCityMapping> uccfDestinationCityMappings = uccfManager.fetchUCCFDestinationCityMapping(uccfModalData.getUccfId(), 1);

						if (uccfSourceCityMappings.contains(sourceCityId) && uccfDestinationCityMappings.contains(destinationCityId))
						{
							CallLog.info(15, bookingRefId + "Inside contains check ");
							uccfModal.setUccfId(uccfModalData.getUccfId());
							uccfModal.setAppliedOnTotal(uccfModalData.getAppliedOnTotal());
							uccfModal.setIsPercent(uccfModalData.getIsPercent());
							uccfModal.setUccfCharge(uccfModalData.getUccfCharge());
							resultBean.setIserror(false);
							resultBean.setResultObject(uccfModal);
							break;
						}
					}
				}
				else
				{
					resultBean.setIserror(true);
				}
			}
			else
				resultBean.setIserror(true);
		}
		catch (Exception e)
		{

			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_UCCF;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(16, errorCode, e);
		}
		return resultBean;
	}

	public ResultBean setUCCFPaymentDetails(UCCFModal uccfModal, FlightBookingRequestBean bookingRequestBean)
	{
		PaymentRequest paymentRequestData = bookingRequestBean.getPaymentRequest();
		if(null!=paymentRequestData)
			paymentRequestData.setUCCFPayment(true);
			else{
				paymentRequestData =new PaymentRequest();
				paymentRequestData.setUCCFPayment(true);
			}
		String bookingRefId = "[" + bookingRequestBean.getFbBookingReference() + " setUCCFPaymentDetails()]";
		ResultBean resultBean = new ResultBean();
		double uccfTxnCharge = 0.0;
		double totalAmount = 0.0;
		double totalBaseFare = 0.0;
		resultBean.setIserror(false);

		try
		{

			if (bookingRequestBean.getTripType().equalsIgnoreCase(ROUNDTRIP))
			{
				totalAmount = bookingRequestBean.getRoundTripFlightOption().getTotalJourneyFare();
				totalBaseFare = bookingRequestBean.getRoundTripFlightOption().getTotalBaseFare();
			}
			else
			{
				totalAmount = bookingRequestBean.getOnwardFlightOption().getTotalFare();
				totalBaseFare = bookingRequestBean.getOnwardFlightOption().getFlightFare().getTotalBaseFare();
			}

			CallLog.info(15, bookingRefId + "Total Amount= " + totalAmount);
			CallLog.info(15, bookingRefId + "TotalBaseFare= " + totalBaseFare);

			int uccfId = uccfModal.getUccfId();
			int appliedOnTotal = uccfModal.getAppliedOnTotal();
			int isPercent = uccfModal.getIsPercent();
			BigDecimal uccfCharge = uccfModal.getUccfCharge();

			CallLog.info(15, bookingRefId + "UccfId= " + uccfId);
			CallLog.info(15, bookingRefId + "AppliedOnTotal= " + appliedOnTotal);
			CallLog.info(15, bookingRefId + "IsPercent= " + isPercent);
			CallLog.info(15, bookingRefId + "UccfCharge= " + uccfCharge);

			if (isPercent == 0)
			{ // If percentage is applied

				if (appliedOnTotal == 0)
				{ // If applied on total
					uccfTxnCharge = (totalAmount * uccfCharge.doubleValue()) / 100;
					paymentRequestData.setUccfTxnOn(0);
					paymentRequestData.setUccfTxnCharge(uccfTxnCharge);
				}
				else
				{ // If applied on base fare
					uccfTxnCharge = (totalBaseFare * uccfCharge.doubleValue()) / 100;
					paymentRequestData.setUccfTxnOn(1);
					paymentRequestData.setUccfTxnCharge(uccfTxnCharge);
				}
			}
			else
			{ // Amount

				uccfTxnCharge = uccfModal.getUccfCharge().doubleValue();
				paymentRequestData.setUccfTxnOn(2);
				paymentRequestData.setUccfTxnCharge(uccfTxnCharge);
			}
			CallLog.info(15, bookingRefId + "Calculated UccfCharge= " + uccfTxnCharge);
			resultBean.setResultObject(paymentRequestData);

		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_UCCF;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			CallLog.info(15, bookingRefId + "-Exception occured");
			TTLog.printStackTrace(16, errorCode, e);
		}
		return resultBean;
	}

	public double getUCCFTxnCharge(UCCFModal uccfModal, FlightBookingRequestBean bookingRequestBean)
	{
		PaymentRequest paymentRequestData = bookingRequestBean.getPaymentRequest();
		if(null!=paymentRequestData)
			paymentRequestData.setUCCFPayment(true);
			else{
				paymentRequestData =new PaymentRequest();
				paymentRequestData.setUCCFPayment(true);
			}
		String bookingRefId = "[" + bookingRequestBean.getFbBookingReference() + " getUCCFTxnCharge()]";
		double uccfTxnCharge = 0.0;
		double totalAmount = 0.0;
		double totalBaseFare = 0.0;
		try
		{

			if (bookingRequestBean.getTripType().equalsIgnoreCase(ROUNDTRIP))
			{
				totalAmount = bookingRequestBean.getRoundTripFlightOption().getTotalJourneyFare();
				totalBaseFare = bookingRequestBean.getRoundTripFlightOption().getTotalBaseFare();
			}
			else
			{
				totalAmount = bookingRequestBean.getOnwardFlightOption().getTotalFare();
				totalBaseFare = bookingRequestBean.getOnwardFlightOption().getFlightFare().getTotalBaseFare();
			}

			CallLog.info(15, bookingRefId + "Total Amount= " + totalAmount);
			CallLog.info(15, bookingRefId + "TotalBaseFare= " + totalBaseFare);

			int uccfId = uccfModal.getUccfId();
			int appliedOnTotal = uccfModal.getAppliedOnTotal();
			int isPercent = uccfModal.getIsPercent();
			BigDecimal uccfCharge = uccfModal.getUccfCharge();

			CallLog.info(15, bookingRefId + "UccfId= " + uccfId);
			CallLog.info(15, bookingRefId + "AppliedOnTotal= " + appliedOnTotal);
			CallLog.info(15, bookingRefId + "IsPercent= " + isPercent);
			CallLog.info(15, bookingRefId + "UccfCharge= " + uccfCharge);

			if (isPercent == 0)
			{ // If percentage is applied

				if (appliedOnTotal == 0)
				{ // If applied on total
					uccfTxnCharge = (totalAmount * uccfCharge.doubleValue()) / 100;
					CallLog.info(15, bookingRefId + "UccfCharge is percent= " + uccfTxnCharge);
				}
				else
				{ // If applied on base fare
					uccfTxnCharge = (totalBaseFare * uccfCharge.doubleValue()) / 100;
					CallLog.info(15, bookingRefId + "totalBaseFare uccfTxnCharge is percent= " + uccfTxnCharge);
				}
			}
			else
			{ // Amount

				uccfTxnCharge = uccfModal.getUccfCharge().doubleValue();
				CallLog.info(15, bookingRefId + "UccfCharge amount= " + uccfTxnCharge);
			}

		}
		catch (Exception e)
		{
			String errorCode = ErrorCodeContant.DATA_NOT_FOUND_UCCF;
			CallLog.info(15, bookingRefId + "-Exception occured");
			TTLog.printStackTrace(16, errorCode, e);
		}
		return uccfTxnCharge;
	}

	public ResultBean fetchCountry(CountryBean countryBean)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<CountryBean> countryBeans = orgManager.fetchCountryList(countryBean);

			resultBean.setResultList(countryBeans);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FETCH_COUNTRY;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(16, errorCode, e);
		}
		return resultBean;
	}

	@SuppressWarnings("rawtypes")
	public ResultBean getAllAirportsCountryCity(AirportModal airportModel)
	{
		ResultBean resultBean = new ResultBean();
		List<AutoSuggestCountry> countryBeans = new ArrayList<>();
		try
		{
			List<Object> o = orgManager.getAllAirportsCountryCity(airportModel);

			List<String> s = new ArrayList<>();

			if (o != null && !o.isEmpty())
			{

				AutoSuggestCountry c;
				for (Iterator iteratorr = o.iterator(); iteratorr.hasNext();)
				{
					Object[] objectt = (Object[]) iteratorr.next();
					c = new AutoSuggestCountry();
					c.setCountryCode((String) objectt[2]);
					c.setCountryName((String) objectt[1]);
					c.setCityCode((String) objectt[0]);
					c.setCityName((String) objectt[3]);
					c.setCurrency((String) objectt[4]);
					c.setCountryCodeIso((String) objectt[5]);
					c.setCountryId((Integer) objectt[6]);
					if (!s.contains(c.getCountryCode() + "-" + c.getCountryName() + "-" + c.getCityCode()))
					{
						s.add(c.getCountryCode() + "-" + c.getCountryName() + "-" + c.getCityCode());
						countryBeans.add(c);
					}

				}
				s = null;
			}
			resultBean.setResultList(countryBeans);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(16, e);
		}
		return resultBean;
	}

	public ResultBean getEnumListByValue(String columnName)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			resultBean.setIserror(true);
			List<EnumModel> enumData = orgManager.getEnumListByValue(columnName);
			resultBean.setResultList(enumData);
			ObjectMapper objMapper = new ObjectMapper();
			String json = objMapper.writeValueAsString(enumData);
			resultBean.setResultString(json);
		}
		catch (Exception e)
		{
			resultBean.setIserror(false);
			String errorCode = ErrorCodeContant.ENUM_NOT_FOUND;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;

	}

	public ResultBean getAirportDetailsByCode(String columnName)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			resultBean.setIserror(true);
			List<AirportModal> airportModals = orgManager.getAirportDetailsByCode(columnName);

			if (airportModals != null && !airportModals.isEmpty())
			{
				List<CityBean> cityBeans = geoLocationManager.fetchCity();
				List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
				CityBean cityBean;
				for (AirportModal airportModal : airportModals)
				{
					cityBean = new CityBean();
					CountryBean countryBean;
					if (airportModal.getCityId() > 0)
					{
						cityBean.setCityId(airportModal.getCityId());
						airportModal.setCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
						cityBean.setSearchCityCode("Yes");
						airportModal.setUpdatedCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
					}
					countryBean = new CountryBean();
					if (airportModal.getCountryID() > 0)
					{
						countryBean.setCountryId(airportModal.getCountryID());
						airportModal.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
						countryBean.setSearchCountryCode("Yes");
						airportModal.setUpdatedCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
					}
				}
			}
			resultBean.setResultList(airportModals);
		}
		catch (Exception e)
		{
			resultBean.setIserror(false);
			String errorCode = ErrorCodeContant.ENUM_NOT_FOUND;
			String errorMessage = ResourceBundle.getBundle(APPLICATIONRESOURCE, LocaleContextHolder.getLocale()).getString(errorCode);
			resultBean.setErrorCode(errorCode);
			resultBean.setErrorMessage(errorMessage);
			TTLog.printStackTrace(0, errorCode, e);
		}
		return resultBean;

	}

	public ResultBean searchAirport(AirportModal airportModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<AirportModal> airportModals = orgManager.searchAirport(airportModel);
			if (airportModals != null && !airportModals.isEmpty())
			{
				List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
				CountryBean countryBean;
				List<CityBean> cityBeans = geoLocationManager.fetchCity();
				CityBean cityBean;
				for (AirportModal airportModal : airportModals)
				{
					countryBean = new CountryBean();
					if (airportModal.getCountryID() > 0)
					{
						countryBean.setCountryId(airportModal.getCountryID());
						airportModal.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
						countryBean.setSearchCountryCode("Yes");
						airportModal.setUpdatedCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
					}
					cityBean = new CityBean();
					if (airportModal.getCityId() > 0)
					{
						cityBean.setCityId(airportModal.getCityId());
						airportModal.setCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
						cityBean.setSearchCityCode("Yes");
						airportModal.setUpdatedCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
					}
				}
			}
			resultBean.setResultList(airportModals);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}

	public ResultBean getFactObjectIdAgencyBranch(CommonModal modal)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Integer> i = new ArrayList<>();
			i.add(modal.getBranchId());
			i.add(modal.getGroupId());// Agency Id

			List<Object> factId = orgManager.getFactObjectIdAgencyBranch(i);

			resultBean.setResultList(factId);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}

	public ResultBean fetchPaymentOption(String agencyCode)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaymentMode> paymentModel = orgManager.fetchPaymentOption(agencyCode);
			resultBean.setResultList(paymentModel);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}
	public ResultBean prefferdAirline(AirlineModel airlineModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<AirlineModel> airlineModal = orgManager.prefferdAirline(airlineModel);
			resultBean.setResultList(airlineModal);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}

	public ResultBean calculateUccfOld(FlightBookingRequestBean bookingRequestBean)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			String bookingRefId="["+bookingRequestBean.getFbBookingReference()+" uccfSearchData]";
			CallLog.info(15,bookingRefId+ "tripType = " + bookingRequestBean.getTripType());
			int onwardSourceCityId = -1;
			int onwardDestinationCityId=-1;
			int returnSourceCityId = -1;
			int returnDestinationCityId=-1;	
			double onwardUccfTxnCharge=0.0;
			double returnUccfTxnCharge=0.0;
			String onwardCareer;
			String onwardOperatedByAirline;
			String returnCareer;
			String returnOperatedByAirline;
			ResultBean isUCCFDataOnward ;
			ResultBean isUCCFDataReturn ;
			UCCFModal uccfModalOnward;
			UCCFModal uccfModalReturn;
			double finalUccfTxnChargeOnward=0.0;
			double finalUccfTxnChargeReturn=0.0;
			double finalUccfTxnChargeMulticity=0.0;
			UCCFModal validUccfModal=null;
			UCCFModal validUccfModalReturn=null;
			UCCFModal validUccfModalMulticity=null;
			if(bookingRequestBean.getTripType().equals(ONEWAY)){
				onwardSourceCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getOriginCity());
				List <FlightLegs> flightLegsList= bookingRequestBean.getOnwardFlightOption().getFlightlegs();
				onwardDestinationCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getDestinationCity());
				CallLog.info(15,bookingRefId+ "OneWay SourceCityId = " + onwardSourceCityId+": SourceCity Code = "+bookingRequestBean.getOnwardFlightOption().getOrigin());
				CallLog.info(15,bookingRefId+ "OneWay DestinationCityId = " + onwardDestinationCityId+": DestinationCity Code ="+bookingRequestBean.getOnwardFlightOption().getDestination());
				if(null!=flightLegsList){
					CallLog.info(15,bookingRefId+ "OneWay flightLegsList size = " + flightLegsList.size());
					for(FlightLegs flightleg: flightLegsList){
						int i =0;
						onwardCareer=flightleg.getCarrier();
						onwardOperatedByAirline=flightleg.getOperatedByAirline();
						CallLog.info(15,bookingRefId+ "OneWay Carrier = " + onwardCareer);
						CallLog.info(15,bookingRefId+ "OneWay OperatedByAirline = " + onwardOperatedByAirline);

						isUCCFDataOnward = getUccfData(new UCCFModal(),onwardSourceCityId, onwardDestinationCityId, onwardOperatedByAirline, bookingRequestBean.getFbBookingReference());
						uccfModalOnward =!isUCCFDataOnward.isError()?(UCCFModal) isUCCFDataOnward.getResultObject():null;

						if(null!=uccfModalOnward){
							onwardUccfTxnCharge= getUCCFTxnCharge(uccfModalOnward, bookingRequestBean);	
							if(i==0)
							{
								validUccfModal=uccfModalOnward;
								finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
							}
							else{
								if(onwardUccfTxnCharge>finalUccfTxnChargeOnward){
								validUccfModal=uccfModalOnward;
								}
							}
							i++;
						}
					}
					if(null!=validUccfModal)
						resultBean = setUCCFPaymentDetails(validUccfModal, bookingRequestBean);
				}
			}
			else if(bookingRequestBean.getTripType().equals(ROUNDTRIP)){
				List <FlightLegs> onwardFlightLegsList= bookingRequestBean.getOnwardFlightOption().getFlightlegs();
				List <FlightLegs> returnFlightLegsList= bookingRequestBean.getReturnFlightOption().getFlightlegs();
				onwardSourceCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getOriginCity());
				onwardDestinationCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getDestinationCity());

				CallLog.info(15,bookingRefId+ "RoundTrip onward SourceCityId = " + onwardSourceCityId+": SourceCity Code = "+bookingRequestBean.getOnwardFlightOption().getOrigin());
				CallLog.info(15,bookingRefId+ "RoundTrip onward DestinationCityId = " + onwardDestinationCityId+": DestinationCity Code ="+bookingRequestBean.getOnwardFlightOption().getDestination());

				if(null!=onwardFlightLegsList){
					CallLog.info(15,bookingRefId+ "RoundTrip onward FlightLegsList size = " + onwardFlightLegsList.size());
					for(FlightLegs onwardFlightleg: onwardFlightLegsList){
						int i =0;
						onwardCareer=onwardFlightleg.getCarrier();
						onwardOperatedByAirline=onwardFlightleg.getOperatedByAirline();
						CallLog.info(15,bookingRefId+ "RoundTrip Onward Carrier = " + onwardCareer);
						CallLog.info(15,bookingRefId+ "RoundTrip Onward OperatedByAirline = " + onwardOperatedByAirline);

						isUCCFDataOnward = getUccfData(new UCCFModal(),onwardSourceCityId,onwardDestinationCityId,onwardOperatedByAirline,bookingRequestBean.getFbBookingReference());
						uccfModalOnward =!isUCCFDataOnward.isError()?(UCCFModal) isUCCFDataOnward.getResultObject():null;

						if(null!=uccfModalOnward){
							onwardUccfTxnCharge= getUCCFTxnCharge(uccfModalOnward, bookingRequestBean);	
							
							if(i==0)
							{
								validUccfModal=uccfModalOnward;
								finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
							}
							else{
								if(onwardUccfTxnCharge>finalUccfTxnChargeOnward){
								validUccfModal=uccfModalOnward;
								finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
								}
							}
							i++;
						}
					}
				}
				returnSourceCityId = Integer.parseInt(bookingRequestBean.getReturnFlightOption().getOriginCity());
				returnDestinationCityId = Integer.parseInt(bookingRequestBean.getReturnFlightOption().getDestinationCity());
				CallLog.info(15,bookingRefId+ "RoundTrip return SourceCityId = " + returnSourceCityId+": SourceCity Code = "+bookingRequestBean.getReturnFlightOption().getOrigin());
				CallLog.info(15,bookingRefId+ "RoundTrip return DestinationCityId = " + returnDestinationCityId+": SourceCity Code = "+bookingRequestBean.getReturnFlightOption().getDestination());
				if(null!=returnFlightLegsList){
					CallLog.info(15,bookingRefId+ "RoundTrip return FlightLegsList size = " + returnFlightLegsList.size());
					for(FlightLegs returnFlightleg: returnFlightLegsList){
						int i =0;
						returnCareer=returnFlightleg.getCarrier();
						returnOperatedByAirline=returnFlightleg.getOperatedByAirline();
						CallLog.info(15,bookingRefId+ "RoundTrip return Carrier = " + returnCareer);
						CallLog.info(15,bookingRefId+ "RoundTrip return OperatedByAirline = " + returnOperatedByAirline);

						isUCCFDataReturn = getUccfData(new UCCFModal(),returnSourceCityId,returnDestinationCityId,returnOperatedByAirline,bookingRequestBean.getFbBookingReference());
						uccfModalReturn =!isUCCFDataReturn.isError()?(UCCFModal) isUCCFDataReturn.getResultObject():null;

						if(null!=uccfModalReturn){
							returnUccfTxnCharge= getUCCFTxnCharge(uccfModalReturn, bookingRequestBean);	
							
							if(i==0)
							{
								validUccfModalReturn=uccfModalReturn;
								finalUccfTxnChargeReturn=returnUccfTxnCharge;	
							}
							else{
								if(returnUccfTxnCharge>finalUccfTxnChargeReturn){
								validUccfModalReturn=uccfModalReturn;
								finalUccfTxnChargeReturn=returnUccfTxnCharge;	
								}
							}
							i++;
						}
					}
				}
				if(finalUccfTxnChargeOnward>0.0 && finalUccfTxnChargeOnward>=finalUccfTxnChargeReturn){
					if(null!=validUccfModal)
						resultBean = setUCCFPaymentDetails(validUccfModal, bookingRequestBean);
					CallLog.info(15,bookingRefId+ "RoundTrip Onward Flight UCCF Charges applied");

				}else if(finalUccfTxnChargeReturn>0.0 && finalUccfTxnChargeReturn>=finalUccfTxnChargeOnward){
					if(null!=validUccfModalReturn)
						resultBean = setUCCFPaymentDetails(validUccfModalReturn, bookingRequestBean);
					CallLog.info(15,bookingRefId+ "RoundTrip Return Flight UCCF Charges applied");
				}
			}
			else{
				List<OptionSegmentBean> optionSegmentBeans=bookingRequestBean.getOnwardFlightOption().getOptionSegmentBean();
				if (!optionSegmentBeans.isEmpty())
				{
					CallLog.info(15,bookingRefId+ "MultiCity optionSegmentBeans size=" + optionSegmentBeans.size());
					for (OptionSegmentBean optionSegmentData : optionSegmentBeans)
					{
						int j=0;
						List <FlightLegs> onwardFlightLegsList= optionSegmentData.getFlightlegs();
						if(null!=onwardFlightLegsList){
							for(FlightLegs onwardFlightleg: onwardFlightLegsList){
								int i =0;
								onwardCareer=onwardFlightleg.getCarrier();
								onwardOperatedByAirline=onwardFlightleg.getOperatedByAirline();
								onwardSourceCityId = Integer.parseInt(optionSegmentData.getOriginCity());
								onwardDestinationCityId = Integer.parseInt(optionSegmentData.getDestinationCity());
								
								CallLog.info(15,bookingRefId+ "MultiCity Onward Carrier = " + onwardCareer);
								CallLog.info(15,bookingRefId+ "MultiCity Onward OperatedByAirline = " + onwardOperatedByAirline);
								CallLog.info(15, bookingRefId+"MultiCity SourceCityId=" + onwardSourceCityId+": OriginCity Code = "+optionSegmentData.getOrigin());
								CallLog.info(15, bookingRefId+"MultiCity DestinationCityId=" + onwardDestinationCityId+": DestinationCity Code = "+optionSegmentData.getDestination());

								isUCCFDataOnward = getUccfData(new UCCFModal(),onwardSourceCityId,onwardDestinationCityId,onwardOperatedByAirline,bookingRequestBean.getFbBookingReference());
								uccfModalOnward =!isUCCFDataOnward.isError()?(UCCFModal) isUCCFDataOnward.getResultObject():null;

								if(null!=uccfModalOnward){
									onwardUccfTxnCharge= getUCCFTxnCharge(uccfModalOnward, bookingRequestBean);
									if(i==0)
									{
										validUccfModal=uccfModalOnward;
										finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
									}
									else{
										if(onwardUccfTxnCharge>finalUccfTxnChargeOnward){
											validUccfModal=uccfModalOnward;
											finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
										}
									}
									i++;
								}
							}
							if(j==0){
								finalUccfTxnChargeMulticity=finalUccfTxnChargeOnward;
								validUccfModalMulticity=validUccfModal;
							}else{
								if(finalUccfTxnChargeOnward>finalUccfTxnChargeMulticity){
									finalUccfTxnChargeMulticity=finalUccfTxnChargeOnward;
									validUccfModalMulticity=validUccfModal;
								}
							}
								j++;
						}
					}
					if(null!=validUccfModalMulticity)
					resultBean = setUCCFPaymentDetails(validUccfModalMulticity, bookingRequestBean);	
				}
			}
		}
		catch (Exception e)
		{
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}
	
	public ResultBean calculateUccf(FlightBookingRequestBean bookingRequestBean)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			String bookingRefId="["+bookingRequestBean.getFbBookingReference()+" uccfSearchData]";
			CallLog.info(15,bookingRefId+ "tripType = " + bookingRequestBean.getTripType());
			int onwardSourceCityId = -1;
			int onwardDestinationCityId=-1;
			int returnSourceCityId = -1;
			int returnDestinationCityId=-1;	
			double onwardUccfTxnCharge=0.0;
			double returnUccfTxnCharge=0.0;
			String onwardPlatingCarrier;
			String returnPlatingCarrier;
			ResultBean isUCCFDataOnward ;
			ResultBean isUCCFDataReturn ;
			UCCFModal uccfModalOnward;
			UCCFModal uccfModalReturn;
			double finalUccfTxnChargeOnward=0.0;
			UCCFModal validUccfModal=null;
			if(bookingRequestBean.getTripType().equals(ONEWAY)){
				onwardSourceCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getOriginCity());
				onwardDestinationCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getDestinationCity());
				onwardPlatingCarrier=bookingRequestBean.getOnwardFlightOption().getPlatingCarrier();
				CallLog.info(15,bookingRefId+ "OneWay onwardPlatingCarrier = " + onwardPlatingCarrier);
				CallLog.info(15,bookingRefId+ "OneWay SourceCityId = " + onwardSourceCityId+": SourceCity Code = "+bookingRequestBean.getOnwardFlightOption().getOrigin());
				CallLog.info(15,bookingRefId+ "OneWay DestinationCityId = " + onwardDestinationCityId+": DestinationCity Code ="+bookingRequestBean.getOnwardFlightOption().getDestination());
				
						isUCCFDataOnward = getUccfData(new UCCFModal(),onwardSourceCityId, onwardDestinationCityId, onwardPlatingCarrier, bookingRequestBean.getFbBookingReference());
						uccfModalOnward =!isUCCFDataOnward.isError()?(UCCFModal) isUCCFDataOnward.getResultObject():null;
						if(null!=uccfModalOnward){
							resultBean = setUCCFPaymentDetails(uccfModalOnward, bookingRequestBean);
						}
					
			}
			else if(bookingRequestBean.getTripType().equals(ROUNDTRIP)){
				onwardSourceCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getOriginCity());
				onwardDestinationCityId = Integer.parseInt(bookingRequestBean.getOnwardFlightOption().getDestinationCity());
				onwardPlatingCarrier=bookingRequestBean.getOnwardFlightOption().getPlatingCarrier();
				CallLog.info(15,bookingRefId+ "RoundTrip onwardPlatingCarrier = " + onwardPlatingCarrier);
				CallLog.info(15,bookingRefId+ "RoundTrip onward SourceCityId = " + onwardSourceCityId+": SourceCity Code = "+bookingRequestBean.getOnwardFlightOption().getOrigin());
				CallLog.info(15,bookingRefId+ "RoundTrip onward DestinationCityId = " + onwardDestinationCityId+": DestinationCity Code ="+bookingRequestBean.getOnwardFlightOption().getDestination());


						isUCCFDataOnward = getUccfData(new UCCFModal(),onwardSourceCityId,onwardDestinationCityId,onwardPlatingCarrier,bookingRequestBean.getFbBookingReference());
						uccfModalOnward =!isUCCFDataOnward.isError()?(UCCFModal) isUCCFDataOnward.getResultObject():null;

						if(null!=uccfModalOnward){
							onwardUccfTxnCharge= getUCCFTxnCharge(uccfModalOnward, bookingRequestBean);	
						}
				returnSourceCityId = Integer.parseInt(bookingRequestBean.getReturnFlightOption().getOriginCity());
				returnDestinationCityId = Integer.parseInt(bookingRequestBean.getReturnFlightOption().getDestinationCity());
				returnPlatingCarrier=bookingRequestBean.getReturnFlightOption().getPlatingCarrier();
				CallLog.info(15,bookingRefId+ "RoundTrip returnPlatingCarrier = " + returnPlatingCarrier);
				CallLog.info(15,bookingRefId+ "RoundTrip return SourceCityId = " + returnSourceCityId+": SourceCity Code = "+bookingRequestBean.getReturnFlightOption().getOrigin());
				CallLog.info(15,bookingRefId+ "RoundTrip return DestinationCityId = " + returnDestinationCityId+": SourceCity Code = "+bookingRequestBean.getReturnFlightOption().getDestination());

						isUCCFDataReturn = getUccfData(new UCCFModal(),returnSourceCityId,returnDestinationCityId,returnPlatingCarrier,bookingRequestBean.getFbBookingReference());
						uccfModalReturn =!isUCCFDataReturn.isError()?(UCCFModal) isUCCFDataReturn.getResultObject():null;

						if(null!=uccfModalReturn){
							returnUccfTxnCharge= getUCCFTxnCharge(uccfModalReturn, bookingRequestBean);	
						}
						
				if(onwardUccfTxnCharge>0.0 && onwardUccfTxnCharge>=returnUccfTxnCharge){
					if(null!=uccfModalOnward)
						resultBean = setUCCFPaymentDetails(uccfModalOnward, bookingRequestBean);
					CallLog.info(15,bookingRefId+ "RoundTrip Onward Flight UCCF Charges applied");

				}else if(returnUccfTxnCharge>0.0 && returnUccfTxnCharge>=onwardUccfTxnCharge){
					if(null!=uccfModalReturn)
						resultBean = setUCCFPaymentDetails(uccfModalReturn, bookingRequestBean);
					CallLog.info(15,bookingRefId+ "RoundTrip Return Flight UCCF Charges applied");
				}
			}
			else{
				List<OptionSegmentBean> optionSegmentBeans=bookingRequestBean.getOnwardFlightOption().getOptionSegmentBean();
				if (!optionSegmentBeans.isEmpty())
				{
					CallLog.info(15,bookingRefId+ "MultiCity optionSegmentBeans size=" + optionSegmentBeans.size());
					for (OptionSegmentBean optionSegmentData : optionSegmentBeans)
					{
						int i=0;
								onwardSourceCityId = Integer.parseInt(optionSegmentData.getOriginCity());
								onwardDestinationCityId = Integer.parseInt(optionSegmentData.getDestinationCity());
								onwardPlatingCarrier=bookingRequestBean.getOnwardFlightOption().getPlatingCarrier();
								CallLog.info(15,bookingRefId+ "MultiCity onwardPlatingCarrier = " + onwardPlatingCarrier);
								CallLog.info(15, bookingRefId+"MultiCity SourceCityId=" + onwardSourceCityId+": OriginCity Code = "+optionSegmentData.getOrigin());
								CallLog.info(15, bookingRefId+"MultiCity DestinationCityId=" + onwardDestinationCityId+": DestinationCity Code = "+optionSegmentData.getDestination());

								isUCCFDataOnward = getUccfData(new UCCFModal(),onwardSourceCityId,onwardDestinationCityId,onwardPlatingCarrier,bookingRequestBean.getFbBookingReference());
								uccfModalOnward =!isUCCFDataOnward.isError()?(UCCFModal) isUCCFDataOnward.getResultObject():null;

								if(null!=uccfModalOnward){
									onwardUccfTxnCharge= getUCCFTxnCharge(uccfModalOnward, bookingRequestBean);
									if(i==0)
									{
										validUccfModal=uccfModalOnward;
										finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
									}
									else{
										if(onwardUccfTxnCharge>finalUccfTxnChargeOnward){
											validUccfModal=uccfModalOnward;
											finalUccfTxnChargeOnward=onwardUccfTxnCharge;	
										}
									}
									i++;
								}
					}
					if(null!=validUccfModal)
					resultBean = setUCCFPaymentDetails(validUccfModal, bookingRequestBean);	
				}
			}
		}
		catch (Exception e)
		{
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}
	public ResultBean fraudCardCheckInDB(String cardNumber)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			//CallLog.info(15,"Card Number = " + cardNumber);
			ResultBean fraudCardCheck =null;
			FraudulentModel fraudulentModel=null;
			if(null!=cardNumber && !cardNumber.equals("")){				
				fraudCardCheck = getFraudCardData(new FraudulentModel(),cardNumber);
				fraudulentModel =(fraudCardCheck.isError()==true?(FraudulentModel) fraudCardCheck.getResultObject():null);
				if(null!=fraudulentModel){
					resultBean.setIserror(fraudCardCheck.isError());
				}else
					resultBean.setIserror(false);
			}
		}
		catch (Exception e)
		{
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}
	
	@SuppressWarnings("unchecked")
	public ResultBean getFraudCardData(FraudulentModel fraudulentModel, String cardNumber)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			resultBean.setIserror(true);
			fraudulentModel.setCardNumber(cardNumber);
			//CallLog.info(15, "fraudulentModel.getCardNumber() =" + fraudulentModel.getCardNumber());
			if (!fraudulentModel.getCardNumber().equals(""))
			{
				
				fraudulentModel.setStatus(1);
				fraudulentModel.setApprovalStatus(1);
				List<FraudulentModel> searchFraudulentModals = paymentGatewayManager.searchFraudulent(fraudulentModel);
				if (!searchFraudulentModals.isEmpty())
				{
					CallLog.info(15, "searchFraudulentModalData size=" + searchFraudulentModals.size());
					for (FraudulentModel fraudulentModelData : searchFraudulentModals)
					{
						CallLog.info(15, fraudulentModelData.getCardNumber()+" is found in fraudulent card list in Database ");
						resultBean.setResultObject(fraudulentModelData);
						break;
					}
				}
				else
				{
					resultBean.setIserror(false);
				}
			}
			else
				resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(false);
			TTPortalLog.printStackTrace(113, e);
		}
		return resultBean;
	}
	
	@SuppressWarnings("unchecked")
	public ResultBean searchAllAirportNew(AirportModal airportModel)
	{
		ResultBean resultBean = new ResultBean();
		List<AirportModal> airportModals = new ArrayList<>();
		try
		{
			Integer expiryTimeCache = Integer.parseInt(messageSource.getMessage(ALL_AIRPORT_EXPIRE_TIME, null ,"360", null));
			String flightSearchKey = com.tt.ts.rest.common.util.CommonUtil.prepareKeyForAutoSuggest("allairport");
			Boolean isSearchKeyFound = false;
			resultBean = RedisCacheUtil.isSearchKeyInCacheFlight(flightSearchKey, redisService);
			if (resultBean != null && !resultBean.isError())
			{
				isSearchKeyFound = resultBean.getResultBoolean();
			}
			if (isSearchKeyFound)
			{
				ResultBean resultBeanCache = RedisCacheUtil.getResponseObjectFromCacheFlight(flightSearchKey, redisService);
				if (resultBeanCache != null && !resultBeanCache.isError())
				{
					airportModals = (List<AirportModal>)resultBeanCache.getResultObject();
				}
				else
				{
					Long s1 = System.currentTimeMillis();
					airportModals = orgManager.searchAirport(airportModel);
					CallLog.info(102,"fetch Airport*************>>"+(System.currentTimeMillis() - s1));
					if (airportModals != null && !airportModals.isEmpty())
					{
						Long s2 = System.currentTimeMillis();
						List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
						CallLog.info(102,"fetch country*************>>"+(System.currentTimeMillis() - s2));
						CountryBean countryBean;
						Long s3 = System.currentTimeMillis();
						List<CityBean> cityBeans = geoLocationManager.fetchCity();
						CallLog.info(102,"fetch city*************>>"+(System.currentTimeMillis() - s3));
						CityBean cityBean;
						Long s4 = System.currentTimeMillis();
						for (AirportModal airportModal : airportModals)
						{
							countryBean = new CountryBean();
							if (airportModal.getCountryID() > 0)
							{
								countryBean.setCountryId(airportModal.getCountryID());
								airportModal.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
								countryBean.setSearchCountryCode("Yes");
								airportModal.setUpdatedCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
							}
							cityBean = new CityBean();
							if (airportModal.getCityId() > 0)
							{
								cityBean.setCityId(airportModal.getCityId());
								airportModal.setCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
								cityBean.setSearchCityCode("Yes");
								airportModal.setUpdatedCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
							}
						}
						CallLog.info(102,"set country and city of airports*************>>"+(System.currentTimeMillis() - s4));
					}
					
					RedisCacheUtil.setResponseObjectInCacheFlight(flightSearchKey, airportModals, redisService,expiryTimeCache);
				}
			}
			else
			{
				Long s1 = System.currentTimeMillis();
				airportModals = orgManager.searchAirport(airportModel);
				CallLog.info(102,"fetch Airport*************>>"+(System.currentTimeMillis() - s1));
				if (airportModals != null && !airportModals.isEmpty())
				{
					Long s2 = System.currentTimeMillis();
					List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
					CallLog.info(102,"fetch country*************>>"+(System.currentTimeMillis() - s2));
					CountryBean countryBean;
					Long s3 = System.currentTimeMillis();
					List<CityBean> cityBeans = geoLocationManager.fetchCity();
					CallLog.info(102,"fetch city*************>>"+(System.currentTimeMillis() - s3));
					CityBean cityBean;
					Long s4 = System.currentTimeMillis();
					for (AirportModal airportModal : airportModals)
					{
						countryBean = new CountryBean();
						if (airportModal.getCountryID() > 0)
						{
							countryBean.setCountryId(airportModal.getCountryID());
							airportModal.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
							countryBean.setSearchCountryCode("Yes");
							airportModal.setUpdatedCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
						}
						cityBean = new CityBean();
						if (airportModal.getCityId() > 0)
						{
							cityBean.setCityId(airportModal.getCityId());
							airportModal.setCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
							cityBean.setSearchCityCode("Yes");
							airportModal.setUpdatedCityName(CommonUtil.getCityNameById(cityBeans, cityBean));
						}
					}
					
					CallLog.info(102,"set country and city of airports*************>>"+(System.currentTimeMillis() - s4));
					RedisCacheUtil.setResponseObjectInCacheFlight(flightSearchKey, airportModals, redisService,expiryTimeCache);
				}
				
			}
			
			resultBean.setResultList(airportModals);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(15, e);
		}
		return resultBean;
	}
	
	public ResultBean getDealCodeListForSearch(FlightSearchRequestBean flightSearchRequestBean, int pccId, Integer countryId) {
		ResultBean resultBean = new ResultBean();
		try {
			List<GDSDealCodeModel> gdsDealCodeModelList;
			GDSDealCodeModel gdsDealCodeModel = new GDSDealCodeModel();
			gdsDealCodeModel.setCredentialId(pccId);
			gdsDealCodeModel.setStatus(1);
			gdsDealCodeModel.setApprovalStatus(1);
			String tripType = flightSearchRequestBean.getTripType() != null && !flightSearchRequestBean.getTripType().isEmpty() ? flightSearchRequestBean.getTripType() : "";

			List<com.ws.services.flight.config.GDSDealCodeModel> newGdsDealCodeModelList = new ArrayList<>();
			gdsDealCodeModelList = orgManager.getDealCodeModelList(gdsDealCodeModel);
			
			if (tripType.equalsIgnoreCase(ONEWAY) || tripType.equalsIgnoreCase(ROUNDTRIP)) {

				int originAirportId = 0;
				int destinationAirportId = 0;
				
				ResultBean resultBeanOrigin = airportService.getAirportByAirportCode(flightSearchRequestBean.getOrigin());
				if(resultBeanOrigin!=null && !resultBeanOrigin.isError() && resultBeanOrigin.getResultObject()!=null) {
					AirportModal airportModal = (AirportModal)resultBeanOrigin.getResultObject();
					originAirportId = airportModal.getAirportId();
				}
				ResultBean resultBeanDestination = airportService.getAirportByAirportCode(flightSearchRequestBean.getDestination());
				if(resultBeanDestination!=null && !resultBeanDestination.isError() && resultBeanDestination.getResultObject()!=null) {
					AirportModal airportModal = (AirportModal)resultBeanDestination.getResultObject();
					destinationAirportId = airportModal.getAirportId();
				}
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;

				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				
				boolean isCountryPosFound = false;
				
				if (gdsDealCodeModelList != null && !gdsDealCodeModelList.isEmpty()) {
					for (GDSDealCodeModel gdsDealCode : gdsDealCodeModelList) {
						com.ws.services.flight.config.GDSDealCodeModel gdsDealCodeModelWs = new com.ws.services.flight.config.GDSDealCodeModel();
						gdsDealCodeModelWs.setCredentialId(gdsDealCode.getCredentialId());
						gdsDealCodeModelWs.setValidFrom(gdsDealCode.getValidFrom());
						gdsDealCodeModelWs.setValidTo(gdsDealCode.getValidTo());
						gdsDealCodeModelWs.setSupplierId(gdsDealCode.getSupplierId());
						gdsDealCodeModelWs.setAirlineId(gdsDealCode.getAirlineId());
						gdsDealCodeModelWs.setAirlineCode(gdsDealCode.getAirlineCode());
						gdsDealCodeModelWs.setImportPNR(gdsDealCode.isImportPNR());
						gdsDealCodeModelWs.setDealCode(gdsDealCode.getDealCode());
						gdsDealCodeModelWs.setLastModTime(gdsDealCode.getLastModTime());
						isOriginAirportFound = false;
						isDestinationAirportFound = false;

						isOriginCountryFound = false;
						isDestinationCountryFound = false;

						isCountryPosFound = false;
						
						List<GDSDealCodeCountryModel> gdsCountryList = gdsDealCode.getGdsDealCountryList();
						List<GDSDealCodeAirportModel> gdsAirportList = gdsDealCode.getGdsDealAirporList();
						List<GDSDealCodeAgencyModel> gdsAgencyList = gdsDealCode.getGdsDealAgencyList();
						
							if (gdsDealCode.getIsCountrySelected() == 0 && gdsAirportList != null && !gdsAirportList.isEmpty()) {
								
							if(tripType.equalsIgnoreCase(ONEWAY)){
								
								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {
									
									if (originAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 0)
										isOriginAirportFound = true;
	
									if (destinationAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 1)
										isDestinationAirportFound = true;
	
									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
								}
								
							}
							else{

								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {
									
									if ((originAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 0) || (originAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 1))
										isOriginAirportFound = true;
							
									if ((destinationAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 1) || (destinationAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 0))
										isDestinationAirportFound = true;
	
									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
							}
							}
						
							if(isOriginAirportFound && isDestinationAirportFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
								for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
									if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
										isCountryPosFound = true;
										break;
									}
								}
								if (isCountryPosFound) {
									newGdsDealCodeModelList.add(gdsDealCodeModelWs);
								}
							}
						}
						else if (gdsDealCode.getIsCountrySelected() == 1 && gdsCountryList != null && !gdsCountryList.isEmpty()) {
							
							if(tripType.equalsIgnoreCase(ONEWAY)){
								
								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if (flightSearchRequestBean.getOriginCountryId()!=null && flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0)
										isOriginCountryFound = true;

									if (flightSearchRequestBean.getDestinationCountryId()!=null && flightSearchRequestBean.getDestinationCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1)
										isDestinationCountryFound = true;
									
									if(isOriginCountryFound && isDestinationCountryFound){
										break;
									}
								}
								
							}
							else{
								
								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if (flightSearchRequestBean.getOriginCountryId()!=null && (flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0) || (flightSearchRequestBean.getOriginCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1))
										isOriginCountryFound = true;

									if (flightSearchRequestBean.getDestinationCountryId()!=null && (flightSearchRequestBean.getDestinationCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1) || (flightSearchRequestBean.getDestinationCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0))
										isDestinationCountryFound = true;
									
									if(isOriginCountryFound && isDestinationCountryFound){
										break;
									}
								}
							}
							
							if(isOriginCountryFound && isDestinationCountryFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
								for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
									if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
										isCountryPosFound = true;
										break;
									}
								}
								
								if (isCountryPosFound) {
									newGdsDealCodeModelList.add(gdsDealCodeModelWs);
								}
							}
							
							
						}

					}
				}
			} else if (tripType.equalsIgnoreCase(MULTICITY)) {
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;
				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				boolean isCountryPosFound = false;
				if (gdsDealCodeModelList != null && !gdsDealCodeModelList.isEmpty()) {
					for (GDSDealCodeModel gdsDealCode : gdsDealCodeModelList) {
						com.ws.services.flight.config.GDSDealCodeModel gdsDealCodeModelWs = new com.ws.services.flight.config.GDSDealCodeModel();
						gdsDealCodeModelWs.setCredentialId(gdsDealCode.getCredentialId());
						gdsDealCodeModelWs.setValidFrom(gdsDealCode.getValidFrom());
						gdsDealCodeModelWs.setValidTo(gdsDealCode.getValidTo());
						gdsDealCodeModelWs.setSupplierId(gdsDealCode.getSupplierId());
						gdsDealCodeModelWs.setAirlineId(gdsDealCode.getAirlineId());
						gdsDealCodeModelWs.setAirlineCode(gdsDealCode.getAirlineCode());
						gdsDealCodeModelWs.setImportPNR(gdsDealCode.isImportPNR());
						gdsDealCodeModelWs.setDealCode(gdsDealCode.getDealCode());
						gdsDealCodeModelWs.setLastModTime(gdsDealCode.getLastModTime());
						isCountryPosFound = false;
						List<GDSDealCodeCountryModel> gdsCountryList = gdsDealCode.getGdsDealCountryList();
						List<GDSDealCodeAirportModel> gdsAirportList = gdsDealCode.getGdsDealAirporList();
						List<GDSDealCodeAgencyModel> gdsAgencyList = gdsDealCode.getGdsDealAgencyList();
						
						if (gdsDealCode.getIsCountrySelected() == 0 && gdsAirportList != null && !gdsAirportList.isEmpty()) {
							for (FlightSearchRequestBean eachSearchSegment : flightSearchRequestBean.getFlightSearchRequestList()) {
								
								int originAirportId = 0;
								int destinationAirportId = 0;
								
								ResultBean resultBeanOrigin = airportService.getAirportByAirportCode(eachSearchSegment.getOrigin());
								if(resultBeanOrigin!=null && !resultBeanOrigin.isError() && resultBeanOrigin.getResultObject()!=null) {
									AirportModal airportModal = (AirportModal)resultBeanOrigin.getResultObject();
									originAirportId = airportModal.getAirportId();
								}
								ResultBean resultBeanDestination = airportService.getAirportByAirportCode(eachSearchSegment.getDestination());
								if(resultBeanDestination!=null && !resultBeanDestination.isError() && resultBeanDestination.getResultObject()!=null) {
									AirportModal airportModal = (AirportModal)resultBeanDestination.getResultObject();
									destinationAirportId = airportModal.getAirportId();
								}

								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {
									isOriginAirportFound = false;
									isDestinationAirportFound = false;
									if (originAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 0)
										isOriginAirportFound = true;

									if (destinationAirportId == eachAirportModal.getAirportId() && eachAirportModal.getIsSource() == 1)
										isDestinationAirportFound = true;

									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
								}
								if(isOriginAirportFound && isDestinationAirportFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
									for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
										if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
											isCountryPosFound = true;
											break;
										}
									}
									if (isCountryPosFound) {
										newGdsDealCodeModelList.add(gdsDealCodeModelWs);
										break;
									}
								}
								
							}
						}

						else if (gdsDealCode.getIsCountrySelected() == 1 && gdsCountryList != null && !gdsCountryList.isEmpty()) {

							for (FlightSearchRequestBean eachSearchSegment : flightSearchRequestBean.getFlightSearchRequestList()) {
								isOriginCountryFound = false;
								isDestinationCountryFound = false;

								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if (eachSearchSegment.getOriginCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0)
										isOriginCountryFound = true;

									if (eachSearchSegment.getDestinationCountryId().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1)
										isDestinationCountryFound = true;
									
									if(isOriginCountryFound && isDestinationCountryFound) {
										break;
									}
								}

								if(isOriginCountryFound && isDestinationCountryFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
									for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
										if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
											isCountryPosFound = true;
											break;
										}
									}
									if (isCountryPosFound) {
										newGdsDealCodeModelList.add(gdsDealCodeModelWs);
										break;
									}
								}	
							}
						}
					}
				}
			}
			resultBean.setIserror(false);
			resultBean.setResultList((List<com.ws.services.flight.config.GDSDealCodeModel>) newGdsDealCodeModelList);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(0, e);
		}
		return resultBean;
	}
	
	@SuppressWarnings("unchecked")
	public ResultBean getDealCodeList(FlightBookingRequestBean flightBookingRequestBean, int pccId, Integer countryId) {
		ResultBean resultBean = new ResultBean();
		try {
			List<GDSDealCodeModel> gdsDealCodeModelList;
			ResultBean resultBean2 = airlineService.getAirlinesNew(new AirlineModel());
			List<AirlineModel> airlineModels = (List<AirlineModel>) resultBean2.getResultList();
			int airlineId = com.tt.ts.rest.common.util.CommonUtil.fetchAirlineIdByCode(airlineModels, flightBookingRequestBean.getOnwardFlightOption().getPlatingCarrier());
			ResultBean resultBeanAirline;
			
			GDSDealCodeModel gdsDealCodeModel = new GDSDealCodeModel();
			gdsDealCodeModel.setCredentialId(pccId);
			gdsDealCodeModel.setAirlineId(airlineId);
			gdsDealCodeModel.setStatus(1);
			gdsDealCodeModel.setApprovalStatus(1);
			String tripType = flightBookingRequestBean.getTripType() != null && !flightBookingRequestBean.getTripType().isEmpty() ? flightBookingRequestBean.getTripType() : "";

			List<com.ws.services.flight.config.GDSDealCodeModel> newGdsDealCodeModelList = new ArrayList<>();
			gdsDealCodeModelList = orgManager.getDealCodeModelList(gdsDealCodeModel);
			if (gdsDealCodeModelList != null && !gdsDealCodeModelList.isEmpty()) {
				for (GDSDealCodeModel gdsDealCode : gdsDealCodeModelList) {
					gdsDealCode.setAirlineName(CommonUtil.fetchAirlineCodeById(airlineModels, gdsDealCode.getAirlineId()));
				}
			}
			if (tripType.equalsIgnoreCase(ONEWAY) || tripType.equalsIgnoreCase(ROUNDTRIP)) {

				String originAirportCode = flightBookingRequestBean.getOnwardFlightOption().getOrigin();
				ResultBean resultBeanOrigin = airportService.getAirportByAirportCode(originAirportCode);
				if(resultBeanOrigin!=null && !resultBeanOrigin.isError() && resultBeanOrigin.getResultObject()!=null) {
					AirportModal airportModal = (AirportModal)resultBeanOrigin.getResultObject();
					flightBookingRequestBean.getOnwardFlightOption().setOriginId(String.valueOf(airportModal.getAirportId()));
				}
				String destinationAirportCode = flightBookingRequestBean.getOnwardFlightOption().getDestination();
				ResultBean resultBeanDestination = airportService.getAirportByAirportCode(destinationAirportCode);
				if(resultBeanDestination!=null && !resultBeanDestination.isError() && resultBeanDestination.getResultObject()!=null) {
					AirportModal airportModal = (AirportModal)resultBeanDestination.getResultObject();
					flightBookingRequestBean.getOnwardFlightOption().setDestinationId(String.valueOf(airportModal.getAirportId()));
				}
				
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;

				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				
				boolean isCountryPosFound = false;
				
				if (gdsDealCodeModelList != null && !gdsDealCodeModelList.isEmpty()) {
					for (GDSDealCodeModel gdsDealCode : gdsDealCodeModelList) {
						
						com.ws.services.flight.config.GDSDealCodeModel gdsDealCodeModelWs = new com.ws.services.flight.config.GDSDealCodeModel();
						gdsDealCodeModelWs.setCredentialId(gdsDealCode.getCredentialId());
						gdsDealCodeModelWs.setValidFrom(gdsDealCode.getValidFrom());
						gdsDealCodeModelWs.setValidTo(gdsDealCode.getValidTo());
						gdsDealCodeModelWs.setSupplierId(gdsDealCode.getSupplierId());
						gdsDealCodeModelWs.setAirlineId(gdsDealCode.getAirlineId());
						gdsDealCodeModelWs.setAirlineCode(gdsDealCode.getAirlineCode());
						gdsDealCodeModelWs.setImportPNR(gdsDealCode.isImportPNR());
						gdsDealCodeModelWs.setDealCode(gdsDealCode.getDealCode());
						gdsDealCodeModelWs.setLastModTime(gdsDealCode.getLastModTime());
						
						isOriginAirportFound = false;
						isDestinationAirportFound = false;

						isOriginCountryFound = false;
						isDestinationCountryFound = false;

						isCountryPosFound = false;
						
						List<GDSDealCodeCountryModel> gdsCountryList = gdsDealCode.getGdsDealCountryList();
						List<GDSDealCodeAirportModel> gdsAirportList = gdsDealCode.getGdsDealAirporList();
						List<GDSDealCodeAgencyModel> gdsAgencyList = gdsDealCode.getGdsDealAgencyList();
						
						if (gdsDealCode.getIsCountrySelected() == 0 && gdsAirportList != null && !gdsAirportList.isEmpty()) {
							
							if(tripType.equalsIgnoreCase(ONEWAY)){
								
								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {

									if (flightBookingRequestBean.getOnwardFlightOption().getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 0)
										isOriginAirportFound = true;

									if (flightBookingRequestBean.getOnwardFlightOption().getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 1)
										isDestinationAirportFound = true;

									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
								}
								
							}
							else{

								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {

									if ((flightBookingRequestBean.getOnwardFlightOption().getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 0) || (flightBookingRequestBean.getOnwardFlightOption().getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 1))
										isOriginAirportFound = true;

									if ((flightBookingRequestBean.getOnwardFlightOption().getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 1) || (flightBookingRequestBean.getOnwardFlightOption().getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 0))
										isDestinationAirportFound = true;

									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
								}
							}

							if(isOriginAirportFound && isDestinationAirportFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
								for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
									if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
										isCountryPosFound = true;
										break;
									}
								}
								if (isCountryPosFound) {
									resultBeanAirline = geoLocationService.fetchObjectById(gdsDealCodeModelWs.getAirlineId(), AirlineModel.class);
									if(resultBeanAirline!=null && !resultBeanAirline.isError() && resultBeanAirline.getResultObject()!=null) {
										AirlineModel airlineModel = (AirlineModel)resultBeanAirline.getResultObject();
										gdsDealCodeModelWs.setAirlineCode(airlineModel.getAirlineCode());
									}
									newGdsDealCodeModelList.add(gdsDealCodeModelWs);
								}
							}	
						}

						else if (gdsDealCode.getIsCountrySelected() == 1 && gdsCountryList != null && !gdsCountryList.isEmpty()) {

							if(tripType.equalsIgnoreCase(ONEWAY)){
								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if (flightBookingRequestBean.getOnwardFlightOption().getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0)
										isOriginCountryFound = true;

									if (flightBookingRequestBean.getOnwardFlightOption().getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1)
										isDestinationCountryFound = true;
									
									if(isOriginCountryFound && isDestinationCountryFound) {
										break;
									}
								}
								
							}
							else{
								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if ((flightBookingRequestBean.getOnwardFlightOption().getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0) || (flightBookingRequestBean.getOnwardFlightOption().getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1))
										isOriginCountryFound = true;

									if ((flightBookingRequestBean.getOnwardFlightOption().getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1) || (flightBookingRequestBean.getOnwardFlightOption().getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0))
										isDestinationCountryFound = true;
									
									if(isOriginCountryFound && isDestinationCountryFound) {
										break;
									}
								}
							}
					
							if(isOriginCountryFound && isDestinationCountryFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
								for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
									if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
										isCountryPosFound = true;
										break;
									}
								}
								if (isCountryPosFound) {
									resultBeanAirline = geoLocationService.fetchObjectById(gdsDealCodeModelWs.getAirlineId(), AirlineModel.class);
									if(resultBeanAirline!=null && !resultBeanAirline.isError() && resultBeanAirline.getResultObject()!=null) {
										AirlineModel airlineModel = (AirlineModel)resultBeanAirline.getResultObject();
										gdsDealCodeModelWs.setAirlineCode(airlineModel.getAirlineCode());
									}
									newGdsDealCodeModelList.add(gdsDealCodeModelWs);
								}
							}
						}
					}
				}
			} else if (tripType.equalsIgnoreCase(MULTICITY)) {
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;
				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				boolean isCountryPosFound = false;
				if (gdsDealCodeModelList != null && !gdsDealCodeModelList.isEmpty()) {
					for (GDSDealCodeModel gdsDealCode : gdsDealCodeModelList) {
						com.ws.services.flight.config.GDSDealCodeModel gdsDealCodeModelWs = new com.ws.services.flight.config.GDSDealCodeModel();
						gdsDealCodeModelWs.setCredentialId(gdsDealCode.getCredentialId());
						gdsDealCodeModelWs.setValidFrom(gdsDealCode.getValidFrom());
						gdsDealCodeModelWs.setValidTo(gdsDealCode.getValidTo());
						gdsDealCodeModelWs.setSupplierId(gdsDealCode.getSupplierId());
						gdsDealCodeModelWs.setAirlineId(gdsDealCode.getAirlineId());
						gdsDealCodeModelWs.setAirlineCode(gdsDealCode.getAirlineCode());
						gdsDealCodeModelWs.setImportPNR(gdsDealCode.isImportPNR());
						gdsDealCodeModelWs.setDealCode(gdsDealCode.getDealCode());
						gdsDealCodeModelWs.setLastModTime(gdsDealCode.getLastModTime());
						isCountryPosFound = false;
						List<GDSDealCodeCountryModel> gdsCountryList = gdsDealCode.getGdsDealCountryList();
						List<GDSDealCodeAirportModel> gdsAirportList = gdsDealCode.getGdsDealAirporList();
						List<GDSDealCodeAgencyModel> gdsAgencyList = gdsDealCode.getGdsDealAgencyList();
						
						if (gdsDealCode.getIsCountrySelected() == 0 && gdsAirportList != null && !gdsAirportList.isEmpty()) {
							for (OptionSegmentBean eachOptionSegment : flightBookingRequestBean.getOnwardFlightOption().getOptionSegmentBean()) {
								
								String originAirportCode = eachOptionSegment.getOrigin();
								ResultBean resultBeanOrigin = airportService.getAirportByAirportCode(originAirportCode);
								if(resultBeanOrigin!=null && !resultBeanOrigin.isError() && resultBeanOrigin.getResultObject()!=null) {
									AirportModal airportModal = (AirportModal)resultBeanOrigin.getResultObject();
									eachOptionSegment.setOriginId(String.valueOf(airportModal.getAirportId()));
								}
								String destinationAirportCode = eachOptionSegment.getDestination();
								ResultBean resultBeanDestination = airportService.getAirportByAirportCode(destinationAirportCode);
								if(resultBeanDestination!=null && !resultBeanDestination.isError() && resultBeanDestination.getResultObject()!=null) {
									AirportModal airportModal = (AirportModal)resultBeanDestination.getResultObject();
									eachOptionSegment.setDestinationId(String.valueOf(airportModal.getAirportId()));
								}
								
								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {
									isOriginAirportFound = false;
									isDestinationAirportFound = false;
									if (eachOptionSegment.getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 0)
										isOriginAirportFound = true;

									if (eachOptionSegment.getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 1)
										isDestinationAirportFound = true;

									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
								}

								if(isOriginAirportFound && isDestinationAirportFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
									for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
										if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
											isCountryPosFound = true;
											break;
										}
									}
									if (isCountryPosFound) {
										resultBeanAirline = geoLocationService.fetchObjectById(gdsDealCodeModelWs.getAirlineId(), AirlineModel.class);
										if(resultBeanAirline!=null && !resultBeanAirline.isError() && resultBeanAirline.getResultObject()!=null) {
											AirlineModel airlineModel = (AirlineModel)resultBeanAirline.getResultObject();
											gdsDealCodeModelWs.setAirlineCode(airlineModel.getAirlineCode());
										}
										newGdsDealCodeModelList.add(gdsDealCodeModelWs);
									}
								}
							}
						}

						if (gdsDealCode.getIsCountrySelected() == 1 && gdsCountryList != null && !gdsCountryList.isEmpty()) {

							for (OptionSegmentBean eachOptionSegment : flightBookingRequestBean.getOnwardFlightOption().getOptionSegmentBean()) {
								isOriginCountryFound = false;
								isDestinationCountryFound = false;

								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if (eachOptionSegment.getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0)
										isOriginCountryFound = true;

									if (eachOptionSegment.getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1)
										isDestinationCountryFound = true;
									
									if(isOriginCountryFound && isDestinationCountryFound) {
										break;
									}
								}
								if(isOriginCountryFound && isDestinationCountryFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
									for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
										if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
											isCountryPosFound = true;
											break;
										}
									}
									if (isCountryPosFound) {
										resultBeanAirline = geoLocationService.fetchObjectById(gdsDealCodeModelWs.getAirlineId(), AirlineModel.class);
										if(resultBeanAirline!=null && !resultBeanAirline.isError() && resultBeanAirline.getResultObject()!=null) {
											AirlineModel airlineModel = (AirlineModel)resultBeanAirline.getResultObject();
											gdsDealCodeModelWs.setAirlineCode(airlineModel.getAirlineCode());
										}
										newGdsDealCodeModelList.add(gdsDealCodeModelWs);
									}
								}
							}
						}
					}
				}
			}
			resultBean.setIserror(false);
			resultBean.setResultList((List<com.ws.services.flight.config.GDSDealCodeModel>) newGdsDealCodeModelList);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(0, e);
		}
		return resultBean;
	}
	
	public ResultBean getDealCodeListForImportPnr(ImportPNRPricingRequestBean importPnrPricingRequestBean, int pccId, Integer countryId) {
		ResultBean resultBean = new ResultBean();
		try {
			List<GDSDealCodeModel> gdsDealCodeModelList;
			ResultBean resultBeanAirline;
			GDSDealCodeModel gdsDealCodeModel = new GDSDealCodeModel();
			gdsDealCodeModel.setCredentialId(pccId);
			gdsDealCodeModel.setStatus(1);
			gdsDealCodeModel.setApprovalStatus(1);
			
			List<com.ws.services.flight.config.GDSDealCodeModel> newGdsDealCodeModelList = new ArrayList<>();
			gdsDealCodeModelList = orgManager.getDealCodeModelList(gdsDealCodeModel);
			
				boolean isOriginAirportFound = false;
				boolean isDestinationAirportFound = false;
				boolean isOriginCountryFound = false;
				boolean isDestinationCountryFound = false;
				boolean isCountryPosFound = false;
				
				if (gdsDealCodeModelList != null && !gdsDealCodeModelList.isEmpty()) {
					for (GDSDealCodeModel gdsDealCode : gdsDealCodeModelList) {
						com.ws.services.flight.config.GDSDealCodeModel gdsDealCodeModelWs = new com.ws.services.flight.config.GDSDealCodeModel();
						gdsDealCodeModelWs.setCredentialId(gdsDealCode.getCredentialId());
						gdsDealCodeModelWs.setValidFrom(gdsDealCode.getValidFrom());
						gdsDealCodeModelWs.setValidTo(gdsDealCode.getValidTo());
						gdsDealCodeModelWs.setSupplierId(gdsDealCode.getSupplierId());
						gdsDealCodeModelWs.setAirlineId(gdsDealCode.getAirlineId());
						gdsDealCodeModelWs.setAirlineCode(gdsDealCode.getAirlineCode());
						gdsDealCodeModelWs.setImportPNR(gdsDealCode.isImportPNR());
						gdsDealCodeModelWs.setDealCode(gdsDealCode.getDealCode());
						gdsDealCodeModelWs.setLastModTime(gdsDealCode.getLastModTime());
						isCountryPosFound = false;
						List<GDSDealCodeCountryModel> gdsCountryList = gdsDealCode.getGdsDealCountryList();
						List<GDSDealCodeAirportModel> gdsAirportList = gdsDealCode.getGdsDealAirporList();
						List<GDSDealCodeAgencyModel> gdsAgencyList = gdsDealCode.getGdsDealAgencyList();
						
						if (gdsDealCode.getIsCountrySelected() == 0 && gdsAirportList != null && !gdsAirportList.isEmpty()) {
							for (OptionSegmentBean eachOptionSegment : importPnrPricingRequestBean.getOptionSegmentBeanList()) {
					
								String originAirportCode = eachOptionSegment.getOrigin();
								ResultBean resultBeanOrigin = airportService.getAirportByAirportCode(originAirportCode);
								if(resultBeanOrigin!=null && !resultBeanOrigin.isError() && resultBeanOrigin.getResultObject()!=null) {
									AirportModal airportModal = (AirportModal)resultBeanOrigin.getResultObject();
									eachOptionSegment.setOriginId(String.valueOf(airportModal.getAirportId()));
								}
								String destinationAirportCode = eachOptionSegment.getDestination();
								ResultBean resultBeanDestination = airportService.getAirportByAirportCode(destinationAirportCode);
								if(resultBeanDestination!=null && !resultBeanDestination.isError() && resultBeanDestination.getResultObject()!=null) {
									AirportModal airportModal = (AirportModal)resultBeanDestination.getResultObject();
									eachOptionSegment.setDestinationId(String.valueOf(airportModal.getAirportId()));
								}
								isOriginAirportFound = false;
								isDestinationAirportFound = false;
								
								for (GDSDealCodeAirportModel eachAirportModal : gdsAirportList) {
									
									if (eachOptionSegment.getOriginId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 0)
										isOriginAirportFound = true;

									if (eachOptionSegment.getDestinationId().equalsIgnoreCase(String.valueOf(eachAirportModal.getAirportId())) && eachAirportModal.getIsSource() == 1)
										isDestinationAirportFound = true;

									if(isOriginAirportFound && isDestinationAirportFound) {
										break;
									}
								}

								if(isOriginAirportFound && isDestinationAirportFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
									for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
										if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
											isCountryPosFound = true;
											break;
										}
									}
									if (isCountryPosFound) {
										resultBeanAirline = geoLocationService.fetchObjectById(gdsDealCodeModelWs.getAirlineId(), AirlineModel.class);
										if(resultBeanAirline!=null && !resultBeanAirline.isError() && resultBeanAirline.getResultObject()!=null) {
											AirlineModel airlineModel = (AirlineModel)resultBeanAirline.getResultObject();
											gdsDealCodeModelWs.setAirlineCode(airlineModel.getAirlineCode());
										}
										newGdsDealCodeModelList.add(gdsDealCodeModelWs);
									}
								}
							}
						}

						else if (gdsDealCode.getIsCountrySelected() == 1 && gdsCountryList != null && !gdsCountryList.isEmpty()) {

							for (OptionSegmentBean eachOptionSegment : importPnrPricingRequestBean.getOptionSegmentBeanList()) {
								isOriginCountryFound = false;
								isDestinationCountryFound = false;

								for (GDSDealCodeCountryModel eachCountryModal : gdsCountryList) {
									if (eachOptionSegment.getOriginCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 0)
										isOriginCountryFound = true;

									if (eachOptionSegment.getDestinationCountry().equalsIgnoreCase(String.valueOf(eachCountryModal.getCountryId())) && eachCountryModal.getIsSource() == 1)
										isDestinationCountryFound = true;
									if(isOriginCountryFound && isDestinationCountryFound) {
										break;
									}
								}
								if(isOriginCountryFound && isDestinationCountryFound && gdsAgencyList!=null && !gdsAgencyList.isEmpty()) {
									for(GDSDealCodeAgencyModel gdsAgency : gdsAgencyList) {
										if(String.valueOf(gdsAgency.getAgencyId()).equals(String.valueOf(countryId))) {
											isCountryPosFound = true;
											break;
										}
									}
									if (isCountryPosFound) {
										resultBeanAirline = geoLocationService.fetchObjectById(gdsDealCodeModelWs.getAirlineId(), AirlineModel.class);
										if(resultBeanAirline!=null && !resultBeanAirline.isError() && resultBeanAirline.getResultObject()!=null) {
											AirlineModel airlineModel = (AirlineModel)resultBeanAirline.getResultObject();
											gdsDealCodeModelWs.setAirlineCode(airlineModel.getAirlineCode());
										}
										newGdsDealCodeModelList.add(gdsDealCodeModelWs);
									}
								}
							}
						}
					}
				}
			
			resultBean.setIserror(false);
			resultBean.setResultList((List<com.ws.services.flight.config.GDSDealCodeModel>) newGdsDealCodeModelList);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(0, e);
		}
		return resultBean;
	}

	public ResultBean fetchNationalityName(String nationalityCode) {
		ResultBean resultBean = new ResultBean();
		try {
			List<Object> nationalityList=orgManager.fetchNationalityName(nationalityCode);
			resultBean.setResultList(nationalityList);
			resultBean.setIserror(false);
		} catch (Exception e) {
			resultBean.setIserror(true);
			CallLog.printStackTrace(0, e);
		}
		return resultBean;
	}
	
	
	/*
	 * This method is used to filter the deal code based on journey date onward and return journey date
	 */
	public List<com.ws.services.flight.config.GDSDealCodeModel> getFilteredDealCode(List<com.ws.services.flight.config.GDSDealCodeModel> lccDealCodeList,FlightSearchRequestBean flightRequestBean)
	{
		String onwardJourneyDate = flightRequestBean.getOnwardDate();
		String returnJourneyDate = "";
		List<com.ws.services.flight.config.GDSDealCodeModel> newLccDealCode = new ArrayList<>();
		boolean roundTripStatus = false;
		Date retJourneyDate = null;
		try
		{
				 if(flightRequestBean.getTripType().equalsIgnoreCase("RoundTrip"))
				 {
					roundTripStatus = true;
					returnJourneyDate = flightRequestBean.getReturnDate();
					retJourneyDate = new SimpleDateFormat("yyyy-MM-dd").parse(returnJourneyDate);
				  }
				  Date onJourneyDate = new SimpleDateFormat("yyyy-MM-dd").parse(onwardJourneyDate);
				  if(null != lccDealCodeList)
				  {
					  for(int i = 0; i< lccDealCodeList.size();i++)
					  {
						 if(lccDealCodeList.get(i).getValidFrom().compareTo(onJourneyDate) * onJourneyDate.compareTo(lccDealCodeList.get(i).getValidTo())  >= 0)
						  {
							 if(roundTripStatus)
							 {
								 if(lccDealCodeList.get(i).getValidFrom().compareTo(retJourneyDate) * retJourneyDate.compareTo(lccDealCodeList.get(i).getValidTo())  >= 0)
								  {
									 newLccDealCode.add(lccDealCodeList.get(i));
								  }
								 
							 }
							 else
							   newLccDealCode.add(lccDealCodeList.get(i));
						  }
					  }
				  }
		}
		catch(Exception ex)
		{
			CallLog.printStackTrace(0, ex);
		}
		return newLccDealCode;
	}
	
	
}
