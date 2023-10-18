package com.sb.rp01;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class AcnoGen {
	
	@Autowired
	IAcnoGen	iAcnoGen;
	
	public AcnoGenOutVO acnoGen(AcnoGenInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		AcnoGenOutVO out = new AcnoGenOutVO();
		String sAcno = null;
		int    rsCnt = 0;  /*결과 건수*/
		
		try
		{	
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/********************************************************************
			 *  계좌번호 채번
			 ********************************************************************/			
			Map<String, Object>  i001MapIn = new HashMap<String, Object>();
			
			i001MapIn.put("CUST_ID", inData.getCust_id());			
			
			rsCnt = iAcnoGen.I001(i001MapIn);
			
			if(rsCnt <=  0) {
				throw new Exception( iAcnoGen.getClass().getName()+"."+"I001"+" 처리 건수 없음.");
			}
			
			/********************************************************************
			 *  계좌번호 조회
			 ********************************************************************/
			
			Map<String, Object>  s001MapIn = new HashMap<String, Object>();
			Map<String, Object>  s001MapOut = null;
			
			s001MapIn.put("CUST_ID", inData.getCust_id());
			
			s001MapOut = iAcnoGen.S001(s001MapIn);
			
			log.debug("s001MapOut:" + s001MapOut.get("ACNO"));
			
			sAcno = String.valueOf(s001MapOut.get("ACNO"));
			
			
			/********************************************************************
			 * 채번 및 잔고 테이블 생성
			 ********************************************************************/
			/*채번테이블  생성*/
			Map<String, Object>  i002MapIn = new HashMap<String, Object>();
			
			i002MapIn.put("ACNO", sAcno);
			i002MapIn.put("LAST_TR_NO", 0);
			i002MapIn.put("LAST_TR_DT", "00000000");
			
			
			rsCnt = iAcnoGen.I002(i002MapIn);
			
			if(rsCnt <=  0) {
				throw new Exception( iAcnoGen.getClass().getName()+"."+"I002"+" 처리 건수 없음.");
			}
			
			/*예수금잔고  생성*/			
			Map<String, Object>  i003MapIn = new HashMap<String, Object>();
			
			i003MapIn.put("ACNO", sAcno);
			i003MapIn.put("CUST_ID", inData.getCust_id());
			
			
			rsCnt = iAcnoGen.I003(i003MapIn);
			
			if(rsCnt <=  0) {
				throw new Exception( iAcnoGen.getClass().getName()+"."+"I002"+" 처리 건수 없음.");
			}
			
			/********************************************************************
			 *  예수금잔고조회
			 ********************************************************************/
			Map<String, Object>  s002MapOut = null;
			Map<String, Object>  s002MapIn = new HashMap<String, Object>();
			s002MapIn.put("ACNO", sAcno);
			
			s002MapOut = iAcnoGen.S002(s002MapIn);
			
			if( s002MapOut == null)
			{
				log.error("계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("계좌잔고 정보가 존재하지 않습니다.");
			}
			/********************************************************************
			 * 결과값 생성
			 ********************************************************************/
			out.setAcno(sAcno);;
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
	private void f_init_Check(AcnoGenInVO inData) throws Exception {
		
		if(inData.getCust_id() == null || inData.getCust_id().isEmpty()) {
			throw new Exception("입력값을 확인하세요");
		}
	}
}
