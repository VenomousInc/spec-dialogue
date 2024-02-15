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
                    ",semicolon format --- Dragon axe : Chop chop! ;"

    )
    default String weapsAndDialogue() {
        return "The semicolon is really easy to forget lol ;\n" +
                "\n" +
                "Armadyl Crossbow : Oh, a bird's nest. ;\n" +
                "\n" +
                "Dragon Dagger : Measure twice... ;\n" +
                "\n" +
                "Dragon Claws : Meeooowwww! ; \n" +
                "\n" +
                "Dragon Warhammer : Bonk. ;\n" +
                "\n" +
                "Dragon Warhammer (cr) : Bonk ; \n" +
                "\n" +
                "Granite Maul : BASHING! ;\n" +
                "\n" +
                "Saradomin Sword : Leodero! ;\n" +
                "\n" +
                "Blessed Saradomin sword : Leodero! ;\n" +
                "\n" +
                "Toxic Blowpipe : pew ;\n" +
                "\n" +
                "Zamorak Godword : Freeze! ;\n" +
                "\n" +
                "\n" +
                "\n";
    }

}
