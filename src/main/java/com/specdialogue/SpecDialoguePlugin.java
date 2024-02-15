package com.specdialogue;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.callback.ClientThread;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
	name = "Spec Dialogue",
	description = "Add overhead dialogue text to any special attack!",
	tags = "spec, special, special attack, fun"
)
public class SpecDialoguePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SpecDialogueConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ClientThread clientThread;


	private HashMap<String, String> weaponsToDialoguesMap = new HashMap<String,String>(); //again, map keys lowercase

	private ArrayList<String> invalidWeaps = new ArrayList<>();
	private int specPercent;

	@Override
	protected void startUp() throws Exception
	{
		computeMap();
		initInvalidWeaps();
		specPercent = -1;

		log.info("Spec Dialogue plugin started.");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Spec Dialogue plugin stopped.");
	}

	@Subscribe
	public void  onGameStateChanged(GameStateChanged event){
		if(event.getGameState() == GameState.LOGGED_IN){

		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event){
		if(!event.getGroup().equals("specdialogue")){
			return;
		}

		computeMap();
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() != VarPlayer.SPECIAL_ATTACK_PERCENT)
		{
			return;
		}

		int specPercent = event.getValue();
		if (this.specPercent == -1 || specPercent >= this.specPercent)
		{
			this.specPercent = specPercent;
			return;
		}

		//spec has been used

		this.specPercent = specPercent;
		Item currentWeapon = getCurrentWeapon();

		if(currentWeapon == null){return;}  // i hope this cannot happen... if the weapon is unequipped
		// next tick after spec

		String currentWeaponName = itemManager.
				getItemComposition(currentWeapon.getId()).getName();

		//all map keys lowercase
		//return if equipped weapon not in the list
		if(!weaponsToDialoguesMap.containsKey(currentWeaponName.toLowerCase())){return;}

		if(invalidWeaps.contains(currentWeaponName.toLowerCase()))
		{
			return;
		}

		//if present and valid
		String overheadText = weaponsToDialoguesMap.get(currentWeaponName.toLowerCase());

		client.getLocalPlayer().setOverheadText(overheadText);
		client.addChatMessage(ChatMessageType.PUBLICCHAT, client.getLocalPlayer().getName(), overheadText, null);
		client.getLocalPlayer().setOverheadCycle(120);

	}


	@Subscribe
	void onOverheadTextChanged(OverheadTextChanged event){

		if(!event.getActor().equals(client.getLocalPlayer())) {return;}

		//if player's overhead text
		String text = event.getOverheadText();
		boolean changeText = text.equalsIgnoreCase("Chop chop!")
				|| text.equalsIgnoreCase("Smashing!")
				|| text.equalsIgnoreCase("Here fishy fishies!")
				|| text.equalsIgnoreCase("Raarrrrrgggggghhhhhhh!")
				|| text.equalsIgnoreCase("For Camelot!");


		if(!changeText){ return; }


		//get weapon name
		Item currentWeapon = getCurrentWeapon();
		if(currentWeapon == null){return;}

		String currentWeaponName = itemManager.
				getItemComposition(currentWeapon.getId()).getName();

		if(!weaponsToDialoguesMap.containsKey(currentWeaponName.toLowerCase())){return;}
		//if weapon in list
		//set text
		event.getActor().setOverheadText(weaponsToDialoguesMap.get(currentWeaponName.toLowerCase()));
	}

	Item getCurrentWeapon(){

		final ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if(equipment == null) {return null;}

		final Item currentWeapon = equipment.getItem(EquipmentInventorySlot.WEAPON.getSlotIdx());
		if(currentWeapon == null){return null;}

		//only if wielding a weapon
		return currentWeapon;

	}

	void computeMap(){
		weaponsToDialoguesMap.clear();
		String[] wholePairs = config.weapsAndDialogue().split(";");
		for(String wholePair : wholePairs){

			String[] pair = wholePair.split(":");

			if(pair.length < 2){continue;} //if invalid pair

			String weapon = pair[0].trim();
			String dialogue = pair[1].trim();
			weaponsToDialoguesMap.put(weapon.toLowerCase(),dialogue); //map keys lowercase
		}

	}

	@Provides
	SpecDialogueConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SpecDialogueConfig.class);
	}

	//weapons that already have overhead dialogue need to be handled differently...ugh. I can't figure out
	//a different workaround for this.
	//invokeLater did not work either, though I might've missed something. It sounds like it should.
	//the weapon spec varbit changes much earlier, but using invokeLater, the original chat message for the
	//spec dialogue is still added. But its not when doing it this way. I'm surely missing something obvious but im too tired at this point
	void initInvalidWeaps()
	{
		invalidWeaps.add("dragon axe");
		invalidWeaps.add("infernal axe");
		invalidWeaps.add("3rd age axe");
		invalidWeaps.add("crystal axe");
		invalidWeaps.add("dragon harpoon");
		invalidWeaps.add("infernal harpoon");
		invalidWeaps.add("crystal harpoon");
		invalidWeaps.add("dragon pickaxe");
		invalidWeaps.add("infernal pickaxe");
		invalidWeaps.add("3rd age pickaxe");
		invalidWeaps.add("crystal pickaxe");
		invalidWeaps.add("dragon battleaxe");
		invalidWeaps.add("excalibur");
	}
}
