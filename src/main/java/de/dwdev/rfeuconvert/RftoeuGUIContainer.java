package de.dwdev.rfeuconvert;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class RftoeuGUIContainer extends GuiContainer {
	public static final int WIDTH = 175;
	public static final int HEIGHT = 165;
	private static final ResourceLocation background = new ResourceLocation(Rfeuconvert.MODID,
			"textures/gui/rftoeu.png");

	public RftoeuGUIContainer(TileRFtoEU te, RftoeuContainer container) {
		super(container);

		xSize = WIDTH;
		ySize = HEIGHT;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		mc.getTextureManager().bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
	}

}
