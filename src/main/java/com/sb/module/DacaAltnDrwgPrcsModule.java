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
public class DacaAltnDrwgPrcsModule {
	
	@Autowired
	private IRPB1000Module	iRPB1000Module;

	@Autowired
	private IRPD1010Module	iRPD1010Module;

	
	@Autowired
	private DrwgPsblAmtInqrModule drwgPsblAmtInqrModule;

	@Autowired
	private DacaDrwgRfctModule dacaDrwgRfctModule;
	
	@Autowired
	private DacaRctmRfctModule dacaRctmRfctModule;
	
	@Autowired
	private TrDetlRfctModule trDetlRfctModule;
	
	public DacaAltnDrwgPrcsModuleOutVO dacaAltnDrwgPrcsModule(DacaAltnDrwgPrcsModuleInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		int  rsCnt = 0;  /*결과 건수*/
		DacaAltnDrwgPrcsModuleOutVO out = new DacaAltnDrwgPrcsModuleOutVO();
		try
		{	
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/********************************************************************
			*	당사대체처리
			 ********************************************************************/
			/***********************************************************************
		    * 출금계좌예수금기본조회 
		    ***********************************************************************/
			
			Map<String, Object>  rpb1000_s001MapOut = null;
			Map<String, Object>  rpb1000_s001MapIn = new HashMap<String, Object>();
			
			rpb1000_s001MapIn.put("ACNO", inData.getDrwg_acno());
			
			rpb1000_s001MapOut = iRPB1000Module.S001(rpb1000_s001MapIn);
			
			log.debug("rpb1000_s001MapOut:" + rpb1000_s001MapOut );
			
			if( rpb1000_s001MapOut == null)
			{
				log.error("계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("계좌잔고 정보가 존재하지 않습니다.");
			}
			
			/***********************************************************************
		    * 출금가능금액 조회
		    ***********************************************************************/
			DrwgPsblAmtInqrModuleInVO dpaiInVO = new DrwgPsblAmtInqrModuleInVO();
			DrwgPsblAmtInqrModuleOutVO dpaiOutVO = null;
			
			dpaiInVO.setAcno(inData.getDrwg_acno());
			
			dpaiOutVO = drwgPsblAmtInqrModule.drwgPsblAmtInqrModule(dpaiInVO);
			
			
			if(inData.getAltn_amt() > dpaiOutVO.getDrwg_psbl_amt()) {
				log.error("대체금액이 출금가능금액보다 큽니다.");
				throw new Exception("대체금액이 출금가능금액보다 큽니다.");
			}
			
			/***********************************************************************
		    * 예수금 출금반영 호출
		    ***********************************************************************/
			DacaDrwgRfctModuleInVO dacaDrwgRfctModuleInVO = new DacaDrwgRfctModuleInVO();
			
			dacaDrwgRfctModuleInVO.setTr_dt(inData.getTr_dt());
			dacaDrwgRfctModuleInVO.setAcno(inData.getDrwg_acno());
			dacaDrwgRfctModuleInVO.setDrwg_amt(inData.getAltn_amt());
			
			DacaDrwgRfctModuleOutVO ddrmOutVo =  dacaDrwgRfctModule.dacaDrwgRfctModule(dacaDrwgRfctModuleInVO);
			
			/***********************************************************************
		    * 출금거래내역 반영 호출
		    ***********************************************************************/
			log.debug("[START] 출금거래내역 생성  ");
			
			TrDetlRfctModuleInVO  tdrmInVO = new TrDetlRfctModuleInVO();
			
			tdrmInVO.setTr_dt			(inData.getTr_dt());
			tdrmInVO.setAcno			(inData.getDrwg_acno());
			tdrmInVO.setTrno			(inData.getDrwg_trno());
			tdrmInVO.setSyns_cd			(inData.getDrwg_syns_cd());
			tdrmInVO.setTr_amt			(inData.getAltn_amt());
			tdrmInVO.setBf_daca			(ddrmOutVo.getBf_daca());
			tdrmInVO.setAf_daca			(ddrmOutVo.getAf_daca());
			tdrmInVO.setStrt_tr_no		(inData.getDrwg_strt_trno());
						
			log.debug("거래내역 반영INPUT " + tdrmInVO);
			
			trDetlRfctModule.trDetlRfctModule(tdrmInVO);
			
			/***********************************************************************
		    * 연계거래내역 반영 호출
		    ***********************************************************************/
			log.debug("[START] 연계거래내역 생성  ");
			Map<String, Object>  rpd1010_i001MapIn = new HashMap<String, Object>();
			
			rpd1010_i001MapIn.put("TR_DT", 		inData.getTr_dt());
			rpd1010_i001MapIn.put("ACNO", 		inData.getDrwg_acno());
			rpd1010_i001MapIn.put("TR_NO", 		inData.getDrwg_trno());
			rpd1010_i001MapIn.put("REL_TR_DT", 	inData.getTr_dt());
			rpd1010_i001MapIn.put("OPNT_ACNO", 	inData.getRctm_acno());
			rpd1010_i001MapIn.put("OPNT_TR_NO", inData.getRctm_trno());
			
			
			rsCnt =iRPD1010Module.I001(rpd1010_i001MapIn);
			if(rsCnt <=  0) {
				throw new Exception( iRPD1010Module.getClass().getName()+"."+"I001"+" 처리 건수 없음.");
			}
	
			/***********************************************************************
		    * 예수금 입금반영 호출
		    ***********************************************************************/
			DacaRctmRfctModuleInVO dacaRctmRfctModuleInVO = new DacaRctmRfctModuleInVO();
			
			dacaRctmRfctModuleInVO.setTr_dt(inData.getTr_dt());
			dacaRctmRfctModuleInVO.setAcno(inData.getRctm_acno());
			dacaRctmRfctModuleInVO.setRctm_amt(inData.getAltn_amt());
			
			DacaRctmRfctModuleOutVO drrmOutVo =  dacaRctmRfctModule.dacaRctmRfctModule(dacaRctmRfctModuleInVO);
			
			
			/***********************************************************************
		    * 입금거래내역 반영 호출
		    ***********************************************************************/
			log.debug("[START] 입금거래내역 생성  ");
			
			TrDetlRfctModuleInVO  rtdrmInVO = new TrDetlRfctModuleInVO();
			
			rtdrmInVO.setTr_dt			(inData.getTr_dt());
			rtdrmInVO.setAcno			(inData.getRctm_acno());
			rtdrmInVO.setTrno			(inData.getRctm_trno());
			rtdrmInVO.setSyns_cd		(inData.getRctm_syns_cd());
			rtdrmInVO.setTr_amt			(inData.getAltn_amt());
			rtdrmInVO.setBf_daca		(drrmOutVo.getBf_daca());
			rtdrmInVO.setAf_daca		(drrmOutVo.getAf_daca());
			rtdrmInVO.setStrt_tr_no		(inData.getRctm_strt_trno());
						
			log.debug("거래내역 반영INPUT " + rtdrmInVO);
			
			trDetlRfctModule.trDetlRfctModule(rtdrmInVO);
			
			/***********************************************************************
		    * 입금 연계거래내역 반영 호출
		    ***********************************************************************/
			log.debug("[START] 연계거래내역 생성  ");
			Map<String, Object>  rpd1010_i001MapIn2 = new HashMap<String, Object>();
			
			rpd1010_i001MapIn2.put("TR_DT", 		inData.getTr_dt());
			rpd1010_i001MapIn2.put("ACNO", 			inData.getRctm_acno());
			rpd1010_i001MapIn2.put("TR_NO", 		inData.getRctm_trno());
			rpd1010_i001MapIn2.put("REL_TR_DT", 	inData.getTr_dt());
			rpd1010_i001MapIn2.put("OPNT_ACNO", 	inData.getDrwg_acno());
			rpd1010_i001MapIn2.put("OPNT_TR_NO", 	inData.getDrwg_trno());
			
			
			rsCnt =iRPD1010Module.I001(rpd1010_i001MapIn2);
			if(rsCnt <=  0) {
				throw new Exception( iRPD1010Module.getClass().getName()+"."+"I001"+" 처리 건수 없음.");
			}
			
			
			out.setRctm_bf_daca(drrmOutVo.getBf_daca());
			out.setRctm_af_daca(drrmOutVo.getAf_daca());
			out.setDrwg_bf_daca(ddrmOutVo.getBf_daca());
			out.setDrwg_af_daca(ddrmOutVo.getAf_daca());
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
	private void f_init_Check(DacaAltnDrwgPrcsModuleInVO inData) throws Exception {
		
		if(inData.getDrwg_acno() == null || inData.getDrwg_acno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
		
		if(inData.getTr_dt() == null || inData.getTr_dt().isEmpty()) {
			throw new Exception("입력값 거래일자를 확인하세요");
		}
		
		if(inData.getDrwg_trno() <= 0) {
			throw new Exception("입력값 출금거래번호를 확인하세요");
		}
		
		if(inData.getRctm_trno() <= 0) {
			throw new Exception("입력값 입금거래번호를 확인하세요");
		}
		
		
		if(inData.getAltn_amt() <= 0) {
			throw new Exception("입력값 대체금액를 확인하세요");
		}
		
		if(inData.getDrwg_syns_cd() == null || inData.getDrwg_syns_cd().isEmpty()) {
			throw new Exception("입력값 출금적요코드를 확인하세요");
		}
		
		if(inData.getRctm_syns_cd() == null || inData.getRctm_syns_cd().isEmpty()) {
			throw new Exception("입력값 입금적요코드를 확인하세요");
		}
	}
}
