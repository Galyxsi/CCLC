package com.eclipticcosmos.chosenthreads.species;

import dev.shadowsoffire.apothic_attributes.api.ALObjects;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;

public class VarynBira extends VarynBaseSpecies {

    public static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("chosenthreads", "bira_damage");
    public static final ResourceLocation ARMOR_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("chosenthreads", "bira_armor");
    public static final ResourceLocation PROJ_VELOCITY_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("chosenthreads", "bira_projectile");
    public static final ResourceLocation JUMP_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("chosenthreads", "bira_jump");
    public static final ResourceLocation MINING_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("chosenthreads", "bira_mining");
    public static final ResourceLocation SPEED_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("chosenthreads", "bira_speed");

    public VarynBira()
    {

        super("bira", Component.literal("A standard humanoid."), Component.literal("Bira"));
    }

    @Override
    public Collection<ResourceLocation> getModifierIds() {
        return List.of(ATTACK_DAMAGE_MODIFIER_ID, ARMOR_MODIFIER_ID, PROJ_VELOCITY_MODIFIER_ID, JUMP_MODIFIER_ID, MINING_MODIFIER_ID, SPEED_MODIFIER_ID);
    }

    @Override
    public void onPlayerTick(Player player, Level level)
    {

        addAttribute(player, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_ID, 2.0, AttributeModifier.Operation.ADD_VALUE);
        addAttribute(player, Attributes.ARMOR, ARMOR_MODIFIER_ID, 2.0, AttributeModifier.Operation.ADD_VALUE);
        addAttribute(player, ALObjects.Attributes.ARROW_VELOCITY, PROJ_VELOCITY_MODIFIER_ID, 0.5, AttributeModifier.Operation.ADD_VALUE);
        addAttribute(player, Attributes.JUMP_STRENGTH, JUMP_MODIFIER_ID, 0.05, AttributeModifier.Operation.ADD_VALUE);
        addAttribute(player, ALObjects.Attributes.MINING_SPEED, MINING_MODIFIER_ID, 0.05, AttributeModifier.Operation.ADD_VALUE);
        addAttribute(player, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_ID, 0.02, AttributeModifier.Operation.ADD_VALUE);
    }

    @Override
    public void onVillagerTradeOpen(Player player, Level level, Villager villager)
    {

        for (MerchantOffer offer : villager.getOffers())
        {
            ItemStack original = offer.getCostA();
            if (original.getItem() == Items.EMERALD)
            {
                if (original.getCount() < 45)
                {
                    int increasedPrice = (int) Math.ceil(original.getCount() * 0.5);

                    increasedPrice = Math.min(increasedPrice, 45 - original.getCount());

                    offer.resetSpecialPriceDiff();
                    offer.addToSpecialPriceDiff(increasedPrice);


                }

            }
        }
    }

    @Override
    public void onPlayerTradeWithVillager(Player player, Level level, AbstractVillager villager, MerchantOffer offer) {
        offer.setToOutOfStock();
        offer.updateDemand();
    }

    @Override
    public void onShieldBlock(Player player, Level level, DamageSource source, float blocked)
    {
        player.heal(2);
    }


    public void addAttribute(Player player, Holder<Attribute> attribute, ResourceLocation modifierId, double amount, AttributeModifier.Operation operation)
    {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance != null)
        {
            attributeInstance.removeModifier(modifierId);
            attributeInstance.addPermanentModifier(new AttributeModifier(
                    modifierId, amount, operation
            ));
        }
    }
}
