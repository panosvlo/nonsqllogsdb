package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.dto.LogCount;
import gr.uoa.di.cs.nonsqllogsdb.model.Log;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LogRepository extends MongoRepository<Log, String> {

    @Aggregation(pipeline = {
            "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$log_type_id', count: { $sum: 1 } } }",
            "{ $lookup: { from: 'log_types', localField: '_id', foreignField: '_id', as: 'logType' } }",
            "{ $unwind: '$logType' }",
            "{ $project: { typeName: '$logType.type_name', count: 1, _id: 0 } }", // Exclude _id and include only typeName and count
            "{ $sort: { count: -1 } }"
    })
    List<LogCount> countLogsByTypeAndTimestampBetween(Date start, Date end);
}
