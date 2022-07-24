package com.kakuab.service.impl;

import com.kakuab.mapper.TranHistoryMapper;
import com.kakuab.mapper.TranRemarkMapper;
import com.kakuab.pojo.TranHistory;
import com.kakuab.service.TranHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranHistoryServiceImpl implements TranHistoryService {

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public List<TranHistory> selectTranHistoryByDetailByTranId(String tranId) {
        return tranHistoryMapper.selectTranHistoryByDetailByTranId(tranId);
    }
}
