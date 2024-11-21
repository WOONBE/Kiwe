package com.d205.KIWI_Backend.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlackList is a Querydsl query type for BlackList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlackList extends EntityPathBase<BlackList> {

    private static final long serialVersionUID = 207550028L;

    public static final QBlackList blackList = new QBlackList("blackList");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath invalidRefreshToken = createString("invalidRefreshToken");

    public QBlackList(String variable) {
        super(BlackList.class, forVariable(variable));
    }

    public QBlackList(Path<? extends BlackList> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlackList(PathMetadata metadata) {
        super(BlackList.class, metadata);
    }

}

