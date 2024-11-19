package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.CommodityRebateInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 商品折扣设置 mapper层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface CommodityRebateInfoMapper extends BaseMapper<CommodityRebateInfo> {

    /**
     * 分页获取商品信息
     *
     * @param page                分页对象
     * @param commodityRebateInfo 商品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRebatePage(Page<CommodityRebateInfo> page, @Param("commodityRebateInfo") CommodityRebateInfo commodityRebateInfo);
}
