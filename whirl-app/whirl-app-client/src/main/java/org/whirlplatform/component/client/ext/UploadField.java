package org.whirlplatform.component.client.ext;

import com.google.gwt.user.client.Random;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import org.whirlplatform.meta.shared.AppConstant;

public class UploadField extends FileUploadField {

	private String fileId = null;

	public UploadField() {
		super();
		fileId = AppConstant.FORM_UPLOAD + "_FILE_"
				+ String.valueOf(System.currentTimeMillis())
				+ String.valueOf(Random.nextInt());
		setName("file");
	}

	public String getFileId() {
		return fileId;
	}

}
