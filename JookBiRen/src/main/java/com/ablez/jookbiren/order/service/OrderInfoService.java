package com.ablez.jookbiren.order.service;

import com.ablez.jookbiren.order.dto.OrderInfoDto.PostOrderInfoDto;
import com.ablez.jookbiren.order.entity.OrderInfo;
import com.ablez.jookbiren.order.repository.OrderInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderInfoService {
    private final OrderInfoRepository orderInfoRepository;

    public OrderInfo insertOrderInfo(PostOrderInfoDto orderInformation) {
        return orderInfoRepository.save(OrderInfo.postOrderInfoDtoToOrderInfo(orderInformation));
    }

    public OrderInfo makeOrderInfo(PostOrderInfoDto orderInformation) {
        return OrderInfo.postOrderInfoDtoToOrderInfo(orderInformation);
    }
}
