package com.sb.module;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ITrnoGenModule {

	/*채번 조회*/
	public Map<String,Object> S001(Map<String,Object> map);
	
	/*채번 등록*/
	public int U001(Map<String,Object> map);
}
