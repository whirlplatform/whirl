package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.presenter.DataSourcePresenter.IDataSourceView;
import org.whirlplatform.editor.client.view.DataSourceView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

@Presenter(view = DataSourceView.class)
public class DataSourcePresenter extends BasePresenter<IDataSourceView, EditorEventBus>
        implements ElementPresenter {

    private DataSourceElement datasource;

    public DataSourcePresenter() {
        super();
    }

    @Override
    public void bind() {
        ContentPanel panel = ((ContentPanel) view.asWidget());
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent e) {
                datasource.setAlias(view.getAlias());
                datasource.setDatabaseName(view.getDatabaseName());
                eventBus.syncServerApplication();
            }
        }));
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                eventBus.closeElementView();
            }
        }));
    }

    @Override
    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof DataSourceElement)) {
            return;
        }
        datasource = (DataSourceElement) element;
        view.setHeaderText(EditorMessage.Util.MESSAGE.editing_datasource() + datasource.getName());
        view.clearValues();
        view.setAlias(datasource.getAlias());
        view.setDatabaseName(datasource.getDatabaseName());
        eventBus.openElementView(view);
    }

    public interface IDataSourceView extends IsWidget {

        void setHeaderText(String text);

        void clearValues();

        String getAlias();

        void setAlias(String alias);

        String getDatabaseName();

        void setDatabaseName(String databaseName);

    }
}
