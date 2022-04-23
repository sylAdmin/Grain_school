package com.syl.eduorder.mapper;

import com.syl.eduorder.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * <p>
 * 订单 Mapper 接口
 * </p>
 *
 * @author sylup
 * @since 2022-04-18
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
