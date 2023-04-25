package org.whirlplatform.meta.shared;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormatInfo;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;

public class AppConstant {

    public static final String TOKEN = "token";
    public static final String TOKEN_ID = "TOKEN_ID";

    public static final String NEW_SESSION = "ns";
    public static final String OLD_TOKEN = "ot";

    /**
     * Текущий пользователь.
     */
    public static final String WHIRL_USER = "WHIRL_USER";

    /**
     * Аттрибут указывающй на то, что пользователь является гостем(не авторизован).
     */
    public static final String WHIRL_USER_GUEST = "WHIRL_USER_GUEST";

    /**
     * Текущее приложение.
     */
    public static final String WHIRL_APPLICATION = "WHIRL_APPLICATION";

    /**
     * Текущий IP пользователя.
     */
    public static final String WHIRL_IP = "WHIRL_IP";

    /**
     * Группы пользователя.
     */
    public static final String WHIRL_USER_GROUPS = "WHIRL_USER_GROUPS";

    /**
     * Признак перезагрузки формы.
     */
    public static final String WHIRL_FORM_REFRESH = "WHIRL_FORM_REFRESH";

    /**
     * Результат выполнения для события.
     */
    public static final String WHIRL_RESULT = "WHIRL_RESULT";

    /**
     * Формат отчета.
     */
    public static final String WHIRL_REPORT_FORMAT = "WHIRL_REPORT_FORMAT";

    /**
     * Идентификатор сессии.
     */
    public static final String WHIRL_TOKEN_ID = "WHIRL_TOKEN_ID";

    /**
     * формат даты
     */
    public static final String DATE_FORMAT_LONG = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_FORMAT_LONGEST = "dd.MM.yyyy HH:mm:ss.SSS";
    public static final String DATE_FORMAT_ORACLE = "dd.MM.yyyy HH24:mi:ss";
    public static final String DATE_FORMAT_SHORT = "dd.MM.yyyy";
    public static final String DATE_FORMAT_SERIALIZE = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    /**
     * Формат чисел
     */
    public static final String NUMBER_FORMAT = "#.##";
    /**
     * параметр сервлета эспорта - тип (pdf,rtf,xls)
     */
    public static final String EXPIMP_TYPE_PARAM = "type";
    /**
     * параметр сервлета эспорта - заголовок отчета
     */
    public static final String EXPORT_TITLE_PARAM = "title";
    /**
     * параметр сервлета эспорта - ориентация страницы
     */
    public static final String EXPORT_LANDSCAPE_PARAM = "landscape";
    /**
     * параметр сервлета эспорта - выборка всех записей
     */
    public static final String EXPORT_ALLREC_PARAM = "allrec";
    public static final String EXPORT_COLUMNS_PARAM = "columns";
    public static final String EXPORT_XLSX_PARAM = "xlsx";
    /**
     * Модули
     */
    public static final String module_app = "app";
    public static final String REPORT_FORMAT_XLS = "xls";
    public static final String REPORT_FORMAT_XLSX = "xlsx";
    public static final String REPORT_FORMAT_HTML = "html";
    public static final String REPORT_FORMAT_CSV = "csv";
    /* Файлы */
    public static final String GETTYPE = "gettype";
    public static final String TABLE = "table";
    public static final String UPLOAD = "upload";
    public static final String FORM_UPLOAD = "form_upload";
    public static final String FIELD = "field";
    public static final String TABLE_ID = "tableid";
    public static final String TABOLE_CODE = "tablecode";
    public static final String ID = "id";
    public static final String CONTENT_TYPE = "contenttype";
    public static final String ATTACHMENT = "attachment";
    public static final String SAVE_FILE_NAME = "savefilename";
    public static final String CAPTCHA_SESSION_KEY = "captcha.code";
    public static final String APPLICATION_URL = "application";
    public static final String EVENT_URL = "event";
    public static final String BRANCH_URL = "branch";
    public static final String VERSION_URL = "version";
    public static final String MASK_EMPTY = "MASK_EMPTY";
    public static final String VALUE = "VALUE";
    public static final String STYLE = "STYLE";
    private static DateTimeFormat dateFormatLong;
    private static DateTimeFormat dateFormatShort;
    private static DateTimeFormat dateFormatSerialize;

    public static DateTimeFormat getDateFormatLong() {
        if (dateFormatLong == null) {
            DateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
            dateFormatLong = new DateTimeFormat(AppConstant.DATE_FORMAT_LONG,
                info) {
            };
        }
        return dateFormatLong;
    }

    public static DateTimeFormat getDateFormatShort() {
        if (dateFormatShort == null) {
            DateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
            dateFormatShort = new DateTimeFormat(AppConstant.DATE_FORMAT_SHORT,
                info) {
            };
        }
        return dateFormatLong;
    }

    public static DateTimeFormat getDateFormatSerialize() {
        if (dateFormatSerialize == null) {
            DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
            dateFormatSerialize = new DateTimeFormat(AppConstant.DATE_FORMAT_SERIALIZE, info) {
            };
        }
        return dateFormatSerialize;
    }

}