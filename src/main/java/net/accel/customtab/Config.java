package net.accel.customtab;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public final String header;
    public final String footer;

    public static final Config DEFAULT = new Config();

    public Config(Path filePath) throws IOException, IllegalArgumentException {
        var results = new String[]{null, null};
        Files.lines(filePath, StandardCharsets.UTF_8).forEach(s -> {
            if (s.isEmpty())
                return;
            if (s.charAt(0) == '#')
                return;
            int i = s.indexOf('=');
            if (i == -1)
                throw new IllegalArgumentException("\"" + s + "\" is not a key-pair");
            var key = s.substring(0, i);
            var value = s.substring(i + 1);
            switch (key) {
                case "header" -> results[0] = value;
                case "footer" -> results[1] = value;
                default -> throw new IllegalArgumentException("unknown option \"" + key + "\"");
            }
        });
        if (results[0] == null || results[1] == null)
            throw new IllegalArgumentException("missing options");
        header = results[0];
        footer = results[1];
    }

    private Config() {
        header = footer = "";
    }
}
