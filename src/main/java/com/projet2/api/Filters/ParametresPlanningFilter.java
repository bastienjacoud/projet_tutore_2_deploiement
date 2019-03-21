package com.projet2.api.Filters;

import com.projet2.api.Services.IParametresApplicationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

public class ParametresPlanningFilter implements Filter {

    @Autowired
    private IParametresApplicationService parametresApplicationService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean choicesAvailable;
        try {
            choicesAvailable = parametresApplicationService.schedulesAvailable();
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
        if(!choicesAvailable){
            throw new ServletException("Les plannings ne sont pas disponibles actuellement.");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
