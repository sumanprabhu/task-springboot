package com.task1.suman.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final ChatClient chatClient;

    @Autowired
    private UserAdminAgent userAdminAgent;

    // Spring AI auto-creates ChatClient.Builder for us
    // We just inject it and build
    public AiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // ============================================
    //  Simple chat — just ask AI anything
    // ============================================
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()   // Start a prompt
                .user(message)       // What the USER is asking
                .call()              // Send to OpenAI
                .content();          // Get the text response
    }

    // ✅ NEW — User Admin Agent (with tools!)
    @PostMapping("/agent")
    public Map<String, String> agentChat(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        String response = userAdminAgent.chat(message);
        return Map.of("response", response);
    }
    //  ↑↑↑
    //  We use POST because we send JSON body
    //  We return Map so response is clean JSON:
    //  { "response": "User created successfully!" }
}