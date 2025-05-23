package com.rest_au_rant.utils;


import com.rest_au_rant.exception.UnauthorizedException;
import com.rest_au_rant.model.Restaurant;
import com.rest_au_rant.model.user.User;
import com.rest_au_rant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PermissionUtil {

    private static UserRepository userRepository = null;

    @Autowired
    public PermissionUtil(UserRepository userRepository) {
        PermissionUtil.userRepository = userRepository;
    }

    public static void checkRestaurantPermission(Restaurant restaurant) throws UnauthorizedException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> claims = jwtToken.getTokenAttributes();

        // Assuming the user ID is stored under "sub", "user_id", or similar
        String userEmail = claims.get("email").toString();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        if (restaurant.getManager().getId() != user.getId()) {
            throw new UnauthorizedException(
                    String.format("User %s is not authorized to make changes to restaurant %s",
                            user.getId(), restaurant.getId())
            );
        }
    }

}
