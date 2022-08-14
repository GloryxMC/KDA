package net.dv8tion.jda.api.utils.markdown;

public enum DiscordDecoration implements IDecoration {
    ITALIC {
        @Override
        public String apply(String text) {
            return IDecoration.around(text, "_");
        }
    },
    BOLD {
        @Override
        public String apply(String text) {
            return IDecoration.around(text, "**");
        }
    },
    UNDERLINE {
        @Override
        public String apply(String text) {
            return IDecoration.around(text, "__");
        }
    },
    STRIKETHROUGH {
        @Override
        public String apply(String text) {
            return IDecoration.around(text, "~~");
        }
    }
}
