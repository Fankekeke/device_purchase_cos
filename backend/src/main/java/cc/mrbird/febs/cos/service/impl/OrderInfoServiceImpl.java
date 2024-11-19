package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.OrderOutInfoMapper;
import cc.mrbird.febs.cos.dao.OrderPutInfoMapper;
import cc.mrbird.febs.cos.dao.StoreRecordInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单管理 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final IAddressInfoService addressInfoService;

    private final ICommodityInfoService commodityInfoService;

    private final ICommodityTypeService commodityTypeService;

    private final ILogisticsInfoService logisticsInfoService;

    private final IUserInfoService userInfoService;

    private final IOrderDetailService orderDetailService;

    private final IBulletinInfoService bulletinInfoService;

    private final StoreRecordInfoMapper storeRecordInfoMapper;

    private final OrderOutInfoMapper orderOutInfoMapper;

    private final OrderPutInfoMapper orderPutInfoMapper;



    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单编号
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderDetail(Integer orderId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("order", null);
                put("detail", Collections.emptyList());
                put("user", null);
                put("logistics", Collections.emptyList());
                put("address", null);
            }
        };

        // 订单信息
        OrderInfo orderInfo = this.getById(orderId);
        result.put("order", orderInfo);

        // 订单详情
        List<OrderDetail> orderDetailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderId));
        List<String> commodityCodeList = orderDetailList.stream().map(OrderDetail::getCommodityCode).collect(Collectors.toList());
        // 商品信息
        List<CommodityInfo> commodityInfoList = commodityInfoService.list(Wrappers.<CommodityInfo>lambdaQuery().in(CommodityInfo::getCode, commodityCodeList));
        Map<String, CommodityInfo> commodityInfoMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getCode, e -> e));
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setCommodity(commodityInfoMap.get(orderDetail.getCommodityCode()));
        }
        result.put("detail", orderDetailList);

        // 用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        result.put("user", userInfo);

        // 物流信息
        List<LogisticsInfo> logisticsInfoList = logisticsInfoService.list(Wrappers.<LogisticsInfo>lambdaQuery().eq(LogisticsInfo::getOrderId, orderId));
        result.put("logistics", logisticsInfoList);

        // 收货地址
        if (orderInfo.getAddressId() != null) {
            AddressInfo addressInfo = addressInfoService.getById(orderInfo.getAddressId());
            result.put("address", addressInfo);
        }
        return result;
    }

    /**
     * 用户添加订单
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderAdd(OrderInfo orderInfo) throws FebsException {
        // 获取订单详情
        List<OrderDetail> orderDetailList = JSONUtil.toList(orderInfo.getOrderDetail(), OrderDetail.class);
        if (CollectionUtil.isEmpty(orderDetailList)) {
            throw new FebsException("订单详情不能为空");
        }

        // 订单总金额
        BigDecimal totalPrice = orderDetailList.stream().map(e -> NumberUtil.mul(e.getNum(), e.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        orderInfo.setTotalPrice(totalPrice);

        // 如果为预付款
        if ("1".equals(orderInfo.getType())) {
            // 预付款金额为40%
            orderInfo.setOweCode("OWE-" + System.currentTimeMillis());
            orderInfo.setSubsistPrice(NumberUtil.mul(orderInfo.getTotalPrice(), 0.4));
            orderInfo.setOwePrice(NumberUtil.sub(totalPrice, orderInfo.getSubsistPrice()));
        }
        this.save(orderInfo);
        orderDetailList.forEach(e -> e.setOrderId(orderInfo.getId()));
        return orderDetailService.saveBatch(orderDetailList);
    }

    /**
     * 管理员首页数据统计
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectHomeData() {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        List<OrderInfo> orderInfoList = this.list(Wrappers.<OrderInfo>lambdaQuery().ne(OrderInfo::getStatus, "0"));
        // 总订单
        result.put("registerNum", orderInfoList.size());
        // 总收益s
        result.put("orderPrice", orderInfoList.stream().map(OrderInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 出库数量
        result.put("outNum", orderOutInfoMapper.selectCount(Wrappers.<OrderOutInfo>lambdaQuery()));
        // 入库数量
        result.put("putNum", orderPutInfoMapper.selectCount(Wrappers.<OrderPutInfo>lambdaQuery()));

        int year = DateUtil.year(new Date());
        int month = DateUtil.month(new Date()) + 1;

        // 本月出库数量
        List<StoreRecordInfo> monthOutRecordList = storeRecordInfoMapper.selectRecordBySlot(year, month, "2");
        result.put("monthOutNum", monthOutRecordList.size());
        // 本月出库收益
        result.put("monthOutPrice", monthOutRecordList.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));

        // 本年出库数量
        List<StoreRecordInfo> yearOutRecordList = storeRecordInfoMapper.selectRecordBySlot(year, null, "2");
        result.put("yearOutNum", yearOutRecordList.size());
        // 本年出库收益
        result.put("yearOutPrice", yearOutRecordList.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));

        // 本月入库数量
        List<StoreRecordInfo> monthPutRecordList = storeRecordInfoMapper.selectRecordBySlot(year, month, "1");
        result.put("monthPutNum", monthPutRecordList.size());
        // 本月入库支出
        result.put("monthPutPrice", monthPutRecordList.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));

        // 本年入库数量
        List<StoreRecordInfo> yearPutRecordList = storeRecordInfoMapper.selectRecordBySlot(year, month, "1");
        result.put("yearPutNum", yearPutRecordList.size());
        // 本年入库支出
        result.put("yearPutPrice", yearPutRecordList.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));


        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        // 近十天内出库统计
        result.put("orderNumWithinDays", baseMapper.selectOrderNumWithinDays("2"));
        // 近十天内出库收益统计
        result.put("orderPriceWithinDays", baseMapper.selectOrderPriceWithinDays("2"));

        // 近十天内入库统计
        result.put("putNumWithinDays", baseMapper.selectOrderNumWithinDays("1"));
        // 近十天内入库收益统计
        result.put("putPriceWithinDays", baseMapper.selectOrderPriceWithinDays("1"));
        return result;
    }

    /**
     * 年统计订单及收益
     *
     * @param date 年份
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectStoreStatisticsByYear(String date) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        int year =  DateUtil.year(new Date());
        if (StrUtil.isNotEmpty(date)) {
            year = Integer.parseInt(date);
        }

        List<OrderOutInfo> orderOutList = orderOutInfoMapper.selectOrderOutListByDate(year, null);
        Map<Integer, List<OrderOutInfo>> orderOutMonthMap = orderOutList.stream().collect(Collectors.groupingBy(OrderOutInfo::getMonth));
        List<OrderPutInfo> orderPutList = orderPutInfoMapper.selectOrderPutListByDate(year, null);
        Map<Integer, List<OrderPutInfo>> orderPutMonthMap = orderPutList.stream().collect(Collectors.groupingBy(OrderPutInfo::getMonth));

        // 本年订单量
        result.put("orderNum", orderOutList.size());
        // 本年总收益
        BigDecimal totalPrice = orderOutList.stream().map(OrderOutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalPrice", totalPrice);
        // 本年入库单量
        result.put("putNum", orderPutList.size());
        // 本年总支出
        BigDecimal outlayPrice = orderPutList.stream().map(OrderPutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("outlayPrice", outlayPrice);

        List<Integer> orderNumList = new ArrayList<>();
        List<BigDecimal> orderPriceList = new ArrayList<>();
        List<Integer> outlayNumList = new ArrayList<>();
        List<BigDecimal> outlayPriceList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {

            // 本月出库
            List<OrderOutInfo> currentMonthOutList = orderOutMonthMap.get(i);
            if (CollectionUtil.isEmpty(currentMonthOutList)) {
                orderNumList.add(0);
                orderPriceList.add(BigDecimal.ZERO);
            } else {
                orderNumList.add(currentMonthOutList.size());
                BigDecimal currentMonthOutPrice = currentMonthOutList.stream().map(OrderOutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                orderPriceList.add(currentMonthOutPrice);
            }

            // 本月入库
            List<OrderPutInfo> currentMonthPutList = orderPutMonthMap.get(i);
            if (CollectionUtil.isEmpty(currentMonthPutList)) {
                outlayNumList.add(0);
                outlayPriceList.add(BigDecimal.ZERO);
            } else {
                outlayNumList.add(currentMonthPutList.size());
                BigDecimal currentMonthPutPrice = currentMonthPutList.stream().map(OrderPutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                outlayPriceList.add(currentMonthPutPrice);
            }

        }
        // 12月内订单收益统计
        result.put("orderPriceList", orderPriceList);
        // 12月内订单量统计
        result.put("orderNumList", orderNumList);
        // 12月内入库成本统计
        result.put("outlayPriceList", outlayPriceList);
        // 12月内入库量统计
        result.put("outlayNumList", outlayNumList);

        // 商品销量排行
        List<LinkedHashMap<String, Object>> saleRank = new ArrayList<>();
        List<LinkedHashMap<String, Object>> salePriceRank = new ArrayList<>();
        LinkedHashMap<Integer, Integer> saleTypeRankMap = new LinkedHashMap<>();

        List<String> orderCodes = orderOutList.stream().map(OrderOutInfo::getCode).collect(Collectors.toList());
        List<StoreRecordInfo> recordInfoList = CollectionUtil.isNotEmpty(orderCodes) ? storeRecordInfoMapper.selectList(Wrappers.<StoreRecordInfo>lambdaQuery().in(StoreRecordInfo::getOrderNumber, orderCodes)) : Collections.emptyList();
        Map<String, List<StoreRecordInfo>> recordInfoMap = recordInfoList.stream().collect(Collectors.groupingBy(StoreRecordInfo::getCommodityCode));

        // 商品信息
        Set<String> commodityCodeList = recordInfoMap.keySet();
        List<CommodityInfo> commodityInfoList = commodityInfoService.list(Wrappers.<CommodityInfo>lambdaQuery().in(CollectionUtil.isNotEmpty(commodityCodeList), CommodityInfo::getCode, commodityCodeList));
        Map<String, CommodityInfo> commodityMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getCode, e -> e));

        recordInfoMap.forEach((key, value) -> {
            CommodityInfo commodity = commodityMap.get(key);
            if (commodity != null) {
                saleRank.add(new LinkedHashMap<String, Object>() {
                    {
                        put("name", commodity.getName());
                        put("num", value.stream().mapToInt(StoreRecordInfo::getNum).sum());
                    }
                });
                salePriceRank.add(new LinkedHashMap<String, Object>() {
                    {
                        put("name", commodity.getName());
                        put("num", value.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    }
                });

                saleTypeRankMap.merge(commodity.getTypeId(), value.size(), Integer::sum);
            }
        });
        result.put("saleRank", saleRank);
        result.put("salePriceRank", salePriceRank);
        // 销售商品分类
        LinkedHashMap<String, Integer> saleTypeRankMapCopy = new LinkedHashMap<>();
        List<CommodityType> commodityTypeList = commodityTypeService.list();
        Map<Integer, String> commodityTypeMap = commodityTypeList.stream().collect(Collectors.toMap(CommodityType::getId, CommodityType::getName));
        commodityTypeMap.forEach((key, value) -> {
            saleTypeRankMapCopy.put(value, saleTypeRankMap.get(key) == null ? 0 : saleTypeRankMap.get(key));
        });
        result.put("saleTypeRankMapCopy", saleTypeRankMapCopy);
        return result;
    }

    /**
     * 月统计订单及收益
     *
     * @param dateStr 日期
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectStoreStatisticsByMonth(String dateStr) {
        String date = dateStr + "-01";
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        int year =  DateUtil.year(new Date());
        int month =  DateUtil.month(new Date()) + 1;
        if (StrUtil.isNotEmpty(date)) {
            year = DateUtil.year(DateUtil.parseDate(date));
            month = DateUtil.month(DateUtil.parseDate(date)) + 1;
        }

        List<OrderOutInfo> orderOutList = orderOutInfoMapper.selectOrderOutListByDate(year, month);
        Map<Integer, List<OrderOutInfo>> orderOutDayMap = orderOutList.stream().collect(Collectors.groupingBy(OrderOutInfo::getDay));
        List<OrderPutInfo> orderPutList = orderPutInfoMapper.selectOrderPutListByDate(year, month);
        Map<Integer, List<OrderPutInfo>> orderPutDayMap = orderPutList.stream().collect(Collectors.groupingBy(OrderPutInfo::getDay));

        // 本月订单量
        result.put("orderNum", orderOutList.size());
        // 本月总收益
        BigDecimal totalPrice = orderOutList.stream().map(OrderOutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalPrice", totalPrice);
        // 本月入库单量
        result.put("putNum", orderPutList.size());
        // 本月总支出
        BigDecimal outlayPrice = orderPutList.stream().map(OrderPutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("outlayPrice", outlayPrice);

        List<Integer> orderNumList = new ArrayList<>();
        List<BigDecimal> orderPriceList = new ArrayList<>();
        List<Integer> outlayNumList = new ArrayList<>();
        List<BigDecimal> outlayPriceList = new ArrayList<>();
        int days = DateUtil.getLastDayOfMonth(DateUtil.parseDate(date));

        // 本月日期
        List<String> dateTimeList = new ArrayList<>();

        for (int i = 1; i <= days; i++) {
            dateTimeList.add(month + "月" + i + "日");
            // 本天出库
            List<OrderOutInfo> currentDayOutList = orderOutDayMap.get(i);
            if (CollectionUtil.isEmpty(currentDayOutList)) {
                orderNumList.add(0);
                orderPriceList.add(BigDecimal.ZERO);
            } else {
                orderNumList.add(currentDayOutList.size());
                BigDecimal currentDayOutPrice = currentDayOutList.stream().map(OrderOutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                orderPriceList.add(currentDayOutPrice);
            }

            // 本天入库
            List<OrderPutInfo> currentDayPutList = orderPutDayMap.get(i);
            if (CollectionUtil.isEmpty(currentDayPutList)) {
                outlayNumList.add(0);
                outlayPriceList.add(BigDecimal.ZERO);
            } else {
                outlayNumList.add(currentDayPutList.size());
                BigDecimal currentDayPutPrice = currentDayPutList.stream().map(OrderPutInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                outlayPriceList.add(currentDayPutPrice);
            }

        }
        // 本月内订单收益统计
        result.put("orderPriceList", orderPriceList);
        // 本月内订单量统计
        result.put("orderNumList", orderNumList);
        // 本月内入库成本统计
        result.put("outlayPriceList", outlayPriceList);
        // 本月内入库量统计
        result.put("outlayNumList", outlayNumList);

        result.put("dateList", dateTimeList);
        // 商品销量排行
        List<LinkedHashMap<String, Object>> saleRank = new ArrayList<>();
        List<LinkedHashMap<String, Object>> salePriceRank = new ArrayList<>();
        LinkedHashMap<Integer, Integer> saleTypeRankMap = new LinkedHashMap<>();

        List<String> orderCodes = orderOutList.stream().map(OrderOutInfo::getCode).collect(Collectors.toList());
        List<StoreRecordInfo> recordInfoList = CollectionUtil.isNotEmpty(orderCodes) ? storeRecordInfoMapper.selectList(Wrappers.<StoreRecordInfo>lambdaQuery().in(StoreRecordInfo::getOrderNumber, orderCodes)) : Collections.emptyList();
        Map<String, List<StoreRecordInfo>> recordInfoMap = recordInfoList.stream().collect(Collectors.groupingBy(StoreRecordInfo::getCommodityCode));

        // 商品信息
        Set<String> commodityCodeList = recordInfoMap.keySet();
        List<CommodityInfo> commodityInfoList = commodityInfoService.list(Wrappers.<CommodityInfo>lambdaQuery().in(CollectionUtil.isNotEmpty(commodityCodeList), CommodityInfo::getCode, commodityCodeList));
        Map<String, CommodityInfo> commodityMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getCode, e -> e));

        recordInfoMap.forEach((key, value) -> {
            CommodityInfo commodity = commodityMap.get(key);
            if (commodity != null) {
                saleRank.add(new LinkedHashMap<String, Object>() {
                    {
                        put("name", commodity.getName());
                        put("num", value.stream().mapToInt(StoreRecordInfo::getNum).sum());
                    }
                });
                salePriceRank.add(new LinkedHashMap<String, Object>() {
                    {
                        put("name", commodity.getName());
                        put("num", value.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
                    }
                });

                saleTypeRankMap.merge(commodity.getTypeId(), value.size(), Integer::sum);
            }
        });
        result.put("saleRank", saleRank);
        result.put("salePriceRank", salePriceRank);
        // 销售商品分类
        LinkedHashMap<String, Integer> saleTypeRankMapCopy = new LinkedHashMap<>();
        List<CommodityType> commodityTypeList = commodityTypeService.list();
        Map<Integer, String> commodityTypeMap = commodityTypeList.stream().collect(Collectors.toMap(CommodityType::getId, CommodityType::getName));
        commodityTypeMap.forEach((key, value) -> {
            saleTypeRankMapCopy.put(value, saleTypeRankMap.get(key) == null ? 0 : saleTypeRankMap.get(key));
        });
        result.put("saleTypeRankMapCopy", saleTypeRankMapCopy);
        return result;
    }
}
