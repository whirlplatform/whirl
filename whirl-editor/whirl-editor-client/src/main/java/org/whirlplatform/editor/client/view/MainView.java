package org.whirlplatform.editor.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.presenter.MainPresenter.IMainView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class MainView extends Viewport implements IMainView {

    private VerticalLayoutContainer root;

    private BorderLayoutContainer mainContainer;
    private BorderLayoutContainer leftPanel;
    private BorderLayoutContainer rightPanel;
    private TabPanel tabPanel;
    private SimpleContainer dummy;

    public MainView() {
        super();
        root = new VerticalLayoutContainer();
        setWidget(root);
    }

    @Override
    public void initUi() {
        mainContainer = new BorderLayoutContainer();
        mainContainer.getElement().getStyle().setBackgroundColor("white");
        root.add(mainContainer, new VerticalLayoutData(1, 1));

        leftPanel = new BorderLayoutContainer();
        leftPanel.getElement().getStyle().setBackgroundColor("white");
        BorderLayoutData d = new BorderLayoutData(250);
        d.setMinSize(100);
        d.setMaxSize(600);
        // d.setCollapsible(true);
        d.setSplit(true);
        ContentPanel p = new ContentPanel();
        p.setHeaderVisible(false);
        p.setWidget(leftPanel);
        mainContainer.setWestWidget(p, d);

        rightPanel = new BorderLayoutContainer();
        rightPanel.getElement().getStyle().setBackgroundColor("white");
        tabPanel = new PlainTabPanel();
        tabPanel.setBorders(false);
        rightPanel.setCenterWidget(tabPanel);
        d = new BorderLayoutData(260);
        d.setMinSize(100);
        d.setMaxSize(600);
        // d.setCollapsible(true);
        d.setSplit(true);
        p = new ContentPanel();
        p.setHeaderVisible(false);
        p.setBorders(false);
        rightPanel.setBorders(false);
        p.setWidget(rightPanel);
        mainContainer.setEastWidget(p, d);

        dummy = new SimpleContainer();
        setSecondLeftComponent(dummy);
    }

    @Override
    public void setTopComponent(IsWidget component) {
        int mainIndex = getWidgetIndex(mainContainer);
        if (mainIndex > 0) {
            root.remove(0);
        }
        VerticalLayoutData d = new VerticalLayoutData(1, -1);
        root.insert(component, 0, d);
        root.forceLayout();
    }

    @Override
    public void setFirstLeftWidget(IsWidget component) {
        leftPanel.setCenterWidget(component);
        leftPanel.forceLayout();
    }

    @Override
    public void setSecondLeftComponent(IsWidget component) {
        if (component != null && !component.equals(dummy)) {
            BorderLayoutData d = new BorderLayoutData();
            d.setMinSize(5);
            d.setMaxSize(1000);
            d.setSplit(true);
            double size = 0.5;
            // Если текущий элемент не "заглушка" возьмем его размер.
            Widget oldWidget = leftPanel.getSouthWidget();
            if (!oldWidget.equals(dummy)) {
                BorderLayoutData oldSize = (BorderLayoutData) oldWidget.getLayoutData();
                if (oldSize != null) {
                    size = oldSize.getSize();
                }
            }
            d.setSize(size);
            ContentPanel panel = new ContentPanel();
            panel.setHeaderVisible(false);
            panel.setBorders(false);
            panel.add(component);
            leftPanel.setSouthWidget(panel, d);
        } else {
            leftPanel.setSouthWidget(dummy, new BorderLayoutData(0));
        }
        leftPanel.syncSize();
        leftPanel.forceLayout();

    }

    @Override
    public void setSecondRightComponent(IsWidget component) {
        BorderLayoutData d = new BorderLayoutData(0.5);
        d.setMinSize(5);
        d.setMaxSize(1000);
        // d.setCollapsible(true);
        d.setSplit(true);
        rightPanel.setSouthWidget(component, d);
        rightPanel.forceLayout();
        rightPanel.syncSize();
    }

    @Override
    public void setCenterComponent(IsWidget component) {
        mainContainer.setCenterWidget(component);
        mainContainer.forceLayout();
    }

    @Override
    public void addFirstRightComponent(IsWidget component) {
        TabItemConfig config = new TabItemConfig(EditorMessage.Util.MESSAGE.design_panel_pallete());
        config.setIcon(EditorBundle.INSTANCE.palette());
        tabPanel.add(component, config);
        tabPanel.forceLayout();
        rightPanel.forceLayout();
        rightPanel.syncSize();
    }

    @Override
    public void addSecondRightComponent(IsWidget component) {
        TabItemConfig config = new TabItemConfig(EditorMessage.Util.MESSAGE.design_panel_components());
        config.setIcon(EditorBundle.INSTANCE.templates());
        tabPanel.add(component, config);
        tabPanel.forceLayout();
        rightPanel.forceLayout();
        rightPanel.syncSize();
    }

    @Override
    public void addThirdRightComponent(IsWidget component) {
        TabItemConfig config = new TabItemConfig(EditorMessage.Util.MESSAGE.design_panel_events());
        config.setIcon(EditorBundle.INSTANCE.events());
        tabPanel.add(component, config);
        tabPanel.forceLayout();
        rightPanel.forceLayout();
        rightPanel.syncSize();
    }
}
