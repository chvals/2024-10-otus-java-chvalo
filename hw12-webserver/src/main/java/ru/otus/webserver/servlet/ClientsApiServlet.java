package ru.otus.webserver.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.db.crm.model.Client;
import ru.otus.db.crm.service.DBServiceClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private final transient DBServiceClient DBServiceClient;
    private final transient ObjectMapper mapper;

    public ClientsApiServlet(DBServiceClient DBServiceClient, ObjectMapper mapper) {
        this.DBServiceClient = DBServiceClient;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Client> clients = DBServiceClient.findAll();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(mapper.writeValueAsString(clients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getContentType();
        if (!("application/json".equals(contentType))) {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "Invalid content type");
            return;
        }
        try (BufferedReader reader = request.getReader()) {
            Client newClient = DBServiceClient.saveClient(mapper.readValue(reader, Client.class));
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(mapper.writeValueAsString(newClient));
        }
    }
}
