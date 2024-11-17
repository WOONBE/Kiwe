package com.d205.KIWI_Backend.log.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QViewCount is a Querydsl query type for ViewCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewCount extends EntityPathBase<ViewCount> {

    private static final long serialVersionUID = 1752689445L;

    public static final QViewCount viewCount1 = new QViewCount("viewCount1");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath requestURI = createString("requestURI");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QViewCount(String variable) {
        super(ViewCount.class, forVariable(variable));
    }

    public QViewCount(Path<? extends ViewCount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QViewCount(PathMetadata metadata) {
        super(ViewCount.class, metadata);
    }

}

