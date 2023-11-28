package com.example.demo.mongo;

import com.example.demo.comment.ChildComment;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ChildCommentService {
    public ChildCommentService(@Autowired MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MongoTemplate mongoTemplate;
    // 목록 조회
    public void dropCollection() {
        mongoTemplate.dropCollection("child_comments");
    }
    public List<ChildComment> getChildCommentsByParentCommentId(@NonNull ObjectId parentCommentId) {
        List<ChildComment> childComments = mongoTemplate.find(
                Query.query(Criteria.where("parentCommentId").is(parentCommentId)),
                ChildComment.class);

        return childComments;
    }
    public void insertChildComment (ChildComment childComment) {
        mongoTemplate.save(childComment);
    }
    public void updateChildComment(ObjectId objectId, String contents) {
        Update update = new Update();
        update.set("contents", contents);
        update.set("lastModifiedDate", LocalDateTime.now());
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(objectId)),
                update,
                ChildComment.class);
    }
    public void deleteChildComment(ObjectId objectId) {
        mongoTemplate.remove(
                Query.query(Criteria.where("id").is(objectId)),
                ChildComment.class);
    }









}
