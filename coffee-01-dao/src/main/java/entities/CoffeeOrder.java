package entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class CoffeeOrder {
    private int id;
    private Timestamp orderDate;
    private String customerName;
    private String deliveryAddress;
    private double cost;
}
