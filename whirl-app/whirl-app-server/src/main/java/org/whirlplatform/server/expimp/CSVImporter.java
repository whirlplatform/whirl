package org.whirlplatform.server.expimp;

import au.com.bytecode.opencsv.CSVReader;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CSVImporter extends Importer {

	private static Logger _log = LoggerFactory.getLogger(CSVImporter.class);

	public CSVImporter(Connector connector, ApplicationUser user,
			ClassMetadata metadata) {
		this.connector = connector;
		this.user = user;
		this.metadata = metadata;
	}

	public void importFromStream(InputStream input) throws IOException {
		CSVReader reader = new CSVReader(new InputStreamReader(input, "UTF-8"),
				';');
		String[] header = reader.readNext();
		int size = header.length;
		Map<Integer, FieldMetadata> headerMap = new HashMap<Integer, FieldMetadata>();

		for (int i = 0; i < header.length; i++) {
			for (FieldMetadata f : metadata.getFields()) {
				String column = header[i];
				if (column != null
						&& ((f.getName() != null && column.equalsIgnoreCase(f
								.getName().trim())) || (f.getRawLabel() != null && column
								.equalsIgnoreCase(f.getRawLabel().trim())))) {
					headerMap.put(i, f);
				}
			}
		}
		String[] line = reader.readNext();
		do {
			if (line.length != size) {
				break;
			}
			Map<FieldMetadata, String> data = new HashMap<FieldMetadata, String>();
			for (int i = 0; i < line.length; i++) {
				data.put(headerMap.get(i), line[i]);
			}
			try {
				insert(data);
			} catch (CustomException e) {
				_log.error(e);
				importError = true;
			}

			line = reader.readNext();

			Thread.yield();
		} while (line != null);

	}

}
