import React, { useState, useRef, useEffect } from "react";
import { useMutation } from "@tanstack/react-query";
import axios from "axios";

const MainChat = () => {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);
  const scrollRef = useRef(null);

  const { mutate, isPending } = useMutation({
    mutationFn: (prompt) =>
      axios.post("http://localhost:8080/api/ai/request", prompt)
        .then((res) => res.data),
    onSuccess: (data) => {
      setMessages((prev) => [
        ...prev,
        { id: Date.now(), text: data, sender: "ai" },
      ]);
    },
    onError: () => {
      setMessages((prev) => [
        ...prev,
        { id: Date.now(), text: "Error fetching response.", sender: "ai" },
      ]);
    },
  });

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = () => {
    if (!input.trim()) return;
    setMessages((prev) => [
      ...prev,
      { id: Date.now(), text: input, sender: "user" },
    ]);
    mutate({ id: 1, request: input });
    setInput("");
  };

  return (
    <div className="flex flex-col h-screen bg-[#131314] text-[#e3e3e3] font-sans">
      <div ref={scrollRef} className="flex-1 overflow-y-auto pt-10 pb-32 px-4 md:px-0">
        <div className="max-w-3xl mx-auto space-y-8">
          {messages.map((msg) => (
            <div key={msg.id} className={`flex ${msg.sender === "user" ? "justify-end" : "justify-start"}`}>
              <div className={`max-w-[85%] rounded-2xl px-5 py-3 ${
                msg.sender === "user"
                  ? "bg-[#2b2c2f] border border-[#444746] shadow-sm"
                  : "bg-transparent"
              }`}>
                <p className="text-[15.5px] leading-relaxed whitespace-pre-wrap">{msg.text}</p>
              </div>
            </div>
          ))}
          {isPending && (
            <div className="flex justify-start">
              <p className="text-gray-500 text-sm italic">Gemini is thinking...</p>
            </div>
          )}
        </div>
      </div>

      <div className="fixed bottom-0 left-0 w-full bg-[#131314] pb-6 pt-2">
        <div className="max-w-3xl mx-auto px-4">
          <div className="relative flex items-center bg-[#1e1f20] rounded-full px-6 py-2 border border-transparent focus-within:bg-[#28292a] transition-all">
            <input
              type="text"
              className="flex-1 bg-transparent py-3 outline-none text-white placeholder-gray-500"
              placeholder="Enter a prompt here..."
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && handleSend()}
            />
            <button
              onClick={handleSend}
              disabled={isPending}
              className={`ml-2 p-2 rounded-full transition-colors ${
                input && !isPending
                  ? "bg-[#a8c7fa] text-[#062e6f]"
                  : "text-gray-600 cursor-not-allowed"
              }`}
            >
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                <path d="M3.478 2.405a.75.75 0 00-.926.94l2.432 7.905H13.5a.75.75 0 010 1.5H4.984l-2.432 7.905a.75.75 0 00.926.94 60.519 60.519 0 0018.445-8.986.75.75 0 000-1.218A60.517 60.517 0 003.478 2.405z" />
              </svg>
            </button>
          </div>
          <p className="text-center text-[11px] text-gray-500 mt-3">
            Gemini may display inaccurate info, including about people, so double-check its responses.
          </p>
        </div>
      </div>
    </div>
  );
};

export default MainChat;