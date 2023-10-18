package com.sb.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DacaAltnDrwgPrcsModuleInVO {

	@ApiModelProperty(required = true, value= "거래일자")
	private	String tr_dt;
	
	@ApiModelProperty(required = true, value= "출금계좌번호")
	private	String drwg_acno;
	
	@ApiModelProperty(required = true, value= "출금거래번호")
	private	long drwg_trno;
	
	@ApiModelProperty(required = true, value= "입금계좌번호")
	private	String rctm_acno;
	
	@ApiModelProperty(required = true, value= "입금거래번호")
	private	long rctm_trno;
	
	@ApiModelProperty(required = true, value= "출금시작거래번호")
	private	long drwg_strt_trno;
	@ApiModelProperty(required = true, value= "입금시작거래번호")
	private	long rctm_strt_trno;
	
	@ApiModelProperty(required = true, value= "대체금액")
	private	long altn_amt;
	
	@ApiModelProperty(required = true, value= "출금적요코드")
	private	String drwg_syns_cd;
	
	@ApiModelProperty(required = true, value= "입금적요코드")
	private	String rctm_syns_cd;
	
}
