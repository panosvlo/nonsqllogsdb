package gr.uoa.di.cs.nonsqllogsdb.service;

import gr.uoa.di.cs.nonsqllogsdb.dto.ActiveAdminDTO;
import gr.uoa.di.cs.nonsqllogsdb.dto.AdminIPsDTO;
import gr.uoa.di.cs.nonsqllogsdb.dto.EmailLogsDTO;
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

    public List<EmailLogsDTO> getLogsBySharedEmails() {
        return upvoteRepository.findLogsBySharedEmails();
    }
}
