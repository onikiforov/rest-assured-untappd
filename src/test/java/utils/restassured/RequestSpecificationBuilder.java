package utils.restassured;

import apitests.BaseAPITest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;

import java.util.Properties;

import static utils.PropertiesUtils.getEnvironmentConfigPathByConfigType;
import static utils.PropertiesUtils.getProp;
import static utils.PropertiesUtils.loadProperties;

class RequestSpecificationBuilder extends RequestSpecBuilder {
    static Properties properties = loadProperties(getEnvironmentConfigPathByConfigType(BaseAPITest.environment));

    private RequestSpecificationBuilder() {
        super();
    }

    static RequestSpecificationBuilder createCommonRequestSpecBuilder() {
        RequestSpecificationBuilder requestSpecificationBuilder = new RequestSpecificationBuilder();
        return requestSpecificationBuilder.setProxyFromConfig(properties)
                .setBaseURIFromConfig(properties)
                .buildHeadersFromConfig(properties)
                .addRequestLoggingFilter()
                .addResponseLoggingFilter()
                .addAllureAttachmentFilter("http-request.ftl", "http-response.ftl");
    }

    private RequestSpecificationBuilder buildHeadersFromConfig(Properties properties) {
        String languageHeader = getProp(properties, "accept_language_header");
        ContentType acceptHeader = ContentType.fromContentType(getProp(properties, "accept_header"));

        return this.setAcceptHeader(acceptHeader)
                .addAcceptLanguageHeader(languageHeader);
    }

    private RequestSpecificationBuilder setAcceptHeader(ContentType contentType) {
        this.setAccept(contentType);
        return this;
    }

    private RequestSpecificationBuilder setContentTypeHeader(ContentType contentType) {
        this.setContentType(contentType);
        return this;
    }

    private RequestSpecificationBuilder addAcceptLanguageHeader(String language) {
        this.addHeader("Accept-Language", language);
        return this;
    }

    private RequestSpecificationBuilder setupProxy(String server, int port) {
        this.setProxy(server, port);
        this.setRelaxedHTTPSValidation();
        return this;
    }

    private RequestSpecificationBuilder setBaseURI(String uri) {
        this.setBaseUri(uri);
        return this;
    }

    private RequestSpecificationBuilder addRequestLoggingFilter() {
        this.addFilter(new RequestLoggingFilter());
        return this;
    }

    private RequestSpecificationBuilder addResponseLoggingFilter() {
        this.addFilter(new ResponseLoggingFilter());
        return this;
    }

    private RequestSpecificationBuilder addAllureAttachmentFilter(String requestFtl, String responseFtl) {
        this.addFilter(new CustomAllureRestAssured()
                .setRequestTemplate(requestFtl)
                .setResponseTemplate(responseFtl));
        return this;
    }

    private RequestSpecificationBuilder resetBaseURI() {
        this.setBaseUri("");
        return this;
    }

    private RequestSpecificationBuilder setBaseURIFromConfig(Properties properties) {
        String baseUrl = getProp(properties, "base_url");
        String apiVersion = getProp(properties, "api_version");

        String uri = baseUrl + "/" + apiVersion;
        return this.setBaseURI(uri);
    }

    private RequestSpecificationBuilder setProxyFromConfig(Properties properties) {
        if (Boolean.parseBoolean(getProp(properties, "proxy.set"))) {
            String server = getProp(properties, "proxy.server");
            int port = Integer.parseInt(getProp(properties, "proxy.port"));

            return this.setupProxy(server, port);
        } else {
            return this;
        }
    }

    private RequestSpecificationBuilder addClientSecret(String secret) {
        this.addQueryParam("client_secret", secret);
        return this;
    }

    private RequestSpecificationBuilder addClientId(String id) {
        this.addQueryParam("client_id", id);
        return this;
    }

    RequestSpecificationBuilder buildClientDataFromConfig(Properties properties) {
        String secret = getProp(properties, "client_secret");
        String id = getProp(properties, "client_id");

        return this.addClientSecret(secret)
                .addClientId(id);
    }

    private RequestSpecificationBuilder enableUrlEncoding() {
        this.setUrlEncodingEnabled(true);
        return this;
    }

    private RequestSpecificationBuilder disableUrlEncoding() {
        this.setUrlEncodingEnabled(false);
        return this;
    }

    RequestSpecificationBuilder addToken(String token) {
        this.addQueryParam("access_token", token);
        return this;
    }
}
