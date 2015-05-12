/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package basedefense.client.renderer.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.Status;

/**
 * Provides an event listener used for rendering outdated version notices.
 * @author Johannes Donath <a href="mailto:johannesd@torchmind.com">johannesd@torchmind.com</a>
 */
@AllArgsConstructor
public class OutdatedVersionGUIRenderer {
        public static final String OUTDATED_MESSAGE = EnumChatFormatting.RED + "BaseDefense %s is available (%s installed)";
        private final String installed;
        private final String latest;

        /**
         * Handles {@link net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent}.
         * @param event The event.
         */
        @SubscribeEvent
        public void onGuiScreen (GuiScreenEvent.DrawScreenEvent.Post event) {
                if (!(event.gui instanceof GuiMainMenu)) return;

                GuiMainMenu mainMenu = ((GuiMainMenu) event.gui);
                FontRenderer font = Minecraft.getMinecraft ().fontRenderer;
                Status forgeStatus = ForgeVersion.getStatus ();
                String line = String.format (OUTDATED_MESSAGE, this.latest, this.installed);

                int lineWidth = font.getStringWidth (line);
                mainMenu.drawString (font, line, ((mainMenu.width / 2) - (lineWidth / 2)), ((mainMenu.width - lineWidth) > 400 ? (mainMenu.height - font.FONT_HEIGHT - 1) : 1), -1);
}
}
