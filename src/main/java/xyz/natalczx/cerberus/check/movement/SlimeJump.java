// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class SlimeJump extends Check
{
    public SlimeJump(final CerberusAntiCheat plugin) {
        super("SlimeJump", "SlimeJump", plugin);
        this.setMaxViolations(5);
    }
}
