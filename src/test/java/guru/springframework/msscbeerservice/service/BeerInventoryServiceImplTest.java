package guru.springframework.msscbeerservice.service;

import guru.springframework.msscbeerservice.bootstrap.BeerLoader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
class BeerInventoryServiceImplTest {

    @Autowired
    BeerInventoryService beerInventoryService;

    @Test
    void getBeerInventory() {
        Integer quantityOnHand = beerInventoryService.getBeerInventory(BeerLoader.BEER_1_UUID);

        System.out.println(quantityOnHand);
    }
}