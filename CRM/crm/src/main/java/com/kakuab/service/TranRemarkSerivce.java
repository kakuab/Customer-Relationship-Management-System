package com.kakuab.service;

import com.kakuab.pojo.TranRemark;

import java.util.List;

public interface TranRemarkSerivce {
    List<TranRemark> selectTranRemarkForDetailByTranId(String tranId);
}
