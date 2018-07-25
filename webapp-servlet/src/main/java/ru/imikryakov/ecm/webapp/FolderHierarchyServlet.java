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
import java.util.Enumeration;

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
        switch (action) {
            case "show": {
                hierarchy.exportToJSON(resp.getWriter(), logger);
                return;
            }
        }

        Enumeration<String> e = req.getAttributeNames();
        while (e.hasMoreElements()) {
            logger.trace("attributeName " + e.nextElement());
        }

        e = req.getParameterNames();
        while (e.hasMoreElements()) {
            logger.trace("parameterName " + e.nextElement());
        }

        resp.sendRedirect("show.html");
    }
}
