package com.kakuab.service;

import com.kakuab.pojo.ActivityRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActivityRemarkService {

    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityId);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int updateActivityRemarkById(ActivityRemark activityRemark);
}
