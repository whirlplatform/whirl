package org.whirlplatform.editor.client;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.SIMPLE)
public class EditorHistoryConverter implements HistoryConverter<EditorEventBus> {

    public EditorHistoryConverter() {

    }

    @Override
    public void convertFromToken(String historyName, String param, EditorEventBus eventBus) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }

    public String convertToToken(String eventType) {
        return "";
    }

    public void onInitHistory() {
    }
}
