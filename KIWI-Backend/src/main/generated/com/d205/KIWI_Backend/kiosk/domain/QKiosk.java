package com.d205.KIWI_Backend.kiosk.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKiosk is a Querydsl query type for Kiosk
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKiosk extends EntityPathBase<Kiosk> {

    private static final long serialVersionUID = -1535707169L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKiosk kiosk = new QKiosk("kiosk");

    public final com.d205.KIWI_Backend.global.common.QBaseEntity _super = new com.d205.KIWI_Backend.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<com.d205.KIWI_Backend.order.domain.KioskOrder, com.d205.KIWI_Backend.order.domain.QKioskOrder> kioskOrders = this.<com.d205.KIWI_Backend.order.domain.KioskOrder, com.d205.KIWI_Backend.order.domain.QKioskOrder>createList("kioskOrders", com.d205.KIWI_Backend.order.domain.KioskOrder.class, com.d205.KIWI_Backend.order.domain.QKioskOrder.class, PathInits.DIRECT2);

    public final StringPath location = createString("location");

    public final com.d205.KIWI_Backend.member.domain.QMember member;

    public final StringPath status = createString("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QKiosk(String variable) {
        this(Kiosk.class, forVariable(variable), INITS);
    }

    public QKiosk(Path<? extends Kiosk> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKiosk(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKiosk(PathMetadata metadata, PathInits inits) {
        this(Kiosk.class, metadata, inits);
    }

    public QKiosk(Class<? extends Kiosk> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.d205.KIWI_Backend.member.domain.QMember(forProperty("member")) : null;
    }

}

