package com.kakuab.workbench.controller;

import com.kakuab.commons.constant.Constants;
import com.kakuab.commons.domain.ReturnObject;
import com.kakuab.pojo.*;
import com.kakuab.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private TranService tranService;

    @Autowired
    private UserService userService;

    @Autowired
    private TranRemarkSerivce tranRemarkSerivce;

    @Autowired
    private TranHistoryService tranHistoryService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/workbench/transaction/tranIndex.do")
    public String tranIndex(HttpServletRequest request){
        List<DicValue> transactionTypeList = dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        List<Tran> tranList = tranService.selectAllTransaction();
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("tranList",tranList);
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        List<DicValue> transactionTypeList = dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");
        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        List<User> userList = userService.selectUser();
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("userList",userList);
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        //根据资源绑定器解析文件
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        return possibility;
    }
    @RequestMapping("/workbench/transaction/selectCusntomerName.do")
    @ResponseBody
    public Object selectCusntomerName(String customerName){
        List<String> customerNameList = customerService.selectCustomerNameByName(customerName);
        return customerNameList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.saveCreateTran(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/toDetailTran.do")
    public String toDetailTran(String id,HttpServletRequest request){
        //调用service查询信息
        Tran tran = tranService.selectTranById(id);
        List<TranRemark> tranRemarkList = tranRemarkSerivce.selectTranRemarkForDetailByTranId(id);
        List<TranHistory> tranHistories = tranHistoryService.selectTranHistoryByDetailByTranId(id);
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString(tran.getStage());
        tran.setPossiblity(possibility);
        request.setAttribute("tran",tran);
        request.setAttribute("tranRemarkList",tranRemarkList);
        request.setAttribute("tranHistories",tranHistories);

        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/detail";
    }
}
