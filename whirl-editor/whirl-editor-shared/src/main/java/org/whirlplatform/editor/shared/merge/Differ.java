package org.whirlplatform.editor.shared.merge;

import org.whirlplatform.meta.shared.editor.ApplicationElement;

public interface Differ {

    ApplicationsDiff diff(ApplicationElement base, ApplicationElement target) throws DiffException;

}
