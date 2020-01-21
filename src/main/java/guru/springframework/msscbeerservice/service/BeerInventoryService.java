package guru.springframework.msscbeerservice.service;

import java.util.UUID;

public interface BeerInventoryService {

    Integer getBeerInventory(UUID beerId);

}
