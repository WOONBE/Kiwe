package com.d205.KIWI_Backend.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 328426475L;

    public static final QMember member = new QMember("member1");

    public final com.d205.KIWI_Backend.global.common.QBaseEntity _super = new com.d205.KIWI_Backend.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.d205.KIWI_Backend.kiosk.domain.Kiosk, com.d205.KIWI_Backend.kiosk.domain.QKiosk> kiosks = this.<com.d205.KIWI_Backend.kiosk.domain.Kiosk, com.d205.KIWI_Backend.kiosk.domain.QKiosk>createList("kiosks", com.d205.KIWI_Backend.kiosk.domain.Kiosk.class, com.d205.KIWI_Backend.kiosk.domain.QKiosk.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final EnumPath<MemberType> type = createEnum("type", MemberType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

