package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.StoreRecordInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 库房记录 mapper层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface StoreRecordInfoMapper extends BaseMapper<StoreRecordInfo> {

    /**
     * 分页获取库房记录信息
     *
     * @param page            分页对象
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStoreRecordPage(Page<StoreRecordInfo> page, @Param("storeRecordInfo") StoreRecordInfo storeRecordInfo);

    /**
     * 分页获取库房库存
     *
     * @param page            分页对象
     * @param storeRecordInfo 库房记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStorePage(Page<StoreRecordInfo> page, @Param("storeRecordInfo") StoreRecordInfo storeRecordInfo);

    /**
     * 根据年月获取商品出入库信息
     *
     * @param year  年份
     * @param month 月份
     * @param type  类型
     * @return 结果
     */
    List<StoreRecordInfo> selectRecordBySlot(@Param("year") Integer year, @Param("month") Integer month, @Param("type") String type);
}
