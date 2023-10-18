package com.sb.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrDetlRfctModuleInVO {

	@ApiModelProperty(required = true, value= "거래일자")
	private	String tr_dt;
	
	@ApiModelProperty(required = true, value= "계좌번호")
	private	String acno;
	
	@ApiModelProperty(required = true, value= "거래번호")
	private	long trno;
	
	@ApiModelProperty(required = true, value= "적요코드")
	private	String syns_cd;
	@ApiModelProperty(required = true, value= "거래금액")
	private	long tr_amt;
	@ApiModelProperty(required = true, value= "이전예수금")
	private	long bf_daca;
	@ApiModelProperty(required = true, value= "이후예수금")
	private	long af_daca;
	@ApiModelProperty(required = true, value= "시작거래번호")
	private	long strt_tr_no;
	@ApiModelProperty(required = true, value= "원거래번호")
	private	long orgn_tr_no;
	
	@ApiModelProperty(required = true, value= "취소여부")
	private	String cncl_yn;
	
	@ApiModelProperty(required = true, value= "의뢰인명")
	private	String clnt_nm;
	
}
