package guru.springframework.service;

import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerPagedList;
import guru.sfg.brewery.model.BeerStyleEnum;
import guru.springframework.domain.Beer;
import guru.springframework.repositories.BeerRepository;
import guru.springframework.web.controller.NotFoundException;
import guru.springframework.web.mapper.BeerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getById(UUID beerId, boolean withInventory) {
        if (withInventory) {
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new)
            );
        } else {
            return beerMapper.beerToBeerDto(
                    beerRepository.findById(beerId).orElseThrow(NotFoundException::new)
            );
        }

    }

    @Override
    public BeerDto getByUpc(String upc) {
        return beerMapper.beerToBeerDto(
                beerRepository.findByUpc(upc).orElseThrow(NotFoundException::new)
        );
    }

    @Override
    public BeerPagedList beerList(String beerName, BeerStyleEnum beerStyle, boolean withInventory, PageRequest pageRequest) {
        BeerPagedList beerPagedList;
        Page<Beer> pageBeer;

        if(!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)){
            pageBeer = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if(!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle )) {
            pageBeer = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if(StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle )) {
            pageBeer = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            pageBeer = beerRepository.findAll(pageRequest);
        }

        Function<Beer, BeerDto> mappingFunction;
        if (withInventory) {
            mappingFunction = beerMapper::beerToBeerDtoWithInventory;
        } else {
            mappingFunction = beerMapper::beerToBeerDto;
        }

        beerPagedList = new BeerPagedList(pageBeer
                .getContent()
                .stream()
                .map(mappingFunction)
                .collect(Collectors.toList()),
                PageRequest
                    .of(pageBeer.getPageable().getPageNumber(),
                            pageBeer.getPageable().getPageSize()),
                pageBeer.getTotalElements());

        return beerPagedList;
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

}
