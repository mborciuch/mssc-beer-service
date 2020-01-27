package guru.springframework.service;

import java.util.UUID;

public interface BeerInventoryService {

    Integer getBeerInventory(UUID beerId);

}
