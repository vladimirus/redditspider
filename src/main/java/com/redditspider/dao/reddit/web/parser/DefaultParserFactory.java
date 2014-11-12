package com.redditspider.dao.reddit.web.parser;

import static com.google.common.collect.FluentIterable.from;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Default implementation of parser factory.
 */
@Service
public class DefaultParserFactory implements ParserFactory {
    @Autowired
    Collection<ParserCreator> parserCreators;

    @Override
    public Parser getParser(final WebDriver driver) {
        return from(parserCreators).filter(new Predicate<ParserCreator>() {
            @Override
            public boolean apply(ParserCreator parserCreator) {
                return parserCreator.isApplicable(driver);
            }
        }).firstMatch(new Predicate<ParserCreator>() {
            @Override
            public boolean apply(ParserCreator input) {
                return true;
            }
        }).transform(new Function<ParserCreator, Parser>() {
            @Override
            public Parser apply(ParserCreator parserCreator) {
                return parserCreator.getInstance(driver);
            }
        }).or(new NopParser());
    }
}
