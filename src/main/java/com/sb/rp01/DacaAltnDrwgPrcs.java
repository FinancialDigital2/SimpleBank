package com.sb.rp01;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sb.module.DacaAltnDrwgPrcsModule;
import com.sb.module.DacaAltnDrwgPrcsModuleInVO;
import com.sb.module.DacaAltnDrwgPrcsModuleOutVO;
import com.sb.module.IRPB1000Module;
import com.sb.module.TrnoGenModule;
import com.sb.module.TrnoGenModuleInVO;
import com.sb.module.TrnoGenModuleOutVO;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional(rollbackFor = {Throwable.class})
public class DacaAltnDrwgPrcs {
	
	
	@Autowired
	private IRPB1000Module  iRPB1000Module;
	
	@Autowired
	private TrnoGenModule	trnoGenModule;
	
	@Autowired
	private DacaAltnDrwgPrcsModule dacaAltnDrwgPrcsModule;
	
	
	public DacaAltnDrwgPrcsOutVO dacaAltnDrwgPrcs(DacaAltnDrwgPrcsInVO inData) throws Exception{
		log.debug("[Start] "+ getClass().getName());
		/*************************************************************
		 * Declare Var
		 *************************************************************/
		DacaAltnDrwgPrcsOutVO out = new DacaAltnDrwgPrcsOutVO();
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
			 *  출금계좌잔고정보조회
			 ********************************************************************/
			Map<String, Object>  d_rpb1000_s001MapOut = null;
			Map<String, Object>  d_rpb1000_s001MapIn = new HashMap<String, Object>();
			
			 d_rpb1000_s001MapIn.put("ACNO", inData.getDrwg_acno());
			
			 d_rpb1000_s001MapOut = iRPB1000Module.S001(d_rpb1000_s001MapIn);
			
			log.debug("d_rpb1000_s001MapOut:" + d_rpb1000_s001MapOut );
			
			if( d_rpb1000_s001MapOut == null)
			{
				log.error("출금계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("출금계좌잔고 정보가 존재하지 않습니다.");
			}
			
			
			/********************************************************************
			 *  출금 거래일련번호 채번
			 ********************************************************************/
			TrnoGenModuleInVO 	d_trnoGenInVO 	=  	new TrnoGenModuleInVO();
			TrnoGenModuleOutVO 	d_trnoGenOutVO 	= 	null;
			
			d_trnoGenInVO.setAcno(inData.getDrwg_acno());
			d_trnoGenInVO.setTr_dt(today);
			
			d_trnoGenOutVO = trnoGenModule.trnoGenModule(d_trnoGenInVO);
			
			
			/********************************************************************
			 *  입금계좌잔고정보조회
			 ********************************************************************/
			Map<String, Object>  r_rpb1000_s001MapOut = null;
			Map<String, Object>  r_rpb1000_s001MapIn = new HashMap<String, Object>();
			
			 r_rpb1000_s001MapIn.put("ACNO", inData.getRctm_acno());
			
			 r_rpb1000_s001MapOut = iRPB1000Module.S001(r_rpb1000_s001MapIn);
			
			log.debug("r_rpb1000_s001MapOut:" + r_rpb1000_s001MapOut );
			
			if( r_rpb1000_s001MapOut == null)
			{
				log.error("입금계좌잔고 정보가 존재하지 않습니다.");
				throw new Exception("입금계좌잔고 정보가 존재하지 않습니다.");
			}
			
			/********************************************************************
			 *  입금 거래일련번호 채번
			 ********************************************************************/
			TrnoGenModuleInVO 	r_trnoGenInVO 	=  	new TrnoGenModuleInVO();
			TrnoGenModuleOutVO 	r_trnoGenOutVO 	= 	null;
			
			r_trnoGenInVO.setAcno(inData.getRctm_acno());
			r_trnoGenInVO.setTr_dt(today);
			
			r_trnoGenOutVO = trnoGenModule.trnoGenModule(r_trnoGenInVO);
			
			
			/********************************************************************
			 *  대체 출금처리
			 ********************************************************************/
			
			DacaAltnDrwgPrcsModuleInVO dadpmInVO =	new DacaAltnDrwgPrcsModuleInVO(); 
			DacaAltnDrwgPrcsModuleOutVO dadpmOutVO =	null;
			
			dadpmInVO.setTr_dt(today);
			dadpmInVO.setDrwg_acno(inData.getDrwg_acno());
			dadpmInVO.setAltn_amt(inData.getTot_tr_amt());
			dadpmInVO.setDrwg_trno(d_trnoGenOutVO.getTr_no());
			dadpmInVO.setDrwg_strt_trno(d_trnoGenOutVO.getTr_no());
			dadpmInVO.setDrwg_syns_cd("004");   /*적요코드 004: 대체출금 */
			
			dadpmInVO.setRctm_acno(inData.getRctm_acno());			
			dadpmInVO.setRctm_trno(r_trnoGenOutVO.getTr_no());
			dadpmInVO.setRctm_strt_trno(r_trnoGenOutVO.getTr_no());
			dadpmInVO.setRctm_syns_cd("003");   /*적요코드 003: 대체입금 */
			
			
			dadpmOutVO = dacaAltnDrwgPrcsModule.dacaAltnDrwgPrcsModule(dadpmInVO);
			
			out.setRctm_bf_daca(dadpmOutVO.getRctm_bf_daca());
			out.setRctm_af_daca(dadpmOutVO.getRctm_af_daca());
			out.setDrwg_bf_daca(dadpmOutVO.getDrwg_bf_daca());
			out.setDrwg_af_daca(dadpmOutVO.getDrwg_af_daca());
			
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
	private void f_init_Check(DacaAltnDrwgPrcsInVO inData) throws Exception {
		
		if(inData.getDrwg_acno() == null || inData.getDrwg_acno().isEmpty()) {
			throw new Exception("입력값 출금계좌번호를 확인하세요");
		}
		
		if(inData.getRctm_acno() == null || inData.getRctm_acno().isEmpty()) {
			throw new Exception("입력값 입금계좌번호를 확인하세요");
		}
		
		if(inData.getTot_tr_amt() <= 0) {
			throw new Exception("입력값 총거래금액를 확인하세요");
		}
		
	}
}
