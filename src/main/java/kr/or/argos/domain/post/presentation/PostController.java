package kr.or.argos.domain.post.presentation;

import org.springframework.web.bind.annotation.RestController;

import kr.or.argos.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostController {
    
    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity getPosts(@RequestParam Object page) {
        return null;
    }
    
    @GetMapping("/{postId}")
    public ResponseEntity getPost(@PathVariable(name = "postId") Long postId) {
        return null;
    }
    
    @GetMapping("/announcement")
    public ResponseEntity getAnnouncementPosts() {
        return null;
    }

    @PostMapping("/")
    public ResponseEntity addPost(@RequestBody Object newPost) {
        return null;
    }
    

    @PatchMapping("/{postId}")
    public ResponseEntity updatePost(
        @PathVariable(name = "postId") Long postId,
        @RequestBody Object updatedPost
    ) {
        return null;
    }

    @PatchMapping("/{postId}/hits")
    public ResponseEntity updateHits(@PathVariable(name = "postId") Long postId) {
        return null;
    }
    
    @DeleteMapping("/{postId}")
    public ResponseEntity deletePost(@PathVariable(name = "postId") Long postId) {
        return null;
    }

}
