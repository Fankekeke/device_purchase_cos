package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AlertConfigurationInfo;
import cc.mrbird.febs.cos.service.IAlertConfigurationInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 预警配置 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/alert-configuration-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlertConfigurationInfoController {

    private final IAlertConfigurationInfoService alertConfigurationInfoService;

    /**
     * 分页获取报警配置
     *
     * @param page                   分页对象
     * @param alertConfigurationInfo 报警配置
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AlertConfigurationInfo> page, AlertConfigurationInfo alertConfigurationInfo) {
        return R.ok(alertConfigurationInfoService.selectAlertConfigurationPage(page, alertConfigurationInfo));
    }

    /**
     * 查询报警配置详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(alertConfigurationInfoService.getById(id));
    }

    /**
     * 查询报警配置列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(alertConfigurationInfoService.list());
    }

    /**
     * 新增报警配置
     *
     * @param alertConfigurationInfo 报警配置
     * @return 结果
     */
    @PostMapping
    public R save(AlertConfigurationInfo alertConfigurationInfo) {
        alertConfigurationInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(alertConfigurationInfoService.save(alertConfigurationInfo));
    }

    /**
     * 修改报警配置
     *
     * @param alertConfigurationInfo 报警配置
     * @return 结果
     */
    @PutMapping
    public R edit(AlertConfigurationInfo alertConfigurationInfo) {
        return R.ok(alertConfigurationInfoService.updateById(alertConfigurationInfo));
    }

    /**
     * 删除报警配置
     *
     * @param ids ids
     * @return 报警配置
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(alertConfigurationInfoService.removeByIds(ids));
    }
}
