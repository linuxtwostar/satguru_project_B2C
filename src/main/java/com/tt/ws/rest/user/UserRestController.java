package com.tt.ws.rest.user;

import java.util.List;
import java.util.Random;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.ServiceResponse;
import com.tt.nc.common.util.TTLog;
import com.tt.nc.login.services.LoginService;
import com.tt.nc.user.model.User;
import com.tt.nc.user.services.UserService;
import com.tt.nc.user.util.UserUtil;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airline.service.AirlineService;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.blacklist.service.BlackOutFlightService;
import com.tt.ts.bsp.service.BspService;
import com.tt.ts.common.enums.service.EnumService;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.currency.model.CurrencyBean;
import com.tt.ts.currency.service.CurrencyService;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterModel;
import com.tt.ts.knowledgecenter.service.KnowledgeCenterService;
import com.tt.ts.language.service.LanguageService;
import com.tt.ts.organization.model.OrganizationFinancialModel;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.agent.model.AgencyMarkupModel;
import com.tt.ts.rest.agent.service.AgentService;
import com.tt.ts.rest.common.CommonModal;
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.forgotpass.modal.ForgotPasswordModal;
import com.tt.ts.rest.forgotpass.service.ForgotPasswordService;
import com.tt.ts.rest.organization.service.OrganizationService;
import com.tt.ts.rest.register.model.RegisterModel;
import com.tt.ts.rest.register.service.RegisterService;
import com.tt.ts.staff.service.StaffService;
import com.tt.ts.uccf.modal.UCCFModal;
import com.tt.ts.uccf.service.UCCFService;
import com.tt.ws.rest.hotel.services.HotelServices;
import com.ws.services.flight.bean.booking.FlightBookingRequestBean;

@RestController
@RequestMapping("/user")
public class UserRestController
{
	@Autowired
	private LoginService loginService;

	@Autowired
	private UserService userService;

	@Autowired
	private StaffService staffService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrganizationService orgService;

	@Autowired
	private BlackOutFlightService blackOutFlightService;

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	private UCCFService uccfService;

	@Autowired
	private KnowledgeCenterService knowledgeCenterService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private BspService bspService;

	@Autowired
	private RegisterService registerService;

	@Autowired
	private EnumService enumService;

	@Autowired
	private GeoLocationService geoLocationService;

	@Autowired
	private LanguageService languageService;

	@Autowired
	private HotelServices hotelService;

	@Autowired
	private CorporateService corporateServiceService;
	
	@Autowired
	private AgentService agentServices;
	
	@Autowired
	private AirlineService airlineService;
	


