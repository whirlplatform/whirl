package org.whirlplatform.js.client;

import org.whirlplatform.component.client.utils.FileLinkHelper;

/**
 * Получение ссылок на файлы, изображения, документы, хранящиеся в таблицах базы данных
 */
public class Files {

    /**
     * Получить ссылку на содержимое файла, хранящегося в таблице tableCode в строке с
     * идентификатором rowId и столбце column
     *
     * @param tableCode - код таблицы из редактора форм
     * @param column    - название столбца типа BLOB
     * @param rowId     - идентификатор строки
     * @return String - url-ссылка на файл
     */
    public static String getTableFileLink(String tableCode, String column,
                                          String rowId) {
        return FileLinkHelper.getTableFileLinkByCode(tableCode, column, rowId);
    }

}
