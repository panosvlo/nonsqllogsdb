import React from 'react';
import UserLogsDisplay from './UserLogsDisplay';

const logTypeDescriptions = {
  '1': 'access_log',
  '3': 'hdfs_fs_namesystem_log',
  '2': 'hdfs_dataxceiver_log'
};

const MainContent = ({ data, selectedApi }) => {
  if (!data) return null;

  if (Array.isArray(data) && data.length > 0 && data[0].hasOwnProperty('email')) {
    return <UserLogsDisplay userLogs={data} />;
  }

  let dataArray;
  if (Array.isArray(data)) {
    dataArray = data;
  } else if (data !== null && typeof data === 'object') {
    if (selectedApi && selectedApi.name === 'Query 6') {
      // Handling for Query 6
      dataArray = Object.entries(data).map(([blockId, count]) => ({ blockId, count }));
    } else {
      // Default handling for object type responses (including Query 5)
      dataArray = [data];
    }
  } else {
    dataArray = [data];
  }

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
                  {Array.isArray(item[key]) ? item[key].join(', ') : item[key]}
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
