package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DrwgPsblAmtInqrOutVO {

	@ApiModelProperty(required = true, value= "출금가능금액")
	private	long drwg_psbl_amt;
	
	@ApiModelProperty(required = true, value= "예수금")
	private	long daca;
	
	
	
}
