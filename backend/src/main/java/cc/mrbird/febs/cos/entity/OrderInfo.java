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
 * 订单管理
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 尾款订单编号
     */
    private String oweCode;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 订单类型（0.正常订单 1.预付款）
     */
    private String type;

    /**
     * 预付款金额
     */
    private BigDecimal subsistPrice;

    /**
     * 欠款金额
     */
    private BigDecimal owePrice;

    /**
     * 订单状态（0.待支付 1.预付款已缴 2.尾款已缴 3.已支付 4.已出库 5.已签收）
     */
    private String status;

    /**
     * 收货地址
     */
    private Integer addressId;

    /**
     * 下单时间
     */
    private String createDate;

    /**
     * 支付时间
     */
    private String payDate;

    /**
     * 尾款支付时间
     */
    private String oweDate;

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
     * 订单详情
     */
    @TableField(exist = false)
    private String orderDetail;
}
