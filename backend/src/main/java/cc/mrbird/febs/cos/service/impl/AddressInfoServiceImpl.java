package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.AddressInfo;
import cc.mrbird.febs.cos.dao.AddressInfoMapper;
import cc.mrbird.febs.cos.service.IAddressInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * 用户收货地址 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
public class AddressInfoServiceImpl extends ServiceImpl<AddressInfoMapper, AddressInfo> implements IAddressInfoService {

    /**
     * 分页获取用户收货地址信息
     *
     * @param page        分页对象
     * @param addressInfo 用户收货地址信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectAddressPage(Page<AddressInfo> page, AddressInfo addressInfo) {
        return baseMapper.selectAddressPage(page, addressInfo);
    }
}
