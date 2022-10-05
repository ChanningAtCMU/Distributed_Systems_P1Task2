package ds.project1task2;

/**
 * Author: Changzhou (Channing) Zheng (changzhz)
 * Last Modified: September 24, 2022
 *
 * This servlet get state information results by using methods from stateInfoModel.java
 * Upon getting the results, it parses the results to the front-end handled by result.jsp
 * All 7 categories of information are included (state name, nickname, capital, song, flower, flag, and population)
 */

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "stateInfoServlet", urlPatterns = {"/getStateName"})
public class stateInfoServlet extends HttpServlet {

    stateInfoModel stateModel = new stateInfoModel();

    private String message;

    public void init() {message = "Welcome to States Info Search!";}

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        // Get state name as a parameter
        String state = request.getParameter("stateName");

        // Pass nickname result to clients
        String nickname = stateModel.getNicknames(state);
        request.setAttribute("nickname", nickname);

        // Pass capital name result to clients
        String capital = stateModel.getCapital(state);
        request.setAttribute("capital", capital);

        // Pass state song results to clients
        String song = stateModel.getSong(state);
        request.setAttribute("song", song);

        // Pass state flower result to clients
        String flower = stateModel.getFlower(state);
        request.setAttribute("flower", flower);

        // Pass flag result to clients
        String flag = stateModel.getFlag(state);
        request.setAttribute("flag", flag);

        // Pass population result to clients
        String pop = stateModel.getPop(state);
        request.setAttribute("pop", pop);

        RequestDispatcher view = request.getRequestDispatcher("result.jsp");
        view.forward(request, response);
    }


}
