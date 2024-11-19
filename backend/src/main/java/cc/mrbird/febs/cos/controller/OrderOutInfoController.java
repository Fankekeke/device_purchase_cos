package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderOutInfo;
import cc.mrbird.febs.cos.service.IOrderOutInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 库房出库 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/order-out-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderOutInfoController {

    private final IOrderOutInfoService orderOutInfoService;

    /**
     * 分页获取库房出库信息
     *
     * @param page         分页对象
     * @param orderOutInfo 库房出库信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderOutInfo> page, OrderOutInfo orderOutInfo) {
        return R.ok(orderOutInfoService.selectOrderOutPage(page, orderOutInfo));
    }

    /**
     * 查询库房出库信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderOutInfoService.selectOrderOutDetail(id));
    }

    /**
     * 查询库房出库信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderOutInfoService.list());
    }

    /**
     * 新增库房出库信息
     *
     * @param orderOutInfo 库房出库信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderOutInfo orderOutInfo) {
        // 出库单号
        orderOutInfo.setCode("OUT-" + System.currentTimeMillis());
        // 创建时间
        orderOutInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(orderOutInfoService.save(orderOutInfo));
    }

    /**
     * 修改库房出库信息
     *
     * @param orderOutInfo 库房出库信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderOutInfo orderOutInfo) {
        return R.ok(orderOutInfoService.updateById(orderOutInfo));
    }

    /**
     * 删除库房出库信息
     *
     * @param ids ids
     * @return 库房出库信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderOutInfoService.removeByIds(ids));
    }
}
