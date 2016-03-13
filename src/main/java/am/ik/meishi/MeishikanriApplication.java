package am.ik.meishi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.LinkedHashMap;

@SpringBootApplication
@RestController
public class MeishikanriApplication {

    @Autowired
    MeishiUser meishiUser;

    @RequestMapping(path = {"/me", "/user"})
    Object me() {
        return new LinkedHashMap<Object, Object>() {
            {
                put("userId", meishiUser.getUserId());
                put("name", meishiUser.getDisplayName());
                put("email", meishiUser.getEmail());
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(MeishikanriApplication.class, args);
    }

    @EnableOAuth2Sso
    @Configuration
    @EnableWebSecurity
    static class C extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/**").authenticated()
                    .and()
                    .headers().cacheControl().disable()
                    .and()
                    .logout().logoutSuccessUrl("/");
        }
    }

    @Configuration
    static class M extends WebMvcConfigurerAdapter {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addViewController("/").setViewName("redirect:/meishi");
        }
    }
}
