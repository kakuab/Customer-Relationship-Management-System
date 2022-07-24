package com.kakuab.service;

import com.kakuab.pojo.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> selectTranHistoryByDetailByTranId(String tranId);
}
