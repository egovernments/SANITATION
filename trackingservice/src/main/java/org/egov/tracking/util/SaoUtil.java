package org.egov.tracking.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Slf4j
public class SaoUtil {

    public static HttpEntity<Map<String, Object>> getMapHttpEntity(String authToken, Map<String, Object> additionalData) {
        log.info("## Invoked getMapHttpEntity ");
        log.info("## DEBUG - Auth Token Present: {}, Token Length: {}", (authToken != null), (authToken != null ? authToken.length() : 0));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create userInfo with empty roles list to avoid NullPointerException
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("roles", new ArrayList<>());

        Map<String, Object> mapInner = new HashMap<>();
        mapInner.put("apiId", "Rainmaker");
        if (authToken != null && !authToken.trim().isEmpty()) {
            mapInner.put("authToken", authToken);
            log.info("## DEBUG - Auth token added to RequestInfo");
        } else {
            log.warn("## WARNING - Auth token is null or empty - downstream API may fail with 401");
        }
        mapInner.put("msgId", "1694796531963|en_IN");
        mapInner.put("userInfo", userInfo);

        Map<String, Object> mapOuter = new HashMap<>();
        mapOuter.put("RequestInfo", mapInner);
        //In case additional data payload map is provided, add it to the request body
        if(additionalData != null){
            mapOuter.putAll(additionalData);
            log.info("## DEBUG - Additional data added to request body");
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapOuter, headers);
        return entity;
    }

}
