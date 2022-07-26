package com.kakuab.mapper;

import com.kakuab.pojo.TranRemark;

import java.util.List;

public interface TranRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Thu Jun 16 15:19:15 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Thu Jun 16 15:19:15 CST 2022
     */
    int insert(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Thu Jun 16 15:19:15 CST 2022
     */
    int insertSelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Thu Jun 16 15:19:15 CST 2022
     */
    TranRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Thu Jun 16 15:19:15 CST 2022
     */
    int updateByPrimaryKeySelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Thu Jun 16 15:19:15 CST 2022
     */
    int updateByPrimaryKey(TranRemark record);

    /**
     * 批量保存创建的交易备注
     * @param tranRemarkList
     * @return
     */
    int insertTranRemarkByList(List<TranRemark> tranRemarkList);

    /**
     * 根据交易的id查询交易备注的所有信息
     * @param id
     * @return
     */
    List<TranRemark> selectTranRemarkForDetailByTranId(String tranId);
}