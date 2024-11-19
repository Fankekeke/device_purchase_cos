package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.CommodityType;
import cc.mrbird.febs.cos.service.IAlertConfigurationInfoService;
import cc.mrbird.febs.cos.service.ICommodityTypeService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 商品类型 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/commodity-type")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommodityTypeController {

    private final ICommodityTypeService commodityTypeService;

    private final IAlertConfigurationInfoService alertConfigurationInfoService;

    /**
     * 分页获取商品类型信息
     *
     * @param page          分页对象
     * @param commodityType 商品类型信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<CommodityType> page, CommodityType commodityType) {
        return R.ok(commodityTypeService.selectTypePage(page, commodityType));
    }

    /**
     * 查询商品类型信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(commodityTypeService.getById(id));
    }

    /**
     * 查询商品类型信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(commodityTypeService.list());
    }

    /**
     * 新增商品类型信息
     *
     * @param commodityType 商品类型信息
     * @return 结果
     */
    @PostMapping
    public R save(CommodityType commodityType) {
        // 类型编号
        commodityType.setCode("CT-" + System.currentTimeMillis());
        // 创建时间
        commodityType.setCreateDate(DateUtil.formatDateTime(new Date()));
        commodityTypeService.save(commodityType);
        // 添加默认预警配置
        return R.ok(alertConfigurationInfoService.addDefaultConfiguration(commodityType.getId()));
    }

    /**
     * 修改商品类型信息
     *
     * @param commodityType 商品类型信息
     * @return 结果
     */
    @PutMapping
    public R edit(CommodityType commodityType) {
        return R.ok(commodityTypeService.updateById(commodityType));
    }

    /**
     * 删除商品类型信息
     *
     * @param ids ids
     * @return 商品类型信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(commodityTypeService.removeByIds(ids));
    }
}
