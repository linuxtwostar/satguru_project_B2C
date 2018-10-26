package com.tt.ts.rest.ruleengine.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ws.services.util.CallLog;

@Service
public class KieSessionService {

	@Autowired
	public static KieSession kieSessFlight;

	@Autowired
	public static KieSession kieSessInsurance;

	@Autowired
	public static KieSession kieSessHotel;
	
	@Autowired
	public static KieSession kieSessCar;

	@Autowired
	public static KieSession kieSessImportPnr;
	
	@Autowired
	public static KieSession kieSessActivity;

	private static KieServices ks = KieServices.Factory.get();

	public static Date kieSessionLastUpdated;
	
	public static Map<String,Date> kieSessionLastUpdatedFlightSearch = new HashMap<>();

	public static Date kieSessionInsuranceLastUpdated;
	
	public static Date kieHotelSessionLastUpdated;

	public static Date kieSessionImportPnrLastUpdated;
	
	public static Date kieCarSessionLastUpdated;
	
	public static Date kieActivitySessionLastUpdated;

	private KieSessionService() {

	}
	
	
	public static void getKieSessionCar() {
		try{
			if (kieSessCar == null) {
				CallLog.info(105, "=====create a New Car KieSession=====");
				KieRepository kr = ks.getRepository();
				KieContainer kieContainer = ks.newKieContainer(kr
						.getDefaultReleaseId());
				kieSessCar = kieContainer.newKieSession();
				kieSessCar.setGlobal("out", System.out);
				//kieCarSessionLastUpdated = new Date();
			}
		}  catch (Exception e) {
			CallLog.printStackTrace(105, e);
		}
		
	}
	
	

	public static void getKieSessionFlight() {
		try {
			if (kieSessFlight == null) {
				CallLog.info(105, "=====create a New Flight KieSession=====");
				KieRepository kr = ks.getRepository();
				KieContainer kieContainer = ks.newKieContainer(kr
						.getDefaultReleaseId());
				kieSessFlight = kieContainer.newKieSession();
				kieSessFlight.setGlobal("out", System.out);
				kieSessionLastUpdated = new Date();
			}
		} catch (Exception e) {
			e.printStackTrace();
			CallLog.printStackTrace(105, e);
		}
		
	}

	public static boolean writeRuleTextCar(String ruleText) {

		try {
			KieFileSystem kfs = ks.newKieFileSystem();

			kfs.write("src/main/resources/car.drl", ruleText);
			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to
							// KieRepository
							// if successfully built.
			if (kb.getResults().hasMessages(Level.ERROR)) {
				CallLog.error(
						105,
						"Inside invokeRule of [SimulationRuleService ::Hotel]:::"
								+ kb.getResults());

			}
		} catch (Exception e) {

			CallLog.printStackTrace(105, e);
			return false;
		}

		return true;
	}
	
	public static void getKieSessionHotel() {
		try {
			if (kieSessHotel == null) {
				CallLog.info(105, "=====create a New Hotel KieSession=====");
				KieRepository kr = ks.getRepository();
				KieContainer kieContainer = ks.newKieContainer(kr
						.getDefaultReleaseId());
				kieSessHotel = kieContainer.newKieSession();
				kieSessHotel.setGlobal("out", System.out);
				//kieHotelSessionLastUpdated = new Date();
			}
		}catch (Exception e) {
			CallLog.printStackTrace(105, e);
		}
		
	}

	public static void getKieSessionInsurance() {
		try {
			if (kieSessInsurance == null) {
				CallLog.info(105, "=====create a New Insurance KieSession=====");
				KieRepository kr = ks.getRepository();
				KieContainer kieContainer = ks.newKieContainer(kr
						.getDefaultReleaseId());
				kieSessInsurance = kieContainer.newKieSession();
				kieSessInsurance.setGlobal("out", System.out);
				//kieSessionInsuranceLastUpdated = new Date();
			}
		} catch (Exception e) {
			CallLog.printStackTrace(105, e);
		}
		
	}

