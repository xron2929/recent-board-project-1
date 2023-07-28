package com.example.demo.socket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    // stompClient.subscribe('/topic/my-topic', 얘랑 연결
    @MessageMapping("/send-message")
    // stomp를 처리하기 위한 메세지로 전달
    @SendTo("/topic/my-topic")
    // /topic이라는 경로에 suscribe 하고 있는 사용자들에게 데이터를 전달
    public String sendMessage(String message) {
        System.out.println("message = " + message);
        return message;
    }
    // /user/{sessionId}/queue/messages로 보내짐

    @MessageMapping("/send-message-to-user")
    public void sendMessageToUser(@Header("sessionId")String sessionId, MessageDto message) {
        // messagingTemplate.convertAndSend("/queue/messages", message);
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/messages", message);
    }
    @MessageMapping("/send-message-to-user2")
    public void sendMessageToUser2(@Header("sessionId2")String sessionId, String message) {
        messagingTemplate.convertAndSendToUser(sessionId, "/queue/messages2", message);
    }
    // messagingTemplate.convertAndSendToUser("12345", "/queue/messages", message);
    // 뷰

}
