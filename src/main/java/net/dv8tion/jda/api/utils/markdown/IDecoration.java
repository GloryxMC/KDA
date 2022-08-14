package net.dv8tion.jda.api.utils.markdown;

public interface IDecoration {
    String apply(String text);

    static String around(String text, String around) {
        return around + text + around;
    }
}
