package com.example.demo.mongo;

import com.example.demo.comment.ChildComment;
import com.example.demo.comment.ParentComment;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.hibernate.annotations.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ParentCommentService {
    public ParentCommentService(@Autowired MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    private MongoTemplate mongoTemplate;
    public void dropCollection() {
        mongoTemplate.dropCollection("parent_comment");

    }
    public List<ParentComment> getParentCommentsByBoardId(@NonNull Long boardId) {

        List<ParentComment> parentComments = mongoTemplate.find(
                Query.query(Criteria.where("boardId").is(boardId)),
                ParentComment.class);

        return parentComments;
    }
    public List<ChildComment> getParentCommentByParentCommentIds(@NonNull ObjectId objectId) {

        ParentComment parentComment = mongoTemplate.find(
                Query.query(Criteria.where("id").is(objectId)),
                ParentComment.class).stream().findAny().orElse(null);
            return parentComment.getChildComments();
    }
    public List<ChildComment> getChildrenCommentsByParentCommentIds(@NonNull List<ObjectId> objectIds) {

        ParentComment parentComment = mongoTemplate.find(
                Query.query(Criteria.where("id").in(objectIds)),
                ParentComment.class).stream().findAny().orElse(null);
        return parentComment.getChildComments();
    }
    public ParentComment getParentComment(@NonNull ObjectId parentCommentId) {
        ParentComment parentComment = mongoTemplate.find(
                Query.query(Criteria.where("id").is(parentCommentId)),
                ParentComment.class).stream().findAny().orElse(null);
        System.out.println("ParentCommentService - getParentComment() parentComment.getUserId() = " + parentComment.getParentCommentUserId());
        return parentComment;
    }
    public void insertParentComment (ParentComment parentComment) {
        mongoTemplate.save(parentComment);
    }

    public void addChildComment( ChildComment childComment) {
        Update update = new Update();

        ParentComment parentComment = getParentComment(childComment.getParentCommentId());
        List<ChildComment> childComments = parentComment.getChildComments();
        if (childComments==null) {
            childComments = new ArrayList<>();
            childComments.add(childComment);
        }
        else {
            childComments.add(childComment);
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(childComment.getParentCommentId())),
                Update.update("childComments",childComments),
                ParentComment.class);
        /*
                 mongoTemplate.updateFirst(query,
                    Update.update("sibling", sibling.stream().filter(pet -> pet.getId().equals(nowPetId) == false).collect(Collectors.toList())),
                    PetEntity.class);
        }
         */
    }
    public void deleteParentCommentById(ObjectId objectId) {
        mongoTemplate.remove(
                Query.query(Criteria.where("id").is(objectId)),
                ParentComment.class);
    }
    public void deleteParentCommentByBoardIds(Set<Long> boardIds) {
        mongoTemplate.remove(
                Query.query(Criteria.where("boardId").in(boardIds)),
                ParentComment.class);
    }
    public void insertAllParentComment(List<ParentComment>parentComments) {
        mongoTemplate.insertAll(parentComments);
    }

}
