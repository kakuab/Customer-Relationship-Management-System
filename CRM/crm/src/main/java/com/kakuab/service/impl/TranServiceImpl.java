package com.kakuab.service.impl;

import com.kakuab.commons.constant.Constants;
import com.kakuab.commons.utils.DateUtil;
import com.kakuab.commons.utils.UUIDUtils;
import com.kakuab.mapper.CustomerMapper;
import com.kakuab.mapper.TranHistoryMapper;
import com.kakuab.mapper.TranMapper;
import com.kakuab.pojo.*;
import com.kakuab.service.CustomerService;
import com.kakuab.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<Tran> selectAllTransaction() {
        return tranMapper.selectAllTransaction();
    }

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        String customerName=(String)map.get("customerName");
        User user=(User)map.get(Constants.SESSION_USER);
        Customer customer = customerMapper.selectCustomerByName(customerName);
        //如果customer为空，则创建customer
        if (customer==null){
            customer=new Customer();
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtil.formatDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomer(customer);
        }
        Tran tran = new Tran();
        tran.setStage((String) map.get("stage"));
        tran.setSource((String) map.get("source"));
        tran.setOwner(user.getId());
        tran.setName(customerName);
        tran.setId(UUIDUtils.getUUID());
        tran.setMoney((String) map.get("money"));
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DateUtil.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setType((String) map.get("type"));
        tranMapper.insertTran(tran);

        //保存交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtil.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public Tran selectTranById(String id) {
        return tranMapper.selectTranById(id);
    }

    @Override
    public List<FunnelVO> selectCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
