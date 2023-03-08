package com.example.servlet;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Log

@WebServlet(value = "/login",loadOnStartup = 1)
public class LoginServlet extends HttpServlet {
    private static SqlSessionFactory factory;
    @Override
    public void init() throws ServletException {
        try {
            factory=new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.setContentType("text/html;charset=UTF-8");
        Map<String,String[]> map=req.getParameterMap();
        if(map.containsKey("username")&&map.containsKey("password")){
            String username=req.getParameter("username");
            String password=req.getParameter("password");
            try(SqlSession session=factory.openSession(true)){
                UserMapper mapper=session.getMapper(UserMapper.class);
                User user=mapper.getUser(username,password);

                if(user!=null){
                    if(map.containsKey("remember-me")){
                        Cookie cookie_username=new Cookie("username",username);
                        Cookie cookie_password=new Cookie("password",password);
                        resp.addCookie(cookie_username);
                        resp.addCookie(cookie_password);
                    }
                    HttpSession httpSession=req.getSession();
                    httpSession.setAttribute("user",user);

                    resp.sendRedirect("time");
                }else{
                    resp.getWriter().write("密码错误或用户不存在");
                }
            }
        }else{
            resp.getWriter().write("表单不完整");
        }



    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("servlet");
        resp.setContentType("text/html;charset=UTF-8");
        Map<String,String[]> map=req.getParameterMap();
        Cookie[] cookies=req.getCookies();
        if(cookies!=null){
            String username="";
            String password="";
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("username"))
                    username=cookie.getValue();
                if(cookie.getName().equals("password"))
                    password=cookie.getValue();
            }
            try(SqlSession session=factory.openSession(true)){
                UserMapper mapper=session.getMapper(UserMapper.class);
                User user=mapper.getUser(username,password);
                if(user!=null){
                    HttpSession httpSession=req.getSession();
                    httpSession.setAttribute("user",user);

                    resp.sendRedirect("time");
                    return;
                }else{
                    Cookie cookie_username=new Cookie("username",username);
                    Cookie cookie_password=new Cookie("password",password);
                    cookie_password.setMaxAge(0);
                    cookie_username.setMaxAge(0);
                    resp.addCookie(cookie_username);
                    resp.addCookie(cookie_password);
                }

            }

        }
        req.getRequestDispatcher("/").forward(req,resp);
    }
}
