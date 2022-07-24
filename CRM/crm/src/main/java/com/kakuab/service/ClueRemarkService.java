package com.kakuab.service;

import com.kakuab.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);
}
