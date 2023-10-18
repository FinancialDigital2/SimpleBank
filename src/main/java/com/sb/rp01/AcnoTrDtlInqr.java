package com.sb.rp01;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class AcnoTrDtlInqr {
	
	@Autowired
	IAcnoTrDtlInqr	iAcnoTrDtlInqr;
	
	public AcnoTrDtlInqrOutVO acnoTrDtlInqr(AcnoTrDtlInqrInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		AcnoTrDtlInqrOutVO out = new AcnoTrDtlInqrOutVO();
		List<AcnoTrDtlInqrSub1OutVO> outDataList = null;
		
		try
		{	
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/********************************************************************
			 *  거래내역 조회
			 ********************************************************************/
			
			Map<String, Object>  s001MapIn = new HashMap<String, Object>();
			
			
			s001MapIn.put("acno", 			inData.getAcno());
			s001MapIn.put("inqr_strt_dt", 	inData.getInqr_strt_dt());
			s001MapIn.put("inqr_end_dt", 	inData.getInqr_end_dt());
			
			outDataList = iAcnoTrDtlInqr.S001(s001MapIn);
			
			log.debug("[outDataList] "+ outDataList);
			out.setAcnoTrDtl(outDataList);
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
	private void f_init_Check(AcnoTrDtlInqrInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력계좌번호를 확인하세요");
		}
	}
}
