package bauction.web.filters;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoggedInUserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");

        if (sci != null) {
            UserDetails cud = (UserDetails) sci.getAuthentication().getPrincipal();

            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect("/home");
            return;
        }
        chain.doFilter(request,  response);
    }
}
