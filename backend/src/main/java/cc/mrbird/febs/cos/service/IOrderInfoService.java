package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 订单管理 service层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo);

    /**
     * 查询订单详情
     *
     * @param orderId 订单编号
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderDetail(Integer orderId);

    /**
     * 用户添加订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    boolean orderAdd(OrderInfo orderInfo) throws FebsException;

    /**
     * 管理员首页数据统计
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> selectHomeData();

    /**
     * 年统计订单及收益
     *
     * @param date 年份
     * @return 结果
     */
    LinkedHashMap<String, Object> selectStoreStatisticsByYear(String date);

    /**
     * 月统计订单及收益
     *
     * @param date 日期
     * @return 结果
     */
    LinkedHashMap<String, Object> selectStoreStatisticsByMonth(String date);
}
