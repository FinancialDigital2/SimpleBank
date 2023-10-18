package com.sb.rp01;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcnoGenInVO {

	@ApiModelProperty(required = true, value= "고객id")
	private	String cust_id;
}
