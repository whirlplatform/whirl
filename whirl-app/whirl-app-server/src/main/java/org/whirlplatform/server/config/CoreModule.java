
package org.whirlplatform.server.config;

import com.google.inject.AbstractModule;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.TomcatConnectionProvider;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.driver.multibase.MultibaseConnector;
import org.whirlplatform.server.login.AccountAuthenticator;
import org.whirlplatform.server.login.impl.DBAccountAuthenticator;
import org.whirlplatform.server.metadata.MetadataConfig;
import org.whirlplatform.server.metadata.MetadataConfigImpl;
import org.whirlplatform.server.metadata.MetadataProvider;
import org.whirlplatform.server.metadata.MetadataProviderImpl;
import org.whirlplatform.server.metadata.container.DefaultMetadataContainer;
import org.whirlplatform.server.metadata.container.MetadataContainer;
import org.whirlplatform.server.metadata.store.MetadataStore;
import org.whirlplatform.server.metadata.store.file.FileSystemMetadataStore;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public class CoreModule extends AbstractModule {

	private Configuration configuration;

	public CoreModule(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void configure() {
		bind(Configuration.class).toInstance(configuration);

		bind(ConnectionProvider.class).to(TomcatConnectionProvider.class);

		bind(MetadataConfig.class).to(MetadataConfigImpl.class);
		bind(MetadataProvider.class).to(MetadataProviderImpl.class);

		bind(FileSystem.class).toInstance(FileSystems.getDefault());
        bind(MetadataStore.class).to(FileSystemMetadataStore.class); //TODO configuration.<String>lookup("Whirl/config/metadata/store")

		bind(MetadataContainer.class).to(DefaultMetadataContainer.class);

        bind(AccountAuthenticator.class).to(DBAccountAuthenticator.class); //TODO configuration.<String>lookup("Whirl/config/authenticator")

		bind(Connector.class).to(MultibaseConnector.class);
	}

}
