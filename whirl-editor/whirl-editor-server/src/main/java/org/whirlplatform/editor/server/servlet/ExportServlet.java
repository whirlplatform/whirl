package org.whirlplatform.editor.server.servlet;

import com.google.inject.Singleton;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

@Singleton
public class ExportServlet extends HttpServlet {

    private static final long serialVersionUID = -8640171414726450396L;

    private MetadataStore metadataStore;

    @Inject
    public ExportServlet(MetadataStore applicationStore) {
        this.metadataStore = applicationStore;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        doExport(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        doExport(req, resp);
    }

    public void doExport(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        ServletOutputStream out = resp.getOutputStream();
        resp.setContentType("application/xml;");
        resp.setCharacterEncoding("UTF-8");

        ApplicationElement app = (ApplicationElement) req.getSession()
            .getAttribute("APPLICATION");

        String code = "application";
        if (app.getCode() != null) {
            code = app.getCode();
        }
        resp.setHeader("Content-Disposition",
            "attachment;filename=\"application-" + code + ".xml\"");
        try {
            out.write(metadataStore.serialize(app).getBytes("UTF-8"));
            out.flush();
        } catch (MetadataStoreException e) {
            throw new IOException(e);
        }
    }
}
