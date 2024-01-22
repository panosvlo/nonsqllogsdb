package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.dto.CommonLogCount;
import gr.uoa.di.cs.nonsqllogsdb.dto.DailyLogCount;
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



    @Aggregation(pipeline = {
            "{ $lookup: { from: 'log_types', localField: 'log_type_id', foreignField: '_id', as: 'logType' } }",
            "{ $unwind: '$logType' }",
            "{ $match: { $and: [ { 'logType.type_name': ?0 }, { timestamp: { $gte: ?1, $lte: ?2 } } ] } }",
            "{ $group: { _id: { $dateToString: { format: '%Y-%m-%d', date: '$timestamp' } }, count: { $sum: 1 } } }",
            "{ $project: { day: '$_id', count: 1, _id: 0 } }",
            "{ $sort: { day: 1 } }"
    })
    List<DailyLogCount> countDailyLogsByTypeName(String typeName, Date start, Date end);

    @Aggregation(pipeline = {
            "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$source_ip', count: { $sum: 1 } } }",
            "{ $sort: { count: -1 } }",
            "{ $project: { sourceIp: '$_id', count: 1, _id: 0 } }",
            "{ $limit: 3 }"
    })
    List<CommonLogCount> findTopCommonLogsBySourceIp(Date dayStart, Date dayEnd);
}