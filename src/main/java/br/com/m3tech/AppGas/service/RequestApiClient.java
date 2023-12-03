package br.com.m3tech.AppGas.service;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RequestApiClient {

    private final RestTemplate restTemplate;
    private final HttpMethod requestMethod;
    private final String rootURL;
    private final List<String> pathValues;
    private final ImmutableSortedMap.Builder<String, String> requestParam;
    private final ImmutableSortedMap.Builder<String, String> httpHeaders;

    private Object body;
    private String urlCompleta;
    private HttpEntity<Object> httpEntity;
    private boolean desabilitaSSL;
    private boolean buildRealizado;

    public RequestApiClient(@NonNull final RestTemplate restTemplate, @NonNull final HttpMethod requestMethod,
                            @NonNull final String rootURL) {
        log.info("RestTemplate: {} - RequestMethod: {} - RootURL: {}", restTemplate, requestMethod, rootURL);
        this.restTemplate = restTemplate;
        this.requestMethod = requestMethod;
        this.rootURL = rootURL;
        this.pathValues = Lists.newArrayList();
        this.requestParam = ImmutableSortedMap.reverseOrder();
        this.httpHeaders = ImmutableSortedMap.reverseOrder();
    }

    public RequestApiClient build() {

        this.urlCompleta = StringUtils.endsWith(this.rootURL, "/") ? this.rootURL : this.rootURL + "/";
        this.urlCompleta = this.urlCompleta.trim() + buildPathValues() + buildRequestParam();

        log.info("Body: {}", body);
        this.httpEntity = new HttpEntity<>(this.body, buildHeaders());
        this.buildRealizado = true;
        return this;
    }
    
    public RequestApiClient buildSemRemoverUltimaBarra() {

        this.urlCompleta = StringUtils.endsWith(this.rootURL, "/") ? this.rootURL : this.rootURL + "/";
        this.urlCompleta = this.urlCompleta.trim() + buildPathValuesSemRemoverUltimaBarra() + buildRequestParam();

        log.info("Body: {}", body);
        this.httpEntity = new HttpEntity<>(this.body, buildHeaders());
        this.buildRealizado = true;
        return this;
    }

    private HttpHeaders buildHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        this.httpHeaders.build().forEach(httpHeaders::add);
        return httpHeaders;
    }

    private String buildPathValues() {

        StringBuilder sbPath = new StringBuilder();
        for (String pathValue : this.pathValues) {
            sbPath.append(StringUtils.remove(pathValue, "/")).append("/");
        }
        return StringUtils.removeEnd(sbPath.toString(), "/");
    }
    
    private String buildPathValuesSemRemoverUltimaBarra() {

        StringBuilder sbPath = new StringBuilder();
        for (String pathValue : this.pathValues) {
            sbPath.append(StringUtils.remove(pathValue, "/")).append("/");
        }
        return sbPath.toString();
    }

    public String buildRequestParam() {

        StringBuilder sbRequest = new StringBuilder();
        if (this.requestParam == null) {
            return "";
        }
        log.info("Build RequestParam: {}", this.requestParam);
        for (Map.Entry<String, String> requestEntry : this.requestParam.build().entrySet()) {
            if (sbRequest.length() == 0) {
                sbRequest.append("?");
            }
            sbRequest.append(requestEntry.getKey()).append("=").append(requestEntry.getValue()).append("&");
        }
        return StringUtils.removeEnd(sbRequest.toString(), "&");
    }

    public RequestApiClient pathValue(final String value) {

        Preconditions.checkNotNull(value);
        this.pathValues.add(value);
        return this;
    }

    public RequestApiClient requestParam(final String name, final String value) {

        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        this.requestParam.put(name, value);
        return this;
    }

    public RequestApiClient bodyFromJson(final String json) {

        Preconditions.checkNotNull(json);
        log.info("json body request: {}", json);
        this.body = json;
        return this;
    }

    public RequestApiClient bodyFromObject(final Object objBody) {

        Preconditions.checkNotNull(objBody);
        log.info("Objeto Body Request: {}", objBody);
        this.body = removeAccents(new Gson().toJson(objBody));
        log.info("Body Criado: {}", this.body);
        return this;
    }
    
    public RequestApiClient bodyFromMultiValueMap(MultiValueMap<String, String> multiValueMap) {
        this.body = multiValueMap;
        return this;
    }
    
    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public RequestApiClient addHeader(final String headerName, final String headerValue) {

        Preconditions.checkNotNull(headerName, "Nenhum headerName informado");
        Preconditions.checkNotNull(headerValue, "Nenhum headerValue informado");
        this.httpHeaders.put(headerName, headerValue);
        return this;
    }

    public RequestApiClient addContentTypeJson() {
        log.info("Add Content-Type");
        addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return this;
    }
    
    public RequestApiClient addContentTypeUrlEncoded() {
        log.info("Add Content-Type");
        addHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return this;
    }

    public RequestApiClient addAcceptJson() {
        log.info("Add Accept");
        addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
        return this;
    }

    public RequestApiClient desabitaliSSL() {

        this.desabilitaSSL = true;
        return this;
    }

    public ResponseEntity<String> enviar() {
        log.info("Inicio do Envio");
        if (!buildRealizado) {
            log.info("Build nao realizado");
            return new ResponseEntity<>(new Gson().toJson(new RetornoInesperado(
                            HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Build nao executado", null, null)
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

//        if (this.desabilitaSSL) {
//            log.info("SSL disabilitado");
//            DisableSSL.disable();
//        }
        try {
            log.info("URL Completa: {} RequestMethod: {}, Entity: {}", this.urlCompleta, this.requestMethod, this.httpEntity);
            this.restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            final ResponseEntity<String> exchange = this.restTemplate.exchange(this.urlCompleta, this.requestMethod, this.httpEntity, String.class);
            log.info("Retorno com sucesso");
            log.info("Response {} -  body: {}",exchange, exchange.getBody());
            return exchange;
        } catch (Exception e) {
            log.error("Erro na requisicao ao barramento ", e);
            RetornoInesperado retornoInesperado;
            if (e instanceof HttpStatusCodeException) {
                log.error("Response do Erro Client:  {}", ((HttpStatusCodeException) e).getResponseBodyAsString());
                retornoInesperado = new RetornoInesperado(((HttpStatusCodeException) e).getStatusCode().toString(),
                        e.getMessage(), ((HttpStatusCodeException) e).getResponseBodyAsString(), e.getMessage());
            } else {
                retornoInesperado = new RetornoInesperado(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), null, e.getMessage());
            }

			try {
				String json = new Gson().toJson(retornoInesperado);

				return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (Exception ex) {
				throw ex;
			}
        }
    }

    @RequiredArgsConstructor
    @Getter
    public class RetornoInesperado {
        private final String statusCode;
        private final String mensagem;
        private final String body;
        private final String exception;

    }

}
