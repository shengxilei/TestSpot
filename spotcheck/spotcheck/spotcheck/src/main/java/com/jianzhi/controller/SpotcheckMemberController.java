package com.jianzhi.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jianzhi.model.*;
import com.jianzhi.service.impl.PxbmServiceImpl;
import com.sheng.service.PxbmService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.service.MemberTempService;
import com.jianzhi.service.RedisService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.service.SpotcheckmemberService;
import com.jianzhi.util.MyExcel;
import com.jianzhi.util.R;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/spotcheckmember")
//@WebServlet("/spotcheckmember")
public class SpotcheckMemberController {

    @Resource
    private SpotcheckmemberService spotcheckmemberServiceImpl;
    @Resource
    private SpotcheckService spotcheckServiceImpl;
    @Autowired
    private EnterpriseService enterpriseServiceImpl;
    @Autowired
    private MemberTempService memberTempServiceImpl;

    @Autowired
    private RedisService redisService;

    @Resource
    private  PxbmServiceImpl pxbmService;

    @MyLog(value = "查询记录中的具体人员") // 这里添加了AOP的自定义注解
    @PostMapping("/list")
    @ApiOperation(value = "查询该记录正式导入的人员", notes = "")
    public Object getSpotcheckMemberList(SpotcheckMember spotcheckMember,
                                         @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                                         @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SpotcheckMember> list = spotcheckmemberServiceImpl.getSpotcheckMemberList(spotcheckMember);
        PageInfo<SpotcheckMember> pageInfo = new PageInfo<SpotcheckMember>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    }

    @MyLog(value = "导出该抽查记录的人员") // 这里添加了AOP的自定义注解
    @PostMapping("/export")
    @ApiOperation(value = "Excel导出该抽查记录的人员信息", notes = "")
    public void exportSpotcheckMemberList(HttpServletResponse response, @RequestBody SpotcheckMember spotcheckMember) {

        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spotcheckMember.getSpid());

        List<List<String>> rows = new ArrayList<>();

