package com.redditspider.dao.reddit.parser;

import static com.google.common.collect.FluentIterable.from;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Default implementation of parser factory.
 */
@Service
public class DefaultParserFactory implements ParserFactory {
    @Autowired
    Collection<Parser> parsers;

    @Override
    public Parser getParser(final String url) {
        return from(parsers).firstMatch(new Predicate<Parser>() {
            @Override
            public boolean apply(Parser input) {
                return input.isApplicable(url);
            }
        }).or(Optional.of(new NopParser())).get();
    }
}
