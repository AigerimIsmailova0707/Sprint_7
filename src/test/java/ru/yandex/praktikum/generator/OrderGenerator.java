package ru.yandex.praktikum.generator;

import ru.yandex.praktikum.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class OrderGenerator {

    private static final Random RANDOM = new Random();

    public static Order getOrderWithColor(List<String> color) {
        return new Order(
                "Имя" + RANDOM.nextInt(1000),
                "Фамилия" + RANDOM.nextInt(1000),
                "Москва, ул. Тестовая, " + (RANDOM.nextInt(100) + 1),
                String.valueOf(RANDOM.nextInt(200) + 1),
                "+7900" + (1000000 + RANDOM.nextInt(8999999)),
                RANDOM.nextInt(7) + 1,
                LocalDate.now().plusDays(RANDOM.nextInt(10) + 1).toString(),
                "Комментарий " + RANDOM.nextInt(1000),
                color
        );
    }
}
