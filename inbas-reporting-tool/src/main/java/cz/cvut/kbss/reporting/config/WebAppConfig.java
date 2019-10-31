package cz.cvut.kbss.reporting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.reporting.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@Import({RestConfig.class, SecurityConfig.class})
public class WebAppConfig extends WebMvcConfigurerAdapter {

    private final ObjectMapper objectMapper;

    private final ObjectMapper jsonLdMapper;

    @Autowired
    public WebAppConfig(@Qualifier("objectMapper") ObjectMapper objectMapper,
                        @Qualifier("jsonLdMapper") ObjectMapper jsonLdMapper) {
        this.objectMapper = objectMapper;
        this.jsonLdMapper = jsonLdMapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/").setCacheControl(
                CacheControl.maxAge(7, TimeUnit.DAYS));
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCacheControl(
                CacheControl.maxAge(24, TimeUnit.HOURS));
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(createJsonLdMessageConverter());
        converters.add(createDefaultMessageConverter());
        final StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(Charset.forName(
                Constants.UTF_8_ENCODING));
        converters.add(stringConverter);
        converters.add(new ResourceHttpMessageConverter());
        super.configureMessageConverters(converters);
    }

    private HttpMessageConverter<?> createJsonLdMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(jsonLdMapper);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.valueOf(JsonLd.MEDIA_TYPE)));
        return converter;
    }

    private HttpMessageConverter<?> createDefaultMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer matcher) {
        matcher.setUseSuffixPatternMatch(false);
    }
}
