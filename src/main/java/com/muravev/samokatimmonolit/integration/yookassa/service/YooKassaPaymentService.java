package com.muravev.samokatimmonolit.integration.yookassa.service;

import com.muravev.samokatimmonolit.entity.UserEntity;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentItem;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface YooKassaPaymentService {

    Payment hold(String orderId,
                 BigDecimal price,
                 String description,
                 List<PaymentItem> items,
                 String returnUrl,
                 UserEntity customer);

    Payment debit(String paymentId);

    Payment cancel(String paymentId);

    Payment getPaymentById(String id);
}
