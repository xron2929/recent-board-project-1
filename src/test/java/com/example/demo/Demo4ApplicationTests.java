package com.example.demo;

import com.example.demo.comment.ChildComment;
import com.example.demo.comment.ParentComment;
import com.example.demo.mongo.ChildCommentService;
import com.example.demo.mongo.MongoConfig;
import com.example.demo.mongo.ParentCommentService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class Demo4ApplicationTests {
    ChildCommentService childCommentService;
    ParentCommentService parentCommentService;
    @Test
    void contextLoads() {
        String a = "";
        String b = null;
        String c = " ";
        if(a.isBlank()) {
            System.out.println("error ");
        }
        if(b == null || b.isBlank()) {
            System.out.println("error 2");
        }
        if(c.isBlank()) {
            System.out.println("error 3");
        }
        if(a.isEmpty()) {
            System.out.println("error 4");
        }
        if(b == null || b.isEmpty()) {
            System.out.println("error 5");
        }
        if(c.isEmpty()) {
            System.out.println("error 6");
        }
    }
    @BeforeEach
    void init() {
        MongoConfig mongoConfig = new MongoConfig("test",
                "mongodb://svc.sel3.cloudtype.app:32690/test?authSource=admin");
        childCommentService = new ChildCommentService(mongoConfig.mongoTemplate());
        parentCommentService = new ParentCommentService(mongoConfig.mongoTemplate());

    }

    @Test
    public void testMongo() {
        // parentCommentService.dropCollection();
        // childCommentService.dropCollection();
        ParentComment parentComment = ParentComment
                .builder()
                .parentCommentContent("sdfsdf")
                .boardId(1l)
                .parentCommentNickname("sdf")
                .parentCommentUserId("sdf")
                .build();
        parentCommentService.insertParentComment(parentComment);
        ParentComment findParentComment = parentCommentService.getParentCommentsByBoardId(1l).stream().findFirst().orElse(null);
        ChildComment childComment = ChildComment.builder()
                .authorName("dfsa")
                .userId("dfs")
                .contents("sdfwefw")
                .parentCommentId(findParentComment.getId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        ChildComment childComment2 = ChildComment.builder()
                .authorName("dfsa2")
                .userId("dfsa2")
                .contents("sdfwefw2")
                .parentCommentId(findParentComment.getId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ChildComment childComment3 = ChildComment.builder()
                .authorName("dfsa3")
                .userId("dfs3")
                .contents("sdfwefw3")
                .parentCommentId(findParentComment.getId())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

      //   childCommentService.insertChildComment(childComment);
        // childCommentService.insertChildComment(childComment2);
        // childCommentService.insertChildComment(childComment3);
        parentCommentService.addChildComment(childComment);
        parentCommentService.addChildComment(childComment2);
        parentCommentService.addChildComment(childComment3);
        // ChildComment childComment4 = childCommentService.getChildCommentsByParentCommentId(findParentComment.getId()).stream().findAny().orElse(null);
        // childCommentService.updateChildComment(childComment4.getId(), "changeData");
        List<ChildComment> childComments = childCommentService.getChildCommentsByParentCommentId(findParentComment.getId());
        for (ChildComment comment:childComments) {
            System.out.println("comment = " + comment);
            System.out.println("comment.getCreatedDate() = " + comment.getCreatedDate());
            System.out.println("comment.getLastModifiedDate() = " + comment.getLastModifiedDate());
        }



    }
    // @Test
    void dropCollection() {
        parentCommentService.dropCollection();
        childCommentService.dropCollection();
    }
    @Test
    void getParentCommentTest() {
        ParentComment parentComment = parentCommentService.getParentComment
                (new ObjectId("655868e084138f60aab6ca70"));
        //

        System.out.println("parentComment = " + parentComment.getParentCommentUserId());
        System.out.println("parentComment = " + parentComment.getParentCommentNickname());
        System.out.println("parentComment = " + parentComment.getParentCommentContent());
        // 655723b9a13c872c97757d4b
    }




}
