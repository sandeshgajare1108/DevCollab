
package com.DevCollab.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

            // =========================================
            // CSRF
            // =========================================
            .csrf()
            .disable()

            // =========================================
            // CORS
            // =========================================
            .cors()
            .and()

            // =========================================
            // SESSION
            // =========================================
            .sessionManagement()
            .sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
            )

            .and()

            // =========================================
            // AUTHORIZATION
            // =========================================
            .authorizeRequests()

            // =========================================
            // AUTH
            // =========================================
            .antMatchers("/api/auth/**")
            .permitAll()

            // =========================================
            // REGISTER
            // =========================================
            .antMatchers("/api/users/register")
            .permitAll()
            

            // =========================================
            // H2
            // =========================================
            .antMatchers("/h2-console/**")
            .permitAll()

            // =========================================
            // JSP + STATIC
            // =========================================
            //"/project.jsp",
            //   "/settings.jsp",
            .antMatchers(
            		"/login.jsp",
                "/forgot-password.jsp",
                "/reset-password.jsp",
                "/register.jsp",
                "/dashboard.jsp",
                "/settings.jsp",
                "/project-details.jsp",
                "/edit-project.jsp",
                "/project.jsp",
                "/tasks.jsp",
                "/task-details.jsp",
                "/edit-task.jsp",
                "/team.jsp",
                "/profile.jsp",
                "/notifications.jsp",
                "/bugs.jsp",
                "/team-chat.jsp",
                "/css/**",
                "/js/**",
                "/ws",
                "/ws/**",
                "/images/**",
                "/favicon.ico"
                
            )
            .permitAll()
            .antMatchers(
                    "/api/ai-reviews/**"
            )
            .authenticated()

            // =========================================
            // ADMIN
            // =========================================
            .antMatchers("/api/admin/**")
            .hasRole("ADMIN")

            // =========================================
            // PROJECTS
            // =========================================
            .antMatchers("/api/projects/**")
            .authenticated()

            // =========================================
            // PROJECT MEMBERS
            // =========================================
            .antMatchers("/api/project-members/**")
            .authenticated()

            // =========================================
            // TASKS
            // =========================================
            .antMatchers("/api/tasks/**")
            .authenticated()
            .antMatchers("/api/github/**").authenticated()
            .antMatchers("/api/pull-requests/**").authenticated()
          
            .antMatchers("/api/ai-reviews/**").authenticated()
            .antMatchers("/api/code-reviews/**").authenticated()
            .antMatchers("/api/bugs/**").authenticated()
            .antMatchers("/api/auth/**").permitAll()
            
            .antMatchers(
            	    "/ws",
            	    "/ws/**"
            	).permitAll()

            // =========================================
            // DASHBOARD
            // =========================================
            .antMatchers("/api/dashboard/**")
            .authenticated()

            // =========================================
            // EVERYTHING ELSE
            // =========================================
            .anyRequest()
            .authenticated();

        // =========================================
        // JWT FILTER
        // =========================================

        http.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}
