package org.whirlplatform.component.client.report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.HasCreateParameters;
import org.whirlplatform.component.client.form.FieldFormPanel;
import org.whirlplatform.component.client.utils.DPIHelper;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.storage.client.StorageHelper;
import org.whirlplatform.storage.client.StorageHelper.StorageWrapper;

import java.util.*;
import java.util.Map.Entry;

/**
 * Построитель формы ввода параметров для отчёта.
 */
@JsType(name = "Report", namespace = "Whirl")
public class ReportBuilder extends ComponentBuilder implements
		HasCreateParameters {
	
	private static final String STORAGE_PREFIX = "report-params-";

	private VerticalLayoutContainer container;

	private FieldFormPanel panel;

	private Map<FieldMetadata, DataValue> paramValues;

	private boolean showReportParams;

	private List<DataValue> startParameters;

	private String reportFormat;

	@JsConstructor
	public ReportBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public ReportBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.ReportType;
	}
	
	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		showReportParams = true;
		container = new VerticalLayoutContainer();
		return container;
	}

	@Override
	public Component create() {
        return create(Collections.emptyList());
	}

	@JsIgnore
	@Override
	public Component create(List<DataValue> parameters) {
		Component result = super.create();
		startParameters = parameters;
		loadRptParams();
		return result;
	}

	private void loadRptParams() {
		AsyncCallback<List<FieldMetadata>> callback = new AsyncCallback<List<FieldMetadata>>() {

			@Override
			public void onSuccess(List<FieldMetadata> result) {
				Map<String, DataValue> values = new HashMap<String, DataValue>();
				StorageWrapper<HashMap<String, DataValue>> storage = StorageHelper.local();
				if (storage != null) {
					values = storage.get(STORAGE_PREFIX + getId());
				}
				paramValues = new HashMap<FieldMetadata, DataValue>();
				for (FieldMetadata f : result) {
					DataValue value = values == null ? null : values.get(f
							.getName());
					paramValues.put(f, value);
				}
				processRptParams();
			}

			@Override
			public void onFailure(Throwable caught) {
				InfoHelper.throwInfo("get-report-fields", caught);
			}
		};
		DataServiceAsync.Util.getDataService(callback).getReportFields(SessionToken.get(), getId());
	}

	private void processRptParams() {
		if (showReportParams) {
			List<FieldMetadata> fieldDataList = new ArrayList<FieldMetadata>(
					paramValues.keySet());

			panel = new FieldFormPanel(fieldDataList);

			if (startParameters.isEmpty()) {
				for (Entry<FieldMetadata, DataValue> entry : paramValues
						.entrySet()) {
					panel.setValue(entry.getKey(), entry.getValue());
				}
			} else {
				for (DataValue value : startParameters) {
					for (FieldMetadata field : fieldDataList) {
						if (field.getName().equalsIgnoreCase(value.getCode())) {
							panel.setValue(field, value);
							break;
						}
					}
				}
			}
			ButtonBar buttonBar = new ButtonBar();
			buildButtons(buttonBar);
			container.add(panel, new VerticalLayoutData(1, 1, new Margins(5, 5,
					5, 5)));
			container.add(buttonBar, new VerticalLayoutData(1, -1, new Margins(
					10, 15, 0, 0)));
		} else {
			Map<String, DataValue> values = new HashMap<String, DataValue>();
			for (DataValue param : startParameters) {
				values.put(param.getCode(), param);
			}
			saveParams(reportFormat, values);
		}
	}

	protected void buildButtons(ButtonBar buttonBar) {
		TextButton showRptBtn = new TextButton(
				AppMessage.Util.MESSAGE.report_makeHTML());
		showRptBtn.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				saveParamsFromFields(AppConstant.REPORT_FORMAT_HTML);
			}
		});

		TextButton showRptBtnExcel = new TextButton(
				AppMessage.Util.MESSAGE.report_makeXLSX());
		showRptBtnExcel.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				saveParamsFromFields(AppConstant.REPORT_FORMAT_XLSX);
			}
		});

		TextButton showRptBtnExcel2000 = new TextButton(
				AppMessage.Util.MESSAGE.report_makeXLS());
		showRptBtnExcel2000.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				saveParamsFromFields(AppConstant.REPORT_FORMAT_XLS);
			}
		});

		TextButton showRptBtnCsv = new TextButton(
				AppMessage.Util.MESSAGE.report_makeCSV());
		showRptBtnCsv.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				saveParamsFromFields(AppConstant.REPORT_FORMAT_CSV);
			}
		});

		BoxLayoutData boxData = new BoxLayoutData(new Margins(5));
		buttonBar.setPack(BoxLayoutPack.END);
		buttonBar.add(showRptBtn, boxData);
		buttonBar.add(showRptBtnExcel, boxData);
		buttonBar.add(showRptBtnExcel2000, boxData);
		buttonBar.add(showRptBtnCsv, boxData);
	}

	private void saveParamsFromFields(final String rptFormat) {
		HashMap<String, DataValue> values = new HashMap<String, DataValue>();

		// Не подходит, если параметры пришли из предыдущего события.
		// if (startParameters.isEmpty()) {
		// for (FieldMetadata f : panel.getChanged()) {
		// values.put(f.getName(), panel.getValue(f));
		// }
		// }
		for (FieldMetadata f : paramValues.keySet()) {
			values.put(f.getName(), panel.getValue(f));
		}
		if (values.size() > 0) {
			StorageWrapper<HashMap<String, DataValue>> storage = StorageHelper.local();
			storage.put(STORAGE_PREFIX + getId(), values);
			saveParams(rptFormat, values);
		} else {
			showReport(rptFormat);
		}
	}

	// Если saveParams = true, значения сохраняются в базу
	private void saveParams(final String rptFormat,
			Map<String, DataValue> values) {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				if (rptFormat != null)
					showReport(rptFormat);
			}

			public void onFailure(Throwable ex) {
				InfoHelper.throwInfo("save-report-values", ex);
			}
		};
		DataServiceAsync.Util.getDataService(callback).saveReportValues(SessionToken.get(),
				getId(), values);
	}

	private void showReport(String rptFormat) {
		StringBuilder str = new StringBuilder(GWT.getHostPageBaseURL()
				+ "report?format=" + rptFormat + "&rpt=" + getId() + "&dpih="
				+ DPIHelper.getHorizontalDPI() + "&dpiv="
				+ DPIHelper.getVerticalDPI() + "&" + AppConstant.TOKEN_ID + "="
				+ SessionToken.get().getTokenId());
		if (rptFormat.equalsIgnoreCase(AppConstant.REPORT_FORMAT_HTML)) {
			com.google.gwt.user.client.Window.open(str.toString(), "SubWindow"
					+ Math.abs(Random.nextInt()),
					"menubar,resizable,scrollbars,status");
		} else {
			com.google.gwt.user.client.Window.open(str.toString(), "_blank",
					null);
		}
	}

	@JsIgnore
	@Override
	public boolean setProperty(String name, DataValue value) {
		if (name.equalsIgnoreCase(PropertyType.ShowReportParams.getCode())
				&& value != null && value.getBoolean() != null) {
			showReportParams = value.getBoolean();
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.ReportFormat.getCode())) {
			if (value != null){
				reportFormat = value.getString();
				return true;
			}
		}
		return super.setProperty(name, value);
	}

	public boolean isShowReportParams() {
		return showReportParams;
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) panel;
	}

	/**
	 * Checks if component is in hidden state.
	 *
	 * @return true if component is hidden
	 */
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Sets component's hidden state.
	 *
	 * @param hidden true - to hide component, false - to show component
	 */
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Focuses component.
	 */
	public void focus() {
		if (componentInstance == null) {
			return;
		}
		componentInstance.focus();
	}

	/**
	 * Checks if component is enabled.
	 *
	 * @return true if component is enabled
	 */
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets component's enabled state.
	 *
	 * @param enabled true - to enable component, false - to disable component
	 */
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}
}
