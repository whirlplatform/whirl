package org.whirlplatform.editor.client.view.allapps;

import org.whirlplatform.editor.client.view.context.AbstractContextMenu;

public class AllApplicationsContextMenu extends AbstractContextMenu<AllApplicationsView> {

    public AllApplicationsContextMenu(final AllApplicationsView context) {
        super(context);
    }

    @Override
    protected void initMenu() {
        addMenuItem(new ExpandContextMenuItem());
        addMenuItem(new CollapseContextMenuItem());
        addSeparator();
        addMenuItem(new LoadApplicationContextMenuItem());
        addMenuItem(new PackageApplicationContextMenuItem());
        addSeparator();
        addMenuItem(new RunApplicationContextMenuItem());
    }
}
