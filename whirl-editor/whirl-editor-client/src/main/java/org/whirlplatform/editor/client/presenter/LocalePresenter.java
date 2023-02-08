package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Collection;
import java.util.List;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.tree.dummy.DummyAppLocales;
import org.whirlplatform.editor.client.view.LocaleView;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

@Presenter(view = LocaleView.class)
public class LocalePresenter extends BasePresenter<LocalePresenter.ILocaleView, EditorEventBus>
    implements ElementPresenter {

    private ApplicationElement application;

    public LocalePresenter() {
        super();
    }

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof DummyAppLocales)) {
            return;
        }
        view.setLocales(application.getLocales());
        eventBus.openElementView(view);
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        this.application = application;
    }

    public void onUpdateLocales(List<LocaleElement> locales) {
        application.changeLocales(locales);
    }

    public ApplicationElement getApplication() {
        return application;
    }

    public interface ILocaleView extends ReverseViewInterface<LocalePresenter>, IsWidget {

        void setLocales(Collection<LocaleElement> locales);
    }

}
