package com.sb.rp01;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcnoTrDtlInqrOutVO {

	@ApiModelProperty(required = true, value= "계좌거래내역")
	private	List<AcnoTrDtlInqrSub1OutVO> acnoTrDtl;
	
}
