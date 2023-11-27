package com.example.demo.comment;


import com.example.demo.baseTime.BaseTimeEntity;
import com.example.demo.board.Board;
import com.example.demo.user.defaultuser.DefaultMember;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Document("parent_comment")
@AllArgsConstructor
@NoArgsConstructor

@Setter
@Builder
public class ParentComment {
    @Id
    private ObjectId id;

    private String parentCommentContent;
    private String parentCommentUserId;
    private String parentCommentNickname;
    private Long boardId;
    private List<ChildComment> childComments;
    // 항상 save전에는 null이어야 함


    public ParentComment(String parentCommentContent, String parentCommentUserId, String parentCommentNickname, Long boardId) {
        this.parentCommentContent = parentCommentContent;
        this.parentCommentUserId = parentCommentUserId;
        this.parentCommentNickname = parentCommentNickname;
        this.boardId = boardId;
    }

    public ParentComment(String parentCommentContent, String parentCommentUserId, String parentCommentNickname, Long boardId, List<ChildComment> childComments) {
        this.parentCommentContent = parentCommentContent;
        this.parentCommentUserId = parentCommentUserId;
        this.parentCommentNickname = parentCommentNickname;
        this.boardId = boardId;
        this.childComments = childComments;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getParentCommentContent() {
        return parentCommentContent;
    }

    public void setParentCommentContent(String parentCommentContent) {
        this.parentCommentContent = parentCommentContent;
    }

    public String getParentCommentUserId() {
        return parentCommentUserId;
    }

    public void setParentCommentUserId(String parentCommentUserId) {
        this.parentCommentUserId = parentCommentUserId;
    }

    public String getParentCommentNickname() {
        return parentCommentNickname;
    }

    public void setParentCommentNickname(String parentCommentNickname) {
        this.parentCommentNickname = parentCommentNickname;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public List<ChildComment> getChildComments() {
        return childComments;
    }

    public void setChildComments(List<ChildComment> childComments) {
        this.childComments = childComments;
    }
}
// mongodb로 parentComment ChildComment 를 저장하는게 나은게
// parentComment랑 ChildComment를 한 곳에 저장하면 각각 1번, 2번,3번,.. 이렇게 구분행
// 대댓글에 Step이랑 Next라는 필요 없는 값이 들어가야 하고, 스프링이나 JS에서 여기에 있는 걸로 Order해야되기 때문에
// 비효율적으로 나감
// 이렇게 하면 BoardId를 Comment mongodb의 where절 쓰면 되고,
// comment.board <-> board join 걸면 됨
// 어차피 NoneUser면 member랑
