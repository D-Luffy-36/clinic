package com.an.DentalClinicSystem.filter;

import com.an.DentalClinicSystem.service.CustomUserDetails;
import com.an.DentalClinicSystem.service.CustomUserDetailsService;
import com.an.DentalClinicSystem.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private CustomUserDetailsService customUserDetailsService;


    public  JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            @Lazy CustomUserDetailsService customUserDetailsService)
    {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Lấy giá trị từ header Authorization (dạng: "Bearer <token>")
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2️⃣ Kiểm tra header có tồn tại và bắt đầu bằng "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 3️⃣ Cắt bỏ tiền tố "Bearer " để lấy token
            jwt = authHeader.substring(7);

            // 4️⃣ Trích xuất username từ token bằng JwtUtil
            username = jwtUtil.extractUsername(jwt);
        }

        // 5️⃣ Nếu có username và chưa có xác thực trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6️⃣ Tải thông tin người dùng từ DB thông qua CustomUserDetailsService
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // 7️⃣ Kiểm tra token có hợp lệ không (chữ ký đúng, chưa hết hạn, đúng người dùng)
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // 8️⃣ Tạo đối tượng xác thực chứa thông tin người dùng và quyền
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 9️⃣ Gắn thông tin chi tiết từ request vào đối tượng xác thực
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 🔟 Gắn đối tượng xác thực vào SecurityContext để Spring Security xử lý
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 🔚 Cho phép request đi tiếp trong chuỗi filter
        filterChain.doFilter(request, response);
    }
}
