package com.kakuab.workbench.controller;

import com.kakuab.commons.constant.Constants;
import com.kakuab.commons.domain.ReturnObject;
import com.kakuab.commons.utils.DateUtil;
import com.kakuab.commons.utils.UUIDUtils;
import com.kakuab.pojo.*;
import com.kakuab.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private CLueService cLueService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @RequestMapping("/workbench/clue/clueIndex.do")
    public String clueIndex(HttpServletRequest request){
        List<User> userList=userService.selectUser();
        List<DicValue> appellationList=dicValueService.selectDicValueByTypeCode("appellation");
        List<DicValue> clueStateList=dicValueService.selectDicValueByTypeCode("clueState");
        List<DicValue> sourceList=dicValueService.selectDicValueByTypeCode("source");
        List<Clue> clueList=cLueService.selectAllClue();
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("clueList",clueList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user=(User)session.getAttribute(Constants.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtil.formatDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        //调用service层
        try {
            int result=cLueService.inserClue(clue);
            if (result>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/selectClueForDetailByid.do")
    public String selectClueForDetailByid(String id,HttpServletRequest request){
        //调用service层
        Clue clue=cLueService.selectClueForDetailByid(id);
        List<Activity>activityList=activityService.selectActivityByCLueId(id);
        List<ClueRemark> clueRemarkList=clueRemarkService.selectClueRemarkForDetailByClueId(id);
        request.setAttribute("clue",clue);
        request.setAttribute("activityList",activityList);
        request.setAttribute("clueRemarkList",clueRemarkList);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/selectActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object selectActivityForDetailByNameClueId(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层
        List<Activity> activityList=activityService.selectActivityForDetailByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId){
        //封装参数
        ClueActivityRelation relation = null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        ReturnObject returnObject = new ReturnObject();
        for(String ai:activityId){
            relation = new ClueActivityRelation();
            relation.setActivityId(ai);
            relation.setClueId(clueId);
            relation.setId(UUIDUtils.getUUID());
            relationList.add(relation);
        }
        //调用service层
        try {
            int result=clueActivityRelationService.insertClueActivityRelationByList(relationList);
            if (result>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList=activityService.selectAicivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/relieveActivityClueRelation.do")
    @ResponseBody
    public Object relieveActivityClueRelation(ClueActivityRelation clueActivityRelation){
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service，解除市场活动和线索的管理关系
            int result=clueActivityRelationService.deleteClueActivityRelationByActivityIdClueId(clueActivityRelation);
            if (result>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后...");
            }
        } catch (Exception e) {
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
            e.printStackTrace();
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        //调用service，查询线索
        Clue clue = cLueService.selectClueForDetailByid(id);
        List<DicValue> dicValueList = dicValueService.selectDicValueByTypeCode("stage");
        //存入作用域中
        request.setAttribute("clue",clue);
        request.setAttribute("dicValueList",dicValueList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/converClueChange.do")
    @ResponseBody
    public Object converClueChange(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        Map<String,Object> map=new HashMap<>();
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        ReturnObject returnObject = new ReturnObject();
        try {
            cLueService.saveConvert(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/clue/selectActivityForDetailByNameClueIdIn.do")
    @ResponseBody
    public Object selectActivityForDetailByNameClueIdIn(String activityName,String clueId){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层
        List<Activity> activityList=activityService.selectActivityForDetailByNameClueIdIn(map);
        return activityList;
    }
}
