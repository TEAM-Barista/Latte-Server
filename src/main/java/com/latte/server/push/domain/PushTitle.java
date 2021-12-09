package com.latte.server.push.domain;

/**
 * Created by Minky on 2021-12-09
 */
public final class PushTitle {
    private PushTitle() { }

    public static String MATCH_NEW_QUESTION_AND_ANSWER_PUSH_TITLE = "태그에 해당하는 새로운 질문 게시판 글이 등록되었습니다."; // 태그 매칭 되는 새로운 질문 게시판 알림 제목
    public static String MATCH_NEW_POST_PUSH_TITLE = "태그에 해당하는 새로운 자유 게시판 글이 등록되었습니다."; // 태그 매칭 되는 새로운 자유 게시판 알림 제목
    public static String MATCH_NEW_INTERVIEW_PUSH_TITLE = "태그에 해당하는 새로운 인터뷰가 등록되었습니다."; // 태그 매칭 되는 새로운 인터뷰 알림 제목
    public static String NEW_INTERVIEW_PUSH_TITLE = "새로운 인터뷰가 등록되었습니다."; // 새로운 인터뷰 알림 제목
    public static String NEW_REPLY_PUSH_TITLE = "새로운 댓글이 등록되었습니다.";  // 새로운 댓글 알림 제목
}