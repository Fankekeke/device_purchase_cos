package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.AlertConfigurationInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 预警配置 service层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface IAlertConfigurationInfoService extends IService<AlertConfigurationInfo> {

    /**
     * 分页获取报警配置
     *
     * @param page                   分页对象
     * @param alertConfigurationInfo 报警配置
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectAlertConfigurationPage(Page<AlertConfigurationInfo> page, AlertConfigurationInfo alertConfigurationInfo);

    /**
     * 添加默认预警配置
     *
     * @param typeId 类型ID
     * @return 结果
     */
    boolean addDefaultConfiguration(Integer typeId);
}
