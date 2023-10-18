package com.sb.rp01;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAcnoTrDtlInqr {

	
	/*계좌거래내역 조회*/
	public List<AcnoTrDtlInqrSub1OutVO> S001(Map<String,Object> map);	
	
}
