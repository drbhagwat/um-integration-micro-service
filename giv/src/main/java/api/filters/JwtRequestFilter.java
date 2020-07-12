package api.filters;

import api.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  @Autowired
  JwtTokenUtil jwtTokenUtil;

  private String userName = null;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                  FilterChain filterChain) throws ServletException, IOException {
    final String authorizationHeader = httpServletRequest.getHeader("Authorization");
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
      jwt = authorizationHeader.substring(7);
      userName = jwtTokenUtil.getUsernameFromToken(jwt);
    }
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
