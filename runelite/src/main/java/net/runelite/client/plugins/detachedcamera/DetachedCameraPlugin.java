package net.runelite.client.plugins.detachedcamera;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "Detached Camera",
        description = "Allows free roaming the camera",
        enabledByDefault = false
)
public class DetachedCameraPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private DetachedCameraConfig config;

    @Inject
    private KeyManager keyManager;

    private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.detachedCameraHotkey())
    {
        @Override
        public void hotkeyPressed()
        {
            boolean cameraDetached = (client.getOculusOrbState() == 1);
            if (cameraDetached)
            {
                attachCamera();
            }
            else
            {
                detachCamera();
            }
        }
    };

    @Override
    protected void startUp()
    {
        keyManager.registerKeyListener(hotkeyListener);
        detachCamera();
    }

    @Override
    protected void shutDown()
    {
        keyManager.unregisterKeyListener(hotkeyListener);
        attachCamera();
    }

    private void detachCamera()
    {
        client.setOculusOrbState(1);
        client.setOculusOrbNormalSpeed(36);
    }

    private void attachCamera()
    {
        client.setOculusOrbState(0);
        client.setOculusOrbNormalSpeed(12);
    }

    @Provides
    DetachedCameraConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(DetachedCameraConfig.class);
    }
}