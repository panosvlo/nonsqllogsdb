package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.dto.ActiveAdminDTO;
import gr.uoa.di.cs.nonsqllogsdb.dto.AdminIPsDTO;
import gr.uoa.di.cs.nonsqllogsdb.model.Upvote;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import gr.uoa.di.cs.nonsqllogsdb.dto.EmailLogsDTO;

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
            "{ $lookup: { from: 'users', localField: 'userId', foreignField: '_id', as: 'userDetails' } }",
            "{ $unwind: '$userDetails' }",
            "{ $lookup: { from: 'logs', localField: 'logId', foreignField: '_id', as: 'logDetails' } }",
            "{ $unwind: '$logDetails' }",
            "{ $lookup: { from: 'logtypes', localField: 'logDetails.log_type_id', foreignField: '_id', as: 'logTypeDetails' } }", // Adjust this line according to your LogType collection name
            "{ $group: { _id: '$userDetails.email', usernames: { $addToSet: '$userDetails.username' }, logs: { $push: { id: '$logDetails._id', logType: { $arrayElemAt: ['$logTypeDetails', 0] }, timestamp: '$logDetails.timestamp', sourceIp: '$logDetails.source_ip', destinationIp: '$logDetails.destination_ip', details: '$logDetails.details', upvoteCount: '$logDetails.upvoteCount' } } } }",
            "{ $match: { 'usernames.1': { $exists: true } } }",
            "{ $project: { email: '$_id', usernames: 1, logs: 1, _id: 0 } }"
    })
    List<EmailLogsDTO> findLogsBySharedEmails();
}
