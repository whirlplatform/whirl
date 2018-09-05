package org.whirlplatform.editor.server.merge;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.merge.Differ;
import org.whirlplatform.editor.shared.merge.Merger;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.metadata.store.AbstractMetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class JaversDiffTest {

    private MetadataStore metadata = new AbstractMetadataStore() {

        @Override
        public void saveApplication(ApplicationElement application, Version version, ApplicationUser user) {
        }

        @Override
        public ApplicationElement loadApplication(String code, Version version) {
            return null;
        }

        @Override
        public List<ApplicationStoreData> all() {
            return null;
        }

        @Override
        public ApplicationElement loadApplication(String code, Version version, boolean ignoreReferences) {
            return null;
        }

        @Override
        protected Path resolveApplicationPath(String appCode, Version version) {
            return null;
        }

        @Override
        public Version getLastVersion(String appCode) {
            return null;
        }
    };

    private Differ differ = new ReflectionJaversDiffer();
    private Merger merger = new ReflectionMerger();

    private ApplicationElement load(String file) throws MetadataStoreException, IOException {
        return metadata.deserialize(IOUtils.toString(JaversDiffTest.class.getResourceAsStream(file)));
    }

    private ApplicationElement base() throws MetadataStoreException, IOException {
        return load("base.xml");
    }

    @Test
    public void diff() throws MetadataStoreException, IOException {

        ApplicationElement base = base();

        // изменение свойства на компоненте
        checkDiff(base, load("button-change-property.xml"), base(), 1);

        // добавление кнопки на форму
        checkDiff(base, load("form-add-button.xml"), base(), 1);

        // удаление кнопки с формы
        checkDiff(base, load("form-delete-button.xml"), base(), 1);

        // добавление строки на форму
        checkDiff(base, load("form-add-row.xml"), base(), 1);

        // удаление строки с формы
        checkDiff(base, load("form-delete-row.xml"), base(), 1);

        // добавление колонки на форму
        checkDiff(base, load("form-add-column.xml"), base(), 1);

        // удаление колонки с формы
        checkDiff(base, load("form-delete-column.xml"), base(), 3);

        // добавление события
        checkDiff(base, load("add-event.xml"), base(), 1);

        // добавление права на событие
        checkDiff(load("add-event.xml"), load("event-add-right.xml"), base(), 1);

        // удаление права с события
        checkDiff(load("event-add-right.xml"), load("add-event.xml"), load("add-event.xml"), 1);

        // изменение права на событии
        checkDiff(load("event-add-right.xml"), load("event-change-right.xml"), load("event-add-right.xml"), 1);

    }

    private void checkDiff(ApplicationElement left, ApplicationElement right, ApplicationElement control,
                           int changesCount) {
        ApplicationsDiff changes = differ.diff(left, right);
        Assert.assertSame(changesCount, changes.getChanges().size());
        merger.merge(control, changes.getChanges());
        Assert.assertSame(0, differ.diff(control, right).getChanges().size());
    }
}
