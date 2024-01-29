package gr.uoa.di.cs.nonsqllogsdb.service;

import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import gr.uoa.di.cs.nonsqllogsdb.model.Upvote;
import gr.uoa.di.cs.nonsqllogsdb.repository.LogRepository;
import gr.uoa.di.cs.nonsqllogsdb.repository.UpvoteRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpvoteService {
    @Autowired
    private UpvoteRepository upvoteRepository;
    @Autowired
    private LogRepository logRepository;

    public void upvoteLog(String logId, String userId) {
        Upvote upvote = new Upvote();
        upvote.setLogId(new ObjectId(logId));
        upvote.setUserId(new ObjectId(userId));
        upvoteRepository.save(upvote);

        Log log = logRepository.findById(logId).orElseThrow(() -> new RuntimeException("Log not found"));
        log.setUpvoteCount(log.getUpvoteCount() + 1);
        logRepository.save(log);
    }
}
