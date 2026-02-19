import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../services/api";
import "./AddUserPage.css";

const AddUserPage = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    contactNum: "",
    roleName: "USER",
  });
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await registerUser(form);
      navigate("/users");
    } catch (err) {
      setError(err.response?.data?.error || "Failed to add user!");
    }
  };

  return (
    <div className="adduser-container">
      <form onSubmit={handleSubmit} className="adduser-form">
        <h2>➕ Add New User</h2>

        {error && <p className="error-msg">{error}</p>}

        <input
          type="text"
          name="name"
          placeholder="Full Name"
          value={form.name}
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={form.password}
          onChange={handleChange}
          className="form-input"
          required
        />

        <input
          type="text"
          name="contactNum"
          placeholder="Contact Number"
          value={form.contactNum}
          onChange={handleChange}
          className="form-input"
          required
        />

        <select
          name="roleName"
          value={form.roleName}
          onChange={handleChange}
          className="form-input"
        >
          <option value="USER">USER</option>
          <option value="ADMIN">ADMIN</option>
        </select>

        <div className="btn-group">
          <button type="submit" className="submit-btn">
            Add User
          </button>
          <button
            type="button"
            onClick={() => navigate("/users")}
            className="cancel-btn"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default AddUserPage;
