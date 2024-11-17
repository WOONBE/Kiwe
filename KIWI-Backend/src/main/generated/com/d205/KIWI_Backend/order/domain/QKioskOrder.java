package com.d205.KIWI_Backend.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QKioskOrder is a Querydsl query type for KioskOrder
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKioskOrder extends EntityPathBase<KioskOrder> {

    private static final long serialVersionUID = 18419156L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QKioskOrder kioskOrder = new QKioskOrder("kioskOrder");

    public final DateTimePath<java.time.LocalDateTime> assignedTime = createDateTime("assignedTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.d205.KIWI_Backend.kiosk.domain.QKiosk kiosk;

    public final QOrder order;

    public QKioskOrder(String variable) {
        this(KioskOrder.class, forVariable(variable), INITS);
    }

    public QKioskOrder(Path<? extends KioskOrder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QKioskOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QKioskOrder(PathMetadata metadata, PathInits inits) {
        this(KioskOrder.class, metadata, inits);
    }

    public QKioskOrder(Class<? extends KioskOrder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.kiosk = inits.isInitialized("kiosk") ? new com.d205.KIWI_Backend.kiosk.domain.QKiosk(forProperty("kiosk"), inits.get("kiosk")) : null;
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order")) : null;
    }

}

