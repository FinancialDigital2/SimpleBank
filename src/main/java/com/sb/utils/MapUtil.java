package com.sb.utils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @fileName    : MapUtil.java
 * @date        : 2021. 2. 24.
 * @author      : 
 * @description : MAP 객체에서 key의 value를 Type에 맞게 추출하는 유틸리티
 * ================================================
 * DATE        AUTHOR    NOTE
 * 
 */
public class MapUtil {

	public static String getString(Map<String,Object> map, Object key) {
		String rtn = (String) map.get(key);
		return rtn;
	}
	public static Integer getInteger(Map<String,Object> map, Object key) {
		Number rtn = (Number) map.get(key);
		if(rtn == null) return null;
		return rtn.intValue();
	}
	public static Long getLong(Map<String,Object> map, Object key) {
		Number rtn = (Number) map.get(key);
		if(rtn == null) return null;
		return rtn.longValue();
	}
	public static Double getDouble(Map<String,Object> map, Object key) {
		Number rtn = (Number) map.get(key);
		if(rtn == null) return null;
		return rtn.doubleValue();
	}
	public static BigDecimal getBigDecimal(Map<String,Object> map, Object key) {
		Object rtn = map.get(key);
		if(rtn instanceof BigDecimal) {
			return (BigDecimal) rtn;
		}else {
			return new BigDecimal(rtn.toString());
		}
	}
}
