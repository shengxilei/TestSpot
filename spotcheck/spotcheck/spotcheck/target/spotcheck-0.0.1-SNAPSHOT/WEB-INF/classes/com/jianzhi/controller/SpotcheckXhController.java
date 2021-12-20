package com.jianzhi.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.common.component.WebSocket;
import com.jianzhi.model.*;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.service.RedisService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.service.impl.PxbmServiceImpl;
import com.jianzhi.service.impl.SpotcheckMemberServiceImpl;
import com.jianzhi.util.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@RequestMapping("/spotjd")
public class SpotcheckXhController {

    @Resource
    SpotcheckService SpotcheckServiceImpl;

    @Resource
    EnterpriseService enterpriseServiceImpl;

    @Autowired
    private RedisService redisService;

    @Autowired
    WebSocket webSocket;

    @Resource
    SpotcheckMemberServiceImpl spotcheckMemberService;

    @Resource
    PxbmServiceImpl pxbmService;



    @MyLog("添加新的协会监督计划")
    @PostMapping("/addJdSpot")
    @ApiOperation(value = "添加新的监督计划",notes = "")
    public R addJdSpot(HttpServletRequest request, Spotcheck spotcheck){
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise en=enterpriseServiceImpl.getEnterprise(entid);
        List<SpotcheckMember>list=null;
        if ("dqeditor".equals(en.getRole())) {
            spotcheck.setAreaName(en.getAreaName());
        }
        if(spotcheck.getXhjd()==1){
            list=spotcheckMemberService.selectIsCheckStatusSpotmember(spotcheck);

        }
        else
        {
            list=spotcheckMemberService.selectNotCheckStatusSpotmember(spotcheck);

        }
        if(SpotcheckServiceImpl.addSpotcheck(spotcheck)>0){
            Pxbm pxbm=new Pxbm();
            int jhspid=spotcheck.getId();
            System.out.println(spotcheck.getId());

            for(SpotcheckMember spotcheckMember : list){
                //修改原来计划人员的xhjd
               // System.out.println(spotcheckMember.getSpid());
                spotcheckMember.setXhjd(spotcheck.getXhjd());
               // setIds(spotcheckMember.getSpid());
               // deleteSpidList.add(getIds());
                spotcheckMemberService.updateSpmXhjd(spotcheckMember);
                //SpotcheckServiceImpl.updateSpotcheckXhjd(spotcheckMember);
                SpotcheckServiceImpl.insertSpid(spotcheckMember.getId(),spotcheck.getId());

                //插入正式表
                spotcheckMember.setSpid(spotcheck.getId());
                spotcheckMember.setJg(0);
                spotcheckMember.setP_bj(0);
               // pxbm.setId(spotcheckMember.getPxbmid());
               // pxbm.setSpid(spotcheck.getId());
                //System.out.println(spotcheckMember.getSpid());
                int count=spotcheckMemberService.addspotMember(spotcheckMember);

               // int coutspid=pxbmService.update_jdfpspid(pxbm);
                if(count<=0){
                    return R.error(-1,"未知原因，错误范围在人员数据中，请联系管理员!");
                }
            }
            //
            int num=spotcheckMemberService.xhJdAlgorithmTime(jhspid);
            if(num>0){
                return R.ok().put("num", num).put("msg", ",计划创建成功，且人员信息已导入！");
            }
        }
        return R.error(-1,"未知原因添加监督抽查计划失败，请联系管理员！");
    }

