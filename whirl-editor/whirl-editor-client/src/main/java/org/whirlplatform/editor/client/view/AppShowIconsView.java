package org.whirlplatform.editor.client.view;

import com.google.gwt.aria.client.State;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.ListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;
import com.sencha.gxt.theme.base.client.listview.ListViewDefaultAppearance;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import org.whirlplatform.editor.shared.EditorDataService;

import java.util.List;

public class AppShowIconsView extends Window {
    protected static final int MIN_HEIGHT = 200;
    protected static final int MIN_WIDTH = 200;

    private Dialog choose;
    private ListStore<String> store;

    private ListView<String, String> view;

    private RpcProxy<Void, List<String>> proxy;
    public AppShowIconsView() {

        store = new ListStore<>(String::toString);
        proxy = new RpcProxy<Void, List<String>>() {
            @Override
            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
                EditorDataService.Util.getDataService().getIcons(callback);
            }
        };

        Loader<Void, List<String>> loader = new Loader<>(proxy);
        loader.addLoadHandler(new ListStoreBinding<>(store));

//        ListViewDefaultAppearance.ListViewDefaultResources resources = GWT.create(CustomListViewResources.class);
//        ListViewDefaultAppearance<State> appearance = new ListViewDefaultAppearance<State>(new );

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
                String html = "<span style='display: inline-block; width:20px; height:20px; vertical-align: middle; background-color: "
                        + object + "; border: 1px solid gray; margin: 3px;'>&nbsp;</span>";
                return SafeHtmlUtils.fromSafeConstant(html);
            }
        }));

        view.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        view.setBorders(false);



        loader.load();
        EditorDataService.Util.getDataService().getIcons(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<String> result) {
                System.out.println(result.toString());
                System.out.println("Result OK");

            }
        });

        buildUI();
    }

    public void buildUI() {
        BorderLayoutContainer.BorderLayoutData centerData = new BorderLayoutContainer.BorderLayoutData();
        centerData.setMargins(new Margins(0, 0, 0, 0));

        BorderLayoutContainer con = new BorderLayoutContainer();
        con.setCenterWidget(view, centerData);
  //      con.add(view);

        // Dialog ex. Window
        choose = new Dialog();
        choose.setPixelSize(640, 480);
        choose.setResizable(false);
        choose.setMinHeight(MIN_HEIGHT);
        choose.setMinWidth(MIN_WIDTH);
//        choose.setId("img-chooser-dlg");
        choose.setHeading("Chooser icon");
        choose.setModal(true);
        choose.setBodyBorder(false);
        choose.setPredefinedButtons(Dialog.PredefinedButton.OK, Dialog.PredefinedButton.CANCEL);
        choose.setHideOnButtonClick(true);
        choose.setClosable(false);
        choose.add(con);
        choose.show();

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
