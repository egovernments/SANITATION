package org.egov.tracking.tracking.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.egov.tracking.controller.PoiController;
import org.egov.tracking.service.POIService;
import org.junit.jupiter.api.Test;
import org.openapitools.model.POI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PoiController.class)
class PoiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private POIService service;

    @Test
    void getPOIShouldReturnRouteList() throws Exception {
        //Set test data
        List<POI> entityList= new ArrayList();
        POI entity = new POI();
        entity.setUserId("TestUser");
        entity.setStatus(POI.StatusEnum.ACTIVE);
        entity.setLocationName("Location name");
        entity.setId("PoiUUID");
        entityList.add(entity);

        //Mock the service call
        when(service.getPOIsById("TestId")).thenReturn(entityList);
        //Perform assertion
        this.mockMvc.perform(get("/api/v3/poi/TestId")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("TestUser")));
    }

}
