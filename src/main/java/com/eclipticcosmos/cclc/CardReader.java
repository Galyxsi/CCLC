package com.eclipticcosmos.cclc;

import com.mojang.serialization.MapCodec;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.IEasyEntityBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.RotatableBlock;
import io.github.lightman314.lightmanscurrency.common.blockentity.variant.IVariantSupportingBlockEntity;
import io.github.lightman314.lightmanscurrency.common.blocks.variant.IEasyVariantBlock;
import io.github.lightman314.lightmanscurrency.common.blocks.variant.IVariantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class CardReader extends RotatableBlock implements EntityBlock, IEasyEntityBlock/*, IVariantBlock*/ {

    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 2.0D, 14.0D, 8.0D, 14.0D);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CardReader(Properties properties) {super(properties, SHAPE); this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        //builder.add(VARIANT);
    }

    /*@Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }*/

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Nonnull
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {return RenderShape.MODEL; }


    @Override
    public void setPlacedBy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity player, @Nonnull ItemStack stack) {
        this.tryCopyVariant(level,pos,stack);
        if (level.getBlockEntity(pos) instanceof BlockEntityCardReader be)
        {
            if(stack.has(DataComponents.CUSTOM_NAME))
            {
                be.setCustomName(stack.getHoverName());
            }
        }
        super.setPlacedBy(level, pos, state, player, stack);
        this.tryCopyVariant(level,pos,stack);
    }

    @Nonnull
    @Override
    protected InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult hit)
    {
        if(!level.isClientSide && level.getBlockEntity(pos) instanceof BlockEntityCardReader be)
        {
            if(be.allowAccess(player))
            {
                player.openMenu(BlockEntityCardReader.getMenuProvider(be), pos);
            }
        }
        return InteractionResult.CONSUME;
    }

    /*@Nonnull
    @Override
    public BlockState playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
        if (level.getBlockEntity(pos) instanceof BlockEntityCardReader be)
        {

        }
        return super.playerWillDestroy(level, pos, state, player);
    }*/

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean flag)
    {
        if(level.getBlockEntity(pos) instanceof BlockEntityCardReader be)
        {
            Containers.dropContents(level, pos, be.getStorage());
        }
        super.onRemove(state, level, pos, newState, flag);
    }

    @Override
    public @NotNull Collection<BlockEntityType<?>> getAllowedTypes() {
        return Collections.singleton(ModBlockEntities.CARD_READER.get());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new BlockEntityCardReader(pos, state);
    }

    /*@Override
    public boolean triggerEvent(@Nonnull BlockState p_49226_, @Nonnull Level p_49227_, @Nonnull BlockPos p_49228_, int p_49229_, int p_49230_) {
        super.triggerEvent(p_49226_, p_49227_, p_49228_, p_49229_, p_49230_);
        BlockEntity blockentity = p_49227_.getBlockEntity(p_49228_);
        return blockentity != null && blockentity.triggerEvent(p_49229_, p_49230_);
    }*/

}
