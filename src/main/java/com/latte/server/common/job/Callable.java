package com.latte.server.common.job;

/**
 * Created by Minky on 2021-09-03
 */

/**
 * 인터페이스를 통한 함수 전달
 */

@FunctionalInterface
public interface Callable {
    void call();
}
