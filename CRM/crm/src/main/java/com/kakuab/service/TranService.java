package com.kakuab.service;

import com.kakuab.pojo.FunnelVO;
import com.kakuab.pojo.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<Tran> selectAllTransaction();


    void saveCreateTran(Map<String,Object> map);

    Tran selectTranById(String id);

    List<FunnelVO> selectCountOfTranGroupByStage();
}
