package com.sb.module;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class TrDetlRfctModule {
	
	@Autowired
	IRPA0100Module	iRPA0100Module;
	
	@Autowired
	IRPB1000Module	iRPB1000Module;
	
	@Autowired
	IRPD1000Module	iRPD1000Module;
	
	public TrDetlRfctModuleOutVO trDetlRfctModule(TrDetlRfctModuleInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		int    rsCnt = 0;  /*결과 건수*/
		TrDetlRfctModuleOutVO out = new TrDetlRfctModuleOutVO();
		try
		{	
			
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/***********************************************************************
		    * 적요조회 호출
		    ***********************************************************************/
			Map<String, Object>  rpa0100_s001MapOut = null;
			Map<String, Object>  rpa0100_s001MapIn = new HashMap<String, Object>();
			
			rpa0100_s001MapIn.put("SYNS_CD", inData.getSyns_cd());
			rpa0100_s001MapOut = iRPA0100Module.S001(rpa0100_s001MapIn);
			
			log.debug("rpa0100_s001MapOut:" );
			/***********************************************************************
		    * 계좌예수금기본조회 호출
		    ***********************************************************************/
			
			Map<String, Object>  rpb1000_s001MapOut = null;
			Map<String, Object>  rpb1000_s001MapIn = new HashMap<String, Object>();
			
			rpb1000_s001MapIn.put("ACNO", inData.getAcno());
			
			rpb1000_s001MapOut = iRPB1000Module.S001(rpb1000_s001MapIn);
			
			log.debug("rpb1000_s001MapOut:" + rpb1000_s001MapOut );
			
			if( rpb1000_s001MapOut == null)
			{
				log.error("계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("계좌잔고 정보가 존재하지 않습니다.");
			}
			/***********************************************************************
		    * 거래내역 생성
		    ***********************************************************************/
			log.debug("[START]거래내역 생성  ");
			
			Map<String, Object>  rpd1000_i001MapIn = new HashMap<String, Object>();
			
			rpd1000_i001MapIn.put("TR_DT",         inData.getTr_dt());
			rpd1000_i001MapIn.put("ACNO",          inData.getAcno());
			rpd1000_i001MapIn.put("TR_NO",         inData.getTrno());
			rpd1000_i001MapIn.put("SYNS_CD",       inData.getSyns_cd());
			rpd1000_i001MapIn.put("TR_TP_DCD",     rpa0100_s001MapOut.get("TR_TP_DCD"));
			rpd1000_i001MapIn.put("TR_AMT",        inData.getTr_amt());
			rpd1000_i001MapIn.put("BF_DACA",       inData.getBf_daca());
			rpd1000_i001MapIn.put("AF_DACA",       inData.getAf_daca());
			rpd1000_i001MapIn.put("CNCL_YN",       "N");
			rpd1000_i001MapIn.put("STRT_TR_NO",    inData.getStrt_tr_no());
			rpd1000_i001MapIn.put("ORGN_TR_NO",    inData.getOrgn_tr_no());
			rpd1000_i001MapIn.put("CLNT_NM",       inData.getClnt_nm());
						
			log.debug("거래내역 생성INPUT " + rpd1000_i001MapIn);
			
			rsCnt = iRPD1000Module.I001(rpd1000_i001MapIn);
			
			if(rsCnt <=  0) {
				throw new Exception( iRPD1000Module.getClass().getName()+"."+"I001"+" 등록오류.");
			}
			
			out.setTr_dt		(inData.getTr_dt());
			out.setAcno			(inData.getAcno());
			out.setTrno			(inData.getTrno());
			out.setSyns_cd		(inData.getSyns_cd());
			out.setTr_amt		(inData.getTr_amt());
			out.setBf_daca		(inData.getBf_daca());
			out.setAf_daca		(inData.getAf_daca());			
			out.setStrt_tr_no	(inData.getStrt_tr_no());
			out.setOrgn_tr_no	(inData.getOrgn_tr_no());
			out.setClnt_nm		(inData.getClnt_nm());
			
			return out;
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}
	}
	
	/**
	 * @param inData
	 * @throws Exception
	 */
	private void f_init_Check(TrDetlRfctModuleInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
		
		if(inData.getTr_dt() == null || inData.getTr_dt().isEmpty()) {
			throw new Exception("입력값 거래일자를 확인하세요");
		}
		
		if(inData.getTrno() <= 0) {
			throw new Exception("입력값 거래번호를 확인하세요");
		}
	}
}
