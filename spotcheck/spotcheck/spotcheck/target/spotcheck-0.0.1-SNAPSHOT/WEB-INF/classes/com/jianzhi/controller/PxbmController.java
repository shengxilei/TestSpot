package com.jianzhi.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jianzhi.model.*;
import com.jianzhi.service.impl.MemberTempServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.service.PxbmApplyService;
import com.jianzhi.service.PxbmService;
import com.jianzhi.service.RedisService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.util.MyExcel;
import com.jianzhi.util.R;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pxbm")
public class PxbmController {

    @Resource
    private PxbmService pxbmServiceimpl;
    @Resource
    EnterpriseService enterpriseServiceImpl;
    @Resource
    SpotcheckService spotcheckServiceImpl;
    @Resource
    PxbmApplyService pxbmApplyServiceImpl;
    @Autowired
    private RedisService redisService;
    @Resource
    MemberTempServiceImpl memberTempService;


    @MyLog(value = "查询用户总表")  //这里添加了AOP的自定义注解
    @PostMapping("/list")
    @ApiOperation(value = "根据条件查询用户总表", notes = "")
    public Object getPxbmList(HttpServletRequest request, Pxbm pxbm,
                              @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                              @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        if (en.getRole().equals("dqeditor")) {
            pxbm.setAreaName(en.getAreaName());
        }
        if (en.getRole().equals("editor")) {
            /*条件修改为以单位条件查询*/
            //pxbm.setAreaName(en.getAreaName());
            pxbm.setJqdwm(en.getName());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Pxbm> list = pxbmServiceimpl.getPxbmList(pxbm);
        PageInfo<Pxbm> pageInfo = new PageInfo<Pxbm>(list);
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

    @MyLog(value = "查询根据spid查询用户总表科目")  //这里添加了AOP的自定义注解
    @PostMapping("/addlist")
    @ApiOperation(value = "根据登录人员权限与抽查记录主键查询该科目的人员", notes = "")
    public Object getPxbmSelectList(HttpServletRequest request, Pxbm pxbm, Integer spid,
                                    @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                                    @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        String token = request.getHeader("token");
        String kmm = spotcheckServiceImpl.getSpotcheckById(spid).getKmm();
        pxbm.setKmm(kmm);
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        if ("dqeditor".equals(en.getRole())) {
            pxbm.setAreaName(en.getAreaName());
        }
        if ("editor".equals(en.getRole())) {
            //现在查询条件改为通过单位名称查询，员先的地区名称去掉了
            //pxbm.setAreaName(en.getAreaName());
            pxbm.setJqdwm(en.getName());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Pxbm> list = pxbmServiceimpl.getPxbmList(pxbm);
        PageInfo<Pxbm> pageInfo = new PageInfo<Pxbm>(list);
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

    @MyLog(value = "导出总表数据")
    @PostMapping("/export")
    @CrossOrigin
    @ApiOperation(value = "导出总表数据", notes = "")
    public void exportPxbmList(HttpServletRequest request, HttpServletResponse response, @RequestBody Pxbm pxbm) {
        String token = request.getHeader("token");
        if (token == null) {
            R.error("请求格式错误");
        }
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        if (en.getRole().equals("dqeditor")) {
            pxbm.setAreaName(en.getAreaName());
        }
        List<Pxbm> list = pxbmServiceimpl.getPxbmList(pxbm);

        List<List<String>> rows = new ArrayList<>();

        rows.add(CollUtil.newArrayList("姓名", "身份证", "性别", "单位名称", "考试科目", "承诺书", "理论结果", "实操结果","公示状态","证书状态"));
        List<String> row;
        for (Pxbm px : list) {
            row = CollUtil.newArrayList(px.getXm(), px.getSfzhm(), px.getXb(), px.getDwm(),
                    px.getKmm(), px.getIsCNS() == 1 ? "有" : px.getIsCNS() == 2 ? "无" : "未设",
                   // px.getIsSB() == 1 ? "有" : px.getIsSB() == 2 ? "无" : "未设",
                    px.getJg().equals("0") ? "未设" : px.getJg().equals("1") ? "合格" : "不合格",
                    px.getOpZt().equals("0") ? "未设" : px.getOpZt().equals("1") ? "合格" : "不合格",
                    px.getP_bj()==0 ? "未设" : px.getP_bj()==1 ? "已公示" : "未公示",
                    px.getIsUse().equals("0") ? "未发证" : "已发证");
            rows.add(row);
        }
        try {
            MyExcel.getExcel(response, rows.get(0).size(), rows, "人员信息导出");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @MyLog("修改承诺书与社保状态")
    @PostMapping("/modifypxbm")
    @ApiOperation(value = "修改承诺书与社保状态", notes = "")
    public R modifyCNSandSB(@ApiParam(value = "状态0未设;1有;2无", required = true) @RequestParam("cns") Integer cns,
                            @ApiParam(value = "状态0未设;1有;2无", required = true) @RequestParam("sb") Integer sb,
                            @ApiParam(value = "备注", required = false) @RequestParam("bz2") String bz2,
                            @ApiParam(value = "主键", required = false) @RequestParam("id") String id) {
        if (id != null && !id.isEmpty()) {
            if (pxbmServiceimpl.modifyCNSandSB(cns, sb, bz2, id) > 0) {
                return R.ok("修改成功");
            } else {
                return R.error("修改失败");
            }
        }

        return R.error("未知原因，请联系管理员");

    }

    @MyLog("申请修改承诺书与社保")
    @PostMapping("/applyPxbm")
    @ApiOperation(value = "申请修改承诺书与社保", notes = "")
    public R applyPxbm(PxbmApply pxbmApply) {
        if (null == pxbmApply.getApply() || "".equals(pxbmApply.getApply())) {
            return R.error("请填写申请原因！");
        }
        Pxbm px = pxbmServiceimpl.getPxbmById(pxbmApply.getPxid());
        if (px.getIsApply() == 1) {
            return R.error("已提交申请，请等待审核！");
        }
        if (pxbmApplyServiceImpl.addPxbmApply(pxbmApply) > 0) {
            return R.ok();
        }
        return R.error("申请失败，请联系管理员");

    }

    @MyLog("查询申请修改的承诺书与社保记录")
    @PostMapping("/getApply")
    @ApiOperation(value = "查询申请修改的承诺书与社保记录", notes = "")
    public R getApplyPxbm(String pxid) {
        Map<String, Object> map = new HashMap<>();
        PxbmApply ap = pxbmApplyServiceImpl.getByPxid(pxid);
        if (ap != null) {
            map.put("apply", ap);
            return R.ok(map);
        }
        return R.error("查询失败，请重试");

    }

    @MyLog("审核提交申请的承诺书与社保")
    @PostMapping("/replyPxbm")
    @ApiOperation(value = "审核提交申请的承诺书与社保", notes = "")
    public R replyPxbm(PxbmApply pxbmApply) {
        if (pxbmApplyServiceImpl.checkstatus(pxbmApply) > 0) {
            return R.ok();
        }
        return R.error("回复失败，请联系管理员");
    }

    /*
    *Title:导入人员下载模板，标题由：地区、姓名、身份证号码、培训科目组成
    * Name:盛锡磊
    * Data:2021/4/3
     */
    @MyLog(value = "下载导入人员模板")
    @PostMapping("/excelDowload")
    @ApiOperation(value = "下载Excel模板人员信息", notes = "")
    public void excelDowload(HttpServletRequest request, HttpServletResponse response) {
        List<List<String>> rows = new ArrayList<>();
        rows.add(CollUtil.newArrayList("修改地区", "姓名", "身份证号码", "单位名称","培训科目"));
        List<String> row;
        row = CollUtil.newArrayList("温州", "小盛", "361121200108749321","杭州工程检测有限公司（单位全称，去掉分公司后缀词）","见证取样检测（房建）（科目全称）");
        rows.add(row);
        try {
            MyExcel.getExcel(response, rows.get(0).size(),rows,"人员导入模板");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
    * Title:导入人员信息到数据库，实质通过下载的导入人员信息模板上的姓名、身份证号码、培训科目名称来修改地区
    * Name:盛锡磊
    * Date:2021/4/3
     */

    @MyLog(value = "导入人员信息")
    @PostMapping("/updateMember")
    @ApiOperation(value = "导入Excel人员信息",notes = "")
    public Object updateMemberListByExcel(@RequestParam("memberExcel") MultipartFile memberExcel) throws IOException {


        ObjectMapper objectMapper =new ObjectMapper();

        InputStream inputStream = null;

        Map<String, Object> data = new HashMap();

        Map<String, Object> map = new HashMap<String, Object>();
        String msg=null;
        try {


            inputStream =memberExcel.getInputStream();//获取前端传递过来的文件对象，存储在“inputStream”中
            String fileName = memberExcel.getOriginalFilename();//获取文件名

            Workbook workbook =null; //用于存储解析后的Excel文件

            //判断文件扩展名为“.xls还是xlsx的Excel文件”,因为不同扩展名的Excel所用到的解析方法不同
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if(".xls".equals(fileType)){
                workbook= new HSSFWorkbook(inputStream);//HSSFWorkbook专门解析.xls文件
            }else if(".xlsx".equals(fileType)){
                workbook = new XSSFWorkbook(inputStream);//XSSFWorkbook专门解析.xlsx文件
            }

            ArrayList<Pxbm>list =new ArrayList<>();
            ArrayList<Pxbm>fountList =new ArrayList<>();
            ArrayList<Pxbm>spkList =new ArrayList<>();



            Sheet sheet; //工作表
            Row row;      //行
            Cell cell;    //单元格

            Pxbm pxbm1=null;
            int count = 0;
            int cgcs=0;
            int sxl=1;

            //循环遍历，获取数据
            for(int i=0;i<workbook.getNumberOfSheets();i++){

                sheet=workbook.getSheetAt(i);//获取sheet
                for(int j=sheet.getFirstRowNum();j<=sheet.getLastRowNum();j++) {//从有数据的第行开始遍历
                    row = sheet.getRow(j);
                    if (row != null && row.getFirstCellNum() != j) { //row.getFirstCellNum()!=j的作用是去除首行，即标题行，如果无标题行可将该条件去掉
                        Pxbm pxbm=new Pxbm();
                        ArrayList tempList =new ArrayList();

                        pxbm.setAreaName(row.getCell(0).toString());
                        pxbm.setXm(row.getCell(1).toString());
                        pxbm.setSfzhm(row.getCell(2).toString());
                        pxbm.setDwm(row.getCell(3).toString());
                        pxbm.setKmm(row.getCell(4).toString());

                        pxbm1 = pxbmServiceimpl.getPxbmExcelList(pxbm);
                        if (pxbm1 == null || pxbm1.equals("")) {

                            tempList.add(pxbm.getXm());
                            tempList.add(pxbm.getSfzhm());
                            tempList.add(pxbm.getKmm());
                            list.add(pxbm);
                            sxl=2;
                        }

                        if (sxl!=2) {

                            //这里是查询Excel表里的数据是否在抽查计划表中存在的人员信息
                           int spkCounts=pxbmServiceimpl.selecctPxbmId(pxbm1);

                           //判断存在的人员信息是否已经在抽查计划中
                            if(spkCounts<=0){
                                fountList.add(pxbm);
                            }else {
                                spkList.add(pxbm);
                            }

                        }

                        switch (pxbm.getAreaName()) {
                            case "杭州":
                                pxbm.setAreaCode("01");
                                break;
                            case "湖州":
                                pxbm.setAreaCode("02");
                                break;
                            case "嘉兴":
                                pxbm.setAreaCode("03");
                                break;
                            case "宁波":
                                pxbm.setAreaCode("04");
                                break;
                            case "绍兴":
                                pxbm.setAreaCode("05");
                                break;
                            case "台州":
                                pxbm.setAreaCode("06");
                                break;
                            case "温州":
                                pxbm.setAreaCode("07");
                                break;
                            case "丽水":
                                pxbm.setAreaCode("08");
                                break;
                            case "金华":
                                pxbm.setAreaCode("09");
                                break;
                            case "舟山":
                                pxbm.setAreaCode("10");
                                break;
                            case "衢州":
                                pxbm.setAreaCode("11");
                                break;

                        }


                    }
                }
            }

            if(spkList.size()!=0){
                map.put("code","-1");
                map.put("msg","这些数据是存在于之前的抽查计划当中，无法导入！");
                map.put("spkList",spkList);
                return  map;
            }
            ArrayList<Integer>successCounts =new ArrayList<Integer>();
            if(list.size() == 0){
                for(Pxbm pxbm2 : fountList){
                    int sum = pxbmServiceimpl.updatePxbmExcelList(pxbm2);
                    if (sum > 0) {
                        cgcs++;
                        successCounts.add(cgcs);
                    }
                }
                map.put("code", 0);
                map.put("msg", "导入成功！");
                map.put("success", true);
                map.put("successCounts",successCounts.size());
                if(cgcs>0&&sxl!=2){
                    return  map;
                }
            }else{
                map.put("list",list);
                map.put("code",-1);
                map.put("msg","导入失败！");

                //JSONObject.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }

        return map;
    }

    @MyLog(value = "查询培训报名表")  //这里添加了AOP的自定义注解
    @PostMapping("/douloadList")
    @ApiOperation(value = "根据条件查询用户总表", notes = "")
    public Object douloadList(HttpServletRequest request, Pxbm pxbm,
                              @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                              @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        PageHelper.startPage(pageNum, pageSize);
        List<Pxbm> list = pxbmServiceimpl.getPxbmList(pxbm);
        PageInfo<Pxbm> pageInfo = new PageInfo<Pxbm>(list);
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


    @MyLog("修改Pxbm表人员信息")
    @PostMapping("/updatePxbm")
    @ApiOperation(value = "修改培训报名表数据", notes = "")
    public R updatePxbmInfos(String id,String areaName){
        Pxbm pxbm=new Pxbm();
        pxbm.setId(id);
        pxbm.setAreaName(areaName);
       int sum=pxbmServiceimpl.selecctPxbmId(pxbm);
       if(sum>0){
           return R.error("修改失败！在抽查计划中存在此人！");
     }
        switch (pxbm.getAreaName()) {
            case "杭州":
                pxbm.setAreaCode("01");
                break;
            case "湖州":
                pxbm.setAreaCode("02");
                break;
            case "嘉兴":
                pxbm.setAreaCode("03");
                break;
            case "宁波":
                pxbm.setAreaCode("04");
                break;
            case "绍兴":
                pxbm.setAreaCode("05");
                break;
            case "台州":
                pxbm.setAreaCode("06");
                break;
            case "温州":
                pxbm.setAreaCode("07");
                break;
            case "丽水":
                pxbm.setAreaCode("08");
                break;
            case "金华":
                pxbm.setAreaCode("09");
                break;
            case "舟山":
                pxbm.setAreaCode("10");
                break;
            case "衢州":
                pxbm.setAreaCode("11");
                break;

        }

        int count=pxbmServiceimpl.updatePxbmInfo(pxbm);
        if(count>0){
            return R.ok("修改成功！");
        }else {
            return R.error("修改失败！");
        }

    }
    @MyLog(value = "下载导入实操结果模板")
    @PostMapping("/opZtDowlad")
    @ApiOperation(value = "下载Excel模板人员信息", notes = "")
    public void opZtDowlad(HttpServletRequest request, HttpServletResponse response) {
        List<List<String>> rows = new ArrayList<>();
        rows.add(CollUtil.newArrayList( "姓名","身份证号码","考试科目","工作单位","实操结果"));
        List<String> row;
        row = CollUtil.newArrayList("小盛", "361121200104264112", "见证取样检测（房建）（科目全称）","杭州工程检测有限公司（单位全称，去掉分公司后缀词）","合格(填写：合格或不合格)");
        rows.add(row);
        try {
            MyExcel.getExcel(response, rows.get(0).size(),rows,"实操结果导入模板");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @MyLog(value = "导入实操结果")
    @PostMapping("/opZtImport")
    @ApiOperation(value = "导入实操结果信息", notes = "")
    public Object opZtImport(@RequestParam("memberExcel") MultipartFile memberExcel,HttpServletRequest request) throws IOException {


        String token = request.getHeader("token");
        //String kmm = spotcheckServiceImpl.getSpotcheckById(spid).getKmm();
        // pxbm.setKmm(kmm);
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);

        ObjectMapper objectMapper =new ObjectMapper();

        InputStream inputStream = null;

        Map<String, Object> data = new HashMap();

        Map<String, Object> map = new HashMap<String, Object>();
        String msg=null;
        try {

            inputStream =memberExcel.getInputStream();//获取前端传递过来的文件对象，存储在“inputStream”中
            String fileName = memberExcel.getOriginalFilename();//获取文件名

            Workbook workbook =null; //用于存储解析后的Excel文件

            //判断文件扩展名为“.xls还是xlsx的Excel文件”,因为不同扩展名的Excel所用到的解析方法不同
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if(".xls".equals(fileType)){
                workbook= new HSSFWorkbook(inputStream);//HSSFWorkbook专门解析.xls文件
            }else if(".xlsx".equals(fileType)){
                workbook = new XSSFWorkbook(inputStream);//XSSFWorkbook专门解析.xlsx文件
            }

            ArrayList<Pxbm>list =new ArrayList<>();
            ArrayList<Pxbm>fountList =new ArrayList<>();
            ArrayList<Pxbm>spkList =new ArrayList<>();

            Sheet sheet; //工作表
            Row row;      //行
            Cell cell;    //单元格
          // Pxbm pxbm=new Pxbm();
            Pxbm pxbm1=null;
            int count = 0;
            int cgcs=0;
            int sxl=1;

            //循环遍历，获取数据
            for(int i=0;i<workbook.getNumberOfSheets();i++){

                sheet=workbook.getSheetAt(i);//获取sheet
                for(int j=sheet.getFirstRowNum();j<=sheet.getLastRowNum();j++) {//从有数据的第行开始遍历
                    row = sheet.getRow(j);
                    if (row != null && row.getFirstCellNum() != j) { //row.getFirstCellNum()!=j的作用是去除首行，即标题行，如果无标题行可将该条件去掉

                        ArrayList tempList =new ArrayList();

                        Pxbm pxbm=new Pxbm();
                        if ("dqeditor".equals(en.getRole())) {
                            pxbm.setAreaName(en.getAreaName());
                        }
                        pxbm.setXm(row.getCell(0).toString());
                        pxbm.setSfzhm(row.getCell(1).toString());
                        pxbm.setKmm(row.getCell(2).toString());
                        pxbm.setDwm(row.getCell(3).toString());
                        pxbm.setOpZt(row.getCell(4).toString());

                        pxbm1 = pxbmServiceimpl.selectPxbmOpzt(pxbm);
                        if (pxbm1 == null || pxbm1.equals("")) {

                            tempList.add(pxbm.getXm());
                            tempList.add(pxbm.getSfzhm());
                            tempList.add(pxbm.getKmm());
                            list.add(pxbm);
                            sxl=2;
                        }
                        if (sxl==1) {
                            fountList.add(pxbm);
                        }


                        switch (pxbm.getOpZt()) {
                            case "未设":
                                pxbm.setOpZt("0");
                                break;
                            case "合格":
                                pxbm.setOpZt("1");
                                break;
                            case "不合格":
                                pxbm.setOpZt("2");
                                break;

                        }
                    }
                }
            }
            ArrayList<Integer>successCounts =new ArrayList<Integer>();
            if(list.size() == 0){
                for(Pxbm pxbm2 : fountList){
                    int sum = pxbmServiceimpl.updateOpZt(pxbm2);
                    if (sum > 0) {
                        cgcs++;
                        successCounts.add(cgcs);
                    }
                }
                map.put("code", 0);
                map.put("msg", "导入成功！");
                map.put("success", true);
                map.put("successCounts",successCounts.size());
                if(cgcs>0&&sxl==1){
                    return  map;
                }
            }else{
                map.put("list",list);
                map.put("code",-1);
                map.put("msg","导入失败！");

                //JSONObject.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }

        return map;
    }


    @MyLog(value = "下载导入承诺书模板")
    @PostMapping("/isCNSDowlad")
    @ApiOperation(value = "下载Excel模板人员信息", notes = "")
    public void isCNSDowlad(HttpServletRequest request, HttpServletResponse response) {
        List<List<String>> rows = new ArrayList<>();
        rows.add(CollUtil.newArrayList( "地区","姓名","身份证号码","考试科目","工作单位","承诺书"));
        List<String> row;
        row = CollUtil.newArrayList("杭州","小盛", "361121200104262112", "见证取样检测（房建）（科目全称）","杭州工程检测有限公司（单位全称，去掉分公司后缀词）","有(填写：有或无)");
        rows.add(row);
        try {
            MyExcel.getExcel(response, rows.get(0).size(),rows,"承诺书导入模板");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @MyLog(value = "导入承诺书")
    @PostMapping("/isCNSImport")
    @ApiOperation(value = "导入承诺书信息", notes = "")
    public Object isCNSImport(@RequestParam("memberExcel") MultipartFile memberExcel,HttpServletRequest request) throws IOException {


        String token = request.getHeader("token");
        //String kmm = spotcheckServiceImpl.getSpotcheckById(spid).getKmm();
       // pxbm.setKmm(kmm);
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        ObjectMapper objectMapper =new ObjectMapper();

        InputStream inputStream = null;

        Map<String, Object> data = new HashMap();

        Map<String, Object> map = new HashMap<String, Object>();
        String msg=null;
        try {

            inputStream =memberExcel.getInputStream();//获取前端传递过来的文件对象，存储在“inputStream”中
            String fileName = memberExcel.getOriginalFilename();//获取文件名

            Workbook workbook =null; //用于存储解析后的Excel文件

            //判断文件扩展名为“.xls还是xlsx的Excel文件”,因为不同扩展名的Excel所用到的解析方法不同
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if(".xls".equals(fileType)){
                workbook= new HSSFWorkbook(inputStream);//HSSFWorkbook专门解析.xls文件
            }else if(".xlsx".equals(fileType)){
                workbook = new XSSFWorkbook(inputStream);//XSSFWorkbook专门解析.xlsx文件
            }

            ArrayList<Pxbm>list =new ArrayList<>();
            ArrayList<Pxbm>fountList =new ArrayList<>();
            ArrayList<Pxbm>spkList =new ArrayList<>();

            Sheet sheet; //工作表
            Row row;      //行
            Cell cell;    //单元格

            Pxbm pxbm1=null;
            int count = 0;
            int cgcs=0;
            int sxl=1;
            String CNS;
            //循环遍历，获取数据
            for(int i=0;i<workbook.getNumberOfSheets();i++){

                sheet=workbook.getSheetAt(i);//获取sheet
                for(int j=sheet.getFirstRowNum();j<=sheet.getLastRowNum();j++) {//从有数据的第行开始遍历
                    row = sheet.getRow(j);
                    if (row != null && row.getFirstCellNum() != j) { //row.getFirstCellNum()!=j的作用是去除首行，即标题行，如果无标题行可将该条件去掉

                        ArrayList tempList =new ArrayList();

                        Pxbm pxbm=new Pxbm();
                        if ("dqeditor".equals(en.getRole())) {
                            pxbm.setAreaName(en.getAreaName());
                        }
                        pxbm.setXm(row.getCell(1).toString());
                        pxbm.setSfzhm(row.getCell(2).toString());
                        pxbm.setKmm(row.getCell(3).toString());
                        pxbm.setDwm(row.getCell(4).toString());
                        CNS=row.getCell(5).toString();
                        pxbm1 = pxbmServiceimpl.getSelectisCNS(pxbm);
                        if (pxbm1 == null || pxbm1.equals("")) {

                            tempList.add(pxbm.getXm());
                            tempList.add(pxbm.getSfzhm());
                            tempList.add(pxbm.getKmm());
                            list.add(pxbm);
                            sxl=2;
                        }
                        if (sxl==1) {
                            fountList.add(pxbm);
                        }


                        switch (CNS) {
                            case "未设":
                                pxbm.setIsCNS(0);
                                break;
                            case "有":
                                pxbm.setIsCNS(1);
                                break;
                            case "无":
                                pxbm.setIsCNS(2);
                                break;

                        }
                    }
                }
            }
            if(list.size() == 0){
                ArrayList<Integer>successCounts =new ArrayList<Integer>();
                for(Pxbm pxbm2 : fountList){
                    int sum = pxbmServiceimpl.updateIsCNS(pxbm2);
                    if (sum > 0) {
                        cgcs++;
                        successCounts.add(cgcs);
                    }
                }
                map.put("code", 0);
                map.put("msg", "导入成功！");
                map.put("success", true);
                map.put("successCounts",successCounts.size());
                if(cgcs>0&&sxl==1){
                    return  map;
                }
            }else{
                map.put("list",list);
                map.put("code",-1);
                map.put("msg","导入失败！");

                //JSONObject.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }

        return map;
    }

    @MyLog(value = "下载抽查人员导入")
    @PostMapping("/spkMemberDowlad")
    @ApiOperation(value = "下载Excel模板人员信息", notes = "")
    public void spkMemberDowlad(HttpServletRequest request, HttpServletResponse response) {
        List<List<String>> rows = new ArrayList<>();
        rows.add(CollUtil.newArrayList( "姓名","身份证号码","工作单位","考试科目"));
        List<String> row;
        row = CollUtil.newArrayList("小盛", "361121200104262232", "杭州工程检测有限公司（单位全称，去掉分公司后缀词）","见证取样检测（房建）（科目全称）");
        rows.add(row);
        try {
            MyExcel.getExcel(response, rows.get(0).size(),rows,"抽查人员导入模板");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @MyLog(value = "导入抽查人员")
    @PostMapping("/spokeMemberImport")
    @ApiOperation(value = "导入抽查人员", notes = "")
    public Object spokeMemberImport(@RequestParam("memberExcel") MultipartFile memberExcel,HttpServletRequest request,Integer spid) throws IOException {

        Spotcheck spotcheck=spotcheckServiceImpl.getSpotcheckById(spid);
        if(spotcheck.getIsInput()>0) {
            return R.error("该记录已经正式导入，不能导入临时库");
        }


        String token = request.getHeader("token");
        String kmm = spotcheckServiceImpl.getSpotcheckById(spid).getKmm();

        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);

        ObjectMapper objectMapper =new ObjectMapper();

        InputStream inputStream = null;

        String[] sz=null;

        Map<String, Object> data = new HashMap();

        Map<String, Object> map = new HashMap<String, Object>();
        String msg=null;
        try {

            inputStream =memberExcel.getInputStream();//获取前端传递过来的文件对象，存储在“inputStream”中
            String fileName = memberExcel.getOriginalFilename();//获取文件名

            Workbook workbook =null; //用于存储解析后的Excel文件

            //判断文件扩展名为“.xls还是xlsx的Excel文件”,因为不同扩展名的Excel所用到的解析方法不同
            String fileType = fileName.substring(fileName.lastIndexOf("."));
            if(".xls".equals(fileType)){
                workbook= new HSSFWorkbook(inputStream);//HSSFWorkbook专门解析.xls文件
            }else if(".xlsx".equals(fileType)){
                workbook = new XSSFWorkbook(inputStream);//XSSFWorkbook专门解析.xlsx文件
            }

            ArrayList<Pxbm>list =new ArrayList<>();
            ArrayList<Pxbm>fountList =new ArrayList<>();
            ArrayList<Pxbm>spkList =new ArrayList<>();


            List<MemberTemp> memberTempList=new ArrayList<>();

            Sheet sheet; //工作表
            Row row;      //行
            Cell cell;    //单元格

            Pxbm pxbm1=null;
            Pxbm pxbm3=null;
            int count = 0;
            int cgcs=0;
            int spksums=0;
            int sxl=1;
            //String CNS;
            //循环遍历，获取数据
            for(int i=0;i<workbook.getNumberOfSheets();i++){

                sheet=workbook.getSheetAt(i);//获取sheet
                for(int j=sheet.getFirstRowNum();j<=sheet.getLastRowNum();j++) {//从有数据的第行开始遍历
                    row = sheet.getRow(j);
                    if (row != null && row.getFirstCellNum() != j) { //row.getFirstCellNum()!=j的作用是去除首行，即标题行，如果无标题行可将该条件去掉

                        ArrayList tempList =new ArrayList();

                     //   ArrayList tempList1 =new ArrayList();

                        Pxbm pxbm=new Pxbm();
                        MemberTemp memberTemp=new MemberTemp();
                        pxbm.setKmm(kmm);
                        pxbm.setXm(row.getCell(0).toString());
                        pxbm.setSfzhm(row.getCell(1).toString());
                        pxbm.setDwm(row.getCell(2).toString());
                        pxbm.setKmm(row.getCell(3).toString());

                        if ("dqeditor".equals(en.getRole())) {
                            pxbm.setAreaName(en.getAreaName());
                        }
                        pxbm.setSpid(spid);
                        pxbm.setIsfp(1);
                       // CNS=row.getCell(3).toString();
                        pxbm1 = pxbmServiceimpl.getSelectSpokeMembers(pxbm);

                        if (pxbm1 == null || pxbm1.equals("")) {

                            tempList.add(pxbm.getXm());
                            tempList.add(pxbm.getSfzhm());
                            tempList.add(pxbm.getKmm());
                            list.add(pxbm);
                            sxl=2;
                        }else {
                            pxbm3 = pxbmServiceimpl.getPxbmById(pxbm1.getId());
                            if (pxbm3.getSpid()!=null) {
                                spkList.add(pxbm3);
                                sxl=2;
                            }
                        }
                        if (sxl==1) {
                            memberTemp.setAge(pxbm1.getAge2());
                            memberTemp.setDwm(pxbm1.getDwm());
                            memberTemp.setJobtitle(pxbm1.getZc());
                            memberTemp.setKmm(pxbm1.getKmm());
                            memberTemp.setSfzhm(pxbm1.getSfzhm());
                            memberTemp.setJg(Integer.valueOf(pxbm1.getJg()));
                            memberTemp.setSpid(spid);
                            memberTemp.setXb(pxbm1.getXb().equals("男")?0:1);
                            memberTemp.setXm(pxbm1.getXm());
                            memberTemp.setIsCNS(pxbm1.getIsCNS());
                            memberTemp.setIsSB(pxbm1.getIsSB());
                            memberTemp.setPxbmid(pxbm1.getId());
                            memberTempList.add(memberTemp);

                            fountList.add(pxbm);

                        }
                    }
                }
            }

            //针对Excel的数据是否有误，是否已分配进行筛选后的数据进行更新pxbm
            if(list.size() == 0&&spkList.size()==0){

                ArrayList<Integer>successCounts =new ArrayList<Integer>();
                for(Pxbm pxbm2 : fountList){
                    int sum = pxbmServiceimpl.updateSpokeMembers(pxbm2);
                    int spcounts=pxbmServiceimpl.updateIsFp(spid);
                    if (sum > 0) {
                        cgcs++;
                        successCounts.add(cgcs);
                    }
                }

                //去重，针对于Excel表格里面出现重复数据，导致临时表中出现两条一摸一样的数据
                ArrayList<Integer>successCounts2 =new ArrayList<Integer>();


          /*     List<MemberTemp> newList = memberTempList.stream().collect(Collectors
                        .collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(MemberTemp::getSfzhm))),
                                ArrayList::new));
                newList.forEach(System.out::println);
           */
                List<MemberTemp>newList=new ArrayList<>();

                memberTempList.stream().filter(distinctByKey(MemberTemp->MemberTemp.getSfzhm()))
                        .forEach(newList::add);

              /*  List<MemberTemp> newList = new  ArrayList<>();
                for (MemberTemp cd:memberTempList) {

                    if(!newList.contains(cd)){  //主动判断是否包含重复元素

                        newList.add(cd);

                    }

                }
                System.out.println(newList);

               */
                //对最后筛选后的无误数据进行插入到临时表中
               for(MemberTemp memberTemp1 : newList){
                    int spokCount =memberTempService.insertSpokenMembers(memberTemp1);
                    if(spokCount>0){
                        spksums++;
                        successCounts2.add(spksums);
                    }
                }

                map.put("code", 0);
                map.put("msg", "导入成功！");
                map.put("success", true);
                map.put("successCounts",successCounts2.size());
               // map.put("successCounts2",successCounts2.size());
                if(cgcs>0&&sxl==1&&spksums>0){
                    return  map;
                }
            }else{
                if(spkList.size()==0){
                    map.put("list",list);
                    map.put("code",-1);
                    map.put("msg","导入失败，数据有误！");
                }else {
                    map.put("spkList",spkList);
                    map.put("code",-1);
                    map.put("msg","导入失败,存在已分配人员！");
                }
                //JSONObject.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }

        return map;
    }
    static <T>Predicate<T>distinctByKey(Function<? super T,?>keyExtractor){
        Map<Object,Boolean>seen=new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE)==null;
    }
}
