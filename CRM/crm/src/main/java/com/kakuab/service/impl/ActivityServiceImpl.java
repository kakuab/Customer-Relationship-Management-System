package com.kakuab.service.impl;

import com.kakuab.mapper.ActivityMapper;
import com.kakuab.pojo.Activity;
import com.kakuab.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int insertActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> selectActivityByConditionForPage(Map<String,Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public Activity selectActivityForDetailByid(String id) {
        return activityMapper.selectActivityForDetailByid(id);
    }

    @Override
    public List<Activity> selectActivityForDetailByNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameClueId(map);
    }

    @Override
    public List<Activity> selectActivityByCLueId(String clueId) {
        return activityMapper.selectActivityByCLueId(clueId);
    }

    @Override
    public int selectCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] id) {
        return activityMapper.deleteActivityByIds(id);
    }

    @Override
    public Activity queryActivityByID(String id) {
        return activityMapper.queryActivityByID(id);
    }

    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }

    @Override
    public List<Activity> selectAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public List<Activity> selectExportActivityById(String[] id) {
        return activityMapper.selectExportActivityById(id);
    }

    @Override
    public List<Activity> selectAicivityForDetailByIds(String[] ids) {
        return activityMapper.selectAicivityForDetailByIds(ids);
    }

    @Override
    public List<Activity> selectActivityForDetailByNameClueIdIn(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByNameClueIdIn(map);
    }

    @Override
    public int importActivityByList(List<Activity> activityList) {
        return activityMapper.importActivityByList(activityList);
    }
}
