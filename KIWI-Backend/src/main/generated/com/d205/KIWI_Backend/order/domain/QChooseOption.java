package com.d205.KIWI_Backend.order.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChooseOption is a Querydsl query type for ChooseOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChooseOption extends EntityPathBase<ChooseOption> {

    private static final long serialVersionUID = -1957558373L;

    public static final QChooseOption chooseOption = new QChooseOption("chooseOption");

    public final StringPath engName = createString("engName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath korName = createString("korName");

    public final NumberPath<Integer> megaDrinkPrice = createNumber("megaDrinkPrice", Integer.class);

    public final NumberPath<Integer> normalDrinkPrice = createNumber("normalDrinkPrice", Integer.class);

    public QChooseOption(String variable) {
        super(ChooseOption.class, forVariable(variable));
    }

    public QChooseOption(Path<? extends ChooseOption> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChooseOption(PathMetadata metadata) {
        super(ChooseOption.class, metadata);
    }

}