	@RequestMapping(value = "/productDetail", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getProduct(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		if (modal!= null && modal.getGroupId()!=null && modal.getGroupId() != 0)
		{
			ResultBean resultBean = orgService.getProductById(modal.getGroupId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed to get Product");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/supplierDetail", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getSupplier(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		if (modal.getGroupId() != null && modal.getProductId() != null)
		{
			ResultBean resultBean = orgService.getSupplierById(modal.getGroupId(), modal.getProductId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/supplierCredentialDetail", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getCredential(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		if (modal.getGroupId() != null && modal.getProductId() != null && modal.getSupplierId() != null)
		{
			ResultBean resultBean = orgService.getCredentialById(modal.getGroupId(), modal.getProductId(), modal.getSupplierId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/getSupplierCredentialById", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getSupplierCredentialById(@RequestBody CommonModal modal)
	{
		ResultBean resultBean = null;
		if (modal.getGroupId() != null)
		{
			 resultBean = orgService.getSupplierCredentialById(modal.getGroupId());
			
		}
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/getCompanyDetailsById", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getCompanyDetailsById(@RequestBody CommonModal modal)
	{
		ResultBean resultBean = new ResultBean();
		if (modal.getUserId() != null && modal.getOrganizationType() != null)
		{
			 resultBean = orgService.getCompanyDetailsById(modal.getUserId(), modal.getOrganizationType());
		}
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/loginCheck1", method = RequestMethod.POST)
	public String getLogin1(@RequestHeader String uName, @RequestHeader String password, @RequestHeader String agencyCode)
	{
		ResultBean resultBean = new ResultBean();
		ServiceResponse response = new ServiceResponse();
		User user = new User();
		user.setUserAlias(uName);
		user.setPassword(password);

		List<User> userList;
		String jsonString = ""; 
		String password1 = user.getPassword();
		String hql = UserUtil.preparedQueryByUserName(user.getUserAlias());
		userList = userService.getUsersByQuery(hql);// checks user exist
		if (userList != null && userList.size() == 1)
		{ // user Exist
			User loggedUser = userList.get(0);
			String userPassword = loggedUser.getPassword();
			int disableStatus = loggedUser.getDisableSignIn();
			int userStatus = loggedUser.getUserStatus();
			resultBean= agentServices.getUserMapping(loggedUser.getUserId(), agencyCode);
			boolean isAgencyCodeExist = resultBean.getResultBoolean();
			if ((disableStatus == 0 || userStatus == 0) || loggedUser.getUserType() != 3 || !isAgencyCodeExist)
			{
				response.setJsonString("Invalid Username or Password");//Not authorized
			}
			else
			{
				try
				{
					if (password1.equals(UserUtil.decodeString(userPassword)))
					{
						response.setJsonString("Login Successful");
						response.setResultObject(loggedUser);
					}
					else
						response.setJsonString("Invalid Username or Password");//Invalid password !
				}
				catch (Exception e)
				{
					response.setErrorMsg(e.getMessage());
					TTLog.info(0, "[LoginRestController][loginCheck] resultBean  :::::::::> " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		else
		{
			response.setJsonString("Invalid Username or Password");//User does not exist
		}
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			jsonString = mapper.writeValueAsString(response);
		}
		catch (Exception e)
		{
			response.setResponseStatus("Failed");
			response.setErrorMsg(e.getMessage());
			TTLog.info(0, "[LoginRestController][loginCheck] Total User  :::::::::" + e.getMessage());
		}
		return jsonString;
	}

	// gets the details of user by its Id
	@RequestMapping(value = "/userDetail", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getuser(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		ResultBean resultBean = staffService.getUserById(modal.getUserId());
		User u = (User) resultBean.getResultObject();
		TTLog.info(0, "[staffDetail] Staff getResultObject  :" + resultBean.getResultObject());
		if (u != null)
		{
			response.setResultObject(u);
		}
		else
		{
			response.setJsonString("");
			response.setResponseStatus("Failed");
			response.setErrorMsg("No data found");
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/getAllAirlines", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAirline(@RequestBody AirlineModel airlineModel)
	{
		ResultBean resultBean = airlineService.getAirlinesNew(airlineModel);
		return CommonUtil.convertIntoJson(resultBean);
	}
	@RequestMapping(value = "/getAllAirlinesNames", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAirline()
	{
		AirlineModel am=new AirlineModel();
		ResultBean resultBean = airlineService.getAirlinesNew(am);
		return CommonUtil.convertIntoJson(resultBean);
	}
	@RequestMapping(value = "/getAirports", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAirport(@RequestBody AirportModal airportModel)
	{
		ResultBean resultBean = orgService.searchAirport(airportModel);
		return resultBean.getResultString();
	}
	@RequestMapping(value = "/getAllAirports", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAllAirports(@RequestBody AirportModal airportModel)
	{
		ResultBean resultBean = orgService.searchParticularAirport(airportModel);
		return resultBean.getResultString();
	}
	@RequestMapping(value = "/getAllAirportsData", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAllAirportsData()
	{
		ResultBean resultBean = orgService.searchAllAirportNew(null);
		return CommonUtil.convertIntoJson(resultBean.getResultList());
	}

	/*@RequestMapping(value = "/fetchBlacklistDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String fetchBlacklistDetails(@RequestBody BlackOutFlightModel blackOutFlightModel)
	{
		ResultBean resultBean = orgService.searchBlackOutFlight(blackOutFlightModel);
		return CommonUtil.convertIntoJson(resultBean);
	}*/

	@RequestMapping(value = "/fetchKnowledgeCenterDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String fetchKnowledgeCenterDetails(@RequestBody KnowledgeCenterModel kcModel)
	{
		ResultBean resultBean = orgService.searchKnowledgeCentersData(kcModel);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String changePassword(@RequestBody ForgotPasswordModal forgotPasswordModal)
	{
		ResultBean resultBean = forgotPasswordService.forgotPassOrChange(forgotPasswordModal);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/registerAction", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String registerAction(@RequestBody RegisterModel register)
	{
		ResultBean resultBean = registerService.registerUser(register);
		if (resultBean.isError())
			return "Something went wrong. Please try again after sometime";
		else
			return "Successfully Registered.";
	}
	@RequestMapping(value = "/currencyDetail", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getCurrencyDetail(@RequestBody CurrencyBean currencyBean)
	{
		ResultBean resultBean = currencyService.searchCurrency(currencyBean);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/getUccfByCountryId", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getUccfByCountryId(@RequestBody UCCFModal uccfModal)
	{
		ResultBean resultBean = uccfService.searchUccfData(uccfModal);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/isUccf", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String isUccf(@RequestBody UCCFModal uccfModal)
	{
		ResultBean resultBean = uccfService.searchUccfData(uccfModal);
		return CommonUtil.convertIntoJson(resultBean);
	}

	/*@RequestMapping(value = "/bspCommission", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getbspCommission(@RequestBody FlightBookingRequestBean flightBookingRequestBean)
	{
		ResultBean resultBean = orgService.getBspList(flightBookingRequestBean,122);
		return CommonUtil.convertIntoJson(resultBean);
	}*/

	@RequestMapping(value = "/fetchCountry", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchCountry(@RequestBody CountryBean countryBean)
	{
		ResultBean resultBean = orgService.fetchCountry(countryBean);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/fetchLanguage", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchLanguage()
	{
		ResultBean resultBean = languageService.fetchLanguage();
		
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/generateOtp", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String generateOtp()
	{
		Random random = new Random();
		return  String.format("%04d", random.nextInt(10000));
	}

	@RequestMapping(value = "/fetchEnum", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchEnum(@RequestBody CommonModal commonModal)
	{
		ResultBean resultBean = enumService.getEnumList(commonModal.getJsonString());
		
		return CommonUtil.convertIntoJson(resultBean);
	}
	
	@RequestMapping(value = "/fetchEnumByValue", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchEnumByValue(@RequestBody String columnVal)
	{
		ResultBean resultBean = orgService.getEnumListByValue(columnVal);
		return CommonUtil.convertIntoJson(resultBean);
	}
	
	@RequestMapping(value = "/fetchDestinationByCode", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchDestinationByCode(@RequestBody String columnVal)
	{
		ResultBean resultBean = orgService.getAirportDetailsByCode(columnVal);
		return CommonUtil.convertIntoJson(resultBean);
	}
	

	@RequestMapping(value = "/fetchEnumByIntCode", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchEnumByIntCode(@RequestBody CommonModal commonModal)
	{
		ResultBean resultBean = enumService.getEnumList(commonModal.getColName(), commonModal.getIntCode());
		
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/fetchAgencyCreditLimit", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchAgencyCreditLimit(@RequestBody CommonModal commonModal)
	{
		ServiceResponse response = new ServiceResponse();
		ResultBean resultBean = orgService.fetchAgencyCreditLimit(commonModal.getGroupId());
		if (resultBean.getResultList() != null)
		{
			OrganizationFinancialModel financialModel = (OrganizationFinancialModel) resultBean.getResultList().get(0);
			String creditLimit = String.valueOf(financialModel.getWalletAmount());
			response.setJsonString(creditLimit);
			
		}
		if (resultBean.isError())
		{
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getResultString());
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/getAllHotelCities", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAllHotelCities()
	{
		ServiceResponse response = new ServiceResponse();
		ResultBean resultBean = hotelService.fetchHotelCities();

		if (!resultBean.getResultList().isEmpty())
		{
			response.setResultList(resultBean.getResultList());
		}
		if (resultBean.isError())
		{
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/getAllHotelCountries", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAllHotelCountries()
	{

		ServiceResponse response = new ServiceResponse();
		ResultBean resultBean = geoLocationService.fetchCountry();

		if (!resultBean.getResultList().isEmpty())
		{
			response.setResultList(resultBean.getResultList());
		}
		if (resultBean.isError())
		{
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
		}
		return CommonUtil.convertIntoJson(response);
	}


	

	@RequestMapping(value = "/getProductSupplierCredentialById", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getProductSupplierCredentialById(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "getProductSupplierCredentialById :: ccc::: Group Id = " + modal.getGroupId());
		if (modal.getGroupId() != null)
		{
			ResultBean resultBean = orgService.getProductSupplierCredentialById(modal.getGroupId(), modal.getProductId(), modal.getSupplierId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}

	@RequestMapping(value = "/getServiceConfigCredential", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getServiceConfigCredential(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "getServiceConfigCredential :: ::: Group Id = " + modal.getGroupId());
		if (modal.getGroupId() != null)
		{
			ResultBean resultBean = orgService.getServiceConfigCredential(modal.getGroupId(), modal.getColName(), modal.getCountryId(),null);
			response.setResultList(resultBean.getResultList());
		}
		return CommonUtil.convertIntoJson(response);
	}
	
	/*@RequestMapping(value = "/gdsSearchSupplierCredential", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String gdsSearchSupplierCredential(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "gdsSearchSupplierCredential :: ::: Group Id = " + modal.getGroupId());
		if (modal.getGroupId() != null)
		{
			ResultBean resultBean = orgService.gdsSearchSupplierCredential(modal.getGroupId());
			response.setResultList(resultBean.getResultList());
		}
		return CommonUtil.convertIntoJson(response);
	}*/
	/*@RequestMapping(value = "/gdsTicketingSupplierCredential", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String gdsTicketingSupplierCredential(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "getProductSupplierCredentialById :: ::: Group Id = " + modal.getGroupId());
		if (modal.getGroupId() != null)
		{
			ResultBean resultBean = orgService.gdsTicketingSupplierCredential(modal.getGroupId());
			response.setResultList(resultBean.getResultList());
		}
		return CommonUtil.convertIntoJson(response);
	}*/
	@RequestMapping(value = "/getCorporateListByAgencyId", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getCorporateListByAgencyId(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "getProductSupplierCredentialById :: ::: Group Id = " + modal.getGroupId());
		if (modal.getGroupId() != null)
		{
			ResultBean resultBean = corporateServiceService.getCorporateListByAgencyId(modal.getGroupId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	@RequestMapping(value = "/getAirlineListByAgencyId", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAirlineListByAgencyId(@RequestBody CommonModal modal) {
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "getAgencyListByAgencyId :: ::: Group Id = "
				+ modal.getGroupId());
		if (modal.getGroupId() != null) {
			ResultBean resultBean = orgService.getAirlinesByAgencyId(modal.getGroupId(), modal.getDomOrIntl());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError()) {
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	
	@RequestMapping(value = "/getProfileImage", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getProfileImage(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "Group Id getProfileImage= " + modal.getUserId() + " ::  ::  " + modal.getGroupId());
		if (modal.getGroupId() != null)
		{
			ResultBean resultBean = orgService.getProfileImage(modal.getGroupId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	
	@RequestMapping(value = "/getMarkupRecordByUser", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getMarkupRecordByUser(@RequestBody CommonModal modal)
	{
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "Group Id = " + modal.getUserId() + " ::  ::  " + modal.getCorporateOrRetail());
		if (modal.getUserId() != null)
		{
			ResultBean resultBean = orgService.getMarkupRecordByUser(modal.getUserId(), modal.getCorporateOrRetail(), modal.getIntCode());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	
	@RequestMapping(value="/getGdsSupplierList")
	public String getGdsSupplierList(@RequestBody CommonModal modal){
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "Group Id = " + modal.getOrganizationId() );
		if (modal.getOrganizationId() != null)
		{
			ResultBean resultBean = orgService.getGdsSupplierList(modal.getOrganizationId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	
	
	@RequestMapping(value="/getRestrictedAirlineListByAgengy")
	public String getRestrictedAirlineListByAgengy(@RequestBody CommonModal modal){
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "Group Id = " + modal.getOrganizationId() );
		if (modal.getOrganizationId() != null)
		{
			ResultBean resultBean = orgService.getRestrictedAirlineListByAgengy(modal.getOrganizationId());
			response.setResultList(resultBean.getResultList());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	
	@RequestMapping(value="/getAgentType")
	public String getAgentType(@RequestBody CommonModal modal){
		ServiceResponse response = new ServiceResponse();
		TTLog.error(0, "Group Id = " + modal.getUserId() );
		if (modal.getUserId() != null)
		{
			ResultBean resultBean = orgService.getAgentType(modal.getUserId());
			response.setResultInteger(resultBean.getResultInteger());
			if (resultBean.isError())
			{
				response.setResponseStatus("Failed");
				response.setErrorMsg(resultBean.getResultString());
			}
		}
		return CommonUtil.convertIntoJson(response);
	}
	@RequestMapping(value="/uccfSearchData")
	public String uccfSearchData(@RequestBody FlightBookingRequestBean bookingRequestBean){
		ResultBean resultBean = orgService.calculateUccf(bookingRequestBean);
		return CommonUtil.convertIntoJson(resultBean);
	}
	

	@RequestMapping(value = "/fetchAgencyMarkupRest", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchAgencyMarkupRest(@RequestBody AgencyMarkupModel agencyMarkup)
	{
		ResultBean resultBean = agentServices.fetchAgencyMarkup(agencyMarkup);
		return CommonUtil.convertIntoJson(resultBean);
	}
	
	@RequestMapping(value = "/getAllAirportsCountryCity", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAllAirportsCountryCity(@RequestBody AirportModal airportModel)
	{
		ResultBean resultBean = orgService.getAllAirportsCountryCity(airportModel);
		return CommonUtil.convertIntoJson(resultBean);
	}
	/*@RequestMapping(value = "/fetchTagFlight", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getAllAirportsCountryCity(@RequestBody FlightTagModel tagFlight)
	{
		ResultBean resultBean = orgService.searchFlightTag(tagFlight);
		return CommonUtil.convertIntoJson(resultBean);
	}
	*/
	@RequestMapping(value = "/getFactObjectIdAgencyBranch", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getFactObjectIdAgencyBranch(@RequestBody CommonModal modal)
	{
		ResultBean resultBean = orgService.getFactObjectIdAgencyBranch(modal);
		return CommonUtil.convertIntoJson(resultBean);
	}
	
	@RequestMapping(value = "/getCompanyDetailsByAgentId", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getCompanyDetailsByAgenId(@RequestBody CommonModal modal)
	{
		ResultBean resultBean = new ResultBean();
		if (modal.getGroupId() != null && modal.getOrganizationType() != null)
		{
			 resultBean = orgService.getCompanyDetailsByAgentId(modal.getGroupId(), modal.getOrganizationType());
		}
		return CommonUtil.convertIntoJson(resultBean);
	}
	
}
