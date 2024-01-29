package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.model.Upvote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UpvoteRepository extends MongoRepository<Upvote, String> {
    List<Upvote> findByUserId(ObjectId userId);
    List<Upvote> findByLogId(ObjectId logId);
}
