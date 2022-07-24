package com.kakuab.service;

import com.kakuab.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    int insertClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueActivityRelationByActivityIdClueId(ClueActivityRelation clueActivityRelation);
}
