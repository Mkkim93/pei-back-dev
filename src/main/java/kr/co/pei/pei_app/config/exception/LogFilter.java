package kr.co.pei.pei_app.config.exception;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[LOG START] ");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        try {
            log.info("[LOG REQUEST] [{}][{}][{}] ", uuid, servletRequest.getDispatcherType(), requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.info("[LOG EX]: {}", e.getMessage());
            throw e;
        } finally {
            log.info("[LOG RESPONSE] [{}][{}][{}] ", uuid, servletRequest.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("[LOG END]");
    }
}
