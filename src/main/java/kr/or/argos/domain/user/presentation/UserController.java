package kr.or.argos.domain.user.presentation;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    @GetMapping("/{userId}")
    public ResponseEntity getMethodName(@PathVariable(name = "userId") Long userId) {
        return null;
    }
    
    @PostMapping("/")
    public ResponseEntity addUser(@RequestBody Object newUser) {
        return null;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity updatePassword(
        @PathVariable(name = "userId") Long userId,
        @RequestBody Object updatedPassword
    ) {
        return null;
    }

}
