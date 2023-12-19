package fileio.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * Generic Printer class to be subclassed by more specialized printers.
 */
public abstract class Printer {
    protected ArrayNode output;
    protected final ObjectMapper mapper = new ObjectMapper();

    /* Constructor */
    public Printer(final ArrayNode output) {
        this.output = output;
    }
}
