package com.kakuab.mapper;

import com.kakuab.pojo.Customer;

import java.util.List;

public interface CustomerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Wed Jun 15 15:17:40 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Wed Jun 15 15:17:40 CST 2022
     */
    int insert(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Wed Jun 15 15:17:40 CST 2022
     */
    int insertSelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Wed Jun 15 15:17:40 CST 2022
     */
    Customer selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Wed Jun 15 15:17:40 CST 2022
     */
    int updateByPrimaryKeySelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Wed Jun 15 15:17:40 CST 2022
     */
    int updateByPrimaryKey(Customer record);

    /**
     * 保存客户创建
     * @param customer
     * @return
     */
    int insertCustomer(Customer customer);

    /**
     * 查询客户全部信息
     * @return
     */
    List<Customer> selectAllCustomer();

    /**
     * 根据用户名字模糊查询
     * @param name
     * @return
     */
    List<String> selectCustomerNameByName(String name);

    /**
     * 根据客户名字精确查询
     * @param name
     * @return
     */
    Customer selectCustomerByName(String name);
}