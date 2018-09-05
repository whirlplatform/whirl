package org.whirlplatform.component.client.utils;

public class KeyMappings {

    public static final int VK_0 = 48;
    public static final int VK_1 = 49;
    public static final int VK_2 = 50;
    public static final int VK_3 = 51;
    public static final int VK_4 = 52;
    public static final int VK_5 = 53;
    public static final int VK_6 = 54;
    public static final int VK_7 = 55;
    public static final int VK_8 = 56;
    public static final int VK_9 = 57;
    public static final int VK_A = 65;
    public static final int VK_B = 66;
    public static final int VK_C = 67;
    public static final int VK_D = 68;
    public static final int VK_E = 69;
    public static final int VK_F = 70;
    public static final int VK_G = 71;
    public static final int VK_H = 72;
    public static final int VK_I = 73;
    public static final int VK_J = 74;
    public static final int VK_K = 75;
    public static final int VK_L = 76;
    public static final int VK_M = 77;
    public static final int VK_N = 78;
    public static final int VK_O = 79;
    public static final int VK_P = 80;
    public static final int VK_Q = 81;
    public static final int VK_R = 82;
    public static final int VK_S = 83;
    public static final int VK_T = 84;
    public static final int VK_U = 85;
    public static final int VK_V = 86;
    public static final int VK_W = 87;
    public static final int VK_X = 88;
    public static final int VK_Y = 89;
    public static final int VK_Z = 90;

    public static final int VK_NUM_LOCK = 144;
    public static final int VK_NUMBER_SIGN = 520;
    public static final int VK_NUMPAD0 = 96;
    public static final int VK_NUMPAD1 = 97;
    public static final int VK_NUMPAD2 = 98;
    public static final int VK_NUMPAD3 = 99;
    public static final int VK_NUMPAD4 = 100;
    public static final int VK_NUMPAD5 = 101;
    public static final int VK_NUMPAD6 = 102;
    public static final int VK_NUMPAD7 = 103;
    public static final int VK_NUMPAD8 = 104;
    public static final int VK_NUMPAD9 = 105;

    public static final int VK_F1 = 112;
    public static final int VK_F10 = 121;
    public static final int VK_F11 = 122;
    public static final int VK_F12 = 123;
    public static final int VK_F13 = 61440;
    public static final int VK_F14 = 61441;
    public static final int VK_F15 = 61442;
    public static final int VK_F16 = 61443;
    public static final int VK_F17 = 61444;
    public static final int VK_F18 = 61445;
    public static final int VK_F19 = 61446;
    public static final int VK_F2 = 113;
    public static final int VK_F20 = 61447;
    public static final int VK_F21 = 61448;
    public static final int VK_F22 = 61449;
    public static final int VK_F23 = 61450;
    public static final int VK_F24 = 61451;
    public static final int VK_F3 = 114;
    public static final int VK_F4 = 115;
    public static final int VK_F5 = 116;
    public static final int VK_F6 = 117;
    public static final int VK_F7 = 118;
    public static final int VK_F8 = 119;
    public static final int VK_F9 = 120;

    public static final int VK_AMPERSAND = 150;
    public static final int VK_ASTERISK = 151;
    public static final int VK_AT = 512;
    public static final int VK_BACK_QUOTE = 192;
    public static final int VK_BACK_SLASH = 92;
    public static final int VK_BACK_SPACE = 8;
    public static final int VK_BRACELEFT = 161;
    public static final int VK_BRACERIGHT = 162;
    public static final int VK_CAPS_LOCK = 20;
    public static final int VK_CLOSE_BRACKET = 93;
    public static final int VK_COLON = 513;
    public static final int VK_COMMA = 44;
    public static final int VK_DECIMAL = 110;
    public static final int VK_DELETE = 127;
    public static final int VK_DIVIDE = 111;
    public static final int VK_DOLLAR = 515;
    public static final int VK_DOWN = 40;
    public static final int VK_ENTER = 13;
    public static final int VK_EQUALS = 61;
    public static final int VK_ESCAPE = 27;
    public static final int VK_EURO_SIGN = 516;
    public static final int VK_EXCLAMATION_MARK = 517;
    public static final int VK_HOME = 36;
    public static final int VK_INSERT = 155;
    public static final int VK_KP_DOWN = 225;
    public static final int VK_KP_LEFT = 226;
    public static final int VK_KP_RIGHT = 227;
    public static final int VK_KP_UP = 224;
    public static final int VK_LEFT = 37;
    public static final int VK_LEFT_PARENTHESIS = 519;
    public static final int VK_LESS = 153;
    public static final int VK_MINUS = 45;
    public static final int VK_MULTIPLY = 106;
    public static final int VK_OPEN_BRACKET = 91;
    public static final int VK_PAGE_DOWN = 34;
    public static final int VK_PAGE_UP = 33;
    public static final int VK_PLUS = 521;
    public static final int VK_PRINTSCREEN = 154;
    public static final int VK_QUOTE = 222;
    public static final int VK_QUOTEDBL = 152;
    public static final int VK_RIGHT = 39;
    public static final int VK_RIGHT_PARENTHESIS = 522;
    public static final int VK_ROMAN_CHARACTERS = 245;
    public static final int VK_SCROLL_LOCK = 145;
    public static final int VK_SEMICOLON = 59;
    public static final int VK_SEPARATER = 108;
    public static final int VK_SEPARATOR = 108;
    public static final int VK_SHIFT = 16;
    public static final int VK_SLASH = 47;
    public static final int VK_SPACE = 32;
    public static final int VK_SUBTRACT = 109;
    public static final int VK_TAB = 9;
    public static final int VK_UP = 38;
    public static final int VK_WINDOWS = 524;

