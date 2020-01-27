package guru.springframework.web.controller;

import guru.springframework.service.BeerService;
import guru.springframework.web.model.BeerDto;
import guru.springframework.web.model.BeerPagedList;
import guru.springframework.web.model.BeerStyleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {

    private static final  Integer DEFAULT_PAGE_NUMBER = 0;
    private static final  Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @Autowired
    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping(produces = "application/json")
    @Cacheable(value = "beerListCache", condition = "#withInventory == false ")
    public ResponseEntity<BeerPagedList> listBeer(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "beerName", required = false) String beerName,
                                                  @RequestParam(value = "beerStyle", required = false)BeerStyleEnum beerStyle,
                                                  @RequestParam(value = "withInventory", required = false) Boolean withInventory){

        if(pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if(pageSize == null || pageSize < 0){
            pageSize = DEFAULT_PAGE_SIZE;
        }

        BeerPagedList beerList = beerService.beerList(beerName, beerStyle, withInventory, PageRequest.of(pageNumber, pageSize));
        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    @GetMapping("/{beerId}")
    @Cacheable(value = "beerCache",key = "#beerId",condition = "#withInventory == false ")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value = "withInventory", required = false) Boolean withInventory){
        return new ResponseEntity<>(beerService.getById(beerId,withInventory), HttpStatus.OK);
    }

    @GetMapping(value = "/{beerUpc}", params = "by=upc")
    @Cacheable(value = "beerUpcCache",key = "#beerUpc")
    public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("beerUpc") String beerUpc){
        return new ResponseEntity<>(beerService.getByUpc(beerUpc), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@RequestBody @Valid BeerDto beerDto){
        return new ResponseEntity(beerService.saveNewBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDto beerDto){
        return new ResponseEntity(beerService.updateBeer(beerId, beerDto), HttpStatus.NO_CONTENT);
    }


}
