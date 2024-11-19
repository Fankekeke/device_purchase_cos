package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.CommodityRebateInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * 商品折扣设置 service层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface ICommodityRebateInfoService extends IService<CommodityRebateInfo> {

    /**
     * 分页获取商品信息
     *
     * @param page                分页对象
     * @param commodityRebateInfo 商品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectRebatePage(Page<CommodityRebateInfo> page, CommodityRebateInfo commodityRebateInfo);

    /**
     * 添加商品后保存默认商品折扣
     *
     * @param commodityId 商品ID
     * @param price       商品价格
     * @return 结果
     */
    boolean addRebate(Integer commodityId, BigDecimal price);
}
