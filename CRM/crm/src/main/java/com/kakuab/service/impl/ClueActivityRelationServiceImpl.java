package com.kakuab.service.impl;

import com.kakuab.mapper.ClueActivityRelationMapper;
import com.kakuab.pojo.ClueActivityRelation;
import com.kakuab.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int insertClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.insertClueActivityRelationByList(clueActivityRelationList);
    }

    @Override
    public int deleteClueActivityRelationByActivityIdClueId(ClueActivityRelation clueActivityRelation) {
        return clueActivityRelationMapper.deleteClueActivityRelationByActivityIdClueId(clueActivityRelation);
    }
}
