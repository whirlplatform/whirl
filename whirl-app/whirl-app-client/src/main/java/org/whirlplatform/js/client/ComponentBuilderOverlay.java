package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;


/**
 * Представляет базовый класс для всех компонентов Whirl.
 */
@Export("Component")
@ExportPackage("Whirl")
public abstract class ComponentBuilderOverlay implements
        ExportOverlay<ComponentBuilder> {

}
