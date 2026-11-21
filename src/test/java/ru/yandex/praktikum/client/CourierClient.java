package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.CourierCredentials;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создать курьера")
    public ValidatableResponse create(Courier courier) {
        return spec()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Авторизовать курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return spec()
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удалить курьера с id {id}")
    public ValidatableResponse delete(int id) {
        return spec()
                .when()
                .delete(COURIER_PATH + "/" + id)
                .then();
    }

    @Step("Удалить курьера без id")
    public ValidatableResponse deleteWithoutId() {
        return spec()
                .when()
                .delete(COURIER_PATH + "/")
                .then();
    }

    @Step("Получить id курьера по логину и паролю")
    public int getId(CourierCredentials credentials) {
        return login(credentials).extract().path("id");
    }

    @Step("Удалить созданного курьера (очистка данных)")
    public void deleteIfExists(CourierCredentials credentials) {
        ValidatableResponse response = login(credentials);
        if (response.extract().statusCode() == 200) {
            Integer id = response.extract().path("id");
            if (id != null) {
                delete(id);
            }
        }
    }
}
