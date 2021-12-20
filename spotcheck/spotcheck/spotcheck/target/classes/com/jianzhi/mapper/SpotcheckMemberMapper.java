package com.jianzhi.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jianzhi.model.*;
import org.apache.ibatis.annotations.Param;

public interface SpotcheckMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpotcheckMember record);

    int insertSelective(SpotcheckMember record);

    SpotcheckMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpotcheckMember record);

    int updateByPrimaryKey(SpotcheckMember record);


    int updateJg(@Param("id") Integer id, @Param("jg") Integer jg);

    /**
     * 查询该次抽查的人员数据
     *
     * @param spotcheckMember
     * @return
     */
    List<SpotcheckMember> selectSpotcheckMemberByDetail(SpotcheckMember spotcheckMember);

    /**
     * 更新抽查状态
     *
     * @param id
     * @param spid
     * @return
     */
    int updateSpot(@Param("id") String id, @Param("spid") String spid);

    /**
     * 查询抽取状态
     *
     * @param id
     * @param spid
     * @return
     */
    Integer selectPrize(@Param("id") String id, @Param("spid") String spid);


    int insertSpotcheckMemberList(@Param("spotcheckMemberList") List<SpotcheckMember> spotcheckMemberList);

    /**
     * 根据科目名查询没有资质的单位
     *
     * @param spid
     * @param kmm
     * @return
     */
    List<String> getNonedwm(@Param("spid") Integer spid, @Param("kmm") String kmm);

    /**
     * 根据zz_code查询有资质的单位
     *
     * @param spid
     * @param zzCode
     * @return
     */
    List<String> getHavedwmByCode(@Param("spid") Integer spid, @Param("zzCode") String zzCode);

    int getSpotcheckMemberNumBydw(@Param("dwm") String dwm, @Param("spid") Integer spid);

    SpotcheckMember getSpotcheckMemberByInfo(SpotcheckMember spotcheckMember);

    /**
     * 从spid=?的临时表中导入数据
     *
     * @param spid
     * @return
     */
    int insertFromMemberTemp(@Param("spid") Integer spid);

    /**
     * 更新有资质单位的状态
     *
     * @param havedwnList
     * @return
     */
    int updateByDwmSetZz(@Param("havedwnList") List<String> havedwnList);

    /**
     * 批量修改实操结果
     *
     * @param idlist 批量id
     * @param jg     设置结果
     * @return
     */
    int updateJgStatus(@Param("idlist") List<Integer> idlist, @Param("jg") Integer jg);

    /**
     * 查询附加与第一次无资质单位与随机抽查后未抽到人的有资质单位
     *
     * @param spid
     * @return
     */
    List<String> getHavedwmBySpid(@Param("spid") Integer spid);

    /**
     * 查询未抽的单位及未抽人数
     *
     * @param spid
     * @return
     */
    List<HashMap<String, Long>> getNotCheck(@Param("spid") Integer spid, @Param("dwm") String dwm);

    /**
     * 查询是否存在抽查计划里面的人员信息
     */
    int selecctPxbmId(Pxbm pxbm);

    /**
     * 根据科目名查询所有单位
     *
     * @param spid
     * @param kmm
     * @return
     */
    List<String> getVeryDw(@Param("spid") Integer spid, @Param("kmm") String kmm);

    /**
     * 根据人员ID更新图片地址
     *
     * @param spotcheckMember
     * @return int
     * SpotheckMember
     */
    int uploadImgSpotheckMember(SpotcheckMember spotcheckMember);

    /**
    *根据人员pxbmId和Spid来删除图片地址
    */
    int deleteImgurl(SpotcheckMember spotcheckMember);

    /**
     * 查询图片地址
     */
     Photo[] selectImgurl(Photo Photo);

    /**
     * 添加图片到kh_photo表
     */
     int insertKhphoto(Photo photo);

    /**
     * 删除图片
     */
    int deleteimg(String urlName);

    /**
     * 查询spotcheckMemberMapper的kh_photot值
     */
    String selectKh_photo(SpotcheckMember spotcheckMember);

    /**
     * 修改计划的的一键抽取
     */
    int updateCheckstatus(Integer spid);

    /**
     * 查询Checkstatus
     *
     */
     int selectCheckstatus(Integer spid);

    /**
     * 根据spid查询该计划下的所有人员信息
     */
    List<SpotcheckMember> getPxbmIdr_jgCheckstatus(Integer spid);

    /**
     * 更新P_bj公示字段
     * 根据传过来的计划ID和pxbmID
     * */
    int updateP_bj(@Param("idlist") List<Integer> idlist, @Param("p_bj") Integer p_bj,@Param("num")Integer num);

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
     * 将参与监督计划中的人员添加到正式表中去
     * @name addspotMember
     * @return int
     * @createName sxl
     * @createDate 2021/7/23
     */
    int addspotMember(SpotcheckMember spotcheckMember);

    /**
     * 监督计划人员加载查询
     * @name selectCheckstatusMember
     * @return List<SpotcheckMember></>
     * @createName sxl
     * @createDate 2021/7/23
     */
    // List<SpotcheckMember>selectCheckstatusMember(SpotcheckMember spotcheckMember);

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
     *更新spotmember正式表的xhjd字段
     * @name updateSpmXhjd(SpotcheckMember spotcheckMember)
     * @return int
     * @createName sxl
     * @createDate 2021/7/27
     */
    int updateSpmXhjd(SpotcheckMember spotcheckMember);

    /**
     * 按监督计划ID查询正式表里面的参与抽验的人员
     * @name getSelectXhJdSpotmember
     * @return list<Spotmember></>
     * @createName sxl
     * @createDate 2021/7/28
     */
    List<SpotcheckMember>getSelectXhJdSpotmember(Integer spid);

    /**
     *查询监督计划中已经进行抽取的人员
     * @name getSelectAleaderCheckStusSpm(SpotcheckMember)
     * @return int
     * @createName sxl
     * @createDate 2021/7/29
     */
    int getSelectAleaderCheckStusSpm(SpotcheckMember spotcheckMember);

    int deleteXhSpotmember(Integer id);

}