package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StockAlertInfo;
import cc.mrbird.febs.cos.service.IStockAlertInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存预警 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/stock-alert-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockAlertInfoController {

    private final IStockAlertInfoService stockAlertInfoService;

    /**
     * 分页获取库存预警信息
     *
     * @param page           分页对象
     * @param stockAlertInfo 库存预警信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo) {
        return R.ok(stockAlertInfoService.selectStockAlertPage(page, stockAlertInfo));
    }

    /**
     * 生成库存预警
     *
     * @return 结果
     */
    @GetMapping("/generateStoreAlert")
    public R generateStoreAlert() {
        return R.ok(stockAlertInfoService.generateStoreAlert());
    }

    /**
     * 查询库存预警信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(stockAlertInfoService.getById(id));
    }

    /**
     * 查询库存预警信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(stockAlertInfoService.list());
    }

    /**
     * 新增库存预警信息
     *
     * @param stockAlertInfo 库存预警信息
     * @return 结果
     */
    @PostMapping
    public R save(StockAlertInfo stockAlertInfo) {
        return R.ok(stockAlertInfoService.save(stockAlertInfo));
    }

    /**
     * 修改库存预警信息
     *
     * @param stockAlertInfo 库存预警信息
     * @return 结果
     */
    @PutMapping
    public R edit(StockAlertInfo stockAlertInfo) {
        return R.ok(stockAlertInfoService.updateById(stockAlertInfo));
    }

    /**
     * 删除库存预警信息
     *
     * @param ids ids
     * @return 库存预警信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(stockAlertInfoService.removeByIds(ids));
    }
}
