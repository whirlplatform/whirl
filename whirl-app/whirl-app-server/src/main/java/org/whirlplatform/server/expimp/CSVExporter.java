package org.whirlplatform.server.expimp;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.empire.commons.StringUtils;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcherUtil;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.utils.ServerGetter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVExporter extends Exporter {

	private ClassMetadata metadata;

	private DBReader reader;

	private boolean columnHeader;

	private CSVWriter writer;

	public CSVExporter(ClassMetadata metadata, DBReader reader) {
		this.metadata = metadata;
		this.reader = reader;
	}

	public void setColumnHeader(boolean columnHeader) {
		this.columnHeader = columnHeader;
	}

    public void write(OutputStream stream) throws IOException {
		writer = new CSVWriter(new OutputStreamWriter(stream, "UTF-8"), ';');

		// первая строка это названия колонок
		List<String> header = new ArrayList<String>();
		for (FieldMetadata f : metadata.getFields()) {
			if (!f.isView()) {
				continue;
			}
			if (columnHeader) {
				header.add(f.getName());
			} else {
				header.add(f.getRawLabel());
			}
		}

		writer.writeNext(header.toArray(new String[0]));

		while (reader.moveNext()) {
			List<String> line = new ArrayList<String>();
			for (FieldMetadata f : metadata.getFields()) {
				if (!f.isView()) {
					continue;
				}
				String result = (String) getColumnValue(f, reader);
				if (!StringUtils.isEmpty(f.getViewFormat())) {
					result = decode(result);
				}
				line.add(result);
			}
			writer.writeNext(line.toArray(new String[0]));
			writer.flush();

			Thread.yield();
		}
	}

	@Override
	protected Object getColumnValue(FieldMetadata field,
                                    DBReader reader) {
		String result;
		if (DataType.LIST == field.getType()) {
			result = reader.getString(reader.getFieldIndex(field.getName()
					+ SrvConstant.COLUMN_LIST_POSTFIX));
		} else if (DataType.FILE == field.getType()) {
			result = reader.getString(reader.getFieldIndex(field.getName()
					+ SrvConstant.COLUMN_FILE_POSTFIX));
		} else {
			result = ServerGetter.getResultSetValue(reader,
					reader.getFieldIndex(field.getName()));
		}
//		if (!StringUtils.isEmpty(field.getViewFormat())) {
//			Map<String, String> map = DataFetcherUtil.fromURLEncoded(result);
//			result = map.get(AppConstant.VALUE);
//		}
		return result;
	}

	private static String decode(String encoded) {
//		Map<String, String> map = fromURLEncoded((String) encoded);
		Map<String, String> map = DataFetcherUtil.fromURLEncoded(encoded);
		return map.get(AppConstant.VALUE);
	}

	// Аналог метода из Getter, но он не работает на сервере
//	public static Map<String, String> fromURLEncoded(String value) {
//		Map<String, String> result = new FastMap<String>();
//		if (value != null) {
//			String[] parameters = value.split("&");
//			for (int i = 0; i < parameters.length; i++) {
//				if (!parameters[i].isEmpty()) {
//					String[] pair = parameters[i].split("=");
//					if (pair.length != 1) {
//						if (pair[1] == null) {
//							result.put(pair[0], null);
//						} else {
//							try {
//								result.put(pair[0],
//										URLDecoder.decode(pair[1], "UTF-8"));
//							} catch (UnsupportedEncodingException e) {
//							}
//						}
//
//					}
//				}
//			}
//		}
//		return result;
//	}
}
