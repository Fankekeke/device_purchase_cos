package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderPutInfo;
import cc.mrbird.febs.cos.service.IOrderPutInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 库房入库 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/order-put-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderPutInfoController {

    private final IOrderPutInfoService orderPutInfoService;

    /**
     * 分页获取库房入库信息
     *
     * @param page         分页对象
     * @param orderPutInfo 库房入库信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderPutInfo> page, OrderPutInfo orderPutInfo) {
        return R.ok(orderPutInfoService.selectOrderPutPage(page, orderPutInfo));
    }

    /**
     * 添加库房入库
     *
     * @param orderPutInfo 入库信息
     * @return 结果
     */
    @PostMapping("/addOrderPut")
    public R addOrderPut(OrderPutInfo orderPutInfo) {
        return R.ok(orderPutInfoService.addOrderPut(orderPutInfo));
    }

    /**
     * 查询库房入库信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderPutInfoService.selectOrderPutDetail(id));
    }

    /**
     * 查询库房入库信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderPutInfoService.list());
    }

    /**
     * 新增库房入库信息
     *
     * @param orderPutInfo 库房入库信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderPutInfo orderPutInfo) {
        // 出库单号
        orderPutInfo.setCode("OUT-" + System.currentTimeMillis());
        // 创建时间
        orderPutInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(orderPutInfoService.save(orderPutInfo));
    }

    /**
     * 修改库房入库信息
     *
     * @param orderPutInfo 库房入库信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderPutInfo orderPutInfo) {
        return R.ok(orderPutInfoService.updateById(orderPutInfo));
    }

    /**
     * 删除库房入库信息
     *
     * @param ids ids
     * @return 库房入库信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderPutInfoService.removeByIds(ids));
    }
}
