package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 配送物流信息
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LogisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 物流备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 是否为当前物流（0.否 1.是）
     */
    private Integer currentLogistics;

    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String code;

    /**
     * 所属用户
     */
    @TableField(exist = false)
    private String name;

    /**
     * 详细地址
     */
    @TableField(exist = false)
    private String address;

    /**
     * 联系人
     */
    @TableField(exist = false)
    private String contact;

    /**
     * 所属用户
     */
    @TableField(exist = false)
    private Integer userId;
}
