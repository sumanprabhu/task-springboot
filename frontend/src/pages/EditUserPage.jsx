import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getUserById, updateUser } from "../services/api";
import "./EditUserPage.css";

const EditUserPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [form, setForm] = useState({
    name: "",
    email: "",
    contactNum: "",
    password: "",
    role: { id: "", roleName: "" },
    address: {
      street: "",
      city: "",
      state: "",
      zipCode: "",
    },
  });

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await getUserById(id);
        const user = response.data;
        setForm({
          name: user.name || "",
          email: user.email || "",
          contactNum: user.contactNum || "",
          password: "",
          role: user.role || { id: "", roleName: "" },
          address: user.address || {
            street: "",
            city: "",
            state: "",
            zipCode: "",
          },
        });
      } catch (err) {
        setError("Failed to load user");
      }
    };
    fetchUser();
  }, [id]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleAddressChange = (e) => {
    setForm({
      ...form,
      address: { ...form.address, [e.target.name]: e.target.value },
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const updateData = {
        name: form.name,
        email: form.email,
        contactNum: form.contactNum,
        role: form.role,
        address: form.address,
      };

      if (form.password) {
        updateData.password = form.password;
      }

      await updateUser(id, updateData);
      navigate("/users");
    } catch (err) {
      setError(err.response?.data?.error || "Update failed!");
    }
  };

  return (
    <div className="edit-container">
      <form onSubmit={handleSubmit} className="edit-form">
        <h2>✏️ Edit User</h2>

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
          type="text"
          name="contactNum"
          placeholder="Contact Number"
          value={form.contactNum}
          onChange={handleChange}
          className="form-input"
        />

        <input
          type="password"
          name="password"
          placeholder="New Password (leave blank to keep current)"
          value={form.password}
          onChange={handleChange}
          className="form-input"
        />

        <p className="role-display">
          Role: <strong>{form.role?.roleName}</strong>
        </p>

        <h4 className="section-title">📍 Address</h4>

        <input
          type="text"
          name="street"
          placeholder="Street"
          value={form.address?.street || ""}
          onChange={handleAddressChange}
          className="form-input"
        />

        <input
          type="text"
          name="city"
          placeholder="City"
          value={form.address?.city || ""}
          onChange={handleAddressChange}
          className="form-input"
        />

        <input
          type="text"
          name="state"
          placeholder="State"
          value={form.address?.state || ""}
          onChange={handleAddressChange}
          className="form-input"
        />

        <input
          type="text"
          name="zipCode"
          placeholder="Zip Code"
          value={form.address?.zipCode || ""}
          onChange={handleAddressChange}
          className="form-input"
        />

        <div className="btn-group">
          <button type="submit" className="submit-btn">
            Update User
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

export default EditUserPage;
