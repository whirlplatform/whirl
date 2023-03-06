package org.whirlplatform.meta.shared;

import java.io.Serializable;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class PageConfig implements Serializable {

    private int page = 1;

    private int rows = 0;

    private int rowsPerPage = 20;

    public PageConfig() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

}
