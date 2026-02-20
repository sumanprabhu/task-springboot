import { useEffect, useState } from "react";
import {
  getPendingRequests,
  approveRequest,
  rejectRequest,
} from "../services/api";
import "./AdminRequestsPage.css";

const AdminRequestsPage = () => {
  const [requests, setRequests] = useState([]);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const fetchRequests = async () => {
    try {
      const response = await getPendingRequests();
      setRequests(response.data);
    } catch (err) {
      setError("Failed to load requests");
    }
  };

  useEffect(() => {
    fetchRequests();
  }, []);

  const handleApprove = async (id, name) => {
    if (window.confirm(`Approve admin access for "${name}"?`)) {
      try {
        await approveRequest(id);
        setSuccess(`${name} is now an ADMIN!`);
        fetchRequests();
      } catch (err) {
        setError(err.response?.data?.error || "Approval failed!");
      }
    }
  };

  const handleReject = async (id, name) => {
    if (window.confirm(`Reject admin request from "${name}"?`)) {
      try {
        await rejectRequest(id);
        setSuccess(`Request from ${name} rejected.`);
        fetchRequests();
      } catch (err) {
        setError(err.response?.data?.error || "Rejection failed!");
      }
    }
  };

  return (
    <div className="requests-container">
      <h2>📋 Pending Admin Requests</h2>

      {error && <p className="error-msg">{error}</p>}
      {success && <p className="success-msg">{success}</p>}

      {requests.length === 0 ? (
        <div className="no-requests">
          <p>✅ No pending requests!</p>
        </div>
      ) : (
        <div className="requests-list">
          {requests.map((req) => (
            <div key={req.id} className="request-card">
              <div className="request-info">
                <h3>{req.user?.name}</h3>
                <p className="request-email">{req.user?.email}</p>
                <p className="request-reason">
                  <strong>Reason:</strong> {req.reason}
                </p>
                <p className="request-date">
                  Requested: {new Date(req.requestedAt).toLocaleString()}
                </p>
              </div>

              <div className="request-actions">
                <button
                  onClick={() => handleApprove(req.id, req.user?.name)}
                  className="approve-btn"
                >
                  ✅ Approve
                </button>
                <button
                  onClick={() => handleReject(req.id, req.user?.name)}
                  className="reject-btn"
                >
                  ❌ Reject
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminRequestsPage;
