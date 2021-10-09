// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CerberusLogger
{
    private static final Logger LOGGER;
    
    public static void log(final Level level, final String message, final Object... params) {
        CerberusLogger.LOGGER.log(level, message, params);
    }
    
    public static void exception(final String message, final Exception ex) {
        CerberusLogger.LOGGER.log(Level.WARNING, message, ex);
    }
    
    static {
        LOGGER = CerberusAntiCheat.getInstance().getLogger();
    }
}
