package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * 用户管理 service层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
public interface IUserInfoService extends IService<UserInfo> {

    /**
     * 分页获取用户信息
     *
     * @param page     分页对象
     * @param userInfo 用户信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectUserPage(Page<UserInfo> page, UserInfo userInfo);

    /**
     * 获取用户详情
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectUserDetail(Integer userId);

    /**
     * 查询用户信息详情【用户地址】
     *
     * @param userId 主键ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectAddressDetail(Integer userId);

    /**
     * 查询用户信息详情【公告信息】
     *
     * @param userId 主键ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectBulletinDetail(Integer userId);
}
