package com.d205.KIWI_Backend.menu.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenu is a Querydsl query type for Menu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenu extends EntityPathBase<Menu> {

    private static final long serialVersionUID = -998451979L;

    public static final QMenu menu = new QMenu("menu");

    public final com.d205.KIWI_Backend.global.common.QBaseEntity _super = new com.d205.KIWI_Backend.global.common.QBaseEntity(this);

    public final StringPath category = createString("category");

    public final NumberPath<Integer> categoryNumber = createNumber("categoryNumber", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath description = createString("description");

    public final StringPath hotOrIce = createString("hotOrIce");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath imgPath = createString("imgPath");

    public final StringPath name = createString("name");

    public final ListPath<com.d205.KIWI_Backend.order.domain.OrderMenu, com.d205.KIWI_Backend.order.domain.QOrderMenu> orderMenus = this.<com.d205.KIWI_Backend.order.domain.OrderMenu, com.d205.KIWI_Backend.order.domain.QOrderMenu>createList("orderMenus", com.d205.KIWI_Backend.order.domain.OrderMenu.class, com.d205.KIWI_Backend.order.domain.QOrderMenu.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QMenu(String variable) {
        super(Menu.class, forVariable(variable));
    }

    public QMenu(Path<? extends Menu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenu(PathMetadata metadata) {
        super(Menu.class, metadata);
    }

}

