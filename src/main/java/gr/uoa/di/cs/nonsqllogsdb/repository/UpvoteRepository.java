package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.dto.*;
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
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'logs', localField: 'logId', foreignField: '_id', as: 'logDetails' } }",
            "{ $unwind: '$logDetails' }",
            "{ $group: { _id: '$userId', distinctSourceIPs: { $addToSet: '$logDetails.source_ip' } } }",
            "{ $project: { userId: '$_id', _id: 0, distinctIPCount: { $size: '$distinctSourceIPs' }, sourceIPs: '$distinctSourceIPs' } }",
            "{ $sort: { distinctIPCount: -1 } }",
            "{ $limit: 50 }",
            "{ $lookup: { from: 'users', localField: 'userId', foreignField: '_id', as: 'userDetails' } }",
            "{ $project: { userId: 1, distinctIPCount: 1, sourceIPs: 1, username: { $arrayElemAt: [\"$userDetails.username\", 0] } } }"
    })
    List<AdminIPsDTO> findTopFiftyAdminsWithMostDistinctIPs();
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'users', localField: 'userId', foreignField: '_id', as: 'user_data' } }",
            "{ $unwind: '$user_data' }",
            "{ $group: { _id: '$user_data.email', usernames: { $addToSet: '$user_data.username' }, logIds: { $addToSet: '$logId' } } }",
            "{ $match: { 'usernames.1': { $exists: true } } }",
            "{ $lookup: { from: 'logs', localField: 'logIds', foreignField: '_id', as: 'logs' } }",
            "{ $project: { email: '$_id', usernames: 1, logs: 1, _id: 0 } }"
    })
    List<IntermediateUserLogsDTO> findMultiUsernameLogs();
    @Aggregation(pipeline = {
            "{ $lookup: { from: 'users', localField: 'userId', foreignField: '_id', as: 'user_data' } }",
            "{ $unwind: '$user_data' }",
            "{ $match: { 'user_data.name': ?0 } }",
            "{ $lookup: { from: 'logs', localField: 'logId', foreignField: '_id', as: 'log_details' } }",
            "{ $unwind: '$log_details' }",
            "{ $unwind: '$log_details.details' }",
            "{ $match: { 'log_details.details.key': 'block_id' } }",
            "{ $group: { _id: '$log_details.details.value' } }",
            "{ $project: { blockId: '$_id', _id: 0 } }"
    })
    List<UserBlockDTO> findBlockIdsForUser(String userName);
}
