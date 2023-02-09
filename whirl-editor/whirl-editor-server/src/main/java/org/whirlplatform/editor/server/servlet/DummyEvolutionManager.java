package org.whirlplatform.editor.server.servlet;

import org.whirlplatform.server.evolution.EvolutionException;
import org.whirlplatform.server.evolution.EvolutionManager;

/**
 * This evolution manager do nothing.
 */
public class DummyEvolutionManager implements EvolutionManager {

    @Override
    public void applyApplicationEvolution(String alias, String scriptPath)
        throws EvolutionException {
        // do nothing
    }

    @Override
    public void applyMetadataEvolution(String alias, String scriptPath) throws EvolutionException {
        // do nothing
    }

    @Override
    public void rollbackApplicationEvolution(String alias, String scriptPath) throws EvolutionException {
        // do nothing
    }

    @Override
    public void rollbackMetadataEvolution(String alias, String scriptPath) throws EvolutionException {
        // do nothing
    }
}
