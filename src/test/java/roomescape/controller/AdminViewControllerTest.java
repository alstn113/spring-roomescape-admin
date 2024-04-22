package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

class AdminViewControllerTest extends BaseControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {"/admin", "/admin/reservation", "/admin/time"})
    void adminPage(String path) {
        Response response = RestAssured.given()
                .when().get(path);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
