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
 * 商品管理
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommodityInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 型号
     */
    private String model;

    /**
     * 商品类型
     */
    private Integer typeId;

    /**
     * 单位
     */
    private String unit;

    /**
     * 采购价
     */
    private BigDecimal purchasePrice;

    /**
     * 售价
     */
    private BigDecimal sellPrice;

    /**
     * 商品图片
     */
    private String images;

    /**
     * 商品备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 库房库存
     */
    @TableField(exist = false)
    private Integer reserve;

    /**
     * 商品类型
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 用户账户ID
     */
    @TableField(exist = false)
    private Integer userId;
}
