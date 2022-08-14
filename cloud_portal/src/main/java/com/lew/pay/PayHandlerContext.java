package com.lew.pay;

import com.lew.pay.annotation.PayTypeAnnotation;
import com.lew.pay.constant.PayType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Slf4j
@Component
public class PayHandlerContext implements ApplicationListener<ContextRefreshedEvent> {
    private static Map<PayType, PayHandler> payHandlerMap = new EnumMap<>(PayType.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 根容器为Spring容器
        if (event.getApplicationContext().getParent() == null) {
            Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(PayTypeAnnotation.class);
            for (Object bean : beans.values()) {
                if (bean instanceof PayHandler && bean.getClass().isAnnotationPresent(PayTypeAnnotation.class)) {
                    PayTypeAnnotation payTypeAnnotation = bean.getClass().getAnnotation(PayTypeAnnotation.class);
                    payHandlerMap.put(payTypeAnnotation.payType(), (PayHandler) bean);
                }
            }
            log.info("beans: {}", beans);
            log.info("payHandlerMap: {}", payHandlerMap);
        }
    }

    public static PayHandler getPayHandler(PayType payType) {
        if (!payHandlerMap.containsKey(payType)) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "pay type [%s] is not supported", payType));
        }
        return payHandlerMap.get(payType);
    }
}
