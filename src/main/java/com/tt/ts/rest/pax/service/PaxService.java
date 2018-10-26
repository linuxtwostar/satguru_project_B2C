package com.tt.ts.rest.pax.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tt.nc.common.util.TTLog;
import com.tt.satguruportal.util.common.TTPortalLog;
import com.tt.ts.common.enums.manager.EnumManager;
import com.tt.ts.common.enums.model.EnumModel;
import com.tt.ts.common.errorConstant.ErrorCodeContant;
import com.tt.ts.common.modal.ResultBean;
import com.tt.ts.common.util.CommonUtil;
import com.tt.ts.geolocations.manager.GeoLocationManager;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.corporate.manager.CorporateManager;
import com.tt.ts.rest.corporate.model.CorporateModel;
import com.tt.ts.rest.pax.manager.PaxManager;
import com.tt.ts.rest.pax.model.PaxAirlinePrefernce;
import com.tt.ts.rest.pax.model.PaxDocumentModel;
import com.tt.ts.rest.pax.model.PaxFrequentFlyer;
import com.tt.ts.rest.pax.model.PaxModel;
import com.tt.ts.rest.pax.model.PaxRelationModel;
import com.tt.ts.validation.StringValidation;

@Service
public class PaxService
{

	@Autowired
	private PaxManager paxManager;

	@Autowired
	private GeoLocationManager geoLocationManager;

	@Autowired
	CorporateManager corporateManager;

	@Autowired
	EnumManager enumManager;

	public ResultBean savePax(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();

		try
		{
 			if (paxModal != null)
			{
				paxModal.setCreatedDate(new Date());
				paxModal.setUpdateDate(new Date());
				paxModal.setStatus(1);
				int seq = 0;
 				List<PaxAirlinePrefernce> paxAirlinePrefList = new ArrayList<>();
 				for (int i = 0; i < paxModal.getPaxAirlinePrefernce().size(); i++)
				{
 					if (paxModal.getPaxAirlinePrefernce().get(i).getAirline() != null && !paxModal.getPaxAirlinePrefernce().get(i).getAirline().isEmpty())
					{
						PaxAirlinePrefernce paxAirPref = new PaxAirlinePrefernce();
						paxAirPref.setAirline(paxModal.getPaxAirlinePrefernce().get(i).getAirline());
						paxAirPref.setSequence(seq);
						paxAirPref.setPaxModel(paxModal);
						paxAirlinePrefList.add(paxAirPref);
						seq++;
					}

				}
 				paxModal.setPaxAirlinePrefernce(paxAirlinePrefList);
				int seqq = 0;
 				List<PaxFrequentFlyer> frqFlyrList = new ArrayList<>();
				for (int i = 0; i < paxModal.getPaxFrequentFlyer().size(); i++)
				{
					if (paxModal.getPaxFrequentFlyer().get(i).getFreqFlyr() != null && !paxModal.getPaxFrequentFlyer().get(i).getFreqFlyr().isEmpty())
					{
						PaxFrequentFlyer freqFlyr = new PaxFrequentFlyer();
						freqFlyr.setFreqFlyr(paxModal.getPaxFrequentFlyer().get(i).getFreqFlyr());
						freqFlyr.setFrequentFlyerNumber(paxModal.getPaxFrequentFlyer().get(i).getFrequentFlyerNumber());
						freqFlyr.setSequence(seqq);
						freqFlyr.setPaxModel(paxModal);
						frqFlyrList.add(freqFlyr);
						seqq++;
					}
				}
 				paxModal.setPaxFrequentFlyer(frqFlyrList);

				if (paxModal.getPaxRelationList() != null && !paxModal.getPaxRelationList().isEmpty())
				{
					List<PaxRelationModel> paxRelationList = paxModal.getPaxRelationList();
					for (PaxRelationModel paxRelationModel : paxRelationList)
					{
						paxRelationModel.setPaxId(paxModal.getMainPaxId());
						paxRelationModel.setPaxModel(paxModal);
					}
				}
				PaxModel paxModel = paxManager.saveUpdatePax(paxModal);

				List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
				CountryBean countryBean;
				if (paxModal.getPassportIssueCountry() != null && paxModal.getPassportIssueCountry() > 0)
				{
					countryBean = new CountryBean();
					countryBean.setCountryId(paxModal.getPassportIssueCountry());
					paxModal.setPassportIssuedCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
				}

				if (paxModal.getNationality() != null && !paxModal.getNationality().isEmpty() && StringValidation.IsNumericString(paxModal.getNationality()) && Integer.parseInt(paxModal.getNationality()) > 0)
				{
					countryBean = new CountryBean();
					countryBean.setCountryId(Integer.parseInt(paxModal.getNationality()));
					paxModal.setNationalityStr(CommonUtil.getCountryNameById(countryBeans, countryBean));
				}
				if (paxModel.getCompanyId() != null && paxModel.getCompanyId() > 0)
				{
					CorporateModel corporateModel = new CorporateModel();
					corporateModel.setCorpId(paxModel.getCompanyId());
					List<CorporateModel> corporateList = corporateManager.searchCorporateList(corporateModel);
					if (corporateList != null && !corporateList.isEmpty())
					{
						paxModel.setCompanyName(corporateList.get(0).getCorporateName());
						countryBean = new CountryBean();
						countryBean.setCountryId(corporateList.get(0).getCountryId());
						paxModel.setCompanyLocation(CommonUtil.getCountryNameById(countryBeans, countryBean));
						paxModel.setCompanyLocation(corporateList.get(0).getCountryName());
					}
				}

				resultBean.setIserror(false);
				resultBean.setResultInteger(paxModel.getId());
				resultBean.setResultObject(paxModel);
			}

		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.printStackTrace(0, e);
		}
		return resultBean;
	}

