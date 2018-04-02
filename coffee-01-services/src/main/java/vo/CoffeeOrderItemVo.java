package vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoffeeOrderItemVo {
    private int coffeeTypeId;
    private String coffeeTypeName;
    private double coffeePrice;
    private int quantity;
}
