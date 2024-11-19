package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.AddressInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 用户收货地址 mapper层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface AddressInfoMapper extends BaseMapper<AddressInfo> {

    /**
     * 分页获取用户收货地址信息
     *
     * @param page        分页对象
     * @param addressInfo 用户收货地址信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectAddressPage(Page<AddressInfo> page, @Param("addressInfo") AddressInfo addressInfo);
}
