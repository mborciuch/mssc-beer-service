package guru.springframework.events;

import guru.springframework.web.model.BeerDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {

    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
