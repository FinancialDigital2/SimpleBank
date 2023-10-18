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
public class DacaRctmPrcsModule {
	
	@Autowired
	IRPB1000Module	iRPB1000Module;
	
	@Autowired
	DacaRctmRfctModule dacaRctmRfctModule;

	@Autowired
	TrDetlRfctModule trDetlRfctModule;
	
	public DacaRctmPrcsModuleOutVO dacaRctmPrcsModule(DacaRctmPrcsModuleInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		
		DacaRctmPrcsModuleOutVO out = new DacaRctmPrcsModuleOutVO();
		try
		{	
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
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
		    * 예수금 입금반영 호출
		    ***********************************************************************/
			DacaRctmRfctModuleInVO dacaRctmRfctModuleInVO = new DacaRctmRfctModuleInVO();
			
			dacaRctmRfctModuleInVO.setTr_dt(inData.getTr_dt());
			dacaRctmRfctModuleInVO.setAcno(inData.getAcno());
			dacaRctmRfctModuleInVO.setRctm_amt(inData.getTot_tr_amt());
			
			DacaRctmRfctModuleOutVO drrmOutVo =  dacaRctmRfctModule.dacaRctmRfctModule(dacaRctmRfctModuleInVO);
			
			/***********************************************************************
		    * 거래내역 반영 호출
		    ***********************************************************************/
			log.debug("[START]거래내역 생성  ");
			
			TrDetlRfctModuleInVO  tdrmInVO = new TrDetlRfctModuleInVO();
			
			tdrmInVO.setTr_dt			(inData.getTr_dt());
			tdrmInVO.setAcno			(inData.getAcno());
			tdrmInVO.setTrno			(inData.getTrno());
			tdrmInVO.setSyns_cd			(inData.getSyns_cd());
			tdrmInVO.setTr_amt			(inData.getTot_tr_amt());
			tdrmInVO.setBf_daca			(drrmOutVo.getBf_daca());
			tdrmInVO.setAf_daca			(drrmOutVo.getAf_daca());
			tdrmInVO.setStrt_tr_no		(inData.getStrt_tr_no());
			tdrmInVO.setOrgn_tr_no		(0);
			tdrmInVO.setClnt_nm			(inData.getClnt_nm());
						
			log.debug("거래내역 반영INPUT " + tdrmInVO);
			
			trDetlRfctModule.trDetlRfctModule(tdrmInVO);
			
			out.setBf_daca(drrmOutVo.getBf_daca());
			out.setAf_daca(drrmOutVo.getAf_daca());
			
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
	private void f_init_Check(DacaRctmPrcsModuleInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
		
		if(inData.getTr_dt() == null || inData.getTr_dt().isEmpty()) {
			throw new Exception("입력값 거래일자를 확인하세요");
		}
		
		if(inData.getTrno() <= 0) {
			throw new Exception("입력값 거래번호를 확인하세요");
		}
		
		if(inData.getStrt_tr_no() <= 0) {
			throw new Exception("입력값 시작거래번호를 확인하세요");
		}
		
		
		if(inData.getTot_tr_amt() <= 0) {
			throw new Exception("입력값 총거래금액를 확인하세요");
		}
		
		if(inData.getSyns_cd() == null || inData.getSyns_cd().isEmpty()) {
			throw new Exception("입력값 적요코드를 확인하세요");
		}
	}
}
