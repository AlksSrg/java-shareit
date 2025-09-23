package ru.practicum.shareit.gateway.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурационный класс для настройки компонентов Gateway.
 * Предоставляет бины для работы с REST запросами к основному серверу.
 */
@Configuration
public class GatewayConfig {

    /**
     * Создает и настраивает RestTemplate с поддержкой HTTP методов, включая PATCH.
     * Использует Apache HttpClient 5 с пулом соединений для улучшения производительности.
     *
     * @return настроенный экземпляр RestTemplate с поддержкой всех HTTP методов
     */
    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .build();

        var httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(5000);
        requestFactory.setConnectionRequestTimeout(5000);

        return new RestTemplate(requestFactory);
    }
}