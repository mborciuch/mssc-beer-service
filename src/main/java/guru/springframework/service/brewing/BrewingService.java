package guru.springframework.service.brewing;

import guru.sfg.brewery.model.events.BrewBeerEvent;
import guru.springframework.config.JmsConfiguration;
import guru.springframework.domain.Beer;
import guru.springframework.repositories.BeerRepository;
import guru.springframework.service.BeerInventoryService;
import guru.springframework.web.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrewingService {

    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory() {
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            Integer inventoryQualityOnHand = beerInventoryService.getBeerInventory(beer.getId());
            log.debug("Min onHand is: " + beer.getMinOnHand());
            log.debug("Inventory is: " + inventoryQualityOnHand);

            if (beer.getMinOnHand() >= inventoryQualityOnHand) {
                jmsTemplate.convertAndSend(JmsConfiguration.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
