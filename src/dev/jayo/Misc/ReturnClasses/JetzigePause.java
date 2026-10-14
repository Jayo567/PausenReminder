package dev.jayo.Misc.ReturnClasses;

import java.time.Instant;
import java.util.ArrayList;

public record JetzigePause(
        String pausenName,
        ArrayList<String> uhrzeit,
        Instant instant
) {
}
