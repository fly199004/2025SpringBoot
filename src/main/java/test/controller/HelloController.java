package test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2025/3/11.
 */
@RestController
public class HelloController {
    @GetMapping("/h")
    public  String hello(){
        return  "Hello";
    }
}
