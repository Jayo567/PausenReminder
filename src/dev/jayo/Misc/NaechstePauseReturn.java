package dev.jayo.Misc;

import java.time.Instant;
import java.util.ArrayList;

public record NaechstePauseReturn(
        String key,
        ArrayList<String> uhrzeit,
        Instant instant
) {
}
