package guru.springframework.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class BeerInventoryServiceImplTest {

    @Autowired
    BeerInventoryService beerInventoryService;

    @Test
    void getBeerInventory() {
//        Integer quantityOnHand = beerInventoryService.getBeerInventory(BeerLoader.BEER_1_UUID);
//
//        System.out.println(quantityOnHand);
    }
}