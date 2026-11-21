package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.OrderClient;
import ru.yandex.praktikum.generator.OrderGenerator;
import ru.yandex.praktikum.model.Order;

import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;
    private OrderClient orderClient;
    private Integer track;

    public OrderCreateTest(String description, List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] getColors() {
        return new Object[][]{
                {"BLACK", List.of("BLACK")},
                {"GREY", List.of("GREY")},
                {"BLACK и GREY", List.of("BLACK", "GREY")},
                {"без цвета", null},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderClient.cancel(track);
        }
    }

    @Test
    @DisplayName("Создание заказа с разными цветами самоката")
    @Description("Заказ создаётся с одним цветом, двумя цветами и без цвета; в ответе есть track")
    public void orderCanBeCreated() {
        Order order = OrderGenerator.getOrderWithColor(color);

        track = orderClient.create(order)
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract().path("track");
    }
}
