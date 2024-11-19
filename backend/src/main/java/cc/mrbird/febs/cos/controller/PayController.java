package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.AlipayBean;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IMailService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cc.mrbird.febs.cos.service.PayService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 支付宝支付接口 控制层
 *
 * @author FanK wechat Storm_Berserker email fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/pay")
public class PayController {

    @Resource
    private PayService payService;

    @Resource
    private IOrderInfoService orderInfoService;

    @Resource
    private IUserInfoService userInfoService;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private IMailService mailService;

    /**
     * 支付宝支付回调
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping(value = "/rollback")
    public R alipayRollback(String orderCode) {

        // 获取订单信息
        OrderInfo orderInfo = null;
        if (orderCode.contains("OD")) {
            orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        }
        if (orderCode.contains("OWE")) {
            orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getOweCode, orderCode));
        }
        if (orderInfo == null) {
            return R.ok(false);
        }
        // 获取用户信息
        UserInfo user = userInfoService.getById(orderInfo.getUserId());

        String typeStr = "";
        if ("0".equals(orderInfo.getType())) {
            orderInfo.setPayDate(DateUtil.formatDateTime(new Date()));
            orderInfo.setStatus("3");
            user.setPrice(NumberUtil.add(user.getPrice(), orderInfo.getTotalPrice()));
        } else if ("1".equals(orderInfo.getType()) && "0".equals(orderInfo.getStatus())) {
            orderInfo.setPayDate(DateUtil.formatDateTime(new Date()));
            orderInfo.setStatus("1");
            user.setPrice(NumberUtil.add(user.getPrice(), orderInfo.getSubsistPrice()));
            typeStr = "预付款";
        } else if ("1".equals(orderInfo.getType()) && StrUtil.isEmpty(orderInfo.getOweDate())) {
            orderInfo.setOweDate(DateUtil.formatDateTime(new Date()));
            orderInfo.setStatus("3");
            user.setPrice(NumberUtil.add(user.getPrice(), orderInfo.getOwePrice()));
            typeStr = "尾款";
        }
        // 发送邮件
        if (StrUtil.isNotEmpty(user.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", user.getName() + "，您好。您的订单 " + orderInfo.getCode() + " " + typeStr + "已缴纳，请注意查看");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(user.getEmail(), DateUtil.formatDate(new Date()) + "订单缴费通知", emailContent);
        }
        Context context = new Context();
        context.setVariable("today", DateUtil.formatDate(new Date()));
        context.setVariable("custom", "用户：" + user.getName() + "，订单 " + orderInfo.getCode() + " " + typeStr + "已缴纳，请注意查看");
        String emailContent = templateEngine.process("registerEmail", context);
        mailService.sendHtmlMail("fan1ke2ke@gmail.com", DateUtil.formatDate(new Date()) + "订单缴费通知-管理员", emailContent);
        userInfoService.updateById(user);
        return R.ok(orderInfoService.updateById(orderInfo));
    }

    /**
     * 阿里支付
     *
     * @param outTradeNo  订单编号
     * @param subject     备注
     * @param totalAmount 付款金额
     * @param body        主体
     * @return 结果
     * @throws AlipayApiException 异常信息
     */
    @PostMapping(value = "/alipay")
    public R alipay(String outTradeNo, String subject, String totalAmount, String body) throws AlipayApiException {
        AlipayBean alipayBean = new AlipayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String result = payService.aliPay(alipayBean);
        return R.ok(result);
    }

}
