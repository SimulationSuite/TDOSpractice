package org.tdos.tdospractice.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

public class TDOSSessionListener implements HttpSessionListener {
    public static ConcurrentHashMap<String, HttpSession> sessions = new ConcurrentHashMap<>();

    /**
     * 用户登录，创建session，用户数增加
     * @param event
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        session.setMaxInactiveInterval(10 * 60);
        sessions.put(session.getId(), session);
    }

    /**
     * 用户下线，销毁session，用户数减少
     * @param event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        sessions.remove(event.getSession().getId());
    }
}
