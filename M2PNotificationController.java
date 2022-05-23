package com.cards.zokudo.controllers;


import com.cards.zokudo.services.card.notification.M2PNotificationDto;
import com.cards.zokudo.services.card.notification.NotificationInf;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/{processor}/resteasy")
public class M2PNotificationController {

    private final NotificationInf notificationInf;

    public M2PNotificationController(NotificationInf notificationInf) {
        this.notificationInf = notificationInf;
    }


    @CrossOrigin(allowedHeaders = "*", allowCredentials = "true", origins = {"*"})
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map dumpNotificationData(HttpServletRequest request, @PathVariable("processor") String processor,
                                    @RequestBody M2PNotificationDto m2PNotificationDto) {
        return notificationInf.execute(request, processor , m2PNotificationDto);
    }

}
