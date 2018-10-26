package com.tt.ts.rest.corporate.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.common.errorConstant.ErrorCodeContant;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.common.util.CommonUtil;
import com.tt.ts.geolocations.manager.GeoLocationManager;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.rest.corporate.manager.CorporateManager;
import com.tt.ts.rest.corporate.model.CorporateFinanceContact;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.corporate.model.CorporateTravelCordinator;

@Service
@Component(value = "CorporateService")
public class CorporateService {

    @Autowired
    private CorporateManager corporateManger;

    @Autowired
    private GeoLocationManager geoLocationManager;

    public ResultBean saveCorporate(CorporateModel corporateModel) {
	ResultBean resultBean = new ResultBean();
	try {

	    corporateModel.setCreationTime(new Date());
	    corporateModel.setStatus(1);
	
	    for (int i = 0; i < corporateModel.getCorporateFinanceContact().size(); i++) {
		corporateModel.getCorporateFinanceContact().get(i).setSequence(i);
		corporateModel.getCorporateFinanceContact().get(i).setCorporateModel(corporateModel);
	    }
	    for (int i = 0; i < corporateModel.getCorporateTravelCordinator().size(); i++) {
		corporateModel.getCorporateTravelCordinator().get(i).setSequence(i);
		corporateModel.getCorporateTravelCordinator().get(i).setCorporateModel(corporateModel);
	    }

	    CorporateModel corporateMdl = corporateManger.saveCorporate(corporateModel);
	    List<CountryBean> countryBeans = geoLocationManager.fetchCountry();

	    if (corporateMdl.getCountryId() != null && corporateMdl.getCountryId() > 0) {
		CountryBean countryBean = new CountryBean();
		countryBean.setCountryId(corporateMdl.getCountryId());
		corporateMdl.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));

	    }
	    resultBean.setIserror(false);
	    resultBean.setResultObject(corporateMdl);

	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }

    public ResultBean updateCorporate(CorporateModel corporateModel) {
	ResultBean resultBean = new ResultBean();
	try {

	    corporateModel.setLastUpdationTime(new Date());

	    for (int i = 0; i < corporateModel.getCorporateFinanceContact().size(); i++) {
		corporateModel.getCorporateFinanceContact().get(i).setSequence(i);
		corporateModel.getCorporateFinanceContact().get(i).setCorporateModel(corporateModel);
	    }
	    for (int i = 0; i < corporateModel.getCorporateTravelCordinator().size(); i++) {
		corporateModel.getCorporateTravelCordinator().get(i).setSequence(i);
		corporateModel.getCorporateTravelCordinator().get(i).setCorporateModel(corporateModel);
	    }

	    CorporateModel corporateMdl = corporateManger.updateCorporate(corporateModel);
	    List<CountryBean> countryBeans = geoLocationManager.fetchCountry();

	    if (corporateMdl.getCountryId() != null && corporateMdl.getCountryId() > 0) {
		CountryBean countryBean = new CountryBean();
		countryBean.setCountryId(corporateMdl.getCountryId());
		corporateMdl.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));

	    }
	    resultBean.setIserror(false);
	    resultBean.setResultObject(corporateMdl);

	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }
    
    public ResultBean fetchTravelCrdntDetails(CorporateModel corporateModel) {
	ResultBean resultBean = new ResultBean();
	try {
	    List<CorporateTravelCordinator> list = corporateManger.fetchTravelCrdntDetails(corporateModel.getCorpId());
	    resultBean.setResultList(list);
	    resultBean.setIserror(false);
	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }

    public ResultBean fetchFinanceContactDetails(CorporateModel corporateModel) {
	ResultBean resultBean = new ResultBean();
	try {
	    List<CorporateFinanceContact> list = corporateManger.fetchFinanceContactDetails(corporateModel.getCorpId());
	    resultBean.setResultList(list);
	    resultBean.setIserror(false);
	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }

    public ResultBean searchCorporateList(CorporateModel corporateModel) {
	ResultBean resultBean = new ResultBean();
	try {
	    List<CorporateModel> list = corporateManger.searchCorporateList(corporateModel);
	    List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
	    if (list != null && !list.isEmpty()) {
		for (CorporateModel cModel : list) {

		    if (cModel.getCountryId() != null && cModel.getCountryId() > 0) {
			CountryBean countryBean = new CountryBean();
			countryBean.setCountryId(cModel.getCountryId());
			cModel.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
		    }

		}
	    }
	    resultBean.setResultList(list);
	    resultBean.setIserror(false);
	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }

    public ResultBean searchCorporateListByName(CorporateModel corporateModel) {
	ResultBean resultBean = new ResultBean();
	try {
	    List<CorporateModel> list = corporateManger.searchCorporateListByName(corporateModel);
	    resultBean.setResultList(list);
	    resultBean.setIserror(false);
	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }

    public ResultBean getCorporateListByAgencyId(Integer groupId) {
	ResultBean resultBean = new ResultBean();
	try {
	    List<CorporateModel> list = corporateManger.getCorporateListByAgencyId(groupId);
	    List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
	    if (list != null && !list.isEmpty()) {
		for (CorporateModel cModel : list) {
		    if (cModel.getCountryId() != null && cModel.getCountryId() > 0) {
			CountryBean countryBean = new CountryBean();
			countryBean.setCountryId(cModel.getCountryId());
			cModel.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
		    }
		}
	    }
	    resultBean.setResultList(list);
	    resultBean.setIserror(false);
	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }
    
    public ResultBean saveUpdateCorporate(CorporateModel corparateModal) {
	ResultBean resultBean = new ResultBean();
	try {
	    if (corparateModal != null) {
		corporateManger.saveUpdateCorporate(corparateModal);
	    }

	} catch (Exception e) {
	    resultBean.setIserror(true);
	    TTLog.error(0, e);
	}
	return resultBean;
    }
    
    public ResultBean getCurrencyRate(String fromCurrency,String toCurrecny) {
    	ResultBean resultBean = new ResultBean();
    	try {
    		List<Object> currRate =corporateManger.getCurrencyRate(fromCurrency,toCurrecny);
    		
    		if(currRate!=null && !currRate.isEmpty()){
    			Object objectArr = (Object)currRate.get(0);
    			if(objectArr!=null){
    				double currencyRate=(double) objectArr;
    				resultBean.setResultString(currencyRate+"");
    			}
    		}
    	} catch (Exception e) {
    	    resultBean.setIserror(true);
    	    TTLog.error(0, e);
    	}
    	return resultBean;
        }
    
    public ResultBean getCurrencyId(String currencyCode) {
    	ResultBean resultBean = new ResultBean();
    	try {
    		List<Object> currId =corporateManger.getCurrencyId(currencyCode);
    		
    		if(currId!=null && !currId.isEmpty()){
    			Object objectArr = (Object)currId.get(0);
    			if(objectArr!=null){
    				String currencyId=(String) objectArr;
    				resultBean.setResultString(currencyId);
    			}
    		}

    	} catch (Exception e) {
    	    resultBean.setIserror(true);
    	    TTLog.error(0, e);
    	}
    	return resultBean;
        }
    
    //add by amit
    
	public ResultBean groupId(int id) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		try{
			List<Object> userIdList= corporateManger.groupId(id);
			//groupId = paxManager.groupId(id);
			resultBean.setResultList(userIdList);
		}
		catch (Exception e)
		{
			TTLog.error(0, e);
			TTLog.printStackTrace(0, e);
		}
		return resultBean;

	}
	public ResultBean checkDuplicateCorporate(String branchId, String firstName, String email)
	{
		ResultBean resultBean = new ResultBean();
		try {
			boolean status = corporateManger.checkDuplicate(branchId, firstName,email );
			
				resultBean.setIserror(false);
				resultBean .setResultBoolean(status);
		
		} catch (Exception e) {
			resultBean.setIserror(true);
			String errorCode = ErrorCodeContant.DATA_NOT_FETCH_DESIGNATION;
			resultBean.setErrorCode(errorCode);
			//resultBean.setErrorMessage(ResourceBundle.getBundle(APPLICATION_RESOURCES, LocaleContextHolder.getLocale()).getString(errorCode));
			TTLog.printStackTrace(16, errorCode, e);
		}

		return resultBean;
	}

	
	
	public ResultBean fetchCompanyName(String corpId)
	{
		   ResultBean resultBean = new ResultBean();
		   try {
			List<CorporateModel> corporateModels = corporateManger.fetCompanyName(corpId);
			resultBean.setResultList(corporateModels);
			resultBean.setIserror(false);
		    } catch (Exception e) {
			resultBean.setIserror(true);
			TTLog.printStackTrace(16,e);
		    }
            return resultBean;
	}

     //Added By Pramod for SAT-14018 on 08-08-2018
	 public ResultBean searchCorporateListByLoggedInUser(CorporateModel corporateModel) {
			ResultBean resultBean = new ResultBean();
			try {
			    List<CorporateModel> list = corporateManger.searchCorporateListByLoggedInUser(corporateModel);
			    List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
			    if (list != null && !list.isEmpty()) {
				for (CorporateModel cModel : list) {

				    if (cModel.getCountryId() != null && cModel.getCountryId() > 0) {
					CountryBean countryBean = new CountryBean();
					countryBean.setCountryId(cModel.getCountryId());
					int a=0;
					cModel.setCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
				    }

				}   
			    }
			    resultBean.setResultList(list);
			    resultBean.setIserror(false);
			} catch (Exception e) {
			    resultBean.setIserror(true);
			    System.out.println(e);
			    TTLog.error(0, e);
			}
			return resultBean;
		    }
}
