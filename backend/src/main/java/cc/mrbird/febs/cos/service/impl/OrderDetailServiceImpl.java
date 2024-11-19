package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OrderDetail;
import cc.mrbird.febs.cos.dao.OrderDetailMapper;
import cc.mrbird.febs.cos.service.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 订单详情 实现层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
