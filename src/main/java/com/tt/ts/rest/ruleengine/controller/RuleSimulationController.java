package com.tt.ts.rest.ruleengine.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tt.ts.airline.service.AirlineService;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.geolocations.service.GeoLocationService;
import com.tt.ts.product.service.ProductService;
import com.tt.ts.rest.common.util.GenericHelperUtil;
import com.tt.ts.rest.corporate.service.CorporateService;
import com.tt.ts.rest.insurance.model.InsuranceCommonJsonModel;
import com.tt.ts.rest.insurance.model.InsuranceWidget;
import com.tt.ts.rest.organization.service.OrganizationService;
import com.tt.ts.rest.ruleengine.service.RuleSimulationService;
import com.tt.ts.ruleengine.service.SimulationRuleService;
import com.tt.ws.rest.hotel.bean.HotelSearchRespDataBean;
import com.ws.services.insurance.bean.product.FProduct;

@RestController
@RequestMapping("/restRule")
public class RuleSimulationController
{

	@Autowired
	SimulationRuleService simulationRuleService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private RuleSimulationService ruleSimulationService;

	@Autowired
	private GeoLocationService geoLocationService;

	@Autowired
	private CorporateService corporateService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AirlineService airlineService;

	@RequestMapping(value = "/applyRuleRestHotel", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String getApplyRuleHotelResult(@RequestBody HotelSearchRespDataBean hotelCommonJsonModel)
	{
		return  ruleSimulationService.getApplyRuleHotelResult(hotelCommonJsonModel);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/applyRuleRestInsurance", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String applyRuleRestInsurance(@RequestBody InsuranceCommonJsonModel insuranceCommonJsonModel)
	{
		String jsonString;
		GenericHelperUtil<FProduct> genHelper = new GenericHelperUtil<>();
		InsuranceWidget insuranceWidget = insuranceCommonJsonModel.getInsuranceWidget();
		//ProductPriceRequestBean requestBean = insuranceCommonJsonModel.getP
		List<FProduct> fProduct = insuranceCommonJsonModel.getfProduct();
		String countryId = insuranceCommonJsonModel.getCountryId();
		String branchId = insuranceCommonJsonModel.getBranchId();
		String agencyId = insuranceCommonJsonModel.getAgencyId();
		ResultBean resultBean = null;
	//	ResultBean resultBean = ruleSimulationService.applyRuleOnInsurance1(fProduct, insuranceWidget, simulationRuleService, organizationService,insuranceCommonJsonModel.getAgencyMarkUp(),countryId,branchId,agencyId,geoLocationService);
		jsonString = genHelper.getJsonStringByListEntity((List<FProduct>) resultBean.getResultObject());

		return jsonString;
	}
	
}
