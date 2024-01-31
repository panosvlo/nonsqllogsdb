import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

const ApiForm = ({ api, params, onParamChange, onSubmit }) => {
  const [startDate, setStartDate] = useState(new Date());
  if (!api) return null;

  return (
    <form onSubmit={onSubmit}>
      <div className="api-description">{api.description}</div>
      {api.params.map((param, index) => (
        param.type === 'date' ? (
          <DatePicker
            selected={params[param.name] ? new Date(params[param.name]) : startDate}
            onChange={(date) => onParamChange({ target: { name: param.name, value: date.toISOString().split('T')[0] } })}
            dateFormat="yyyy-MM-dd"
            key={index}
          />
        ) : (
          <input
            key={index}
            type={param.type}
            name={param.name}
            placeholder={param.placeholder}
            value={params[param.name]}
            onChange={onParamChange}
            required
          />
        )
      ))}
      <button type="submit">Fetch Data</button>
    </form>
  );
};

export default ApiForm;
