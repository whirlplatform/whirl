package org.whirlplatform.app.client;

import java.util.Collections;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.ContainerHelper;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.component.ComponentModel;

/**
 * Постоитель приложения
 */
public class AppBuilder {

    // Данные о текущем приложении
    private static ApplicationData currApp;

    /**
     * Создание приложения по конфиг файлу
     */
    public static void buildApplication(ComponentModel componentTree) {
        Containable parent = AppEntryPoint.getRootContainer();

        // TODO Selenium
        // SeleniumManager.init();

        // зарегистрировать доступные методы
        JSMethods.export();

        buildComponentTree(componentTree, parent);
        BuilderManager.setRoot(parent);

    }

    public static void buildComponentTree(ComponentModel componentTree, Containable parent) {
        ContainerHelper.buildComponent(Collections.singletonList(componentTree), parent);
        parent.forceLayout();
    }

    public static void setCurrentApp(ApplicationData data) {
        currApp = data;
        BuilderManager.setApplicationData(data);
    }

    public static ApplicationData getCurrApp() {
        return currApp;
    }

}