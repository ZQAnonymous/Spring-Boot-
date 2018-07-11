    package com.websocket.web.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.socket.server.standard.ServerEndpointExporter;

    /**
     * @Auther: zhao quan
     * @Date: 2018/7/9 09:32
     * @PACKAGE_NAME: com.websocket.web.config
     * @Description: websocket的配置
     */
    @Configuration
    public class WebSocketStompConfig {

        @Bean
        public ServerEndpointExporter serverEndpointExporter(){
            return new ServerEndpointExporter();
        }

    }
