package com.latte.server.common.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Created by Minky on 2021-06-09
 */

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();
}
