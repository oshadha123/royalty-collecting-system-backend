package org.osca.controller;

import com.google.gson.Gson;
import org.osca.controller.auth.JWebToken;
import org.osca.controller.httpRequest.HeaderAndBody;
import org.osca.model.ShowOrganizer;
import org.osca.service.ImageService;
import org.osca.service.ShowOrganizerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "MemAndSODashboardServlet", value = "/MemAndSODashboardServlet")
public class MemAndSODashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HeaderAndBody data = new HeaderAndBody();

        String header = data.getAuthenticationHeader(request);

        String token = header.substring(7);


        JWebToken tokennObj = null;
        try {
            tokennObj = new JWebToken(token);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert tokennObj != null;
        int uid = tokennObj.getUserID(token);
        int userType = tokennObj.getUserType(token);

        ShowOrganizer sa = new ShowOrganizer();
        System.out.println(userType);

        if (userType == 5){

            ShowOrganizerService SOserivice=new ShowOrganizerService();
            ArrayList<String> details = new ArrayList<>();

            try {
                details = SOserivice.getDashboardDetails(uid);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ImageService dp = new ImageService();
            String path = null;

            try {
                path = dp.getUserDP(uid);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            sa.setUserType(Integer.parseInt(details.get(0)));
            sa.setUid(uid);
            sa.setFname(details.get(1));
            sa.setLname(details.get(2));
            sa.setEmail(details.get(3));
            sa.setPhone(details.get(4));
            sa.setAcceptedLicense(Integer.parseInt(details.get(5)));
            sa.setRejectedLicense(Integer.parseInt(details.get(6)));
            sa.setNextEventDate(details.get(7));
            sa.setDPpath(path);

        }


        Gson gson = new Gson();
        System.out.println(sa);
        String saobj =gson.toJson(sa);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(saobj);




    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
