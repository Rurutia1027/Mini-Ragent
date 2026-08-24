import { FormEvent, useRef, useState } from "react";

type Role = "user" | "assistant";

interface Message {
    role: Role;
    content: string;
}

export default function ChatPage() {
    // reduce:func, memo:func, fetch:func, state:func create instance, context:func create instance
    const [messages, setMessages] = useState<Message[]>([]);
    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const abortRef = useRef<AbortController | null>(null);

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        const text = input.trim();
        if (!text || loading) {
            return;
        }

        setError(null);
        setInput("");
        setLoading(true); // state machine flag

        const userMessage: Message = { role: "user", content: text };
        const history = messages;
        setMessages((prev) => [...prev, userMessage, { role: "assistant", content: "" }]);

        const controller = new AbortController();
        abortRef.current = controller;

        try {
            const response = await fetch("/api/chat/stream", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "text/event-stream"
                },
                body: JSON.stringify({
                    message: text,
                    history: history.map((item) => ({ role: item.role, content: item.content }))
                }),
                signal: controller.signal
            });

            if (!response.ok || !response.body) {
                throw new Error(`HTTP ${response.status}`);
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder();
            let buffer = "";

            while (true) {
                const { done, value } = await reader.read();
                if (done) {
                    break;
                }

                buffer += decoder.decode(value, { stream: true });
                const parts = buffer.split("\n\n");
                buffer = parts.pop() ?? "";

                for (const part of parts) {
                    const lines = part.split("\n");
                    let eventName = "message";
                    let data = "";

                    for (const line of lines) {
                        if (line.startsWith("event:")) {
                            eventName = line.slice(6).trim();
                        } else if (line.startsWith("data:")) {
                            data += line.slice(5);
                        }
                    }

                    if (eventName === "done") {
                        continue;
                    }

                    if (data) {
                        setMessages((prev) => {
                            const next = [...prev]; // [0... user-message]
                            const last = next[next.length - 1]; // last ->assistant message instance
                            if (last?.role === "assistant") {
                                next[next.length - 1] = { ...last, content: last.content + data };
                            }
                            return next;
                        });
                    }
                }
            }
        } catch (err) {
            if ((err as Error).name !== "AbortError") {
                setError((err as Error).message || "Request failed");
            }
        } finally {
            setLoading(false);
            abortRef.current = null;
        }
    }

    function handleStop() {
        abortRef.current?.abort();
        setLoading(false);
    }

    return (
        <div className="page">
            <header className="header">
                <h1>Mini Ragent</h1>
                <p>Section 1 Local Ollama SSE Conversation</p>
            </header>

            <main className="chat-panel">
                {messages.length === 0 && (
                    <div className="empty">Input Question, Ollama will answer</div>
                )}

                {messages.map((message, index) => (
                    <div key={index} className={`bubble ${message.role}`}>
                        <div className="role">{message.role === "user" ? "You" : "AI"}</div>
                        <div className="content">{message.content || (loading ? "..." : "")}</div>
                    </div>
                ))}

                {error && <div className="error">Error: {error}</div>}
            </main>

            <form className="composer" onSubmit={handleSubmit}>
                <textarea
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    placeholder="input message, enter to send"
                    rows={3}
                    disabled={loading}
                    onKeyDown={(e) => {
                        if (e.key === "Enter" && !e.shiftKey) {
                            e.preventDefault();
                            e.currentTarget.form?.requestSubmit();
                        }
                    }}
                />
                <div className="actions">
                    {loading ? (
                        <button type="button" onClick={handleStop} className="secondary">
                            Stop
                        </button>
                    ) : (
                        <button type="submit" disabled={!input.trim()}>
                            Send
                        </button>
                    )}
                </div>
            </form>
        </div>
    );
}
