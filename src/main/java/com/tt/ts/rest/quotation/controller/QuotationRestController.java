package com.tt.ts.rest.quotation.controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.tt.ts.rest.common.CommonModal;
import com.tt.ts.rest.common.util.GenericHelperUtil;
import com.tt.ts.rest.quotation.model.QuotationCommonBean;
import com.tt.ts.rest.quotation.model.QuotationModel;
import com.tt.ts.rest.quotation.service.QuotationService;

@RestController
@RequestMapping("/quotation")
public class QuotationRestController {

	@Autowired
	private QuotationService quotationService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse responseServlet;
	
	@RequestMapping(value = "/saveQuotation", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String saveQuotation(@RequestBody QuotationCommonBean quotationCommonBean) {
		ResultBean resultBean;
		ServiceResponse response = new ServiceResponse();
		resultBean = quotationService.processQuotationModel(quotationCommonBean);
		resultBean = quotationService.saveQuotationData((QuotationModel)resultBean.getResultObject());
		if (resultBean.isError()) {
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
		}
		return convertIntoJson(response);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchQuotationByQuoteNo", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchQuotationByQuoteNo(@RequestBody CommonModal commonModal) {
		ResultBean resultBean = new ResultBean();
		ServiceResponse response = new ServiceResponse();
		resultBean = quotationService.fetchQuotationByQuoteNo(commonModal.getQuoteNo());
		GenericHelperUtil<QuotationModel> utilHelperQuote = new GenericHelperUtil<QuotationModel>();
		if (!resultBean.isError() && resultBean.getResultList().size() > 0) {
			String jsonString = "";
			try {
				jsonString = utilHelperQuote
						.getJsonStringByListEntity((List<QuotationModel>) resultBean
								.getResultList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setJsonString(jsonString);
		} else {
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
			response.setErrorCode(resultBean.getErrorCode());
		}
		return convertIntoJson(response);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchQuoteByUserId", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchQuoteByUserId(@RequestBody CommonModal commonModal) {
		ResultBean resultBean = new ResultBean();
		ServiceResponse response = new ServiceResponse();
		resultBean = quotationService.fetchQuotationByUserId(commonModal.getUserId());
		GenericHelperUtil<QuotationModel> utilHelperQuote = new GenericHelperUtil<QuotationModel>();
		if (!resultBean.isError() && resultBean.getResultList().size() > 0) {
			String jsonString = "";
			try {
				jsonString = utilHelperQuote
						.getJsonStringByListEntity((List<QuotationModel>) resultBean
								.getResultList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setJsonString(jsonString);
		} else {
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
			response.setErrorCode(resultBean.getErrorCode());
		}
		return convertIntoJson(response);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchQuoteByUserIdAndDateRange", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String fetchQuoteByUserIdAndDateRange(@RequestBody CommonModal commonModal) {
		ResultBean resultBean = new ResultBean();
		ServiceResponse response = new ServiceResponse();
		resultBean = quotationService.fetchQuoteByUserIdAndDateRange(commonModal.getUserId(), commonModal.getStartDate(), commonModal.getEndDate());
		GenericHelperUtil<QuotationModel> utilHelperQuote = new GenericHelperUtil<QuotationModel>();
		if (!resultBean.isError() && resultBean.getResultList().size() > 0) {
			String jsonString = "";
			try {
				jsonString = utilHelperQuote
						.getJsonStringByListEntity((List<QuotationModel>) resultBean
								.getResultList());
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.setJsonString(jsonString);
		} else {
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
			response.setErrorCode(resultBean.getErrorCode());
		}
		return convertIntoJson(response);
	}
	
	@RequestMapping(value = "/downloadQuotePdf", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String downloadQuotePdf(@RequestBody QuotationModel quotationModel) {
		ResultBean resultBean = new ResultBean();
		ServiceResponse response = new ServiceResponse();
		final ServletContext servletContext = request.getSession().getServletContext();
		final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		final String temperotyFilePath = tempDirectory.getAbsolutePath();

		String fileName = "Quotation.pdf";
		responseServlet.setContentType("application/pdf");
		responseServlet.setHeader("Content-disposition", "attachment; filename=" + fileName);
		resultBean = quotationService.downloadQuotePdf(temperotyFilePath + "\\" + fileName, quotationModel,responseServlet);
		if (resultBean.isError()) {
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
		}
		return convertIntoJson(response);
	}
	

	@RequestMapping(value = "/sentMailQuotation", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public String sentMailQuotation(@RequestBody QuotationCommonBean quotationCommonBean) {
		ResultBean resultBean = new ResultBean();
		ServiceResponse response = new ServiceResponse();
		QuotationModel model = new QuotationModel();
		resultBean = quotationService.sentMailQuotation(model);
		if (resultBean.isError()) {
			response.setResponseStatus("Failed");
			response.setErrorMsg(resultBean.getErrorMessage());
		}
		return convertIntoJson(response);
	}
	
	private String convertIntoJson(ServiceResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(response);
		} catch (Exception e) {
			TTLog.info(0, "[convertIntoJson] Exception  :" + e.getMessage());
			e.printStackTrace();
		}
		return jsonString;
	}
	
	
}
