import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const email = localStorage.getItem("email");

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("email");
    navigate("/login");
    window.location.reload();
  };

  return (
    <nav className="navbar">
      <h2 className="navbar-logo">👤 User Management</h2>

      <div className="navbar-links">
        {token ? (
          <>
            <Link to="/users" className="nav-link">
              Users
            </Link>
            <Link to="/agent" className="nav-link agent-link">
              🤖 Agent
            </Link>

            {role === "ADMIN" && (
              <>
                <Link to="/add-user" className="nav-link">
                  Add User
                </Link>
                <Link to="/admin-requests" className="nav-link">
                  📋 Requests
                </Link>
              </>
            )}

            <Link to="/profile" className="nav-link profile-link">
              ⚙️ Profile
            </Link>

            <span className="nav-email">{email}</span>
            <span
              className={`nav-role ${role === "ADMIN" ? "role-admin" : "role-user"}`}
            >
              {role}
            </span>
            <button onClick={handleLogout} className="logout-btn">
              Logout
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="nav-link">
              Login
            </Link>
            <Link to="/register" className="nav-link">
              Register
            </Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
