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
            {role === "ADMIN" && (
              <Link to="/add-user" className="nav-link">
                Add User
              </Link>
            )}
            <span className="nav-email">{email}</span>
            <span className="nav-role">{role}</span>
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
