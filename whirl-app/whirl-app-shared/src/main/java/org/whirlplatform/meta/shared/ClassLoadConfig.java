package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.whirlplatform.meta.shared.data.DataValue;

@SuppressWarnings("serial")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = Id.MINIMAL_CLASS)
@JsonSubTypes(@JsonSubTypes.Type(TreeClassLoadConfig.class))
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class ClassLoadConfig implements Serializable {

    private PageConfig page = new PageConfig();

    private String whereSql;

    public PageConfig getPage() {
        return page;
    }

    public void setPage(PageConfig page) {
        this.page = page;
    }

    public void setFilters(List<FilterValue> filters) {
        this.filters = filters;
    }

    public void setSorts(List<SortValue> sorts) {
        this.sorts = sorts;
    }

    private String query;

    private boolean all = false;

    private List<FilterValue> filters = new ArrayList<FilterValue>();

    private List<SortValue> sorts = new ArrayList<SortValue>();

    private Map<String, DataValue> parameters = new HashMap<String, DataValue>();

    @Deprecated
    private boolean useSearchParameters = false;

    private boolean reloadMetadata;

    private FieldMetadata tableField;

    private String labelExpression;

    public ClassLoadConfig() {
    }

    public String getLabelExpression() {
        return this.labelExpression;
    }

    public void setLabelExpression(String newLabelExpression) {
        this.labelExpression = newLabelExpression;
    }

    public int getPageNum() {
        return page.getPage();
    }

    public void setPageNum(int page) {
        this.page.setPage(page);
    }

    public int getRows() {
        return this.page.getRows();
    }

    public void setRows(int rows) {
        this.page.setRows(rows);
    }

    public int getRowsPerPage() {
        return this.page.getRowsPerPage();
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.page.setRowsPerPage(rowsPerPage);
    }

    public PageConfig getPageConfig() {
        return page;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getWhereSql() {
        return whereSql;
    }

    public void setWhereSql(String whereSql) {
        this.whereSql = whereSql;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public void addFilter(FilterValue filter) {
        filters.add(filter);
    }

    public void addFilterAll(Collection<FilterValue> values) {
        filters.addAll(values);
    }

    public List<FilterValue> getFilters() {
        return filters;
    }

    public void clearFilters() {
        filters.clear();
    }

    public void addSort(SortValue sort) {
        sorts.add(sort);
    }

    public void addSortAll(Collection<SortValue> values) {
        sorts.addAll(values);
    }

    public List<SortValue> getSorts() {
        return sorts;
    }

    public void clearSort() {
        sorts.clear();
    }

    public FilterValue getFilterValue(FieldMetadata field) {
        for (FilterValue f : filters) {
            if (field.equals(f.getMetadata())) {
                return f;
            }
        }
        FilterValue value = new FilterValue(field);
        filters.add(value);
        return value;
    }

    public void addParameter(String code, DataValue value) {
        parameters.put(code, value);
    }

    public Map<String, DataValue> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public void setParameters(Map<String, DataValue> parameters) {
        this.parameters = parameters;
    }

    @Deprecated
    public boolean isUseSearchParameters() {
        return useSearchParameters;
    }

    @Deprecated
    public void setUseSearchParameters(boolean useSearchParameters) {
        this.useSearchParameters = useSearchParameters;
    }

    public boolean isReloadMetadata() {
        return reloadMetadata;
    }

    public void setReloadMetadata(boolean reloadMetadata) {
        this.reloadMetadata = reloadMetadata;
    }

    public FieldMetadata getTableField() {
        return tableField;
    }

    public void setTableField(FieldMetadata tableField) {
        this.tableField = tableField;
    }
}
