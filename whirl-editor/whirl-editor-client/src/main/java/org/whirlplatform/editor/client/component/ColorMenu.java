package org.whirlplatform.editor.client.component;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.widget.core.client.ColorPalette;
import com.sencha.gxt.widget.core.client.menu.Menu;

public class ColorMenu extends Menu {

    protected ColorPalette palette;
    private String[] colors = new String[] {"E9967A", "B22222", "FFB6C1",
        "DB7093", "FF6347", "FFD700", "FFFACD", "FFDAB9", "BDB76B",
        "EE82EE", "FF00FF", "9400D3", "800080", "7B68EE", "7CFC00",
        "90EE90", "3CB371", "006400", "808000", "20B2AA", "00FFFF",
        "7FFFD4", "5F9EA0", "B0E0E6", "00BFFF", "4169E1", "000080",
        "FFEBCD", "DEB887", "F4A460", "D2691E", "A52A2A", "F0FFF0",
        "F0F8FF", "F5F5DC", "FFFFF0", "FFE4E1", "C0C0C0", "778899",
        "000000", "FFFFFF"};
    private String[] labels = new String[] {"Dark Salmon", "Fire Brick",
        "Light Pink", "Pale Violet Red", "Tomato", "Gold", "Lemon Chiffon",
        "Peach Puff", "Dark Khaki", "Violet", "Magenta", "Dark Violet",
        "Purple", "Medium Slate Blue", "Lawn Green", "Light Green",
        "Medium Sea Green", "Dark Green", "Olive", "Light Sea Green",
        "Cyan", "Aquamarine", "Cadet Blue", "Powder Blue", "Deep Sky Blue",
        "Royal Blue", "Navy", "Blanched Almond", "Burly Wood",
        "Sandy Brown", "Chocolate", "Brown", "Honeydew", "Alice Blue",
        "Beige", "Ivory", "Misty Rose", "Silver", "Light Slate Gray",
        "Black", "White"};

    public ColorMenu() {
        palette = new ColorPalette(colors, labels);
        palette.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                onValueChanged(event);
            }
        });
        add(palette);
        plain = true;
        showSeparator = false;
        setEnableScrolling(false);
    }

    @Override
    public void focus() {
        super.focus();
        palette.getElement().focus();
    }

    public String getColor() {
        return palette.getValue();
    }

    public void setColor(String color) {
        palette.setValue(color);
    }

    public ColorPalette getPalette() {
        return palette;
    }

    protected void onValueChanged(ValueChangeEvent<String> event) {

    }
}
