package com.sb.module;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IDacaDrwgRfctModule {

	/*예수금 조회*/
	public Map<String,Object> S001(Map<String,Object> map);
	
	/*잔고갱신*/
	public int U001(Map<String,Object> map);
	
	
}
