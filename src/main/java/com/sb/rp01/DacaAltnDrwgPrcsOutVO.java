package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DacaAltnDrwgPrcsOutVO {

	@ApiModelProperty(required = true, value= "출금이전예수금")
	private	long drwg_bf_daca;
	@ApiModelProperty(required = true, value= "출금이후예수금")
	private	long drwg_af_daca;
	
	@ApiModelProperty(required = true, value= "입금이전예수금")
	private	long rctm_bf_daca;
	@ApiModelProperty(required = true, value= "입금이후예수금")
	private	long rctm_af_daca;
}
