package guru.springframework.events;

import guru.springframework.web.model.BeerDto;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BeerEvent implements Serializable {

    static final long serialVersionUID = -102360743692843045L;

    private BeerDto beerDto;

}
