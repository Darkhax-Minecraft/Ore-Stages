package com.jarhax.oretiers.compat.crt;

import com.jarhax.oretiers.compat.crt.handlers.OreTiersCrT;
import minetweaker.MineTweakerAPI;

public class CompatCRT {
    
    public static void postInit(){
        MineTweakerAPI.registerClass(OreTiersCrT.class);
    }
}
