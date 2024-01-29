package gr.uoa.di.cs.nonsqllogsdb.controller;

import gr.uoa.di.cs.nonsqllogsdb.dto.*;
import gr.uoa.di.cs.nonsqllogsdb.model.User;
import gr.uoa.di.cs.nonsqllogsdb.service.LogService;
import gr.uoa.di.cs.nonsqllogsdb.service.UpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/referersWithMultipleResources")
    public ResponseEntity<List<RefererResourceCount>> getReferersWithMultipleResources() {
        List<RefererResourceCount> refererResourceCounts = logService.findReferersWithMultipleResources();
        return ResponseEntity.ok(refererResourceCounts);
    }



    @GetMapping("/replicatedSameDay")
    public ResponseEntity<Map<String, Integer>> getBlocksReplicatedSameDay() {
        Map<String, Integer> blocks = logService.findBlocksReplicatedSameDayAsCreated();
        return ResponseEntity.ok(blocks);
    }


    @GetMapping("/blockReplications")
    public ResponseEntity<List<BlockReplication>> getBlockReplications() {
        List<BlockReplication> blockReplications = logService.findBlockReplications();
        return ResponseEntity.ok(blockReplications);
    }

    @GetMapping("/blockCreations")
    public ResponseEntity<List<BlockCreation>> getBlockCreations() {
        List<BlockCreation> blockCreations = logService.findBlockCreations();
        return ResponseEntity.ok(blockCreations);
    }
    @Autowired
    private UpvoteService upvoteService;
    @PostMapping("/{logId}/upvote")
    public ResponseEntity<Void> upvoteLog(@PathVariable String logId, @AuthenticationPrincipal User user) {
        upvoteService.upvoteLog(logId, user.getId());
        return ResponseEntity.ok().build();
    }
}
