package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.dto.ActiveAdminDTO;
import gr.uoa.di.cs.nonsqllogsdb.model.Upvote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UpvoteRepository extends MongoRepository<Upvote, String> {
    List<Upvote> findByUserId(ObjectId userId);
    List<Upvote> findByLogId(ObjectId logId);
    @Aggregation(pipeline = {
            "{ $group: { _id: '$userId', totalUpvotes: { $sum: 1 } } }",
            "{ $sort: { totalUpvotes: -1 } }",
            "{ $limit: 50 }",
            "{ $lookup: { from: 'users', localField: '_id', foreignField: '_id', as: 'userDetails' } }",
            "{ $unwind: '$userDetails' }",
            "{ $project: { username: '$userDetails.username', totalUpvotes: 1 } }"
    })
    List<ActiveAdminDTO> findTopFiftyActiveAdministrators();
}
