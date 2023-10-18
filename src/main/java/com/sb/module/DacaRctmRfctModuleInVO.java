package com.sb.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DacaRctmRfctModuleInVO {

	@ApiModelProperty(required = true, value= "거래일자")
	private	String tr_dt;
	
	@ApiModelProperty(required = true, value= "계좌번호")
	private	String acno;
	@ApiModelProperty(required = true, value= "입금금액")
	private	long rctm_amt;
	
	
}
