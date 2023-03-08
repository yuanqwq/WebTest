package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;

import java.io.*;

@MultipartConfig
@WebServlet("/file")
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/png");

        OutputStream outputStream=resp.getOutputStream();
        InputStream inputStream= Resources.getResourceAsStream("home.png");
        IOUtils.copy(inputStream,outputStream);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(FileOutputStream stream=new FileOutputStream("C:\\Users\\QwQ\\IdeaProjects\\WebTest\\src\\main\\resources\\test.png")){
            Part part=req.getPart("test-file");
            IOUtils.copy(part.getInputStream(),stream);
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("文件上传成功");
        }
    }
}
