package com.sencha.gxt.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.theme.base.client.field.TextFieldDefaultAppearance.TextFieldStyle;

public class TriggerFieldDefaultAppearance extends ValueBaseFieldDefaultAppearance implements TriggerFieldAppearance {

  public interface TriggerFieldResources extends ValueBaseFieldResources, ClientBundle {

    @Source({"ValueBaseField.gss", "TextField.gss", "TriggerField.gss"})
    TriggerFieldStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, preventInlining = true)
    ImageResource invalidLine();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource textBackground();

    ImageResource triggerArrow();

    ImageResource triggerArrowClick();

    ImageResource triggerArrowFocus();

    ImageResource triggerArrowOver();
  }

  public interface TriggerFieldStyle extends TextFieldStyle {

    String click();

    String noedit();

    String over();

    String trigger();

  }

  private final TriggerFieldResources resources;
  private final TriggerFieldStyle style;

  public TriggerFieldDefaultAppearance() {
      this(GWT.create(TriggerFieldResources.class));
  }

  public TriggerFieldDefaultAppearance(TriggerFieldResources resources) {
    super(resources);
    this.resources = resources;
    this.style = resources.css();
  }

  @Override
  public XElement getInputElement(Element parent) {
    return parent.<XElement> cast().selectNode("input");
  }

  protected TriggerFieldResources getResources() {
    return resources;
  }

  public TriggerFieldStyle getStyle() {
    return style;
  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    parent.<XElement> cast().setClassName(getResources().css().focus(), focus);
    getInputElement(parent).setClassName(getResources().css().focus(), focus);
  }

  @Override
  public void onTriggerClick(XElement parent, boolean click) {
    parent.setClassName(getResources().css().click(), click);
  }

  @Override
  public void onTriggerOver(XElement parent, boolean over) {
    parent.setClassName(getResources().css().over(), over);
  }

  @Override
  public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
    int width = options.getWidth();
    boolean hideTrigger = options.isHideTrigger();

    if (width == -1) {
      width = 150;
    }

    String wrapStyles = "width:" + width + "px;";

    // 6px margin, 2px border
    width -= 8;

    if (!hideTrigger) {
      width -= getResources().triggerArrow().getWidth();
    }
    width = Math.max(0, width);
    SafeStyles inputStyles = SafeStylesUtils.fromTrustedString("width:" + width + "px;");

    sb.appendHtmlConstant("<div style='" + wrapStyles + "' class='" + getStyle().wrap() + "'>");

    if (!hideTrigger) {
      sb.appendHtmlConstant("<table cellpadding=0 cellspacing=0><tr><td>");
      renderInput(sb, value, inputStyles, options);
      sb.appendHtmlConstant("</td>");
      sb.appendHtmlConstant("<td><div class='" + getStyle().trigger() + "' /></td>");
      sb.appendHtmlConstant("</table>");
    } else {
      renderInput(sb, value, inputStyles, options);
    }

    sb.appendHtmlConstant("</div>");
  }

  /**
   * Helper method to render the input in the trigger field.
   */
  protected void renderInput(SafeHtmlBuilder shb, String value, SafeStyles inputStyles,
      FieldAppearanceOptions options) {
    // Deliberately using a StringBuilder, not SafeHtmlBuilder, as each append isn't adding
    // complete elements, but building up this single element, one attribute at a time.
    StringBuilder sb = new StringBuilder();
    sb.append("<input ");

    if (options.isDisabled()) {
      sb.append("disabled=true ");
    }

    if (options.getName() != null) {
      // if set, escape the name property so it is a valid attribute
      sb.append("name='").append(SafeHtmlUtils.htmlEscape(options.getName())).append("' ");
    }

    if (options.isReadonly() || !options.isEditable()) {
      sb.append("readonly ");
    }

    if (inputStyles != null) {
      sb.append("style='").append(inputStyles.asString()).append("' ");
    }


    sb.append("class='").append(getStyle().field()).append(" ").append(getStyle().text());

    String placeholder = options.getEmptyText() != null ? " placeholder='" + SafeHtmlUtils.htmlEscape(options.getEmptyText()) + "' " : "";

    if (value.equals("") && options.getEmptyText() != null) {
      sb.append(" ").append(getStyle().empty());
      if (GXT.isIE8() || GXT.isIE9()) {
        value = options.getEmptyText();
      }
    }

    if (!options.isEditable()) {
      sb.append(" ").append(getStyle().noedit());
    }
    sb.append("' ");
    sb.append(placeholder);

    //escaping the value string so it is a valid attribute
    sb.append("type='text' value='").append(SafeHtmlUtils.htmlEscape(value)).append("'/>");

    // finally, converting stringbuilder into a SafeHtml instance and appending it
    // to the buidler we were given
    shb.append(SafeHtmlUtils.fromTrustedString(sb.toString()));
  }

  @Override
  public void setEditable(XElement parent, boolean editable) {
    getInputElement(parent).setClassName(getStyle().noedit(), !editable);
  }

  @Override
  public boolean triggerIsOrHasChild(XElement parent, Element target) {
    return parent.isOrHasChild(target) && target.<XElement> cast().is("." + getStyle().trigger());
  }

  @Override
  public void onResize(XElement parent, int width, int height, boolean hideTrigger) {
    if (width != -1) {
      Element div = parent.getFirstChildElement();
      div.getStyle().setPropertyPx("width", width);

      // 6px margin, 2px border
      width -= 8;

      if (!hideTrigger) {
        width -= getResources().triggerArrow().getWidth();
      }
      width = Math.max(0, width);
      parent.selectNode("input").getStyle().setPropertyPx("width", width);
    }
  }

}