    public static int keyCode(String ch) {
        if ("0".equals(ch)) {
            return VK_0;
        } else if ("1".equals(ch)) {
            return VK_1;
        } else if ("2".equals(ch)) {
            return VK_2;
        } else if ("3".equals(ch)) {
            return VK_3;
        } else if ("4".equals(ch)) {
            return VK_4;
        } else if ("5".equals(ch)) {
            return VK_5;
        } else if ("6".equals(ch)) {
            return VK_6;
        } else if ("7".equals(ch)) {
            return VK_7;
        } else if ("8".equals(ch)) {
            return VK_8;
        } else if ("9".equals(ch)) {
            return VK_9;
        } else if ("A".equals(ch)) {
            return VK_A;
        } else if ("B".equals(ch)) {
            return VK_B;
        } else if ("C".equals(ch)) {
            return VK_C;
        } else if ("D".equals(ch)) {
            return VK_D;
        } else if ("E".equals(ch)) {
            return VK_E;
        } else if ("F".equals(ch)) {
            return VK_F;
        } else if ("G".equals(ch)) {
            return VK_G;
        } else if ("H".equals(ch)) {
            return VK_H;
        } else if ("I".equals(ch)) {
            return VK_I;
        } else if ("J".equals(ch)) {
            return VK_J;
        } else if ("K".equals(ch)) {
            return VK_K;
        } else if ("L".equals(ch)) {
            return VK_L;
        } else if ("M".equals(ch)) {
            return VK_M;
        } else if ("N".equals(ch)) {
            return VK_N;
        } else if ("O".equals(ch)) {
            return VK_O;
        } else if ("P".equals(ch)) {
            return VK_P;
        } else if ("Q".equals(ch)) {
            return VK_Q;
        } else if ("R".equals(ch)) {
            return VK_R;
        } else if ("S".equals(ch)) {
            return VK_S;
        } else if ("T".equals(ch)) {
            return VK_T;
        } else if ("U".equals(ch)) {
            return VK_U;
        } else if ("V".equals(ch)) {
            return VK_V;
        } else if ("W".equals(ch)) {
            return VK_W;
        } else if ("X".equals(ch)) {
            return VK_X;
        } else if ("Y".equals(ch)) {
            return VK_Y;
        } else if ("Z".equals(ch)) {
            return VK_Z;
        } else if ("ENTER".equalsIgnoreCase(ch)) {
            return VK_ENTER;
        } else if ("ESCAPE".equalsIgnoreCase(ch)) {
            return VK_ESCAPE;
        } else if ("F1".equalsIgnoreCase(ch)) {
            return VK_F1;
        } else if ("F2".equalsIgnoreCase(ch)) {
            return VK_F2;
        } else if ("F3".equalsIgnoreCase(ch)) {
            return VK_F3;
        } else if ("F4".equalsIgnoreCase(ch)) {
            return VK_F4;
        } else if ("F5".equalsIgnoreCase(ch)) {
            return VK_F5;
        } else if ("F6".equalsIgnoreCase(ch)) {
            return VK_F6;
        } else if ("F7".equalsIgnoreCase(ch)) {
            return VK_F7;
        } else if ("F8".equalsIgnoreCase(ch)) {
            return VK_F8;
        } else if ("F9".equalsIgnoreCase(ch)) {
            return VK_F9;
        } else if ("F10".equalsIgnoreCase(ch)) {
            return VK_F10;
        } else if ("F11".equalsIgnoreCase(ch)) {
            return VK_F11;
        } else if ("F12".equalsIgnoreCase(ch)) {
            return VK_F12;
        } else if ("UP".equalsIgnoreCase(ch)) {
            return VK_KP_UP;
        } else if ("RIGHT".equalsIgnoreCase(ch)) {
            return VK_KP_RIGHT;
        } else if ("DOWN".equalsIgnoreCase(ch)) {
            return VK_KP_DOWN;
        } else if ("LEFT".equalsIgnoreCase(ch)) {
            return VK_KP_LEFT;
        } else if ("SPACE".equalsIgnoreCase(ch)) {
            return VK_SPACE;
        } else {
            return -1;
        }

    }
}
