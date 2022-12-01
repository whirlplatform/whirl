package org.whirlplatform.editor.client.tree.dummy;

import org.whirlplatform.editor.client.tree.visitor.VisitableTreeElement;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 * Базовый класс для отображения заголовков разделов в дереве приложения
 */
@SuppressWarnings("serial")
public abstract class AbstractDummyElement extends AbstractElement
        implements VisitableTreeElement, TreeDummy {

    public AbstractDummyElement() {
        super();
        setName(getTitle());
    }

    /**
     * @param id - id родительского AbstractElement. Уникальность dummy-элемента обеспечивается
     *           добавлением префикса, уникального для каждого класса dummy
     */
    public AbstractDummyElement(final String id) {
        this();
        this.setId(id);
    }

    /**
     * Задает id для dummy используя заданный id родительского AbstractElement и префикса,
     * уникального для каждого класса dummy
     *
     * @param id родительского AbstractElement
     */
    @Override
    public void setId(final String id) {
        StringBuilder sb = new StringBuilder(getIdPrefix());
        sb.append(id);
        super.setId(sb.toString());
    }

    /**
     * @return Префикс, используемый при формировании id dummy-элемента
     */
    protected abstract String getIdPrefix();

    /**
     * @return Название раздела, отображаемое в дереве приложения
     */
    protected abstract String getTitle();
}
