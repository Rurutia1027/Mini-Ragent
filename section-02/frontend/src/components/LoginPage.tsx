import { FormEvent, useState } from "react";

interface LoginPageProps {
    onLogin: (token: string, username: string) => void;
}

export default function LoginPage({ onLogin }: LoginPageProps) {
    const [mode, setMode] = useState<"login" | "register">("login");
    const [username, setUsername] = useState("admin");
    const [password, setPassword] = useState("admin123");
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const path = mode === "login" ? "/api/auth/login" : "/api/auth/register";
            const response = await fetch(path, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username: username.trim(), password })
            });

            const body = await response.json();
            if (!response.ok || body.code !== "0") {
                throw new Error(body.message || `HTTP ${response.status}`);
            }

            if (mode === "register") {
                setMode("login");
                setError(null);
                return;
            }

            onLogin(body.data.token, body.data.username);
        } catch (err) {
            setError((err as Error).message || "Request failed");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="page auth-page">
            <header className="header">
                <h1>Mini Ragent</h1>
                <p>Section 2 . Login and Enable SSE Chat</p>
            </header>

            <form className="auth-card" onSubmit={handleSubmit}>
                <h2>{mode === "login" ? "Login" : "Register"}</h2>
                <label>
                    Username
                    <input
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        autoComplete="username"
                        disabled={loading}
                    />
                </label>
                <label>
                    Password
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        autoComplete={mode === "login" ? "current-password" : "new-password"}
                        disabled={loading}
                    />
                </label>
                {error && <div className="error">{error}</div>}

                {mode === "register" && !error && (
                    <div className="hint">Please login after successful register. Default seed admin/admin123</div>
                )}

                <button type="submit" disabled={loading || !username.trim() || !password}>
                    {loading ? "Processing" : mode === "login" ? "Login" : "Register"}
                </button>

                <button
                    type="button"
                    className="linkish"
                    onClick={() => {
                        setMode(mode === "login" ? "register" : "login");
                        setError(null);
                    }}
                >
                    {mode === "login" ? "No Account? Register" : "Already have account? Login"}
                </button>
            </form>
        </div>
    );
}
