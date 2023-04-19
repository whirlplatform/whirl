package org.whirlplatform.editor.shared.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

@SuppressWarnings("checkstyle:all")
public interface EditorMessage extends Messages {

    String help_db();

    String select_icon();

    String login_submit();

    String login_fill_login_password();

    String login_login();

    String login_password();//

    String refresh();

    String title();

    String success();

    String save();

    String close();

    String back();

    String next();

    String create();

    String load();

    String open();

    String expand();

    String collapse();

    String text();

    String message();

    String exit();

    String apply();

    String properties();

    String property();

    String merge();

    String date();

    String revision();

    String code();

    String modified();

    String branch();

    String version();

    String language();

    String country();

    String comment();

    String checkout();

    String update();

    String undo();

    String commit();

    String remove();

    String save_as();

    String run();

    String action_load_application();

    String action_merge_selection();

    String action_compare_applications();

    String action_save_application();

    String toolbar_new();

    String toolbar_save_xml();

    String toolbar_load_xml();

    String toolbar_creating_application();

    String toolbar_package();

    String toolbar_compare();

    String compare_apps_title();

    String compare_apps_apply_title();

    String compare_apps_apply_text();

    String compare_apps_compare_result(int number);

    String compare_apps_added();

    String compare_apps_removed();

    String compare_apps_changed();

    String compare_apps_nested();

    String compare_apps_value();

    String compare_apps_type();

    String compare_apps_target();

    String all_applications_header();

    String all_applications_close();

    String all_applications_retrieving_apps();

    String new_application_name();

    String new_application_code();

    String new_application_title();

    String new_application_locale();

    String new_application_version();

    String new_application_exists();

    String context_menu_rename();

    String context_menu_add();

    String context_menu_copy();

    String context_menu_paste();

    String context_menu_remove();

    String context_menu_rights();

    String context_menu_cut();

    String context_menu_changes();

    String application_code();

    String application_title();

    String application_guest();

    String application_css();

    String application_javascript();

    String application_java();

    String application_images();

    String application_static();

    String application_html_header();

    String application_download();

    String application_add();

    String application_file_name();

    String datasource_synonym();

    String datasource_source_name();

    String design_remove();

    String design_row_add();

    String design_row_remove();

    String design_column_add();

    String design_column_remove();

    String design_union();

    String design_split();

    String design_border_top();

    String design_border_right();

    String design_border_bottom();

    String design_border_left();

    String design_background_color();

    String design_border_color();

    String design_border_width();

    String design_clear_color();

    String design_show_hidden();

    String design_hide_hidden();

    String event_parameter_type();

    String event_parameter_code();

    String event_parameter_component();

    String event_parameter_component_code();

    String event_parameter_storage_code();

    String event_parameter_static();

    String event_parameter_data_type();

    String event_parameter_value();

    String event_parameter_list_id();

    String event_parameter_list_value();

    String event_parameter_remove();

    String event_parameter_add();

    String event_parameter_title();

    String event_type();

    String event_handler();

    String event_code();

    String event_single_parameter();

    String event_confirm();

    String event_confirm_text();

    String event_wait();

    String event_wait_text();

    String event_schema();

    String event_function();

    String event_source();

    String event_component();

    String event_contaner();

    String event_table();

    String event_datasource();

    String event_create_new();

    String group_name();

    String group_group();

    String group_add();

    String group_remove();

    String locale_lang();

    String locale_country();

    String locale_add();

    String locale_remove();

    String locale_err_msg();

    String property_parameters();

    String property_maket();

    String property_no_data();

    String property_form_row_height();

    String property_form_row_index();

    String property_form_row_h();

    String property_form_column_width();

    String property_form_column_index();

    String property_form_column_w();

    String property_form_sql_query();

    String property_form_sql_by_row();

    String property_form_sql_top_index();

    String property_form_sql_bottom_index();

    String property_form_sql_no_data();

    String property_form_sql_datasource();

    String property_form_sql_already_exists();

    String property_form_sql_add();

    String property_form_sql_remove();

    String property_form_sql_edit();

    String property_form_sql_editing();

    String editing_application();

    String editing_datasource();

    String editing_event_parameter();

    String editing_event();

    String editing_schema();

