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
public class DrwgPsblAmtInqrModule {
	
	@Autowired
	private IRPB1000Module	iRPB1000Module;
	
	
	public DrwgPsblAmtInqrModuleOutVO drwgPsblAmtInqrModule(DrwgPsblAmtInqrModuleInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		
		DrwgPsblAmtInqrModuleOutVO out = new DrwgPsblAmtInqrModuleOutVO();
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
			
			out.setDrwg_psbl_amt((Long)rpb1000_s001MapOut.get("DACA"));
			out.setDaca((Long)rpb1000_s001MapOut.get("DACA"));
			
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
	private void f_init_Check(DrwgPsblAmtInqrModuleInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
	}
}
