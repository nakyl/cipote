package com.listener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent evt) {
        System.out.println("Session created");
        evt.getSession().setMaxInactiveInterval(-1);
    }

    public void sessionDestroyed(HttpSessionEvent evt) {
        System.out.println("Session destroyed");
    }
}