    String editing_table();

    String editing_group();

    String editing_locale();

    String editing_context_menu();

    String error();

    String error_more_than_one_element();

    String error_application_is_main();

    String error_dynamic_table_empty_metadata();

    String error_dynamic_table_empty_data();

    String error_dynamic_table_get_data();

    String error_dynamic_table_insert();

    String error_dynamic_table_update();

    String error_dynamic_table_delete();

    String error_import_application();

    String error_version_format();

    String error_branch_format();

    String error_name_format();

    String error_language_format();

    String error_country_format();

    String warn();

    String warn_need_to_split();

    String warn_tables_already_imported();

    String warn_this_tables_already_imported();

    String warn_element_deletion();

    String warn_is_delete(String elementName);

    String warn_input_data_is_not_valid();

    String info();

    String info_application_saved();

    String info_tables_imported();

    String info_table_saved();

    String property_report_master_table();

    String property_report_field_type();

    String property_report_required();

    String property_report_field_name();

    String property_report_field_code();

    String property_report_list_view_type();

    String property_report_parameter();

    String property_report_append();

    String right_edit_edit_condition();

    String right_edit_condition_type();

    String right_edit_condition_value();

    String right_edit_application();

    String right_edit_right();

    String schema_name_changed_message();

    String schema_schema();

    String schema_import();

    String schema_check_import_objects();

    String schema_table();

    String schema_name();

    String schema_source();

    String schema_object_type();

    String schema_processing();

    String table_name();

    String table_code();

    String table_empty_row();

    String table_primary_key();

    String table_delete_column();

    String table_view_name();

    String table_edit();

    String table_synchronize();

    String table_index();

    String table_view_edit();

    String table_clear();

    String table_metadata_function();

    String table_data_function();

    String table_insert_function();

    String table_update_function();

    String table_delete_function();

    String table_no_subrequest();

    String table_column_name();

    String table_column_datatype();

    String table_column_list_table();

    String table_column_label();

    String table_column_size();

    String table_column_scale();

    String table_column_width();

    String table_column_height();

    String table_column_value();

    String table_column_not_null();

    String table_column_hidden();

    String table_column_filter();

    String table_column_data_format();

    String table_column_regex();

    String table_column_regex_message();

    String table_column_default_order();

    String table_column_order();

    String table_column_config_column();

    String table_db_table();

    String session_expired();

    String session_expired_on_save();

    String alert_editor_not_allowed();

    String dummy_application_locales();

    String dummy_application_groups();

    String dummy_application_datasources();

    String dummy_application_events();

    String dummy_application_components();

    String dummy_application_freecomponents();

    String dummy_application_references();

    String dummy_application_addrefference();

    String dummy_component_events();

    String dummy_datasource_schemas();

    String dummy_event_parameters();

    String dummy_event_subevents();

    String dummy_schema_tables();

    String dummy_schema_dynamic_tables();

    String dummy_table_indexes();

    String dummy_table_clones();

    String dummy_context_menu();

    String new_element_event();

    String new_element_parameter();

    String new_element_datasource();

    String new_element_schema();

    String new_element_table();

    String new_element_dynamic_table();

    String new_element_view_v();

    String new_element_view_l();

    String new_element_column();

    String new_element_group();

    String new_element_table_clone();

    String new_element_context_menu();

    String primary_key();

    String deleted();

    String copy();

    String context_menu_item_image();

    String page_noJavaScript();

    String help_js_api();

    String btn_hint_title();

    String sort_title();

    String sort_default();

    String sort_asc();

    String sort_desc();

    String sort_default_tooltip();

    String sort_asc_tooltip();

    String sort_desc_tooltip();

    String sort_display_title();

    String sort_display_default();

    String sort_display_asc();

    String sort_display_desc();

    String design_panel_pallete();

    String design_panel_components();

    String design_panel_events();

    String templ_save();

    String templ_enter_name();

    String templ_name_message();

    String templ_save_cancel();

    String templ_events();

    String templ_delete();

    String templ_delete_req();

    String templ_delete_cancel();

    String templ_success_delete();

    String templ_success_save_with_name();

    class Util {
        public static EditorMessage MESSAGE = GWT.create(EditorMessage.class);
    }
}
