package guru.springframework.service;

import guru.springframework.web.model.BeerDto;
import guru.springframework.web.model.BeerPagedList;
import guru.springframework.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {

    BeerDto getById(UUID beerId, boolean withInventory);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getByUpc(String upc);

    BeerPagedList beerList(String beerName, BeerStyleEnum beerStyle, boolean withInventory, PageRequest pageRequest);
}
