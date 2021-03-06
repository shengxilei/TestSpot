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
        int count;// ??????????????????
        int spot;// ??????????????????
        // int pipei=0;//???????????????
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

        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);// ??????????????????

        String zzCode = subjectServiceImpl.getSubjectByName(spotcheck.getKmm());// ????????????????????????

        spotcheckMemberMapper.insertFromMemberTemp(spid);// ??????????????????????????????
        spotcheck.setIsInput(1);
        spotcheckServiceImpl.modifySpotcheck(spotcheck);// ??????????????????????????????
        List<String> havedwnList = getHavedwmByCode(spid, zzCode);// ???????????????????????????????????????
        if (havedwnList != null && !havedwnList.isEmpty()) {
            spotcheckMemberMapper.updateByDwmSetZz(havedwnList);
        }
        Job job = null;
        switch (2) {// ==1??????????????????????????? ?????????1???????????????????????????????????????
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
								 case "??????":
								 case "??????":
								 case "??????":
								 case "??????":
								 case "??????":
								 case "??????":
									 System.out.println("?????????????????????????????????");
									 if (spotVeryDwMember(spid)>0) {
										 jobServiceImpl.updateJobUsed(spid);
									 }
									 break;
								 default:
									 System.out.println("??????????????????????????????!");
									 if (countspot2(spid) > 0) {
										 jobServiceImpl.updateJobUsed(spid);
									 }
							 }

					        */
                            System.out.println("?????????????????????????????????");
                            if (spotVeryDwMember(spid) > 0) {
                                jobServiceImpl.updateJobUsed(spid);
                            }
							 /*
					         if(spotcheck.getAreaName().equals("??????")==true||spotcheck.getAreaName().equals("??????")){
								 System.out.println("?????????????????????????????????");
								 if (spotVeryDwMember(spid)>0) {
									 jobServiceImpl.updateJobUsed(spid);
								 }
							 }else {
					         	System.out.println("??????????????????????????????!");
								 if (countspot2(spid) > 0) {
									 jobServiceImpl.updateJobUsed(spid);
								 }
							 }
							  */

                            CronUtil.remove(spid.toString());
                        });
                return 1;
            default:

                if (countspot(spid) > 0) { // ??????????????????
                    return 1;
                }
                break;
        }
        return 0;

    }

    /**
     * ????????????????????????
     *
     * @param spid
     * @param
     * @return
     */
    @Override
    public int countspot(Integer spid) {
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);
        SpotcheckMember spotcheckMember = new SpotcheckMember();

        int count;// ??????????????????
        int spot;// ??????????????????
        int bfb = spotcheck.getBfb();
        Random r = new Random();
        spotcheckMember.setSpid(spid);
        spotcheckMember.setIsFj(0);
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                .selectSpotcheckMemberByDetail(spotcheckMember);// ?????????spid?????????????????????
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
            System.out.println("??????????????????" + dwmlist.size());
            for (String dwm : dwmlist) {
                spotPrize = new SpotPrize();
                spotPrize.setDwm(dwm);
                spotPrize.setSpid(spid);
                List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                count = splist.size();
                System.out.println("??????????????????????????????" + count);
                spot = r.nextInt(count);
                System.out.println("???????????????????????????" + spot);
                spotPrize = new SpotPrize();
                spotPrize = splist.get(spot);
                System.out.println("?????????id" + spotPrize.getId());
                spotPrize.setStatus(1);
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            } // ??????????????????????????????????????????

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
     * ???member????????????????????????prize?????????????????????????????????
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
            return 1;//???????????????0????????????
        }
        return 0;
    }

    // ?????????????????????????????????
    void countspotTigger(Integer spid, int num) {
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);
        SpotcheckMember spotcheckMember = new SpotcheckMember();
        spotcheckMember.setSpid(spid);
        int count;// ??????????????????
        int spot;// ??????????????????
        // int pipei=0;//???????????????
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

        int count;// ??????????????????
        int spot;// ??????????????????
        int bfb = spotcheck.getBfb();
        Random r = new Random();
        spotcheckMember.setSpid(spid);
        spotcheckMember.setIsFj(0);
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper
                .selectSpotcheckMemberByDetail(spotcheckMember);// ?????????spid?????????????????????
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
            //System.out.println("??????????????????" + dwmlist.size());
            System.out.println("???????????????" + dwmlist.size());
            for (String dwm : dwmlist) {
                spotPrize = new SpotPrize();
                spotPrize.setDwm(dwm);
                spotPrize.setSpid(spid);
                List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                count = splist.size();
                //System.out.println("??????????????????????????????" + count);
                System.out.println("??????????????????" + count);
                spot = r.nextInt(count);
                //System.out.println("???????????????????????????" + spot);
                System.out.println("???????????????????????????" + dwmlist.size());
                spotPrize = new SpotPrize();
                spotPrize = splist.get(spot);
                //	System.out.println("?????????id" + spotPrize.getId());
                System.out.println("??????id" + spotPrize.getId());
                spotPrize.setStatus(1);
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            } // ????????????????????????????????????

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
			System.out.println("?????????????????????" + dwmhavelist.size());
			for (String dwm : dwmhavelist) {
				spotPrize = new SpotPrize();
				spotPrize.setDwm(dwm);
				spotPrize.setSpid(spid);
				List<SpotPrize> sphavelist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
				count = sphavelist.size();
				System.out.println("??????????????????????????????" + count);
				spot = r.nextInt(count);
				System.out.println("???????????????????????????" + spot);
				spotPrize = new SpotPrize();
				spotPrize = sphavelist.get(spot);
				System.out.println("?????????id" + spotPrize.getId());
				spotPrize.setStatus(1);
				spotPrizeServiceImpl.updateSpotPrize(spotPrize);
			} // ??????????????????????????????????????????
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
        //???????????????????????????????????????Spid
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);

        SpotcheckMember spotcheckMember = new SpotcheckMember();

        // ????????????????????????
        int count;

        int spot;

        //???????????????????????????
        int bfb = spotcheck.getBfb();

        //??????????????????????????????
        Random r = new Random();

        //???????????????ID???????????????
        spotcheckMember.setSpid(spid);

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        spotcheckMember.setIsFj(0);

        // ?????????spid?????????????????????
        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper.selectSpotcheckMemberByDetail(spotcheckMember);

        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;

        for (SpotcheckMember sp : spotcheckmemberlist) {

            //?????????????????????????????????????????????????????????????????????spotPrize?????????,Status?????????????????????1????????????,2???????????????
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }

        //???????????????????????????????????????????????????????????????spot_prize???
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
            String kmm = spotcheck.getKmm();
            List<String> dwmlist = getVeryDw(spid, kmm);
            double members;

            System.out.println("???????????????" + dwmlist.size());
            for (String dwm : dwmlist) {
                spotPrize = new SpotPrize();
                spotPrize.setDwm(dwm);
                spotPrize.setSpid(spid);
                List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
                count = splist.size();
                System.out.println("???????????????????????????" + count);
                System.out.println("?????????????????????" + bfb);
                if (bfb == 0) {
                    System.out.println("????????????" + bfb);
                    members = count * 0.1;
                } else {
                    System.out.println("????????????+?????????10???????????????" + bfb);
                    members = count * (0.1 + (bfb * 0.01));
                }

                int chouzhong = (int) Math.ceil(members);
                int[] randowmnums = RandomNums.getNnms(splist.size(), chouzhong);
                System.out.println("?????????????????????" + chouzhong);

				/*spotPrize = new SpotPrize();
				spotPrize = splist.get(chouzhong);
				System.out.println("id" + spotPrize.getId());
				spotPrize.setStatus(1);
				spotPrizeServiceImpl.updateSpotPrize(spotPrize);*/

                for (int i : randowmnums) {
                    spotPrize = splist.get(i - 1);
                    spotPrize.setStatus(1);
                    System.out.println("?????????:" + i);
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

        //????????????????????????
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);

        // ??????????????????????????????????????????
        String zzCode = subjectServiceImpl.getSubjectByName(spotcheck.getKmm());

        spotcheck.setIsInput(1);
        // ??????????????????????????????
        spotcheckServiceImpl.modifySpotcheck(spotcheck);

        Job job = new Job();
        try {
            job.setSpid(spid);
            job.setStartdate(spotcheck.getStart());
            jobServiceImpl.addNewJob(job);
            CronUtil.schedule(spid.toString(), DateCoreUtil.getCron(spotcheck.getStart()),
                    () -> {
                        System.out.println(spotcheck.getAreaName());
                        System.out.println("???????????????????????????");
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

        //???????????????????????????????????????Spid
        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spid);

        SpotcheckMember spotcheckMember = new SpotcheckMember();

        // ????????????????????????
        int count;

        //???????????????????????????
        int bfb = spotcheck.getBfb();

        //???????????????ID???????????????
        spotcheckMember.setSpid(spid);

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        spotcheckMember.setIsFj(0);

        // ?????????spid?????????????????????

        List<SpotcheckMember> spotcheckmemberlist = spotcheckMemberMapper.getSelectXhJdSpotmember(spid);
        System.out.println("?????????????????????"+spotcheckmemberlist.size());

        List<SpotPrize> spotPrizeList = new ArrayList<>();
        SpotPrize spotPrize;

        for (SpotcheckMember sp : spotcheckmemberlist) {

            //?????????????????????????????????????????????????????????????????????spotPrize?????????,Status?????????????????????1????????????,2???????????????
            spotPrize = new SpotPrize();
            spotPrize.setSpmemid(sp.getId());
            spotPrize.setStatus(2);
            spotPrize.setSpid(spid);
            spotPrize.setDwm(sp.getDwm());
            spotPrizeList.add(spotPrize);
        }

        //??????????????????????????????????????????????????????
       // spotcheckMember.setXhjd(spotcheck.getXhjd());
        //int checkStauCount = spotcheckMemberMapper.getSelectAleaderCheckStusSpm(spotcheckMember);


        //???????????????????????????????????????????????????????????????spot_prize???
        if (spotPrizeServiceImpl.addSpotPrizeList(spotPrizeList) > 0) {
            String kmm = spotcheck.getKmm();
            double members;

            spotPrize = new SpotPrize();
            //spotPrize.setDwm(dwm);
            spotPrize.setSpid(spid);
            List<SpotPrize> splist = spotPrizeServiceImpl.selectSpotPrizeByDetail(spotPrize);
            count = splist.size();
            System.out.println("??????????????????" + count);
            System.out.println("?????????????????????" + bfb);

            members = count*bfb*0.01;
            int chouzhong = (int) Math.ceil(members);
            if (spotcheck.getXhjd() == 1) {
                System.out.println("????????????????????????????????????"+chouzhong);
                if (chouzhong < 3) {
                    chouzhong = 3;
                    System.out.println("??????????????????????????????????????????????????????"+bfb+"??????3???,?????????????????????"+chouzhong);
                }
            } else {
                System.out.println("????????????????????????????????????"+chouzhong);
                if (chouzhong < 5) {
                    chouzhong = 5;
                    System.out.println("?????????????????????????????????????????????????????????"+bfb+"??????5???,?????????????????????"+chouzhong);
                }
            }
            int[] cymembers=new int[count];
            for (int i=0;i<cymembers.length;i++){
                cymembers[i]=i+1;
            }
            //?????????????????????
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
                System.out.println("?????????????????????????????????:" + i+"???");
                spotPrizeServiceImpl.updateSpotPrize(spotPrize);
            }

           /* System.out.println("?????????????????????" + chouzhong);
            int[] randowmnums = RandomNums.getNnms(count, chouzhong);
            System.out.println("????????????" + chouzhong);
            for (int i : randowmnums) {
                spotPrize = splist.get(i - 1);
                spotPrize.setStatus(1);
                System.out.println("?????????????????????????????????:" + i);
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
