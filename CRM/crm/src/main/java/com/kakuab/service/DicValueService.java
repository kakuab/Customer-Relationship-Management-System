package com.kakuab.service;

import com.kakuab.pojo.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}
