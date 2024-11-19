package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.CommodityType;
import cc.mrbird.febs.cos.dao.CommodityTypeMapper;
import cc.mrbird.febs.cos.service.ICommodityTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 商品类型 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
public class CommodityTypeServiceImpl extends ServiceImpl<CommodityTypeMapper, CommodityType> implements ICommodityTypeService {

    /**
     * 分页获取商品类型信息
     *
     * @param page          分页对象
     * @param commodityType 商品类型信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectTypePage(Page<CommodityType> page, CommodityType commodityType) {
        return baseMapper.selectTypePage(page, commodityType);
    }
}
