package org.whirlplatform.component.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ApplicationBundle extends ClientBundle {

    ApplicationBundle INSTANCE = GWT
			.create(ApplicationBundle.class);

	@Source("add.gif")
	ImageResource add();

	@Source("copy.png")
	ImageResource copy();

	@Source("delete.gif")
	ImageResource delete();

	@Source("edit.png")
	ImageResource edit();

	@Source("refresh.gif")
	ImageResource refresh();

	@Source("refresh-on.png")
	ImageResource refresh_on();

	@Source("refresh-off.png")
	ImageResource refresh_off();

	@Source("search.gif")
	ImageResource search();

	@Source("slave.gif")
	ImageResource slave();

	@Source("sort.gif")
	ImageResource sort();

	@Source("view.png")
	ImageResource view();

	@Source("method.png")
	ImageResource method();

	@Source("excel.png")
	ImageResource excel();

	@Source("csv.gif")
	ImageResource csv();

	@Source("next.gif")
	ImageResource next();

	@Source("next-disabled.gif")
	ImageResource next_disabled();

	@Source("previous.gif")
	ImageResource previous();

	@Source("previous-disabled.gif")
	ImageResource previous_disabled();

	@Source("first.gif")
	ImageResource first();

	@Source("first-disabled.gif")
	ImageResource first_disabled();

	@Source("last.gif")
	ImageResource last();

	@Source("last-disabled.gif")
	ImageResource last_disabled();

	@Source("arrow-up.png")
	ImageResource arrow_up();

	@Source("arrow-down.png")
	ImageResource arrow_down();

	@Source("tick.png")
	ImageResource tick();

	@Source("cross.png")
	ImageResource cross();
	
	@Source("magnifier-left.png")
	ImageResource magnifier();
	
	@Source("magnifier-left-exclamation.png")
	ImageResource magnifierExclamation();
}
