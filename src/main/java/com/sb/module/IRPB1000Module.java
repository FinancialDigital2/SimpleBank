package com.sb.module;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRPB1000Module {

	/*계좌예수금기본 조회 */
	public Map<String,Object> S001(Map<String,Object> map);
	
	
}
