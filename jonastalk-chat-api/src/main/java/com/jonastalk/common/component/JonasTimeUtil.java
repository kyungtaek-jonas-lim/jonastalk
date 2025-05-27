package com.jonastalk.common.component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @name JonasTimeUtil.java
 * @brief Jonas Time Util
 * @author Jonas Lim
 * @date 2023.07.27
 */
public class JonasTimeUtil {
	
	
	/**
	 * @name convertISO8601StringToZonedDateTime(String date, ZoneId zoneId)
	 * @brief ISO8601 Form String to ZonedDateTime (default ZoneId UTC)
	 * @author Jonas Lim
	 * @date 2023.07.27
	 * @param date
	 * @param zoneId
	 * @return
	 */
	public static ZonedDateTime convertISO8601StringToZonedDateTime(String date, ZoneId zoneId) {
		
		if (date == null || date.isEmpty()) return null; 
		if (zoneId == null ) zoneId = ZoneId.of("UTC");
		try {
		    ZonedDateTime zonedDateTime = ZonedDateTime.from(
		        Instant.from(
		            DateTimeFormatter.ISO_DATE_TIME.parse(date)
		        ).atZone(zoneId)
		    );
	    	return zonedDateTime;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * @name getCurrentZonedDateTime(ZoneId zoneId)
	 * @brief Current ZonedDateTime (default ZoneId UTC)
	 * @author Jonas Lim
	 * @date 2023.07.27
	 * @param zoneId
	 * @return
	 */
	public static ZonedDateTime getCurrentZonedDateTime(ZoneId zoneId) {
		if (zoneId == null ) zoneId = ZoneId.of("UTC");
		try {
			ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
			return zonedDateTime;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * @name convertZonedDateTimeToISO8601StringWithoutZoneId(ZonedDateTime zonedDateTime)
	 * @brief ZonedDateTime to ISO8601 Form String (Except for ZoneId)
	 * @author Jonas Lim
	 * @date 2023.07.27
	 * @param zonedDateTime
	 * @return
	 */
	public static String convertZonedDateTimeToISO8601StringWithoutZoneId(ZonedDateTime zonedDateTime) {
		try {
		    String result = zonedDateTime.toLocalDateTime().toString();
		    return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * @name convertZonedDateTimeToISO8601String(ZonedDateTime zonedDateTime)
	 * @brief ZonedDateTime to ISO8601 Form String
	 * @author Jonas Lim
	 * @date 2023.07.27
	 * @param zonedDateTime
	 * @return
	 */
	public static String convertZonedDateTimeToISO8601String(ZonedDateTime zonedDateTime) {
		try {
		    String result = zonedDateTime.toString();
		    result = result.substring(0, result.indexOf("["));
		    return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * @name convertZonedDateTimeToString(ZonedDateTime zonedDateTime, String format)
	 * @brief ZonedDateTime to ISO8601 From String
	 * @author Jonas Lim
	 * @date 2023.07.27
	 * @param zonedDateTime
	 * @return
	 */
	public static String convertZonedDateTimeToString(ZonedDateTime zonedDateTime, String format) {
		if (!StringUtils.hasText(format)) return convertZonedDateTimeToISO8601String(zonedDateTime);
		try {
		    String result = zonedDateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern(format));
		    return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * @name getDifferenceMillis(ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2)
	 * @brief Time Difference between the two ZonedDateTime (ms)
	 * @author Jonas Lim
	 * @date 2023.07.27
	 * @param zonedDateTime1
	 * @param zonedDateTime2
	 * @return
	 */
	public static Long getDifferenceMillis(ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2) {
		if (zonedDateTime1 == null || zonedDateTime2 == null) return null;
		try {
			return Duration.between(zonedDateTime1, zonedDateTime2).toMillis();
		} catch (Exception e) {
			return null;
		}
	}
	
}
