package ru.netology.ibank.data;

public class DataHelper {

    public static String getValidLogin() {
        return "vasya";
    }

    public static String getValidPassword() {
        return "qwerty123";
    }

    public static String getValidVerificationCode() {
        return "12345";
    }

    public static String getFirstCardNumber() {
        return "5559 0000 0000 0001";
    }

    public static String getSecondCardNumber() {
        return "5559 0000 0000 0002";
    }


    public static String getFirstCardMaskedNumber() {
        return "**** **** **** 0001";
    }

    public static String getSecondCardMaskedNumber() {
        return "**** **** **** 0002";
    }
}