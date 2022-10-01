package org.whirlplatform.component.client.base;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@JsType(name = "FramedLogin", namespace = "Whirl")
public class FramedLoginPanelBuilder extends LoginPanelBuilder {

	private HtmlLayoutContainer htmlLayout;
	private FramedPanel loginPanel;

	@JsConstructor
	public FramedLoginPanelBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public FramedLoginPanelBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.FramedLoginPanelType;
	}

	@Override
	protected Component init(Map<String, DataValue> builderProperties) {
		Component comp = super.init(builderProperties);
		htmlLayout = new HtmlLayoutContainer(initHtml());
		loginPanel = new FramedPanel();
		loginPanel.setHeading(AppMessage.Util.MESSAGE.login_header());
		loginPanel.setButtonAlign(BoxLayoutPack.CENTER);
		loginPanel.setWidth(400);
		loginPanel.setWidget(comp);
		htmlLayout.add(loginPanel, new HtmlData("div[id=whirl-login-id]"));
		return htmlLayout;
	}

	private SafeHtml initHtml() {
		StringBuilder html = new StringBuilder();

		html.append(
				"<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" height=\"100%\" id=\"login-page\">");
		html.append("<tbody>");
		html.append("<tr class=\"xs-header\">");
		Element elHeader = DOM.getElementById("login-header-template");
		String loginHeader;
		if (elHeader != null) {
			loginHeader = elHeader.getInnerHTML();
		} else {
			loginHeader = "<div id=\"login-header\" style=\"font-weight: bold; font-size: 16px;\">"
					+ AppMessage.Util.MESSAGE.login_auth() + "</div>";
		}
		html.append("<td align=\"center\">" + loginHeader + "</td><td>");
		html.append("</td></tr>");
		html.append("<tr class=\"xs-header-text\">");
		html.append("<td id=\"login-page-title\" align=\"center\">");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td valign=\"top\" align=\"center\">");
        html.append("<div id=\"whirl-login-id\">");
		html.append("</div>");
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr class=\"xs-footer\">");
		html.append("<td align=\"center\">");
		Element elFooter = DOM.getElementById("login-footer-template");
		String loginFooter;
		if (elFooter != null) {
			loginFooter = elFooter.getInnerHTML() + "&copy;";

		} else {
			loginFooter = "";
		}
		html.append(loginFooter);
		html.append(DateTimeFormat.getFormat("yyyy").format(new Date()));
		html.append("</td>");
		html.append("</tr>");
		html.append("</tbody>");
		html.append("</table>");

		return SafeHtmlUtils.fromTrustedString(html.toString());
	}

	@Override
	public Locator getLocatorByElement(Element element) {
		return super.getLocatorByElement(element);
	}

	/**
	 * Returns component's code.
	 *
	 * @return component's code
	 */
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * Checks if component is in hidden state.
	 *
	 * @return true if component is hidden
	 */
	@Override
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Sets component's hidden state.
	 *
	 * @param hidden true - to hide component, false - to show component
	 */
	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Focuses component.
	 */
	@Override
	public void focus() {
		super.focus();
	}

	/**
	 * Checks if component is enabled.
	 *
	 * @return true if component is enabled
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Sets component's enabled state.
	 *
	 * @param enabled true - to enable component, false - to disable component
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}


	}
