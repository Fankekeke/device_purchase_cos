package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.StoreRecordInfo;
import cc.mrbird.febs.cos.dao.StoreRecordInfoMapper;
import cc.mrbird.febs.cos.service.IStoreRecordInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 库房记录 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
public class StoreRecordInfoServiceImpl extends ServiceImpl<StoreRecordInfoMapper, StoreRecordInfo> implements IStoreRecordInfoService {

    /**
     * 分页获取库房记录信息
     *
     * @param page            分页对象
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStoreRecordPage(Page<StoreRecordInfo> page, StoreRecordInfo storeRecordInfo) {
        return baseMapper.selectStoreRecordPage(page, storeRecordInfo);
    }

    /**
     * 分页获取库房库存
     *
     * @param page            分页对象
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStorePage(Page<StoreRecordInfo> page, StoreRecordInfo storeRecordInfo) {
        return baseMapper.selectStorePage(page, storeRecordInfo);
    }

    /**
     * 根据商品编号获取出库入库记录
     *
     * @param commodityCode 商品编号
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOutInDetail(String commodityCode) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("outData", Collections.emptyList());
                put("putData", Collections.emptyList());
            }
        };

        // 出库记录
        List<StoreRecordInfo> outData = this.list(Wrappers.<StoreRecordInfo>lambdaQuery().eq(StoreRecordInfo::getType, "2").eq(StoreRecordInfo::getCommodityCode, commodityCode));
        // 入库记录
        List<StoreRecordInfo> putData = this.list(Wrappers.<StoreRecordInfo>lambdaQuery().eq(StoreRecordInfo::getType, "1").eq(StoreRecordInfo::getCommodityCode, commodityCode));
        result.put("outData", outData);
        result.put("putData", putData);
        return result;
    }
}
