package com.jianzhi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.jianzhi.model.Pxbm;
import com.jianzhi.model.SpotcheckMember;
import com.jianzhi.service.impl.PxbmServiceImpl;
import com.jianzhi.service.impl.SpotcheckMemberServiceImpl;
import com.sheng.service.PxbmService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.common.component.WebSocket;
import com.jianzhi.model.Enterprise;
import com.jianzhi.model.Spotcheck;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.service.RedisService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.util.R;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/spotcheck")
public class SpotcheckController {

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

    @MyLog(value = "查询当前抽查记录")  //这里添加了AOP的自定义注解
    @PostMapping("/list")
    @ApiOperation(value = "根据条件查询抽查记录", notes = "")
    public Object getSpotcheckList(HttpServletRequest request, Spotcheck spotcheck,
                                   @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                                   @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        if ("dqeditor".equals(en.getRole())) {
            spotcheck.setAreaName(en.getAreaName());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Spotcheck> list = SpotcheckServiceImpl.getSpotcheckList(spotcheck);
        PageInfo<Spotcheck> pageInfo = new PageInfo<Spotcheck>(list);
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

    @MyLog(value = "添加新抽查记录")  //这里添加了AOP的自定义注解
    @PostMapping("/add")
    @ApiOperation(value = "添加新的抽查记录", notes = "")
    public Object AddSpotcheck(HttpServletRequest request, Spotcheck spotcheck) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise en = enterpriseServiceImpl.getEnterprise(entid);
        if ("dqeditor".equals(en.getRole())) {
            spotcheck.setAreaName(en.getAreaName());
        }
        if (SpotcheckServiceImpl.addSpotcheck(spotcheck) > 0) {
            return R.ok();
        }
        return R.error(1, "未知原因添加失败，请联系管理员");
    }

    @MyLog(value = "修改抽查记录")  //这里添加了AOP的自定义注解
    @PutMapping("/modify")
    @ApiOperation(value = "修改抽查记录", notes = "")
    public Object modifySpotcheck(Spotcheck spotcheck) {
        if (SpotcheckServiceImpl.modifySpotcheck(spotcheck) > 0) {
            return R.ok();
        }
        return R.error(1, "未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "删除抽查记录")  //这里添加了AOP的自定义注解
    @PostMapping("/delete")
    @ApiOperation(value = "删除抽查记录", notes = "")
    public Object deleteSpotcheck(Integer id) {
        Spotcheck spotcheck = SpotcheckServiceImpl.getSpotcheckById(id);
        if (spotcheck.getIsDqenter() == 1) {
            return R.error("地区已确认不能删除");
        }
        if (SpotcheckServiceImpl.deleteSpotcheck(id) > 0) {
            return R.ok();
        }
        return R.error("未知原因修改失败，请联系管理员");
    }


    @MyLog(value = "查询当前登录单位的抽查任务")  //这里添加了AOP的自定义注解
    @PostMapping("/enterplist")
    @ApiOperation(value = "查询当前登录单位的抽查任务", notes = "")
    public Object getSpotcheckEnterpriseList(HttpServletRequest request, Spotcheck spotcheck,
                                             @RequestParam(defaultValue = "1", value = "pageNumber") Integer pageNum,
                                             @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        PageHelper.startPage(pageNum, pageSize);
        List<Spotcheck> list = SpotcheckServiceImpl.getSpotcheckListByEntid(entid, spotcheck);
        PageInfo<Spotcheck> pageInfo = new PageInfo<Spotcheck>(list);
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

    @MyLog(value = "地区确认")  //这里添加了AOP的自定义注解
    @PostMapping("/dqenter")
    @ApiOperation(value = "地区确认", notes = "")
    public Object modifyDqEnter(HttpServletRequest request, @RequestParam("idlist") List<Integer> idlist) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        String lgname = enterpriseServiceImpl.getEnterprise(entid).getName();
        System.out.println("开始调用同步数据的接口！");
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(PxbmService.class);
        factory.setAddress("http://202.75.213.91/SSMCXF/webservice/pxbm?wsdl");
        // 需要服务接口文件
        PxbmService client = (PxbmService) factory.create();

        for (Integer id : idlist) {
            System.out.println("获取到Spid"+id);
            List<SpotcheckMember> list = spotcheckMemberService.getPxbmIdr_jgCheckstatus(id);
            System.out.println("通过spid查询到的该计划总人数："+list.size());
            List<Pxbm> pxbms = pxbmService.selectPxbmInformations(id);
            System.out.println("通过spid查询到的op_zt字段总人数："+pxbms.size());
            for (SpotcheckMember item : list) {
                System.out.println("获取到的Pxbmid:"+item.getPxbmid());
                try {
                    int count1=client.selectPxbmId(item.getPxbmid());
                    System.out.println("将pxbmID传到167之后匹配到的结果返回：" + count1);
                    if (count1==0) {
                        System.out.println("数据信息缺失，同步失败！");
                        return R.error("数据信息有误，同步失败！");
                    }
                    if(item.getCheckstatus()!=1){
                        item.setCheckstatus(0);
                    }
                    int count2=client.updatePxbm(item.getJg().toString(),item.getCheckstatus().toString(),item.getSpid().toString(),"0",item.getPxbmid());
                    System.out.println("同步到167上的受影响行数返回(除更新承诺书字段外):" +count2);
                    if(count2>0){
                        System.out.println("OK");
                    }else {
                        return R.error("同步异常，请联系管理员！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            for (Pxbm pxbm : pxbms) {
                System.out.println("这里是根据spid获取到的pxbm表里面的承诺书id:"+pxbm.getIsCNS());
                System.out.println("这里是根据spid查询到的pxbm表里面的id:"+pxbm.getId());
                try {
                    int count3=client.updatePxbmopZt(pxbm.getIsCNS().toString(),pxbm.getId());
                    System.out.println("同步到167上的承诺书,返回受影响行数:"+count3);
                    if(count3>0){
                        System.out.println("OK");
                    }else {
                        return R.error("同步异常，请联系管理员！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (SpotcheckServiceImpl.modifyDqenter(idlist) > 0) {
            for (Integer id : idlist) {
                webSocket.sendAllMessage("用户名：" + lgname
                        + "确认了" + SpotcheckServiceImpl.getSpotcheckById(id).getName());
            }

            return R.ok("数据同步成功！");
        }
        return R.error("未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "省协会确认")  //这里添加了AOP的自定义注解
    @PostMapping("/xhenter")
    @ApiOperation(value = "省协会确认", notes = "")
    public R modifyXhEnter(@RequestParam("idlist") List<Integer> idlist) {
       for (Integer id : idlist) {
            int dqqr = SpotcheckServiceImpl.getSpotcheckById(id).getIsDqenter();

            if (dqqr != 1) {
                return R.error("地区未确认，无法点击");
            }
        }
          if (SpotcheckServiceImpl.modifyXhEnter(idlist) > 0) {
              //System.out.println("数据同步成功！");
            //return R.ok("数据同步成功！");
            return R.ok();
        }
        return R.error("未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "查询未确认记录")  //这里添加了AOP的自定义注解
    @PostMapping("/notEnt")
    @ApiOperation(value = "查询未确认记录", notes = "")
    public Object getNumNotEnter(HttpServletRequest request) {
        String token = request.getHeader("token");
        String entid = (String) redisService.get(token);
        Enterprise ent = enterpriseServiceImpl.getEnterprise(entid);
        Map<String, Object> map = new HashMap<>();
        Spotcheck sp = new Spotcheck();
        if ("admin".equals(ent.getRole())) {
            sp.setIsDqenter(1);
            sp.setIsXhenter(0);
            List<Spotcheck> lt = SpotcheckServiceImpl.getSpotcheckList(sp);
            map.put("neednum", lt.size());
            return R.ok(map);
        }
        return R.error("未知原因修改失败，请联系管理员");
    }

    @MyLog(value = "设置记录为已读")  //这里添加了AOP的自定义注解
    @PostMapping("/read")
    @ApiOperation(value = "设置记录为已读", notes = "")
    public R setIsRead(Integer id) {
        if (SpotcheckServiceImpl.setIsRead(id) > 0) {
            return R.ok();
        }
        return R.error("状态设置不成功");
    }


}
