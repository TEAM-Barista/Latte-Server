package com.latte.server.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by Minky on 2021-09-03
 */

@NoArgsConstructor
@Getter
@Setter
public class UserAccessNotifyRequestDto {
    @NotNull(message = "accessNotify cannot be null")
    private Boolean accessNotify;

    public UserAccessNotifyRequestDto(Boolean accessNotify) {
        this.accessNotify = accessNotify;
    }
}
