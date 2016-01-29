package controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.AuthController;
import org.junit.Test;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import util.AbstractTest;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.*;

public class MediaControllerTest extends AbstractTest {
    private ObjectNode loginJson;
    private ObjectNode loginAdmin;

    public MediaControllerTest() {
        loginJson = Json.newObject();
        loginJson.put("email", "test@testing.dev");
        loginJson.put("password", "josevte1");

        loginAdmin = Json.newObject();
        loginAdmin.put("email", "admin@admin.dev");
        loginAdmin.put("password", "josevte1");
    }

    @Test
    public void testMediaControllerUnauthorized() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media").post(new File("LICENSE")).get(timeout);
            assertEquals(UNAUTHORIZED, response.getStatus());
            response = WS.url("http://localhost:3333/recipes/1/media/1").delete().get(timeout);
            assertEquals(UNAUTHORIZED, response.getStatus());
            response = WS.url("http://localhost:3333/recipes/1/media/test").delete().get(timeout);
            assertEquals(UNAUTHORIZED, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerGetOk() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/1").get().get(timeout);
            assertEquals(OK, response.getStatus());
            response = WS.url("http://localhost:3333/recipes/1/media/test").get().get(timeout);
            assertEquals(OK, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerGetNotFound() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/2").get().get(timeout);
            assertEquals(NOT_FOUND, response.getStatus());
            response = WS.url("http://localhost:3333/recipes/1/media/not-found").get().get(timeout);
            assertEquals(NOT_FOUND, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerDeleteByIdOk() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse login = WS.url("http://localhost:3333/auth/login").post(loginJson).get(timeout);
            token = login.asJson().get(AuthController.AUTH_TOKEN).asText();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/1").setHeader(AuthController.AUTH_TOKEN_HEADER, token).delete().get(timeout);
            assertEquals(OK, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerDeleteByFileOk() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse login = WS.url("http://localhost:3333/auth/login").post(loginJson).get(timeout);
            token = login.asJson().get(AuthController.AUTH_TOKEN).asText();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/test").setHeader(AuthController.AUTH_TOKEN_HEADER, token).delete().get(timeout);
            assertEquals(OK, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerDeleteByIdAdminOk() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse login = WS.url("http://localhost:3333/auth/login").post(loginAdmin).get(timeout);
            token = login.asJson().get(AuthController.AUTH_TOKEN).asText();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/1").setHeader(AuthController.AUTH_TOKEN_HEADER, token).delete().get(timeout);
            assertEquals(OK, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerDeleteByFilAdmineOk() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse login = WS.url("http://localhost:3333/auth/login").post(loginAdmin).get(timeout);
            token = login.asJson().get(AuthController.AUTH_TOKEN).asText();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/test").setHeader(AuthController.AUTH_TOKEN_HEADER, token).delete().get(timeout);
            assertEquals(OK, response.getStatus());

            successTest();
        });
    }

    @Test
    public void testMediaControllerDeleteNotFound() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), () -> {
            initializeDataController();
            WSResponse login = WS.url("http://localhost:3333/auth/login").post(loginJson).get(timeout);
            token = login.asJson().get(AuthController.AUTH_TOKEN).asText();
            WSResponse response = WS.url("http://localhost:3333/recipes/1/media/2").setHeader(AuthController.AUTH_TOKEN_HEADER, token).delete().get(timeout);
            assertEquals(NOT_FOUND, response.getStatus());
            response = WS.url("http://localhost:3333/recipes/1/media/not-found").setHeader(AuthController.AUTH_TOKEN_HEADER, token).delete().get(timeout);
            assertEquals(NOT_FOUND, response.getStatus());

            successTest();
        });
    }
}
