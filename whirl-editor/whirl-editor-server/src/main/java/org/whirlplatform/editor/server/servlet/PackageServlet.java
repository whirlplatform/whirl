package org.whirlplatform.editor.server.servlet;

import com.google.inject.Inject;
import org.whirlplatform.editor.server.packager.Packager;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

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
			ApplicationElement application = metadataStore
					.loadApplication(data.getCode(), data.getVersion());
			if (application == null) {
				resp.sendError(HttpServletResponse.SC_NO_CONTENT,
						"Application for import have not chosen");
				return;
			}
			ZipOutputStream out = new ZipOutputStream(resp.getOutputStream());
			packager.make(application, data.getVersion(), out);
			out.flush();
			out.finish();
			out.close();

			req.getSession().removeAttribute(Packager.SESSION_KEY);
		} catch (MetadataStoreException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			_log.error("Package creating exception", e);
		}
	}

}
