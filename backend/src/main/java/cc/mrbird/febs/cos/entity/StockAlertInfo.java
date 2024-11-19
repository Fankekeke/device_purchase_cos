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
 * 库存预警
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StockAlertInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品ID
     */
    private Integer commodityId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 已读状态（0.未处理 1.已处理）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 当前库存
     */
    private Integer currentNum;

    /**
     * 补货数量
     */
    private Integer putNum;

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
}
