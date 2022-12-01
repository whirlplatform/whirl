
package org.whirlplatform.editor.server.servlet;

import com.google.inject.AbstractModule;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import org.whirlplatform.editor.server.EditorConnector;
import org.whirlplatform.editor.server.EditorDatabaseConnector;
import org.whirlplatform.editor.server.EditorDatabaseConnectorImpl;
import org.whirlplatform.editor.server.MultibaseEditorConnector;
import org.whirlplatform.editor.server.merge.ReflectionJaversDiffer;
import org.whirlplatform.editor.server.merge.ReflectionMerger;
import org.whirlplatform.editor.server.packager.Packager;
import org.whirlplatform.editor.server.packager.ZipPackager;
import org.whirlplatform.editor.server.templates.MixedTemplateStore;
import org.whirlplatform.editor.server.templates.TemplateStore;
import org.whirlplatform.editor.shared.merge.Differ;
import org.whirlplatform.editor.shared.merge.Merger;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.config.JndiConfiguration;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.TomcatConnectionProvider;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.evolution.EvolutionManager;
import org.whirlplatform.server.login.AccountAuthenticator;
import org.whirlplatform.server.login.impl.DBAccountAuthenticator;
import org.whirlplatform.server.metadata.MetadataConfig;
import org.whirlplatform.server.metadata.MetadataConfigImpl;
import org.whirlplatform.server.metadata.MetadataProvider;
import org.whirlplatform.server.metadata.MetadataProviderImpl;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.file.FileSystemMetadataStore;

public class EditorCoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Configuration.class).to(JndiConfiguration.class);

        bind(ConnectionProvider.class).to(TomcatConnectionProvider.class);

        bind(MetadataConfig.class).to(MetadataConfigImpl.class);
        bind(MetadataDatabase.class).toInstance(MetadataDatabase.get());
        bind(MetadataProvider.class).to(MetadataProviderImpl.class);
        bind(FileSystem.class).toInstance(FileSystems.getDefault());

        bind(MetadataStore.class).to(FileSystemMetadataStore.class);

        bind(AccountAuthenticator.class).to(DBAccountAuthenticator.class);

        bind(Differ.class).to(ReflectionJaversDiffer.class);
        bind(Merger.class).to(ReflectionMerger.class);
        bind(TemplateStore.class).to(MixedTemplateStore.class);

        bind(EditorDatabaseConnector.class).to(EditorDatabaseConnectorImpl.class);
        bind(EditorConnector.class).to(MultibaseEditorConnector.class);

        bind(EvolutionManager.class).to(DummyEvolutionManager.class);

        bind(Packager.class).to(ZipPackager.class);
    }
}
