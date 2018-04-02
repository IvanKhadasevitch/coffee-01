package command.enums;


import command.Controller;
import command.impl.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandType {
    ORDERS("orders/main.jsp", "Orders", "orders.title", new OrdersController()),
    DELIVERY("orders/deliveryOrder.jsp", "Delivery", "orders.title", new OrdersDeliveryController()),
    ORDERS_SHOW("orders/showOrder.jsp", "ShowOrder", "orders.title", new OrdersShowController()),
    COFFEE("coffee/main.jsp", "Coffee", "coffee.title", new CoffeeController()),
    COFFEE_CHANGE("coffee/changeCoffee.jsp", "changeCoffee", "coffee.title", new CoffeeChangeController()),
    COFFEE_ADD("coffee/addCoffee.jsp", "addCoffee", "coffee.title", new CoffeeAddController()),
    CONFIGURATION("configuration/main.jsp", "Configuration", "configuration.title", new ConfigurationController());

    private String pagePath;
    private String pageName;
    private String i18nKey;
    private Controller controller;

    public static CommandType getByPageName(String page) {
        //del after debug
        System.out.println(String.format("Start CommandType.getByPageName page=%s ",page));

        for (CommandType type : CommandType.values()) {
            if (type.pageName.equalsIgnoreCase(page)) {

                //del after debug
                System.out.println("CommandType, return type.pagePath= " + type.pagePath);

                return type;
            }
        }
        // If found nothing, return to the start page:

        //del after debug
        System.out.println("CommandType, found nothing, return to the start page:" + ORDERS);

        return ORDERS;
    }
}
