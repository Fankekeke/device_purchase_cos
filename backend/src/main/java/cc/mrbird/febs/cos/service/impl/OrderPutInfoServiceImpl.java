package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.OrderOutInfo;
import cc.mrbird.febs.cos.entity.OrderPutInfo;
import cc.mrbird.febs.cos.dao.OrderPutInfoMapper;
import cc.mrbird.febs.cos.entity.StoreRecordInfo;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.IOrderPutInfoService;
import cc.mrbird.febs.cos.service.IStoreRecordInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
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
 * 库房入库 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderPutInfoServiceImpl extends ServiceImpl<OrderPutInfoMapper, OrderPutInfo> implements IOrderPutInfoService {

    private final IStoreRecordInfoService storeRecordInfoService;

    private final ICommodityInfoService commodityInfoService;

    /**
     * 分页获取库房入库信息
     *
     * @param page         分页对象
     * @param orderPutInfo 库房入库信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPutPage(Page<OrderPutInfo> page, OrderPutInfo orderPutInfo) {
        return baseMapper.selectOrderPutPage(page, orderPutInfo);
    }

    /**
     * 查询库房入库信息详情
     *
     * @param putId 主键ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderPutDetail(Integer putId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("put", null);
                put("record", Collections.emptyList());
            }
        };

        // 出库信息
        OrderPutInfo putInfo = this.getById(putId);
        if (putInfo == null) {
            return result;
        }
        result.put("put", putInfo);

        // 入库详情
        List<StoreRecordInfo> recordInfoList = storeRecordInfoService.list(Wrappers.<StoreRecordInfo>lambdaQuery().eq(StoreRecordInfo::getOrderNumber, putInfo.getCode()));
        List<String> codeList = recordInfoList.stream().map(StoreRecordInfo::getCommodityCode).collect(Collectors.toList());
        // 获取商品信息
        List<CommodityInfo> commodityInfoList = commodityInfoService.list(Wrappers.<CommodityInfo>lambdaQuery().in(CommodityInfo::getCode, codeList));
        Map<String, CommodityInfo> commodityMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getCode, e -> e));

        for (StoreRecordInfo storeRecordInfo : recordInfoList) {
            CommodityInfo commodityInfo = commodityMap.get(storeRecordInfo.getCommodityCode());
            if (commodityInfo == null) {
                continue;
            }
            storeRecordInfo.setName(commodityInfo.getName());
            storeRecordInfo.setModel(commodityInfo.getModel());
            storeRecordInfo.setImages(commodityInfo.getImages());
        }
        result.put("record", recordInfoList);
        return result;
    }

    /**
     * 添加库房入库
     *
     * @param orderPutInfo 入库信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrderPut(OrderPutInfo orderPutInfo) {
        // 获取入库信息
        List<StoreRecordInfo> storeRecordList = JSONUtil.toList(orderPutInfo.getStoreRecord(), StoreRecordInfo.class);
        // 创建时间
        orderPutInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 入库单号
        orderPutInfo.setCode("PUT-" + System.currentTimeMillis());
        // 计算总金额
        for (StoreRecordInfo storeRecordInfo : storeRecordList) {
            // 入库
            storeRecordInfo.setType("1");
            storeRecordInfo.setOrderNumber(orderPutInfo.getCode());
            storeRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            storeRecordInfo.setTotalPrice(NumberUtil.mul(storeRecordInfo.getPrice(), storeRecordInfo.getNum()));
        }
        BigDecimal totalPrice = storeRecordList.stream().map(StoreRecordInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        orderPutInfo.setTotalPrice(totalPrice);

        // 获取库房库存
        List<String> commodityCodeList = storeRecordList.stream().map(StoreRecordInfo::getCommodityCode).collect(Collectors.toList());
        List<StoreRecordInfo> storeRecords = storeRecordInfoService.list(Wrappers.<StoreRecordInfo>lambdaQuery().eq(StoreRecordInfo::getType, "0")
                .in(StoreRecordInfo::getCommodityCode, commodityCodeList));
        Map<String, StoreRecordInfo> storeMap = storeRecords.stream().collect(Collectors.toMap(StoreRecordInfo::getCommodityCode, e -> e));

        // 更新库房库存
        List<StoreRecordInfo> toUpdateList = new ArrayList<>();
        for (StoreRecordInfo storeRecordInfo : storeRecordList) {
            // 获取此商品库存
            StoreRecordInfo currentStoreRecordInfo = storeMap.get(storeRecordInfo.getCommodityCode());
            if (currentStoreRecordInfo == null) {
                continue;
            }
            // 更新此商品库存
            currentStoreRecordInfo.setNum(currentStoreRecordInfo.getNum() + storeRecordInfo.getNum());
            toUpdateList.add(currentStoreRecordInfo);
        }
        storeRecordInfoService.updateBatchById(toUpdateList);

        // 添加入库记录
        storeRecordInfoService.saveBatch(storeRecordList);

        // 保存入库单
        return this.save(orderPutInfo);
    }
}
