package ru.wca.rf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static ru.wca.rf.Main.*;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * The method of executing the program.
     */
    public static void main(String[] args) {
        try {
            log.debug("START");
            renameFiles();
            log.debug("SUCCESS");
        } catch (IOException e) {
            log.debug("FAILURE");
        }
    }
}
