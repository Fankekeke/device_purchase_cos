package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.CommodityInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品管理 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommodityInfoServiceImpl extends ServiceImpl<CommodityInfoMapper, CommodityInfo> implements ICommodityInfoService {

    private final ICommodityRebateInfoService commodityRebateInfoService;

    private final IUserInfoService userInfoService;

    private final IStoreRecordInfoService storeRecordInfoService;

    private final ICommodityTypeService commodityTypeService;

    /**
     * 分页获取商品信息
     *
     * @param page          分页对象
     * @param commodityInfo 商品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectCommodityPage(Page<CommodityInfo> page, CommodityInfo commodityInfo) {
        return baseMapper.selectCommodityPage(page, commodityInfo);
    }

    /**
     * 根据用户获取商品信息【动态价格】
     *
     * @param commodityInfo 商品信息
     * @return 结果
     */
    @Override
    public List<CommodityInfo> selectCommodityByUser(CommodityInfo commodityInfo) {
        // 商品信息
        List<CommodityInfo> commodityInfoList = this.list(Wrappers.<CommodityInfo>lambdaQuery()
                .like(StrUtil.isNotEmpty(commodityInfo.getName()), CommodityInfo::getName, commodityInfo.getName()));
        List<String> commodityCodeList = commodityInfoList.stream().map(CommodityInfo::getCode).collect(Collectors.toList());

        // 商品折扣
        List<CommodityRebateInfo> rebateInfoList = commodityRebateInfoService.list();
        Map<Integer, CommodityRebateInfo> rebateMap = rebateInfoList.stream().collect(Collectors.toMap(CommodityRebateInfo::getCommodityId, e -> e));

        // 商品类型
        List<CommodityType> commodityTypeList = commodityTypeService.list();
        Map<Integer, String> commodityTypeMap = commodityTypeList.stream().collect(Collectors.toMap(CommodityType::getId, CommodityType::getName));

        // 获取用户信息计算注册时常
        UserInfo user = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, commodityInfo.getUserId()));
//        long month = DateUtil.betweenMonth(DateUtil.parseDate(user.getCreateDate()), new Date(), true);

        // 商品库存
        List<StoreRecordInfo> recordInfoList = storeRecordInfoService.list(Wrappers.<StoreRecordInfo>lambdaQuery().in(CollectionUtil.isNotEmpty(commodityCodeList), StoreRecordInfo::getCommodityCode, commodityCodeList)
                .eq(StoreRecordInfo::getType, 0));
        Map<String, Integer> recordInfoMap = recordInfoList.stream().collect(Collectors.toMap(StoreRecordInfo::getCommodityCode, StoreRecordInfo::getNum));

        for (CommodityInfo commodity : commodityInfoList) {
            // 商品类型
            commodity.setTypeName(commodityTypeMap.get(commodity.getTypeId()));
            // 获取此商品折扣信息
            CommodityRebateInfo rebateInfo = rebateMap.get(commodity.getId());
            commodity.setReserve(recordInfoMap.get(commodity.getCode()));
            if (rebateInfo == null) {
                continue;
            }

            if (user.getPrice().compareTo(rebateInfo.getSpecialRestriction()) > 0) {
                commodity.setSellPrice(rebateInfo.getSpecialPrice());
            } else if (user.getPrice().compareTo(rebateInfo.getLowRestriction()) > 0) {
                commodity.setSellPrice(rebateInfo.getLowRate());
            }
        }
        return commodityInfoList;
    }
}
