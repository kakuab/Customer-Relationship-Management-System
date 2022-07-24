package com.kakuab.service.impl;

import com.kakuab.mapper.TranRemarkMapper;
import com.kakuab.pojo.TranRemark;
import com.kakuab.service.TranRemarkSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranRemarkServiceImpl implements TranRemarkSerivce {

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public List<TranRemark> selectTranRemarkForDetailByTranId(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }
}
