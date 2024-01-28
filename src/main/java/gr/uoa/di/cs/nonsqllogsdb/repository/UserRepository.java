package gr.uoa.di.cs.nonsqllogsdb.repository;

import gr.uoa.di.cs.nonsqllogsdb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long>  {
    User findByUsername(String username);
}