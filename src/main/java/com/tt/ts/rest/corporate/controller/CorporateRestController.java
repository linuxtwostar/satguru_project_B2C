package com.tt.ts.rest.corporate.controller;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.ServiceResponse;
import com.tt.nc.common.util.TTLog;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.corporate.service.CorporateService;

@RestController
@RequestMapping("/corporate")
public class CorporateRestController {

    private static final String FAILED = "Failed";
    
    @Autowired
    private CorporateService corporateServices;

    @RequestMapping(value = "/corporateSaveAction", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String corporateSaveAction(@RequestBody CorporateModel corporateModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.saveCorporate(corporateModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong. Please try Creating Corporate after sometime");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultObject(resultBean.getResultObject());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
    
    @RequestMapping(value = "/corporateUpdateAction", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String corporateUpdateAction(@RequestBody CorporateModel corporateModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.updateCorporate(corporateModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong. Please try Creating Corporate after sometime");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultObject(resultBean.getResultObject());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
    
    @RequestMapping(value = "/corporateTravelDetails", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String corporateTravelDetails(@RequestBody CorporateModel corporateModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.fetchTravelCrdntDetails(corporateModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultList(resultBean.getResultList());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }

    @RequestMapping(value = "/corporateFinanceContactDetails", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String corporateFinanceContactDetails(@RequestBody CorporateModel corporateModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.fetchFinanceContactDetails(corporateModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultList(resultBean.getResultList());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }

    @RequestMapping(value = "/corporateSearchResult", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String corporateSearchResult(@RequestBody CorporateModel corporateModel) {
	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.searchCorporateList(corporateModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultList(resultBean.getResultList());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
    
    @RequestMapping(value = "/checkDuplicateCorporate", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String checkDuplicateCorporate(@RequestBody CorporateModel corporateModel) {
	ServiceResponse response = new ServiceResponse();
	response.setResultBoolean(false);
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.searchCorporateListByName(corporateModel);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong.");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		if(resultBean.getResultList()!=null && !resultBean.getResultList().isEmpty()) {
		    response.setResultBoolean(true);
		} 	
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
    
    @RequestMapping(value = "/saveUpdateCorporate", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public String saveUpdateCorporate(@RequestBody CorporateModel corporateModal) {

	ServiceResponse response = new ServiceResponse();
	String jsonString = "";
	try {
	    ResultBean resultBean = corporateServices.saveUpdateCorporate(corporateModal);
	    if (resultBean.isError()) {
		response.setResponseStatus(FAILED);
		response.setErrorMsg("Something went wrong. Please try Creating Pax after sometime");
		response.setErrorCode(resultBean.getErrorCode());
	    } else {
		response.setResultObject(resultBean.getResultObject());
	    }
	    ObjectMapper objMapper = new ObjectMapper();
	    jsonString = objMapper.writeValueAsString(response);
	} catch (Exception ex) {
	    TTLog.error(0, ex);
	}
	return jsonString;
    }
    

}
