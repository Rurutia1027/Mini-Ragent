import {useState} from "react";
import ChatPage from "./components/ChatPage";
import LoginPage from "./components/LoginPage";

const TOKEN_KEY = "mini-ragent-token";
const USER_KEY = "mini-ragent-username";

export default function App() {
    const [token, setToken] = useState<string | null>(() => localStorage.getItem(TOKEN_KEY));
    const [username, setUsername] = useState<string | null>(() => localStorage.getItem(USER_KEY));

    function handleLogin(nextToken: string, nextUsername: string) {
        localStorage.setItem(TOKEN_KEY, nextToken);
        localStorage.setItem(USER_KEY, nextUsername);
        setToken(nextToken);
        setUsername(nextUsername);
    }

    function handleLogout() {
        const current = localStorage.getItem(TOKEN_KEY);
        if (current) {
            fetch("/api/auth/logout", {
                method: "POST",
                headers: {Authorization: `Bearer ${current}`}
            }).catch(() => undefined)
        }
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        setToken(null);
        setUsername(null);
    }

    if (!token) {
        return <LoginPage onLogin={handleLogin}/>
    }

    return (
        <ChatPage token={token}
                  username={username ?? "user"}
                  onLogout={handleLogout}
                  onUnauthorized={handleLogout}/>
    );
}