package gr.uoa.di.cs.nonsqllogsdb.service;

import gr.uoa.di.cs.nonsqllogsdb.dto.ActiveAdminDTO;
import gr.uoa.di.cs.nonsqllogsdb.dto.AdminIPsDTO;
import gr.uoa.di.cs.nonsqllogsdb.dto.IntermediateUserLogsDTO;
import gr.uoa.di.cs.nonsqllogsdb.dto.UserLogsDTO;
import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import gr.uoa.di.cs.nonsqllogsdb.model.Upvote;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogRepository;
import gr.uoa.di.cs.nonsqllogsdb.repository.UpvoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpvoteService {
    @Autowired
    private UpvoteRepository upvoteRepository;
    @Autowired
    private LogRepository logRepository;



    @Autowired
    private MongoTemplate mongoTemplate;  // Inject MongoTemplate

    public void upvoteLog(String logId, String userId) {
        Upvote upvote = new Upvote();
        upvote.setLogId(new ObjectId(logId));
        upvote.setUserId(new ObjectId(userId));
        upvoteRepository.save(upvote);

        // Use MongoTemplate to update the upvote count
        Query query = new Query(Criteria.where("_id").is(new ObjectId(logId)));
        Update update = new Update().inc("upvoteCount", 1);
        mongoTemplate.updateFirst(query, update, Log.class);
    }

    public List<ActiveAdminDTO> getTopFiftyActiveAdministrators() {
        return upvoteRepository.findTopFiftyActiveAdministrators();
    }

    public List<AdminIPsDTO> getTopFiftyAdminsWithMostDistinctIPs() {
        return upvoteRepository.findTopFiftyAdminsWithMostDistinctIPs();
    }
    public List<UserLogsDTO> getMultiUsernameLogs() {
        List<IntermediateUserLogsDTO> intermediateResults = upvoteRepository.findMultiUsernameLogs();

        return intermediateResults.stream().map(this::transformToUserLogsDTO).collect(Collectors.toList());
    }

    private UserLogsDTO transformToUserLogsDTO(IntermediateUserLogsDTO intermediate) {
        UserLogsDTO dto = new UserLogsDTO();
        dto.setEmail(intermediate.getEmail());
        dto.setUsernames(intermediate.getUsernames());

        List<UserLogsDTO.LogInfo> logs = intermediate.getLogs().stream().map(log -> {
            UserLogsDTO.LogInfo logInfo = new UserLogsDTO.LogInfo();
            logInfo.setId(log.getId().toString());  // Convert ObjectId to String
            logInfo.setTimestamp(log.getTimestamp());
            logInfo.setUpvoteCount(log.getUpvoteCount());
            return logInfo;
        }).collect(Collectors.toList());

        dto.setLogs(logs);
        return dto;
    }
}
