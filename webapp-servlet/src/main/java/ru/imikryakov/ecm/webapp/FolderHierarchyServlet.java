package ru.imikryakov.ecm.webapp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.imikryakov.ecm.FolderHierarchyManager;
import ru.imikryakov.ecm.types.FolderHierarchy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FolderHierarchyServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger();
    private FolderHierarchy hierarchy;

    @Override
    public void init() throws ServletException {
        super.init();
        hierarchy = FolderHierarchyManager.get().createExisting();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (hierarchy != null) {
            hierarchy.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.trace("query string " + req.getQueryString());

        String action = req.getParameter("action");

        if (action == null) {
            resp.sendRedirect("show.html");
            return;
        }

        switch (action) {
            case "show": {
                hierarchy.exportToJson(resp.getWriter(), logger);
                break;
            }
            case "createFolder": {
                String name = req.getParameter("name");
                if (name == null) {
                    resp.getWriter().write("Please specify name.");
                    return;
                }
                hierarchy.createFolder(name);
                hierarchy.exportToJson(resp.getWriter(), logger);
                break;
            }
            case "createDocument": {
                String name = req.getParameter("name");
                if (name == null) {
                    resp.getWriter().write("Please specify name.");
                    return;
                }
                hierarchy.createDocument(name);
                hierarchy.exportToJson(resp.getWriter(), logger);
                break;
            }
            case "goTo": {
                String name = req.getParameter("name");
                if (name == null) {
                    resp.getWriter().write("Please specify name.");
                    return;
                }
                hierarchy.goToFolder(name);
                hierarchy.exportToJson(resp.getWriter(), logger);
                break;
            }
            case "up": {
                hierarchy.up();
                hierarchy.exportToJson(resp.getWriter(), logger);
                break;
            }
            default: {
                resp.getWriter().write("Invalid command.");
            }
        }
    }
}
