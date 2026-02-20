import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getMyProfile,
  updateMyProfile,
  requestAdminAccess,
} from "../services/api";
import "./ProfilePage.css";

const ProfilePage = () => {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const [form, setForm] = useState({
    name: "",
    contactNum: "",
    address: {
      street: "",
      city: "",
      state: "",
      zipCode: "",
    },
  });
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [adminReason, setAdminReason] = useState("");
  const [adminMsg, setAdminMsg] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getMyProfile();
        const user = response.data;
        setEmail(user.email);
        setForm({
          name: user.name || "",
          contactNum: user.contactNum || "",
          address: user.address || {
            street: "",
            city: "",
            state: "",
            zipCode: "",
          },
        });
      } catch (err) {
        setError("Failed to load profile");
      }
    };
    fetchProfile();
  }, []);

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
    setSuccess("");

    try {
      await updateMyProfile(form);
      setSuccess("Profile updated successfully!");
    } catch (err) {
      setError(err.response?.data?.error || "Update failed!");
    }
  };

  const handleAdminRequest = async () => {
    setAdminMsg("");

    if (!adminReason.trim()) {
      setAdminMsg("Please provide a reason!");
      return;
    }

    try {
      await requestAdminAccess(adminReason);
      setAdminMsg("Admin request submitted! Waiting for approval.");
      setAdminReason("");
    } catch (err) {
      setAdminMsg(
        err.response?.data?.message ||
          err.response?.data?.error ||
          "Request failed!",
      );
    }
  };

  return (
    <div className="profile-container">
      <form onSubmit={handleSubmit} className="profile-form">
        <h2>⚙️ My Profile</h2>

        {error && <p className="error-msg">{error}</p>}
        {success && <p className="success-msg">{success}</p>}

        <div className="profile-email">
          📧 {email}
          <span
            className={`profile-role ${role === "ADMIN" ? "role-admin" : "role-user"}`}
          >
            {role}
          </span>
        </div>

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
          type="text"
          name="contactNum"
          placeholder="Contact Number"
          value={form.contactNum}
          onChange={handleChange}
          className="form-input"
        />

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

        <button type="submit" className="submit-btn">
          Save Changes
        </button>
      </form>

      {/* Request Admin Access — Only show for USERs */}
      {role === "USER" && (
        <div className="admin-request-box">
          <h3>🔑 Request Admin Access</h3>
          <textarea
            placeholder="Why do you need admin access?"
            value={adminReason}
            onChange={(e) => setAdminReason(e.target.value)}
            className="reason-input"
          />
          <button onClick={handleAdminRequest} className="request-btn">
            Submit Request
          </button>
          {adminMsg && <p className="admin-msg">{adminMsg}</p>}
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
