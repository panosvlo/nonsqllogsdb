package gr.uoa.di.cs.nonsqllogsdb.controller;

import gr.uoa.di.cs.nonsqllogsdb.dto.*;
import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import gr.uoa.di.cs.nonsqllogsdb.model.LogType;
import gr.uoa.di.cs.nonsqllogsdb.model.User;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogRepository;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogTypeRepository;
import gr.uoa.di.cs.nonsqllogsdb.repository.UserRepository;
import gr.uoa.di.cs.nonsqllogsdb.service.LogParsingService;
import gr.uoa.di.cs.nonsqllogsdb.service.LogService;
import gr.uoa.di.cs.nonsqllogsdb.service.UpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private UserRepository userRepository;

    @Autowired
    private UpvoteService upvoteService;

    @PostMapping("/{logId}/upvote")
    public ResponseEntity<Void> upvoteLog(@PathVariable String logId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userRepository.findByUsername(userDetails.getUsername());
            if (user != null) {
                upvoteService.upvoteLog(logId, user.getId());
            }
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/top50UpvotedLogs")
    public ResponseEntity<List<LogUpvoteDTO>> getTopUpvotedLogsByDate(
            @RequestParam("day") @DateTimeFormat(pattern = "yyyy-MM-dd") Date day) {
        Date dayEnd = new Date(day.getTime() + 86400000); // Add one day in milliseconds
        List<LogUpvoteDTO> topUpvotedLogs = logService.findTop50UpvotedLogsByDate(day, dayEnd);
        return ResponseEntity.ok(topUpvotedLogs);
    }

    @GetMapping("/topActiveAdmins")
    public ResponseEntity<List<ActiveAdminDTO>> getTopActiveAdmins() {
        List<ActiveAdminDTO> activeAdmins = upvoteService.getTopFiftyActiveAdministrators();
        return ResponseEntity.ok(activeAdmins);
    }
    @GetMapping("/topDistinctIPs")
    public ResponseEntity<List<AdminIPsDTO>> getTopDistinctIPs() {
        List<AdminIPsDTO> admins = upvoteService.getTopFiftyAdminsWithMostDistinctIPs();
        return ResponseEntity.ok(admins);
    }
    @GetMapping("/multiUsernameLogs")
    public ResponseEntity<List<UserLogsDTO>> getMultiUsernameLogs() {
        try {
            List<UserLogsDTO> userLogs = upvoteService.getMultiUsernameLogs();
            return ResponseEntity.ok(userLogs);
        } catch (Exception e) {
            // Handle exceptions appropriately
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/blockIds")
    public ResponseEntity<List<UserBlockDTO>> getBlockIdsForUser(@RequestParam String username) {
        List<UserBlockDTO> blockIds = upvoteService.getBlockIdsForUser(username);
        return ResponseEntity.ok(blockIds);
    }
    @Autowired
    private LogParsingService parsingLogService;

    @Autowired
    private LogTypeRepository logTypeRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadLogFile(@ModelAttribute LogFileUploadDTO logFileUploadDTO) {
        try {
            LogType logType = logTypeRepository.findByTypeName(logFileUploadDTO.getLogTypeName());
            if (logType == null) {
                return ResponseEntity.badRequest().body("Invalid log type name.");
            }

            switch (logFileUploadDTO.getLogTypeName()) {
                case "access_log":
                    parsingLogService.parseAndStoreAccessLog(logFileUploadDTO.getFile(), logType);
                    break;
                case "hdfs_fs_namesystem_log":
                    parsingLogService.parseAndStoreHdfsFsNamesystemLog(logFileUploadDTO.getFile(), logType);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Unsupported log type.");
            }
            return ResponseEntity.ok("File uploaded and parsed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload and parse file: " + e.getMessage());
        }
    }
}
