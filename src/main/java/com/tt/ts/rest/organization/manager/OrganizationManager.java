package com.tt.ts.rest.organization.manager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airport.model.AirportModal;
import com.tt.ts.blacklist.model.BlackOutFlightModel;
import com.tt.ts.bsp.model.BspCommissionModel;
import com.tt.ts.common.enums.model.EnumModel;
import com.tt.ts.currency.model.CurrencyBean;
import com.tt.ts.flighttag.model.FlightTagModel;
import com.tt.ts.gdsdealcode.model.GDSDealCodeModel;
import com.tt.ts.geolocations.model.CountryBean;
import com.tt.ts.knowledgecenter.model.KnowledgeCenterModel;
import com.tt.ts.organization.model.OrganizationAirlineCodeModal;
import com.tt.ts.organization.model.OrganizationDocumentModel;
import com.tt.ts.organization.model.OrganizationFinancialModel;
import com.tt.ts.payment.model.PaymentMode;
import com.tt.ts.product.model.ProductModel;
import com.tt.ts.rest.common.util.QueryConstantRest;
import com.tt.ts.rest.organization.dao.OrganizationDao;
import com.tt.ts.supplier.modal.SupplierModal;
import com.tt.ts.suppliercredential.modal.SupplierCredentialModal;

@Component(value = "orgManagerRest")
public class OrganizationManager {

	@Autowired
	OrganizationDao orgDaoRest;

	public List<ProductModel> getProductById(Integer groupId,String productName) throws Exception {
		return orgDaoRest.getProductById(groupId, productName);
	}

	public List<SupplierModal> getSupplierById(Integer groupId,
			Integer productId) throws Exception {
		return orgDaoRest.getSupplierById(groupId, productId);
	}

	public List<SupplierCredentialModal> getCredentialById(Integer groupId,
			Integer productId, Integer supplierId) throws Exception {
		return orgDaoRest.getCredentialById(groupId, productId, supplierId);
	}

	public List<SupplierModal> getSupplierCredentialById(Integer groupId)
			throws Exception {
		return orgDaoRest.getSupplierCredentialById(groupId);
	}

	public  List<Object> getCompanyDetailsById(Integer userId,
			Integer organizationType) throws Exception {
		return orgDaoRest.getCompanyDetailsById(userId, organizationType);
	}
	
	public  List<Object> getCompanyDetailsByAgentId(Integer userId,
			Integer organizationType) throws Exception {
		return orgDaoRest.getCompanyDetailsByAgentId(userId, organizationType);
	}

	public List<Object> fetchOrganizationDetails(Integer groupId,String agencyCode)
			throws Exception {
		return orgDaoRest.fetchOrganizationDetails(groupId,agencyCode);
	}

	public List<OrganizationFinancialModel> fetchAgencyCreditLimit(Integer groupId)
			throws Exception {
		return orgDaoRest.fetchAgencyCreditLimit(groupId);
	}
	
	public List<Object> getProductSupplierCredentialId(String agencyCode,
			Integer productId, Integer supplierId,String orgType) throws Exception {
		return orgDaoRest.getProductSupplierCredentialId(agencyCode, productId,
				supplierId,orgType);
	}
	public List<Object> getCurrencyCode(String credentialId) throws Exception {
		return orgDaoRest.getCurrencyCode(credentialId);
	}
	
	public List<Object[]> getSupplierCredentialById(Integer groupId,
			Integer productId, Integer supplierId,String credentialId) throws Exception {
		return orgDaoRest.getSupplierCredentialById(groupId, productId,
				supplierId,credentialId);
	}
	public List<Object[]> getProductSupplierCredentialById(Integer groupId,
			Integer productId, Integer supplierId) throws Exception {
		return orgDaoRest.getProductSupplierCredentialById(groupId, productId,
				supplierId);
	}
	
	public List<CurrencyBean> fetchCurrencyById(Integer quotationId) throws Exception {
		return orgDaoRest.fetchCurrencyById(quotationId);
	}

	public List<Object[]> searchAirportData(AirportModal airportModel)
			throws Exception {
		return orgDaoRest.searchAirportData(airportModel);
	}
	public List<AirportModal> searchAirport(AirportModal airportModel)
			throws Exception {
		return orgDaoRest.searchAirport(airportModel);
	}

	public List<AirlineModel> getAirlinesByAgencyId(Integer groupId, Integer domOrIntl)
			throws Exception {
		return orgDaoRest.getAirlinesByAgencyId(groupId,domOrIntl);
	}
	
	
	public List<OrganizationDocumentModel> getProfileImage(Integer groupId)
			throws Exception {
		return orgDaoRest.getProfileImage(groupId);
	}
	
