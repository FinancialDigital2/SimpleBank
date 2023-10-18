package com.sb.module;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrnoGenModuleOutVO {

	@ApiModelProperty(required = true, value= "거래번호")
	private	long tr_no;
	
}
