package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderDetail;
import cc.mrbird.febs.cos.service.IOrderDetailService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 订单详情 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/order-detail")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderDetailController {

    private final IOrderDetailService orderDetailService;

    /**
     * 分页获取订单详情
     *
     * @param page        分页对象
     * @param orderDetail 订单详情
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderDetail> page, OrderDetail orderDetail) {
        return R.ok();
    }

    /**
     * 查询订单详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderDetailService.getById(id));
    }

    /**
     * 查询订单详情列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderDetailService.list());
    }

    /**
     * 新增订单详情
     *
     * @param orderDetail 订单详情
     * @return 结果
     */
    @PostMapping
    public R save(OrderDetail orderDetail) {
        return R.ok(orderDetailService.save(orderDetail));
    }

    /**
     * 修改订单详情
     *
     * @param orderDetail 订单详情
     * @return 结果
     */
    @PutMapping
    public R edit(OrderDetail orderDetail) {
        return R.ok(orderDetailService.updateById(orderDetail));
    }

    /**
     * 删除订单详情
     *
     * @param ids ids
     * @return 订单详情
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderDetailService.removeByIds(ids));
    }
}
