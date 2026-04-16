package ai.claude.code.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 网站主页面路由：
 *   GET /            → 首页（docs/study/index.html，实时读取，修改后刷新即生效）
 *   GET /api         → API 文档（docs/study/api.html）
 *   GET /playground  → Playground 执行界面（static/playground.html）
 */
@Controller
public class PageController {

    @Value("${claude.workdir:${user.dir}}")
    private String workDir;

    @GetMapping("/")
    public ResponseEntity<String> home() throws IOException {
        String html = Files.readString(Paths.get(workDir, "docs", "study", "index.html"));
        return htmlResponse(html);
    }

    @GetMapping("/api")
    public ResponseEntity<String> apiDocs() throws IOException {
        String html = Files.readString(Paths.get(workDir, "docs", "study", "api.html"));
        return htmlResponse(html);
    }

    @GetMapping("/playground")
    public ResponseEntity<String> playground() throws IOException {
        ClassPathResource res = new ClassPathResource("static/playground.html");
        try (InputStream is = res.getInputStream()) {
            String html = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return htmlResponse(html);
        }
    }

    private ResponseEntity<String> htmlResponse(String html) {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}
