package com.example.demo.comment;

import com.example.demo.baseTime.BaseTimeEntity;
import com.example.demo.board.Board;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.tomcat.jni.Local;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;




@Data
@Builder
@Document("child_comments")
@AllArgsConstructor
@NoArgsConstructor
public class ChildComment {
    @Id
    private ObjectId id;
    @NonNull private String contents;
    @NonNull private String userId;
    @NonNull private String authorName;
    @NonNull private ObjectId parentCommentId;
    @NonNull private LocalDateTime createdDate;
    @NonNull private LocalDateTime lastModifiedDate;

    public ChildComment( @NonNull String contents,@NonNull String userId, @NonNull String authorName,
                         @NonNull ObjectId parentCommentId, @NonNull LocalDateTime createdDate, @NonNull LocalDateTime lastModifiedDate) {
        this.contents = contents;
        this.userId = userId;
        this.authorName = authorName;
        this.parentCommentId = parentCommentId;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}

