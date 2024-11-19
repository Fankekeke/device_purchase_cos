package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.StockAlertInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 库存预警 service层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface IStockAlertInfoService extends IService<StockAlertInfo> {

    /**
     * 分页获取库存预警信息
     *
     * @param page           分页对象
     * @param stockAlertInfo 库存预警信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStockAlertPage(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo);

    /**
     * 生成库存预警
     *
     * @return 结果
     */
    boolean generateStoreAlert();
}
