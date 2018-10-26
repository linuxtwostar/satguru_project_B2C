package com.tt.ts.rest.pax.controller;

import java.util.Date;
import java.util.List;

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
import com.tt.ts.rest.common.util.CommonUtil;
import com.tt.ts.rest.pax.model.PaxModel;
import com.tt.ts.rest.pax.model.PaxRelationModel;
import com.tt.ts.rest.pax.service.PaxService;

@RestController
@RequestMapping("/pax")
public class PaxRestController {

	private static final String FAILED = "Failed";

	@Autowired
	private PaxService paxService;

	@RequestMapping(value = "/paxAction", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxAction(@RequestBody PaxModel paxModal) {

		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.savePax(paxModal);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
				response.setErrorCode(resultBean.getErrorCode());
			} else {

				response.setResultObject(resultBean.getResultObject());
			}
			ObjectMapper objMapper = new ObjectMapper();
			jsonString = objMapper.writeValueAsString(response);
		} catch (Exception ex) {
			TTLog.printStackTrace(0, ex);
		}
		return jsonString;
	}

	@RequestMapping(value = "/paxValidation", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxValidation(@RequestBody PaxModel paxModal) {
		ResultBean resultBean = paxService.paxValidation(paxModal);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/paxUpdateAction", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxUpdateAction(@RequestBody PaxModel paxModal) {

		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.updatePax(paxModal);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@RequestMapping(value = "/airlinePrefDetails", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String airlinePrefDetails(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.fetchAirlinePrefDetails(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@RequestMapping(value = "/paxFrequentFlyrDetails", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxFrequentFlyrDetails(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.fetchPaxFrequentFlyrDetails(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@RequestMapping(value = "/paxDocumentDetails", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxDocumentDetails(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.fetchPaxDocumentDetails(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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
	
	@RequestMapping(value = "/paxSearchResultCount", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxSearchResultCount(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {

			ResultBean resultBean = paxService.searchPaxListCount(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@RequestMapping(value = "/paxSearchResult", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxSearchResult(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {

			ResultBean resultBean = paxService.searchPaxList(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@RequestMapping(value = "/validateEmail", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String validateEmail(@RequestBody PaxModel paxModel) {
		ResultBean resultBean = paxService.validatePaxList(paxModel);
		return CommonUtil.convertIntoJson(resultBean);
	}

	@RequestMapping(value = "/paxSearchResultByNameMob", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxSearchResultByNameMob(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.paxSearchResultByNameMob(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@RequestMapping(value = "/colleagueSearchResult", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String colleagueSearchResult(@RequestBody PaxModel paxModel) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.searchColleagueList(paxModel);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/coPaxSearchResult", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String coPaxSearchResult(@RequestBody Integer paxId) {
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.searchPaxRelationList(paxId);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
				response.setErrorMsg("Something went wrong.");
				response.setErrorCode(resultBean.getErrorCode());
			} else {
				List<PaxRelationModel> list = (List<PaxRelationModel>) resultBean.getResultList();
				if (list != null && !list.isEmpty()) {
					for (PaxRelationModel prm : list) {
						prm.setCoPaxId(prm.getPaxModel().getId());
					}
				}
				response.setResultList(list);

			}

			ObjectMapper objMapper = new ObjectMapper();
			jsonString = objMapper.writeValueAsString(response);
		} catch (Exception ex) {
			TTLog.error(0, ex);
		}
		return jsonString;
	}

	@RequestMapping(value = "/paxRelationAction", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String paxRelationAction(@RequestBody PaxModel paxModal) {

		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.savePaxRelation(paxModal);
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

	@RequestMapping(value = "/saveUpdatePax", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String saveUpdatePax(@RequestBody PaxModel paxModal) {

		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			ResultBean resultBean = paxService.saveUpdatePax(paxModal);
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

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delPaxFamily", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String delPaxFamily(@RequestBody Integer paxId) {
		PaxModel paxModel = new PaxModel();
		paxModel.setId(paxId);
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			PaxModel paxModal = null;
			List<PaxModel> paxList = (List<PaxModel>) paxService.searchPaxList(paxModel).getResultList();
			if (paxList != null && !paxList.isEmpty()) {
				paxModal = paxList.get(0);
				paxModal.setStatus(0);
				paxModal.setUpdateDate(new Date());
			}
			ResultBean resultBean = paxService.saveUpdatePax(paxModal);
			if (resultBean.isError()) {
				response.setResponseStatus(FAILED);
				response.setErrorCode(resultBean.getErrorCode());
			} else {
				paxService.deletePaxRelation(paxModal);
			}
			ObjectMapper objMapper = new ObjectMapper();
			jsonString = objMapper.writeValueAsString(response);
		} catch (Exception ex) {
			TTLog.error(0, ex);
		}
		return jsonString;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delPaxColleague", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String delPaxColleague(@RequestBody Integer paxId) {
		PaxModel paxModel = new PaxModel();
		paxModel.setId(paxId);
		ServiceResponse response = new ServiceResponse();
		String jsonString = "";
		try {
			PaxModel paxModal = null;
			List<PaxModel> paxList = (List<PaxModel>) paxService.searchPaxList(paxModel).getResultList();
			if (paxList != null && !paxList.isEmpty()) {
				paxModal = paxList.get(0);
			}
			paxService.deletePaxRelation(paxModal);
			ObjectMapper objMapper = new ObjectMapper();
			jsonString = objMapper.writeValueAsString(response);
		} catch (Exception ex) {
			TTLog.error(0, ex);
		}
		return jsonString;
	}

}
