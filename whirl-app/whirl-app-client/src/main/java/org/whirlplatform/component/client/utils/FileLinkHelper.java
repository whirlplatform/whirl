package org.whirlplatform.component.client.utils;

import com.google.gwt.core.client.GWT;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.rpc.shared.SessionToken;

public class FileLinkHelper {

	public static String getTableFileLinkById(String tableId, String column,
			String rowId) {
		String fileTypeParam = AppConstant.GETTYPE + "=" + AppConstant.TABLE;
		String nameParam = AppConstant.FIELD + "=" + column;
		String classListParam = AppConstant.TABLE_ID + "=" + tableId;
		String objParem = AppConstant.ID + "=" + rowId;
		return GWT.getHostPageBaseURL() + "file?" + fileTypeParam + "&"
				+ nameParam + "&" + classListParam + "&" + objParem + "&"
				+ AppConstant.TOKEN_ID + "=" + SessionToken.get().getTokenId();
	}

	public static String getTableFileLinkByCode(String tableCode,
			String column, String rowId) {
		String fileTypeParam = AppConstant.GETTYPE + "=" + AppConstant.TABLE;
		String nameParam = AppConstant.FIELD + "=" + column;
		String classListParam = AppConstant.TABOLE_CODE + "=" + tableCode;
		String objParem = AppConstant.ID + "=" + rowId;
		return GWT.getHostPageBaseURL() + "file?" + fileTypeParam + "&"
				+ nameParam + "&" + classListParam + "&" + objParem + "&"
				+ AppConstant.TOKEN_ID + "=" + SessionToken.get().getTokenId();
	}

}