        rows.add(CollUtil.newArrayList("序号", "姓名", "身份证", "性别", "单位名称", "考试科目", "抽查状态", "考核结果","理论结果","公示状态"));
        List<String> row;
        int i = 0;
        List<SpotcheckMember> list = spotcheckmemberServiceImpl.getSpotcheckMemberList(spotcheckMember);
        for (SpotcheckMember sp : list) {
            i = i + 1;
            row = CollUtil.newArrayList(String.valueOf(i), sp.getXm(), sp.getSfzhm(),
                    sp.getXb() == null ? null : sp.getXb() == 0 ? "男" : "女", sp.getDwm(), sp.getKmm(),
                    sp.getCheckstatus() == 0 ? "待抽查" : sp.getCheckstatus() == 1 ? "抽中" : "未抽中",
                    sp.getJg() == 0 ? "未设" : sp.getJg() == 1 ? "合格" : "不合格",
                    sp.getLl_jg()==0 ? "未设" : sp.getLl_jg() == 1 ? "合格" : "不合格",
                    sp.getP_bj()==0 ? "未设" : sp.getP_bj() == 1 ? "已公示" : "未公示");
            rows.add(row);
        }
        try {
            MyExcel.getExcel(response, rows.get(0).size(), rows, spotcheck.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @MyLog(value = "查询实操抽中人员") // 这里添加了AOP的自定义注解
    @PostMapping("/listSpot")
    @ApiOperation(value = "查询实操抽中人员", notes = "")
    public Object listWinSpotcheckMemberList(SpotcheckMember spotcheckMember,
                                             @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                                             @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        //spotcheckMember.setCheckstatus(1);
        List<SpotcheckMember> list = spotcheckmemberServiceImpl.getSpotcheckMemberList(spotcheckMember);

        PageInfo<SpotcheckMember> pageInfo = new PageInfo<SpotcheckMember>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    }

    @MyLog(value = "导出实操抽中人员") // 这里添加了AOP的自定义注解
    @PostMapping("/exportSpot")
    @ApiOperation(value = "Excel导出被抽中的人员信息", notes = "")
    public void exportWinSpotcheckMemberList(HttpServletResponse response,
                                             @RequestBody SpotcheckMember spotcheckMember) {

        Spotcheck spotcheck = spotcheckServiceImpl.getSpotcheckById(spotcheckMember.getSpid());
        spotcheckMember.setCheckstatus(1);
        List<List<String>> rows = new ArrayList<>();

        rows.add(CollUtil.newArrayList("序号", "姓名", "身份证", "性别", "单位名称", "考试科目", "抽查状态", "考核结果"));
        List<String> row;
        List<SpotcheckMember> list = spotcheckmemberServiceImpl.getSpotcheckMemberList(spotcheckMember);
        for (SpotcheckMember sp : list) {
            row = CollUtil.newArrayList(sp.getXh().toString(), sp.getXm(), sp.getSfzhm(),
                    sp.getXb() == null ? null : sp.getXb() == 0 ? "男" : "女", sp.getDwm(), sp.getKmm(),
                    sp.getCheckstatus() == 0 ? "待抽查" : sp.getCheckstatus() == 1 ? "抽中" : "未抽中",
                    sp.getJg() == 0 ? "未设" : sp.getJg() == 1 ? "合格" : "不合格");
            rows.add(row);
        }
        try {
            MyExcel.getExcel(response, rows.get(0).size(), rows, spotcheck.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @MyLog(value = "该单位该条抽查记录中存在的任务人员") // 这里添加了AOP的自定义注解
    @PostMapping("/entmemberlist")
    @ApiOperation(value = "查询该单位该条记录中存在的人员", notes = "")
    public Object getEntmemberlist(HttpServletRequest request, int spid, SpotcheckMember spotcheckMember,
                                   @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                                   @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise enterprise = enterpriseServiceImpl.getEnterprise(entid);
        spotcheckMember.setDwm(enterprise.getName());
        PageHelper.startPage(pageNum, pageSize);
        List<SpotcheckMember> list = spotcheckmemberServiceImpl.getSpotcheckMemberList(spotcheckMember);
        PageInfo<SpotcheckMember> pageInfo = new PageInfo<SpotcheckMember>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    }

    @MyLog(value = "点击抽查") // 这里添加了AOP的自定义注解
    @PostMapping("/spot")
    @ApiOperation(value = "点击抽查，抽中与未抽中", notes = "")
    public Object spotcheck(String id, String spid) {
        Map<String, Object> map = new HashMap<String, Object>();
        int status = spotcheckmemberServiceImpl.spot(id, spid);
        if (status == 4) {
            return R.error("抽查未启动，请联系管理员");
        }
        map.put("status", status);
        return R.ok().put("spotjg", map);
    }

    @MyLog(value = "点击抽查") // 这里添加了AOP的自定义注解
    @PostMapping("/spotrandom")
    @ApiOperation(value = "点击抽查，抽中与未抽中", notes = "")
    public Object spotrandom(Integer id, Integer spid) {
        int status = spotcheckmemberServiceImpl.spotrandom(id, spid);
        if (status > 0) {
            return R.ok("已进入抽取队列");
        }
        return R.error("进入队列失败，请重试");
    }

    @MyLog(value = "修改人员抽查状态") // 这里添加了AOP的自定义注解
    @PostMapping("/modify")
    @ApiOperation(value = "修改抽查人员的是否被抽中", notes = "")
    public Object modifySpotcheck(SpotcheckMember spotcheckMember, String editor) {
        SpotcheckMember mem = spotcheckmemberServiceImpl.getSpotcheckMemberById(spotcheckMember.getId());
        if (mem != null) {
            Spotcheck sp = spotcheckServiceImpl.getSpotcheckById(mem.getSpid());
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(sp.getStart());
            newTime.add(Calendar.MINUTE, -2);//开始时间减2分钟
            Date dt1 = newTime.getTime();
            Date now = new Date();

            if (now.after(dt1) && now.before(sp.getStart())) {
                return R.error("离抽查开始只剩2分钟，关闭附加功能");
            }
            if (sp != null) {
                if (sp.getSpotstatus() != 1) {
                    if (mem.getCheckstatus() == 1) {
                        return R.error("改人员已被抽中无法修改");
                    }
                    if (spotcheckmemberServiceImpl.modifySpotcheckMember(spotcheckMember, editor) > 0) {
                        return R.ok("修改成功");
                    }
                } else {
                    return R.error("该记录正在抽查中,无法修改");
                }
            }
        }

        return R.error("未知原因修改失败，请联系管理员");
    }

    @MyLog("导入实操结果")
    @PostMapping("/inputCheck")
    @ApiOperation(value = "Excel导入实操结果", notes = "")
    public R updateMemberTempList(@RequestParam("memberExcel") MultipartFile memberExcel, Integer spid) {
        if (memberExcel != null && !memberExcel.isEmpty()) {
            ExcelReader excelReader;
            int num = 0;
            try {
                excelReader = ExcelUtil.getReader(memberExcel.getInputStream());
                List<SpotcheckMember> spotcheckMemberList = new ArrayList<SpotcheckMember>();
                SpotcheckMember spotcheckMember;
                List<Map<String, Object>> readAll = excelReader.read(1, 2, Integer.MAX_VALUE);
                for (Map<String, Object> map : readAll) {
                    spotcheckMember = new SpotcheckMember();
                    spotcheckMember.setXm((String) map.get("姓名"));
                    spotcheckMember.setXb(((String) map.get("性别")).equals("男") ? 0 : 1);
                    spotcheckMember.setSfzhm((String) map.get("身份证号码"));
                    spotcheckMember.setSpid(spid);
                    spotcheckMember.setDwm((String) map.get("单位名称"));
                    String jg = (String) map.get("考核结果");
                    spotcheckMember.setJg(((String) jg).equals("合格") ? 1 : 2);
                    spotcheckMemberList.add(spotcheckMember);
                }

                num = spotcheckmemberServiceImpl.updateSpotcheckMemberList(spotcheckMemberList);

                if (num > 0) {
                    return R.ok().put("num", num).put("msg", "成功导入");
                } else {
                    return R.error("导入格式错误或者没有数据");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return R.error("解析excel失败");
    }

    @MyLog("导入抽查人员")
    @PostMapping("/input")
    @ApiOperation(value = "Excel导入抽查人员到正式表", notes = "")
    public R inputMemberTempListByExcel(@RequestParam("memberExcel") MultipartFile memberExcel, Integer spid) {
        Spotcheck ss = spotcheckServiceImpl.getSpotcheckById(spid);
        if (ss != null) {
            if (ss.getIsInput() > 0) {
                return R.error("该记录已导入，不能再次导入");
            } else {
                if (memberExcel != null && !memberExcel.isEmpty()) {
                    ExcelReader excelReader;
                    try {
                        excelReader = ExcelUtil.getReader(memberExcel.getInputStream());
                        List<SpotcheckMember> spotcheckMemberList = new ArrayList<SpotcheckMember>();
                        SpotcheckMember spotcheckMember;
                        List<Map<String, Object>> readAll = excelReader.readAll();
                        for (Map<String, Object> map : readAll) {
                            spotcheckMember = new SpotcheckMember();
                            spotcheckMember.setXm((String) map.get("姓名"));
                            spotcheckMember.setXb(((String) map.get("性别")).equals("男") ? 0 : 1);
                            spotcheckMember.setSfzhm((String) map.get("身份证号码"));
                            spotcheckMember.setSpid(spid);
                            spotcheckMember.setDwm((String) map.get("单位名称"));
                            spotcheckMember.setXh(Integer.valueOf(map.get("序号").toString()));
                            spotcheckMember.setCheckstatus(Integer.valueOf(map.get("序号").toString()));
                            spotcheckMember.setKmm((String) map.get("考试科目"));
                            spotcheckMemberList.add(spotcheckMember);
                        }
                        int num = spotcheckmemberServiceImpl.addMemberList(spotcheckMemberList, spid);
                        if (num > 0) {
                            return R.ok().put("num", num).put("msg", "成功导入");
                        } else {
                            return R.error("导入格式错误或者没有数据");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return R.error("解析excel失败");
    }

    @MyLog("从临时表导入抽查人员")
    @PostMapping("/inputTemp")
    @ApiOperation(value = "临时表中的抽查记录人员中导入到正式表的抽查记录人员", notes = "")
    public R inputByMemberTempList(Integer spid) {
        Spotcheck ss = spotcheckServiceImpl.getSpotcheckById(spid);
        if (ss != null) {
            if (ss.getIsInput() > 0) {
                return R.error("该记录已导入，不能再次导入");
            } else {
                MemberTemp memTemp = new MemberTemp();
                memTemp.setSpid(spid);
                List<MemberTemp> templist = memberTempServiceImpl.getTempList(memTemp);
                for (MemberTemp memberTemp : templist) {

                    /*之前的条件： ||memberTemp.getIsSB()!=1||memberTemp.getJg()!=1*/
                    //return R.error("临时表中存在未补充承诺书、社保、理论成绩不合格的人员");

                    if (memberTemp.getIsCNS() != 1) {
                        return R.error("临时表中存在未补充承诺书的人员");
                    }
                }
                int num = spotcheckmemberServiceImpl.addMemberList(spid);
                if (num > 0) {
                    return R.ok().put("num", num).put("msg", "成功导入");
                } else {
                    return R.error("导入格式错误或者没有数据");
                }

            }
        }

        return R.error("导入失败，请联系管理员");
    }

    @MyLog("修改实操考核结果")
    @PostMapping("/modifyjg")
    @ApiOperation(value = "修改实操考核结果", notes = "")
    public R updateJgStatus(@RequestParam("idlist") List<Integer> idlist, Integer jg) {
        SpotcheckMember sm;
        if (idlist != null) {
            if (spotcheckServiceImpl.getSpotcheckById(spotcheckmemberServiceImpl.
                    getSpotcheckMemberById(idlist.get(0)).getSpid()).getSpotstatus() != 2) {
                return R.error("该记录未开始或在抽查状态，无法录入");
            }
            ;
            for (Integer id : idlist) {
                sm = spotcheckmemberServiceImpl.getSpotcheckMemberById(id);
               /* if (sm.getCheckstatus() == 0) {
                    return R.error(sm.getXm() + " " + sm.getSfzhm() + "状态为未抽查无法修改");
                }
                */
            }
            spotcheckmemberServiceImpl.updateJgStatus(idlist, jg);
            return R.ok("录入成功");
        } else {
            return R.error("请选择后再点击");
        }

    }


    @MyLog(value = "查询未抽的单位及人数") // 这里添加了AOP的自定义注解
    @PostMapping("/notspot")
    @ApiOperation(value = "查询该记录正式导入的人员", notes = "")
    public Object getNoCheckdw(String dwm, Integer spid,
                               @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                               @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<HashMap<String, Long>> list = spotcheckmemberServiceImpl.getNotCheck(spid, dwm);
        PageInfo<HashMap<String, Long>> pageInfo = new PageInfo<HashMap<String, Long>>(list);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    }

    @MyLog("导入图片")
    @PostMapping("/uploadImg")
    @ApiOperation(value = "导入图片", notes = "")
    public Object uploadImg(@RequestParam("memberExcel") MultipartFile memberExcel, HttpServletRequest request, HttpServletResponse response, String pxbmId, Integer spid) throws IllegalStateException, IOException {


        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/imgs/";

        String localDir = "D://upload/imgs";
//        String localDir = request.getServletContext().getRealPath("upload/imgs");
        //获取文件的名称
        String fileName = memberExcel.getOriginalFilename();

        //校验图片类型
        if (!fileName.matches("^.+\\.(jpg|png|gif|JPG|PNG|GIF)$")) {
            return R.error("文件格式不对!");
        }

        //图片类型
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        //使用UUID定义文件名称
        String uuid = UUID.randomUUID().toString().replace("-", "");

        String newFileName = new Date().getTime() + "_" + uuid + fileType;

        String url = "";

        File targetFile = null;
        //准备文件夹
        String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

        //方法读取Tomcat中文件时，发现 该方法被画上了横线，已过期，不建议使用
        //String localDir=request.getRealPath("/");

        File dirFile = new File(localDir + "/" + dateDir);
        if (!dirFile.exists()) {
            //如果文件夹不存在，则创建文件夹
            dirFile.mkdirs();
        }

        targetFile = new File(dirFile, newFileName);

        try {
            memberExcel.transferTo(targetFile);
            url =request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/imgs/"+dateDir + "/" + newFileName;

        } catch (Exception e) {
            e.printStackTrace();
        }

        Pxbm pxbm = new Pxbm();
        SpotcheckMember spotcheckMember = new SpotcheckMember();
        Photo photo=new Photo();

        pxbm.setSpid(spid);
        pxbm.setId(pxbmId);
        pxbm.setKhphoto(url);

        spotcheckMember.setSpid(spid);
        spotcheckMember.setPxbmid(pxbmId);
        spotcheckMember.setKhphoto(url);

        photo.setSpid(spid);
        photo.setPxbmId(pxbmId);
        photo.setKhphoto(url);



        String isurl=null;
        isurl =spotcheckmemberServiceImpl.selectKh_photo(spotcheckMember);
        if(isurl!=null){
            StringBuilder stringBuilder=new StringBuilder(isurl);
            stringBuilder.append(";"+url);
            spotcheckMember.setKhphoto(stringBuilder.toString());
            pxbm.setKhphoto(stringBuilder.toString());
        }


        int count2=spotcheckmemberServiceImpl.insertKhphoto(photo);

        int count = spotcheckmemberServiceImpl.upLoadImgurl(spotcheckMember, pxbm);


        Photo[]photos=spotcheckmemberServiceImpl.selectImgurl(photo);
        Map<String, Object> map = new HashMap<String, Object>();

        if (count > 0&&count2>0) {
            map.put("code",0);
            map.put("msg","上传成功！");
            map.put("photos",photos);
            return map;
        } else {
            map.put("code",-1);
            map.put("msg","上传失败！");
            return  map;
        }

    }

    @MyLog("数据同步")
    @PostMapping("/dataSynchronization")
    @ApiOperation(value = "数据同步", notes = "")
    public R dataSynchronization(HttpServletRequest request,Integer spid){
        List<Pxbm> list=pxbmService.selectPxbmInformations(spid);
       Spotcheck spotcheck=spotcheckServiceImpl.selectSpotcheckInfos(spid);
        
       if(list.size()==0){
           return  R.error("异常！");
       }
       return R.ok("成功！");

    }

   /* @Autowired
    private RestTemplate restTemplate;
    @PostMapping("tedaf")
    public String te(){
        String url = restTemplate.postForObject("url", new ArrayList<>(), String.class);
    }*/

    @MyLog("图片删除")
    @PostMapping("/deleteImg")
    @ApiOperation(value = "图片删除",notes ="" )
    public  R deleteImg(HttpServletRequest request,Integer spid,String pxbmId,String[] khPhoto){
       // System.out.println("数组长度:"+pxbmId.length);
        System.out.println("计划ID"+spid);
        int sun=0;
        for (String url: khPhoto) {

            SpotcheckMember spotcheckMember = new SpotcheckMember();
            spotcheckMember.setPxbmid(pxbmId);
            spotcheckMember.setSpid(spid);

            Pxbm pxbm = new Pxbm();
            pxbm.setId(pxbmId);
            pxbm.setSpid(spid);

            Photo photo = new Photo();
            photo.setSpid(spid);
            photo.setPxbmId(pxbmId);


            // String imgUrl=spotcheckmemberServiceImpl.selectImgurl(spotcheckMember);
            int Counts = spotcheckmemberServiceImpl.deleteimg(url);

            Photo[] photos = spotcheckmemberServiceImpl.selectImgurl(photo);
            StringBuilder stringBuilder = new StringBuilder();
            for (Photo item : photos) {
                stringBuilder.append(item.getKhphoto() + ";");
            }
            spotcheckMember.setKhphoto(stringBuilder.toString());
            pxbm.setKhphoto(stringBuilder.toString());

            int Count = spotcheckmemberServiceImpl.deleteIjmgurl(spotcheckMember, pxbm);

            //String imgUrl2=imgUrl.replaceAll( "\\\\ ",   "/");
            // String a= imgUrl2.substring(6,17);
            // String b=imgUrl2.substring(17);

            //String sbsurl=khPhoto.split()

            File file = new File("D://upload/" + url.substring(url.lastIndexOf("imgs")));
            file.delete();

            if (Counts > 0 && Count > 0) {
                sun++;
            } else {
               sun=0;
            }
        }
        if(sun>0){
            return  R.ok("删除成功！");
        }else {
            return  R.error("删除失败！");
        }
    }
    @MyLog("一键修改抽查状态")
    @PostMapping("/updateCheckstatus")
    @ApiOperation(value = "一键修改抽查状态",notes ="" )
    public  R updateCheckstatus(HttpServletRequest request,Integer spid){

        int selectCount=spotcheckmemberServiceImpl.selectCheckstatus(spid);

        if(selectCount!=0){
            return R.error("您已经提交了数据,请勿重复提交！");
        }

        int count=spotcheckmemberServiceImpl.updateCheckstatus(spid);
        if (count>0){
            return R.ok("修改成功！");
        }else {
           return R.error("修改失败,请联系管理员！");
        }

    }

    @MyLog("同步公示字段数据")
    @PostMapping("updateP_BJ")
    @ApiOperation(value = "同步公示字段",notes = "")
    public R updateP_BJ(@RequestParam("idlist") List<Integer> idlist,Integer p_bj,Integer num) {
        int leng1=0;
       try {
           int pCount = pxbmService.updateP_bj(idlist, p_bj);
           int sCount = spotcheckmemberServiceImpl.updateP_bj(idlist, p_bj,num);
           if (sCount > 0 && pCount > 0) {
               System.out.println("本地数据同步成功！");
           } else {
               leng1=-1;
               return R.error("未知原因，请联系管理员！");

           }
       }catch (Exception e){
            e.printStackTrace();
       }
      /*int leng=0;
       try {
           JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
           factory.setServiceClass(PxbmService.class);
           factory.setAddress("http://202.75.213.91/SSMCXF/webservice/pxbm?wsdl");
           // 需要服务接口文件
           PxbmService client = (PxbmService) factory.create();
           List<String> list = spotcheckmemberServiceImpl.listpxbmid(idlist);

           System.out.println(list.size());

           for (String li : list) {
               try {
                   int count = client.selectPxbmIDSpid(li);
                   if (count > 0) {
                       System.out.println("匹配成功！");
                   } else {
                       leng=-1;
                       return R.error("数据信息异常！同步数据失败！");
                   }
                   int count2 = client.updatePBJ(p_bj.toString(),li);
                   if (count2 > 0) {
                       System.out.println("数据更新成功！");
                   } else {
                       R.error("未知原因，请联系管理员！");
                       leng=-1;
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       if(leng1==-1&&leng==-1){
           return R.error("-1，数据有误，请联系管理员！");
       }else {
           return R.ok("数据同步成功！");
       }

       */
        return R.ok();
    }


    @MyLog("同步公示字段数据")
    @PostMapping("updateP_BJService")
    @ApiOperation(value = "同步公示字段",notes = "")
    public R updateP_BJService(@RequestParam("idlist") List<Integer> idlist) {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(PxbmService.class);
        factory.setAddress("http://202.75.213.91/SSMCXF/webservice/pxbm?wsdl");
        // 需要服务接口文件
        PxbmService client = (PxbmService) factory.create();
        for (Integer id : idlist) {
            System.out.println("获取到Spid"+id);
            List<SpotcheckMember> list = spotcheckmemberServiceImpl.getPxbmIdr_jgCheckstatus(id);
            System.out.println("通过spid查询到的该计划总人数："+list.size());

            for (SpotcheckMember item : list) {
                System.out.println("获取到的Pxbmid:"+item.getPxbmid());
                try {
                    int count1=client.selectPxbmIDSpid(item.getPxbmid());
                    System.out.println("将pxbmID传到167之后匹配到的结果返回：" + count1);
                    if (count1==0) {
                        System.out.println("数据信息缺失，同步失败！");
                        return R.error("数据信息有误，同步失败！");
                    }
                    if(item.getP_bj()!=1){
                        item.setP_bj(0);
                    }
                    int count2=client.updatePBJ(item.getP_bj().toString(),item.getPxbmid());
                    System.out.println("同步到167上的受影响行数更新公示字段:" +count2);
                    if(count2>0){
                        System.out.println("OK");
                    }else {
                        return R.error("同步异常，请联系管理员！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return R.ok("数据同步成功！");
    }
}
