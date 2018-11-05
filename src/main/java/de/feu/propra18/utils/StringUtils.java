package de.feu.propra18.utils;

import java.util.logging.Logger;

public class StringUtils {
    private static final Logger log = Logger.getLogger(StringUtils.class.getName());

    /**
     * Hilfsfunktion zum String parsen
     * @param str String der eine Zahl sein könnte
     * @return TRUE wenn es gelingt eine Integer Wert zu parsen, FALSE wenn nicht.
     */
    public static boolean isNumeric(String str)
    {
        try
        {
            log.finest((String.format("logger called parsing of: %s", str)));
            int i = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
