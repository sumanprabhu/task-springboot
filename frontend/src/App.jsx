import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import NavBar from "./components/NavBar";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import UsersPage from "./pages/UsersPage";
import AddUserPage from "./pages/AddUserPage";
import EditUserPage from "./pages/EditUserPage";
import ProfilePage from "./pages/ProfilePage";
import AdminRequestsPage from "./pages/AdminRequestsPage";
import AgentChatPage from "./pages/AgentChatPage";
import "./App.css";

function App() {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  return (
    <Router>
      <NavBar />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route
          path="/users"
          element={token ? <UsersPage /> : <Navigate to="/login" />}
        />
        <Route
          path="/add-user"
          element={
            token && role === "ADMIN" ? (
              <AddUserPage />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
        <Route
          path="/edit-user/:id"
          element={
            token && role === "ADMIN" ? (
              <EditUserPage />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
        <Route
          path="/profile"
          element={token ? <ProfilePage /> : <Navigate to="/login" />}
        />
        <Route
          path="/admin-requests"
          element={
            token && role === "ADMIN" ? (
              <AdminRequestsPage />
            ) : (
              <Navigate to="/login" />
            )
          }
        />
        <Route
          path="/agent"
          element={token ? <AgentChatPage /> : <Navigate to="/login" />}
        />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;
