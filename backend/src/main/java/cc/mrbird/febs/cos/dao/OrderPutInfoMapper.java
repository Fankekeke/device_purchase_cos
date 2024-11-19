package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderOutInfo;
import cc.mrbird.febs.cos.entity.OrderPutInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 库房入库 mapper层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface OrderPutInfoMapper extends BaseMapper<OrderPutInfo> {

    /**
     * 分页获取库房入库信息
     *
     * @param page         分页对象
     * @param orderPutInfo 库房入库信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPutPage(Page<OrderPutInfo> page, @Param("orderPutInfo") OrderPutInfo orderPutInfo);

    /**
     * 根据时间获取入库信息
     *
     * @param year  年份
     * @param month 月份
     * @return 结果
     */
    List<OrderPutInfo> selectOrderPutListByDate(@Param("year") Integer year, @Param("month") Integer month);
}
