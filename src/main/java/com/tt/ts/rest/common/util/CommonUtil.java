package com.tt.ts.rest.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.tt.nc.common.util.TTLog;
import com.tt.ts.airline.model.AirlineModel;
import com.tt.ts.airport.model.AirportModal;

public class CommonUtil<T> {
    private static final String ISO_FORMAT = "ISO-8859-1";
    private Class<T> type;

    public CommonUtil() {
	super();
    }

    public CommonUtil(Class<T> cls) {
	super();
	setType(cls);
    }

    public static String convertDateToUSFormat(String date) {
	String finalDate = null;
	try {
	    if (date != null) {
		String[] d = date.split("-");
		finalDate = d[2] + "-" + d[1] + "-" + d[0];
	    }
	} catch (Exception e) {
	    TTLog.info(0, "[convertDateToUSFormat] Exception  :" + e.getMessage());
	    TTLog.printStackTrace(0, e);
	}
	return finalDate;
    }

    public static int calAgeFromDob(Date birthDate) {
	int years;
	int months;
	// create calendar object for birth day
	Calendar birthDay = Calendar.getInstance();
	birthDay.setTimeInMillis(birthDate.getTime());
	// create calendar object for current day
	long currentTime = System.currentTimeMillis();
	Calendar now = Calendar.getInstance();
	now.setTimeInMillis(currentTime);
	// Get difference between years
	years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
	int currMonth = now.get(Calendar.MONTH) + 1;
	int birthMonth = birthDay.get(Calendar.MONTH) + 1;
	// Get difference between months
	months = currMonth - birthMonth;
	// if month difference is in negative then reduce years by one and
	// calculate the number of months.
	if (months < 0) {
	    years--;
	    months = 12 - birthMonth + currMonth;
	    if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		months--;
	} else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
	    years--;
	    months = 11;
	}
	// Calculate the days
	if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
	    now.add(Calendar.MONTH, -1);
	} else {
	    if (months == 12) {
		years++;
		months = 0;
	    }
	}
	return years;
    }

    public static String convertIntoJson(Object obj) {
	ObjectMapper mapper = new ObjectMapper();
	String jsonString = "";
	try {
	    jsonString = mapper.writeValueAsString(obj);
	} catch (Exception e) {
	    TTLog.info(0, "[convertIntoJson] Exception  :" + e.getMessage());
	    TTLog.printStackTrace(0, e);
	}
	return jsonString;
    }

    public List<T> convertListJsonIntoObject(String jsonString) {
	ObjectMapper mapper = new ObjectMapper();
	List<T> jsonToObjectList = null;
	try {
	    TypeReference<List<T>> mapType = new TypeReference<List<T>>() {
	    };
	    jsonToObjectList = mapper.readValue(jsonString, mapType);
	} catch (Exception e) {
	    TTLog.info(0, "[convertListJsonIntoObject] Exception  :" + e.getMessage());
	    TTLog.printStackTrace(0, e);
	}
	return jsonToObjectList;
    }

    @SuppressWarnings("unchecked")
    public T convertJSONIntoObject(String json, T entity) {
	T entity2 = null;
	try {
	    ObjectMapper mapper = new ObjectMapper();
	    entity2 = mapper.readValue(json, (Class<T>) entity);
	} catch (Exception e) {
	    TTLog.printStackTrace(0, e);
	}
	return entity2;
    }

    public Class<T> getType() {
	return type;
    }

    public void setType(Class<T> type) {
	this.type = type;
    }

    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        TTLog.info(0, "String Length in compress:: "+str.length());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString(ISO_FORMAT);
        TTLog.info(0, "Output String Length in compress:: "+outStr.length());
        return outStr;
     }
    
    public static String decompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        TTLog.info(0, "Input String Length in decompress:: "+str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes(ISO_FORMAT)));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, ISO_FORMAT));
        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line=bf.readLine())!=null) {
          outStr.append(line);
        }
        TTLog.info(0, "Output String Length in decompress:: "+outStr.length());
        return outStr.toString();
     }
    public static Date convertStringToDateIndianFormat(String dateInString, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			TTLog.printStackTrace(0, e);
		}
		return date;
	}

	public static Date getDateWithTimeStamp(Date date,Boolean isForSearch,Boolean toDate)
	{
		LocalDateTime now = LocalDateTime.now();
		if(!isForSearch){
		date.setHours(now.getHour());
		date.setMinutes(now.getMinute());
		date.setSeconds(now.getSecond());
		}else if(toDate){
			date.setHours(23);
			date.setMinutes(59);
			date.setSeconds(59);
			
		}
		return date;

	}
	public static int fetchAirlineIdByCode(List<AirlineModel> airlinemodal, String airlineCode) throws Exception
	{
		int airlineId = 0;
		for (AirlineModel airlien : airlinemodal)
		{
			if (airlien.getAirlineCode().equals(airlineCode.trim()))
			{
				airlineId = airlien.getContentId();
				break;
			}
		}
		return airlineId;
	}
	
	public static String getcurrentDateIndian()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}
	   public static String convertDateToString(Date dateInString) {
		   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return df.format(dateInString);
		}
	   
	   public static String fetchAirportCodeById(List<AirportModal> airportModal, int airportId) throws Exception
		{
			String airportCode = null;
			for (AirportModal airport : airportModal)
			{
				if (airport.getAirportId()==airportId)
				{
					airportCode = airport.getAirportCode();
					break;
				}
			}

			return airportCode;

		}
	   
	   public static String fetchMonthString(Integer month) {
		   return new DateFormatSymbols().getMonths()[month-1];
	   }
	   
	public static int daysBetweenTwoDate(Date one, Date two) {
		long difference = (two.getTime() - one.getTime()) / 86400000;
		return (int)difference;
	}
	public static String prepareKeyForAutoSuggest(String searchInput) 
	 {
		 String flightSearchKey=null;
		  try 
		  {	
			    StringBuilder searchKeyBuilder = new StringBuilder("");
			    searchKeyBuilder.append(searchInput.replaceAll("\\s", ""));
			    flightSearchKey=searchKeyBuilder.toString();
			} catch (Exception e) {
				TTLog.printStackTrace(0, e);
			}
			return flightSearchKey;
	  }
	 public static Date convertStringToDate(String dateInString, String format) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date parsedDate = null;
			try {
				SimpleDateFormat dateformat = new SimpleDateFormat("dd/mm/yyyy");
				Date date = dateformat.parse(dateInString);
				dateformat = new SimpleDateFormat("yyyy-MM-dd");
				String newdate = dateformat.format(date);
				parsedDate = formatter.parse(newdate);
			} catch (ParseException e) {
				TTLog.printStackTrace(0, e);
			}
			return parsedDate;
		}
	  
	 public static  String convertDatetoString(Date date, String stringDateFormat) {
			DateFormat df = new SimpleDateFormat(stringDateFormat);
			String convertedDate = null;
			try {
				convertedDate = df.format(date);
			} catch (Exception e) {
				TTLog.printStackTrace(0, e);
			}
			return convertedDate;
		}
	   public static byte[] compressHotelsObject(Object obj) throws IOException {

	   	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   	GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
	   	ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut);
	   	objectOut.writeObject(obj);
	   	objectOut.close();
	       return baos.toByteArray();
	    }
	   public static Object decompressHotelsObject(byte[] obj) throws IOException {
		   	ByteArrayInputStream bais = new ByteArrayInputStream(obj);
		   	GZIPInputStream gzipIn = new GZIPInputStream(bais);
		   	ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
		   	Object resultObj = null;
		try {
		resultObj = objectIn.readObject();
		} catch (ClassNotFoundException e) {
		TTLog.printStackTrace(0, e);
		}
		   	objectIn.close();
		       return resultObj;
		    }
}
