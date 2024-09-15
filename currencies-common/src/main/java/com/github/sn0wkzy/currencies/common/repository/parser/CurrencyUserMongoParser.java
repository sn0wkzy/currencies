package com.github.sn0wkzy.currencies.common.repository.parser;

import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.mongodb.parser.AbstractMongoParser;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CurrencyUserMongoParser extends AbstractMongoParser<CurrencyUser> {
    @Override
    public @NotNull BsonDocument write(@NotNull CurrencyUser currencyUser) {
        final BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.append("playerName", new BsonString(currencyUser.getPlayerName()));
        writeCurrency(currencyUser, bsonDocument);

        return bsonDocument;
    }

    @Override
    public @Nullable CurrencyUser read(@NotNull BsonDocument document) {
        final String playerName = document.getString("playerName").getValue();
        final Map<String, Double> currencyMap = readCurrencyMap(document.getDocument("currencyMap"));

        return new CurrencyUser(playerName, currencyMap);
    }

    private void writeCurrency(@NotNull CurrencyUser currencyUser, @NotNull BsonDocument bsonDocument) {
        final BsonDocument currencyMapDocument = new BsonDocument();
        currencyUser.getCurrencies().forEach((id, amount) -> currencyMapDocument.put(id, new BsonDouble(amount)));

        bsonDocument.put("currencyMap", currencyMapDocument);
    }

    private Map<String, Double> readCurrencyMap(@NotNull BsonDocument currencyMapDocument) {
        final Map<String, Double> currencyMap = new HashMap<>();
        currencyMapDocument.forEach((id, amount) -> currencyMap.put(id, amount.asDouble().getValue()));

        return currencyMap;
    }
}