    @MyLog("查询当前监督抽查记录")
    @PostMapping("/jdlist")
    @ApiOperation(value = "根据条件查询抽查记录",notes = "")
    public Object jdList(HttpServletRequest request, Spotcheck spotcheck,
                         @RequestParam(defaultValue = "1",value = "pageNumber") Integer pageNum,
                         @RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        if ("dqeditor".equals(en.getRole())) {
            spotcheck.setAreaName(en.getAreaName());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<Spotcheck>list=SpotcheckServiceImpl.selectXhJdSpotcheckByDetail(spotcheck);
        PageInfo<Spotcheck>pageInfo=new PageInfo<>(list);
        Map<String,Object>map=new HashMap<>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    }

    @MyLog(value = "修改抽查记录")  //这里添加了AOP的自定义注解
    @PostMapping("/updateSpotcheck")
    @ApiOperation(value = "修改抽查记录", notes = "")
    public Object updateJdSpotcheck(Spotcheck spotcheck){
        if (SpotcheckServiceImpl.modifySpotcheck(spotcheck) > 0) {
            return R.ok();
        }
        return R.error(1, "未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "删除抽查记录")  //这里添加了AOP的自定义注解
    @PostMapping("/delete")
    @ApiOperation(value = "删除抽查记录", notes = "")
    public Object deleteJdSpotcheck(Integer id) {
        Spotcheck spotcheck = SpotcheckServiceImpl.getSpotcheckById(id);
        if (spotcheck.getIsDqenter() == 1) {
            return R.error("地区已确认不能删除");
        }
       // int count=0;
        int count2=0;
        List<Integer>list=new ArrayList<>();
        list=SpotcheckServiceImpl.getselectSpid(id);
      /*  Set set=new HashSet();
        List<Integer>newList=new ArrayList<>();
        set.addAll(list);
        newList.addAll(set);

       */
        for (Integer iten : list){
            System.out.println(iten);
           // count=SpotcheckServiceImpl.deleteXhspotecheck(iten);
            count2=spotcheckMemberService.deleteXhSpotmember(iten);
            if (count2>0){
                count2++;
            }else {
                count2=0;
            }
        }
        if (SpotcheckServiceImpl.deleteXhSpotcheck(id) > 0 && count2!=0) {
            return R.ok();
        }
        return R.error("未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "点击抽查") // 这里添加了AOP的自定义注解
    @PostMapping("/jdspot")
    @ApiOperation(value = "点击抽查，抽中与未抽中", notes = "")
    public Object Jdspotcheck(String id, String spid) {
        Map<String, Object> map = new HashMap<String, Object>();
        int status = spotcheckMemberService.spot(id, spid);
        if (status == 4) {
            return R.error("抽查未启动，请联系管理员");
        }
        map.put("status", status);
        return R.ok().put("spotjg", map);
    }

    @MyLog("查询抽验考核及格且抽中或未抽中人员")
    @PostMapping("getJdSpomember")
    @ApiOperation(value = "参与抽验考核及格且抽中或未抽中人员",notes = "")
    public Object getJdSpomember(SpotcheckMember spotcheckMember,
                                 @RequestParam(defaultValue = "1",value = "pageNumber") Integer pageNum,
                                 @RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<SpotcheckMember> list = spotcheckMemberService.getSpotcheckMemberList(spotcheckMember);
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

    @MyLog(value = "修改人员抽查状态--附加功能") // 这里添加了AOP的自定义注解
    @PostMapping("/jdmodify")
    @ApiOperation(value = "修改抽查人员的是否被抽中", notes = "")
    public Object JdmodifySpotcheck(SpotcheckMember spotcheckMember, String editor) {
        SpotcheckMember mem = spotcheckMemberService.getSpotcheckMemberById(spotcheckMember.getId());
        if (mem != null) {
            Spotcheck sp = SpotcheckServiceImpl.getSpotcheckById(mem.getSpid());
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
                    if (spotcheckMemberService.modifySpotcheckMember(spotcheckMember, editor) > 0) {
                        return R.ok("修改成功");
                    }
                } else {
                    return R.error("该记录正在抽查中,无法修改");
                }
            }
        }
        return R.error("未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "修改人员抽查状态--指定是否抽中") // 这里添加了AOP的自定义注解
    @PostMapping("/JdmodifySpotcheckZd")
    @ApiOperation(value = "指定修改抽查人员的是否被抽中", notes = "")
    public Object JdmodifySpotcheckZd(SpotcheckMember spotcheckMember, String editor) {
        SpotcheckMember mem = spotcheckMemberService.getSpotcheckMemberById(spotcheckMember.getId());
        if (mem != null) {
            Spotcheck sp = SpotcheckServiceImpl.getSpotcheckById(mem.getSpid());
            Calendar newTime = Calendar.getInstance();
            newTime.setTime(sp.getStart());
            newTime.add(Calendar.MINUTE, -2);//开始时间减2分钟
            Date dt1 = newTime.getTime();
            Date now = new Date();

            if (now.after(dt1) && now.before(sp.getStart())) {
                return R.error("离抽查开始只剩2分钟，关闭指定是否抽中功能");
            }
            if (sp != null) {
                if (sp.getSpotstatus() != 1) {
              /*  if (mem.getCheckstatus() == 1) {
                    return R.error("改人员已被抽中无法修改");
                }
               */
                    if(mem.getXhjd()==1){
                        if (spotcheckMemberService.updateCheckStusIsCZ(spotcheckMember) > 0) {
                            return R.ok("修改成功");
                        }
                    }else {
                        if (spotcheckMemberService.updateCheckStusNotWCZ(spotcheckMember) > 0) {
                            return R.ok("修改成功");
                        }
                    }

                } else {
                    return R.error("该记录正在抽查中,无法修改");
                }
            }
        }
        return R.error("未知原因修改失败，请联系管理员");
    }

  /*  @MyLog(value = "查询记录中的具体人员") // 这里添加了AOP的自定义注解
    @PostMapping("/jdlist")
    @ApiOperation(value = "查询该记录正式导入的人员", notes = "")
    public Object getJdSpotmeberLise(SpotcheckMember spotcheckMember, @RequestParam(defaultValue = "1",value = "pageNumber") Integer pageNum,
                                     @RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<SpotcheckMember> list = spotcheckMemberService.getSpotcheckMemberList(spotcheckMember);
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

   */



}
