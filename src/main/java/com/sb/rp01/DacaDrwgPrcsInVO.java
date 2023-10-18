package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DacaDrwgPrcsInVO {

	
	@ApiModelProperty(required = true, value= "계좌번호")
	private	String acno;
	
	@ApiModelProperty(required = true, value= "총거래금액")
	private	long tot_tr_amt;
	
	
}
