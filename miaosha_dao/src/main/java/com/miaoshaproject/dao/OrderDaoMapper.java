package com.miaoshaproject.dao;

import com.miaoshaproject.dataobjcet.OrderDao;

public interface OrderDaoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 10:33:16 GMT+08:00 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 10:33:16 GMT+08:00 2019
     */
    int insert(OrderDao record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 10:33:16 GMT+08:00 2019
     */
    int insertSelective(OrderDao record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 10:33:16 GMT+08:00 2019
     */
    OrderDao selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 10:33:16 GMT+08:00 2019
     */
    int updateByPrimaryKeySelective(OrderDao record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 10:33:16 GMT+08:00 2019
     */
    int updateByPrimaryKey(OrderDao record);
}