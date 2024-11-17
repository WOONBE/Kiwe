package com.d205.KIWI_Backend.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 515884031L;

    public static final QOrder order = new QOrder("order1");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final NumberPath<Integer> gender = createNumber("gender", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<KioskOrder, QKioskOrder> kioskOrders = this.<KioskOrder, QKioskOrder>createList("kioskOrders", KioskOrder.class, QKioskOrder.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> orderDate = createDateTime("orderDate", java.time.LocalDateTime.class);

    public final ListPath<OrderMenu, QOrderMenu> orderMenus = this.<OrderMenu, QOrderMenu>createList("orderMenus", OrderMenu.class, QOrderMenu.class, PathInits.DIRECT2);

    public final StringPath status = createString("status");

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

