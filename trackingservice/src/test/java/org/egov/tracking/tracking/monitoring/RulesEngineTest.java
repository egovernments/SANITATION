package org.egov.tracking.tracking.monitoring;

import org.egov.tracking.monitoring.RuleEngine;
import org.egov.tracking.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TripProgress;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(TripService.class)
public class RulesEngineTest {

    TripProgress tripProgress = new TripProgress();
    @BeforeEach
    void setUp() {
        //Set initial values for TripProgress test data
        tripProgress.setId("691ad062-a70c-4018-ad59-d92465b4aeaf");
        tripProgress.setTripId("11ae890f-c3ec-49d7-94f1-cbbdc7f35d87");
        //tripProgress.setProgressData();
    }


    @Test
    public void testLoadData(){
        RuleEngine re = new RuleEngine();
        //re.executeSingleRuleMethod(RULE_LOAD_METHOD, "691ad062-a70c-4018-ad59-d92465b4aeaf");
    }

    @Test
    public void testLoadDataAndRules(){
        RuleEngine re = new RuleEngine();
        //re.executeAllRules();
    }

}
