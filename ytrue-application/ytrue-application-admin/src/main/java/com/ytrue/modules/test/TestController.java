package com.ytrue.modules.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "部门管11理")
@RestController
@RequestMapping("test")
@AllArgsConstructor
public class TestController {

    @Operation(summary = "定时任务22信息")
    @GetMapping("test")
    public String test11() {
        return "test";
    }
}
