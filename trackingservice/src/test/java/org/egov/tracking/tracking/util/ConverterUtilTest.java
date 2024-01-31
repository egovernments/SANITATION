package org.egov.tracking.tracking.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.egov.tracking.util.ConverterUtil;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openapitools.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterUtilTest {
    Logger logger = LoggerFactory.getLogger(ConverterUtilTest.class);

    @Test
    public void testAddressToString() {
        Address address;

        //Test 1
        address = new Address();
        address.setCity("Test city");
        address.setPincode("567123");
        address.setDistrict("null");
        Assertions.assertEquals("Test city, 567123", ConverterUtil.addressToString(address));

        //Test 2
        address = new Address();
        address.setPlotNo("113");
        address.setCity("Bangalore");
        assertEquals("113, Bangalore", ConverterUtil.addressToString(address));
    }
}
