package com.alperen.kitapsatissistemi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SessionTestController {

    @GetMapping("/session-test")
    @ResponseBody
    public Map<String, Object> sessionTest(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("sessionId", session.getId());
        result.put("isNew", session.isNew());
        result.put("creationTime", session.getCreationTime());
        result.put("lastAccessedTime", session.getLastAccessedTime());
        result.put("maxInactiveInterval", session.getMaxInactiveInterval());
        
        // Session attributes
        Map<String, Object> attributes = new HashMap<>();
        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            attributes.put(name, session.getAttribute(name));
        }
        result.put("attributes", attributes);
        
        return result;
    }
}