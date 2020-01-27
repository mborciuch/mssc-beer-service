package guru.springframework.service.brewing;

import guru.springframework.config.JmsConfiguration;
import guru.springframework.domain.Beer;
import guru.springframework.events.BrewBeerEvent;
import guru.springframework.events.NewInventoryEvent;
import guru.springframework.repositories.BeerRepository;
import guru.springframework.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;


    @JmsListener(destination = JmsConfiguration.BREWING_REQUEST_QUEUE)
    @Transactional
    public void listen(BrewBeerEvent brewBeerEvent){
        BeerDto beerDto =  brewBeerEvent.getBeerDto();

        Beer beer = beerRepository.getOne(beerDto.getId());

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent inventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewed beer " + beer.getMinOnHand() + ": QuantityOnHand " + beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfiguration.NEW_INVENTORY_QUEUE, inventoryEvent);

    }

}
