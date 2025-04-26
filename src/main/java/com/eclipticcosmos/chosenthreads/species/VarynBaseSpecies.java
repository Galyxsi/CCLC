package com.eclipticcosmos.chosenthreads.species;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;

import java.util.Collection;

public abstract class VarynBaseSpecies {
    public final String identifier;
    public final Component description;
    public final Component displayName;
    public VarynBaseSpecies(String identifier, Component description, Component displayName)
    {
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
    }

    public abstract Collection<ResourceLocation> getModifierIds();

    public void removeModifiers(Player player) {
        for (ResourceLocation id : getModifierIds()) {
            for (AttributeInstance attr : player.getAttributes().getSyncableAttributes()) {
                attr.removeModifier(id);
            }
        }
    }

    public void onPlayerTick(Player player, Level level) {}
    public void onVillagerTradeOpen(Player player, Level level, Villager villager) {}
    public void onPlayerTradeWithVillager(Player player, Level level, AbstractVillager villager, MerchantOffer offer) {}
    public void onShieldBlock(Player player, Level level, DamageSource source, float blocked) {}
    public void onDamage(Player player, Level level) {}
    public void onAttack(Player player, Level level) {}
    public void onJump(Player player, Level level) {}
    public void onCrouch(Player player, Level level) {}


}
