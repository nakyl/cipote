package com.interceptor;

import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

import com.main.UserOnline;

import ch.qos.logback.classic.Logger;

public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
	 
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(PresenceChannelInterceptor.class);
 
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
 
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
 
        // ignore non-STOMP messages like heartbeat messages
        if(sha.getCommand() == null) {
            return;
        }
 
        String sessionId = sha.getSessionId();
 
        switch(sha.getCommand()) {
            case CONNECT:
            	if (LOG.isDebugEnabled()) {
            		LOG.debug("STOMP Connect [sessionId: " + sessionId + "]");
            	}
            	break;
            case CONNECTED:
            	if (LOG.isDebugEnabled()) {
            		LOG.debug("STOMP Connected [sessionId: " + sessionId + "]");
            	}
                UserOnline.putUser(sha.getUser().getName());
                LOG.info("User "+sha.getUser().getName()+" connected");
                break;
            case DISCONNECT:
            	if (LOG.isDebugEnabled()) {
            		LOG.debug("STOMP Disconnect [sessionId: " + sessionId + "]");
            	}
                UserOnline.removeUser(sha.getUser().getName());
                LOG.info("User "+sha.getUser().getName()+" disconnected");
                break;
            default:
                break;
        }
    }
}
