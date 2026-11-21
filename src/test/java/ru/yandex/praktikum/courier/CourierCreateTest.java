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

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
    }

    @After
    public void tearDown() {
        if (courier.getLogin() != null && courier.getPassword() != null) {
            courierClient.deleteIfExists(CourierCredentials.from(courier));
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Успешное создание курьера: код 201 и ok: true")
    public void courierCanBeCreated() {
        courierClient.create(courier)
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Повторное создание курьера с теми же данными возвращает 409")
    public void duplicateCourierCannotBeCreated() {
        courierClient.create(courier);

        courierClient.create(courier)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера с уже существующим логином")
    @Description("Курьер с тем же логином, но другим паролем и именем, возвращает 409")
    public void courierWithExistingLoginCannotBeCreated() {
        courierClient.create(courier);
        Courier sameLogin = CourierGenerator.getRandomCourier();
        sameLogin.setLogin(courier.getLogin());

        courierClient.create(sameLogin)
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Запрос без поля login возвращает 400")
    public void courierWithoutLoginCannotBeCreated() {
        courier.setLogin(null);

        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    @Description("Запрос без поля password возвращает 400")
    public void courierWithoutPasswordCannotBeCreated() {
        courier.setPassword(null);

        courierClient.create(courier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
