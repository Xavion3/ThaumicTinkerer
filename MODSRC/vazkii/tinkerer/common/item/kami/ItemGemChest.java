/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the ThaumicTinkerer Mod.
 * 
 * ThaumicTinkerer is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * ThaumicTinkerer is a Derivative Work on Thaumcraft 4.
 * Thaumcraft 4 (c) Azanor 2012
 * (http://www.minecraftforum.net/topic/1585216-)
 * 
 * File Created @ [Dec 26, 2013, 4:18:27 PM (GMT)]
 */
package vazkii.tinkerer.common.item.kami;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import vazkii.tinkerer.client.model.ModelWings;
import vazkii.tinkerer.common.item.ModItems;

public class ItemGemChest extends ItemIchorclothArmorAdv {

	public static List<String> playersWithFlight = new ArrayList();
	
	public ItemGemChest(int par1, int par2) {
		super(par1, par2);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
		return new ModelWings();
	}
	
	@ForgeSubscribe
	public void updatePlayerFlyStatus(LivingUpdateEvent event) {
		EntityPlayer player = (EntityPlayer) event.entityLiving;
		
		ItemStack armor = player.getCurrentArmor(3 - armorType);
		if(armor != null && armor.getItem() == this)
			tickPlayer(player);
		
		if(playersWithFlight.contains(playerStr(player)))
			if(shouldPlayerHaveFlight(player))
				player.capabilities.allowFlying = true;
			else {
				player.capabilities.allowFlying = false;
				if(!player.capabilities.isCreativeMode)
					player.capabilities.isFlying = false;
				player.capabilities.disableDamage = false;
				playersWithFlight.remove(playerStr(player));
			}
		else if(shouldPlayerHaveFlight(player)) {
			playersWithFlight.add(playerStr(player));
			player.capabilities.allowFlying = true;
		}
	}

	public static String playerStr(EntityPlayer player) {
		return player.username + ":" + player.worldObj.isRemote;
	}
	
	private static boolean shouldPlayerHaveFlight(EntityPlayer player) {
		ItemStack armor = player.getCurrentArmor(2);
		return armor != null && armor.itemID == ModItems.ichorChestGem.itemID;
	}
	
}
