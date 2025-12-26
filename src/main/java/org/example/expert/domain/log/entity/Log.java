package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.common.entity.Timestamped;

@Getter
@Entity
@Table(name = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean success;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "request_url")
    private String requestUrl;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "client_ip")
    private String clientIp;

    public Log(boolean success, Long userId, String requestUrl, String httpMethod, String clientIp) {
        this.success = success;
        this.requestUrl = requestUrl;
        this.httpMethod = httpMethod;
        this.clientIp = clientIp;
        this.userId = userId;
    }
}
