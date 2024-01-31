import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function UploadLog({ setData }) {
  const [selectedFile, setSelectedFile] = useState(null);
  const [logTypeName, setLogTypeId] = useState('');
  const [uploadMessage, setUploadMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    // Reset the data when the UploadLog component is mounted
    setData(null);
  }, [setData]);

  const handleFileSelect = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleUpload = async (event) => {
    event.preventDefault();
    setUploadMessage('');
    setIsLoading(true);
    const formData = new FormData();
    formData.append('file', selectedFile);
    formData.append('logTypeName', logTypeName);

    try {
      const response = await axios.post('http://localhost:8080/api/logs/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setUploadMessage(response.data);
      setIsLoading(false);
    } catch (error) {
      console.error('Upload failed:', error);
      setUploadMessage('Failed to upload and parse file.');
      setIsLoading(false);
    }
  };

  return (
    <div className="upload-form">
      <h2>Upload Log File</h2>
      {isLoading && <p className="message">Uploading file, please wait...</p>}
      {uploadMessage && <p className="message">{uploadMessage}</p>}
      <form onSubmit={handleUpload}>
        <div>
          <label>Log Type:</label>
          <select value={logTypeName} onChange={(e) => setLogTypeId(e.target.value)} required>
            <option value="">Select Log Type</option>
            <option value="access_log">access_log</option>
            <option value="hdfs_fs_namesystem_log">hdfs_fs_namesystem_log</option>
            <option value="hdfs_dataxceiver_log">hdfs_dataxceiver_log</option>
          </select>
        </div>
        <div>
          <label>File:</label>
          <input type="file" onChange={handleFileSelect} required />
        </div>
        <button type="submit">Upload</button>
      </form>
    </div>
  );
}

export default UploadLog;
