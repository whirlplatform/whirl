package org.whirlplatform.component.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.form.Radio;
import java.util.Collections;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.resource.ApplicationBundle;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.window.WindowBuilder;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class GridExportImport {

    private final int windowWidth = 400;
    private final LoadConfigProvider configProvider;
    private final ParameterHelper paramHelper;

    public GridExportImport(LoadConfigProvider configProvider,
                            ParameterHelper paramHelper) {
        this.configProvider = configProvider;
        this.paramHelper = paramHelper;
    }

    public TextButton create(final ClassMetadata metadata,
                             final ExpImpType exportType, final String title, boolean iconsOnly) {
        TextButton export = new TextButton();
        String label = null;
        ImageResource icon = null;
        // экспорт в EXPORT_CSV
        if (exportType == ExpImpType.EXPORT_CSV) {
            label = AppMessage.Util.MESSAGE.expimp_expCSV();
            export.setTitle(AppMessage.Util.MESSAGE.exportCSV());
            icon = ApplicationBundle.INSTANCE.csv();
        }

        // импорт в CSV
        if (exportType == ExpImpType.IMPORT_CSV) {
            label = AppMessage.Util.MESSAGE.expimp_impCSV();
            export.setTitle(AppMessage.Util.MESSAGE.importCSV());
            icon = ApplicationBundle.INSTANCE.csv();
        }

        // экспорт в Excel
        if (exportType == ExpImpType.EXPORT_XLS) {
            label = AppMessage.Util.MESSAGE.expimp_expXLS();
            export.setTitle(AppMessage.Util.MESSAGE.exportXLS());
            ApplicationBundle.INSTANCE.excel();
            icon = ApplicationBundle.INSTANCE.excel();
        }

        // импорт в Excel
        if (exportType == ExpImpType.IMPORT_XLS) {
            label = AppMessage.Util.MESSAGE.expimp_impXLS();
            export.setTitle(AppMessage.Util.MESSAGE.importXLS());
            icon = ApplicationBundle.INSTANCE.excel();
        }

        if (!iconsOnly) {
            export.setText(label);
        }
        export.setIcon(icon);

        export.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                createWindow(metadata, exportType, title);
            }
        });

        return export;
    }

    private FormPanel createExportForm(TextButton execute,
                                       ClassMetadata metadata, ExpImpType exportType, String title,
                                       WindowBuilder window) {
        FormPanel form = new FormPanel();
        final VerticalLayoutContainer mainContainer = new VerticalLayoutContainer();
        mainContainer.setHeight(exportType.equals(ExpImpType.EXPORT_XLS) ? 220
            : 150);
        MarginData labelMargin = new MarginData(new Margins(0, 0, 5, 5));
        MarginData radioMargin = new MarginData(new Margins(0, 0, 5, 10));
        String borderStyle = "border:1px solid #A0A0A0";

        Label label1 = new Label(AppMessage.Util.MESSAGE.expimp_exportRecords());
        label1.setLayoutData(labelMargin);

        Radio allRecords = new Radio();
        allRecords.setName("allRecords");
        allRecords.setBoxLabel(AppMessage.Util.MESSAGE.expimp_allRecords());
        allRecords.setLayoutData(radioMargin);

        Radio pageRecords = new Radio();
        pageRecords.setName("pageRecords");
        pageRecords.setBoxLabel(AppMessage.Util.MESSAGE.expimp_pageRecords());
        pageRecords.setValue(true);
        pageRecords.setLayoutData(radioMargin);

        VerticalLayoutContainer container1 = new VerticalLayoutContainer();
        ToggleGroup group1 = new ToggleGroup();
        container1.add(label1);
        container1.add(allRecords);
        container1.add(pageRecords);
        group1.add(allRecords);
        group1.add(pageRecords);
        container1.setLayoutData(new VerticalLayoutData(-1, 70, new Margins(0,
            0, 10, 0)));
        container1.getElement().applyStyles(borderStyle);
        mainContainer.add(container1);

        Label label2 = new Label(AppMessage.Util.MESSAGE.expimp_header());
        label2.setLayoutData(labelMargin);

        Radio titles = new Radio();
        titles.setName("titles");
        titles.setBoxLabel(AppMessage.Util.MESSAGE.expimp_titles());
        titles.setValue(true);
        titles.setLayoutData(radioMargin);

        Radio columns = new Radio();
        columns.setName("columns");
        columns.setBoxLabel(AppMessage.Util.MESSAGE.expimp_columns());
        columns.setLayoutData(radioMargin);

        VerticalLayoutContainer container2 = new VerticalLayoutContainer();
        ToggleGroup group2 = new ToggleGroup();
        container2.add(label2);
        container2.add(titles);
        container2.add(columns);
        group2.add(titles);
        group2.add(columns);
        container2.setLayoutData(new VerticalLayoutData(-1, 70, new Margins(0,
            0, 10, 0)));
        container2.getElement().applyStyles(borderStyle);
        mainContainer.add(container2);

        Radio xlsx = null;
        if (exportType.equals(ExpImpType.EXPORT_XLS)) {
            Label label3 = new Label(AppMessage.Util.MESSAGE.expimp_format());
            label3.setLayoutData(labelMargin);

            Radio xls = new Radio();
            xls.setName("xls");
            xls.setBoxLabel(AppMessage.Util.MESSAGE.expimp_xls());
            xls.setLayoutData(radioMargin);

            xlsx = new Radio();
            xlsx.setName("xlsx");
            xlsx.setBoxLabel(AppMessage.Util.MESSAGE.expimp_xlsx());
            xlsx.setValue(true);
            xlsx.setLayoutData(radioMargin);

            VerticalLayoutContainer container3 = new VerticalLayoutContainer();
            ToggleGroup group3 = new ToggleGroup();
            container3.add(label3);
            container3.add(xls);
            container3.add(xlsx);
            group3.add(xls);
            group3.add(xlsx);
            container3.getElement().applyStyles(borderStyle);
            container3.setLayoutData(new VerticalLayoutData(-1, 60));
            mainContainer.add(container3);
        }
        form.add(mainContainer);

        addExportListener(window, execute, metadata, exportType, title,
            allRecords, columns, xlsx);
        return form;
    }

    private FormPanel createImportForm(final TextButton execute,
                                       final ClassMetadata metadata, final ExpImpType exportType,
                                       final String title, final WindowBuilder window) {
        final FormPanel form = new FormPanel();
        form.setMethod(Method.POST);
        form.setEncoding(Encoding.MULTIPART);

        FileUploadField upload = new FileUploadField();
        upload.setName("file");

        upload.setTitle(AppMessage.Util.MESSAGE.expimp_importFile());

        form.add(upload);

        execute.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                form.setAction(GWT.getHostPageBaseURL() + "import?"
                    + AppConstant.TABLE_ID + "=" + metadata.getClassId()
                    + "&" + AppConstant.TOKEN_ID + "="
                    + SessionToken.get().getTokenId() + "&"
                    + AppConstant.EXPIMP_TYPE_PARAM + "="
                    + exportType.toString());
                form.submit();
                execute.setEnabled(false);
                execute.setText(AppMessage.Util.MESSAGE.executing());
            }
        });
        form.addSubmitCompleteHandler(new SubmitCompleteHandler() {

            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                String result = event.getResults();
                if (result != null && result.contains("<pre>OK</pre>")) {
                    InfoHelper.info("grid-export", result,
                        AppMessage.Util.MESSAGE.expimp_importSuccess(title));
                    window.hide();
                } else {
                    InfoHelper.warning("grid-export", result,
                        AppMessage.Util.MESSAGE.expimp_notAllImported());
                }
                execute.setEnabled(true);
                execute.setText(AppMessage.Util.MESSAGE.expimp_doImport());
            }
        });
        return form;
    }

    private void createWindow(ClassMetadata metadata, ExpImpType exportType,
                              String title) {
        WindowBuilder wb = new WindowBuilder();
        wb.setTitle(AppMessage.Util.MESSAGE.expimp_exportData(title));
        wb.setWidth(windowWidth);
        wb.setResizable(false);

        FormPanel form;
        TextButton execute;
        if (exportType == ExpImpType.IMPORT_CSV
            || exportType == ExpImpType.IMPORT_XLS) {
            execute = new TextButton(AppMessage.Util.MESSAGE.expimp_doImport());
            form = createImportForm(execute, metadata, exportType, title, wb);
        } else {
            execute = new TextButton(AppMessage.Util.MESSAGE.expimp_doExport());
            form = createExportForm(execute, metadata, exportType, title, wb);
        }

        Window window = (Window) wb.getComponent();
        window.add(form);
        window.addButton(execute);
        wb.show();
        wb.center();

    }

    private void addExportListener(final WindowBuilder window,
                                   TextButton export, final ClassMetadata metadata,
                                   final ExpImpType exportType, final String title,
                                   final Radio allRecords, final Radio columns, final Radio xlsx) {

        export.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                AsyncCallback<String> callback = new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        InfoHelper.throwInfo("save-load-config", caught);
                    }

                    @Override
                    public void onSuccess(String result) {
                        StringBuilder link = new StringBuilder();
                        link.append(GWT.getHostPageBaseURL() + "export?");
                        link.append(AppConstant.TABLE_ID + "="
                            + metadata.getClassId());
                        link.append("&" + AppConstant.EXPIMP_TYPE_PARAM + "="
                            + exportType.name());
                        link.append("&" + AppConstant.EXPORT_TITLE_PARAM + "="
                            + title);
                        link.append("&" + AppConstant.EXPORT_COLUMNS_PARAM
                            + "=" + columns.getValue());
                        link.append("&" + AppConstant.EXPORT_ALLREC_PARAM + "="
                            + allRecords.getValue());
                        link.append("&" + AppConstant.TOKEN_ID + "="
                            + SessionToken.get().getTokenId());
                        if (exportType.equals(ExpImpType.EXPORT_XLS)) {
                            link.append("&" + AppConstant.EXPORT_XLSX_PARAM
                                + "=" + xlsx.getValue());
                        }
                        link.append("&parameters_id=" + result);
                        com.google.gwt.user.client.Window.open(link.toString(),
                            "_blank", null);
                        window.hide();
                    }
                };

                DataServiceAsync.Util.getDataService(callback).saveLoadConfig(SessionToken.get(),
                    configProvider.getLoadConfig(
                        paramHelper.getValueList(Collections.emptyList())));
            }
        });
    }

    public enum ExpImpType {
        EXPORT_CSV, EXPORT_PDF, EXPORT_XLS, EXPORT_XLS2000, IMPORT_CSV, IMPORT_XLS
    }
}
