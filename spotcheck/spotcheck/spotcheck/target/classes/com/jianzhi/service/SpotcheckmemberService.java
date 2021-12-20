package com.jianzhi.service;

import java.util.HashMap;
import java.util.List;

import com.jianzhi.model.*;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import org.springframework.web.multipart.MultipartFile;

public interface SpotcheckmemberService {
	/**
	 * 抽查
	 * @param spotcheckMember
	 * @return
	 */
	List<SpotcheckMember> getSpotcheckMemberList(SpotcheckMember spotcheckMember);
	
	/**
	 * 
	 * @param id 个人主键
	 * @param spid 抽查主键
	 * @return 返回抽取结果 1抽中 2未抽中
	 */
	int spot(String id, String spid);

	/**
	 * 修改抽查状态
	 * @param spotcheckMember
	 * @param editor 
	 * @return
	 */
	int modifySpotcheckMember(SpotcheckMember spotcheckMember, String editor);
	
	/**
	 * 修改考核结果
	 * @param spotcheckMember
	 * @return
	 */
	int updateSpotcheckMemberList(List<SpotcheckMember> spotcheckMemberList);
	
	/**
	 * 根据id查询SpotcheckMember
	 * @param id
	 * @return
	 */
	SpotcheckMember getSpotcheckMemberById(Integer id);
	
	/**
	 * 根据spotcheckMember码查询SpotcheckMember
	 * @param spotcheckMember
	 * @return
	 */
	SpotcheckMember getSpotcheckMemberByInfo(SpotcheckMember spotcheckMember);
	
	/**
	 * 从excel中导入人员进正式表
	 * @param spotcheckMemberList
	 * @param spid
	 * @return
	 */
	public int addMemberList(List<SpotcheckMember> spotcheckMemberList,Integer spid);
	
	/**
	 * 查询未含有该科目资质的单位
	 * @param spid
	 * @param kmm
	 * @return
	 */
	public List<String> getNonedwm(Integer spid,String kmm);
	
	/**
	 * 根据code查询没有改资质的单位
	 * @param spid
	 * @param kmm
	 * @return
	 */
	public List<String> getHavedwmByCode(Integer spid,String zzCode);

	/**
	 * 得到该条抽查记录查询单位人员的数量
	 * @param dwm 单位名
	 * @return 返回该单位在该条记录的人员数
	 */
	int getSpotcheckMemberNumBydw(String dwm,Integer spid);

	/**
	 * 从临时表中导入数据导正式表
	 * @param spid
	 * @return
	 */
	int addMemberList(Integer spid);

	/**
	 * 完全随机抽取
	 * @param id
	 * @param spid
	 * @return
	 */
	int spotrandom(Integer id, Integer spid);
/**
 * 批量修改实操录入结果
 * @param idlist
 * @param jg
 * @return
 */
	int updateJgStatus(List<Integer> idlist, Integer jg);

	/**
	 * 计算抽中
	 * @param spid
	 * @return
	 */
	int countspot(Integer spid);
	
	/**
	 * 计算概率后所有单位都必须抽到一个
	 * @param spid
	 * @return
	 */
	int countspot2(Integer spid);


	/**
	 * 查询附加与第一次无资质单位与随机抽查后未抽到人的有资质单位
	 * @param spid
	 * @return
	 */
	List<String> getHavedwmBySpid(Integer spid);
	
	/**
	 * 查询未完成抽查的单位
	 * @param spid
	 * @return
	 */
	List<HashMap<String, Long>> getNotCheck(Integer spid,String dwm);

	/**
	 * 计算每个单位抽取必抽10%
	 * @param spid
	 * @return
	 */
	int spotVeryDwMember(Integer spid);

	/**
	 * 查询所有单位
	 */
	List<String> getVeryDw(Integer spid,String kmm);

	/**
	 * 上传图片
	 */
	int upLoadImgurl(SpotcheckMember spotcheckMember, Pxbm pxbm);


	int deleteIjmgurl(SpotcheckMember spotcheckMember, Pxbm pxbm);

	Photo[] selectImgurl(Photo photo);

	/**
	 * 将图片加入pxbm_kh_photo
	 */
	int insertKhphoto(Photo photo);

	/**
	 * 删除图片pxbm_kh_photo
	 */
	int deleteimg(String urlName);

	/**
	 * 查询spokechenMember中的kh_photot值
	 */
	String selectKh_photo(SpotcheckMember spotcheckMember);

	/**
	 * 修改计划中人员的抽取状态
	 */
	int updateCheckstatus(Integer spid);

	/**
	 * 查询计划里面人员的抽查状态
	 */
	int selectCheckstatus(Integer spid);

	/**
	 * 根据spid查询该计划里面所有人员信息
	 */
	List<SpotcheckMember>getPxbmIdr_jgCheckstatus(Integer spid);

	/**
	 * 更新p_bj公示字段
	 */
	int updateP_bj(@Param("idlist") List<Integer> idlist, @Param("p_bj") Integer p_bj,@Param("num") Integer num);

	List<String>listpxbmid(List<Integer>idlist);

	/**
	 * 查询抽验考核及格且抽中人员
	 * @name:selectISCheckStatusSpotmember
	 * @return List<SpotcheckMember></>
	 * @createName sxl
	 * @createDate 2021/7/20
	 */
	List<SpotcheckMember>selectIsCheckStatusSpotmember(Spotcheck spotcheck);

	/**
	 * 查询抽验考核及格且未抽中人员
	 * @name selectISCheckStatusSpotmember
	 * @return List<SpotcheckMember></>
	 * @createName sxl
	 * @createDate 2021/7/20
	 */
	List<SpotcheckMember>selectNotCheckStatusSpotmember(Spotcheck spotcheck);

	/**
	 * 将参与监督计划的人员添加到正式表中
	 * @name addspotMember
	 * @return int
	 * @createName sxl
	 * @createDate 2021/7/23
	 */
	int addspotMember(SpotcheckMember spotcheckMember);

	/**
	 * 监督计划指定抽中人员
	 * @name updatecheckStusCZ
	 * @return int
	 * @createName sxl
	 * @createDate 2021/7/26
	 */
	int updateCheckStusIsCZ(SpotcheckMember spotcheckMember);

	/**
	 * 监督计划指定抽中人员
	 * @name updatecheckStusWCZ
	 * @return int
	 * @createName sxl
	 * @createDate 2021/7/26
	 */
	int updateCheckStusNotWCZ(SpotcheckMember spotcheckMember);

	/**
	 * 更新spotmember正式表中的xhjd字段,标注之前监督计划参与人员
	 * @name updateSpmXhjd
	 * @return int
	 * @createName sxl
	 * @createDate 2021/7/28
	 */
	int updateSpmXhjd(SpotcheckMember spotcheckMember);

	/**
	 * 开启监督计划时间
	 * @name xhJdAlgorithmTime
	 * @return int
	 * @createName sxl
	 * @createDate 2021/7/28
	 */
	int xhJdAlgorithmTime(Integer spid);

	/**
	 * 协会监督计划算法
	 * @name xhJdAlgorithm
	 * @return int
	 * @createName sxl
	 * @createDate 2021/7/28
	 */
	int xhJdAlgorithm(Integer spid);

	/**
	 * 按监督计划ID查询正式表里面的参与抽验的人员
	 * @name getSelectXhJdSpotmember
	 * @return list<Spotmember></>
	 * @createName sxl
	 * @createDate 2021/7/28
	 */
	//List<SpotcheckMember>getSelectXhJdSpotmember(Integer spid);


	int deleteXhSpotmember(Integer id);

}

