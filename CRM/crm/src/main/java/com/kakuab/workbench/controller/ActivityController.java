package com.kakuab.workbench.controller;

import com.kakuab.commons.constant.Constants;
import com.kakuab.commons.domain.ReturnObject;
import com.kakuab.commons.utils.DateUtil;
import com.kakuab.commons.utils.ExportFile;
import com.kakuab.commons.utils.HSSFUtils;
import com.kakuab.commons.utils.UUIDUtils;
import com.kakuab.pojo.Activity;
import com.kakuab.pojo.ActivityRemark;
import com.kakuab.pojo.User;
import com.kakuab.service.ActivityRemarkService;
import com.kakuab.service.ActivityService;
import com.kakuab.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service层查询用户
        List<User> userList = userService.selectUser();
        //将数据保存到request作用域中
        request.setAttribute("userList",userList);
        //请求转发到市场活动页面
        return "workbench/activity/index";
    }
    @RequestMapping("/workbench/activity/insertActivity.do")
    @ResponseBody
    public Object insertActivity(HttpSession session, Activity activity){
        //获取登录用户名
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        //封装参数
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtil.formatDateTime(new Date()));
        //根据登录用户名返回ID是谁创建的
        activity.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        //捕获异常
        try {
            //调用service
            int result = activityService.insertActivity(activity);
            if(result>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }

    /**
     * 封装参数
     * @param name
     * @param owner
     * @param startDate
     * @param endDate
     * @param beginNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/workbench/activity/selectActivityByConditionForPage.do")
    @ResponseBody
    public Object selectActivityByConditionForPage(String name,String owner,String startDate,String endDate,int beginNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(beginNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //根据条件分页查询市场活动的列表
        List<Activity> activityList = activityService.selectActivityByConditionForPage(map);
        //根据条件查询市场活动的总条数
        int totalRows = activityService.selectCountOfActivityByCondition(map);
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int result = activityService.deleteActivityByIds(id);
            if (result>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后");
        }
        return returnObject;
    }

    /**
     * 根据主键id查询市场活动
     * @param id
     * @return
     */
    @RequestMapping("/workbench/activity/queryActivityByID.do")
    @ResponseBody
    public Object queryActivityByID(String id){
        Activity activity = activityService.queryActivityByID(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity,HttpSession session,HttpServletRequest request){
        ReturnObject returnObject = new ReturnObject();
        //封装参数
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        activity.setEditTime(DateUtil.formatDateTime(new Date()));
        activity.setEditBy(user.getId());
        try {
            int result=activityService.updateActivityById(activity);
            if (result>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws Exception {
        OutputStream out=null;
        HSSFWorkbook workbook=new HSSFWorkbook();
        //调用service，查询所有市场活动
        List<Activity> activityList=activityService.selectAllActivities();
        //创建exel文件并且将activityList写入到xsl文件中
        ExportFile exportFile = new ExportFile();
        exportFile.exportActivityFile(activityList,workbook);
        response.setContentType("application/octet-stream;UTF-8");
        response.addHeader("Content-Dispositon","attacment;filename=Activity.xls");
        out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/selectExporActivitytById.do")
    public void selectExporActivitytById(String[] id,HttpServletResponse response) throws Exception{
        /*String id=request.getParameter("id");*/
        OutputStream out=null;
        HSSFWorkbook workbook=new HSSFWorkbook();
        //调用service，查询所有市场活动
        List<Activity> activityList=activityService.selectExportActivityById(id);
        //创建exel文件并且将activityList写入到xsl文件中
        ExportFile exportFile = new ExportFile();
        exportFile.exportActivityFile(activityList,workbook);
        response.setContentType("application/octet-stream;UTF-8");
        response.addHeader("Content-Dispositon","attacment;filename=Activity.xls");
        out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/importActivityByList.do")
    @ResponseBody
    public Object importActivityByList(MultipartFile activityFile,HttpSession session){
        User user=(User)session.getAttribute(Constants.SESSION_USER);//获取登录用户
        ReturnObject returnObject = new ReturnObject();
        try{
            //把exel文件写入到磁盘仲
            /*String originalFilename=activityFile.getOriginalFilename();
            File file=new File("C:\\Users\\17166\\Downloads\\",System.currentTimeMillis()+originalFilename);
            activityFile.transferTo(file);

            //解析文件，获取文件中的数据，并且封装成activityList
            //根据execl文件生成HSSFWorkBook对象，封装了文件的所有信息
            InputStream is =new FileInputStream("C:\\Users\\17166\\Downloads\\"+originalFilename);*/
            //直接从内存中读数据，不需要再生成文件之后再读文件
            InputStream is=activityFile.getInputStream();
            HSSFWorkbook workbook=new HSSFWorkbook(is);
            //获取页
            HSSFSheet sheetAt = workbook.getSheetAt(0);
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity=null;
            List<Activity> activityList = new ArrayList<>();
            //根据页获取最后一行的下标
            for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
                row=sheetAt.getRow(i);
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtil.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j=0;j<row.getLastCellNum();j++){
                    //根据row获取HSSFCell对象，封装了一系列的所有信息
                    cell=row.getCell(j);
                    //获取列中的数据
                    String cellValue= HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }
                //封装list
                activityList.add(activity);
            }

            //调用service
            int count=activityService.importActivityByList(activityList);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(count);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detail.do")
    public String detail(String id,HttpServletRequest request){
        Activity activity=activityService.selectActivityForDetailByid(id);
        List<ActivityRemark> remarkList=activityRemarkService.selectActivityRemarkForDetailByActivityId(id);
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        return "workbench/activity/detail";
    }
}
