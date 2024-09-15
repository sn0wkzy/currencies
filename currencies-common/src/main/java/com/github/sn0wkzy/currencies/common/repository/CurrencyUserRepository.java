package com.github.sn0wkzy.currencies.common.repository;

import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import com.github.sn0wkzy.currencies.common.model.leaderboard.LeadboardUser;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.mongodb.MongoRepository;
import com.github.sn0wkzy.currencies.common.repository.parser.CurrencyUserMongoParser;
import com.mongodb.client.model.*;
import org.bson.BsonDocument;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class CurrencyUserRepository extends MongoRepository<CurrencyUser> {

    public CurrencyUserRepository() {
        super(new CurrencyUserMongoParser());

        collection.createIndex(
                Indexes.ascending("playerName"),
                new IndexOptions()
                        .name("playerName")
                        .unique(true)
                        .background(true)
                        .sparse(true)
        );
    }

    public void insertOne(CurrencyUser currencyUser) {
        this.collection.replaceOne(eq("playerName", currencyUser.getPlayerName()),
                this.parser.write(currencyUser), REPLACE_OPTIONS);
    }

    public CurrencyUser search(String playerName) {
        final BsonDocument bsonDocument = collection.find(eq("playerName", playerName)).first();
        if (bsonDocument == null) return null;

        return parser.read(bsonDocument);
    }

    public void bulkUpdate(Set<CurrencyUser> currencyUsers) {
        final List<ReplaceOneModel<BsonDocument>> models = currencyUsers.stream()
                .map(user -> new ReplaceOneModel<>(
                        eq("playerName", user.getPlayerName()), parser.write(user), REPLACE_OPTIONS))
                .collect(Collectors.toList());

        collection.bulkWrite(models);
    }

    public LinkedList<LeadboardUser> searchLeaderboard(Currency currency) {
        final AtomicInteger position = new AtomicInteger(1);
        return collection.find(Filters.gt("currencyMap." + currency.getId(), 0))
                .sort(Sorts.descending("currencyMap." + currency.getId()))
                .limit(10)
                .map(parser::read)
                .map(currencyUser -> new LeadboardUser(currencyUser.getPlayerName(), position.getAndIncrement(), currencyUser.getAmount(currency.getId())))
                .into(new LinkedList<>());
    }
}
