package com.task1.suman.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class UserAdminAgent {

    private final ChatClient chatClient;

    public UserAdminAgent(ChatClient.Builder builder) {
        this.chatClient = builder

                // SYSTEM PROMPT — The agent's personality and rules
                .defaultSystem("""
                        You are the User Admin Agent for a User Management System.

                        YOUR ROLE:
                        - You help administrators manage users in the system
                        - You can create, find, delete, list users and change their roles

                        RULES:
                        1. Always confirm before deleting a user
                        2. When creating a user, you need: name, email, and contact number
                        3. If any required information is missing, ASK for it
                        4. Valid roles are: ADMIN and USER only
                        5. Always be polite and professional
                        6. After performing any action, summarize what you did
                        7. If something fails, explain why clearly

                        AVAILABLE TOOLS:
                        - createUserTool: Create a new user (needs name, email, contactNum)
                        - findUserTool: Find a user by email
                        - deleteUserTool: Delete a user by email
                        - listUsersTool: List users (filter: all, admins, users)
                        - changeRoleTool: Change a user's role (needs email, newRole)

                        RESPONSE FORMAT:
                        - Keep responses short and clear
                        - Use emojis sparingly for readability
                        - Always state the result of the action
                        """)
                //  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                //  This is like giving a JOB DESCRIPTION to a new employee!
                //  The AI will FOLLOW these rules in every conversation
                //  System prompt = personality + rules + boundaries

                // REGISTER TOOLS — Tell the agent which tools it can use
                .defaultFunctions(
                        "createUserTool",
                        "findUserTool",
                        "deleteUserTool",
                        "listUsersTool",
                        "changeRoleTool"
                )
                //  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
                //  These names MUST match the @Component("name")
                //  in each tool class!
                //
                //  AI now has a MENU of 5 tools
                //  It will automatically decide which tool to call
                //  based on what the user asks!

                .build();
    }

    // The agent processes user messages
    public String chat(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)       // What the user is asking
                .call()                  // Send to AI (with tools available)
                .content();              // Get response text
        //
        //  What happens inside .call():
        //  1. AI reads user message
        //  2. AI reads system prompt (its rules)
        //  3. AI reads tool descriptions
        //  4. AI decides: "Should I call a tool?"
        //     YES → Calls the tool → Gets result → Responds
        //     NO  → Just responds with text
    }
}