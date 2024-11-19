package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户管理 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    private final OrderInfoMapper orderInfoMapper;

    private final ILogisticsInfoService logisticsInfoService;

    private final IAddressInfoService addressInfoService;

    private final IBulletinInfoService bulletinInfoService;

    /**
     * 分页获取用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectUserPage(Page<UserInfo> page, UserInfo userInfo) {
        return baseMapper.selectUserPage(page, userInfo);
    }

    /**
     * 获取用户详情
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectUserDetail(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user,", null);
                put("order", Collections.emptyList());
            }
        };
        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return result;
        }

        result.put("user", userInfo);
        // 用户订单信息
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserId, userInfo.getId()));
        if (CollectionUtil.isEmpty(orderInfoList)) {
            return result;
        }

        // 获取未收货订单状态
        List<OrderInfo> notCheckOrder = orderInfoList.stream().filter(e -> "4".equals(e.getStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(notCheckOrder)) {
            return result;
        }

        // 获取物流信息
        List<Integer> orderIds = notCheckOrder.stream().map(OrderInfo::getId).collect(Collectors.toList());
        List<LogisticsInfo> logisticsList = logisticsInfoService.list(Wrappers.<LogisticsInfo>lambdaQuery().eq(LogisticsInfo::getCurrentLogistics, "1")
                .in(LogisticsInfo::getOrderId, orderIds));
        Map<Integer, String> logisticsMap = logisticsList.stream().collect(Collectors.toMap(LogisticsInfo::getOrderId, LogisticsInfo::getRemark));

        for (OrderInfo orderInfo : notCheckOrder) {
            if (StrUtil.isEmpty(logisticsMap.get(orderInfo.getId()))) {
                continue;
            }
            orderInfo.setAddress(logisticsMap.get(orderInfo.getId()));
        }
        result.put("order", notCheckOrder);
        return result;
    }

    /**
     * 查询用户信息详情【用户地址】
     *
     * @param userId 主键ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectAddressDetail(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", null);
                put("default", null);
                put("address", Collections.emptyList());
            }
        };
        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return result;
        }
        result.put("user", userInfo);

        // 用户地址
        List<AddressInfo> addressInfoList = addressInfoService.list(Wrappers.<AddressInfo>lambdaQuery().eq(AddressInfo::getUserId, userInfo.getId()));
        result.put("address", addressInfoList);
        result.put("default", CollectionUtil.isEmpty(addressInfoList) ? null : addressInfoList.stream().filter(e -> "1".equals(e.getDefaultFlag())).collect(Collectors.toList()).get(0));
        return result;
    }

    /**
     * 查询用户信息详情【公告信息】
     *
     * @param userId 主键ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectBulletinDetail(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("user", null);
                put("bulletin", Collections.emptyList());
            }
        };
        UserInfo userInfo = this.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return result;
        }
        result.put("user", userInfo);

        // 公告信息
        List<BulletinInfo> bulletinInfoList = bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, "1"));
        result.put("bulletin", bulletinInfoList);
        return result;
    }
}
