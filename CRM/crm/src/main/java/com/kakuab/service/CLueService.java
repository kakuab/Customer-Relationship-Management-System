package com.kakuab.service;

import com.kakuab.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface CLueService {
    int inserClue(Clue clue);

    Clue selectClueForDetailByid(String id);

    List<Clue> selectAllClue();

    void saveConvert(Map<String,Object> map);
}
