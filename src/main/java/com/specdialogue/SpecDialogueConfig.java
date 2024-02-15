package com.specdialogue;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("specdialogue")
public interface SpecDialogueConfig extends Config {
    @ConfigItem(
            keyName = "weapsAndDialogue",
            name = "Weapons/Dialogue",
            description = "Enter the spec weapon name and dialogue in name,colon,dialogue" +
                    ",semicolon format --- Dragon axe : Chop chop!;"

    )
    default String weapsAndDialogue() {
        return "Dont forget the semicolon \";\" at the end of every entry, damnit ;\n" +
                "\n" +
                "Toxic Blowpipe: pew ;\n" +
                "\n" +
                "Dragon Dagger: Measure twice! ;\n" +
                "\n" +
                "Granite Hammer : Bonk ;\n" +
                "\n" +
                "Dragon Warhammer: Bonk. ;\n" +
                "\n" +
                "Granite Maul: S M A S H I N G ! ;";
    }

}
