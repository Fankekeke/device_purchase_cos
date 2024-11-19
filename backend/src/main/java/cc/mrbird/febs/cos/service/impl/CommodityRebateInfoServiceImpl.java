package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.CommodityRebateInfo;
import cc.mrbird.febs.cos.dao.CommodityRebateInfoMapper;
import cc.mrbird.febs.cos.service.ICommodityRebateInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 商品折扣设置 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
public class CommodityRebateInfoServiceImpl extends ServiceImpl<CommodityRebateInfoMapper, CommodityRebateInfo> implements ICommodityRebateInfoService {

    /**
     * 分页获取商品信息
     *
     * @param page                分页对象
     * @param commodityRebateInfo 商品信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectRebatePage(Page<CommodityRebateInfo> page, CommodityRebateInfo commodityRebateInfo) {
        return baseMapper.selectRebatePage(page, commodityRebateInfo);
    }

    /**
     * 添加商品后保存默认商品折扣
     *
     * @param commodityId 商品ID
     * @param price       商品价格
     * @return 结果
     */
    @Override
    public boolean addRebate(Integer commodityId, BigDecimal price) {
        CommodityRebateInfo commodityRebate = new CommodityRebateInfo();
        // 编号
        commodityRebate.setCode("REB-" + System.currentTimeMillis());
        // 商品ID
        commodityRebate.setCommodityId(commodityId);

        commodityRebate.setLowRate(price);
        commodityRebate.setNormalPrice(price);
        commodityRebate.setSpecialPrice(price);

        commodityRebate.setLowRestriction(BigDecimal.ZERO);
        commodityRebate.setSpecialRestriction(BigDecimal.ZERO);
        commodityRebate.setCreateDate(DateUtil.formatDateTime(new Date()));
        return this.save(commodityRebate);
    }
}
