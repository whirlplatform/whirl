package org.whirlplatform.editor.client.presenter.compare;

import org.whirlplatform.editor.shared.merge.ChangeType;

/**
 *
 */
public enum ElementChangeState {
    ADDED, INHERITED_ADDED, REMOVED, INHERITED_REMOVED, CHANGED, NONE;

    public static ElementChangeState convert(final ChangeType changeType) {
        if (changeType == null) {
            return NONE;
        }
        switch (changeType) {
            case Add:
                return ADDED;
            case Change:
                return CHANGED;
            case Remove:
                return REMOVED;
            default:
                break;
        }
        return NONE;
    }
}