package org.whirlplatform.meta.shared.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface AppMessage extends Messages {

    class Util {
        public static AppMessage MESSAGE = GWT.create(AppMessage.class);
    }

    String yes();

    String no();

    // execute
    String execute();

    String executing();

    String open();

    // info
    String info();

    String infoCaptchaHowTo();

    String infoNotEditable();

    String infoNotDeletable();

    // success
    String success();

    String successSqlExecuted();

    // errors
    String error();

    String errorDBConnect();

    String errorDBConnectLimitExceeded();

    String errorComponentPropertiesUnloaded();

    String errorCaptchaVerification();

    String errorClearFilter();

    String errorAppData();

    String errorAlreadyLogin();

    String errorWrongURL();

    String errorAppAccess();

    String errorServerConnection();

    // allerts
    String alert();

    String alert_notAllFieldsIsFill();

    String alert_notAllRequiredFieldsIsFill();

    String alert_sessionExpired();

    String alert_noCompBuilder();

    String alert_event_cancelled();

    // sort
    String sort_form();

    String sort_to();

    String sort_order();

    String sort_asc();

    String sort_desc();

    String sort_moveUp();

    String sort_moveDown();

    String sort_add();

    String sort_addAll();

    String sort_remove();

    String sort_removeAll();

    String diagram_building();

    String diagram_create();

    String diagram_noData();

    String diagram_noFlash();

    String diagram_total();

    String pleaseWait();

    String loading();

    String loadingMask();

    String slaves();

    // actions
    String selectRecords();

    String selectOneRecord();

    // дерево
    String menuTitle();

    String data();

    String filterTitle();

    String changeData();

    String searchData();

    String exportImportData();

    String techData();

    // Actions : CRUD
    String filter();

    String doFilter();

    String delete();

    String insert();

    String update();

    String view();

    String clear();

    String refresh();

    String close();

    String save();

    String discard();

    String sort();

    String copy();

    String slaveTables();

    String choose();

    String refreshable();

    String refreshDisable();

    String refreshEnable();

    String search();

    String selectValue();

    String rowsOnPage();

    String rowsFromTo(int from, int to, int of);

    String expimp_allRecords();

    String expimp_pageRecords();

    String expimp_exportRecords();

    String expimp_titles();

    String expimp_columns();

    String expimp_header();

    String expimp_xls();

    String expimp_xlsx();

    String expimp_format();

    String expimp_expCSV();

    String expimp_exportCSV();

    String expimp_impCSV();

    String expimp_importCSV();

    String expimp_expXLS();

    String expimp_exportXLS();

    String expimp_impXLS();

    String expimp_importXLS();

    String expimp_importFile();

    String expimp_notAllImported();

    String expimp_importSuccess(String table);

    String expimp_doImport();

    String expimp_doExport();

    String expimp_exportData(String name);

    String confirm();

    String confirmAsk();

    String confirmDelete();

    String noDirtyFields();

    String alert_rowEdit();

    // filter headers
    String filter_name();

    String filter_condition();

    String filter_value1();

    String filter_value2();

    String filter_filter();

    // filter conditions
    String filter_condition_no_filter();

    String filter_condition_between();

    String filter_condition_equals();

    String filter_condition_contains();

    String filter_condition_not_contains();

    String filter_condition_greater();

    String filter_condition_lower();

    String filter_condition_empty();

    String filter_condition_not_empty();

    String filter_condition_start_with();

    String filter_condition_end_with();

    String filter_condition_reverse();

    String dataChanged();

    String insertRecord();

    String copyRecord();

    String editRecord();

    String viewRecord();

    String window_close();

    String window_maximize();

    String window_minimize();

    String window_restore();

    String masterTable();

    String exportXLS();

    String exportCSV();

    String importXLS();

    String importCSV();

    String menuExpand();

    String menuMinimize();

    String menuReload();

    // тех.процессы
    String methodExecTitle();

    String closeAll();

    String closeOthers();

    String requiredField();

    String notEditableField();

    String notInsertableTable();

    String notEditableTable();

    String notDeletableTable();

    String noTableField(String property);

    String file_notUploaded();

    String recordsSelected();

    String login_login();

    String login_password();

    String login_header();

    String login_auth();

    String login_needLoginPassord();

    String login_authorizing();

    String login_oldPassword();

    String login_newPassord();

    String login_confirmPassword();

    String login_button();

    String login_error();

    String page_noJavaScript();

    String page_loading();

    String page_loadingInnerHTML();

    String report_makeDocument();

    String report_makeHTML();

    String report_makeXLSX();

    String report_makeXLS();

    String report_makeCSV();

    // Form editor


    String errorWrongApplication();

    String deploy_deploy_failed();

    String deploy_rollback_failed();

    String reloadPage();

    String errorCannotCheckPWDChange();

    String emptyValue();

    String noData();
}
