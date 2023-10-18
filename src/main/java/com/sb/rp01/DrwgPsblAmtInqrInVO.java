package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DrwgPsblAmtInqrInVO {

	
	@ApiModelProperty(required = true, value= "계좌번호")
	private	String acno;
	
	
}
