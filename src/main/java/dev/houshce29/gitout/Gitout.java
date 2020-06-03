package dev.houshce29.gitout;

import dev.houshce29.gitout.core.InputParser;
import dev.houshce29.gitout.core.LaunchKey;

public class Gitout {
    private static final InputParser PARSER = new InputParser();

    public static void main(String[] args) {
        LaunchKey key = PARSER.parse(args);
        key.fire();
    }
}
