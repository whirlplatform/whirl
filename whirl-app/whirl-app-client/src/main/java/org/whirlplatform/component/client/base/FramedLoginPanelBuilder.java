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
import jsinterop.annotations.JsIgnore;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class FramedLoginPanelBuilder extends LoginPanelBuilder {

	private HtmlLayoutContainer htmlLayout;
	private FramedPanel loginPanel;

	public FramedLoginPanelBuilder( Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

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

	@JsIgnore
	@Override
	public Locator getLocatorByElement(Element element) {
		return super.getLocatorByElement(element);
	}

	/**
	 * Возвращает код компонента.
	 *
	 * @return код компонента
	 */
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * Проверяет, находится ли компонент в скрытом состоянии.
	 *
	 * @return true если компонент скрыт
	 */
	@Override
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Устанавливает скрытое состояние компонента.
	 *
	 * @param hidden true - для скрытия компонента, false - для отображения компонента
	 */
	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Фокусирует компонент.
	 */
	@Override
	public void focus() {
		super.focus();
	}

	/**
	 * Проверяет, включен ли компонент.
	 *
	 * @return true если компонент включен
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Устанавливает включенное состояние компонента.
	 *
	 * @param enabled true - для включения компонента, false - для отключения компонента
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
	}


	}
