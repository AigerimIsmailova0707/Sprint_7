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
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

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
    @DisplayName("Курьер может авторизоваться")
    @Description("Успешный логин возвращает 200 и id")
    public void courierCanLogin() {
        courierClient.login(CourierCredentials.from(courier))
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    @Description("Пустой логин возвращает 400")
    public void loginWithoutLoginFails() {
        courierClient.login(new CourierCredentials("", courier.getPassword()))
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    @Description("Пустой пароль возвращает 400")
    public void loginWithoutPasswordFails() {
        courierClient.login(new CourierCredentials(courier.getLogin(), ""))
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неверным логином")
    @Description("Неверный логин возвращает 404")
    public void loginWithWrongLoginFails() {
        courierClient.login(new CourierCredentials(courier.getLogin() + "_wrong", courier.getPassword()))
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неверным паролем")
    @Description("Неверный пароль возвращает 404")
    public void loginWithWrongPasswordFails() {
        courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword() + "_wrong"))
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться под несуществующим курьером")
    @Description("Несуществующая пара логин-пароль возвращает 404")
    public void loginWithNonExistentCourierFails() {
        Courier notCreated = CourierGenerator.getRandomCourier();

        courierClient.login(CourierCredentials.from(notCreated))
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
