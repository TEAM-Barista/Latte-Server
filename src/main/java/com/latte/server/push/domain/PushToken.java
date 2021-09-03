package com.latte.server.push.domain;

import com.latte.server.user.domain.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Minky on 2021-09-03
 */

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Entity
public class PushToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence_gen")
    @SequenceGenerator(name = "user_sequence_gen", sequenceName = "user_sequence")
    @Column(name = "push_id")
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public PushToken(Long id, String token, User user) {
        this.id = id;
        this.token = token;
        this.user = user;
    }
}
