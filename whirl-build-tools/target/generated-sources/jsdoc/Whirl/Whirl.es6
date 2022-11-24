/**
 * Application level helper methods.
 */
class Application {

    /**
     * Changes opened application in the current window.
     * 
     * @param {string} appCode application code
     */
    static setCurrentApplication(appCode) {}

    /**
     * Opens application in new browser window with the new session of current user.
     * 
     * @param {string} appCode application code
     */
    static openApplication(appCode) {}

    /**
     * Logout.
     */
    static logout() {}
}

/**
 * Панель вкладок
 */
class TabPane {

    /**
     * Устанавливает отображение границы панели
     * (по умолчанию true, pre-render).
     * 
     * @param {boolean} show - boolean, true для отображения границы
     */
    setBorder(show) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    /**
     * Получает активный компонент.
     * 
     * @return {ComponentBuilder} ComponentBuilder
     */
    getActive() {}

    /**
     * Устанавливает активный компонент.
     * 
     * @param {ComponentBuilder} child - ComponentBuilder
     */
    setActive(child) {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Поле пароля.
 */
class PasswordField {

    
    constructor(builderProperties) {}

    /**
     * Получает значение текстового поля
     * 
     * @return {string} String
     */
    getValue() {}

    /**
     * Устанавливает значения текстового поля
     * 
     * @param {string} value String
     */
    setValue(value) {}

    /**
     * Возвращает требуемую минимальную длину поля.
     * 
     * @return {number} требуемая минимальная длина поля
     */
    getMinLength() {}

    /**
     * Возвращает максимальную длину поля.
     * 
     * @return {number} максимальная длина поля
     */
    getMaxLength() {}
}

/**
 * Текстовая строка вывода, надпись
 */
class Label {

    
    constructor(builderProperties) {}

    /**
     * Установка текста
     * 
     * @param {string} value - String, текст
     */
    setHtml(value) {}

    /**
     * Получение текста
     * 
     * @return {string} String
     */
    getHtml() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}
}


class Context {
}

/**
 * Контейнер расположения компонентов в виде горизонтальных рядов.
 */
class HBoxContainer {

    
    constructor(builderProperties) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Вкладка
 */
class TabItem {

    
    constructor(builderProperties) {}

    /**
     * Делает данную вкладку активной в панели.
     */
    activate() {}

    /**
     * Устанавливает для элемента разрешение быть закрываемым
     * (по умолчанию false).
     * 
     * @param {boolean} closable - boolean, true для закрываемого
     */
    setClosable(closable) {}

    /**
     * Возвращает состояние закрытия элемента.
     * 
     * @return {boolean} true, если закрыт
     */
    isClosable() {}

    /**
     * Закрывает элемент.
     */
    close() {}

    /**
     * Устанавливает заголовок.
     * 
     * @param {string} title - String, заголовок
     */
    setTitle(title) {}

