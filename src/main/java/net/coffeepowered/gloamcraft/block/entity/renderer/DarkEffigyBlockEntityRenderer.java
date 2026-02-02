package net.coffeepowered.gloamcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.coffeepowered.gloamcraft.block.entity.DarkEffigyBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

public class DarkEffigyBlockEntityRenderer implements BlockEntityRenderer<DarkEffigyBlockEntity> {
    public DarkEffigyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DarkEffigyBlockEntity deBlockEntity, float dePartialTick, PoseStack dePoseStack,
                       MultiBufferSource deBufferSource, int dePackedLight, int dePackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        for (int i = 0; i < 4; i++) {

            ItemStack stack = deBlockEntity.inventory.getStackInSlot(i);
            if (stack.isEmpty()) continue;


            dePoseStack.pushPose();
            float xOffset = (i % 2 == 0) ? 0.3f : 0.7f;
            float zOffset = (i < 2) ? 0.3f : 0.7f;
            float yOffset = 1.1f;

            dePoseStack.translate(xOffset, yOffset, zOffset);

            //dePoseStack.translate(1f, 1f, 1f);
            dePoseStack.scale(0.4f, 0.4f, 0.4f);
            dePoseStack.mulPose(Axis.YP.rotationDegrees(deBlockEntity.getRenderingRotation()));

            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(deBlockEntity.getLevel(),
                    deBlockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, dePoseStack, deBufferSource, deBlockEntity.getLevel(), 1);
            dePoseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }



}

