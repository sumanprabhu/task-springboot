import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUsers, deleteUser } from "../services/api";
import "./UsersPage.css";

const UsersPage = () => {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");

  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [size] = useState(5);
  const [error, setError] = useState("");

  const fetchUsers = async (pageNum) => {
    try {
      const response = await getUsers(pageNum, size);
      setUsers(response.data.content);
      setTotalPages(response.data.totalPages);
      setTotalElements(response.data.totalElements);
      setPage(response.data.number);
    } catch (err) {
      if (err.response?.status === 401 || err.response?.status === 403) {
        navigate("/login");
      }
      setError("Failed to fetch users");
    }
  };

  useEffect(() => {
    fetchUsers(0);
  }, []);

  const handleDelete = async (id, name) => {
    if (window.confirm(`Are you sure you want to delete "${name}"?`)) {
      try {
        await deleteUser(id);
        fetchUsers(page);
      } catch (err) {
        setError(err.response?.data?.error || "Delete failed!");
      }
    }
  };

  return (
    <div className="users-container">
      <div className="users-header">
        <h2>📋 All Users ({totalElements})</h2>
        {role === "ADMIN" && (
          <button onClick={() => navigate("/add-user")} className="add-btn">
            + Add User
          </button>
        )}
      </div>

      {error && <p className="error-msg">{error}</p>}

      <table className="users-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Email</th>
            <th>Contact</th>
            <th>Role</th>
            <th>City</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user, index) => (
            <tr key={user.id}>
              <td>{page * size + index + 1}</td>
              <td>{user.name}</td>
              <td>{user.email}</td>
              <td>{user.contactNum}</td>
              <td>
                <span
                  className={`role-badge ${user.role?.roleName === "ADMIN" ? "role-admin" : "role-user"}`}
                >
                  {user.role?.roleName}
                </span>
              </td>
              <td>{user.address?.city || "—"}</td>
              <td>
                {role === "ADMIN" && (
                  <button
                    onClick={() => navigate(`/edit-user/${user.id}`)}
                    className="edit-btn"
                  >
                    ✏️ Edit
                  </button>
                )}
                {role === "ADMIN" && (
                  <button
                    onClick={() => handleDelete(user.id, user.name)}
                    className="delete-btn"
                  >
                    🗑️ Delete
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="pagination">
        <button
          onClick={() => fetchUsers(page - 1)}
          disabled={page === 0}
          className="page-btn"
        >
          ◀ Previous
        </button>
        <span className="page-info">
          Page {page + 1} of {totalPages}
        </span>
        <button
          onClick={() => fetchUsers(page + 1)}
          disabled={page + 1 >= totalPages}
          className="page-btn"
        >
          Next ▶
        </button>
      </div>
    </div>
  );
};

export default UsersPage;
