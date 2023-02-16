package org.whirlplatform.server.evolution;

public interface EvolutionManager {

    void applyApplicationEvolution(String alias, String scriptPath) throws EvolutionException;

    void applyMetadataEvolution(String alias, String scriptPath) throws EvolutionException;

    void rollbackApplicationEvolution(String alias, String scriptPath) throws EvolutionException;

    void rollbackMetadataEvolution(String alias, String scriptPath) throws EvolutionException;

}
