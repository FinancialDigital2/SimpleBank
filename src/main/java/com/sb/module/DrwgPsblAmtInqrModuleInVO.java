package com.sb.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DrwgPsblAmtInqrModuleInVO {
	
	@ApiModelProperty(required = true, value= "계좌번호")
	private	String acno;
	
}
