package org.whirlplatform.event.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.AttachEvent;
import org.whirlplatform.component.client.event.BlurEvent;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.CreateEvent;
import org.whirlplatform.component.client.event.DeleteEvent;
import org.whirlplatform.component.client.event.DetachEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.component.client.event.EventHelper;
import org.whirlplatform.component.client.event.EventManager;
import org.whirlplatform.component.client.event.FocusEvent;
import org.whirlplatform.component.client.event.HideEvent;
import org.whirlplatform.component.client.event.InsertEvent;
import org.whirlplatform.component.client.event.KeyPressEvent;
import org.whirlplatform.component.client.event.LoadEvent;
import org.whirlplatform.component.client.event.RefreshEvent;
import org.whirlplatform.component.client.event.RowDoubleClickEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.event.ShowEvent;
import org.whirlplatform.component.client.event.TimeEvent;
import org.whirlplatform.component.client.event.UpdateEvent;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.component.ComponentModel;

public class EventManagerImpl implements EventManager {

    public EventHelper wrapEvent(EventMetadata metadata) {
        return new EventHelperImpl(metadata);
    }

    public void addEvents(ComponentBuilder builder, ComponentModel model) {
        if (builder instanceof AttachEvent.HasAttachHandlers) {
            if (model.hasEvents(AttachEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(AttachEvent.getType())) {
                    builder.addAttachHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof ChangeEvent.HasChangeHandlers) {
            if (model.hasEvents(ChangeEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(ChangeEvent.getType())) {
                    ((ChangeEvent.HasChangeHandlers) builder).addChangeHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof ClickEvent.HasClickHandlers) {
            if (model.hasEvents(ClickEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(ClickEvent.getType())) {
                    ((ClickEvent.HasClickHandlers) builder).addClickHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof CreateEvent.HasCreateHandlers) {
            if (model.hasEvents(CreateEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(CreateEvent.getType())) {
                    builder.addCreateHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof DeleteEvent.HasDeleteHandlers) {
            if (model.hasEvents(DeleteEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(DeleteEvent.getType())) {
                    ((DeleteEvent.HasDeleteHandlers) builder).addDeleteHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof DetachEvent.HasDetachHandlers) {
            if (model.hasEvents(DetachEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(DetachEvent.getType())) {
                    builder.addDetachHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof DoubleClickEvent.HasDoubleClickHandlers) {
            if (model.hasEvents(DoubleClickEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(DoubleClickEvent.getType())) {
                    ((DoubleClickEvent.HasDoubleClickHandlers) builder).addDoubleClickHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof InsertEvent.HasInsertHandlers) {
            if (model.hasEvents(InsertEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(InsertEvent.getType())) {
                    ((InsertEvent.HasInsertHandlers) builder).addInsertHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof KeyPressEvent.HasKeyPressHandlers) {
            if (model.hasEvents(KeyPressEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(KeyPressEvent.getType())) {
                    ((KeyPressEvent.HasKeyPressHandlers) builder).addKeyPressHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof LoadEvent.HasLoadHandlers) {
            if (model.hasEvents(LoadEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(LoadEvent.getType())) {
                    ((LoadEvent.HasLoadHandlers) builder).addLoadHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof SelectEvent.HasSelectHandlers) {
            if (model.hasEvents(SelectEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(SelectEvent.getType())) {
                    ((SelectEvent.HasSelectHandlers) builder).addSelectHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof HideEvent.HasHideHandlers) {
            if (model.hasEvents(HideEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(HideEvent.getType())) {
                    builder.addHideHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof FocusEvent.HasFocusHandlers) {
            if (model.hasEvents(FocusEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(FocusEvent.getType())) {
                    builder.addFocusHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof BlurEvent.HasBlurHandlers) {
            if (model.hasEvents(BlurEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(BlurEvent.getType())) {
                    builder.addBlurHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof ShowEvent.HasShowHandlers) {
            if (model.hasEvents(ShowEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(ShowEvent.getType())) {
                    builder.addShowHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof UpdateEvent.HasUpdateHandlers) {
            if (model.hasEvents(UpdateEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(UpdateEvent.getType())) {
                    ((UpdateEvent.HasUpdateHandlers) builder).addUpdateHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof TimeEvent.HasTimeHandlers) {
            if (model.hasEvents(TimeEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(TimeEvent.getType())) {
                    ((TimeEvent.HasTimeHandlers) builder).addTimeHandler(new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof RefreshEvent.HasRefreshHandlers) {
            if (model.hasEvents(RefreshEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(RefreshEvent.getType())) {
                    ((RefreshEvent.HasRefreshHandlers) builder).addRefreshHandler(
                            new EventHelperImpl(e));
                }
            }
        }
        if (builder instanceof RowDoubleClickEvent.HasRowDoubleClickHandlers) {
            if (model.hasEvents(RowDoubleClickEvent.getType())) {
                for (EventMetadata e : model.getEventsByType(RowDoubleClickEvent.getType())) {
                    ((RowDoubleClickEvent.HasRowDoubleClickHandlers) builder).addRowDoubleClickHandler(
                            new EventHelperImpl(e));
                }
            }

        }
    }

    @Override
    public void addMenuTreeEvent(ComponentBuilder builder, EventMetadata e) {
        if (builder instanceof SelectEvent.HasSelectHandlers) {
            ((SelectEvent.HasSelectHandlers) builder).addSelectHandler(new EventHelperImpl(e));
        }
    }

}
