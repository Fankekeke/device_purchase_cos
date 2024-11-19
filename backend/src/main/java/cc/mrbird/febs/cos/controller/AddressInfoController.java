package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IAddressInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户收货地址 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/address-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressInfoController {

    private final IAddressInfoService addressInfoService;

    private final IUserInfoService userInfoService;

    /**
     * 分页获取用户收货地址信息
     *
     * @param page        分页对象
     * @param addressInfo 用户收货地址信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<AddressInfo> page, AddressInfo addressInfo) {
        return R.ok(addressInfoService.selectAddressPage(page, addressInfo));
    }

    /**
     * 查询用户收货地址信息详情
     *
     * @param id 主键ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(addressInfoService.getById(id));
    }

    /**
     * 查询用户收货地址信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(addressInfoService.list());
    }

    /**
     * 新增用户收货地址信息
     *
     * @param addressInfo 用户收货地址信息
     * @return 结果
     */
    @PostMapping
    public R save(AddressInfo addressInfo) {
        // 地址编号
        addressInfo.setCode("ARS-" + System.currentTimeMillis());
        // 创建时间
        addressInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        // 获取用户信息
        UserInfo user = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, addressInfo.getUserId()));
        if (user != null) {
            addressInfo.setUserId(user.getId());
        }
        // 默认地址处理
        if ("1".equals(addressInfo.getDefaultFlag())) {
            addressInfoService.update(Wrappers.<AddressInfo>lambdaUpdate().set(AddressInfo::getDefaultFlag, "0").eq(AddressInfo::getUserId, addressInfo.getUserId()));
        }
        return R.ok(addressInfoService.save(addressInfo));
    }

    /**
     * 修改用户收货地址信息
     *
     * @param addressInfo 用户收货地址信息
     * @return 结果
     */
    @PutMapping
    public R edit(AddressInfo addressInfo) {// 默认地址处理
        if ("1".equals(addressInfo.getDefaultFlag())) {
            addressInfoService.update(Wrappers.<AddressInfo>lambdaUpdate().set(AddressInfo::getDefaultFlag, "0").eq(AddressInfo::getUserId, addressInfo.getUserId()));
        }
        return R.ok(addressInfoService.updateById(addressInfo));
    }

    /**
     * 删除用户收货地址信息
     *
     * @param ids ids
     * @return 用户收货地址信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(addressInfoService.removeByIds(ids));
    }
}
