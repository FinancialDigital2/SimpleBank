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
public class DacaRctmRfctModule {
	
	@Autowired
	IDacaRctmRfctModule	iDacaRctmRfctModule;
	
	public DacaRctmRfctModuleOutVO dacaRctmRfctModule(DacaRctmRfctModuleInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		int  rsCnt = 0;  /*결과 건수*/
		long l_bfDaca = 0;  /*이전예수금*/
		long l_afDaca = 0;  /*이전예수금*/
		DacaRctmRfctModuleOutVO out = new DacaRctmRfctModuleOutVO();
		try
		{	
			
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/***********************************************************************
		    * 계좌예수금기본조회 호출
		    ***********************************************************************/
			
			Map<String, Object>  s001MapOut = null;
			Map<String, Object>  s001MapIn = new HashMap<String, Object>();
			
			s001MapIn.put("ACNO", inData.getAcno());
			
			s001MapOut = iDacaRctmRfctModule.S001(s001MapIn);
			
			log.debug("s001MapOut:" + s001MapOut );
			
			if( s001MapOut == null)
			{
				log.error("계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("계좌잔고 정보가 존재하지 않습니다.");
			}
			
			l_bfDaca= (Long)s001MapOut.get("DACA");
			
			/*2. 계좌잔고 갱신 */
			if(l_bfDaca + inData.getRctm_amt() < 0) {
				log.error("예수금이 음수입니다.");
				throw new Exception( "예수금이 음수입니다.");
			}
			
			Map<String, Object>  u001MapIn = new HashMap<String, Object>();
			
			u001MapIn.put("DACA", l_bfDaca + inData.getRctm_amt());
			u001MapIn.put("ACNO", inData.getAcno());
			
			rsCnt = iDacaRctmRfctModule.U001(u001MapIn);
			
			if(rsCnt <=  0) {
				throw new Exception( iDacaRctmRfctModule.getClass().getName()+"."+"U001"+" 처리 건수 없음.");
			}
			
			/*3. 거래후 계좌 잔고조회 */
			Map<String, Object>  s001MapOut2 = null;
			Map<String, Object>  s001MapIn2 = new HashMap<String, Object>();
			
			s001MapIn2.put("ACNO", inData.getAcno());
			
			s001MapOut2 = iDacaRctmRfctModule.S001(s001MapIn2);
			log.debug("s002MapOut:" + s001MapOut2 );
			
			l_afDaca = (Long)s001MapOut2.get("DACA");
			
			/*4. 결과값 생성*/
			out.setAf_daca(l_afDaca);
			out.setBf_daca(l_bfDaca);
			
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
	private void f_init_Check(DacaRctmRfctModuleInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
		
		if(inData.getTr_dt() == null || inData.getTr_dt().isEmpty()) {
			throw new Exception("입력값 거래일자를 확인하세요");
		}
		
		if(inData.getRctm_amt() <= 0) {
			throw new Exception("입력값 입금금액을 확인하세요");
		}
	}
}
