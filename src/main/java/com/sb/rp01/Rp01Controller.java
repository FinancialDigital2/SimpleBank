package com.sb.rp01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Api(tags = "출납01" )
@Log4j2
@RestController
@RequestMapping("/rp01")
public class Rp01Controller {

	@Autowired
	AcnoGen				acnoGen;
	
	@Autowired
	DacaRctmPrcs		dacaRctmPrcs;
	
	@Autowired
	DacaDrwgPrcs		dacaDrwgPrcs;
	
	@Autowired
	DacaAltnDrwgPrcs	dacaAltnDrwgPrcs;
	
	@Autowired
	AcnoTrDtlInqr		acnoTrDtlInqr;
	
	@Autowired
	DrwgPsblAmtInqr		drwgPsblAmtInqr;
	
	@ApiOperation("계좌 생성")
	@PostMapping("/genAcno")
	public AcnoGenOutVO genAcno(@RequestBody AcnoGenInVO inData) throws Exception {
		log.debug("genAcno");
		return acnoGen.acnoGen(inData);
	}
	
	
	@ApiOperation("입금처리")
	@PostMapping("/prcsRctm")
	public DacaRctmPrcsOutVO prcsRctm(@RequestBody DacaRctmPrcsInVO inData) throws Exception {
		log.debug("prcsRctm");
		return dacaRctmPrcs.dacaRctmPrcs(inData);
	}
	
	@ApiOperation("출금처리")
	@PostMapping("/prcsDrwg")
	public DacaDrwgPrcsOutVO prcsDrwg(@RequestBody DacaDrwgPrcsInVO inData) throws Exception {
		log.debug("prcsRctm");
		return dacaDrwgPrcs.dacaDrwgPrcs(inData);
	}
	
	@ApiOperation("대체출금처리")
	@PostMapping("/prcsAltnDrwg")
	public DacaAltnDrwgPrcsOutVO prcsAltnDrwg(@RequestBody DacaAltnDrwgPrcsInVO inData) throws Exception {
		log.debug("prcsRctm");
		return dacaAltnDrwgPrcs.dacaAltnDrwgPrcs(inData);
	}
	
	
	@ApiOperation("계좌거래내역조회")
	@PostMapping("/inqrAcnoTrDtl")
	public AcnoTrDtlInqrOutVO prcsAltnDrwg(@RequestBody AcnoTrDtlInqrInVO inData) throws Exception {
		log.debug("prcsRctm");
		return acnoTrDtlInqr.acnoTrDtlInqr(inData);
	}
	
	@ApiOperation("출금가능금액조회")
	@PostMapping("/inqrDrwgPsblAmt")
	public DrwgPsblAmtInqrOutVO inqrDrwgPsblAmt(@RequestBody DrwgPsblAmtInqrInVO inData) throws Exception {
		log.debug("prcsRctm");
		return drwgPsblAmtInqr.drwgPsblAmtInqr(inData);
	}
}
