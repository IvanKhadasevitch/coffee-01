package entities;

import entities.enums.DisabledFlag;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoffeeType {
    private int id;
    private String typeName;
    private double price;
    private DisabledFlag disabled;
}
