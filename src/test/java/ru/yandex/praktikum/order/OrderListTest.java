package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class OrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("В теле ответа возвращается непустой список заказов")
    public void orderListIsReturned() {
        orderClient.getList()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
                .body("orders", instanceOf(java.util.List.class))
                .body("orders", not(empty()));
    }
}
