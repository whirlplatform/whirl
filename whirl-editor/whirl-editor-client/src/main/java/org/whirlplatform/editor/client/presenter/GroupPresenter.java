package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.tree.dummy.DummyAppGroups;
import org.whirlplatform.editor.client.view.GroupView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.GroupElement;

import java.util.Collection;

@Presenter(view = GroupView.class)
public class GroupPresenter extends BasePresenter<GroupPresenter.IGroupView, EditorEventBus>
        implements ElementPresenter {

    public interface IGroupView extends ReverseViewInterface<GroupPresenter>, IsWidget {

        void addGroup(GroupElement group);

        Collection<GroupElement> getGroups();

        void clearUI();

    }

    private ApplicationElement application;

    public GroupPresenter() {
    }

    @Override
    public void bind() {
        ContentPanel panel = ((ContentPanel) view.asWidget());
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent e) {

                application.changeGroups(view.getGroups());

                eventBus.syncServerApplication();
                eventBus.closeElementView();
            }

        }));
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.clearUI();
                eventBus.closeElementView();
            }

        }));
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        this.application = application;
    }

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof DummyAppGroups)) {
            return;
        }
        view.clearUI();
        for (GroupElement group : application.getGroups()) {
            view.addGroup(group);
        }
        eventBus.openElementView(view);
    }
}
