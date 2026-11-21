package ru.yandex.praktikum.generator;

import ru.yandex.praktikum.model.Courier;

import java.util.UUID;

public class CourierGenerator {

    private static String randomString() {
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public static Courier getRandomCourier() {
        return new Courier("login_" + randomString(), "pass_" + randomString(), "name_" + randomString());
    }
}
