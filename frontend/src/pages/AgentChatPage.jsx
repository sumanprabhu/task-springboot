import { useState, useRef, useEffect } from "react";
import { agentChat } from "../services/api";
import "./AgentChatPage.css";

const AgentChatPage = () => {
  const [messages, setMessages] = useState([
    {
      sender: "agent",
      text: "👋 Hi! I'm the User Admin Agent. I can help you manage users. Try saying:\n\n• Show me all users\n• Create a user named Rahul with email rahul@gmail.com and phone 9876543210\n• Find user with email suman@gmail.com\n• Make someone an admin\n• Delete a user",
    },
  ]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);
  const chatEndRef = useRef(null);

  // Auto scroll to bottom when new message arrives
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const handleSend = async () => {
    if (!input.trim() || loading) return;

    const userMessage = input.trim();
    setInput("");

    // Add user message to chat
    setMessages((prev) => [...prev, { sender: "user", text: userMessage }]);

    // Show loading
    setLoading(true);

    try {
      const response = await agentChat(userMessage);
      const agentResponse =
        response.data.response || "I couldn't process that. Please try again.";

      // Add agent response to chat
      setMessages((prev) => [
        ...prev,
        { sender: "agent", text: agentResponse },
      ]);
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        {
          sender: "agent",
          text:
            "❌ Error: " +
            (err.response?.data?.error || "Something went wrong!"),
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="chat-container">
      <div className="chat-header">
        <h2>🤖 User Admin Agent</h2>
        <p>Manage users through natural language</p>
      </div>

      <div className="chat-messages">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`chat-bubble ${msg.sender === "user" ? "user-bubble" : "agent-bubble"}`}
          >
            <span className="bubble-label">
              {msg.sender === "user" ? "You" : "🤖 Agent"}
            </span>
            <p className="bubble-text">{msg.text}</p>
          </div>
        ))}

        {loading && (
          <div className="chat-bubble agent-bubble">
            <span className="bubble-label">🤖 Agent</span>
            <p className="bubble-text typing">Thinking...</p>
          </div>
        )}

        <div ref={chatEndRef} />
      </div>

      <div className="chat-input-area">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type your message... (e.g., Show all users)"
          className="chat-input"
          disabled={loading}
        />
        <button
          onClick={handleSend}
          disabled={loading || !input.trim()}
          className="send-btn"
        >
          {loading ? "⏳" : "Send ➤"}
        </button>
      </div>
    </div>
  );
};

export default AgentChatPage;
