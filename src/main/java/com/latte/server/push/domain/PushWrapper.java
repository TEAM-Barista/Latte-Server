package com.latte.server.push.domain;

import lombok.Getter;

import static com.latte.server.push.domain.PushMessage.*;
import static com.latte.server.push.domain.PushTarget.*;
import static com.latte.server.push.domain.PushTitle.*;

/**
 * Created by Minky on 2021-12-09
 */

@Getter
public class PushWrapper {
    private String targetToken;
    private String title;
    private String body;
    private String target;
    private String targetUrl;

    public PushWrapper(
            String targetToken,
            String title,
            String body,
            String target,
            String targetUrl
    ) {
        this.targetToken = targetToken;
        this.title = title;
        this.body = body;
        this.target = target;
        this.targetUrl = targetUrl;
    }

    public static PushWrapper ofNewReply(String targetToken, String title) {
        return new PushWrapper(
                targetToken,
                NEW_REPLY_PUSH_TITLE,
                String.format(NEW_REPLY_PUSH_MESSAGE, title),
                REPLY,
                String.format("/api/v1/%s", REPLY)
        );
    }

    public static PushWrapper ofNewInterview(String targetToken, String title) {
        return new PushWrapper(
                targetToken,
                NEW_INTERVIEW_PUSH_TITLE,
                String.format(NEW_INTERVIEW_PUSH_MESSAGE, title),
                INTERVIEW,
                String.format("/api/v1/%s", INTERVIEW)
        );
    }

    public static PushWrapper ofMatchInterview(String targetToken, String title) {
        return new PushWrapper(
                targetToken,
                MATCH_NEW_INTERVIEW_PUSH_TITLE,
                String.format(MATCH_NEW_INTERVIEW_PUSH_MESSAGE, title),
                INTERVIEW,
                String.format("/api/v1/%s", INTERVIEW)
        );
    }

    public static PushWrapper ofMatchQnA(String targetToken, String title) {
        return new PushWrapper(
                targetToken,
                MATCH_NEW_QUESTION_AND_ANSWER_PUSH_TITLE,
                String.format(MATCH_NEW_QUESTION_AND_ANSWER_PUSH_MESSAGE, title),
                QNA,
                String.format("/api/v1/%s", QNA)
        );
    }

    public static PushWrapper ofMatchPost(String targetToken, String title) {
        return new PushWrapper(
                targetToken,
                MATCH_NEW_QUESTION_AND_ANSWER_PUSH_TITLE,
                String.format(MATCH_NEW_QUESTION_AND_ANSWER_PUSH_MESSAGE, title),
                POST,
                String.format("/api/v1/%s", POST)
        );
    }
}
