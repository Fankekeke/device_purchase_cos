package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AlertConfigurationInfo;
import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.StockAlertInfo;
import cc.mrbird.febs.cos.dao.StockAlertInfoMapper;
import cc.mrbird.febs.cos.entity.StoreRecordInfo;
import cc.mrbird.febs.cos.service.IAlertConfigurationInfoService;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.IStockAlertInfoService;
import cc.mrbird.febs.cos.service.IStoreRecordInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存预警 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockAlertInfoServiceImpl extends ServiceImpl<StockAlertInfoMapper, StockAlertInfo> implements IStockAlertInfoService {

    private final IStoreRecordInfoService storeRecordInfoService;

    private final IAlertConfigurationInfoService alertConfigurationInfoService;

    private final ICommodityInfoService commodityInfoService;

    /**
     * 分页获取库存预警信息
     *
     * @param page           分页对象
     * @param stockAlertInfo 库存预警信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStockAlertPage(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo) {
        return baseMapper.selectStockAlertPage(page, stockAlertInfo);
    }

    /**
     * 生成库存预警
     *
     * @return 结果
     */
    @Override
    public boolean generateStoreAlert() {
        // 获取库房库存记录
        List<StoreRecordInfo> recordList = storeRecordInfoService.list(Wrappers.<StoreRecordInfo>lambdaQuery().eq(StoreRecordInfo::getType, "0"));

        // 商品信息
        List<CommodityInfo> commodityInfoList = commodityInfoService.list();
        Map<String, CommodityInfo> commodityTypeMap = commodityInfoList.stream().collect(Collectors.toMap(CommodityInfo::getCode, e -> e));

        // 获取报警配置
        List<AlertConfigurationInfo> configurationList = alertConfigurationInfoService.list();
        Map<Integer, Integer> configurationMap = configurationList.stream().collect(Collectors.toMap(AlertConfigurationInfo::getTypeId, AlertConfigurationInfo::getAlertNum));

        // 获取未处理的预警信息
        List<StockAlertInfo> notCheckAlertList = this.list(Wrappers.<StockAlertInfo>lambdaQuery().eq(StockAlertInfo::getStatus, 0));
        Map<Integer, StockAlertInfo> notCheckAlertMap = notCheckAlertList.stream().collect(Collectors.toMap(StockAlertInfo::getCommodityId, e -> e));

        // 待更新数据
        List<StockAlertInfo> toUpdateList = new ArrayList<>();
        // 待添加数据
        List<StockAlertInfo> toAddList = new ArrayList<>();

        for (StoreRecordInfo storeRecordInfo : recordList) {
            // 商品信息
            CommodityInfo commodityInfo = commodityTypeMap.get(storeRecordInfo.getCommodityCode());

            // 获取此商品类型预警数量
            Integer alertNum = 50;
            if (commodityInfo.getTypeId() != null && CollectionUtil.isNotEmpty(configurationMap) && configurationMap.get(commodityInfo.getTypeId()) != null) {
                alertNum = configurationMap.get(commodityInfo.getTypeId());
            }

            // 当前库存是否到达预警数量
            boolean isAlert = storeRecordInfo.getNum() <= alertNum;

            // 是否存在此商品未处理信息
            StockAlertInfo notCheckItem = notCheckAlertMap.get(commodityInfo.getId());
            if (notCheckItem == null && isAlert) {
                StockAlertInfo addStockAlert = new StockAlertInfo();
                addStockAlert.setCommodityId(commodityInfo.getId());
                addStockAlert.setStatus(0);
                addStockAlert.setCreateDate(DateUtil.formatDateTime(new Date()));
                addStockAlert.setCurrentNum(storeRecordInfo.getNum());
                addStockAlert.setRemark("【" + commodityInfo.getName() + "】库存数量为 " + storeRecordInfo.getNum() + ", 请尽快补货");
                toAddList.add(addStockAlert);
            }

            if (notCheckItem != null && isAlert) {
                notCheckItem.setStatus(0);
                notCheckItem.setCreateDate(DateUtil.formatDateTime(new Date()));
                notCheckItem.setCurrentNum(storeRecordInfo.getNum());
                notCheckItem.setRemark("【" + commodityInfo.getName() + "】库存数量为 " + storeRecordInfo.getNum() + ", 请尽快补货");
                toUpdateList.add(notCheckItem);
            }

            if (notCheckItem != null && !isAlert) {
                notCheckItem.setStatus(1);
                notCheckItem.setCreateDate(DateUtil.formatDateTime(new Date()));
                toUpdateList.add(notCheckItem);
            }

        }

        this.saveBatch(toAddList);
        this.updateBatchById(toUpdateList);

        return true;
    }
}
