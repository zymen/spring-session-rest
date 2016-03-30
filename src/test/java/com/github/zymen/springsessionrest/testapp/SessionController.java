package com.github.zymen.springsessionrest.testapp;

import com.github.zymen.springsessionrest.persistent.RestSession;
import com.github.zymen.springsessionrest.persistent.RestSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Set;

import static com.github.zymen.springsessionrest.persistent.RestSession.globalAttributeName;
import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("session")
public class SessionController {

    public static final String PRINCIPAL_NAME = "user";
    private static final String SESSION_ATTRIBUTE_NAME = "attribute";

    @Autowired(required = false)
    private SessionScopedBean sessionBean;

    @Autowired(required = false)
    private RestSessionRepository sessionRepository;

    @RequestMapping(value = "attribute", method = POST)
    public void setAttribute(@RequestBody Message dto, HttpSession session) {
        session.setAttribute(SESSION_ATTRIBUTE_NAME, dto);
    }

    @RequestMapping(value = "attribute/global", method = POST)
    public void setGlobalAttribute(@RequestBody Message dto, HttpSession session) {
        session.setAttribute(globalAttributeName(SESSION_ATTRIBUTE_NAME), dto);
    }

    @RequestMapping("attribute")
    public Object getAttribute(HttpSession session) {
        return session.getAttribute(SESSION_ATTRIBUTE_NAME);
    }

    @RequestMapping("attribute/global")
    public Object getGlobalAttribute(HttpSession session) {
        return session.getAttribute(globalAttributeName(SESSION_ATTRIBUTE_NAME));
    }

    @RequestMapping(value = "attribute", method = DELETE)
    public void deleteAttribute(HttpSession session) {
        session.removeAttribute(SESSION_ATTRIBUTE_NAME);
    }

    @RequestMapping(value = "attribute/global", method = DELETE)
    public void deleteGlobalAttribute(HttpSession session) {
        session.removeAttribute(globalAttributeName(SESSION_ATTRIBUTE_NAME));
    }

    @RequestMapping(value = "bean", method = POST)
    public void setBean(@RequestBody Message dto) {
        sessionBean.setText(dto.getText());
        sessionBean.setNumber(dto.getNumber());
    }

    @RequestMapping("bean")
    public Message getBean() {
        Message message = new Message();
        message.setText(sessionBean.getText());
        message.setNumber(sessionBean.getNumber());
        return message;
    }

    @RequestMapping(method = DELETE)
    public void invalidateSession(HttpSession session) {
        session.invalidate();
    }

    @RequestMapping(value = "id", method = PUT)
    public void changeSessionId(HttpServletRequest request) {
        request.changeSessionId();
    }

    @RequestMapping(value = "principal", method = POST)
    public String setPrincipalAttribute(HttpSession session) {
        session.setAttribute(PRINCIPAL_NAME_INDEX_NAME, PRINCIPAL_NAME);
        return session.getId();
    }

    @RequestMapping("principal")
    public Set<String> getPrincipalSessions() {
        Map<String, RestSession> sessionsById = sessionRepository.findByIndexNameAndIndexValue(PRINCIPAL_NAME_INDEX_NAME, PRINCIPAL_NAME);
        return sessionsById.keySet();
    }
}
