package com.cards.zokudo.config;

import com.cards.zokudo.exceptions.BizException;
import com.cards.zokudo.exceptions.ForbiddenException;
import com.cards.zokudo.exceptions.UnAuthorizedException;
import com.cards.zokudo.util.CommonUtil;
import com.cards.zokudo.util.UrlMetaData;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UrlMetaData urlMetaData;

    @Autowired
    @Qualifier(value = "client")
    private Client client;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        try {

            String remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }

            final MultivaluedMap<String, Object> headerMap = new MultivaluedHashMap<>();
            String[] requesturl = CommonUtil.getProgramAndRequestUrl(request);
            headerMap.add("program_url",  requesturl[2]);
            headerMap.add("request_url", requesturl[requesturl.length-1]);
            headerMap.add("Authorization", request.getHeader("Authorization"));
            headerMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            headerMap.add("ipaddress" , remoteAddr);
            Response clientResponse = client.target(urlMetaData.AUTHENTICATE_AND_AUTHORIZE_USER)
                    .request()
                    .headers(headerMap)
                    .get();

            if (clientResponse.getStatus() != 200) {
                String stringResponse = clientResponse.readEntity(String.class);
                JSONObject jsonResponse = new JSONObject(stringResponse);
                String errorMessage = jsonResponse.getString("message") != null ? jsonResponse.getString("message") : "";
                if (clientResponse.getStatus() == 401) throw new ForbiddenException(errorMessage);
                throw new UnAuthorizedException(errorMessage);
            }
        } catch (JSONException e) {
            log.error("Exception occurred", e);
            throw new BizException("An exception occurred while parsing!");
        }
        return true;
    }
}
