package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.List;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.PropertyReportView;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ReportElement;

@Presenter(view = PropertyReportView.class)
public class PropertyReportPresenter
        extends
        BasePresenter<PropertyReportPresenter.IPropertyReportView, EditorEventBus> {

    private ReportElement report;

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof ReportElement)) {
            return;
        }
        report = (ReportElement) element;
        view.setParams(report.getFields());
        eventBus.changeSecondRightComponent(view);
    }

    public void addParam(FieldMetadata field) {
        report.addField(field);
    }

    public void removeParam(FieldMetadata field) {
        report.removeField(field);
    }

    public interface IPropertyReportView extends IsWidget,
            ReverseViewInterface<PropertyReportPresenter> {
        void setParams(List<FieldMetadata> fields);
    }
}
