package utils;

import dao.CoffeeTypeDao;
import entities.CoffeeType;
import org.junit.Assert;
import org.junit.Test;

public class SingletonBuilderTest extends Assert  {

    @Test
    public void getInstanceImpl() {
        CoffeeTypeDao coffeeTypeDao1 =  SingletonBuilder.getInstanceImpl(CoffeeTypeDao.class);
        CoffeeTypeDao coffeeTypeDao2 =  SingletonBuilder.getInstanceImpl(CoffeeTypeDao.class);
        assertEquals(coffeeTypeDao1, coffeeTypeDao2);
        assertNotNull(coffeeTypeDao1);
        assertNotNull(coffeeTypeDao2);
    }

    @Test (expected = SingletonException.class)
    public void getInstanceImplNullExeption() {

        SingletonBuilder.getInstanceImpl(null);
    }

    @Test (expected = SingletonException.class)
    public void getInstanceImplNoImplementation() {

        SingletonBuilder.getInstanceImpl(CoffeeType.class);
    }
}
