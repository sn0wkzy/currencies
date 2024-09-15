package com.github.sn0wkzy.currencies.common.mongodb.parser;

import org.bson.BsonDocument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMongoParser<V> {

    public abstract @NotNull BsonDocument write(@NotNull V v);

    public abstract @Nullable V read(@NotNull BsonDocument document);
}
