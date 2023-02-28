package org.whirlplatform.server.utils;

public class TranslitUtil {

    private static final String[] charTable = new String[81];

    private static final char START_CHAR = 'Ё';

    static {
        charTable['А' - START_CHAR] = "A";
        charTable['Б' - START_CHAR] = "B";
        charTable['В' - START_CHAR] = "V";
        charTable['Г' - START_CHAR] = "G";
        charTable['Д' - START_CHAR] = "D";
        charTable['Е' - START_CHAR] = "E";
        charTable['Ё' - START_CHAR] = "E";
        charTable['Ж' - START_CHAR] = "ZH";
        charTable['З' - START_CHAR] = "Z";
        charTable['И' - START_CHAR] = "I";
        charTable['Й' - START_CHAR] = "I";
        charTable['К' - START_CHAR] = "K";
        charTable['Л' - START_CHAR] = "L";
        charTable['М' - START_CHAR] = "M";
        charTable['Н' - START_CHAR] = "N";
        charTable['О' - START_CHAR] = "O";
        charTable['П' - START_CHAR] = "P";
        charTable['Р' - START_CHAR] = "R";
        charTable['С' - START_CHAR] = "S";
        charTable['Т' - START_CHAR] = "T";
        charTable['У' - START_CHAR] = "U";
        charTable['Ф' - START_CHAR] = "F";
        charTable['Х' - START_CHAR] = "KH";
        charTable['Ц' - START_CHAR] = "TS";
        charTable['Ч' - START_CHAR] = "CH";
        charTable['Ш' - START_CHAR] = "SH";
        charTable['Щ' - START_CHAR] = "SHCH";
        charTable['Ъ' - START_CHAR] = "'";
        charTable['Ы' - START_CHAR] = "Y";
        charTable['Ь' - START_CHAR] = "'";
        charTable['Э' - START_CHAR] = "E";
        charTable['Ю' - START_CHAR] = "YU";
        charTable['Я' - START_CHAR] = "YA";

        for (int i = 0; i < charTable.length; i++) {
            char idx = (char) ((char) i + START_CHAR);
            char lower = new String(new char[] {idx}).toLowerCase().charAt(0);
            if (charTable[i] != null) {
                charTable[lower - START_CHAR] = charTable[i].toLowerCase();
            }
        }
    }

    public static String toTranslit(String text) {
        char[] charBuffer = text.toCharArray();
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < charBuffer.length; i++) {
            char symbol = charBuffer[i];
            int num = symbol - START_CHAR;
            if (num >= 0 && i < charTable.length) {
                String replace = null;
                if (i == 0 || charBuffer[i - 1] == ' ') {
                    switch (symbol) {
                        case 'Е':
                            replace = "YE";
                            break;
                        case 'е':
                            replace = "ye";
                            break;
                        case 'Ё':
                            replace = "YO";
                            break;
                        case 'ё':
                            replace = "yo";
                            break;
                        default:
                            break;
                    }
                } else if (charBuffer[i - 1] == 'Ь' || charBuffer[i - 1] == 'ь') {
                    switch (symbol) {
                        case 'И':
                            replace = "YI";
                            break;
                        case 'и':
                            replace = "yi";
                            break;
                        default:
                            break;
                    }
                }
                if (replace == null) {
                    replace = charTable[num];
                }
                sb.append(replace == null ? symbol : replace);
            } else {
                sb.append(symbol);
            }
        }
        return sb.toString();
    }
}
