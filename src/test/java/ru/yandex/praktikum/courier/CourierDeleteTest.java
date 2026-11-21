package ru.yandex.praktikum.courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.CourierClient;
import ru.yandex.praktikum.generator.CourierGenerator;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierCredentials;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierDeleteTest {

    private static final int NON_EXISTENT_ID = 999999999;

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        courierClient.deleteIfExists(CourierCredentials.from(courier));
    }

    @Test
    @DisplayName("Курьера можно удалить")
    @Description("Успешное удаление возвращает 200 и ok: true")
    public void courierCanBeDeleted() {
        int id = courierClient.getId(CourierCredentials.from(courier));

        courierClient.delete(id)
                .statusCode(SC_OK)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя удалить курьера без id")
    @Description("Запрос без id возвращает ошибку")
    public void deleteWithoutIdFails() {
        courierClient.deleteWithoutId()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Not Found."));
    }

    @Test
    @DisplayName("Нельзя удалить курьера с несуществующим id")
    @Description("Запрос с несуществующим id возвращает 404")
    public void deleteWithNonExistentIdFails() {
        courierClient.delete(NON_EXISTENT_ID)
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Курьера с таким id нет."));
    }
}
