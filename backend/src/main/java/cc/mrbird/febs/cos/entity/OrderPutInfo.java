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
 * 库房入库
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderPutInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 入库单号
     */
    private String code;

    /**
     * 入库单名称
     */
    private String name;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 入库人
     */
    private String putUser;

    /**
     * 入库单备注
     */
    private String remark;

    /**
     * 入库时间
     */
    private String createDate;

    /**
     * 库房记录
     */
    @TableField(exist = false)
    private String storeRecord;

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
