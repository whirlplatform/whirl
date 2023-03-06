package org.whirlplatform.meta.shared.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.EventType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.EventParameterImpl;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

@SuppressWarnings("serial")
@Data
public class EventElement extends AbstractElement {

    private EventType type;
    private String code;
    private String handlerType;

    private DataSourceElement dataSource;
    private String schema;
    private String function;
    private String source;

    private ComponentElement component;
    private ComponentElement targetComponent;

    private AbstractElement parentElement;

    private Set<EventParameterElement> parameters = new HashSet<EventParameterElement>();

    private String parentEventId;
    private List<EventElement> subEvents = new ArrayList<EventElement>();

    private boolean confirm = false;
    private PropertyValue confirmText = new PropertyValue(DataType.STRING);

    private boolean wait = false;
    private PropertyValue waitText = new PropertyValue(DataType.STRING);

    private boolean named = false;

    private boolean createNew = false;

    public EventElement() {

    }

    public static EventMetadata eventElementToMetadata(EventElement event, LocaleElement locale) {
        EventMetadata meta = new EventMetadata(event.getType());
        meta.setId(event.getId());
        meta.setCode(event.getCode());
        if (event.getComponent() != null) {
            meta.setComponentId(event.getComponent().getId());
        }
        if (event.getTargetComponent() != null) {
            meta.setTargetComponentId(event.getTargetComponent().getId());
        }

        meta.setConfirm(event.isConfirm());
        meta.setConfirmText(event.getConfirmText().getValue(locale).getString());
        meta.setSource(event.getSource());
        meta.setNamed(event.isNamed());
        meta.setWait(event.isWait());
        meta.setWaitText(event.getWaitText().getValue(locale).getString());
        meta.setCreateNew(event.isCreateNew());
        for (EventParameterElement param : event.getParameters()) {
            EventParameterImpl metaParam = new EventParameterImpl(param.getType());
            metaParam.setCode(param.getCode());
            metaParam.setComponentId(param.getComponentId());
            metaParam.setComponentCode(param.getComponentCode());
            metaParam.setStorageCode(param.getStorageCode());
            if (metaParam.getCode() == null || metaParam.getCode().isEmpty()) {
                metaParam.setDataWithCode(param.getValue());
            } else {
                metaParam.setData(param.getValue());
            }
            meta.addParameter(metaParam);
        }
        return meta;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ComponentElement getComponent() {
        return component;
    }

    public void setComponent(ComponentElement component) {
        this.component = component;
    }

    public ComponentElement getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(ComponentElement targetComponent) {
        this.targetComponent = targetComponent;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public PropertyValue getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(PropertyValue confirmText) {
        this.confirmText = confirmText;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public PropertyValue getWaitText() {
        return waitText;
    }

    public void setWaitText(PropertyValue waitText) {
        this.waitText = waitText;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    /**
     * Пересчитать индексы параметров
     *
     * @return возвращает индекс последнего жлемента. Если елементов нет, то возвращает <i>-1</i>.
     */
    private int rebuildIndex() {
        int index = 0;
        for (EventParameterElement p : getParameters()) {
            p.setIndex(index);
            index++;
        }
        return index - 1;
    }

    public void addParameter(EventParameterElement parameter) {
        if (parameter.getIndex() >= 0) {
            setParameterIndex(parameter, parameter.getIndex());
            parameter.setParentEvent(this);
        } else {
            parameter.setIndex(rebuildIndex() + 1);
            parameters.add(parameter);
            parameter.setParentEvent(this);
        }
    }

    public Collection<EventParameterElement> getParameters() {
        List<EventParameterElement> result = new ArrayList<EventParameterElement>(parameters);
        Collections.sort(result, ParametersComparator.COMPARATOR);
        return Collections.unmodifiableList(result);
    }

    public void removeParameter(EventParameterElement parameter) {
        parameters.remove(parameter);
    }

    public int getParametersCount() {
        return parameters.size();
    }

    public void setParameterIndex(EventParameterElement parameter, int index) {
        boolean addLast = index == parameters.size();
        int i = 0;
        parameters.remove(parameter);
        for (EventParameterElement p : getParameters()) {
            if (p == parameter) {
                continue;
            }
            if (i == index) {
                i++;
            }
            p.setIndex(i);
            parameters.add(p);
            i++;
        }
        if (addLast) {
            parameter.setIndex(i);
        } else {
            parameter.setIndex(index);
        }
        parameters.add(parameter);
        parameter.setParentEvent(this);
    }

    public int getParameterIndex(EventParameterElement parameter) {
        if (!parameters.contains(parameter)) {
            return -1;
        }
        return parameter.getIndex();
    }

    public boolean isNamed() {
        return named;
    }

    public void setNamed(boolean named) {
        this.named = named;
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public void addSubEvent(EventElement subEvent) {
        subEvents.add(subEvent);
        subEvent.setParentEvent(this);
    }

    public List<EventElement> getSubEvents() {
        return subEvents;
    }

    public void removeSubEvent(EventElement subEvent) {
        subEvents.remove(subEvent);
        if (subEvent != null) {
            subEvent.setParentEvent(null);
        }
    }

    public void removeFromParent() {
        if (parentElement == null) {
            return;
        }
        if (parentElement instanceof ComponentElement) {
            ((ComponentElement) parentElement).removeEvent(this);
        } else if (parentElement instanceof EventElement) {
            ((EventElement) parentElement).removeSubEvent(this);
        } else if (parentElement instanceof ApplicationElement) {
            ((ApplicationElement) parentElement).removeFreeEvent(this);
        }
        parentElement = null;
    }

    public void setParentComponent(ComponentElement parentComponent) {
        this.parentElement = parentComponent;
    }

    public void setParentEvent(EventElement parentEvent) {
        this.parentElement = parentEvent;
    }

    public void setParentApplication(ApplicationElement applicationElement) {
        this.parentElement = applicationElement;
    }

    public DataSourceElement getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSourceElement dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isCreateNew() {
        return createNew;
    }

    public void setCreateNew(boolean createNew) {
        this.createNew = createNew;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }
}
