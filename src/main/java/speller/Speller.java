package speller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

public class Speller {
    public enum Language {EN, RU, UK}

    public enum Option {
        IGNORE_DIGITS,
        IGNORE_URLS,
        FIND_REPEAT_WORDS,
        IGNORE_CAPITALIZATION
    }

    public SpellerError[] checkText(String text) {
        return checkText(text, Set.of(), Set.of());
    }

    public SpellerError[] checkText(String text, Set<Language> languages) {
        return checkText(text, languages, Set.of());
    }

    public SpellerError[] checkText(String text, Set<Language> languages, Set<Option> options) {
        var path = "https://speller.yandex.net/services/spellservice.json/checkText";
        var uri = URI.create(path + buildQuery(text, languages, options));
        var request = HttpRequest.newBuilder(uri).build();

        try {
            var response = client.send(request, BodyHandlers.ofString());
            return mapper.readValue(response.body(), SpellerError[].class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return new SpellerError[0];
    }

    public CompletableFuture<SpellerError[]> checkTextAsync(String text) {
        return checkTextAsync(text, Set.of(), Set.of());
    }

    public CompletableFuture<SpellerError[]> checkTextAsync(String text, Set<Language> languages) {
        return checkTextAsync(text, languages, Set.of());
    }

    public CompletableFuture<SpellerError[]> checkTextAsync(String text, Set<Language> languages, Set<Option> options) {
        var path = "https://speller.yandex.net/services/spellservice.json/checkText";
        var uri = URI.create(path + buildQuery(text, languages, options));
        var request = HttpRequest.newBuilder(uri).build();

        return client
                .sendAsync(request, BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return mapper.readValue(response.body(), SpellerError[].class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return new SpellerError[0];
                });
    }

    private static String buildQuery(String text, Collection<Language> languages, Collection<Option> options) {
        var encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        var langParam = languages.stream()
                .map(Speller::langToStr)
                .collect(Collectors.joining(","));

        var optionsParam = options.stream()
                .mapToInt(Speller::optionToInt)
                .sum();

        return String.join("",
                "?text=", encodedText,
                "&lang=", langParam,
                "&options=", String.valueOf(optionsParam));
    }

    private static String langToStr(Language lang) {
        switch (lang) {
            case EN:
                return "en";
            case RU:
                return "ru";
            case UK:
                return "uk";
        }
        return null;
    }

    private static int optionToInt(Option option) {
        switch (option) {
            case IGNORE_DIGITS:
                return 2;
            case IGNORE_URLS:
                return 4;
            case FIND_REPEAT_WORDS:
                return 8;
            case IGNORE_CAPITALIZATION:
                return 512;
        }
        return 0;
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
}
