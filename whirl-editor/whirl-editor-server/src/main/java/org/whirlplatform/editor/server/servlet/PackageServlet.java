package org.whirlplatform.editor.server.servlet;

import com.google.inject.Inject;
import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.whirlplatform.editor.server.packager.Packager;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

@Singleton
public class PackageServlet extends HttpServlet {

    private static final long serialVersionUID = -3540016579210003201L;

    private Logger _log = LoggerFactory.getLogger(PackageServlet.class);

    private Packager packager;
    private MetadataStore metadataStore;

    @Inject
    public PackageServlet(Packager packager, MetadataStore metadataStore) {
        this.packager = packager;
        this.metadataStore = metadataStore;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {

        resp.setHeader("Content-disposition",
            "attachment; filename=\"package.zip\"");
        resp.setContentType("application/zip");

        ApplicationStoreData data = (ApplicationStoreData) req.getSession()
            .getAttribute(Packager.SESSION_KEY);

        try {
            metadataStore.packageToZip(data.getCode(), data.getVersion(),
                resp.getOutputStream());
        } catch (MetadataStoreException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            _log.error("Package creating exception", e);
        }
    }

}
