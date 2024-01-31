package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.model.LogType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogTypeRepository extends MongoRepository<LogType, String> {
    LogType findByTypeName(String typeName);
}
