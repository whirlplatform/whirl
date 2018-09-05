package org.whirlplatform.editor.shared.visitor;

import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

public class SearchGraphVisitor extends GraphVisitor<SearchGraphVisitor.SearchVisitorContext> {

    static class SearchVisitorContext implements ElementVisitor.VisitContext {

        private ApplicationElement application;
        private String currentSearchId;

        private AbstractElement result;

        public SearchVisitorContext(ApplicationElement application, String currentSearchId) {
            this.application = application;
            this.currentSearchId = currentSearchId;
        }

        public String getCurrentSearchId() {
            return currentSearchId;
        }

        public void setResult(AbstractElement result) {
            this.result = result;
        }

        public AbstractElement getResult() {
            return result;
        }

    }

    public SearchGraphVisitor() {

    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractElement> T search(ApplicationElement application, String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can not be null");
        }
        SearchVisitorContext ctx = new SearchVisitorContext(application, id);
        application.accept(ctx, this);

        // зависимости ищем отдельно
        for (ApplicationElement r : application.getReferences()) {
            if (searchAndFinish(ctx, r)) {
                break;
            }
        }

        return (T) ctx.getResult();
    }

    private boolean searchAndFinish(SearchVisitorContext ctx, AbstractElement element) {
        if (ctx.getResult() != null) {
            return true;
        }
        if (element == null) {
            return false;
        }
        if (ctx.getCurrentSearchId().equals(element.getId())) {
            ctx.setResult(element);
            return true;
        }
        return false;
    }

    @Override
    public void visit(SearchVisitorContext ctx, AbstractElement element) {
        searchAndFinish(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, ApplicationElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, CellElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, CellRangeElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, ColumnElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, ComponentElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, EventElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, EventParameterElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, FileElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, FormElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, GroupElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, ReportElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, RequestElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, RightCollectionElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, RowElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, DataSourceElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, SchemaElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, TableColumnElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, AbstractTableElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, ViewElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, DatabaseTableElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, PlainTableElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, DynamicTableElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

    @Override
    public void visit(SearchVisitorContext ctx, ContextMenuItemElement element) {
        if (searchAndFinish(ctx, element)) {
            return;
        }
        super.visit(ctx, element);
    }

}
