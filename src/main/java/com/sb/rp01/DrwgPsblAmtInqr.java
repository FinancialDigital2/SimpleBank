package com.sb.rp01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sb.module.DrwgPsblAmtInqrModule;
import com.sb.module.DrwgPsblAmtInqrModuleInVO;
import com.sb.module.DrwgPsblAmtInqrModuleOutVO;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class DrwgPsblAmtInqr {
	
	@Autowired
	DrwgPsblAmtInqrModule	drwgPsblAmtInqrModule;
	
	public DrwgPsblAmtInqrOutVO drwgPsblAmtInqr(DrwgPsblAmtInqrInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		DrwgPsblAmtInqrOutVO out = new DrwgPsblAmtInqrOutVO();
		
		
		try
		{	
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/********************************************************************
			 *  출금가능금액 조회
			 ********************************************************************/
			DrwgPsblAmtInqrModuleInVO drwgPsblAmtInqrModuleInVO = new DrwgPsblAmtInqrModuleInVO();
			DrwgPsblAmtInqrModuleOutVO drwgPsblAmtInqrModuleOutVO= null;
			
			drwgPsblAmtInqrModuleInVO.setAcno(inData.getAcno());
			
			drwgPsblAmtInqrModuleOutVO= drwgPsblAmtInqrModule.drwgPsblAmtInqrModule(drwgPsblAmtInqrModuleInVO);
			
			
			/********************************************************************
			 *  출력값 조립
			 ********************************************************************/
			out.setDaca(drwgPsblAmtInqrModuleOutVO.getDaca());
			out.setDrwg_psbl_amt(drwgPsblAmtInqrModuleOutVO.getDrwg_psbl_amt());
			
			log.debug("[End] "+ getClass().getName());
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
	private void f_init_Check(DrwgPsblAmtInqrInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력 계좌번호를 확인하세요");
		}
	}
}
