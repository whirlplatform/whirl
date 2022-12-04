package org.whirlplatform.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.presenter.ApplicationPresenter;
import org.whirlplatform.editor.client.presenter.ApplicationPresenter.IApplicationView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class ApplicationView extends ContentPanel implements IApplicationView {

    private VerticalLayoutContainer container;

    private FieldLabel labelCode;
    private TextField fieldCode;
    //    private FieldLabel labelUrl;
    private CheckBox guest;
    private FieldLabel labelTitle;
    private PropertyValueField fieldTitle;

    private FieldLabel javaScriptLabel;
    private FileFormPanel javaScriptFiles;
    private FieldLabel cssLabel;
    private FileFormPanel cssFiles;
    private FieldLabel javaLabel;
    private FileFormPanel javaFiles;
    private FieldLabel imageLabel;
    private FileFormPanel imageFiles;
    private FieldLabel staticLabel;
    private FileFormPanel staticFiles;

    private FieldLabel labelHtmlHeader;
    private TextArea fieldHtmlHeader;

    private Command completeCommand;

    private ApplicationPresenter presenter;

    public ApplicationView() {
        super();
    }

    private void initUI() {
        setHeaderVisible(true);
        container = new VerticalLayoutContainer();
        container.setAdjustForScroll(true);
        container.setScrollMode(ScrollMode.AUTO);
        setWidget(container);
        initFields();
    }

    public void initFields() {
        fieldCode = new TextField();
        fieldCode.setEnabled(false);
        labelCode = new FieldLabel(fieldCode, EditorMessage.Util.MESSAGE.application_code());
        container.add(labelCode,
                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
        guest = new CheckBox();
//        labelUrl = new FieldLabel(guest, EditorMessage.Util.MESSAGE.application_guest());
//        container.add(labelUrl,
//                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));

        fieldTitle = new PropertyValueField();
        labelTitle = new FieldLabel(fieldTitle, EditorMessage.Util.MESSAGE.application_title());
        container.add(labelTitle,
                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));

        initJavaScriptFields();
        initCssFields();
        initJavaFields();
        initImgFields();
        initStaticFields();

        fieldHtmlHeader = new TextArea();
        labelHtmlHeader = new FieldLabel(fieldHtmlHeader,
                EditorMessage.Util.MESSAGE.application_html_header());
        container.add(labelHtmlHeader,
                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
    }

    private void checkSaved() {
        if (javaScriptFiles.isComplete() && cssFiles.isComplete() && javaFiles.isComplete()
                && imageFiles.isComplete()) {
            completeCommand.execute();
        }
    }

    private void initJavaScriptFields() {
        javaScriptFiles = new FileFormPanel("javascript");
        javaScriptFiles.setCompleteComand(new Command() {
            @Override
            public void execute() {
                checkSaved();
            }
        });
        javaScriptLabel = new FieldLabel(new AdapterField<String>(javaScriptFiles) {
            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void setValue(String value) {
            }


        }, EditorMessage.Util.MESSAGE.application_javascript());
//        container.add(javaScriptLabel,
//                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
    }

    private void initCssFields() {
        cssFiles = new FileFormPanel("css");
        cssFiles.setCompleteComand(new Command() {
            @Override
            public void execute() {
                checkSaved();
            }
        });
        cssLabel = new FieldLabel(new AdapterField<String>(cssFiles) {
            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void setValue(String value) {
            }


        }, EditorMessage.Util.MESSAGE.application_css());
//        container.add(cssLabel,
//                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
    }

    private void initJavaFields() {
        javaFiles = new FileFormPanel("java");
        javaFiles.setCompleteComand(new Command() {
            @Override
            public void execute() {
                checkSaved();
            }
        });
        javaLabel = new FieldLabel(new AdapterField<String>(javaFiles) {
            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void setValue(String value) {
            }


        }, EditorMessage.Util.MESSAGE.application_java());
//        container.add(javaLabel,
//                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
    }

    private void initImgFields() {
        imageFiles = new FileFormPanel("image");
        imageFiles.setCompleteComand(new Command() {
            @Override
            public void execute() {
                checkSaved();
            }
        });
        imageLabel = new FieldLabel(new AdapterField<String>(imageFiles) {
            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void setValue(String value) {
            }


        }, EditorMessage.Util.MESSAGE.application_images());
//        container.add(imageLabel,
//                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
    }

    private void initStaticFields() {
        staticFiles = new FileFormPanel("static", true);
        staticFiles.setCompleteComand(new Command() {
            @Override
            public void execute() {
                checkSaved();
            }
        });
        staticLabel = new FieldLabel(new AdapterField<String>(staticFiles) {
            @Override
            public String getValue() {
                return null;
            }

            @Override
            public void setValue(String value) {
            }


        }, EditorMessage.Util.MESSAGE.application_static());
//        container.add(staticLabel,
//                new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
    }

    @Override
    public String getApplicationCode() {
        return fieldCode.getValue();
    }

    @Override
    public void setApplicationCode(String code) {
        fieldCode.setValue(code);
    }

    @Override
    public Boolean getGuest() {
        return guest.getValue();
    }

    @Override
    public void setGuest(Boolean guest) {
        this.guest.setValue(guest);
    }

    @Override
    public PropertyValue getApplicationTitle() {
        return fieldTitle.getPropertyValue();
    }

    @Override
    public void setApplicationTitle(PropertyValue title) {
        fieldTitle.setPropertyValue(title);
    }

    @Override
    public void setHeaderText(String text) {
        setHeading(text);
    }

    @Override
    public void addJavaScriptFile(FileElement file) {
        javaScriptFiles.addFileUpload(file);
    }

    @Override
    public Collection<FileElement> getJavaScriptFiles() {
        return javaScriptFiles.getFiles();
    }

    @Override
    public void addCssFile(FileElement file) {
        cssFiles.addFileUpload(file);
    }

    @Override
    public Collection<FileElement> getCssFiles() {
        return cssFiles.getFiles();
    }

    @Override
    public void addJavaFile(FileElement file) {
        javaFiles.addFileUpload(file);
    }

    @Override
    public Collection<FileElement> getJavaFiles() {
        return javaFiles.getFiles();
    }

    @Override
    public void addImageFile(FileElement file) {
        imageFiles.addFileUpload(file);
    }

    @Override
    public Collection<FileElement> getImageFiles() {
        return imageFiles.getFiles();
    }

    @Override
    public FileElement getStaticFile() {
        Iterator<FileElement> iter = staticFiles.getFiles().iterator();
        if (iter.hasNext()) {
            return iter.next();
        }
        return null;
    }

    @Override
    public void setStaticFile(FileElement file) {
        staticFiles.addFileUpload(file);
        if (!staticFiles.multiply && staticFiles.rows.size() >= 1) {
            staticFiles.add.disable();
        }
    }

    @Override
    public void setHtmlHeader(String headerHtml) {
        fieldHtmlHeader.setValue(headerHtml);
    }

    @Override
    public String getHeaderHtml() {
        return fieldHtmlHeader.getValue();
    }

    @Override
    public void setCompleteCommand(Command completeCommand) {
        this.completeCommand = completeCommand;
    }

    @Override
    public void save() {
        javaScriptFiles.save();
        cssFiles.save();
        javaFiles.save();
        imageFiles.save();
        staticFiles.save();
    }

    @Override
    public ApplicationPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(ApplicationPresenter presenter) {
        this.presenter = presenter;
        initUI();
    }

    public void clearFilesFields() {
        javaScriptFiles.clearFiles();
        cssFiles.clearFiles();
        javaFiles.clearFiles();
        imageFiles.clearFiles();
        staticFiles.clearFiles();
    }

    @Override
    public void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale) {
        fieldTitle.setLocales(defaultLocale, locales);
    }

    @Override
    public void setEnableAll(boolean enable) {
        labelCode.setEnabled(false);
//        labelUrl.setEnabled(enable);
        labelTitle.setEnabled(enable);
        javaScriptLabel.setEnabled(enable);
        javaLabel.setEnabled(enable);
        cssLabel.setEnabled(enable);
        imageLabel.setEnabled(enable);
        staticLabel.setEnabled(enable);
    }

    class FileRow extends HorizontalLayoutContainer {

        FileElement fileElement;
        TextField id;
        TextField name;
        FileUploadField file;
        // Image status;
        TextButton download;
        TextButton remove;

        FileRow(FileElement fileElement) {
            this.fileElement = fileElement;
            initUI();
        }

        void initUI() {
            id = new TextField();
            id.setVisible(false);
            id.setWidth(0);
            id.setHeight(0);
            id.setName("id" + fileElement.getId());
            add(id);

            name = new TextField();
            name.setName("name" + fileElement.getId());
            name.setValue(fileElement.getName());
            add(name, new HorizontalLayoutData(0.4, 1, new Margins(0, 5, 0, 0)));

            file = new FileUploadField() {
                @Override
                public String getValue() {
                    return getElement().child("input[type=\"text\"]").getPropertyString("value");
                }

                @Override
                public void setValue(String value) {
                    getElement().child("input[type=\"text\"]").setPropertyString("value", value);
                }


            };
            file.setName("file" + fileElement.getId());
            add(file, new HorizontalLayoutData(0.4, 1, new Margins(0, 5, 0, 0)));
            file.setValue(fileElement.getFileName());

            download = new TextButton(EditorMessage.Util.MESSAGE.application_download());
            download.addSelectHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    presenter.downloadFile(fileElement);
                }
            });
            add(download, new HorizontalLayoutData(-1, 1, new Margins(0, 5, 0, 0)));

            remove = new TextButton();
            remove.setIcon(EditorBundle.INSTANCE.cross());
            add(remove, new HorizontalLayoutData(-1, 1, new Margins(0, 5, 0, 0)));

            if ("".equals(fileElement.getFileName())) {
                setCanLoad(true);
            }
        }

        HandlerRegistration addFileRemoveHandler(SelectHandler removeHandler) {
            return remove.addSelectHandler(removeHandler);
        }

        FileElement getFileElement() {
            fileElement.setName(name.getValue());
            fileElement.setFileName(file.getValue());
            return fileElement;
        }

        void setCanLoad(boolean loaded) {
            download.setEnabled(loaded);
        }

    }

    class FileFormPanel extends FormPanel {

        boolean multiply = true;
        VerticalLayoutContainer mainContainer;
        TextButton add;
        VerticalLayoutContainer formContainer;
        String type;

        Set<FileRow> rows = new HashSet<FileRow>();

        boolean complete = false;
        Command completeCommand;

        FileFormPanel(String type) {
            this(type, true);
        }

        FileFormPanel(String type, boolean multiply) {
            super();
            this.type = type;
            setEncoding(Encoding.MULTIPART);
            setMethod(Method.POST);
            setAction(GWT.getHostPageBaseURL() + "resource?action=upload&type=" + type);
            addSubmitCompleteHandler(new SubmitCompleteHandler() {
                @Override
                public void onSubmitComplete(SubmitCompleteEvent event) {
                    complete = true;
                    if (completeCommand != null) {
                        completeCommand.execute();
                    }
                }
            });
            initUI();
        }

        void initUI() {
            mainContainer = new VerticalLayoutContainer();
            setWidget(mainContainer);

            add = new TextButton(EditorMessage.Util.MESSAGE.application_add());
            add.addSelectHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    addFileUpload(null);
                    if (!multiply && rows.size() >= 1) {
                        add.disable();
                    }
                }
            });
            mainContainer.add(add, new VerticalLayoutData(-1, -1));

            formContainer = new VerticalLayoutContainer();
            formContainer.setAdjustForScroll(true);
            formContainer.setScrollMode(ScrollMode.AUTO);
            formContainer.setHeight(100);
            mainContainer.add(formContainer, new VerticalLayoutData(1, -1));
        }

        void clearFiles() {
            complete = false;
            formContainer.clear();
            rows.clear();
            formContainer.forceLayout();
        }

        void addFileUpload(FileElement element) {
            complete = false;
            FileElement f = element;
            boolean status = true;
            if (f == null) {
                // TODO создание элементов перенести в ElementEventHandler
                f = new FileElement();
                f.setId(RandomUUID.uuid());
                f.setName(EditorMessage.Util.MESSAGE.application_file_name());
                f.setContentType(type);
                status = false;
            }
            final FileRow r = new FileRow(f);
            r.addFileRemoveHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    formContainer.remove(r);
                    remove(r);
                    if (!multiply && rows.size() == 0) {
                        add.enable();
                    }
                }

            });
            r.setCanLoad(status);
            formContainer.add(r, new VerticalLayoutData(1, 30, new Margins(5, 0, 5, 0)));
            rows.add(r);
            formContainer.forceLayout();
        }

        Collection<FileElement> getFiles() {
            Set<FileElement> result = new HashSet<FileElement>();
            for (FileRow r : rows) {
                result.add(r.getFileElement());
            }
            return result;
        }

        void save() {
            submit();
        }

        boolean isComplete() {
            return complete;
        }

        void setCompleteComand(Command completeCommand) {
            this.completeCommand = completeCommand;
        }

        void remove(FileRow row) {
            rows.remove(row);
        }
    }
}
