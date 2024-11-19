package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.CommodityInfo;
import cc.mrbird.febs.cos.entity.CommodityRebateInfo;
import cc.mrbird.febs.cos.entity.StoreRecordInfo;
import cc.mrbird.febs.cos.service.ICommodityInfoService;
import cc.mrbird.febs.cos.service.ICommodityRebateInfoService;
import cc.mrbird.febs.cos.service.IStoreRecordInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 商品管理 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/commodity-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommodityInfoController {

    private final ICommodityInfoService commodityInfoService;

    private final ICommodityRebateInfoService commodityRebateInfoService;

    private final IStoreRecordInfoService storeRecordInfoService;

    /**
     * 分页获取商品信息
     *
     * @param page          分页对象
     * @param commodityInfo 商品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<CommodityInfo> page, CommodityInfo commodityInfo) {
        return R.ok(commodityInfoService.selectCommodityPage(page, commodityInfo));
    }

    /**
     * 根据用户获取商品信息【动态价格】
     *
     * @param commodityInfo 商品信息
     * @return 结果
     */
    @GetMapping("/list/byUser")
    public R selectCommodityByUser(CommodityInfo commodityInfo) {
        return R.ok(commodityInfoService.selectCommodityByUser(commodityInfo));
    }

    /**
     * 查询商品信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(commodityInfoService.getById(id));
    }

    /**
     * 查询商品信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(commodityInfoService.list());
    }

    /**
     * 新增商品信息
     *
     * @param commodityInfo 商品信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(CommodityInfo commodityInfo) {
        // 商品编号
        commodityInfo.setCode("CDY-" + System.currentTimeMillis());
        // 创建时间
        commodityInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        commodityInfoService.save(commodityInfo);

        // 添加库存
        StoreRecordInfo storeRecordInfo = new StoreRecordInfo();
        storeRecordInfo.setCommodityCode(commodityInfo.getCode());
        storeRecordInfo.setType("0");
        storeRecordInfo.setNum(0);
        storeRecordInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        storeRecordInfoService.save(storeRecordInfo);

        return R.ok(commodityRebateInfoService.addRebate(commodityInfo.getId(), commodityInfo.getSellPrice()));
    }

    /**
     * 修改商品信息
     *
     * @param commodityInfo 商品信息
     * @return 结果
     */
    @PutMapping
    public R edit(CommodityInfo commodityInfo) {
        // 修改商品折扣价格
        commodityRebateInfoService.update(Wrappers.<CommodityRebateInfo>lambdaUpdate().set(CommodityRebateInfo::getNormalPrice, commodityInfo.getSellPrice())
                .eq(CommodityRebateInfo::getCommodityId, commodityInfo.getId()));
        return R.ok(commodityInfoService.updateById(commodityInfo));
    }

    /**
     * 删除商品信息
     *
     * @param ids ids
     * @return 商品信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(commodityInfoService.removeByIds(ids));
    }
}
