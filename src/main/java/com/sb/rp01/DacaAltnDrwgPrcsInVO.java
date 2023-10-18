package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DacaAltnDrwgPrcsInVO {

	
	@ApiModelProperty(required = true, value= "출금계좌번호")
	private	String drwg_acno;
	@ApiModelProperty(required = true, value= "입금계좌번호")
	private	String rctm_acno;
	
	@ApiModelProperty(required = true, value= "총거래금액")
	private	long tot_tr_amt;
	
	
}
