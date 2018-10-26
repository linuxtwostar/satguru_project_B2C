package com.tt.ts.rest.organization.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

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
import com.tt.ts.supplier.modal.SupplierModal;
import com.tt.ts.suppliercredential.modal.SupplierCredentialModal;

@Repository
public interface OrganizationDao {

	public List<ProductModel> getProductById(Integer groupId,String productName) throws Exception;

	public List<SupplierModal> getSupplierById(Integer groupId,
			Integer productId) throws Exception;

	public List<SupplierCredentialModal> getCredentialById(Integer groupId,
			Integer productId, Integer supplierId) throws Exception;

	public List<Object[]> getSupplierCredentialById(Integer groupId,
			Integer productId, Integer supplierId,String credentialId) throws Exception;
	
	public List<Object[]> getProductSupplierCredentialById(Integer groupId,
			Integer productId, Integer supplierId) throws Exception;

	public List<Object> getCurrencyCode(String credentialId) throws Exception;
	
	public List<Object> getProductSupplierCredentialId(String agencyCode,
			Integer productId, Integer supplierId,String orgType) throws Exception;
	
	List<CurrencyBean> fetchCurrencyById(Integer quotationId) throws Exception;

	List<SupplierModal> getSupplierCredentialById(Integer groupId)
			throws Exception;

	 List<Object> getCompanyDetailsById(Integer userId, Integer organizationType)
			throws Exception;
	 
	 List<Object> getCompanyDetailsByAgentId(Integer userId, Integer organizationType)
				throws Exception;

	List<Object> fetchOrganizationDetails(Integer groupId,String agencyCode) throws Exception;

	public List<OrganizationFinancialModel> fetchAgencyCreditLimit(Integer groupId)
			throws Exception;

	public List<Object[]> searchAirportData(AirportModal airportModel)
			throws Exception;
	
	public List<AirportModal> searchAirport(AirportModal airportModel)
			throws Exception;

	
	public List<AirlineModel> getAirlinesByAgencyId(Integer groupId, Integer domOrIntl)
			throws Exception;
	
	public List<OrganizationDocumentModel> getProfileImage(Integer groupId) throws Exception;
	
	public List<Object> getMarkupRecordByUser(Integer organizationId, int corpOrRetail,Integer corpId) throws Exception;

	public List<Object> getRestrictedAirlineCredential(Integer credentialId) throws Exception;

	public List<Object> gdsSearchSupplierCredential(Integer groupId,String orgType) throws Exception;

	public List<Object> gdsTicketingSupplierCredential(Integer groupId,String orgType) throws Exception;
	
	public List<Object> fetchCredential(Integer crendentialId) throws Exception;
	
	public List<Object> fetchDealDetails(Integer groupId, Integer credentialId) throws Exception;
	
	public List<SupplierModal> getGdsSupplierList(String agencyId) throws Exception;
	 
	public List<OrganizationAirlineCodeModal> getRestrictedAirlineListByAgengy(String agencyId) throws Exception;
	
	public List<Object> getAgentType(Integer userId) throws Exception;

	public List<FlightTagModel> searchFlightTag(FlightTagModel flightTagModel) throws Exception;

	public List<BlackOutFlightModel> searchBlackOutFlight(BlackOutFlightModel blackOutFlightModel) throws Exception;
	
	public List<Object> getRBDListCountryWise(Integer countryId) throws Exception;

	public List<BspCommissionModel> getBspCommisisonList(BspCommissionModel bspCommissionModel) throws Exception;

	public List<KnowledgeCenterModel> getKnowledgeCenters(KnowledgeCenterModel kcModel) throws Exception;

	public List<CountryBean> fetchCountryList(CountryBean countryBean) throws Exception;
	
	public List<Object> getAllAirportsCountryCity(AirportModal airportModel) throws Exception;
	
	public List<EnumModel> getEnumListByValue(String name)throws Exception;
	
	public List<AirportModal> getAirportDetailsByCode(String name)throws Exception;
	
	public List<Object> getFactObjectIdAgencyBranch(List<Integer> ids) throws Exception;

	public List<PaymentMode> fetchPaymentOption(String agencyCode)throws Exception;

	public List<Object> fetchProductSupplierMappingWithAgency(Integer groupId) throws Exception;

	public List<AirlineModel> prefferdAirline(AirlineModel airlineModel) throws Exception;
	
	public List<CountryBean> fetchCountryById(String id) throws Exception;
	
	public List<GDSDealCodeModel> getDealCodeModelList(GDSDealCodeModel gdsDealCodeModel) throws Exception;

	public List<Object> fetchNationalityName(String nationalityCode) throws Exception;
	 
}
