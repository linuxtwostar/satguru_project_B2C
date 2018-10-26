package com.tt.ts.rest.common.util;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.tt.nc.common.util.TTLog;

public class GenericHelperUtil<T> {

	private Class<T> type;

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public GenericHelperUtil() {
		super();
	}
	public GenericHelperUtil(Class<T> cls) {
		super();
		setType(cls);
	}

	public String getJsonStringByEntity(T entity) {
		String jsonStr = "";
		try {
			ObjectMapper mapper = getObjectMapper();
			jsonStr = mapper.writeValueAsString(entity);
		} catch (Exception e) {
			TTLog.printStackTrace(0, e);
		}
		return jsonStr;
	}
	
	public String getJsonStringByListEntity(List<T> entity)  {
		String jsonStr = "";
		try {
			ObjectMapper mapper = getObjectMapper();
			jsonStr = mapper.writeValueAsString(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	public String getJsonStringByMapEntity(Map<String,T> entity) throws Exception {
		String jsonStr = "";
		try {
			ObjectMapper mapper = getObjectMapper();
			jsonStr = mapper.writeValueAsString(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return jsonStr;
	}
	
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
}
