package org.whirlplatform.meta.shared.editor;

import java.io.IOException;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class FileElement extends AbstractElement {

    private String fileName;
    private String contentType;
    private long checksum;
    private FileElementCategory category;

    private transient InputStreamProvider inputStreamProvider;

    public FileElement() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
        this.category = FileElementCategory.get(contentType);
    }

    public FileElementCategory getCategory() {
        return category;
    }

    public void setCategory(FileElementCategory category) {
        this.category = category;
        this.contentType = category.toString();
    }

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    public void setInputStreamProvider(InputStreamProvider inputStreamProvider) {
        this.inputStreamProvider = inputStreamProvider;
    }

    public Object getInputStream() throws IOException {
        if (inputStreamProvider == null) {
            return null;
        }
        return inputStreamProvider.get();
    }

    @Deprecated
    public String getPath() {
        if (inputStreamProvider == null) {
            return null;
        }
        return inputStreamProvider.path();
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

    public interface InputStreamProvider {

        Object get() throws IOException;

        @Deprecated
        String path();
    }
}
