package org.whirlplatform.editor.client.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.ListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import java.util.List;
import org.whirlplatform.editor.client.presenter.AppShowIconsPresenter;

public class AppShowIconsView extends Dialog implements AppShowIconsPresenter.IAppShowIconsView {

    protected static final int MIN_HEIGHT = 200;
    protected static final int MIN_WIDTH = 200;

    private AppShowIconsPresenter presenter;

    private ListStore<String> store;

    private ListView<String, String> view;

    private RpcProxy<Void, List<String>> proxy;

    public AppShowIconsView() {
        presenter = new AppShowIconsPresenter(this);

        presenter.setView(this);
        setProxy(presenter.getProxy());
        setStore(presenter.getStore());

        Loader<Void, List<String>> loader = new Loader<>(proxy);
        loader.addLoadHandler(new ListStoreBinding<>(store));

        view = new ListView<>(store, new ValueProvider<String, String>() {
            @Override
            public String getValue(String object) {
                return object;
            }

            @Override
            public void setValue(String object, String value) {
            }

            @Override
            public String getPath() {
                return null;
            }
        });

        view.setCell(new SimpleSafeHtmlCell<String>(new AbstractSafeHtmlRenderer<String>() {
            @Override
            public SafeHtml render(String object) {
                int sep = object.lastIndexOf("/");
                String html = "<div style='width: 50%; overflow: hidden; white-space: nowrap;'>"
                    + "<img src=" + object
                    + " style='width: 5%; display: inline-block; overflow: hidden; "
                    + "vertical-align: middle;'> " + object.substring(sep + 1) + "</div>"
                    + "</div>";
                return SafeHtmlUtils.fromSafeConstant(html);
            }
        }));
        view.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        view.setBorders(false);

        loader.load();
        buildUI();
    }

    private static native void copyToClipboard(String text)/*-{
        navigator.clipboard.writeText(text).then(function () {
            console.log('Async: Copying to clipboard was successful!');
        }, function (err) {
            console.error('Async: Could not copy text: ', err);
        });
    }-*/;

    public void buildUI() {
        BorderLayoutContainer.BorderLayoutData centerData =
            new BorderLayoutContainer.BorderLayoutData();
        centerData.setMargins(new Margins(0, 0, 0, 0));

        BorderLayoutContainer con = new BorderLayoutContainer();
        con.setCenterWidget(view, centerData);

        setPixelSize(640, 480);
        setResizable(false);
        setMinHeight(MIN_HEIGHT);
        setMinWidth(MIN_WIDTH);
        setHeading("Icons chooser");
        setModal(true);
        setBodyBorder(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CLOSE);
        getButton(Dialog.PredefinedButton.OK).setText("Копировать");
        setHideOnButtonClick(true);
        setClosable(false);
        addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                String item = view.getSelectionModel().getSelectedItem();
                if (item != null) {
                    if (event.getHideButton() == Dialog.PredefinedButton.OK) {
                        AppShowIconsView.this.getButton(Dialog.PredefinedButton.OK).enable();
                        copyToClipboard(item);
                    }
                }
            }
        });
        add(con);
    }

    @Override
    public AppShowIconsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(AppShowIconsPresenter presenter) {
        this.presenter = presenter;
    }

    public void setProxy(RpcProxy<Void, List<String>> proxy) {
        this.proxy = proxy;
    }

    public void setStore(ListStore<String> store) {
        this.store = store;
    }

}
