package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.generator.CourierGenerator;
import ru.yandex.praktikum.generator.OrderGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierCredentials;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class OrderAcceptTest {

    private static final int NON_EXISTENT_ID = 999999999;

    private OrderClient orderClient;
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;
    private int track;
    private int orderId;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        courierClient = new CourierClient();

        courier = CourierGenerator.getRandomCourier();
        courierClient.create(courier);
        courierId = courierClient.getId(CourierCredentials.from(courier));

        track = orderClient.create(OrderGenerator.getOrderWithColor(List.of("GREY")))
                .extract().path("track");
        orderId = orderClient.getOrderId(track);
    }

    @After
    public void tearDown() {
        orderClient.cancel(track);
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Курьер может принять заказ")
    @Description("Успешный запрос возвращает 200 и ok: true")
    public void orderCanBeAccepted() {
        orderClient.accept(orderId, courierId)
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя принять заказ без id курьера")
    @Description("Запрос без courierId возвращает 400")
    public void acceptWithoutCourierIdFails() {
        orderClient.acceptWithoutCourier(orderId)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Нельзя принять заказ с неверным id курьера")
    @Description("Запрос с несуществующим courierId возвращает 404")
    public void acceptWithWrongCourierIdFails() {
        orderClient.accept(orderId, NON_EXISTENT_ID)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Нельзя принять заказ без номера заказа")
    @Description("Запрос без id заказа возвращает ошибку")
    public void acceptWithoutOrderIdFails() {
        orderClient.acceptWithoutOrder(courierId)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Not Found."));
    }

    @Test
    @DisplayName("Нельзя принять заказ с неверным номером заказа")
    @Description("Запрос с несуществующим id заказа возвращает 404")
    public void acceptWithWrongOrderIdFails() {
        orderClient.accept(NON_EXISTENT_ID, courierId)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Заказа с таким id не существует"));
    }
}
