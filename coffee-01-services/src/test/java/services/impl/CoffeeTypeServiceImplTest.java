package services.impl;


import entities.CoffeeType;
import entities.enums.DisabledFlag;
import org.junit.Assert;
import org.junit.Test;
import services.CoffeeTypeService;
import utils.SingletonBuilder;

import java.util.List;

public class CoffeeTypeServiceImplTest extends Assert {
    private CoffeeTypeService service = SingletonBuilder.getInstanceImpl(CoffeeTypeService.class);

    @Test
    public void crudGetAll() {
        CoffeeType saved;
        CoffeeType getIt;
        CoffeeType updated;

        CoffeeType newOneForSave = new CoffeeType();
        newOneForSave.setTypeName("Very fragrant coffee");
        newOneForSave.setPrice(1.0);
        newOneForSave.setDisabled(DisabledFlag.Y);

        CoffeeType newOneForUpdate = new CoffeeType();
        newOneForUpdate.setTypeName("Black coffee with cream");
        newOneForUpdate.setPrice(3.0);
        newOneForUpdate.setDisabled(DisabledFlag.N);

        // check getAll before save CoffeeType in DB
        List<CoffeeType> list1 = service.getAll();

        // save CoffeeType in DB
        assertEquals(0, newOneForSave.getId());
        saved = service.add(newOneForSave);
        assertNotNull(saved);
        assertNotEquals(0, saved.getId());

        // getAll check after save CoffeeType in DB
        List<CoffeeType> list2 = service.getAll();
        assertEquals(1, list2.size() - list1.size());

        // get CoffeeType from DB
        getIt = service.get(saved.getId());
        assertNotNull(getIt);
        assertEquals(saved, getIt);

        // update CoffeeType in DB
        newOneForUpdate.setId(saved.getId());
        service.update(newOneForUpdate);
        getIt = service.get(newOneForUpdate.getId());
        assertNotNull(getIt);
        assertNotEquals(getIt, saved);
        assertEquals(newOneForUpdate, getIt);

        // delete CoffeeType from DB
        int deletedRecords = service.delete(saved.getId());
        assertEquals(1, deletedRecords);
        getIt = service.get(saved.getId());
        assertNull(getIt);
    }

    @Test
    public void getAllForDisabledFlag() {
        CoffeeType newOneForSave = new CoffeeType();
        newOneForSave.setTypeName("Very fragrant coffee");
        newOneForSave.setPrice(1.0);
        newOneForSave.setDisabled(DisabledFlag.Y);

        CoffeeType anotherOneForSave = new CoffeeType();
        anotherOneForSave.setTypeName("Black coffee with cream");
        anotherOneForSave.setPrice(3.0);
        anotherOneForSave.setDisabled(DisabledFlag.N);

        // check getAllForDisabledFlag before save CoffeeType in DB
        List<CoffeeType> list1 = service.getAllForDisabledFlag(DisabledFlag.Y);
        List<CoffeeType> listAll1 = service.getAll();

        // save CoffeeType in DB where DisabledFlag.N & DisabledFlag.Y
        CoffeeType savedWhereFlagY = service.add(newOneForSave);
        assertNotNull(savedWhereFlagY);
        CoffeeType savedWhereFlagN = service.add(anotherOneForSave);
        assertNotNull(savedWhereFlagN);

        // getAllForDisabledFlag check after save CoffeeType in DB
        List<CoffeeType> list2 = service.getAllForDisabledFlag(DisabledFlag.Y);
        assertEquals(1, list2.size() - list1.size());
        List<CoffeeType> listAll2 = service.getAll();
        assertEquals(2, listAll2.size() - listAll1.size());

        // delete saved entities
        int deletedRecords = service.delete(savedWhereFlagY.getId());
        assertEquals(1, deletedRecords);
        CoffeeType getIt = service.get(savedWhereFlagY.getId());
        assertNull(getIt);

        deletedRecords = service.delete(savedWhereFlagN.getId());
        assertEquals(1, deletedRecords);
        getIt = service.get(savedWhereFlagN.getId());
        assertNull(getIt);
    }
}
