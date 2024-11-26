package org.themullers.vocab;

import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class Security {

    protected String passwordFile;

    public Security(@Value("${password.file}") String passwordFile) {
        this.passwordFile = passwordFile;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new HtpasswdUserDetailsService(passwordFile);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ApacheMd5PasswordEncoder();
    }

    public class HtpasswdUserDetailsService implements UserDetailsService {

        private String passwordFile;

        public HtpasswdUserDetailsService(String passwordFile) {
            this.passwordFile = passwordFile;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Map<String, String> credentials = loadHtpasswd();

            String passwordHash = credentials.get(username);
            if (passwordHash == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return User.builder()
                    .username(username)
                    .password(passwordHash)
                    .roles("USER")
                    .build();
        }

        private Map<String, String> loadHtpasswd() {
            Map<String, String> credentials = new HashMap<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.contains(":")) continue;

                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        credentials.put(parts[0], parts[1]);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to read password file", e);
            }

            return credentials;
        }
    }

    public static class ApacheMd5PasswordEncoder implements PasswordEncoder {

        @Override
        public String encode(CharSequence rawPassword) {
            // Use Apache's Md5Crypt to hash the raw password
            return Md5Crypt.apr1Crypt(rawPassword.toString());
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            // Compare raw password (hashed) with the provided encoded password
            return Md5Crypt.apr1Crypt(rawPassword.toString(), encodedPassword).equals(encodedPassword);
        }
    }

}
