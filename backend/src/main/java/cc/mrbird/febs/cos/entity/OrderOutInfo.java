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
 * 库房出库
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderOutInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 出库单号
     */
    private String code;

    /**
     * 出库单名称
     */
    private String name;

    /**
     * 出库人
     */
    private String putUser;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 出库用户
     */
    private Integer userId;

    /**
     * 出库地址
     */
    private Integer addressId;

    /**
     * 出库时间
     */
    private String createDate;

    /**
     * 所属订单
     */
    private Integer orderId;

    /**
     * 库房记录
     */
    @TableField(exist = false)
    private String storeRecord;

    /**
     * 用户名称
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 年份
     */
    @TableField(exist = false)
    private Integer year;

    /**
     * 月份
     */
    @TableField(exist = false)
    private Integer month;

    /**
     * 天
     */
    @TableField(exist = false)
    private Integer day;
}