	public List<Object> getMarkupRecordByUser(Integer organizationId, int corpOrRetail,Integer corpId)
			throws Exception {
		return orgDaoRest.getMarkupRecordByUser(organizationId, corpOrRetail, corpId);
	}

	public List<Object> getRestrictedAirlineCredential(Integer credentialId) throws Exception
	{
		return orgDaoRest.getRestrictedAirlineCredential(credentialId);
	}

	public List<Object> gdsSearchSupplierCredential(Integer groupId,String orgType) throws Exception{
		return orgDaoRest.gdsSearchSupplierCredential(groupId,orgType);
	}

	public List<Object> gdsTicketingSupplierCredential(Integer groupId,String orgType) throws Exception{
		return orgDaoRest.gdsTicketingSupplierCredential(groupId,orgType);
	}
	
	public List<Object> fetchCredential(Integer credentialId) throws Exception{
		return orgDaoRest.fetchCredential(credentialId);
	}
	
	public List<Object> fetchDealDetails(Integer groupId, Integer credentialId) throws Exception {
		return orgDaoRest.fetchDealDetails(groupId,credentialId);
	}
	
	public List<SupplierModal> getGdsSupplierList(String agencyId) throws Exception {
		return orgDaoRest.getGdsSupplierList(agencyId);
	}
	
	public List<OrganizationAirlineCodeModal> getRestrictedAirlineListByAgengy(String agencyId) throws Exception {
		return orgDaoRest.getRestrictedAirlineListByAgengy(agencyId);
	}
	
	public List<Object> getAgentType(Integer userId) throws Exception {
		return orgDaoRest.getAgentType(userId);
	}

	public List<FlightTagModel> searchFlightTag(FlightTagModel flightTagModel) throws Exception
	{
		List<FlightTagModel> flightTagModels = null;
		try {
			flightTagModels = orgDaoRest.searchFlightTag(flightTagModel);
		} catch (Exception e) {
			TTLog.printStackTrace(17, e);
		}
		return flightTagModels;
	}

	public List<BlackOutFlightModel> searchBlackOutFlight(BlackOutFlightModel blackOutFlightModel) throws Exception
	{
		return orgDaoRest.searchBlackOutFlight(blackOutFlightModel);

	}
	
	public List<Object> getRBDListCountryWise(Integer countryId) throws Exception {
		return orgDaoRest.getRBDListCountryWise(countryId);
	}

	public List<BspCommissionModel> getBspCommissionList(BspCommissionModel bspCommissionModel) throws Exception
	{
		return orgDaoRest.getBspCommisisonList(bspCommissionModel);
	}

	public List<KnowledgeCenterModel> searchKnowledgeCenters(KnowledgeCenterModel kcModel)  throws Exception
	{
		return orgDaoRest.getKnowledgeCenters(kcModel);
	}

	public List<CountryBean> fetchCountryList(CountryBean countryBean)  throws Exception{
		return orgDaoRest.fetchCountryList(countryBean);
	}
	
	public List<Object> getAllAirportsCountryCity(AirportModal airportModel) throws Exception{
		return orgDaoRest.getAllAirportsCountryCity(airportModel);
	}
	
	public List<EnumModel> getEnumListByValue(String columnName)throws Exception
	{
		return orgDaoRest.getEnumListByValue(columnName);
	}
	
	public List<AirportModal> getAirportDetailsByCode(String columnName)throws Exception
	{
		return orgDaoRest.getAirportDetailsByCode(columnName);
	}
	
	public List<Object> getFactObjectIdAgencyBranch(List<Integer> ids) throws Exception{
		return orgDaoRest.getFactObjectIdAgencyBranch(ids);
	}

	public List<PaymentMode> fetchPaymentOption(String agencyCode)  throws Exception
	{
		return orgDaoRest.fetchPaymentOption(agencyCode);
	}

	public List<Object> fetchProductSupplierMappingWithAgency(Integer groupId)  throws Exception
	{
		return orgDaoRest.fetchProductSupplierMappingWithAgency(groupId);
	}

	public List<AirlineModel> prefferdAirline(AirlineModel airlineModel) throws Exception{
		return orgDaoRest.prefferdAirline(airlineModel);
	}
	public List<CountryBean> fetchCountryById(String id) throws Exception{
		return orgDaoRest.fetchCountryById(id);
	}
	public List<GDSDealCodeModel> getDealCodeModelList(GDSDealCodeModel gdsDealCodeModel) throws Exception
	{
		return orgDaoRest.getDealCodeModelList(gdsDealCodeModel);
	}
	public List<Object> fetchNationalityName(String nationalityCode) throws Exception{
		 return orgDaoRest.fetchNationalityName(nationalityCode);
	}
}
