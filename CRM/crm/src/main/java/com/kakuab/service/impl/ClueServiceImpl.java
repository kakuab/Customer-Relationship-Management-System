package com.kakuab.service.impl;

import com.kakuab.commons.constant.Constants;
import com.kakuab.commons.utils.DateUtil;
import com.kakuab.commons.utils.UUIDUtils;
import com.kakuab.mapper.*;
import com.kakuab.pojo.*;
import com.kakuab.service.CLueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements CLueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public int inserClue(Clue clue) {
        return clueMapper.inserClue(clue);
    }

    @Override
    public Clue selectClueForDetailByid(String id) {
        return clueMapper.selectClueForDateilByid(id);
    }


    @Override
    public List<Clue> selectAllClue() {
        return clueMapper.selectAllClue();
    }

    @Override
    public void saveConvert(Map<String, Object> map) {
        String clueId=(String) map.get("clueId");
        String isCreateTran=(String)map.get("isCreateTran");
        //根据id查询线索的信息
        User user =(User) map.get(Constants.SESSION_USER);
        Clue clue = clueMapper.selectClueByid(clueId);
        Customer c = new Customer();
        c.setAddress(clue.getAddress());
        c.setContactSummary(clue.getContactSummary());
        c.setCreateBy(user.getId());
        c.setDescription(clue.getDescription());
        c.setCreateTime(clue.getCreateTime());
        c.setId(UUIDUtils.getUUID());
        c.setName(clue.getCompany());
        c.setNextContactTime(clue.getNextContactTime());
        c.setOwner(user.getId());
        c.setPhone(clue.getPhone());
        c.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(c);
        //把该线索中有关个人信息转换到联系人中
        Contacts co = new Contacts();
        co.setAddress(clue.getAddress());
        co.setAppellation(clue.getAppellation());
        co.setContactSummary(clue.getContactSummary());
        co.setCreateBy(clue.getCreateBy());
        co.setCreateTime(clue.getCreateTime());
        co.setCustomerId(c.getId());
        co.setDescription(clue.getDescription());
        co.setEmail(clue.getEmail());
        co.setFullname(clue.getFullname());
        co.setId(UUIDUtils.getUUID());
        co.setJob(clue.getJob());
        co.setMphone(clue.getMphone());
        co.setNextContactTime(clue.getNextContactTime());
        co.setOwner(user.getId());
        co.setSource(clue.getSource());
        contactsMapper.insertContacts(co);

        //根据clueId查询该线索下的所有信息
        List<ClueRemark> clueRemarkList=clueRemarkMapper.selectClueRemarkById(clueId);
        List<CustomerRemark> customerRemarkList=new ArrayList<>();
        List<ContactsRemark> contactsRemarkList=new ArrayList<>();
        ContactsRemark cor=null;
        if (clueRemarkList!=null&&clueRemarkList.size()>0){
            CustomerRemark cur = null;
            for (ClueRemark cr:clueRemarkList){
                cur = new CustomerRemark();
                cur.setCreateBy(cr.getCreateBy());
                cur.setCreateTime(cr.getCreateTime());
                cur.setCustomerId(c.getId());
                cur.setEditBy(cr.getEditBy());
                cur.setEditTime(cr.getEditTime());
                cur.setEditFlag(cr.getEditFlag());
                cur.setId(UUIDUtils.getUUID());
                cur.setNoteContent(cr.getNoteContent());
                customerRemarkList.add(cur);

                cor = new ContactsRemark();
                cor.setContactsId(co.getId());
                cor.setCreateBy(cr.getCreateBy());
                cor.setCreateTime(cr.getCreateTime());
                cor.setEditBy(cr.getEditBy());
                cor.setEditFlag(cr.getEditFlag());
                cor.setEditTime(cr.getEditTime());
                cor.setId(UUIDUtils.getUUID());
                cor.setNoteContent(cr.getNoteContent());
                contactsRemarkList.add(cor);
            }
            //调用service保存备注信息
            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
            contactsRemarkMapper.insertContactRemarkByList(contactsRemarkList);
        }
        List<ClueActivityRelation> relationList = clueActivityRelationMapper.selectClueActivityByClueId(clueId);
        List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
        if (relationList!=null&&relationList.size()>0){
            ContactsActivityRelation coat = null;
            for (ClueActivityRelation car:relationList){
                coat = new ContactsActivityRelation();
                coat.setActivityId(car.getActivityId());
                coat.setContactsId(co.getId());
                coat.setId(UUIDUtils.getUUID());
                contactsActivityRelationList.add(coat);
            }
            contactsActivityRelationMapper.insertContactsActivity(contactsActivityRelationList);
        }

        if ("true".equals(isCreateTran)){
            Tran tran = new Tran();
            tran.setActivityId((String)map.get("activityId"));
            tran.setContactsId(co.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtil.formatDateTime(new Date()));
            tran.setCustomerId(c.getId());
            tran.setExpectedDate((String)map.get("expectedDate"));
            tran.setId(UUIDUtils.getUUID());
            tran.setMoney((String)map.get("money"));
            tran.setName((String)map.get("name"));
            tran.setOwner(user.getId());
            tran.setStage((String)map.get("stage"));
            tranMapper.insertTran(tran);
            if (clueRemarkList!=null&&clueRemarkList.size()>0){
                TranRemark tranRemark = null;
                List<TranRemark> tranRemarkList=new ArrayList<>();
                for (ClueRemark cr:clueRemarkList){
                    tranRemark=new TranRemark();
                    tranRemark.setCreateBy(cr.getCreateBy());
                    tranRemark.setCreateTime(cr.getCreateTime());
                    tranRemark.setEditBy(cr.getEditBy());
                    tranRemark.setEditFlag(cr.getEditFlag());
                    tranRemark.setEditTime(cr.getEditTime());
                    tranRemark.setTranId(tran.getId());
                    tranRemark.setNoteContent(cr.getNoteContent());
                    tranRemark.setId(UUIDUtils.getUUID());
                    tranRemarkList.add(tranRemark);
                }
                tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
            }
        }
        clueRemarkMapper.deleteClueRemarkById(clueId);
        clueActivityRelationMapper.delteClueActivityRelationByClueId(clueId);
        clueMapper.deleteClueById(clueId);
    }
}
