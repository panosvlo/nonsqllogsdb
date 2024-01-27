package gr.uoa.di.cs.nonsqllogsdb.service;

import gr.uoa.di.cs.nonsqllogsdb.dto.*;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public List<BlockOperation> getBlocksReplicatedAndServedSameDay() {
        return logRepository.findBlocksReplicatedAndServedSameDay();
    }
}
