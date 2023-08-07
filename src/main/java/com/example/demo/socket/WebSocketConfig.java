package com.example.demo.socket;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

// registerStompEndpoints: 이 메소드는 클라이언트에서 접근할 수 있는 WebSocket 엔드포인트를 등록합니다.
// /my-websocket-endpoint라는 엔드포인트가 등록되어 있습니다.
        /*

    function connect() {
        const socket = new SockJS('/my-websocket-endpoint');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/my-topic', function(message) {
                // showMessage(JSON.parse(message.body).content);
                // json으로 확장도 해봐야될듯 ㅇㅇ..
                showMessage(message.body);
                // showMessage는 받은 데이터를 뷰로 관리하는 거였고 실질적인 프론트<->백엔드 데이터는 /my-websocket-endpoint로
                // 전송이 되는게 맞는듯
            });
        });
    }

         */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user", "/topic");
        config.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/my-websocket-endpoint")
                .setAllowedOrigins("http://localhost:3001","https://web-tip-17xqnr2algm9dni8.sel3.cloudtype.app")
                .withSockJS();
        //setAllowedOrigins() 로 cors 허용
        registry.addEndpoint("/my-websocket-endpoint2")
                .setAllowedOrigins("http://localhost:3001","https://web-tip-17xqnr2algm9dni8.sel3.cloudtype.app")
                .withSockJS();

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(1024 * 1024);

    }
}