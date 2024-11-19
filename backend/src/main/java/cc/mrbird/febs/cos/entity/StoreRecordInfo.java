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
 * 库房记录
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StoreRecordInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号
     */
    private String commodityCode;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 类型（0.库存 1.入库 2.出库）
     */
    private String type;

    /**
     * 出库或入库单号
     */
    private String orderNumber;

    /**
     * 出库或入库价格
     */
    private BigDecimal price;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 商品型号
     */
    @TableField(exist = false)
    private String model;

    /**
     * 商品类型ID
     */
    @TableField(exist = false)
    private Integer typeId;

    /**
     * 商品类型
     */
    @TableField(exist = false)
    private String typeName;

    /**
     * 商品图片
     */
    @TableField(exist = false)
    private String images;
}
