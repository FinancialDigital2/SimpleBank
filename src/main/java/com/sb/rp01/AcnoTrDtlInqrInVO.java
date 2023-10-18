package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcnoTrDtlInqrInVO {

	@ApiModelProperty(required = true, value= "계좌번호")
	private	String acno;
	
	@ApiModelProperty(required = true, value= "조회시작일자")
	private	String inqr_strt_dt;
	
	@ApiModelProperty(required = true, value= "조회종료일자")
	private	String inqr_end_dt;
}