    /**
     * Возвращает заголовок.
     * 
     * @return {string} String - содержимое заголовка в формате html
     */
    getTitle() {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    /**
     * Обновляет элемент.
     */
    refresh() {}

    
    getChildren() {}

    /**
     * Проверяет активность компонента.
     * 
     * @return {boolean} true, компонент активен
     */
    isActive() {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Древовидный список
 */
class TreeComboBox {

    
    constructor(builderProperties) {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для не валидного поля
     * @return {boolean} true если поле доступно
     */
    isValid(invalidate) {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Проверяет доступность для редактирования.
     * 
     * @return {boolean} true, если доступен для редактирования
     */
    isEditable() {}

    /**
     * Устанавливает доступность для редактирования.
     * 
     * @param {boolean} editable true, доступ для редактирования
     */
    setEditable(editable) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Возвращает текст объекта.
     * 
     * @return {string} новый текст объекта
     */
    getText() {}
}

/**
 * Панель задач
 */
class TaskBar {

    
    constructor(builderProperties) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Компонент окна
 */
class Window {

    
    constructor(builderProperties) {}

    /**
     * Устанавливает заголовок окна.
     * 
     * @param {string} title заголовок
     */
    setTitle(title) {}

    /**
     * Является ли окно модальным.
     * 
     * @return {boolean} true, если модальное поведение включено
     */
    isModal() {}

    /**
     * Устанавливает модальность окна и скрывает все за ним при отображении
     * (по умолчанию false).
     * 
     * @param {boolean} modal - boolean, true сделать окно модальным, false, чтобы отобразить его
     * без ограничения доступа к другим элементам пользовательского интерфейса
     */
    setModal(modal) {}

    /**
     * Проверяет, можно ли изменять размеры окна.
     * 
     * @return {boolean} true, если изменение размеров окна включено
     */
    isResizable() {}

    /**
     * Устанавливает, можно ли изменять размеры окна по каждому краю и углу
     * (по умолчанию true).
     * 
     * @param {boolean} resizable - boolean, true для включения изменения размеров
     */
    setResizable(resizable) {}

    /**
     * Проверяет, можно ли свернуть окно.
     * 
     * @return {boolean} true, если минимизация окна включена
     */
    isMinimizable() {}

    /**
     * Устанавливает, можно ли свернуть окно.
     * 
     * @param {boolean} minimizable - boolean, true, чтобы включить минимизацию
     */
    setMinimizable(minimizable) {}

    /**
     * Проверяет, является ли окно максимизируемым.
     * 
     * @return {boolean} true, если максимизация окна включена.
     */
    isMaximizable() {}

    /**
     * Устанавливает, может ли окно быть максимизировано
     * (по умолчанию false).
     * 
     * @param {boolean} maximizable - boolean, true для разрешения пользователю максимизировать окно,
     * false для скрытия кнопки и запрета максимизации окна
     */
    setMaximizable(maximizable) {}

    /**
     * Проверяет можно ли закрыть окно.
     * 
     * @return {boolean} true, если окно является закрываемым
     */
    isClosable() {}

    /**
     * Устанавливает, можно ли закрыть окно.
     * 
     * @param {boolean} closable - boolean true, чтобы включить закрытие
     */
    setClosable(closable) {}

    /**
     * Устанавливает позицию окна.
     * 
     * @param {number} left положение слева
     * @param {number} top  положение сверху
     */
    setPosition(left, top) {}

    /**
     * Устанавливает положение компонент на странице XY.
     * 
     * @param {number} x - координата x
     * @param {number} y - координата y
     */
    setPagePosition(x, y) {}

    /**
     * Скрывает окно.
     */
    hide() {}

    /**
     * Показывает окно.
     */
    show() {}

    /**
     * Устанавливает окно в пределах его текущего контейнера
     * и автоматически заменяет кнопку инструмента "максимизировать"
     * на кнопку инструмента "восстановить".
     */
    maximize() {}

    /**
     * Минимизация окна.
     */
    minimize() {}

    /**
     * Выравнивание окна по центру.
     * Должен вызываться после отображения окна.
     */
    center() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента,
     * false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Чек-бокс поле.
 */
class CheckBox {

    
    constructor(builderProperties) {}

    /**
     * Получает лейбл поля.
     * 
     * @return {string} текст лейбла
     */
    getBoxLabel() {}

    /**
     * Устанавливает лейбл для поля.
     * 
     * @param {string} label - String текст
     */
    setBoxLabel(label) {}

    /**
     * Получить значение поля.
     * 
     * @return {Boolean} значение поля
     */
    getValue() {}

    /**
     * Устанавливает значение поля.
     * 
     * @param {Boolean} value - boolean значение поля
     */
    setValue(value) {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус не валидности для поля.
     */
    clearInvalid() {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}
}

/**
 * Числовое поле ввода
 */
class NumberField {

    
    constructor(builderProperties) {}

    /**
     * Установка максимальной длины числового поля
     * 
     * @param {number} length - int
     */
    setMaxLength(length) {}

    /**
     * Установка минимальной длины числового поля
     * 
     * @param {number} length - int
     */
    setMinLength(length) {}

    /**
     * Установка формата числового поля
     * 
     * @param {string} format - String
     */
    setFormat(format) {}

    /**
     * Получение значение числового поля
     * 
     * @return {number} Double
     */
    getValue() {}

    /**
     * Установка значения числового поля
     * 
     * @param {number} value Double
     */
    setValue(value) {}

    /**
     * Получает текст объекта.
     * 
     * @return {string} новый текст объекта
     */
    getText() {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает не валидный статус для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Удаляет не валидный статус для поля.
     */
    clearInvalid() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для не валидного поля
     * @return {boolean} true если поле доступно
     */
    isValid(invalidate) {}

    /**
     * Проверяет обязательность для заполнения.
     * 
     * @return {boolean} true, if is required
     */
    isRequired() {}

    /**
     * Устанавливает обязательное требование для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}
}

/**
 * Фрейм
 */
class Frame {

    
    constructor(builderProperties) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Компонент - Бордер-контейнер
 */
class BorderContainer {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Контейнер располагающей компоненты в виде вертикальных колонок.
 */
class VBoxContainer {

    
    constructor(builderProperties) {}

    
    addChild(child) {}
}

/**
 * Контент-панель
 */
class ContentPanel {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Каптча
 */
class Captcha {

    
    constructor(builderProperties) {}

    
    static get CAPTCHA_KEY() {}

    
    static get KEY_CAPTCHA_CORRECT() {}

    /**
     * Пометить компонент как прошедший проверку
     */
    clearInvalid() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Button.
 */
class Button {

    
    constructor(builderProperties) {}

    /**
     * Returns html button title.
     * 
     * @return {string} title
     */
    getHtml() {}

    /**
     * Sets html button title.
     * 
     * @param {string} html title
     */
    setHtml(html) {}
}

/**
 * Набор полей
 */
class FieldSet {

    
    constructor(builderProperties) {}

    /**
     * Устанавливает увеличения или свертывания поля.
     * 
     * @param {boolean} expand - boolean, true для расширения набора полей
     */
    setExpanded(expand) {}

    /**
     * Возвращает установленное значение увеличения или свертывания поля.
     * 
     * @return {boolean} - boolean, true для расширения набора полей
     */
    isExpanded() {}

    
    addChild(child) {}

    
    removeChild(child) {}

    
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * HTML компонент
 */
class Html {

    
    constructor(builderProperties) {}

    /**
     * Получение контента из HtmlBuilder
     * 
     * @return {string} String
     */
    getHtml() {}

    /**
     * Установка необходимого контента в HtmlBuilder
     * 
     * @param {string} content - String, контент
     */
    setHtml(content) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Радиокнопка
 */
class Radio {

    
    constructor(builderProperties) {}

    /**
     * Установка подписи радиокнопки
     * 
     * @param {string} label - String
     */
    setBoxLabel(label) {}

    /**
     * Получение подписи радиокнопки
     * 
     * @return {string} String
     */
    getBoxLabel() {}

    /**
     * Установка названия радиогруппы
     * 
     * @param {string} groupName - String
     */
    setGroupName(groupName) {}

    /**
     * Получение названия радиогруппы
     * 
     * @return {string} String
     */
    getGroupName() {}

    /**
     * Получение значения радиокнопки
     * 
     * @return {Boolean} boolean
     */
    getValue() {}

    /**
     * Установка значения радиокнопки
     * 
     * @param {Boolean} value - boolean
     */
    setValue(value) {}

    /**
     * Проверка на валидность CheckBox
     * 
     * @param {boolean} invalidate - boolean
     * @return {boolean} boolean
     */
    isValid(invalidate) {}

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус не валидности для поля.
     */
    clearInvalid() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Очищает значение поля.
     */
    clear() {}
}

/**
 * Элемент меню дерева
 */
class TreeMenuItem {

    
    constructor(builderProperties) {}

    /**
     * 
     * @return {string}
     */
    getImage() {}

    /**
     * Устанавливает изображение.
     * 
     * @param {string} url - String
     */
    setImage(url) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Форма
 */
class Form {

    
    constructor(builderProperties) {}

    /**
     * Загружает.
     */
    load() {}

    /**
     * Обновляет.
     */
    refresh() {}

    
    getChild(row, column) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Устанавливает высоту строки.
     * 
     * @param {number} row    - int, строка
     * @param {number} height - double, высота
     */
    setRowHeight(row, height) {}

    /**
     * Устанавливает ширину столбца.
     * 
     * @param {number} column - int, столбец
     * @param {number} width  - double, ширина
     */
    setColumnWidth(column, width) {}

    /**
     * Задает объединение по горизонтали указанной ячейки.
     * 
     * @param {number} row     - int, номер строки
     * @param {number} column  - int, номер столбца
     * @param {number} rowSpan - int, количество объединяемых ячеек
     */
    setRowSpan(row, column, rowSpan) {}

    /**
     * Устанавливает интервал столбцов.
     * 
     * @param {number} row     - int, строка
     * @param {number} column  - int, столбец
     * @param {number} colSpan - int, интервал
     */
    setColSpan(row, column, colSpan) {}

    /**
     * Устанавливает объединение области.
     * 
     * @param {number} row     - int, строка
     * @param {number} column  - int, столбец
     * @param {number} rowSpan - int, интервал строки
     * @param {number} colSpan - int, интервал  ячейки
     */
    setSpan(row, column, rowSpan, colSpan) {}

    /**
     * Устанавливает цвет верхней границы ячейки.
     * 
     * @param {number} row       - int, строка
     * @param {number} column    - int, столбец
     * @param {number} thickness - int, толщина
     * @param {string} color     - String, цвет
     */
    setCellBorderTop(row, column, thickness, color) {}

    /**
     * Устанавливает цвет правой границы ячейки.
     * 
     * @param {number} row       - int, строка
     * @param {number} column    - int, столбец
     * @param {number} thickness - int, толщина
     * @param {string} color     - String, цвет
     */
    setCellBorderRight(row, column, thickness, color) {}

    /**
     * Устанавливает цвет нижней границы ячейки.
     * 
     * @param {number} row       - int, строка
     * @param {number} column    - int, столбец
     * @param {number} thickness - int, толщина
     * @param {string} color     - String, цвет
     */
    setCellBorderBottom(row, column, thickness, color) {}

    /**
     * Устанавливает цвет левой границы ячейки.
     * 
     * @param {number} row       - int, строка
     * @param {number} column    - int, столбец
     * @param {number} thickness - int, толщина
     * @param {string} color     - String, цвет
     */
    setCellBorderLeft(row, column, thickness, color) {}

    /**
     * Устанавливает цвет ячейки.
     * 
     * @param {number} row    - int, строка
     * @param {number} column - int, столбец
     * @param {string} color  - String, цвет
     */
    setCellColor(row, column, color) {}

    /**
     * Устанавливает интервал между ячейками.
     * 
     * @param {number} spacing - int, интервал
     */
    setCellSpacing(spacing) {}

    /**
     * Устанавливает отступы в ячейке.
     * 
     * @param {number} padding - int, отступ
     */
    setPaddingInCells(padding) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Контейнер содержимое которого центрируется.
 */
class CenterContainer {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Редактируемый грид
 */
class EditGrid {

    
    constructor(builderProperties) {}

    /**
     * Загрузка грида.
     * 
     * @param {Boolean} reconfigure - boolean
     */
    load(reconfigure) {}

    /**
     * Проверка на валидность грида.
     * 
     * @param {boolean} invalidate - boolean
     */
    isValid(invalidate) {}

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у грида.
     * 
     * @return {boolean} boolean
     */
    isRequired() {}

    /**
     * Установка свойства "Обязателен для заполнения" для грида.
     * 
     * @param {boolean} required - boolean
     */
    setRequired(required) {}

    /**
     * Очистка выделения в гриде.
     */
    clear() {}

    /**
     * Возвращает список всех элементов.
     * 
     * @return {RowModelData>} List
     */
    getAllItems() {}

    /**
     * Удаляет все элементы.
     */
    clearItems() {}

    /**
     * Устанавливает гриду сообщение о не валидности данных.
     * 
     * @param {string} msg - String
     */
    markInvalid(msg) {}

    /**
     * Очистка сообщения о не валидности в гриде.
     */
    clearInvalid() {}

    /**
     * Очищает фильтр грида.
     */
    clearFilter() {}

    
    isLoading() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Контейнер располагающей компоненты в одну колонку.
 */
class VerticalContainer {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}
}

/**
 * Html-редактор
 */
class HtmlEditor {

    
    constructor(builderProperties) {}

    /**
     * Получение значение поля.
     * 
     * @return {string} String
     */
    getValue() {}

    /**
     * Установка значение поля.
     * 
     * @param {string} value - String
     */
    setValue(value) {}

    /**
     * Получает текст объекта.
     * 
     * @return {string} новый текст объекта
     */
    getText() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * This helper class contains static methods to work with application components.
 */
class Components {

    /**
     * Finds component by code.
     * 
     * @param {string} code component code
     * @return {Component} component
     */
    static findByCode(code) {}

    /**
     * Finds component by code in the particular container.
     * 
     * @param {Component} container
     * @param {string} code      component code
     * @return {Component} component
     */
    static findByCodeInContainer(container, code) {}

    /**
     * Returns all application components currently available in application.
     * 
     * @return {Array[Component]} components array
     */
    static getAll() {}
}

/**
 * Абстрактный класс - построитель компонента
 * <p>
 * Базовый класс всех построителей компонентов, все компоненты должны его
 * расширять.
 */
class Component {

    /**
     * Returns component's code.
     * 
     * @return {string} component's code
     */
    getCode() {}

    /**
     * Checks if component is in hidden state.
     * 
     * @return {boolean} true if component is hidden
     */
    isHidden() {}

    /**
     * Sets component's hidden state.
     * 
     * @param {boolean} hidden true - to hide component, false - to show component
     */
    setHidden(hidden) {}

    /**
     * Focuses component.
     */
    focus() {}

    /**
     * Checks if component is enabled.
     * 
     * @return {boolean} true if component is enabled
     */
    isEnabled() {}

    /**
     * Sets component's enabled state.
     * 
     * @param {boolean} enabled true - to enable component, false - to disable component
     */
    setEnabled(enabled) {}
}

/**
 * Контейнер с одним элементом.
 */
class SimpleContainer {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Список с мультивыбором
 * 
 * @param <T>
 */
class MultiComboBox {

    
    constructor(builderProperties) {}

    /**
     * Получает текст объекта.
     * 
     * @return {string} новый текст объекта
     */
    getText() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true, если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Проверяет доступность для редактирования.
     * 
     * @return {boolean} true, если доступен для редактирования
     */
    isEditable() {}

    /**
     * Устанавливает доступность для редактирования.
     * 
     * @param {boolean} editable true, доступ для редактирования
     */
    setEditable(editable) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}
}

/**
 * Простая картинка
 */
class Image {

    
    constructor(builderProperties) {}

    /**
     * Установка картинки по умолчанию
     * 
     * @param {string} url - String, ссылка
     */
    setDefaultImage(url) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Дерево
 */
class Tree {

    
    constructor(builderProperties) {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Загружает данные, используя текущую конфигурацию
     */
    load() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Простой HTML редактор
 */
class SimpleHtmlEditor {

    
    constructor(builderProperties) {}

    /**
     * Получает значение текстового поля
     * 
     * @return {string} String
     */
    getValue() {}

    /**
     * Устанавливает значения текстового поля
     * 
     * @param {string} value String
     */
    setValue(value) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}
}

/**
 * Контейнер располагающей компоненты в один ряд.
 */
class HorizontalContainer {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Горизонтальное меню
 */
class HorizontalMenu {

    
    constructor(builderProperties) {}

    
    addChild(child) {}

    
    removeChild(child) {}

    /**
     * Очищает контейнер.
     */
    clearContainer() {}

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    forceLayout() {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Чек-бокс группа
 */
class CheckGroup {

    
    constructor(builderProperties) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Текстовая область ввода
 */
class TextArea {

    
    constructor(builderProperties) {}

    /**
     * Получить значение текстовой области
     * 
     * @return {string} String
     */
    getValue() {}

    /**
     * Установка значения текстовой области
     * 
     * @param {string} value String
     */
    setValue(value) {}

    /**
     * Получает текст объекта.
     * 
     * @return {string} новый текст объекта
     */
    getText() {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает не валидный статус для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Удаляет не валидный статус для поля.
     */
    clearInvalid() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для не валидного поля
     * @return {boolean} true если поле доступно
     */
    isValid(invalidate) {}

    /**
     * Проверяет обязательность для заполнения.
     * 
     * @return {boolean} true, if is required
     */
    isRequired() {}

    /**
     * Устанавливает обязательное требование для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}
}


class ContextMenuItem {

    
    constructor(params) {}

    
    setTitle(title) {}

    
    setImageUrl(imageUrl) {}

    /**
     * Returns component's code.
     * 
     * @return {string} component's code
     */
    getCode() {}

    /**
     * Checks if component is in hidden state.
     * 
     * @return {boolean} true if component is hidden
     */
    isHidden() {}

    /**
     * Sets component's hidden state.
     * 
     * @param {boolean} hidden true - to hide component, false - to show component
     */
    setHidden(hidden) {}

    /**
     * Focuses component.
     */
    focus() {}

    /**
     * Checks if component is enabled.
     * 
     * @return {boolean} true if component is enabled
     */
    isEnabled() {}

    /**
     * Sets component's enabled state.
     * 
     * @param {boolean} enabled true - to enable component, false - to disable component
     */
    setEnabled(enabled) {}
}

/**
 * Поле загрузки файла.
 */
class UploadField {

    
    constructor(builderProperties) {}

    
    submit() {}

    /**
     * Получить значение текстового поля
     * 
     * @return {string} String
     */
    getValue() {}

    /**
     * Установка значения текстового поля
     * 
     * @param {string} value String
     */
    setValue(value) {}

    /**
     * Проверяет готовность компонента к выполнению события.
     * 
     * @return {boolean} true, если готов
     */
    isReady() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для не валидного поля
     * @return {boolean} true если поле доступно
     */
    isValid(invalidate) {}

    /**
     * Получает имя файла.
     * 
     * @return {string} String - имя файла
     */
    getFileName() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}
}

/**
 * Радиогруппа
 */
class RadioGroup {

    
    constructor(builderProperties) {}

    /**
     * Получить информацию о свойстве "Обязателен для заполнения" у радиогруппы
     * 
     * @return {boolean} boolean
     */
    isRequired() {}

    /**
     * Установка свойства "Обязателен для заполнения" для радиогруппы
     * 
     * @param {boolean} required - boolean
     */
    setRequired(required) {}

    /**
     * Очистка значения радиогруппы
     */
    clear() {}

    /**
     * Проверка на валидность радиогруппы
     * 
     * @param {boolean} invalidate - boolean
     * @return {boolean} boolean
     */
    isValid(invalidate) {}

    /**
     * Устанавливает радиогруппе сообщение о не валидности данных
     * 
     * @param {string} msg - String
     */
    markInvalid(msg) {}

    /**
     * Очищает сообщение о не валидности радиогруппы
     */
    clearInvalid() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}
}

/**
 * Древовидное меню
 */
class TreeMenu {

    
    constructor(builderProperties) {}

    /**
     * @param {ComponentBuilder} child
     */
    addChild(child) {}

    /**
     * @param {ComponentBuilder} child
     */
    removeChild(child) {}

    
    getChildren() {}

    
    getChildrenCount() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Устанавливает фокус на компоненте.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента,
     * false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает статус не валидности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус не валидности для поля.
     */
    clearInvalid() {}

    /**
     * Загружает данные, используя текущую конфигурацию
     */
    load() {}
}

/**
 * Текстовое поле.
 */
class TextField {

    
    constructor(builderProperties) {}

    /**
     * Получить значение текстового поля
     * 
     * @return {string} String
     */
    getValue() {}

    /**
     * Установка значения текстового поля
     * 
     * @param {string} value String
     */
    setValue(value) {}

    /**
     * Возвращает требуемую минимальную длину поля.
     * 
     * @return {number} требуемая минимальная длина поля
     */
    getMinLength() {}

    /**
     * Возвращает максимальную длину поля.
     * 
     * @return {number} максимальная длина возвращаемого поля
     */
    getMaxLength() {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для не валидного поля
     * @return {boolean} true если поле доступно
     */
    isValid(invalidate) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}

    /**
     * Возвращает код компонента.
     * 
     * @return {string} код компонента
     */
    getCode() {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Добавляет имя CSS стиля.
     * 
     * @param {string} name - String, имя CSS стиля
     */
    addStyleName(name) {}

    /**
     * Устанавливает стиль компонента.
     * 
     * @param {string} styleName - String, название стиля
     */
    setStyleName(styleName) {}

    /**
     * Удаляет имя CSS стиля.
     * 
     * @param {string} name - String, название стиля
     */
    removeStyleName(name) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Устанавливает ширину виджета.
     * 
     * @param {number} value - int, новая ширина для установки
     */
    setWidth(value) {}

    /**
     * Устанавливает высоту виджета.
     * 
     * @param {number} value - int, новая высота для установки
     */
    setHeight(value) {}

    /**
     * Возвращает идентификатор элемента HTML.
     * 
     * @return {string} идентификатор элемента
     */
    getDomId() {}

    /**
     * Устанавливает идентификатор элемента HTML.
     * 
     * @param {string} domId - String, новый идентификатор компонента
     */
    setDomId(domId) {}
}

/**
 * Список
 */
class ComboBox {

    
    constructor(builderProperties) {}

    /**
     * Проверяет доступность для редактирования.
     * 
     * @return {boolean} true, если доступен для редактирования
     */
    isEditable() {}

    /**
     * Устанавливает доступность для редактирования.
     * 
     * @param {boolean} editable true, доступ для редактирования
     */
    setEditable(editable) {}

    /**
     * Возвращает текст объекта.
     * 
     * @return {string} новый текст объекта
     */
    getText() {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для не валидного поля
     * @return {boolean} true если поле доступно
     */
    isValid(invalidate) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Очищает значение поля.
     */
    clear() {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Устанавливает значение только для чтения.
     * 
     * @param {boolean} readOnly true, если поле доступно только для чтения
     */
    setReadOnly(readOnly) {}
}

/**
 * Поле ввода даты
 */
class DateField {

    
    constructor(builderProperties) {}

    /**
     * Получить тип текста.
     */
    getText() {}

    /**
     * Получение шаблона поля даты.
     * 
     * @return {string} String
     */
    getDatePattern() {}

    /**
     * Установка шаблона поля даты.
     * 
     * @param {string} datePattern - String
     */
    setDatePattern(datePattern) {}

    /**
     * Установить "редактируемость" поля даты.
     * 
     * @param {Boolean} editable - boolean
     */
    setEditable(editable) {}

    /**
     * Установить скрытость выпадающего поля выбора даты.
     * 
     * @param {Boolean} hideTrigger - Boolean
     */
    setHideTrigger(hideTrigger) {}

    /**
     * Установка параметра "только чтение" для поля даты.
     * 
     * @param {Boolean} readOnly - boolean
     */
    setReadOnly(readOnly) {}

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     * 
     * @return {boolean} true, если компонент скрыт
     */
    isHidden() {}

    /**
     * Устанавливает скрытое состояние компонента.
     * 
     * @param {boolean} hidden true - для скрытия компонента, false - для отображения компонента
     */
    setHidden(hidden) {}

    /**
     * Фокусирует компонент.
     */
    focus() {}

    /**
     * Проверяет, включен ли компонент.
     * 
     * @return {boolean} true, если компонент включен
     */
    isEnabled() {}

    /**
     * Устанавливает включенное состояние компонента.
     * 
     * @param {boolean} enabled true - для включения компонента, false - для отключения компонента
     */
    setEnabled(enabled) {}

    /**
     * Проверяет, обязательно ли поле для заполнения.
     * 
     * @return {boolean} true, если обязательно
     */
    isRequired() {}

    /**
     * Устанавливает обязательность для заполнения поля.
     * 
     * @param {boolean} required true, если поле обязательно для заполнения
     */
    setRequired(required) {}

    /**
     * Проверяет, является ли поле валидным.
     * 
     * @param {boolean} invalidate true для признания поля валидным
     * @return {boolean} true если поле валидно
     */
    isValid(invalidate) {}

    /**
     * Получает маску поля.
     * 
     * @return {string} маска поля
     */
    getFieldMask() {}

    /**
     * Устанавливает маску поля.
     * 
     * @param {string} mask новая маска поля
     */
    setFieldMask(mask) {}

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     * 
     * @param {string} msg сообщение
     */
    markInvalid(msg) {}

    /**
     * Очищает статус недействительности для поля.
     */
    clearInvalid() {}

    /**
     * Очищает значение поля.
     */
    clear() {}
}

