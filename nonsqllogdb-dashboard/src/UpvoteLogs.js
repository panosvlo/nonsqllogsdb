import React, { useState, useEffect } from 'react';
import axios from 'axios';
import UpvoteLogsDisplay from './UpvoteLogsDisplay';

const UpvoteLogs = () => {
    const [logs, setLogs] = useState([]);
    const [logType, setLogType] = useState('access_log');
    const [startDate, setStartDate] = useState('2005-06-01');
    const [endDate, setEndDate] = useState('2005-06-30');
    const token = localStorage.getItem('token');

    useEffect(() => {
        fetchLogs();
    }, [logType, startDate, endDate]);

    const fetchLogs = async () => {
        try {
            const formattedStartDate = new Date(startDate).toISOString(); // Convert to ISO string
            const formattedEndDate = new Date(endDate + "T23:59:59.999Z").toISOString(); // Add time and convert to ISO string

            const response = await axios.get(`http://localhost:8080/api/logs`, {
                params: {
                    typeName: logType,
                    start: formattedStartDate,
                    end: formattedEndDate
                },
                headers: token ? { Authorization: `Bearer ${token}` } : {}
            });
            setLogs(response.data);
        } catch (error) {
            console.error('Error fetching logs:', error);
        }
    };


    const handleUpvote = async (logId) => {
        try {
            await axios.post(`http://localhost:8080/api/logs/${logId}/upvote`, {}, {
                headers: token ? { Authorization: `Bearer ${token}` } : {}
            });
            fetchLogs(); // Refresh logs to update upvote count
        } catch (error) {
            console.error('Error upvoting log:', error);
        }
    };

    return (
        <div>
            <h2>Upvote Logs</h2>
            <div>
                <label>
                    Log Type:
                    <select value={logType} onChange={(e) => setLogType(e.target.value)}>
                        <option value="access_log">Access Log</option>
                        <option value="hdfs_dataxceiver_log">HDFS DataXceiver Log</option>
                        <option value="hdfs_fs_namesystem_log">HDFS FSNameSystem Log</option>
                    </select>
                </label>
                <label>
                    Start Date:
                    <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
                </label>
                <label>
                    End Date:
                    <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
                </label>
                <UpvoteLogsDisplay logs={logs} handleUpvote={handleUpvote} />
            </div>
            <div>
                {logs.map(log => (
                    <div key={log.id}>
                        <p>{log.timestamp} - {log.sourceIp} - Upvotes: {log.upvoteCount}</p>
                        <button onClick={() => handleUpvote(log.id)}>Upvote</button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default UpvoteLogs;
