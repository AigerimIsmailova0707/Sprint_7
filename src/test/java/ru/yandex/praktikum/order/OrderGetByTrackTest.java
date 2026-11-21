package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.generator.OrderGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderGetByTrackTest {

    private static final int NON_EXISTENT_TRACK = 999999999;

    private OrderClient orderClient;
    private int track;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        track = orderClient.create(OrderGenerator.getOrderWithColor(List.of("BLACK")))
                .extract().path("track");
    }

    @After
    public void tearDown() {
        orderClient.cancel(track);
    }

    @Test
    @DisplayName("Получение заказа по трек-номеру")
    @Description("Успешный запрос возвращает объект заказа")
    public void orderCanBeFoundByTrack() {
        orderClient.getByTrack(track)
                .statusCode(SC_OK)
                .body("order", notNullValue())
                .body("order.track", equalTo(track));
    }

    @Test
    @DisplayName("Нельзя получить заказ без трек-номера")
    @Description("Запрос без параметра t возвращает 400")
    public void getOrderWithoutTrackFails() {
        orderClient.getWithoutTrack()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Нельзя получить несуществующий заказ")
    @Description("Запрос с несуществующим трек-номером возвращает 404")
    public void getNonExistentOrderFails() {
        orderClient.getByTrack(NON_EXISTENT_TRACK)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Заказ не найден"));
    }
}
