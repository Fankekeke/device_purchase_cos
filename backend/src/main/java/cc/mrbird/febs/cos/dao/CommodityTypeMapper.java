package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.CommodityType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 商品类型 mapper层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface CommodityTypeMapper extends BaseMapper<CommodityType> {

    /**
     * 分页获取商品类型信息
     *
     * @param page          分页对象
     * @param commodityType 商品类型信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectTypePage(Page<CommodityType> page, @Param("commodityType") CommodityType commodityType);
}
