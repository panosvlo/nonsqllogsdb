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
def parse_hdfs_fs_namesystem_log_line(line, log_type_id):
    hdfs_namesystem_log_pattern = re.compile(
        r'(\d{6} \d{6}) (\d+) (\S+) (dfs\.FSNamesystem): (BLOCK\*) (\S+): (.+)'
    )
    matcher = hdfs_namesystem_log_pattern.match(line)

    if matcher:
        datetime_str = matcher.group(1)
        thread_id = matcher.group(2)
        log_level = matcher.group(3)
        component = matcher.group(4)
        operation = matcher.group(6)
        details = matcher.group(7)

        # Convert datetime string to ISODate
        timestamp = date_parser.parse(datetime_str, yearfirst=True)

        # Extract IP from details, if present
        ip_pattern = re.compile(r'(\d+\.\d+\.\d+\.\d+):\d+')
        ip_match = ip_pattern.search(details)
        source_ip = ip_match.group(1) if ip_match else None

        log_entry = {
            "log_type_id": ObjectId(log_type_id),
            "timestamp": timestamp,
            "source_ip": source_ip,
            "destination_ip": None,  # No destination IP in these logs
            "details": [
                {"key": "thread_id", "value": thread_id},
                {"key": "log_level", "value": log_level},
                {"key": "component", "value": component},
                {"key": "operation", "value": operation},
                {"key": "details", "value": details}
            ]
        }

        # Extract and add block ID and size if present
        block_pattern = re.compile(r'blk_(-?\d+)')
        block_match = block_pattern.search(details)
        if block_match:
            log_entry["details"].append({"key": "block_id", "value": block_match.group(1)})

        size_pattern = re.compile(r'size (\d+)')
        size_match = size_pattern.search(details)
        if size_match:
            log_entry["details"].append({"key": "size", "value": size_match.group(1)})

        return log_entry
    else:
        return None

# Function to parse HDFS DataXceiver logs
def parse_hdfs_dataxceiver_log_line(line, log_type_id):
    hdfs_dataxceiver_log_pattern = re.compile(
        r'(\d{6} \d{6}) (\d+) INFO dfs\.DataNode\$DataXceiver: (\S+) block blk_(-?\d+) src: (/\d+\.\d+\.\d+\.\d+:\d+) dest: (/\d+\.\d+\.\d+\.\d+:\d+)'
    )
    matcher = hdfs_dataxceiver_log_pattern.match(line)

    if matcher:
        datetime_str = matcher.group(1)
        action = matcher.group(3)
        block_id = matcher.group(4)
        src = matcher.group(5)
        dest = matcher.group(6)

        # Convert datetime string to ISODate
        timestamp = date_parser.parse(datetime_str, yearfirst=True)

        # Extract IPs and ports
        source_ip, src_port = src[1:].split(':')
        destination_ip, dest_port = dest[1:].split(':')

        log_entry = {
            "log_type_id": ObjectId(log_type_id),
            "timestamp": timestamp,
            "source_ip": source_ip,
            "destination_ip": destination_ip,
            "details": [
                {"key": "action", "value": action},
                {"key": "block_id", "value": block_id},
                {"key": "src_port", "value": src_port},
                {"key": "dest_port", "value": dest_port}
            ]
        }

        return log_entry
    else:
        return None

# Main function to handle the processing
def main():
    # Connect to MongoDB
    client = MongoClient('localhost', 27017)
    db = client['logs_database']

    # Create collections if they don't exist
    log_types = db['log_types']
    logs = db['logs']

    # Insert log types if not already present and store their IDs
    log_type_ids = {}
    for log_type in ["access_log", "hdfs_dataxceiver_log", "hdfs_fs_namesystem_log"]:
        if log_types.count_documents({"type_name": log_type}) == 0:
            result = log_types.insert_one({"type_name": log_type})
            log_type_ids[log_type] = result.inserted_id
        else:
            log_type_ids[log_type] = log_types.find_one({"type_name": log_type})["_id"]

    # File paths and corresponding log types
    file_paths_and_types = [
        ('access_log_full', 'access_log'),
        ('HDFS_FS_Namesystem.log.short', 'hdfs_fs_namesystem_log'),
        ('HDFS_DataXceiver.log.short', 'hdfs_dataxceiver_log')
    ]

    # Process each log file
    for file_path, log_type in file_paths_and_types:
        parse_function = None
        if log_type == 'access_log':
            parse_function = parse_access_log_line
        elif log_type == 'hdfs_fs_namesystem_log':
            parse_function = parse_hdfs_fs_namesystem_log_line
        elif log_type == 'hdfs_dataxceiver_log':
            parse_function = parse_hdfs_dataxceiver_log_line

        if parse_function:
            with open(file_path, 'r') as file:
                batch = []
                for line in file:
                    log_type_id = log_type_ids[log_type]
                    parsed_line = parse_function(line, log_type_id)
                    if parsed_line:
                        batch.append(parsed_line)
                        if len(batch) >= 5000:  # Batch size of 5000
                            logs.insert_many(batch)
                            batch = []
                if batch:  # Insert any remaining logs
                    logs.insert_many(batch)

if __name__ == "__main__":
    main()

