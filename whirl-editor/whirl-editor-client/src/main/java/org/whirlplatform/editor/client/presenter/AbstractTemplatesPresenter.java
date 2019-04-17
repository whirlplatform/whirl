package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.presenter.BasePresenter;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.templates.BaseTemplate;

import java.util.List;

public abstract class AbstractTemplatesPresenter
        extends BasePresenter<AbstractTemplatesPresenter.IComponentTemplatesView, EditorEventBus> {

    public abstract void loadTemplates();

    public void renameTemplate(final BaseTemplate oldTemplate, BaseTemplate template) {
        EditorDataService.Util.getDataService().saveTemplate(template, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                InfoHelper.info("template-rename", EditorMessage.Util.MESSAGE.success(),
                        EditorMessage.Util.MESSAGE.templ_success_save_with_name() + " <b> " + result + " </b> ");
                deleteTemplate(oldTemplate, false);
            }

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.error("template-rename-error", EditorMessage.Util.MESSAGE.error(), caught.getLocalizedMessage());
            }
        });
    }

    public void deleteTemplate(BaseTemplate template, final boolean showMessage) {
        EditorDataService.Util.getDataService().deleteTemplate(template, new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                loadTemplates();
                if (showMessage) {
                    InfoHelper.info("template-delete", EditorMessage.Util.MESSAGE.success(), EditorMessage.Util.MESSAGE.templ_success_delete());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("template-delete-error", caught);
            }
        });
    }

    public void saveTemplate(final BaseTemplate template, final boolean reload) {
        EditorDataService.Util.getDataService().saveTemplate(template, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                InfoHelper.info("template-save", EditorMessage.Util.MESSAGE.success(),
                        EditorMessage.Util.MESSAGE.templ_success_save_with_name() + "<b> " + result + " </b>");
                if (reload) {
                    loadTemplates();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.error("template-save-error", EditorMessage.Util.MESSAGE.error(), caught.getLocalizedMessage());
            }
        });
    }

    public interface IComponentTemplatesView extends IsWidget {
        void addComponents(List<BaseTemplate> components);
    }
}
