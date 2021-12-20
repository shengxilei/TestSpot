package com.jianzhi.service.impl;

import java.io.File;
import java.util.*;

import javax.annotation.Resource;

import com.jianzhi.mapper.PxbmApplyMapper;
import com.jianzhi.mapper.PxbmMapper;
import com.jianzhi.model.*;
import com.jianzhi.util.ImgUploadUtils;
import io.swagger.models.auth.In;
import org.aspectj.weaver.AnnotationOnTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jianzhi.common.component.RedisBlockQueue;
import com.jianzhi.mapper.SpotcheckMemberMapper;
import com.jianzhi.service.JobService;
import com.jianzhi.service.PxbmService;
import com.jianzhi.service.SpotPrizeService;
import com.jianzhi.service.SpotcheckMemberChangeService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.service.SpotcheckmemberService;
import com.jianzhi.service.SubjectService;
import com.jianzhi.util.DateCoreUtil;
import com.jianzhi.util.RandomNums;

import cn.hutool.cron.CronUtil;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpotcheckMemberServiceImpl implements SpotcheckmemberService {


    @Resource
    SpotcheckMemberMapper spotcheckMemberMapper;

    @Resource
    SpotcheckService spotcheckServiceImpl;

    @Resource
    SpotcheckMemberChangeService spotcheckMemberChangeServiceImpl;

    @Resource
    SpotPrizeService spotPrizeServiceImpl;

    @Autowired
    SubjectService subjectServiceImpl;

    @Autowired
    JobService jobServiceImpl;

    @Resource
    PxbmService pxbmServiceImpl;

    @Resource
    private RedisBlockQueue redisBlockQueue;

    @Resource
    private PxbmMapper pxbmMapper;

    @Override
    public List<SpotcheckMember> getSpotcheckMemberList(SpotcheckMember spotcheckMember) {

        return spotcheckMemberMapper.selectSpotcheckMemberByDetail(spotcheckMember);
    }

    @Override
    public synchronized int spot(String id, String spid) {
        Integer status = spotcheckMemberMapper.selectPrize(id, spid);
        if (status != null) {
            spotcheckMemberMapper.updateSpot(id, spid);
            return status;
        }
        return 4;
    }

    @Override
    public int modifySpotcheckMember(SpotcheckMember spotcheckMember, String editor) {
        SpotcheckMember before = spotcheckMemberMapper.selectByPrimaryKey(spotcheckMember.getId());
        SpotcheckMemberChange change = new SpotcheckMemberChange();
        change.setAge(before.getAge());
        change.setBz(spotcheckMember.getBz());
        change.setBeforestatus(before.getCheckstatus());
        change.setDwm(before.getDwm());
        change.setMemid(before.getId());
        change.setJobtitle(before.getJobtitle());
        change.setKmm(before.getKmm());
        change.setSfzhm(before.getSfzhm());
        change.setSpid(before.getSpid());
        change.setXm(before.getXm());
        change.setXb(before.getXb());
        change.setEditor(editor);
        change.setAfterstatus(spotcheckMember.getCheckstatus());
        spotcheckMemberChangeServiceImpl.save(change);
        spotcheckMember.setIsFj(1);
        return spotcheckMemberMapper.updateByPrimaryKeySelective(spotcheckMember);
    }

    @Override
    public SpotcheckMember getSpotcheckMemberById(Integer id) {
        return spotcheckMemberMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int addMemberList(List<SpotcheckMember> spotcheckMemberList, Integer spid) {
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);
        int num = spotcheckMemberMapper.insertSpotcheckMemberList(spotcheckMemberList);
        SpotcheckMember spotcheckMember = new SpotcheckMember();
        spotcheckMember.setSpid(spid);
        int count;// 该单位总人数
        int spot;// 抽到第几个人
        // int pipei=0;//匹配第几个
        if (num > 0) {
            int bfb = spotcheck.getBfb();
            Random r = new Random();
            List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                    .selectSpotcheckMemberByDetail(spotcheckMember);
            List<SpotPrize> spotPrizeList = new ArrayList<>();
            SpotPrize spotPrize;
            for (SpotcheckMember sp : spotcheckmemberlist) {
                spotPrize = new SpotPrize();
                spotPrize.setSpmemid(sp.getId());
                spotPrize.setStatus(2);
                spotPrize.setSpid(spid);
                spotPrize.setDwm(sp.getDwm());
                spotPrizeList.add(spotPrize);
            }
            if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
                float xiaoshu = spotcheckmemberlist.size() * bfb;
                int chouzhong = (int) Math.ceil(xiaoshu / 100);
                String kmm = spotcheck.getKmm();
                List<String> dwmlist = getNonedwm(spid, kmm);
                for (String dwm : dwmlist) {
                    spotPrize = new SpotPrize();
                    spotPrize.setDwm(dwm);
                    spotPrize.setSpid(spid);
                    List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                    count = splist.size();
                    spot = r.nextInt(count);
                    spotPrize = new SpotPrize();
                    spotPrize = splist.get(spot);
                    spotPrize.setStatus(1);
                    spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                }
                System.out.println("chouzhong:" + chouzhong);
                System.out.println("dwmsize:" + dwmlist.size());
                if (chouzhong > dwmlist.size()) {
                    count = chouzhong - dwmlist.size();
                    spotPrize = new SpotPrize();
                    spotPrize.setSpid(spid);
                    spotPrize.setStatus(2);
                    // System.out.println(count);
                    List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                    int[] randowmnums = RandomNums.getNnms(splist.size(), count);
                    System.out.println("splist:" + splist.size());
                    System.out.println("count:" + count);
                    System.out.println("randowmnums:" + randowmnums);
                    for (int i : randowmnums) {
                        spotPrize = splist.get(i - 1);
                        spotPrize.setStatus(1);
                        System.out.println("i:" + i);
                        System.out.println("spotPrize:" + spotPrize);
                        spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                    }
                }

            }
            spotcheck.setIsInput(1);
            spotcheckServiceImpl.modifySpotcheck(spotcheck);

            return 1;
        }

        return 0;
    }

    @Override
    public List<String> getNonedwm(Integer spid, String kmm) {
        return spotcheckMemberMapper.getNonedwm(spid, kmm);
    }

    @Override
    public int getSpotcheckMemberNumBydw(String dwm, Integer spid) {
        return spotcheckMemberMapper.getSpotcheckMemberNumBydw(dwm, spid);
    }

    @Override
    public SpotcheckMember getSpotcheckMemberByInfo(SpotcheckMember spotcheckMember) {
        // TODO Auto-generated method stub
        return spotcheckMemberMapper.getSpotcheckMemberByInfo(spotcheckMember);
    }

    @Override
    @Transactional
    public int updateSpotcheckMemberList(List<SpotcheckMember> spotcheckMemberList) {
        int num = 0;
        for (SpotcheckMember spo : spotcheckMemberList) {
            SpotcheckMember spmem = getSpotcheckMemberByInfo(spo);
            if (spmem != null) {
                num = num + spotcheckMemberMapper.updateJg(spmem.getId(), spo.getJg());
            }
        }
        return num;
    }

    @Override
    @Transactional
    public int addMemberList(Integer spid) {

        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);// 查询该条记录

        String zzCode = subjectServiceImpl.getSubjectByName(spotcheck.getKmm());// 查询相应科目代码

        spotcheckMemberMapper.insertFromMemberTemp(spid);// 从临时表插入到正式表
        spotcheck.setIsInput(1);
        spotcheckServiceImpl.modifySpotcheck(spotcheck);// 修改记录状态为已导入
        List<String> havedwnList = getHavedwmByCode(spid, zzCode);// 查询正式表该记录资质的单位
        if (havedwnList != null && !havedwnList.isEmpty()) {
            spotcheckMemberMapper.updateByDwmSetZz(havedwnList);
        }
        Job job = null;
        switch (2) {// ==1时调用完全随机抽取 不等于1时调用导入完就计算人员方法
            case 1:
                job = new Job();
                job.setSpid(spid);
                job.setStartdate(spotcheck.getStart());
                jobServiceImpl.addNewJob(job);
                CronUtil.schedule(spid.toString(), DateCoreUtil.getCron(spotcheck.getStart()),
                        () -> {
                            if (countspot(spid) > 0) {
                                jobServiceImpl.updateJobUsed(spid);
                            }
                            CronUtil.remove(spid.toString());
                        });
                return 1;
            case 2:
                job = new Job();
                job.setSpid(spid);
                job.setStartdate(spotcheck.getStart());
                jobServiceImpl.addNewJob(job);
                CronUtil.schedule(spid.toString(), DateCoreUtil.getCron(spotcheck.getStart()),
                        () -> {
                            System.out.println(spotcheck.getAreaName());
					       /*  switch (spotcheck.getAreaName()){
								 case "绍兴":
								 case "宁波":
								 case "丽水":
								 case "台州":
								 case "舟山":
								 case "温州":
									 System.out.println("这里是启用第二种算法！");
									 if (spotVeryDwMember(spid)>0) {
										 jobServiceImpl.updateJobUsed(spid);
									 }
									 break;
								 default:
									 System.out.println("这里是启用第一种算法!");
									 if (countspot2(spid) > 0) {
										 jobServiceImpl.updateJobUsed(spid);
									 }
							 }

					        */
                            System.out.println("这里是启用第二种算法！");
                            if (spotVeryDwMember(spid) > 0) {
                                jobServiceImpl.updateJobUsed(spid);
                            }
							 /*
					         if(spotcheck.getAreaName().equals("绍兴")==true||spotcheck.getAreaName().equals("宁波")){
								 System.out.println("这里是启用第二种算法！");
								 if (spotVeryDwMember(spid)>0) {
									 jobServiceImpl.updateJobUsed(spid);
								 }
							 }else {
					         	System.out.println("这里是启用第一种算法!");
								 if (countspot2(spid) > 0) {
									 jobServiceImpl.updateJobUsed(spid);
								 }
							 }
							  */

                            CronUtil.remove(spid.toString());
                        });
                return 1;
            default:

                if (countspot(spid) > 0) { // 计算抽中人员
                    return 1;
                }
                break;
        }
        return 0;

    }

    /**
     * 用于计算抽查概率
     *
     * @param spid
     * @param
     * @return
     */
    @Override
    public int countspot(Integer spid) {
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);
        SpotcheckMember spotcheckMember = new SpotcheckMember();

        int count;// 该单位总人数
        int spot;// 抽到第几个人
        int bfb = spotcheck.getBfb();
        Random r = new Random();
        spotcheckMember.setSpid(spid);
        spotcheckMember.setIsFj(0);
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                .selectSpotcheckMemberByDetail(spotcheckMember);// 查询该spid下未抽的所有人
        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;
        for (SpotcheckMember sp : spotcheckmemberlist) {
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {

            String kmm = spotcheck.getKmm();
            List<String> dwmlist = getNonedwm(spid, kmm);
            System.out.println("无资质单位：" + dwmlist.size());
            for (String dwm : dwmlist) {
                spotPrize = new SpotPrize();
                spotPrize.setDwm(dwm);
                spotPrize.setSpid(spid);
                List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                count = splist.size();
                System.out.println("该无资质单位总人数：" + count);
                spot = r.nextInt(count);
                System.out.println("该无资质抽到第几个" + spot);
                spotPrize = new SpotPrize();
                spotPrize = splist.get(spot);
                System.out.println("无资质id" + spotPrize.getId());
                spotPrize.setStatus(1);
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            } // 先把没有资质单位的人员选出来

            spotPrize = new SpotPrize();
            spotPrize.setSpid(spid);
            spotPrize.setStatus(2);
            List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
            float xiaoshu;
            if (splist != null && !splist.isEmpty()) {
                xiaoshu = (spotcheckmemberlist.size() - dwmlist.size()) * bfb;
                int chouzhong = (int) Math.ceil(xiaoshu / 100);
                int[] randowmnums = RandomNums.getNnms(splist.size(), chouzhong);
                System.out.println("splist:" + splist.size());
                System.out.println("count:" + chouzhong);
                //System.out.println("randowmnums:" + randowmnums);
                for (int i : randowmnums) {
                    spotPrize = splist.get(i - 1);
                    spotPrize.setStatus(1);
                    System.out.println("i:" + i);
                    //System.out.println("spotPrize:" + spotPrize);
                    spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                }
            }
            return 1;
        }
        return 0;

    }


    @Override
    public List<String> getHavedwmByCode(Integer spid, String zzCode) {
        return spotcheckMemberMapper.getHavedwmByCode(spid, zzCode);
    }

    /**
     * 吧member表中的数据导入到prize表中并设置全部为未抽中
     *
     * @param spid
     * @return
     */
    public int addSpotPrize(Integer spid) {
        SpotcheckMember spotcheckMember = new SpotcheckMember();
        spotcheckMember.setSpid(spid);
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                .selectSpotcheckMemberByDetail(spotcheckMember);
        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;
        for (SpotcheckMember sp : spotcheckmemberlist) {
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int spotrandom(Integer id, Integer spid) {
        String map = id + "," + spid;
        redisBlockQueue.lPush("sport", map);

        return 1;
    }

    @Override
    @Transactional
    public int updateJgStatus(List<Integer> idlist, Integer jg) {
        if (pxbmServiceImpl.updateJgStatus(idlist, jg.toString()) > 0 && spotcheckMemberMapper.updateJgStatus(idlist, jg) > 0) {
            return 1;//都成功返回0修改成功
        }
        return 0;
    }

    // 原导入直接计算方法备份
    void countspotTigger(Integer spid, int num) {
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);
        SpotcheckMember spotcheckMember = new SpotcheckMember();
        spotcheckMember.setSpid(spid);
        int count;// 该单位总人数
        int spot;// 抽到第几个人
        // int pipei=0;//匹配第几个
        if (num > 0) {
            int bfb = spotcheck.getBfb();
            Random r = new Random();
            List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                    .selectSpotcheckMemberByDetail(spotcheckMember);
            List<SpotPrize> spotPrizeList = new ArrayList<>();
            SpotPrize spotPrize;
            for (SpotcheckMember sp : spotcheckmemberlist) {
                spotPrize = new SpotPrize();
                spotPrize.setSpmemid(sp.getId());
                spotPrize.setStatus(2);
                spotPrize.setSpid(spid);
                spotPrize.setDwm(sp.getDwm());
                spotPrizeList.add(spotPrize);
            }
            if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
                float xiaoshu = spotcheckmemberlist.size() * bfb;
                int chouzhong = (int) Math.ceil(xiaoshu / 100);
                String kmm = spotcheck.getKmm();
                List<String> dwmlist = getNonedwm(spid, kmm);
                for (String dwm : dwmlist) {
                    spotPrize = new SpotPrize();
                    spotPrize.setDwm(dwm);
                    spotPrize.setSpid(spid);
                    List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                    count = splist.size();
                    spot = r.nextInt(count);
                    spotPrize = new SpotPrize();
                    spotPrize = splist.get(spot);
                    spotPrize.setStatus(1);
                    spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                }
                System.out.println("chouzhong:" + chouzhong);
                System.out.println("dwmsize:" + dwmlist.size());
                if (chouzhong > dwmlist.size()) {
                    count = chouzhong - dwmlist.size();
                    spotPrize = new SpotPrize();
                    spotPrize.setSpid(spid);
                    spotPrize.setStatus(2);
                    // System.out.println(count);
                    List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                    int[] randowmnums = RandomNums.getNnms(splist.size(), count);
                    System.out.println("splist:" + splist.size());
                    System.out.println("count:" + count);
                    System.out.println("randowmnums:" + randowmnums);
                    for (int i : randowmnums) {
                        spotPrize = splist.get(i - 1);
                        spotPrize.setStatus(1);
                        System.out.println("i:" + i);
                        System.out.println("spotPrize:" + spotPrize);
                        spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                    }
                }

            }
            // spotcheck.setIsInput(1);
            // spotcheckServiceImpl.modifySpotcheck(spotcheck);
        }
    }

    @Override
    @Transactional
    public synchronized int countspot2(Integer spid) {
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);
        SpotcheckMember spotcheckMember = new SpotcheckMember();

        int count;// 该单位总人数
        int spot;// 抽到第几个人
        int bfb = spotcheck.getBfb();
        Random r = new Random();
        spotcheckMember.setSpid(spid);
        spotcheckMember.setIsFj(0);
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                .selectSpotcheckMemberByDetail(spotcheckMember);// 查询该spid下未抽的所有人
        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;
        for (SpotcheckMember sp : spotcheckmemberlist) {
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {

            String kmm = spotcheck.getKmm();
            //	List<String> dwmlist = getNonedwm(spid, kmm);
            List<String> dwmlist = getVeryDw(spid, kmm);
            //System.out.println("无资质单位：" + dwmlist.size());
            System.out.println("所有单位：" + dwmlist.size());
            for (String dwm : dwmlist) {
                spotPrize = new SpotPrize();
                spotPrize.setDwm(dwm);
                spotPrize.setSpid(spid);
                List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                count = splist.size();
                //System.out.println("该无资质单位总人数：" + count);
                System.out.println("单位总人数：" + count);
                spot = r.nextInt(count);
                //System.out.println("该无资质抽到第几个" + spot);
                System.out.println("该单位抽到第几个：" + dwmlist.size());
                spotPrize = new SpotPrize();
                spotPrize = splist.get(spot);
                //	System.out.println("无资质id" + spotPrize.getId());
                System.out.println("单位id" + spotPrize.getId());
                spotPrize.setStatus(1);
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            } // 先把每家单位的人员选出来

            spotPrize = new SpotPrize();
            spotPrize.setSpid(spid);
            spotPrize.setStatus(2);
            List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
            float xiaoshu;
            if (splist != null && !splist.isEmpty()) {
                xiaoshu = (spotcheckmemberlist.size() - dwmlist.size()) * bfb;
                int chouzhong = (int) Math.ceil(xiaoshu / 100);
                int[] randowmnums = RandomNums.getNnms(splist.size(), chouzhong);
                System.out.println("splist:" + splist.size());
                System.out.println("count:" + chouzhong);
                //System.out.println("randowmnums:" + randowmnums);
                for (int i : randowmnums) {
                    spotPrize = splist.get(i - 1);
                    spotPrize.setStatus(1);
                    System.out.println("i:" + i);
                    //System.out.println("spotPrize:" + spotPrize);
                    spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                }
            }


		/*	List<String> dwmhavelist = getHavedwmBySpid(spid);
			System.out.println("有资质未抽中：" + dwmhavelist.size());
			for (String dwm : dwmhavelist) {
				spotPrize = new SpotPrize();
				spotPrize.setDwm(dwm);
				spotPrize.setSpid(spid);
				List<SpotPrize> sphavelist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
				count = sphavelist.size();
				System.out.println("该有资质单位总人数：" + count);
				spot = r.nextInt(count);
				System.out.println("该有资质抽到第几个" + spot);
				spotPrize = new SpotPrize();
				spotPrize = sphavelist.get(spot);
				System.out.println("有资质id" + spotPrize.getId());
				spotPrize.setStatus(1);
				spotPrizeServiceImpl.updateSpotPrize(spotPrize);
			} // 先把没有资质单位的人员选出来
            */
            return 1;
        }
        return 0;

    }

    @Override

    public List<String> getHavedwmBySpid(Integer spid) {
        return spotcheckMemberMapper.getHavedwmBySpid(spid);
    }

    @Override
    public List<HashMap<String, Long>> getNotCheck(Integer spid, String dwm) {
        return spotcheckMemberMapper.getNotCheck(spid, dwm);
    }

    @Override
    @Transactional
    public synchronized int spotVeryDwMember(Integer spid) {
        //查询抽查计划是否存在，通过Spid
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);

        SpotcheckMember spotcheckMember = new SpotcheckMember();

        // 存取该单位总人数
        int count;

        int spot;

        //存取所设置的百分比
        int bfb = spotcheck.getBfb();

        //随机类，含有随机函数
        Random r = new Random();

        //将抽查计划ID存入对象中
        spotcheckMember.setSpid(spid);

        //这里否定了添加计划之后，先附加的抽查人员，因此之后不需要减去附加已抽查的人
        spotcheckMember.setIsFj(0);

        // 查询该spid下未抽的所有人
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper.selectSpotcheckMemberByDetail(spotcheckMember);

        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;

        for (SpotcheckMember sp : spotcheckmemberlist) {

            //这里是将抽查计划里面，抽查所有人员的状态记录在spotPrize表里面,Status代表抽查状态，1代表抽中,2代表未抽中
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }

        //将抽查上面所获取到的所有抽查人员信息插入到spot_prize表
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
            String kmm = spotcheck.getKmm();
            List<String> dwmlist = getVeryDw(spid, kmm);
            double members;

            System.out.println("所有单位：" + dwmlist.size());
            for (String dwm : dwmlist) {
                spotPrize = new SpotPrize();
                spotPrize.setDwm(dwm);
                spotPrize.setSpid(spid);
                List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                count = splist.size();
                System.out.println("该所有单位总人数：" + count);
                System.out.println("当前百分比为！" + bfb);
                if (bfb == 0) {
                    System.out.println("默认比例" + bfb);
                    members = count * 0.1;
                } else {
                    System.out.println("默认比例+百分之10以上的比例" + bfb);
                    members = count * (0.1 + (bfb * 0.01));
                }

                int chouzhong = (int) Math.ceil(members);
                int[] randowmnums = RandomNums.getNnms(splist.size(), chouzhong);
                System.out.println("该单位抽中人员" + chouzhong);

				/*spotPrize = new SpotPrize();
				spotPrize = splist.get(chouzhong);
				System.out.println("id" + spotPrize.getId());
				spotPrize.setStatus(1);
				spotPrizeServiceImpl.updateSpotPrize(spotPrize);*/

                for (int i : randowmnums) {
                    spotPrize = splist.get(i - 1);
                    spotPrize.setStatus(1);
                    System.out.println("抽到第:" + i);
                    //System.out.println("spotPrize:" + spotPrize);
                    spotPrizeServiceImpl.updateSpotPrize(spotPrize);
                }
            }
			/*spotPrize = new SpotPrize();
			spotPrize.setSpid(spid);
			spotPrize.setStatus(2);
			List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
			float xiaoshu;
			if (splist != null && !splist.isEmpty()) {
				xiaoshu = (spotcheckmemberlist.size() - dwmlist.size()) * bfb;
				int chouzhong = (int) Math.ceil(xiaoshu / 100);
				int[] randowmnums = RandomNums.getNnms(splist.size(), chouzhong);
				System.out.println("splist:" + splist.size());
				System.out.println("count:" + chouzhong);
				//System.out.println("randowmnums:" + randowmnums);
				for (int i : randowmnums) {
					spotPrize = splist.get(i - 1);
					spotPrize.setStatus(1);
					System.out.println("i:" + i);
					//System.out.println("spotPrize:" + spotPrize);
					spotPrizeServiceImpl.updateSpotPrize(spotPrize);
				}
			}

			 */
            return 1;
        }
        return 0;
    }

    @Override
    public List<String> getVeryDw(Integer spid, String kmm) {
        return spotcheckMemberMapper.getVeryDw(spid, kmm);
    }

    @Override
    public int upLoadImgurl(SpotcheckMember spotcheckMember, Pxbm pxbm) {
        try {
            spotcheckMemberMapper.uploadImgSpotheckMember(spotcheckMember);
            pxbmMapper.uploadImgPxbm(pxbm);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;

    }

    @Override
    public int deleteIjmgurl(SpotcheckMember spotcheckMember, Pxbm pxbm) {
        try {
            spotcheckMemberMapper.deleteImgurl(spotcheckMember);
            pxbmMapper.deleteImgurl(pxbm);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    @Override
    public Photo[] selectImgurl(Photo photo) {

        return spotcheckMemberMapper.selectImgurl(photo);

    }

    @Override
    public int insertKhphoto(Photo photo) {
        return spotcheckMemberMapper.insertKhphoto(photo);
    }

    @Override
    public int deleteimg(String urlName) {
        return spotcheckMemberMapper.deleteimg(urlName);
    }

    @Override
    public String selectKh_photo(SpotcheckMember spotcheckMember) {
        return spotcheckMemberMapper.selectKh_photo(spotcheckMember);
    }

    @Override
    public int updateCheckstatus(Integer spid) {
        return spotcheckMemberMapper.updateCheckstatus(spid);
    }

    @Override
    public int selectCheckstatus(Integer spid) {
        return spotcheckMemberMapper.selectCheckstatus(spid);
    }

    @Override
    public List<SpotcheckMember> getPxbmIdr_jgCheckstatus(Integer spid) {
        return spotcheckMemberMapper.getPxbmIdr_jgCheckstatus(spid);
    }

    @Override
    public int updateP_bj(List<Integer> idlist, Integer p_bj,Integer num) {
        return spotcheckMemberMapper.updateP_bj(idlist, p_bj,num);
    }

    @Override
    public List<String> listpxbmid(List<Integer> idlist) {
        return spotcheckMemberMapper.listpxbmid(idlist);
    }

    @Override
    public List<SpotcheckMember> selectIsCheckStatusSpotmember(Spotcheck spotcheck) {
        return spotcheckMemberMapper.selectIsCheckStatusSpotmember(spotcheck);
    }

    @Override
    public List<SpotcheckMember> selectNotCheckStatusSpotmember(Spotcheck spotcheck) {
        return spotcheckMemberMapper.selectNotCheckStatusSpotmember(spotcheck);
    }

    @Override
    public int addspotMember(SpotcheckMember spotcheckMember) {
        return spotcheckMemberMapper.addspotMember(spotcheckMember);
    }

    @Override
    public int updateCheckStusIsCZ(SpotcheckMember spotcheckMember) {
        return spotcheckMemberMapper.updateCheckStusIsCZ(spotcheckMember);
    }

    @Override
    public int updateCheckStusNotWCZ(SpotcheckMember spotcheckMember) {
        return spotcheckMemberMapper.updateCheckStusNotWCZ(spotcheckMember);
    }

    @Override
    public int updateSpmXhjd(SpotcheckMember spotcheckMember) {
        return spotcheckMemberMapper.updateSpmXhjd(spotcheckMember);
    }

    @Transactional
    @Override
    public int xhJdAlgorithmTime(Integer spid) {

        //查询该条监督计划
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);

        // 查询该条监督计划相应科目代码
        String zzCode = subjectServiceImpl.getSubjectByName(spotcheck.getKmm());

        spotcheck.setIsInput(1);
        // 修改记录状态为已导入
        spotcheckServiceImpl.modifySpotcheck(spotcheck);

        Job job = new Job();
        try {
            job.setSpid(spid);
            job.setStartdate(spotcheck.getStart());
            jobServiceImpl.addNewJob(job);
            CronUtil.schedule(spid.toString(), DateCoreUtil.getCron(spotcheck.getStart()),
                    () -> {
                        System.out.println(spotcheck.getAreaName());
                        System.out.println("启用协会监督计划！");
                        if (xhJdAlgorithm(spid) > 0) {
                            jobServiceImpl.updateJobUsed(spid);
                        }
                        CronUtil.remove(spid.toString());
                    });
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    @Override
    public synchronized int xhJdAlgorithm(Integer spid) {

        System.out.println(spid);

        //查询抽查计划是否存在，通过Spid
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);

        SpotcheckMember spotcheckMember = new SpotcheckMember();

        // 存取该单位总人数
        int count;

        //存取所设置的百分比
        int bfb = spotcheck.getBfb();

        //将抽查计划ID存入对象中
        spotcheckMember.setSpid(spid);

        //这里否定了添加计划之后，先附加的抽查人员，因此之后不需要减去附加已抽查的人
        spotcheckMember.setIsFj(0);

        // 查询该spid下未抽的所有人

        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper.getSelectXhJdSpotmember(spid);
        System.out.println("参与计划人数！"+spotcheckmemberlist.size());

        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;

        for (SpotcheckMember sp : spotcheckmemberlist) {

            //这里是将抽查计划里面，抽查所有人员的状态记录在spotPrize表里面,Status代表抽查状态，1代表抽中,2代表未抽中
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }

        //查询此次计划的已抽中人员或未抽中人员
       // spotcheckMember.setXhjd(spotcheck.getXhjd());
        //int checkStauCount = spotcheckMemberMapper.getSelectAleaderCheckStusSpm(spotcheckMember);


        //将抽查上面所获取到的所有抽查人员信息插入到spot_prize表
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
            String kmm = spotcheck.getKmm();
            double members;

            spotPrize = new SpotPrize();
            //spotPrize.setDwm(dwm);
            spotPrize.setSpid(spid);
            List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
            count = splist.size();
            System.out.println("该计划总人数" + count);
            System.out.println("当前百分比为！" + bfb);

            members = count*bfb*0.01;
            int chouzhong = (int) Math.ceil(members);
            if (spotcheck.getXhjd() == 1) {
                System.out.println("合格且已抽中的抽中人数："+chouzhong);
                if (chouzhong < 3) {
                    chouzhong = 3;
                    System.out.println("当前抽取计划为合格且抽中人员，比例为"+bfb+"小于3人,最终抽取个数为"+chouzhong);
                }
            } else {
                System.out.println("合格且未抽中的抽中人数："+chouzhong);
                if (chouzhong < 5) {
                    chouzhong = 5;
                    System.out.println("当前抽取计划为合格且未抽中人员，比例为"+bfb+"小于5人,最终抽取个数为"+chouzhong);
                }
            }
            int[] cymembers=new int[count];
            for (int i=0;i<cymembers.length;i++){
                cymembers[i]=i+1;
            }
            //需要抽取多少人
            int[] czmembers=new int[chouzhong];
            for (int i=0;i<czmembers.length;i++){
                int czcount=(int)(Math.random()*count);
                czmembers[i]=cymembers[czcount];
                if(count==0){
                   count++;
                }
                cymembers[czcount]=cymembers[count-1];
                count--;
            }

            for (int i : czmembers){
                spotPrize=splist.get(i-1);
                spotPrize.setStatus(1);
                System.out.println("现在依次顺序抽到下标为:" + i+"号");
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            }

           /* System.out.println("需要抽中人数：" + chouzhong);
            int[] randowmnums = RandomNums.getNnms(count, chouzhong);
            System.out.println("抽中人员" + chouzhong);
            for (int i : randowmnums) {
                spotPrize = splist.get(i - 1);
                spotPrize.setStatus(1);
                System.out.println("现在依次顺序抽到下标为:" + i);
                //System.out.println("spotPrize:" + spotPrize);
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            }

            */
            return 1;
        }
        return 0;
    }
    @Override
    public int deleteXhSpotmember(Integer id){
        return spotcheckMemberMapper.deleteXhSpotmember(id);
    }

}
