package org.whirlplatform.server.expimp;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A EXPORT_XLS processor, that uses the MissingRecordAware EventModel code to
 * ensure it outputs all columns and rows.
 * 
 * @author Nick Burch
 */
public class XLSImporter extends Importer implements HSSFListener {

	private static Logger _log = LoggerFactory.getLogger(XLSImporter.class);

	private Map<Integer, FieldMetadata> headerMap = new HashMap<Integer, FieldMetadata>();

	/** Should we output the formula, or the value it has? */
	private boolean outputFormulaValues = true;

	/** For parsing Formulas */
	private SheetRecordCollectingListener workbookBuildingListener;

	private FormatTrackingHSSFListener formatListener;

	// For handling formulas with string results
	private int nextRow;
	private int nextColumn;
	private boolean outputNextStringRecord;

	private SSTRecord sstRecord;

	private Map<Integer, String> currentValues = new HashMap<Integer, String>();

	public XLSImporter(Connector connector, ApplicationUser user,
			ClassMetadata metadata) {
		this.connector = connector;
		this.user = user;
		this.metadata = metadata;
	}

	/**
	 * Initiates the processing of the EXPORT_XLS file
	 */
	public void process(POIFSFileSystem fs) throws IOException {
		MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
				this);
		formatListener = new FormatTrackingHSSFListener(listener);

		HSSFEventFactory factory = new HSSFEventFactory();
		HSSFRequest request = new HSSFRequest();

		if (outputFormulaValues) {
			request.addListenerForAllRecords(formatListener);
		} else {
			workbookBuildingListener = new SheetRecordCollectingListener(
					formatListener);
			request.addListenerForAllRecords(workbookBuildingListener);
		}

		factory.processWorkbookEvents(request, fs);
	}

	/**
	 * Main HSSFListener method, processes events, and outputs the EXPORT_CSV as
	 * the file is processed.
	 */
	public void processRecord(Record record) {
		int thisRow = -1;
		int thisColumn = -1;
		String thisStr = null;

		switch (record.getSid()) {
		case BlankRecord.sid:
			BlankRecord brec = (BlankRecord) record;

			thisRow = brec.getRow();
			thisColumn = brec.getColumn();
			thisStr = "";
			break;
		case BoolErrRecord.sid:
			BoolErrRecord berec = (BoolErrRecord) record;

			thisRow = berec.getRow();
			thisColumn = berec.getColumn();
			thisStr = "";
			break;

		case FormulaRecord.sid:
			FormulaRecord frec = (FormulaRecord) record;

			thisRow = frec.getRow();
			thisColumn = frec.getColumn();

			if (Double.isNaN(frec.getValue())) {
				// Formula result is a string
				// This is stored in the next record
				outputNextStringRecord = true;
				nextRow = frec.getRow();
				nextColumn = frec.getColumn();
			} else {
				thisStr = formatListener.formatNumberDateCell(frec);
			}
			break;
		case StringRecord.sid:
			if (outputNextStringRecord) {
				// String for formula
				StringRecord srec = (StringRecord) record;
				thisStr = srec.getString();
				thisRow = nextRow;
				thisColumn = nextColumn;
				outputNextStringRecord = false;
			}
			break;
		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;

			thisRow = lrec.getRow();
			thisColumn = lrec.getColumn();
			thisStr = lrec.getValue();
			break;
		case NumberRecord.sid:
			NumberRecord numrec = (NumberRecord) record;

			thisRow = numrec.getRow();
			thisColumn = numrec.getColumn();

			// Format
			thisStr = formatListener.formatNumberDateCell(numrec);
			break;
		case SSTRecord.sid:
			sstRecord = (SSTRecord) record;
			break;
		case LabelSSTRecord.sid:
			LabelSSTRecord lsrec = (LabelSSTRecord) record;
			thisRow = lsrec.getRow();
			thisColumn = lsrec.getColumn();
			if (sstRecord == null) {
				throw new IllegalStateException("No SST record found");
			}
			thisStr = sstRecord.getString(lsrec.getSSTIndex()).toString();
			break;
		default:
			break;
		}

		// Handle missing column
		if (record instanceof MissingCellDummyRecord) {
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			thisStr = "";
		}

		// если это первая строка, то сохраняем как заголовок
		if (thisRow == 0 && thisColumn >= 0) {
			String column = thisStr.toUpperCase();
			for (FieldMetadata f : metadata.getFields()) {
				if (column != null
						&& (column.equalsIgnoreCase(f.getName()) || column
								.equalsIgnoreCase(f.getRawLabel()))) {
					headerMap.put(thisColumn, f);
				}
			}
		}
		// сохраняем значение
		if (thisRow > 0 && thisColumn >= 0) {
			currentValues.put(thisColumn, thisStr);
		}

		// Handle end of row
		if (record instanceof LastCellOfRowDummyRecord) {
			if (currentValues.size() > 0) {
				Map<FieldMetadata, String> data = new HashMap<FieldMetadata, String>();
				for (Integer key : headerMap.keySet()) {
					data.put(headerMap.get(key), currentValues.get(key));
				}
				try {
					insert(data);
				} catch (CustomException e) {
					_log.error(e);
					importError = true;
				}
				currentValues.clear();
			}
		}

		Thread.yield();
	}

	@Override
	public void importFromStream(InputStream input) throws IOException {
		process(new POIFSFileSystem(input));
	}

}