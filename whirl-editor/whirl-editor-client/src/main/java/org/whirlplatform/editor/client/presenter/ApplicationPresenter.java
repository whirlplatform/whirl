package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import java.util.Collection;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.ApplicationView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

@Presenter(view = ApplicationView.class)
public class ApplicationPresenter
    extends BasePresenter<ApplicationPresenter.IApplicationView, EditorEventBus>
    implements ElementPresenter {

    private ApplicationElement application;
    private Version version;
    private TextButton saveButton;
    private TextButton closeButton;

    public ApplicationPresenter() {
        super();
    }

    public void onBuildApp() {
        createButtons();
        view.setCompleteCommand(new Command() {
            @Override
            public void execute() {
                application.setCode(view.getApplicationCode());
                application.setTitle(view.getApplicationTitle());
                application.setGuest(view.getGuest());

                application.replaceJavaScriptFiles(view.getJavaScriptFiles());
                application.replaceCssFiles(view.getCssFiles());
                application.replaceJavaFiles(view.getJavaFiles());
                application.replaceImageFiles(view.getImageFiles());
                application.setStaticFile(view.getStaticFile());
                application.setHtmlHeader(view.getHeaderHtml());

                eventBus.closeElementView();
                eventBus.syncServerApplication();
            }
        });
    }

    @Override
    public void onOpenElement(AbstractElement element) {
        showElement(element, false);
    }

    private void createButtons() {
        ContentPanel panel = ((ContentPanel) view.asWidget());
        saveButton = new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.save();
            }
        });
        panel.addButton(saveButton);
        closeButton = new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                eventBus.closeElementView();
            }
        });
        panel.addButton(closeButton);
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        this.application = application;
        this.version = version;
    }

    public void downloadFile(FileElement element) {
        // Возможно нужна проверка сессии на стороне сервера
        StringBuilder sb = new StringBuilder(GWT.getHostPageBaseURL());
        sb.append("download?");
        sb.append("code=").append(application.getCode());
        sb.append("&version=").append(version.toString());
        sb.append("&category=").append(element.getContentType());
        sb.append("&fileName=").append(element.getFileName());
        String url = sb.toString();
        com.google.gwt.user.client.Window.open(url, "_blank", null);
    }

    public void onViewElement(AbstractElement element) {
        showElement(element, true);
    }

    private void showElement(AbstractElement element, boolean readOnly) {
        if (!(element instanceof ApplicationElement)) {
            return;
        }
        // application = (ApplicationElement) element;
        // view.initFields(application, version);
        // view.initFields();

        view.setHeaderText(
            EditorMessage.Util.MESSAGE.editing_application() + application.getName() + " ["
                + application.getVersion() + "]");
        view.clearFilesFields();
        view.setLocales(application.getLocales(), application.getDefaultLocale());
        view.setApplicationCode(application.getCode());
        view.setApplicationTitle(application.getTitle());
        view.setGuest(application.isGuest());
        for (FileElement jsFile : application.getJavaScriptFiles()) {
            view.addJavaScriptFile(jsFile);
        }
        for (FileElement cssFile : application.getCssFiles()) {
            view.addCssFile(cssFile);
        }
        for (FileElement javaFile : application.getJavaFiles()) {
            view.addJavaFile(javaFile);
        }
        for (FileElement imageFile : application.getImageFiles()) {
            view.addImageFile(imageFile);
        }
        if (application.getStaticFile() != null) {
            view.setStaticFile(application.getStaticFile());
        }

        view.setHtmlHeader(application.getHtmlHeader());

        eventBus.openElementView(view);

        view.setEnableAll(!readOnly);
        saveButton.setEnabled(!readOnly);
    }

    public interface IApplicationView extends IsWidget, ReverseViewInterface<ApplicationPresenter> {

        void setHeaderText(String text);

        String getApplicationCode();

        void setApplicationCode(String code);

        PropertyValue getApplicationTitle();

        void setApplicationTitle(PropertyValue title);

        Boolean getGuest();

        void setGuest(Boolean guest);

        void addJavaScriptFile(FileElement file);

        Collection<FileElement> getJavaScriptFiles();

        void addCssFile(FileElement file);

        Collection<FileElement> getCssFiles();

        void addJavaFile(FileElement file);

        Collection<FileElement> getJavaFiles();

        void addImageFile(FileElement file);

        Collection<FileElement> getImageFiles();

        FileElement getStaticFile();

        void setStaticFile(FileElement file);

        String getHeaderHtml();

        void setHtmlHeader(String headerHtml);

        void setCompleteCommand(Command completeCommand);

        void save();

        void clearFilesFields();

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);

        void setEnableAll(boolean enable);

        // void initFields(ApplicationElement application, Version version);
        void initFields();
    }
}
