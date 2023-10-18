package com.sb.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class TrnoGenModule {
	
	@Autowired
	ITrnoGenModule	iTrnoGenModule;
	
	public TrnoGenModuleOutVO trnoGenModule(TrnoGenModuleInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		TrnoGenModuleOutVO out = new TrnoGenModuleOutVO();
		
		int    rsCnt = 0;  /*결과 건수*/
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		String today = "";
		long l_trno = 0;
		
		try
		{	
			Date d = gc.getTime();
			today = sdformat.format(d);
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/*일자체크*/
			if( !today.equals(inData.getTr_dt())) {
				log.error("입력일자가 오늘이 아닙니다. " + inData.getTr_dt());
				throw new Exception("입력일자가 오늘이 아닙니다.");
			}
			/********************************************************************
			 *  채번테이블 조회*
			 ********************************************************************/
			Map<String, Object>  s001MapOut = null;
			Map<String, Object>  s001MapIn = new HashMap<String, Object>();
			
			s001MapIn.put("ACNO", inData.getAcno());
			
			s001MapOut = iTrnoGenModule.S001(s001MapIn);
			
			log.debug("s001MapOut:" + s001MapOut);
			
			/*채번 값 설정*/
			if(today.compareTo(String.valueOf(s001MapOut.get("LAST_TR_DT"))) < 0) {
				log.error("최종거래일자("+s001MapOut.get("LAST_TR_DT") +") 당일 ("+ today+")보다 큽니다.");
				throw new Exception("최종거래일자가 당일보다 큽니다.");
			}
			
			if(today.compareTo(String.valueOf(s001MapOut.get("LAST_TR_DT"))) > 0) {
				l_trno = 1;
			}else{
				
				l_trno = Long.parseLong(String.valueOf(s001MapOut.get("LAST_TR_NO"))) + 1;
			}
			
			/********************************************************************
			 *채번테이블 갱신
			 ********************************************************************/
			Map<String, Object>  u001MapIn = new HashMap<String, Object>();
			u001MapIn.put("ACNO"		, inData.getAcno());
			u001MapIn.put("LAST_TR_NO"	, l_trno);
			u001MapIn.put("LAST_TR_DT"	, today);
			
			rsCnt = iTrnoGenModule.U001(u001MapIn);
			
			if(rsCnt <=  0) {
				throw new Exception( iTrnoGenModule.getClass().getName()+"."+"U001"+" 처리 건수 없음.");
			}
			
			out.setTr_no(l_trno);
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
	private void f_init_Check(TrnoGenModuleInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
		
		if(inData.getTr_dt() == null || inData.getTr_dt().isEmpty()) {
			throw new Exception("입력값 거래일자를 확인하세요");
		}
	}
}
