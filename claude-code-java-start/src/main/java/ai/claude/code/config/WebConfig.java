package ai.claude.code.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 将 docs/study/ 目录映射到 /study/** URL，
 * 使 Web Playground 顶栏可直接跳转到学习指南和 API 文档页面。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${claude.workdir:${user.dir}}")
    private String workDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String docsPath = "file:" + workDir + "/docs/study/";
        registry.addResourceHandler("/study/**")
                .addResourceLocations(docsPath);
    }
}
