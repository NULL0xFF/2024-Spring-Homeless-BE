package kr.or.argos.domain.post.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import kr.or.argos.domain.post.dto.PostCreation;
import kr.or.argos.domain.post.entity.Post;
import kr.or.argos.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "Post Management API")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

  private final PostService postService;

  @PostMapping("/")
  @Operation(summary = "Create a new post", description = "Endpoint to create a new post. Takes a PostCreation object as input and returns the URI of the created post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Post created", content = @Content),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<URI> createPost(HttpServletRequest servlet,
      @RequestBody PostCreation request) {
    return ResponseEntity.created(URI.create(
            "/posts/" + postService.createPost(servlet.getRemoteUser(), request).getIdentifier()))
        .build();
  }

  @GetMapping("/")
  @Operation(summary = "Get all posts", description = "Endpoint to retrieve all posts in the system.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get posts successful", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Post.class)))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<List<Post>> getPosts(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok(postService.getPosts(page, size));
  }

  @GetMapping("/{postId}")
  @Operation(summary = "Get post by ID", description = "Endpoint to retrieve a post by its identifier.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get post successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<Post> getPost(@PathVariable String postId) {
    return ResponseEntity.ok(postService.getPost(postId));
  }

  @GetMapping("/announcements")
  @Operation(summary = "Get all announcements", description = "Endpoint to retrieve all announcement posts.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Get announcements successful", content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Post.class)))})})
  public ResponseEntity<List<Post>> getAnnouncementPosts() {
    return ResponseEntity.ok(postService.getAnnouncements());
  }

  @PatchMapping("/{postId}")
  @Operation(summary = "Update post content", description = "Endpoint to update the content of a post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update post successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<Post> updatePost(HttpServletRequest servlet, @PathVariable String postId,
      @RequestBody String content) {
    return ResponseEntity.ok(postService.updateContent(servlet.getRemoteUser(), postId, content));
  }

  @PatchMapping("/{postId}/hits")
  @Operation(summary = "Update post hits", description = "Endpoint to update the hit count of a post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Update hits successful", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<Post> updateHits(HttpServletRequest servlet, @PathVariable String postId) {
    return ResponseEntity.ok(postService.updateHits(servlet.getRemoteUser(), postId));
  }

  @DeleteMapping("/{postId}")
  @Operation(summary = "Delete post", description = "Endpoint to delete a post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Delete post successful", content = @Content),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)})
  public ResponseEntity<Void> delete(HttpServletRequest servlet, @PathVariable String postId) {
    postService.deletePost(servlet.getRemoteUser(), postId);
    return ResponseEntity.noContent().build();
  }
}