package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.dto.*;
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
    @Aggregation(pipeline = {
            "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$details' }",
            "{ $match: { 'details.key': 'method' } }",
            "{ $group: { _id: '$details.value', count: { $sum: 1 } } }",
            "{ $sort: { count: 1 } }",
            "{ $project: { method: '$_id', count: 1, _id: 0 } }",
            "{ $limit: 2 }"
    })
    List<HttpMethodCount> findLeastCommonHttpMethods(Date start, Date end);
    @Aggregation(pipeline = {
            "{ $project: { referer: { $filter: { input: '$details', as: 'detail', cond: { $eq: ['$$detail.key', 'referer'] } } }, resources: { $filter: { input: '$details', as: 'detail', cond: { $eq: ['$$detail.key', 'resource'] } } } } }",
            "{ $unwind: '$referer' }",
            "{ $unwind: '$resources' }",
            "{ $group: { _id: '$referer.value', resources: { $addToSet: '$resources.value' } } }",
            "{ $match: { resources: { $not: { $size: 1 } } } }",
            "{ $project: { referer: '$_id', resources: 1, _id: 0 } }"
    })
    List<RefererResourceCount> findReferersWithMultipleResources();
    @Aggregation(pipeline = {
            // Extract replicated blocks with timestamp
            "{ $unwind: '$details' }",
            "{ $match: { 'details.value': { $regex: 'is added to' } } }",
            "{ $project: { blockId: { $regexFind: { input: '$details.value', regex: 'blk_[-\\d]+' } }, replicatedTimestamp: '$timestamp', _id: 0 } }",
            "{ $project: { blockId: '$blockId.match', replicatedTimestamp: 1 } }",

            // Store the result in a temporary collection
            "{ $out: 'tempReplicatedBlocks' }"
    })
    void extractReplicatedBlocks();

    @Aggregation(pipeline = {
            "{ $unwind: '$details' }",
            "{ $match: { 'details.value': { $regex: 'is added to' } } }",
            "{ $project: { blockId: { $regexFind: { input: '$details.value', regex: 'blk_[-\\\\d]+' } }, timestamp: 1, _id: 0 } }",
            "{ $project: { blockId: '$blockId.match', timestamp: 1, _id: 0 } }"
    })
    List<BlockReplication> findBlockReplications();

    @Aggregation(pipeline = {
            "{ $match: { 'details': { $elemMatch: { 'key': 'operation', 'value': 'NameSystem.allocateBlock' } } } }",
            "{ $unwind: '$details' }",
            "{ $match: { 'details.key': 'block_id' } }",
            "{ $project: { _id: 0, blockId: '$details.value', timestamp: 1 } }"
    })
    List<BlockCreation> findBlockCreations();

    @Aggregation(pipeline = {
            // Match block creation operations
            "{ $match: { 'details': { $elemMatch: { 'key': 'operation', 'value': 'NameSystem.allocateBlock' } } } }",
            "{ $unwind: '$details' }",
            "{ $match: { 'details.key': 'block_id' } }",
            "{ $project: { blockId: '$details.value', creationDate: { $dateToString: { format: '%Y-%m-%d', date: '$timestamp' } } } }",
            // Join with block replications
            "{ $lookup: { from: 'logs', localField: 'blockId', foreignField: 'details.value', as: 'replications' } }",
            "{ $unwind: '$replications' }",
            "{ $match: { 'replications.details.value': { $regex: 'is added to' } } }",
            "{ $project: { blockId: 1, creationDate: 1, replicationDate: { $dateToString: { format: '%Y-%m-%d', date: '$replications.timestamp' } } } }",
            // Match where creation and replication dates are the same
            "{ $match: { $expr: { $eq: ['$creationDate', '$replicationDate'] } } }",
            "{ $group: { _id: '$blockId', operationDates: { $addToSet: '$creationDate' } } }",
            "{ $project: { blockId: '$_id', operationDates: 1, _id: 0 } }"
    })
    List<BlockOperation> findBlocksReplicatedSameDayAsCreated();

}