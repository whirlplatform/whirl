package org.whirlplatform.editor.client.tree;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.presenter.compare.ChangesSorter;
import org.whirlplatform.editor.client.presenter.compare.ElementChangeState;
import org.whirlplatform.editor.client.tree.cell.ComparableAppTreeCell;
import org.whirlplatform.editor.client.tree.menu.ComparableAppTreeMenu;
import org.whirlplatform.editor.client.view.widget.ChangeUnitsContainer;
import org.whirlplatform.editor.client.view.widget.WidgetUtil;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

import java.util.*;

/**
 *
 */
public abstract class ComparableAppTreeWidget extends AbstractAppTree implements ComparableAppTree {
	private final static String CLOSE = EditorMessage.Util.MESSAGE.close();
	private final static String SAVE = EditorMessage.Util.MESSAGE.save();
	private final static String UNDO = EditorMessage.Util.MESSAGE.undo();
	private final static int HEIGHT = WidgetUtil.MAX_WINDOW_HEIGHT * 2 / 3;
	
	private Map<String, ChangeUnit> changeUnitsMap;
	private ChangesSorter changesSorter;
	private final ChangeUnitsContainer changesContainer = new ChangeUnitsContainer();

	public ComparableAppTreeWidget() {
		super();
		setContextMenu(new ComparableAppTreeMenu(this));
		setCell(new ComparableAppTreeCell(this));
		changeUnitsMap = new HashMap<>();
		changesSorter = new ChangesSorter(this.getStore());
		setCheckable(true);
		setCheckStyle(CheckCascade.NONE);
		setAutoLoad(true);
	}

	@Override
	public void loadApplication(final ApplicationElement app, final Version version, final AppTreePresenter presenter) {
		changeUnitsMap.clear();
		super.loadApplication(app, version, presenter);
		changesSorter = new ChangesSorter(this.getStore());
		setCheckable(false);
		this.setExpanded(getApplication(), true, true);
	}

	@Override
	protected void onCheckClick(Event event, TreeNode<AbstractElement> node) {
		super.onCheckClick(event, node);
		final AbstractElement element = node.getModel();
		final String elementId = element.getId();
		if (changesSorter.changesExist(elementId)) {
			for (ChangeUnit unit : changesSorter.getChangeUnits(elementId)) {
				unit.setApproved(this.isChecked(element));
			}
		}
	}
	
	protected abstract void showDiff(final List<ChangeUnit> changes, final Callback<Map<String, Boolean>, Throwable> callback);

	protected void showDiff(final List<ChangeUnit> changes, boolean locked, final Callback<Map<String, Boolean>, Throwable> callback) {
		if (changes == null) {
			return;
		}
		final Window window = new Window();
		window.setModal(true);
		window.setHeight(HEIGHT);
		changesContainer.setLocked(locked);
		changesContainer.loadData(changes);
		window.add(changesContainer);
		TextButton closeButton = new TextButton(CLOSE);
		closeButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				changesContainer.undoSelection();
				callback.onFailure(null);
				window.hide();
			}
		});
		TextButton undoButton = new TextButton(UNDO);
		undoButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				changesContainer.undoSelection();
			}
		});
		TextButton saveButton = new TextButton(SAVE);
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				callback.onSuccess(changesContainer.getSelectionMap());
				window.hide();
			}
		});
		window.addButton(saveButton);
		window.addButton(undoButton);
		window.addButton(closeButton);
		saveButton.setEnabled(!locked);
		undoButton.setEnabled(!locked);
		window.show();
	}

	@Override
	public void doOpenElement(AbstractElement element) {
		showDiff(getChangeUnits(element), new Callback<Map<String, Boolean>, Throwable>() {
			@Override
			public void onFailure(Throwable reason) {
			}
			@Override
			public void onSuccess(Map<String, Boolean> result) {
				for(Map.Entry<String, Boolean> entry : result.entrySet()) {
					changeUnitsMap.get(entry.getKey()).setApproved(entry.getValue());
				}
			}
		});
	}

	@Override
	public boolean renameSelectedElement() {
		return false;
	}

	@Override
	public ElementChangeState getChangeState(final AbstractElement element) {
		return changesSorter.getChangeState(element.getId());
	}

	@Override
	public void clearAll() {
		setCell(new ComparableAppTreeCell(this));
		changeUnitsMap.clear();
		changesSorter.clear();
		super.clearAll();
	}
	
	@Override
	public void clearChanges() {
		setCell(new ComparableAppTreeCell(this));
		changeUnitsMap.clear();
		changesSorter.clear();
		this.setCheckable(false);
		super.redraw(null);
		this.setExpanded(getApplication(), true, true);
	}

	@Override
	public void setChanges(List<ChangeUnit> changes) {
		for (ChangeUnit unit : changes) {
			changeUnitsMap.put(unit.getId(), unit);
		}
		changesSorter.handleChanges(changes);
		this.setCheckable(true);
		super.redraw(null);
		this.setExpanded(getApplication(), true, true);
	}

	@Override
	public List<ChangeUnit> getChangeUnits(AbstractElement element) {
		return Collections.unmodifiableList(changesSorter.getChangeUnits(element.getId()));
	}

	@Override
	public List<ChangeUnit> getCheckedChangeUnits() {
		List<ChangeUnit> result = new ArrayList<>();
		for (ChangeUnit unit : changeUnitsMap.values()) {
			if (unit.isApproved()) {
				result.add(unit);
			}
		}
		return Collections.unmodifiableList(result);
	}
}
