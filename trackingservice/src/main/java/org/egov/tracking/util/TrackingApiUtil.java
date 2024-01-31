package org.egov.tracking.util;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.NativeWebRequest;

@Slf4j
public class TrackingApiUtil {

    public static void setResponse(NativeWebRequest req, String jsonData) {
        log.info("## setResponse is creating the response message");
        String contentType = "application/json";
        try {
            HttpServletResponse res = req.getNativeResponse(HttpServletResponse.class);
            res.setCharacterEncoding("UTF-8");
            res.addHeader("Content-Type", contentType);
            res.getWriter().print(jsonData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
