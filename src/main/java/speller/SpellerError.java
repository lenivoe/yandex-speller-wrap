package speller;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SpellerError {
    public enum StatusCode {
        ERROR_UNKNOWN_WORD,
        ERROR_REPEAT_WORD,
        ERROR_CAPITALIZATION,
        ERROR_TOO_MANY_ERRORS
    }

    public StatusCode getStatusCode() {
        return StatusCode.values()[this.code - 1];
    }

    int code;
    int pos;
    int row;
    int col;
    int len;
    String word;
    String[] s;
}