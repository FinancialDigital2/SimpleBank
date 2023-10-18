package com.sb.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MapperUtil {

	private static ModelMapper modelMapper;
	private static ObjectMapper objectMapperToMap;
	
	static {
		objectMapperToMap = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
				.setSerializationInclusion(Include.ALWAYS).setDefaultPropertyInclusion(Include.ALWAYS)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	private MapperUtil() {
	}

	/**
	 * @Method Name : convert
	 * @description : source의 속성값을 destinationType 형태의 클래스로 복사하여 반환
	 * @date        : 2021. 3. 3.
	 * @author      : P21055
	 * @param <T>
	 * @param source
	 * @param destinationType
	 * @return
	 */
	public static <T> T convert(Object source, Class<T> destinationType) {
		return modelMapper.map(source, destinationType);
	}
	/**
	 * @Method Name : convert
	 * @description : source의 속성값을 type 형태의 클래스로 복사하여 반환
	 * @date        : 2021. 3. 3.
	 * @author      : P21055
	 * @param <T>
	 * @param source
	 * @param type
	 * @return
	 */
	public static <T> T convert(Object source, Type type) {
		return modelMapper.map(source, type);
	}
	
	/**
	 * @Method Name : convert
	 * @description : source의 속성값을 destination의 속성에 복사
	 * @date        : 2021. 3. 3.
	 * @author      : P21055
	 * @param source : 
	 * @param destination : Map 을 제외한 object
	 */
	public static void convert(Object source, Object destination) {
		if(destination instanceof Map) {
			throw new RuntimeException("destination can't be Map type");
		}else {
			modelMapper.map(source, destination);
		}
	}

	/**
	 * @Method Name : convertList
	 * @description : Collection 안의 데이터를 destinationType형태의 클래스로 속성값 복사하여 List로 반환 
	 * @date        : 2021. 3. 3.
	 * @author      : P21055
	 * @param <T>
	 * @param sourceList
	 * @param destinationType
	 * @return
	 */
	public static <T> List<T> convertList(Collection<?> sourceList, Class<T> destinationType) {
		List<T> list = new ArrayList<T>();

		for(Object obj : sourceList) {
			list.add(modelMapper.map(obj, destinationType));
		};

		return list;
	}
	
	/**
	 * @Method Name : toMap
	 * @description : source의 속성값을 Map 으로 변환합니다.
	 * @date        : 2021. 3. 3.
	 * @author      : P21055
	 * @param source
	 * @return
	 */
	public static Map<String,Object> toMap(Object source) {
		Map<String,Object> rtn = null;
		rtn = objectMapperToMap.convertValue(source,  new TypeReference<Map<String,Object>>(){} );
		return rtn;
		
	}
	/**
	 * @Method Name : toMapList
	 * @description : Collection 안의 데이터를 Map 으로 속성값 복사하여 List로 반환 
	 * @date        : 2021. 3. 3.
	 * @author      : P21055
	 * @param source
	 * @return
	 */
	public static List<Map<String,Object>> toMapList(Collection<?> source) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Object obj : source) {
			list.add(toMap(obj));
		}
		return list;
	}
	
	

}
