package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.annotation.Presenter;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.view.EventTemplatesView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.templates.BaseTemplate;

import java.util.List;

@Presenter(view = EventTemplatesView.class)
public class EventTemplatesPresenter extends AbstractTemplatesPresenter {

    public void onBuildApp() {
        eventBus.addThirdRightComponent(view);
    }

    @Override
    public void bind() {
        loadTemplates();
    }

    @Override
    public void loadTemplates() {
        EditorDataService.Util.getDataService().loadEventTemplates(new AsyncCallback<List<BaseTemplate>>() {

            @Override
            public void onSuccess(List<BaseTemplate> result) {
                view.addComponents(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("template-load-event", caught);
            }
        });
    }
}
