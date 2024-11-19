package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.CommodityRebateInfo;
import cc.mrbird.febs.cos.service.ICommodityRebateInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品折扣设置 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/commodity-rebate-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommodityRebateInfoController {

    private final ICommodityRebateInfoService commodityRebateInfoService;

    /**
     * 分页获取商品信息
     *
     * @param page                分页对象
     * @param commodityRebateInfo 商品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<CommodityRebateInfo> page, CommodityRebateInfo commodityRebateInfo) {
        return R.ok(commodityRebateInfoService.selectRebatePage(page, commodityRebateInfo));
    }

    /**
     * 查询商品信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(commodityRebateInfoService.getById(id));
    }

    /**
     * 查询商品信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(commodityRebateInfoService.list());
    }

    /**
     * 新增商品信息
     *
     * @param commodityRebateInfo 商品信息
     * @return 结果
     */
    @PostMapping
    public R save(CommodityRebateInfo commodityRebateInfo) {
        return R.ok(commodityRebateInfoService.save(commodityRebateInfo));
    }

    /**
     * 修改商品信息
     *
     * @param commodityRebateInfo 商品信息
     * @return 结果
     */
    @PutMapping
    public R edit(CommodityRebateInfo commodityRebateInfo) {
        return R.ok(commodityRebateInfoService.updateById(commodityRebateInfo));
    }

    /**
     * 删除商品信息
     *
     * @param ids ids
     * @return 商品信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(commodityRebateInfoService.removeByIds(ids));
    }
}
