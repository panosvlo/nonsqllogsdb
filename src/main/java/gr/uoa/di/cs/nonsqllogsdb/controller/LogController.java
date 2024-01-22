package gr.uoa.di.cs.nonsqllogsdb.controller;

import gr.uoa.di.cs.nonsqllogsdb.dto.CommonLogCount;
import gr.uoa.di.cs.nonsqllogsdb.dto.DailyLogCount;
import gr.uoa.di.cs.nonsqllogsdb.dto.HttpMethodCount;
import gr.uoa.di.cs.nonsqllogsdb.dto.LogCount; // Correct import
import gr.uoa.di.cs.nonsqllogsdb.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/countByTypeInRange")
    public ResponseEntity<List<LogCount>> getCountOfLogsByTypeInRange( // Updated return type
                                                                       @RequestParam("start") String startStr,
                                                                       @RequestParam("end") String endStr) {

        try {
            Date start = dateFormat.parse(startStr);
            Date end = dateFormat.parse(endStr);
            List<LogCount> logCounts = logService.countLogsByTypeInDateRange(start, end); // Updated method call
            return ResponseEntity.ok(logCounts);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null); // or any other error handling
        }
    }
    @GetMapping("/countDailyByTypeName")
    public ResponseEntity<List<DailyLogCount>> getCountOfDailyLogsByTypeName(
            @RequestParam("typeName") String typeName,
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        try {
            Date start = dateFormat.parse(startStr);
            Date end = dateFormat.parse(endStr);
            List<DailyLogCount> dailyCounts = logService.countDailyLogsByTypeName(typeName, start, end);
            return ResponseEntity.ok(dailyCounts);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/topCommonBySourceIp")
    public ResponseEntity<List<CommonLogCount>> getTopCommonLogsBySourceIp(
            @RequestParam("day") String dayStr) {

        try {
            Date dayStart = dateFormat.parse(dayStr);
            Date dayEnd = new Date(dayStart.getTime() + 86400000); // Add one day in milliseconds
            List<CommonLogCount> commonLogCounts = logService.findTopCommonLogsBySourceIp(dayStart, dayEnd);
            return ResponseEntity.ok(commonLogCounts);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/leastCommonHttpMethods")
    public ResponseEntity<List<HttpMethodCount>> getLeastCommonHttpMethods(
            @RequestParam("start") String startStr,
            @RequestParam("end") String endStr) {

        try {
            Date start = dateFormat.parse(startStr);
            Date end = dateFormat.parse(endStr);
            List<HttpMethodCount> httpMethodCounts = logService.findLeastCommonHttpMethods(start, end);
            return ResponseEntity.ok(httpMethodCounts);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
