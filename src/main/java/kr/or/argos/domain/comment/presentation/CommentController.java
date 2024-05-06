package kr.or.argos.domain.comment.presentation;

import org.springframework.web.bind.annotation.RestController;

import kr.or.argos.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class CommentController {

    private CommentService commentService;

    @GetMapping("/{postId}/comments")
    public ResponseEntity getComments(@PathVariable(name = "postId") Long postId) {
        return null;
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity addComment(@RequestBody Object newComment) {
        return null;
    }
    
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity updateComment(
        @PathVariable(name = "postId") Long postId,
        @PathVariable(name = "commentId") Long commentId,
        @RequestBody Object updatedComment
    ) {
        return null;
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(
        @PathVariable(name = "postId") Long postId,
        @PathVariable(name = "commentId") Long commentId
    ) {
        return null;
    }
    
}
