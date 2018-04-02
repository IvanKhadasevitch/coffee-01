package entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoffeeOrderItem {
    private int id;
    private int coffeeTypeId;
    private int orderId;
    private int quantity;
}
