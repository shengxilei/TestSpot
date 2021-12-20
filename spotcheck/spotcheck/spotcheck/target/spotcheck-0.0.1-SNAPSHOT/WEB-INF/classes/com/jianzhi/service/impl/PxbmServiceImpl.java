package com.jianzhi.service.impl;

import java.security.PublicKey;
import java.util.List;

import javax.annotation.Resource;

import com.jianzhi.mapper.SpotcheckMemberMapper;
import com.jianzhi.model.SpotcheckMember;
import org.springframework.stereotype.Service;

import com.jianzhi.mapper.PxbmMapper;
import com.jianzhi.model.Pxbm;
import com.jianzhi.service.PxbmService;

@Service
public class PxbmServiceImpl implements PxbmService {

	@Resource
	PxbmMapper pxbmMapper;

	@Resource
	SpotcheckMemberMapper spotcheckMemberMapper;

	@Override
	public List<Pxbm> getPxbmList(Pxbm pxbm) {
		// TODO Auto-generated method stub
		return pxbmMapper.selectPxbmByDetail(pxbm);
	}

	@Override
	public int modifyCNSandSB(Integer cns, Integer sb, String bz2, String id) {
		return pxbmMapper.updateCNSorSBorBZ2(cns, sb, bz2, id);
	}

	@Override
	public Pxbm getPxbmById(String id) {
		// TODO Auto-generated method stub
		return pxbmMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateDistribution(List<String> idList, Integer spid) {
		return pxbmMapper.updateDistribution(idList, spid);
	}

	@Override
	public int clearSpid(Integer spid) {
		return pxbmMapper.clearSpid(spid);
	}

	@Override
	public int setIsApply(Integer isApply,String apply,String reply, String id) {
		// TODO Auto-generated method stub
		return pxbmMapper.setIsApply(isApply, apply, reply, id);
	}

	@Override
	public int updateJgStatus(List<Integer> idlist, String jg) {
		return pxbmMapper.updateJgStatus(idlist,jg);
	}

	@Override
	public Pxbm getPxbmExcelList(Pxbm pxbm){
		return  pxbmMapper.getPxblExcelList(pxbm);
	}

	@Override
	public int updatePxbmExcelList(Pxbm pxbm){
		return pxbmMapper.updatePxbmExcelList(pxbm);
	}

	@Override
	public  int updatePxbmInfo(Pxbm pxbm){return  pxbmMapper.updatePxbmInfo(pxbm);}

	@Override
	public int selecctPxbmId(Pxbm pxbm) {
		return spotcheckMemberMapper.selecctPxbmId(pxbm);

	}

	@Override
	public  int updateOpZt(Pxbm pxbm){
		return  pxbmMapper.updateOpZt(pxbm);
	}

	@Override
	public  Pxbm getSelectisCNS(Pxbm pxbm){
		return  pxbmMapper.getSelectisCNS(pxbm);
	}

	@Override
	public  int updateIsCNS(Pxbm pxbm){
		return  pxbmMapper.updateIsCNS(pxbm);
	}

	@Override
	public int updateSpokeMembers(Pxbm pxbm){
		return pxbmMapper.updateSpokeMembers(pxbm);
	}

	@Override
	public Pxbm getSelectSpokeMembers(Pxbm pxbm){
		return  pxbmMapper.getSelectSpokeMembers(pxbm);
	}



	@Override
	public int updateSXHSpokeMembers(Pxbm pxbm){
		return  pxbmMapper.updateSXHSpokeMembers(pxbm);
	}

	/*@Override
	public Pxbm getSelectSXHSpokeMembers(Pxbm pxbm) {
		pxbmMapper.getSelectSXHSpokeMembers(pxbm);
	}*/

	@Override
	public int updateIsFp(Integer spid){
		return pxbmMapper.updateIsFp(spid);
	}

	@Override
	public Pxbm selectPxbmOpzt(Pxbm pxbm){
		return  pxbmMapper.selectPxbmOpzt(pxbm);
	}

	@Override
	public  List<Pxbm> selectPxbmInformations(Integer spid){
		return  pxbmMapper.selectPxbmInformations(spid);
	}

	@Override
	public int updateP_bj(List<Integer> idlist, Integer p_bj){
		return pxbmMapper.updateP_bj(idlist,p_bj);
	}

	@Override
	public int update_jdfpspid(Pxbm pxbm){
		return  pxbmMapper.update_jdfpspid(pxbm);
	}
}
