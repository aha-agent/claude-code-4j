package ai.claude.code.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置。
 * 页面路由由 PageController 处理（/、/api、/playground）。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
}
