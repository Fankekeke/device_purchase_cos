package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.CommodityInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 商品管理 service层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface ICommodityInfoService extends IService<CommodityInfo> {

    /**
     * 分页获取商品信息
     *
     * @param page          分页对象
     * @param commodityInfo 商品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectCommodityPage(Page<CommodityInfo> page, CommodityInfo commodityInfo);

    /**
     * 根据用户获取商品信息【动态价格】
     *
     * @param commodityInfo 商品信息
     * @return 结果
     */
    List<CommodityInfo> selectCommodityByUser(CommodityInfo commodityInfo);
}
