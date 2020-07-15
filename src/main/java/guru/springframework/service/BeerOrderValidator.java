package guru.springframework.service;

import guru.sfg.brewery.model.events.BeerOrderDto;
import guru.sfg.brewery.model.events.ValidateBeerOrderRequest;
import guru.sfg.brewery.model.events.ValidateBeerOrderResponse;
import guru.springframework.config.JmsConfiguration;
import guru.springframework.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeerOrderValidator {

    private final JmsTemplate jmsTemplate;
    private final BeerRepository beerRepository;

    @JmsListener(destination = JmsConfiguration.VALIDATE_ORDER_QUEUE)
    void listen(@Payload ValidateBeerOrderRequest beerOrderRequest){
        BeerOrderDto beerOrderDto = beerOrderRequest.getBeerOrderDto();
        boolean isValid =  beerOrderDto.getBeerOrderLines().stream()
                .allMatch(((beerOrderLineDto -> beerRepository.findByUpc(beerOrderLineDto.getUpc()).isPresent())));
        ValidateBeerOrderResponse validateBeerOrderResponse = ValidateBeerOrderResponse.builder()
                .beerOrderId(beerOrderDto.getId())
                .isValid(isValid)
                .build();
        jmsTemplate.convertAndSend(JmsConfiguration.VALIDATE_ORDER_RESPONSE_QUEUE, validateBeerOrderResponse);
    }

}
