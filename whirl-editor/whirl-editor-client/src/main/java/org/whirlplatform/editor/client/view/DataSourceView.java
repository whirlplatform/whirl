package org.whirlplatform.editor.client.view;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.client.presenter.DataSourcePresenter.IDataSourceView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class DataSourceView extends ContentPanel implements IDataSourceView {

    private VerticalLayoutContainer container;

    private FieldLabel labelAlias;
    private TextField fieldAlias;

    private FieldLabel labelDatabaseName;
    private TextField fieldDatabaseName;

    public DataSourceView() {
        super();
        initUI();
    }

    private void initUI() {
        setHeaderVisible(true);
        container = new VerticalLayoutContainer();
        container.setAdjustForScroll(true);
        container.setScrollMode(ScrollMode.AUTO);
        initFields();
        setWidget(container);
    }

    private void initFields() {
        fieldAlias = new TextField();
        labelAlias = new FieldLabel(fieldAlias,
            EditorMessage.Util.MESSAGE.datasource_synonym());
        container.add(labelAlias, new VerticalLayoutData(1, -1, new Margins(10,
            10, 0, 10)));

        fieldDatabaseName = new TextField();
        labelDatabaseName = new FieldLabel(fieldDatabaseName,
            EditorMessage.Util.MESSAGE.datasource_source_name());
        container.add(labelDatabaseName, new VerticalLayoutData(1, -1,
            new Margins(10, 10, 0, 10)));
    }

    @Override
    public void setHeaderText(String text) {
        setHeading(text);
    }

    @Override
    public void clearValues() {
        fieldAlias.clear();
        fieldDatabaseName.clear();
    }

    @Override
    public String getAlias() {
        return fieldAlias.getValue();
    }

    @Override
    public void setAlias(String alias) {
        fieldAlias.setValue(alias);
    }

    @Override
    public String getDatabaseName() {
        return fieldDatabaseName.getValue();
    }

    @Override
    public void setDatabaseName(String databaseName) {
        fieldDatabaseName.setValue(databaseName);
    }

}
