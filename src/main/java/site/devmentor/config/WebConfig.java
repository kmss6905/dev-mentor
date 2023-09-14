package site.devmentor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.devmentor.argument.LoginUserArgumentResolver;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.auth.post.PostAuthorizationInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final PostRepository postRepository;

  public WebConfig(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new LoginUserArgumentResolver());
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new PostAuthorizationInterceptor(postRepository))
            .addPathPatterns("/api/posts/**");
  }
}
