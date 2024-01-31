import React from 'react';
import UserLogsDisplay from './UserLogsDisplay';

const logTypeDescriptions = {
  '1': 'access_log',
  '3': 'hdfs_fs_namesystem_log',
  '2': 'hdfs_dataxceiver_log'
};

const MainContent = ({ data }) => {
  // Handle case where data is not an array
  if (!data) return null;

  if (Array.isArray(data) && data.length > 0 && data[0].hasOwnProperty('email')) {
    return <UserLogsDisplay userLogs={data} />;
  }

  // Check if data is an array, otherwise make it an array
  const dataArray = Array.isArray(data) ? data : [data];
  const keys = dataArray.length > 0 ? Object.keys(dataArray[0]) : [];

  return (
    <div className="main-content">
      <table>
        <thead>
          <tr>
            {keys.map(key => <th key={key}>{key === 'logTypeName' ? 'Log Type' : key}</th>)}
          </tr>
        </thead>
        <tbody>
          {dataArray.map((item, index) => (
            <tr key={index}>
              {keys.map(key => (
                <td key={key}>
                  {key === 'logTypeName' ? logTypeDescriptions[item[key].toString()] : item[key]}
                 </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default MainContent;
