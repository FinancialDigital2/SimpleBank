package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DacaRctmPrcsOutVO {

	@ApiModelProperty(required = true, value= "이전예수금")
	private	long bf_daca;
	@ApiModelProperty(required = true, value= "이후예수금")
	private	long af_daca;
}
