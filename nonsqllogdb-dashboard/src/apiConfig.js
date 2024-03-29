const apis = [
  {
    name: 'Query 1',
    endpoint: '/api/logs/countByTypeInRange',
    description: 'Find the total logs per type that were created within a specified time range and sort them in a descending order. Please note that individual files may log actions of more than one type',
    params: [
      { name: 'start', type: 'date', placeholder: 'Start Date', value: '' },
      { name: 'end', type: 'date', placeholder: 'End Date', value: '' }
    ]
  },
  {
    name: 'Query 2',
    endpoint: '/api/logs/countDailyByTypeName',
    description: 'Find the number of total requests per day for a specific log type (access_log, hdfs_dataxceiver_log or hdfs_fs_namesystem_log) and time range',
    params: [
      { name: 'typeName', type: 'text', placeholder: 'Log Type', value: 'access_log' },
      { name: 'start', type: 'date', placeholder: 'Start Date', value: '' },
      { name: 'end', type: 'date', placeholder: 'End Date', value: '' }
    ]
  },
  {
    name: 'Query 3',
    endpoint: '/api/logs/topCommonBySourceIp',
    description: 'Find the three most common logs per source IP for a specific day.',
    params: [
      { name: 'day', type: 'date', placeholder: 'Day', value: '' },
    ]
  },
  {
    name: 'Query 4',
    endpoint: '/api/logs/leastCommonHttpMethods',
    description: ' Find the two least common HTTP methods with regards to a given time range.',
    params: [
      { name: 'start', type: 'date', placeholder: 'Start Date', value: '' },
      { name: 'end', type: 'date', placeholder: 'End Date', value: '' }
    ]
  },
	{
	name: 'Query 5',
	endpoint: '/api/logs/referersWithMultipleResources',
	description: 'Find the referers (if any) that have led to more than one resources.',
	params: [] // No parameters required for this API
	},
	{
        name: 'Query 6',
        endpoint: '/api/logs/replicatedSameDay',
        description: 'Find the blocks that have been replicated the same day that they have also been served.',
        params: [] // No parameters required for this API
	},
    {
        name: 'Query 7',
        endpoint: '/api/logs/top50UpvotedLogs',
        description: 'Find the fifty most upvoted logs for a specific day.',
        params: [
          { name: 'day', type: 'date', placeholder: 'Day', value: '' },
        ]
    },
	{
        name: 'Query 8',
        endpoint: '/api/logs/topActiveAdmins',
        description: 'Find the fifty most active administrators, with regard to the total number of upvotes.',
        params: [] // No parameters required for this API
	},
	{
        name: 'Query 9',
        endpoint: '/api/logs/topDistinctIPs',
        description: 'Find the top fifty administrators, with regard to the total number of source IPs for which they have upvoted logs.',
        params: [] // No parameters required for this API
	},
	{
        name: 'Query 10',
        endpoint: '/api/logs/multiUsernameLogs',
        description: ' Find all logs for which the same e-mail has been used for more than one usernames when casting an upvote.',
        params: [] // No parameters required for this API
	},
    {
        name: 'Query 11',
        endpoint: '/api/logs/blockIds',
        description: 'Find all the block ids for which a given name has casted a vote for a log involving it.',
        params: [
          { name: 'username', type: 'text', placeholder: 'Username', value: '' }
        ]
    },
//    {
//        name: 'Upvote Log',
//        endpoint: '/api/logs/{logId}/upvote',
//        method: 'POST',
//        description: 'Upvote a specific log by its ID.',
//        params: [
//          { name: 'logId', type: 'text', placeholder: 'Log ID', value: '' }
//        ]
//    },
];

export default apis;
