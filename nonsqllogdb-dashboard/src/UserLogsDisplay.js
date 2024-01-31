import React from 'react';

const UserLogsDisplay = ({ userLogs }) => {
  return (
    <div>
      {userLogs.map((userLog, index) => (
        <div key={index}>
          <h3>Email: {userLog.email}</h3>
          <p>Usernames: {userLog.usernames.join(', ')}</p>
          <h4>Logs:</h4>
          <ul>
            {userLog.logs.map((log, logIndex) => (
              <li key={logIndex}>
                ID: {log.id}, Timestamp: {new Date(log.timestamp).toLocaleString()}, Upvotes: {log.upvoteCount}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

export default UserLogsDisplay;
