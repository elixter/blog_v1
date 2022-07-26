package elixter.blog.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class MailTemplateConfig {

    @Value("${template.mail.prefix}")
    private String prefix;

    @Value("${template.mail.suffix}")
    private String suffix;

    @Value("${template.mail.template-mode}")
    private String templateMode;

    @Value("${template.mail.cacheable}")
    private boolean cacheable;

    @Value("${template.mail.encoding}")
    private String encoding;



    @Bean
    @Qualifier("mailTemplateResolver")
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(prefix);
        templateResolver.setSuffix(suffix);
        templateResolver.setCacheable(cacheable);
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setCharacterEncoding(encoding);

        return templateResolver;
    }

    @Bean
    @Qualifier("mailTemplateEngine")
    public TemplateEngine templateEngine() {

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }
}
