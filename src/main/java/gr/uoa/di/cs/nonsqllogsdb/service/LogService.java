package gr.uoa.di.cs.nonsqllogsdb.service;

import gr.uoa.di.cs.nonsqllogsdb.dto.*;
import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<LogCount> countLogsByTypeInDateRange(Date start, Date end) { // Updated return type
        return logRepository.countLogsByTypeAndTimestampBetween(start, end);
    }

    public List<DailyLogCount> countDailyLogsByTypeName(String typeName, Date start, Date end) {
        return logRepository.countDailyLogsByTypeName(typeName, start, end);
    }
    public List<CommonLogCount> findTopCommonLogsBySourceIp(Date dayStart, Date dayEnd) {
        return logRepository.findTopCommonLogsBySourceIp(dayStart, dayEnd);
    }
    public List<HttpMethodCount> findLeastCommonHttpMethods(Date start, Date end) {
        return logRepository.findLeastCommonHttpMethods(start, end);
    }
    public List<RefererResourceCount> findReferersWithMultipleResources() {
        return logRepository.findReferersWithMultipleResources();
    }
    public Map<String, Integer> findBlocksReplicatedSameDayAsCreated() {
        List<BlockCreation> blockCreations = logRepository.findBlockCreations();
        List<BlockReplication> blockReplications = logRepository.findBlockReplications();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC")); // Ensure time zone consistency
        Map<String, Date> creationDates = new HashMap<>();
        Map<String, Integer> replicatedSameDayBlocks = new HashMap<>();

        // Logging block creations
        for (BlockCreation creation : blockCreations) {
            creationDates.put(creation.getBlockId(), creation.getTimestamp());
        }

        // Logging block replications
        for (BlockReplication replication : blockReplications) {
            String blockId = extractNumericBlockId(replication.getBlockId());
            Date creationDate = creationDates.get(blockId);

            if (creationDate != null && isSameDay(creationDate, replication.getTimestamp())) {
                replicatedSameDayBlocks.put(blockId, replicatedSameDayBlocks.getOrDefault(blockId, 0) + 1);
            }
        }

        return replicatedSameDayBlocks;
    }

    private String extractNumericBlockId(String blockId) {
        // Extracts the numeric part of the block ID
        Matcher matcher = Pattern.compile("-?\\d+").matcher(blockId);
        return matcher.find() ? matcher.group() : "";
    }

    private boolean isSameDay(Date date1, Date date2) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date1).equals(dateFormat.format(date2));
    }




    public List<BlockReplication> findBlockReplications() {
        return logRepository.findBlockReplications();
    }

    public List<BlockCreation> findBlockCreations() {
        return logRepository.findBlockCreations();
    }

    public List<LogUpvoteDTO> findTop50UpvotedLogsByDate(Date dayStart, Date dayEnd) {
        List<Log> logs = logRepository.findTop50ByDate(dayStart, dayEnd);
        return logs.stream()
                .map(log -> new LogUpvoteDTO(log.getId(), log.getUpvoteCount()))
                .collect(Collectors.toList());
    }
    public List<Log> getLogsByTypeAndDate(String typeName, Date start, Date end) {
        return logRepository.findLogsByTypeAndTimestamp(typeName, start, end);
    }
}
