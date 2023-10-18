package com.sb.module;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRPD1010Module {

	/*연계거래내역 등록*/
	public int I001(Map<String,Object> map);
	
	
}
