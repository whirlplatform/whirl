package org.whirlplatform.editor.client.view.toolbar;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.ToolBarPresenter;
import org.whirlplatform.editor.client.view.context.AbstractContextTextButton;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

import java.io.Serializable;
import java.util.Date;

public class ToolBarShowIconsButton extends Window { //extends AbstractContextTextButton<ToolBarPresenter> {//

    private Dialog choose;
    private Image image;
    private ListView<Photo, Photo> view;
    private ListStore<Photo> store;
    private VerticalLayoutContainer panel;
 //   private DetailRenderer renderer;
    private Style style;
    private ContentPanel details;

    private Window window;

    public ToolBarShowIconsButton() {
        BorderLayoutContainer.BorderLayoutData centerData = new BorderLayoutContainer.BorderLayoutData();
        centerData.setMargins(new Margins(0, 0, 0, 0));

        BorderLayoutContainer con = new BorderLayoutContainer();
        con.setCenterWidget(new Frame("http://www.sencha.com/products/gxt"), centerData);

        // Dialog ex. Window
        choose = new Dialog();
        choose.setPixelSize(640, 480);
        choose.setResizable(false);
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


//    protected SelectEvent.SelectHandler createSelectHandler() {
//    protected void creatIcons(){

//        window = new Window();
//        window.setHeading("Product Information");
//        window.setModal(true);
//        window.setPixelSize(600, 400);
//        window.setMaximizable(true);
//        window.setToolTip("The GXT product page...");
//        window.setWidget(new Frame("http://www.sencha.com/products/gxt"));
//        window.show();


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

        // Dialog ex. Window
//        this = new Dialog();
//        this.setPixelSize(640, 480);
//        this.setResizable(false);
//        this.setId("img-chooser-dlg");
//        this.setHeading("Advanced List View");
//        this.setModal(true);
//        this.setBodyBorder(false);
//        this.setPredefinedButtons(Dialog.PredefinedButton.OK, Dialog.PredefinedButton.CANCEL);
//        this.setHideOnButtonClick(true);
//        this.setClosable(false);
//        this.add(con);
//        this.show();
//
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

//
//
//        return ;
//    }

//    private void onSelectionChange(SelectionChangedEvent<Photo> se) {
//        if (se.getSelection().size() > 0) {
//            details.getBody().setInnerSafeHtml(renderer.render(se.getSelection().get(0), style));
//            chooser.getButton(Dialog.PredefinedButton.OK).enable();
//        } else {
//            chooser.getButton(Dialog.PredefinedButton.OK).disable();
//            details.getBody().setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
//        }
//    }

//    @Override
//    protected String createButtonTitle() {
//        return null;
//    }

//    @Override
//    protected String createToolTip() {
//        return EditorMessage.Util.MESSAGE.select_icon(); //???
//    }

//    @Override
//    protected ImageResource selectButtonIcon() {
//        return  ComponentBundle.INSTANCE.checkBox();
//    }

//    @Override
//    public void updateState() {
//
//    }
}

class Photo implements Serializable {

    private String name;
    private long size;
    private Date date;
    private String path;

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public SafeUri getPathUri() {
        return UriUtils.fromString(getPath());
    }

    public String getShortName() {
        return Format.ellipse(name, 15);
    }

    public long getSize() {
        return size;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
