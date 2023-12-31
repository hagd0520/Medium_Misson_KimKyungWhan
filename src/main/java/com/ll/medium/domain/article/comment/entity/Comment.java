//package com.ll.medium.domain.article.comment.entity;
//
//import com.ll.medium.domain.article.article.entity.Article;
//import com.ll.medium.domain.member.member.entity.Member;
//import com.ll.medium.global.jpa.BaseEntity;
//import jakarta.persistence.Entity;
//import jakarta.persistence.ManyToOne;
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//
//import static jakarta.persistence.FetchType.LAZY;
//import static lombok.AccessLevel.PROTECTED;
//
//@Entity
//@SuperBuilder
//@AllArgsConstructor(access = PROTECTED)
//@NoArgsConstructor(access = PROTECTED)
//@Getter
//@Setter
//@ToString(callSuper = true)
//public class Comment extends BaseEntity {
//    @ManyToOne(fetch = LAZY)
//    private Article article;
//    @ManyToOne(fetch = LAZY)
//    private Member author;
//    private String body;
//}
