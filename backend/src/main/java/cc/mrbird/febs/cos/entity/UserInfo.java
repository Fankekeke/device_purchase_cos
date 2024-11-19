package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户管理
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    private String code;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 类型（1.经销商 2.批发商 3.散客 4.代理商）
     */
    private String type;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 状态（0.未审核 1.审核驳回 2.已审核）
     */
    private String status;

    /**
     * 用户头像
     */
    private String images;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 审核时间
     */
    private String auditDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 所属账户
     */
    private Integer userId;

    /**
     * 总消费金额
     */
    private BigDecimal price;
}
