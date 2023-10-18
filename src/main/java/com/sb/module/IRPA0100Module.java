package com.sb.module;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRPA0100Module {

	/*적요 조회*/
	public Map<String,Object> S001(Map<String,Object> map);
	
	
}
