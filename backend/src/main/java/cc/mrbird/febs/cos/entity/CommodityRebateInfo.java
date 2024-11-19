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
 * 商品折扣设置
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommodityRebateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 商品ID
     */
    private Integer commodityId;

    /**
     * 底价折扣
     */
    private BigDecimal lowRate;

    /**
     * 正常价格
     */
    private BigDecimal normalPrice;

    /**
     * 特价折扣
     */
    private BigDecimal specialPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 底价限制（月）
     */
    private BigDecimal lowRestriction;

    /**
     * 特价限制（月）
     */
    private BigDecimal specialRestriction;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 商品类型
     */
    @TableField(exist = false)
    private String typeName;
}
