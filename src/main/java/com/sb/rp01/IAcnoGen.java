package com.sb.rp01;


import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAcnoGen {

	
	/*계좌번호 조회*/
	public Map<String,Object> S001(Map<String,Object> map);
	
	/*계좌잔고 조회*/
	public Map<String,Object> S002(Map<String,Object> map);
	
	/*계좌번호 채번*/
	public int I001(Map<String,Object> map);
	
	/*채번 등록*/
	public int I002(Map<String,Object> map);
	
	/*계좌잔고 등록*/
	public int I003(Map<String,Object> map);
}
