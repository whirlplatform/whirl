package org.whirlplatform.editor.client.tree;

import com.google.gwt.core.client.Callback;
import java.util.Collection;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.RightType;

public interface AppTreePresenter {

    void riseAddElement(AbstractElement parent, AbstractElement element);

    void riseAddElementUI(AbstractElement parent, AbstractElement element);

    void riseRemoveElement(AbstractElement parent, AbstractElement element, boolean showDialog);

    void riseRemoveElementUI(AbstractElement parent, AbstractElement element);

    void riseEditRights(Collection<? extends AbstractElement> elements,
                        Collection<RightType> rightTypes);

    void riseOpenElement(AbstractElement element);

    void riseCloseOpenApplication();

    void riseShowOpenApplicationsCallback(Callback<ApplicationStoreData, Throwable> callback);
}
