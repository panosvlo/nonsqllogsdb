import React from 'react';
import './UserLogsDisplay.css'; // Import the CSS file

const UserLogsDisplay = ({ userLogs }) => {
  return (
    <div className="user-logs-container">
      {userLogs.map((userLog, index) => (
        <div key={index} className="user-log">
          <h3 className="email">Email: {userLog.email}</h3>
          <p className="usernames">Usernames: {userLog.usernames.join(', ')}</p>
          <div className="logs-section">
            <h4>Logs:</h4>
            <ul className="logs-list">
              {userLog.logs.map((log, logIndex) => (
                <li key={logIndex} className="log-item">
                  ID: {log.id}, Timestamp: {new Date(log.timestamp).toLocaleString()}, Upvotes: {log.upvoteCount}
                </li>
              ))}
            </ul>
          </div>
        </div>
      ))}
    </div>
  );
};

export default UserLogsDisplay;