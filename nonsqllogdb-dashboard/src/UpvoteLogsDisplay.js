import React from 'react';
import './UpvoteLogsDisplay.css'; // Make sure to create and import the corresponding CSS file for styling

const UpvoteLogsDisplay = ({ logs, handleUpvote }) => {
  return (
    <div className="upvote-logs-container">
      {logs.map((log) => (
        <div key={log.id} className="log-entry">
          <div className="log-header">
            <span className="log-id">ID: {log.id}</span>
            <span className="log-timestamp">{new Date(log.timestamp).toLocaleString()}</span>
            <span className="log-source-ip">IP: {log.sourceIp || 'N/A'}</span>
            <span className="log-upvote-count">Upvotes: {log.upvoteCount}</span>
            <button className="upvote-button" onClick={() => handleUpvote(log.id)}>Upvote</button>
          </div>
          <div className="log-details">
            <h4>Details:</h4>
            <ul>
              {log.details.map((detail, index) => (
                <li key={index}>
                  {detail.key}: {detail.value}
                </li>
              ))}
            </ul>
          </div>
        </div>
      ))}
    </div>
  );
};

export default UpvoteLogsDisplay;
