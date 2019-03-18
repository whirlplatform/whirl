package org.whirlplatform.meta.shared.editor;

public class DatabaseEvolution {

    private transient FileElement.InputStreamProvider inputStreamProvider;

    public DatabaseEvolution() {
    }

    public DatabaseEvolution(FileElement.InputStreamProvider inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    public FileElement.InputStreamProvider getInputStreamProvider() {
        return inputStreamProvider;
    }

}
