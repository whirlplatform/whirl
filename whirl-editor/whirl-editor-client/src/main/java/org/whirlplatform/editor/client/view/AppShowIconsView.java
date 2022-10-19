package org.whirlplatform.editor.client.view;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import org.whirlplatform.editor.client.presenter.AppShowIconsPresenter;
import org.whirlplatform.editor.shared.EditorDataService;

import java.util.List;
import java.util.logging.Logger;

public class AppShowIconsView extends Dialog implements AppShowIconsPresenter.IAppShowIconsView {

    private final static Logger logger = Logger.getLogger(AppShowIconsView.class.getName());
    protected static final int MIN_HEIGHT = 200;
    protected static final int MIN_WIDTH = 200;

    private AppShowIconsPresenter presenter;

    private ListStore<String> store;

    private ListView<String, String> view;

    private RpcProxy<Void, List<String>> proxy;

    public AppShowIconsView() {
        presenter = new AppShowIconsPresenter(this);

        presenter.setIconsView(this);
        setProxy(presenter.getProxy());
        setStore(presenter.getStore());

//        store = new ListStore<>(String::toString);
//        proxy = new RpcProxy<Void, List<String>>() {
//            @Override
//            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
//                EditorDataService.Util.getDataService().getIcons(callback);
//            }
//        };

//        presenter.loadIcons();

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
                String html = "<div style='width: 50%; overflow: hidden; white-space: nowrap;'>" +
                        "<img src=" + object + " style='width: 5%; display: inline-block; overflow: hidden; " +
                        "vertical-align: middle;'> " + object.replace("webjars/famfamfam-silk/1.3/icons/", "") + "</div>" +
                        "</div>";
                return SafeHtmlUtils.fromSafeConstant(html);
            }
        }));
        //  view.getSelectionModel().select(0, false);
        view.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        view.setBorders(false);

        loader.load();
        buildUI();
        //======================================================================================
//        EditorDataService.Util.getDataService().getIcons(new AsyncCallback<List<String>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//            }
//
//            @Override
//            public void onSuccess(List<String> result) {
//                logger.info(result.toString());
//                logger.info("Result OK");
//
//            }
//        });
//        SelectionHandler<Item> handler = new SelectionHandler<Item>() {
//            @Override
//            public void onSelection(SelectionEvent<Item> event) {
//
//            }
//        };


    }

    public void buildUI() {
//        Image image = new Image();
//        image.setVisible(false);

//        VerticalLayoutContainer panel = new VerticalLayoutContainer();

        BorderLayoutContainer.BorderLayoutData centerData = new BorderLayoutContainer.BorderLayoutData();
        centerData.setMargins(new Margins(0, 0, 0, 0));

        BorderLayoutContainer con = new BorderLayoutContainer();
        con.setCenterWidget(view, centerData);
        //      con.add(view);
        // Dialog ex. Window

        setPixelSize(640, 480);
        setResizable(false);
        setMinHeight(MIN_HEIGHT);
        setMinWidth(MIN_WIDTH);
        setId("img-chooser-dlg");
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
                String photo = view.getSelectionModel().getSelectedItem();
                if (photo != null) {
                    if (event.getHideButton() == Dialog.PredefinedButton.OK) {
                        AppShowIconsView.this.getButton(Dialog.PredefinedButton.OK).enable();
                        com.google.gwt.user.client.Window.open(view.getSelectionModel().getSelectedItem(), "", "");
//                        image.setUrl(photo);
//                        image.setVisible(true);
//                        panel.forceLayout();
                    }
                }
            }
        });

//        panel.add(buttonBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 20, 0)));
//        panel.add(image, new VerticalLayoutContainer.VerticalLayoutData(1, -1));

        add(con);
        show();
    }

    @Override
    public AppShowIconsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(AppShowIconsPresenter presenter) {
        this.presenter = presenter;
//        buildUI();
    }

    public void setProxy(RpcProxy<Void, List<String>> proxy) {
        this.proxy = proxy;
    }

    public void setStore(ListStore<String> store) {
        this.store = store;
    }


    //        ModelKeyProvider<Photo> keyProvider = new ModelKeyProvider<Photo>() {
//            @Override
//            public String getKey(Photo item) {
//                return item.getName();
//            }
//        };
//
//       store = new ListStore<Photo>(keyProvider);
//
//       view = new ListView<Photo, Photo>(store, new IdentityValueProvider<Photo>() {
//            @Override
//            public void setValue(Photo object, Photo value) {
//            }
//        }, appearance);
//        view.setCell(new SimpleSafeHtmlCell<Photo>(new AbstractSafeHtmlRenderer<Photo>() {
//            @Override
//            public SafeHtml render(Photo object) {
//                return renderer.renderItem(object, style);
//            }
//        }));
//        view.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
//        view.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<Photo>() {
//            @Override
//            public void onSelectionChanged(SelectionChangedEvent<Photo> event) {
//                ToolBarShowIconsButton.this.onSelectionChange(event);
//            }
//        });
//        view.setBorders(false);
//
//        VerticalLayoutContainer main = new VerticalLayoutContainer();
//        main.setBorders(true);
//        main.add(view, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
//

//        BorderLayoutContainer.BorderLayoutData centerData = new BorderLayoutContainer.BorderLayoutData();
//        centerData.setMargins(new Margins(0, 0, 0, 0));
//
//        BorderLayoutContainer con = new BorderLayoutContainer();
//        con.setCenterWidget(new Frame("http://www.sencha.com/products/gxt"), centerData);

    //        return new SelectEvent.SelectHandler() {
//            @Override
//            public void onSelect(SelectEvent event) {
//                getContext().runPackageCreation();
//            }
//        };
//        chooser.setWidget(new Frame("http://www.sencha.com/products/gxt"));
//        chooser.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
//            @Override
//            public void onDialogHide(DialogHideEvent event) {
//                Photo photo = view.getSelectionModel().getSelectedItem();
//                if (photo != null) {
//                    if (event.getHideButton() == Dialog.PredefinedButton.OK) {
//                        image.setUrl(photo.getPath());
//                        image.setVisible(true);
//                        panel.forceLayout();
//                    }
//                }
//            }
//        });

//    private void onSelectionChange(SelectionChangedEvent<Photo> se) {
//        if (se.getSelection().size() > 0) {
//            details.getBody().setInnerSafeHtml(renderer.render(se.getSelection().get(0), style));
//            chooser.getButton(Dialog.PredefinedButton.OK).enable();
//        } else {
//            chooser.getButton(Dialog.PredefinedButton.OK).disable();
//            details.getBody().setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
//        }
//    }

}