	public static void getKieSessionImportPnr() {
		try {
			if (kieSessImportPnr == null) {
				CallLog.info(105, "=====create a Import PNR KieSession=====");
				KieRepository kr = ks.getRepository();
				KieContainer kieContainer = ks.newKieContainer(kr
						.getDefaultReleaseId());
				kieSessImportPnr = kieContainer.newKieSession();
				kieSessImportPnr.setGlobal("out", System.out);
				kieSessionImportPnrLastUpdated = new Date();
			}
		}catch (Exception e) {
			CallLog.printStackTrace(105, e);
		}	
	}
	
	public static void getKieSessionActivity() {
		try{
			if (kieSessActivity == null) {
				CallLog.info(105, "=====create a New Activity KieSession=====");
				KieRepository kr = ks.getRepository();
				KieContainer kieContainer = ks.newKieContainer(kr
						.getDefaultReleaseId());
				kieSessActivity = kieContainer.newKieSession();
				kieSessActivity.setGlobal("out", System.out);
				kieActivitySessionLastUpdated = new Date();
			}
		}catch (Exception e) {
			CallLog.printStackTrace(105, e);
		}	
	}

	public static boolean writeRuleTextFlight(String droolRuleText) {

		try {
			KieFileSystem kfs = ks.newKieFileSystem();

			kfs.write("src/main/resources/flight.drl", droolRuleText);
			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to
							// KieRepository
							// if successfully built.
			if (kb.getResults().hasMessages(Level.ERROR)) {
				CallLog.error(
						105,
						"Inside invokeRule of [SimulationRuleService]:::"
								+ kb.getResults());

			}
		} catch (Exception e) {

			CallLog.printStackTrace(105, e);
			return false;
		}

		return true;
	}

	public static boolean writeRuleTextHotel(String droolRuleText) {

		try {
			KieFileSystem kfs = ks.newKieFileSystem();

			kfs.write("src/main/resources/hotel.drl", droolRuleText);
			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to
							// KieRepository
							// if successfully built.
			if (kb.getResults().hasMessages(Level.ERROR)) {
				CallLog.error(
						105,
						"Inside invokeRule of [SimulationRuleService ::Hotel]:::"
								+ kb.getResults());

			}
		} catch (Exception e) {

			CallLog.printStackTrace(105, e);
			return false;
		}

		return true;
	}

	public static boolean writeRuleTextInsurance(String droolRuleText) {

		try {
			KieFileSystem kfs = ks.newKieFileSystem();

			kfs.write("src/main/resources/Insurance.drl", droolRuleText);
			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to
							// KieRepository
							// if successfully built.
			if (kb.getResults().hasMessages(Level.ERROR)) {
				CallLog.error(
						105,
						"Inside invokeRule of [SimulationRuleService : Insurance]:::"
								+ kb.getResults());

			}
		} catch (Exception e) {

			CallLog.printStackTrace(105, e);
			return false;
		}

		return true;
	}

	public static boolean writeRuleTextImportPnr(String droolRuleText) {

		try {
			KieFileSystem kfs = ks.newKieFileSystem();

			kfs.write("src/main/resources/importPnr.drl", droolRuleText);
			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to
							// KieRepository
							// if successfully built.
			if (kb.getResults().hasMessages(Level.ERROR)) {
				CallLog.error(
						105,
						"Inside invokeRule of [SimulationRuleService]:::"
								+ kb.getResults());

			}
		} catch (Exception e) {

			CallLog.printStackTrace(105, e);
			return false;
		}

		return true;
	}
	
	public static boolean writeRuleTextActivity(String droolRuleText) {

		try {
			KieFileSystem kfs = ks.newKieFileSystem();

			kfs.write("src/main/resources/activity.drl", droolRuleText);
			KieBuilder kb = ks.newKieBuilder(kfs);

			kb.buildAll(); // kieModule is automatically deployed to
							// KieRepository
							// if successfully built.
			if (kb.getResults().hasMessages(Level.ERROR)) {
				CallLog.error(
						105,
						"Inside invokeRule of [SimulationRuleService ::Activity]:::"
								+ kb.getResults());

			}
		} catch (Exception e) {

			CallLog.printStackTrace(105, e);
			return false;
		}

		return true;
	}
}
