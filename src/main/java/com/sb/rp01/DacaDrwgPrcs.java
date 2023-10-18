package com.sb.rp01;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sb.module.DacaDrwgPrcsModule;
import com.sb.module.DacaDrwgPrcsModuleInVO;
import com.sb.module.DacaDrwgPrcsModuleOutVO;
import com.sb.module.IRPB1000Module;
import com.sb.module.TrnoGenModule;
import com.sb.module.TrnoGenModuleInVO;
import com.sb.module.TrnoGenModuleOutVO;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class DacaDrwgPrcs {
	
	
	@Autowired
	IRPB1000Module  iRPB1000Module;
	
	@Autowired
	TrnoGenModule	trnoGenModule;
	
	@Autowired
	DacaDrwgPrcsModule dacaDrwgPrcsModule;
	
	public DacaDrwgPrcsOutVO dacaDrwgPrcs(DacaDrwgPrcsInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		DacaDrwgPrcsOutVO out = new DacaDrwgPrcsOutVO();
		/*일자설정*/
		String today = "";
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");
		
		try
		{	
			Date d = gc.getTime();
			today = sdformat.format(d);
			
			/********************************************************************
			 *  입력값 체크
			 ********************************************************************/
			f_init_Check(inData);
			
			/********************************************************************
			 *  계좌잔고정보조회
			 ********************************************************************/
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
			
			/********************************************************************
			 *  거래일련번호 채번
			 ********************************************************************/
			TrnoGenModuleInVO 	trnoGenInVO 	=  	new TrnoGenModuleInVO();
			TrnoGenModuleOutVO 	trnoGenOutVO 	= 	null;
			
			trnoGenInVO.setAcno(inData.getAcno());
			trnoGenInVO.setTr_dt(today);
			
			trnoGenOutVO = trnoGenModule.trnoGenModule(trnoGenInVO);
			
			/********************************************************************
			 *  출금처리
			 ********************************************************************/
			
			DacaDrwgPrcsModuleInVO drpmInVO =	new DacaDrwgPrcsModuleInVO(); 
			DacaDrwgPrcsModuleOutVO drpmOutVO =	null;
			
			drpmInVO.setTr_dt(today);
			drpmInVO.setAcno(inData.getAcno());
			drpmInVO.setTrno(trnoGenOutVO.getTr_no());
			drpmInVO.setStrt_tr_no(trnoGenOutVO.getTr_no());
			drpmInVO.setTot_tr_amt(inData.getTot_tr_amt());
			drpmInVO.setSyns_cd("002");   /*적요코드 002: 출금 */
			
			drpmOutVO = dacaDrwgPrcsModule.dacaDrwgPrcsModule(drpmInVO);
			
			out.setAf_daca(drpmOutVO.getAf_daca());
			out.setBf_daca(drpmOutVO.getBf_daca());
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
	private void f_init_Check(DacaDrwgPrcsInVO inData) throws Exception {
		
		if(inData.getAcno() == null || inData.getAcno().isEmpty()) {
			throw new Exception("입력값 계좌번호를 확인하세요");
		}
		
		if(inData.getTot_tr_amt() <= 0) {
			throw new Exception("입력값 총거래금액를 확인하세요");
		}
		
	}
}
