package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.AlertConfigurationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 预警配置 mapper层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface AlertConfigurationInfoMapper extends BaseMapper<AlertConfigurationInfo> {

    /**
     * 分页获取报警配置
     *
     * @param page         分页对象
     * @param alertConfigurationInfo 报警配置
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectAlertConfigurationPage(Page<AlertConfigurationInfo> page, @Param("alertConfigurationInfo") AlertConfigurationInfo alertConfigurationInfo);
}
