package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.ExportStaticMethod;
import org.timepedia.exporter.client.Exportable;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;

/**
 * Класс содержит статические методы получения builder-ов компонентов приложения текущего окна.
 * Коды компонентов можно узнать(если они заданы) в редакторе форм - поле Code компонента.
 */
@Export("Components")
@ExportPackage("Whirl")
public class Builders implements Exportable {

    /**
     * Возвращает компонент по его коду
     *
     * @param code - String, код компонента
     * @return ComponentBuilder
     */
    @ExportStaticMethod
    public static ComponentBuilder findByCode(String code) {
        return BuilderManager.findBuilder(code, true);
    }

    /**
     * Возвращает компонент по коду, находящийся в указанном контейнере
     *
     * @param builder - ComponentBuilder, контейнер
     * @param code    - String, код компонента
     * @return ComponentBuilder
     */
    @ExportStaticMethod
    public static ComponentBuilder findByCodeInParent(
            ComponentBuilder builder, String code) {
        if (builder instanceof Containable) {
            return BuilderManager
                    .findBuilder((Containable) builder, code, true);
        } else {
            return null;
        }
    }

    /**
     * Получить все компоненты, доступные в текущем окне
     *
     * @return ComponentBuilder[], массив компонентов
     */
    @ExportStaticMethod
    public static ComponentBuilder[] getAll() {
        return BuilderManager.getAllBuilders().toArray(new ComponentBuilder[0]);
    }

}