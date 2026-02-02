package net.coffeepowered.gloamcraft.block.custom;

import com.mojang.serialization.MapCodec;
import net.coffeepowered.gloamcraft.block.ritualnetwork.DarkEffigyData;
import net.coffeepowered.gloamcraft.block.entity.DarkEffigyBlockEntity;
import net.coffeepowered.gloamcraft.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class DarkEffigyBlock extends BaseEntityBlock {
    public static final MapCodec<DarkEffigyBlock> CODEC = simpleCodec(DarkEffigyBlock::new);

    public DarkEffigyBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* BLOCK ENTITY */

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.DARKEFFIGY_BE.get().create(blockPos, blockState);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (!(level.getBlockEntity(pos) instanceof DarkEffigyBlockEntity be)) {
            return InteractionResult.PASS;
        }
        if(player.isCrouching() && !level.isClientSide()) {
            ((ServerPlayer) player).openMenu(new SimpleMenuProvider(be, Component.literal("Dark Effigy")), pos);
            return InteractionResult.CONSUME;
        }

        // INSERT (player holding item)
        if (!stack.isEmpty()) {
            int emptySlot = findFirstEmpty(be.inventory);
            if (emptySlot != -1) {
                ItemStack toInsert = stack.copyWithCount(1);
                if (be.inventory.insertItem(emptySlot, toInsert, false).isEmpty()) {
                    stack.shrink(1);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3); // Important for syncing
                    return InteractionResult.SUCCESS; // STOP HERE
                }
            }
            return InteractionResult.CONSUME; // Prevent extraction if hand was full but couldn't insert
        }

        // EXTRACT (player empty-handed)
        if (stack.isEmpty()) {
            int lastFilled = findLastFilled(be.inventory);
            if (lastFilled != -1) {
                ItemStack extracted = be.inventory.extractItem(lastFilled, 1, false);

                boolean addedToInv = player.getInventory().add(extracted);

                if (addedToInv) {
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                } else {
                    be.inventory.insertItem(lastFilled, extracted, false);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }


    private static int findFirstEmpty(ItemStackHandler inv) {
        for (int i = 0; i < inv.getSlots(); i++) {
            if (inv.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static int findLastFilled(ItemStackHandler inv) {
        for (int i = inv.getSlots() - 1; i >= 0; i--) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    //Save Data
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {

            DarkEffigyData data = DarkEffigyData.get(serverLevel);
            boolean tooClose = false;
            for (BlockPos existingPos : data.getEffigies().keySet()) {
                if (existingPos.equals(pos)) continue;
                // distanceToSqr is faster than distanceTo because it avoids square root math
                // 48 * 48 = 2304
                if (pos.distSqr(existingPos) < 2304) {
                    tooClose = true;
                    break;
                }
            }

            if (tooClose) {
                popResource(level, pos, new ItemStack(this)); // Drop the block
                level.removeBlock(pos, false); // Remove block
                //level.destroyBlock(pos, true);  //both?
                return;
            }

            DarkEffigyData.get(serverLevel).addEffigy(pos, "");
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
                DarkEffigyData.get(serverLevel).removeEffigy(pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}