	public ResultBean updatePax(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();

		try
		{
			if (paxModal != null)
			{
				paxModal.setUpdateDate(new Date());
				int seq = 0;
				List<PaxAirlinePrefernce> paxAirlinePrefList = new ArrayList<>();
				for (int i = 0; i < paxModal.getPaxAirlinePrefernce().size(); i++)
				{
					if (paxModal.getPaxAirlinePrefernce().get(i).getAirline() != null && !paxModal.getPaxAirlinePrefernce().get(i).getAirline().isEmpty())
					{
						PaxAirlinePrefernce paxAirPref = new PaxAirlinePrefernce();
						paxAirPref.setAirline(paxModal.getPaxAirlinePrefernce().get(i).getAirline());
						paxAirPref.setSequence(seq);
						paxAirPref.setPaxModel(paxModal);
						paxAirlinePrefList.add(paxAirPref);
						seq++;
					}

				}
				paxModal.setPaxAirlinePrefernce(paxAirlinePrefList);
				int seqq = 0;
				List<PaxFrequentFlyer> frqFlyrList = new ArrayList<>();
				for (int i = 0; i < paxModal.getPaxFrequentFlyer().size(); i++)
				{
					if (paxModal.getPaxFrequentFlyer().get(i).getFreqFlyr() != null && !paxModal.getPaxFrequentFlyer().get(i).getFreqFlyr().isEmpty())
					{
						PaxFrequentFlyer freqFlyr = new PaxFrequentFlyer();
						freqFlyr.setFreqFlyr(paxModal.getPaxFrequentFlyer().get(i).getFreqFlyr());
						freqFlyr.setFrequentFlyerNumber(paxModal.getPaxFrequentFlyer().get(i).getFrequentFlyerNumber());
						freqFlyr.setSequence(seqq);
						freqFlyr.setPaxModel(paxModal);
						frqFlyrList.add(freqFlyr);
						seqq++;
					}
				}
				paxModal.setPaxFrequentFlyer(frqFlyrList);
				PaxModel paxModel = paxManager.updatePaxDetail(paxModal);

				List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
				CountryBean countryBean;
				if (paxModal.getPassportIssueCountry() != null && paxModal.getPassportIssueCountry() > 0)
				{
					countryBean = new CountryBean();
					countryBean.setCountryId(paxModal.getPassportIssueCountry());
					paxModal.setPassportIssuedCountryName(CommonUtil.getCountryNameById(countryBeans, countryBean));
				}

				if (paxModal.getNationality() != null && !paxModal.getNationality().isEmpty() && StringValidation.IsNumericString(paxModal.getNationality()) && Integer.parseInt(paxModal.getNationality()) > 0)
				{
					countryBean = new CountryBean();
					countryBean.setCountryId(Integer.parseInt(paxModal.getNationality()));
					paxModal.setNationalityStr(CommonUtil.getCountryNameById(countryBeans, countryBean));
				}
				if (paxModel.getCompanyId() != null && paxModel.getCompanyId() > 0)
				{
					CorporateModel corporateModel = new CorporateModel();
					corporateModel.setCorpId(paxModel.getCompanyId());
					List<CorporateModel> corporateList = corporateManager.searchCorporateList(corporateModel);
					if (corporateList != null && !corporateList.isEmpty())
					{
						paxModel.setCompanyName(corporateList.get(0).getCorporateName());
						countryBean = new CountryBean();
						countryBean.setCountryId(corporateList.get(0).getCountryId());
						paxModel.setCompanyLocation(CommonUtil.getCountryNameById(countryBeans, countryBean));
						paxModel.setCompanyLocation(corporateList.get(0).getCountryName());
					}
				}

				resultBean.setIserror(false);
				resultBean.setResultInteger(paxModel.getId());
				resultBean.setResultObject(paxModel);
			}

		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean fetchAirlinePrefDetails(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxAirlinePrefernce> list = paxManager.fetchAirlinePrefDetails(paxModel.getId());
			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean fetchPaxFrequentFlyrDetails(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxFrequentFlyer> list = paxManager.fetchPaxFrequentFlyrDetails(paxModel.getId());
			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean fetchPaxDocumentDetails(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxDocumentModel> list = paxManager.fetchPaxDocumentDetails(paxModel.getId());
			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean searchPaxListCount(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<Object> list = paxManager.searchPaxListCount(paxModel);

			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}
	public ResultBean searchPaxList(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxModel> list = paxManager.searchPaxList(paxModel);
			List<CountryBean> countryBeans = geoLocationManager.fetchCountry();

			if (list != null && !list.isEmpty())
			{
				List<EnumModel> genderList = enumManager.getEnumList("PAX_GENDER");
				List<EnumModel> titleList = enumManager.getEnumList("PAX_TITLE");
				for (PaxModel paxMdl : list)
				{
					if(paxMdl.getPhoneCode() == null) {
						paxMdl.setPhoneCode("");
					}
					List<CorporateModel> corporateList;
					if (paxMdl.getCompanyId() != null && paxMdl.getCompanyId() > 0)
					{
						CorporateModel corporateModel = new CorporateModel();
						corporateModel.setCorpId(paxMdl.getCompanyId());
						corporateList = corporateManager.searchCorporateList(corporateModel);
						if (corporateList != null && !corporateList.isEmpty())
						{
							paxMdl.setCompanyName(corporateList.get(0).getCorporateName());

							CountryBean countryBean = new CountryBean();
							countryBean.setCountryId(corporateList.get(0).getCountryId());
							paxMdl.setCompanyLocation(CommonUtil.getCountryNameById(countryBeans, countryBean));
						}
					}
					CountryBean cntryBean = new CountryBean();
					if (paxMdl.getPassportIssueCountry() != null && paxMdl.getPassportIssueCountry() > 0)
					{
						cntryBean.setCountryId(paxMdl.getPassportIssueCountry());
						paxMdl.setPassportIssuedCountryName(CommonUtil.getCountryNameById(countryBeans, cntryBean));
					}

					if (paxMdl.getNationality() != null && !paxMdl.getNationality().isEmpty() && StringValidation.IsNumericString(paxMdl.getNationality()))
					{
						cntryBean = new CountryBean();
						cntryBean.setCountryId(Integer.parseInt(paxMdl.getNationality()));
						paxMdl.setNationalityStr(CommonUtil.getCountryNameById(countryBeans, cntryBean));
						cntryBean.setSearchCountryCode("Yes");
						paxMdl.setNationalityCode(CommonUtil.getCountryNameById(countryBeans, cntryBean));
					}

					Date dob = paxMdl.getDob();
					if (dob != null)
					{
						paxMdl.setAge(com.tt.ts.rest.common.util.CommonUtil.calAgeFromDob(dob));
					}

					if (genderList != null && !genderList.isEmpty())
					{
						for (EnumModel enumModel : genderList)
						{
							if (paxMdl.getGender()!= null && paxMdl.getGender().equals(enumModel.getIntCode()))
							{
								paxMdl.setGenderName(enumModel.getValue());
								break;
							}
						}
					}

					if (titleList != null && !titleList.isEmpty())
					{
						for (EnumModel enumModel : titleList)
						{
							if (paxMdl.getTitle() != null && paxMdl.getTitle().equals(enumModel.getIntCode()))
							{
								paxMdl.setTitleName(enumModel.getValue());
								break;
							}
						}
					}
				}
			}

			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean validatePaxList(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxModel> list = paxManager.searchPaxList(paxModel);

			if (list != null && !list.isEmpty())
			{
				resultBean.setResultBoolean(true);
			}else{
				resultBean.setResultBoolean(false);
			}
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean paxSearchResultByNameMob(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxModel> list = paxManager.paxSearchResultByNameMob(paxModel);
			List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
			if (list != null && !list.isEmpty())
			{
				for (PaxModel paxMdl : list)
				{
					if(paxMdl.getPhoneCode() == null) {
						paxMdl.setPhoneCode("");
					}
					List<CorporateModel> corporateList;
					if (paxMdl.getCompanyId() != null && paxMdl.getCompanyId() > 0)
					{
						CorporateModel corporateModel = new CorporateModel();
						corporateModel.setCorpId(paxMdl.getCompanyId());
						corporateList = corporateManager.searchCorporateList(corporateModel);
						if (corporateList != null && !corporateList.isEmpty())
						{
							paxMdl.setCompanyName(corporateList.get(0).getCorporateName());

							CountryBean countryBean = new CountryBean();
							countryBean.setCountryId(corporateList.get(0).getCountryId());
							paxMdl.setCompanyLocation(CommonUtil.getCountryNameById(countryBeans, countryBean));
						}
					}
					CountryBean cntryBean = new CountryBean();
					if (paxMdl.getPassportIssueCountry() != null && paxMdl.getPassportIssueCountry() > 0)
					{
						cntryBean.setCountryId(paxMdl.getPassportIssueCountry());
						paxMdl.setPassportIssuedCountryName(CommonUtil.getCountryNameById(countryBeans, cntryBean));
					}

					if (paxMdl.getNationality() != null && !paxMdl.getNationality().isEmpty() && StringValidation.IsNumericString(paxMdl.getNationality()))
					{
						cntryBean = new CountryBean();
						cntryBean.setCountryId(Integer.parseInt(paxMdl.getNationality()));
						paxMdl.setNationalityStr(CommonUtil.getCountryNameById(countryBeans, cntryBean));
						cntryBean.setSearchCountryCode("Yes");
						paxMdl.setNationalityCode(CommonUtil.getCountryNameById(countryBeans, cntryBean));
						
					}

					Date dob = paxMdl.getDob();
					if (dob != null)
					{
						paxMdl.setAge(com.tt.ts.rest.common.util.CommonUtil.calAgeFromDob(dob));
					}

				}
			}

			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean searchColleagueList(PaxModel paxModel)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxModel> list = paxManager.searchColleagueList(paxModel);
			List<CountryBean> countryBeans = geoLocationManager.fetchCountry();
			if (list != null && !list.isEmpty())
			{
				for (PaxModel paxMdl : list)
				{
					List<CorporateModel> corporateList;
					if (paxMdl.getCompanyId() != null && paxMdl.getCompanyId() > 0)
					{
						CorporateModel corporateModel = new CorporateModel();
						corporateModel.setCorpId(paxMdl.getCompanyId());
						corporateList = corporateManager.searchCorporateList(corporateModel);
						if (corporateList != null && !corporateList.isEmpty())
						{
							paxMdl.setCompanyName(corporateList.get(0).getCorporateName());

							CountryBean countryBean = new CountryBean();
							countryBean.setCountryId(corporateList.get(0).getCountryId());
							paxMdl.setCompanyLocation(CommonUtil.getCountryNameById(countryBeans, countryBean));
						}
					}
					CountryBean cntryBean = new CountryBean();
					if (paxMdl.getPassportIssueCountry() != null && paxMdl.getPassportIssueCountry() > 0)
					{
						cntryBean.setCountryId(paxMdl.getPassportIssueCountry());
						paxMdl.setPassportIssuedCountryName(CommonUtil.getCountryNameById(countryBeans, cntryBean));
					}

					if (paxMdl.getNationality() != null && !paxMdl.getNationality().isEmpty() && StringValidation.IsNumericString(paxMdl.getNationality()))
					{
						cntryBean = new CountryBean();
						cntryBean.setCountryId(Integer.parseInt(paxMdl.getNationality()));
						paxMdl.setNationalityStr(CommonUtil.getCountryNameById(countryBeans, cntryBean));
						cntryBean.setSearchCountryCode("Yes");
						paxMdl.setNationalityCode(CommonUtil.getCountryNameById(countryBeans, cntryBean));
					}

					Date dob = paxMdl.getDob();
					if (dob != null)
					{
						paxMdl.setAge(com.tt.ts.rest.common.util.CommonUtil.calAgeFromDob(dob));
					}

				}
			}

			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean searchPaxRelationList(Integer paxId)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			List<PaxRelationModel> list = paxManager.searchPaxRelationList(paxId);
			resultBean.setResultList(list);
			resultBean.setIserror(false);
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean savePaxRelation(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();

		try
		{
			int id;
			if (paxModal != null)
			{
				PaxRelationModel paxRelationModel = new PaxRelationModel();
				paxRelationModel.setPaxId(paxModal.getMainPaxId());
				paxRelationModel.setPaxType(0);// for colleague
				paxRelationModel.setPaxModel(paxModal);
				id = paxManager.savePaxRelation(paxRelationModel);
				if (id > 0)
				{
					resultBean.setIserror(false);
					resultBean.setResultObject(id);
				}

			}

		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean saveUpdatePax(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			if (paxModal != null)
			{
				paxManager.saveUpdatePax(paxModal);
			}

		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean deletePaxRelation(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			if (paxModal != null && paxModal.getPaxRelationList() != null && !paxModal.getPaxRelationList().isEmpty())
			{
				List<String> queryList = new ArrayList<>();
				List<Object> paramList = new ArrayList<>();
				String paxRelationDeletion = QueryConstantRest.GET_DELETE_FOR_PAX_RELATION;
				if (paxRelationDeletion != null && !paxRelationDeletion.isEmpty())
				{
					queryList.add(paxRelationDeletion);
				}
				paramList.add(paxModal.getId());
				paramList.add(paxModal.getPaxRelationList().get(0).getPaxId());
				paxManager.deleteChildTableListItem(queryList, paramList);
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}

	public ResultBean paxValidation(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();
		try
		{
			if (paxModal != null)
			{
				List<PaxModel> paxModalList= paxManager.paxValidation(paxModal);
				if(paxModalList!=null && !paxModalList.isEmpty()){
					resultBean.setResultBoolean(true);
					PaxModel pax= paxModalList.get(0);
					resultBean.setResultInteger(pax.getId());
					resultBean.setResultObject(paxModalList.get(0));
				}else{
					resultBean.setResultBoolean(false);
				}
				resultBean.setIserror(false);
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
		}
		return resultBean;
	}
	public ResultBean paxValidationForHotel(PaxModel paxModal)
	{
		ResultBean resultBean = new ResultBean();
		boolean validate = false;
		try
		{
			if (paxModal != null)
			{
				List<PaxModel> paxModalList = paxManager.hotelPaxValidation(paxModal);
				PaxModel updatePaxModel = new PaxModel();
				if(paxModalList!=null && !paxModalList.isEmpty()){
					PaxModel pax = paxModalList.get(0);
					if(null != paxModal.getPaxType() && !"1".equals(paxModal.getPaxType())){
						updatePaxModel.setId(pax.getId());
						if((null != paxModal.getTitle() && null != pax.getTitle()) && !paxModal.getTitle().equals(pax.getTitle()))
						{
							updatePaxModel.setTitle(paxModal.getTitle());
							validate = true;
						}
						if((null != paxModal.getLastName() && null != pax.getLastName()) && !paxModal.getLastName().equalsIgnoreCase(pax.getLastName()))
						{
							updatePaxModel.setLastName(paxModal.getLastName());
							validate = true;
						}
						if((null != paxModal.getPhone() && null != pax.getPhone()) && !paxModal.getPhone().equals(pax.getPhone()))
						{
							updatePaxModel.setPhone(paxModal.getPhone());
							validate = true;
						}
						if((null != paxModal.getDob() && null != pax.getDob()) && paxModal.getDob().compareTo(pax.getDob())!=0)
						{
							updatePaxModel.setDob(paxModal.getDob());
							validate = true;
						}
						if((null != paxModal.getNationality() && null != pax.getNationality()) && !paxModal.getNationality().equalsIgnoreCase(pax.getNationality()))
						{
							updatePaxModel.setNationality(paxModal.getNationality());
							validate = true;
						}
						if((null != paxModal.getAddress() && null != pax.getAddress()) && !paxModal.getAddress().equalsIgnoreCase(pax.getAddress()))
						{
							updatePaxModel.setAddress(paxModal.getAddress());
							validate = true;
						}
					}/*else if(null != paxModal.getPaxType() && "1".equals(paxModal.getPaxType())){
						pax.setPhone(paxModal.getPhone());
						pax.setEmail(paxModal.getEmail());
						updatePaxModel = pax;
						validate = true;
					}*/
					
					if(validate)
						paxManager.updatePaxDetails(updatePaxModel);
					
					resultBean.setResultBoolean(true);
					resultBean.setResultInteger(pax.getId());
				}else{
					resultBean.setResultBoolean(false);
				}
				resultBean.setIserror(false);
			}
		}
		catch (Exception e)
		{
			resultBean.setIserror(true);
			TTLog.error(0, e);
			TTLog.printStackTrace(0, e);
		}
		return resultBean;
	}
	//Add by amit for groupId
	public ResultBean groupId(int id) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		try{
			List<Object> userIdList= paxManager.groupId(id);
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
	public ResultBean checkDuplicateTraveller(String branchId, String firstName, String email)
	{
		ResultBean resultBean = new ResultBean();
		try {
			boolean status = paxManager.checkDuplicate(branchId, firstName,email );
			
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
	
	public ResultBean searchCorporateListByName(PaxModel paxModel) {
		ResultBean resultBean = new ResultBean();
		try {
		    List<PaxModel> list = paxManager.searchCorporateListByName(paxModel);
		    resultBean.setResultList(list);
		    resultBean.setIserror(false);
		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTLog.error(0, e);
		}
		return resultBean;
	    }
//
	public ResultBean savePaxDocuments(PaxModel pax, Integer userId) {
		ResultBean resultBean = new ResultBean();
		try {

		    List<PaxDocumentModel> paxDocModals = pax.getPaxDocumentModel();
		    List<PaxDocumentModel> paxDocList = new ArrayList<>();
		    if (paxDocModals != null) {
			String imagePath;
			int seq = 1;
			int index = 0;
			for (PaxDocumentModel docModel : paxDocModals) {

			    if (docModel.getPath() != null && !docModel.getPath().isEmpty()) {
				docModel.setName(docModel.getPath().split("/")[5]);
				docModel.setCreationTime(new Date());
				docModel.setCreatedBy(pax.getCreatedBy());
				docModel.setLastModTime(new Date());
				docModel.setLastUpdatedBy(pax.getCreatedBy());
				docModel.setSequence(seq);
				docModel.setPaxModel(pax);
				paxDocList.add(docModel);
			    } else {
				List<MultipartFile> multiPartfileList = docModel.getDocList();
				List<String> fqnList = new ArrayList<>();
				fqnList.add("pax");
				fqnList.add(userId.toString());
				if (multiPartfileList != null && !multiPartfileList.isEmpty()) {
				    if (multiPartfileList.get(0).getSize() > 0) {
				
					for (MultipartFile multiPartFile : multiPartfileList) {
						PaxDocumentModel paxDocModel = new PaxDocumentModel();
					    byte[] imagesBytes;
					    try {
						imagesBytes = multiPartFile.getBytes();
						imagePath = com.tt.nc.common.util.CommonUtil.uploadProductCommon("tt.uploadfile.path", imagesBytes, multiPartFile, fqnList);
						paxDocModel.setPath(imagePath);
						paxDocModel.setSequence(seq);
						paxDocModel.setPaxModel(pax);
						paxDocModel.setCreationTime(new Date());
						paxDocModel.setLastModTime(new Date());
						paxDocModel.setName(imagePath.split("/")[5]);

						if (index > 0) {
						    paxDocModel.setDocType(1);
						    paxDocModel.setDescription("PHOTO");
						} else {
						    paxDocModel.setDocType(0);
						    paxDocModel.setDescription("DOC");
						}

						paxDocModel.setCreatedBy(pax.getCreatedBy());
						paxDocModel.setLastUpdatedBy(pax.getCreatedBy());

						paxDocList.add(paxDocModel);
						
					    } catch (IOException e) {
						TTPortalLog.printStackTrace(104, e);
					    }
					}
				    }
				}
				
			    }
			    seq++;
			    index++;
			}
		    }
		    if (!paxDocList.isEmpty()) {
			for (PaxDocumentModel agntDocModel : paxDocList) {
			    paxManager.savePaxDocumentData(agntDocModel);
			}
			resultBean.setIserror(false);
		    }
		} catch (Exception e) {
		    resultBean.setIserror(true);
		    TTPortalLog.printStackTrace(104, e);
		}
		return resultBean;
	    }

//today
	 public ResultBean updatePaxDocuments(PaxModel pax, Integer userId) {
			ResultBean resultBean = new ResultBean();
			try {

			    paxManager.deletePaxDocuments(userId);
			    List<PaxDocumentModel> paxDocModals = pax.getPaxDocumentModel();
			    List<PaxDocumentModel> paxDocList = new ArrayList<>();
			    if (paxDocModals != null && !paxDocModals.isEmpty()) {
				
				String imagePath;
				int seq = 1;
				int index = 0;
				for (PaxDocumentModel docModel : paxDocModals) {

				    if (docModel.getPath() != null && !docModel.getPath().isEmpty()) {
					docModel.setName(docModel.getPath().split("/")[5]);
					docModel.setCreationTime(new Date());
					docModel.setLastModTime(new Date());
					docModel.setLastUpdatedBy(pax.getCreatedBy());
					docModel.setSequence(seq);
					docModel.setPaxModel(pax);
					paxDocList.add(docModel);
					
				    } else {
					List<MultipartFile> multiPartfileList = docModel.getDocList();
					List<String> fqnList = new ArrayList<>();
					fqnList.add("pax");
					fqnList.add(userId.toString());
					if (multiPartfileList != null && !multiPartfileList.isEmpty()) {
					    if (multiPartfileList.get(0).getSize() > 0) {
						
						for (MultipartFile multiPartFile : multiPartfileList) {
							PaxDocumentModel paxDocModel = new PaxDocumentModel();
						    byte[] imagesBytes;
						    try {
							imagesBytes = multiPartFile.getBytes();
							imagePath = com.tt.nc.common.util.CommonUtil.uploadProductCommon("tt.uploadfile.path", imagesBytes, multiPartFile, fqnList);
							paxDocModel.setPath(imagePath);
							paxDocModel.setSequence(seq);
							paxDocModel.setPaxModel(pax);
							paxDocModel.setCreationTime(new Date());
							paxDocModel.setLastModTime(new Date());
							paxDocModel.setName(imagePath.split("/")[5]);

							if (index > 0) {
							    paxDocModel.setDocType(1);
							    paxDocModel.setDescription("PHOTO");
							} else {
							    paxDocModel.setDocType(0);
							    paxDocModel.setDescription("DOC");
							}

							paxDocModel.setCreatedBy(pax.getCreatedBy());
							paxDocModel.setLastUpdatedBy(pax.getCreatedBy());

							paxDocList.add(paxDocModel);
							
						    } catch (IOException e) {
							TTPortalLog.printStackTrace(104, e);
						    }
						}

					    }
					}
				    }
				    seq++;
				    index++;
				}
			    }
			    if (!paxDocList.isEmpty()) {
				for (PaxDocumentModel agntDocModel : paxDocList) {
				    paxManager.savePaxDocumentData(agntDocModel);
				}
				resultBean.setIserror(false);
			    }
			} catch (Exception e) {
			    resultBean.setIserror(true);
			    TTPortalLog.printStackTrace(104, e);
			}
			return resultBean;
		    }
	//

}