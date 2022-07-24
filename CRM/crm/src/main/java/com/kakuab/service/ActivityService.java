package com.kakuab.service;

import com.kakuab.pojo.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int insertActivity(Activity activity);

    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    int selectCountOfActivityByCondition(Map<String,Object> map);

    int deleteActivityByIds(String[] id);

    Activity queryActivityByID(String id);

    int updateActivityById(Activity activity);

    List<Activity> selectAllActivities();

    List<Activity> selectExportActivityById(String[] id);

    int importActivityByList(List<Activity> activityList);

    Activity selectActivityForDetailByid(String id);

    List<Activity> selectActivityByCLueId(String clueId);

    List<Activity> selectActivityForDetailByNameClueId(Map<String,Object> map);

    List<Activity> selectAicivityForDetailByIds(String[] ids);

    List<Activity> selectActivityForDetailByNameClueIdIn(Map<String,Object> map);
}


