import pymongo
from pymongo import MongoClient
import dateutil.parser
import re
from dateutil import parser as date_parser
from bson.objectid import ObjectId
from pymongo import MongoClient

# Function to parse access logs
def parse_access_log_line(line, log_type_id):
    access_log_pattern = re.compile(
        r'(\S+) \S+ \S+ \[(.+?)\] "(\S+) (.*?) (\S+)" (\d{3}) (\d+|-) "(.*?)" "(.*?)"'
    )
    matcher = access_log_pattern.match(line)

    if matcher:
        ip = matcher.group(1)
        timestamp_str = matcher.group(2)
        method = matcher.group(3)
        resource = matcher.group(4)
        protocol = matcher.group(5)
        status_code = int(matcher.group(6))
        size = 0 if matcher.group(7) == '-' else int(matcher.group(7))
        referer = None if matcher.group(8) == '-' else matcher.group(8)
        user_agent = matcher.group(9)

        # Convert timestamp string to ISODate
        timestamp = date_parser.parse(timestamp_str, fuzzy=True)

        log_entry = {
            "log_type_id": ObjectId(log_type_id),
            "timestamp": timestamp,
            "source_ip": ip,
            "destination_ip": None,  # No destination IP in access logs
            "details": [
                {"key": "method", "value": method},
                {"key": "resource", "value": resource},
                {"key": "protocol", "value": protocol},
                {"key": "status_code", "value": str(status_code)},
                {"key": "size", "value": str(size)},
                {"key": "referer", "value": referer},
                {"key": "user_agent", "value": user_agent}
            ]
        }

        # Filter out None values
        log_entry["details"] = [detail for detail in log_entry["details"] if detail["value"] is not None]

        return log_entry
    else:
        return None

# Function to parse HDFS FS Namesystem logs
def parse_hdfs_fs_namesystem_log_line(line):
    pass

# Function to parse HDFS DataXceiver logs
def parse_hdfs_dataxceiver_log_line(line):
    pass

# Main function to handle the processing
def main():
    # Connect to MongoDB
    client = MongoClient('localhost', 27017)
    db = client['logs_database']

    # Create collections if they don't exist
    log_types = db['log_types']
    logs = db['logs']

    # Insert log types if not already present
    for log_type in ["access_log", "hdfs_dataxceiver_log", "hdfs_fs_namesystem_log"]:
        if log_types.count_documents({"type_name": log_type}) == 0:
            log_types.insert_one({"type_name": log_type})

    # Process each log file
    for file_path, parse_function in [('access_log_full', parse_access_log_line),
                                      ('HDFS_FS_Namesystem.short', parse_hdfs_fs_namesystem_log_line),
                                      ('HDFS_DataXceiver.short', parse_hdfs_dataxceiver_log_line)]:
        with open(file_path, 'r') as file:
            batch = []
            for line in file:
                log_type_id = db['log_types'].find_one({"type_name": "access_log"})["_id"]
                parsed_line = parse_function(line, log_type_id)
                if parsed_line:
                    batch.append(parsed_line)
                    if len(batch) >= 1000:  # Batch size of 1000
                        logs.insert_many(batch)
                        batch = []
            if batch:  # Insert any remaining logs
                logs.insert_many(batch)

if __name__ == "__main__":
    main()
