/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.shedaniel.autoconfig.AutoConfig
 *  me.shedaniel.autoconfig.serializer.GsonConfigSerializer
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1713
 *  net.minecraft.class_1743
 *  net.minecraft.class_1753
 *  net.minecraft.class_1764
 *  net.minecraft.class_1792
 *  net.minecraft.class_1794
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1810
 *  net.minecraft.class_1819
 *  net.minecraft.class_1821
 *  net.minecraft.class_1829
 *  net.minecraft.class_1835
 *  net.minecraft.class_238
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_2596
 *  net.minecraft.class_2828$class_2829
 *  net.minecraft.class_2828$class_2831
 *  net.minecraft.class_2848
 *  net.minecraft.class_2848$class_2849
 *  net.minecraft.class_2868
 *  net.minecraft.class_304
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_3675$class_307
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_9362
 */
package com.Cna4ik.maceswap.client;

import com.Cna4ik.maceswap.client.DiscordPromptScreen;
import com.Cna4ik.maceswap.client.TargetAssist;
import com.Cna4ik.maceswap.config.MaceSwapConfig;
import java.util.Optional;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1743;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1792;
import net.minecraft.class_1794;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1810;
import net.minecraft.class_1819;
import net.minecraft.class_1821;
import net.minecraft.class_1829;
import net.minecraft.class_1835;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2828;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3675;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_9362;

@Environment(value=EnvType.CLIENT)
public class MaceSwapClient
implements ClientModInitializer {
    private static boolean promptChecked = false;
    private static class_304 toggleKey;
    private static class_304 axeToggleKey;
    public static boolean isAutoMaceEnabled;
    public static boolean wasWhiteModeBeforeAim;
    public static boolean isPerformingInternalAttack;
    private static int swapStep;
    private static int queuedOriginalSlot;
    private boolean wasUsePressed = false;
    private boolean wasAttackPressed = false;
    private static int axeSwapState;
    private static int axeOriginalSlot;
    private static int axeMaceSlot;
    private static class_1297 queuedAxeTarget;
    private static boolean wasOnGround;
    private static float trackedFallDistance;
    private static String displayText = "LyricaClient";
    private static int displayDotColor = 0xFF00FF;

    public void onInitializeClient() {
        AutoConfig.register(MaceSwapConfig.class, GsonConfigSerializer::new);
        String category = "key.category.maceswap.keys";
        toggleKey = KeyBindingHelper.registerKeyBinding((class_304)new class_304("key.maceswap.swap", class_3675.class_307.field_1668, 82, category));
        axeToggleKey = KeyBindingHelper.registerKeyBinding((class_304)new class_304("key.maceswap.axe_swap", class_3675.class_307.field_1668, 71, category));
        HudRenderCallback.EVENT.register((drawContext, renderTickCounter) -> {
            MaceSwapConfig config = (MaceSwapConfig)AutoConfig.getConfigHolder(MaceSwapConfig.class).getConfig();
            class_310 client = class_310.method_1551();
            if (client.field_1724 != null && client.field_1687 != null) {
                TargetAssist.tick(client, config);
            }
            if (config.displayMode == MaceSwapConfig.DisplayMode.LYRICA_CLIENT) {
                if (TargetAssist.isActive) {
                    drawContext.method_25303(client.field_1772, displayText + ".", 10, 10, displayDotColor);
                } else if (isAutoMaceEnabled) {
                    drawContext.method_25303(client.field_1772, displayText + ".", 10, 10, displayDotColor);
                } else {
                    drawContext.method_25303(client.field_1772, displayText, 10, 10, -1);
                }
            }
        });
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!promptChecked && client.field_1755 instanceof class_442) {
                MaceSwapConfig conf = (MaceSwapConfig)AutoConfig.getConfigHolder(MaceSwapConfig.class).getConfig();
                if (!conf.hasShownDiscordPrompt) {
                    client.method_1507((class_437)new DiscordPromptScreen(client.field_1755));
                }
                promptChecked = true;
            }
            if (client.field_1724 == null || client.field_1687 == null) {
                return;
            }
            MaceSwapConfig config = (MaceSwapConfig)AutoConfig.getConfigHolder(MaceSwapConfig.class).getConfig();
            if (TargetAssist.isActive && TargetAssist.lockedTarget != null && (!TargetAssist.lockedTarget.method_5805() || TargetAssist.lockedTarget.method_31481())) {
                TargetAssist.isActive = false;
                TargetAssist.lockedTarget = null;
                isAutoMaceEnabled = wasWhiteModeBeforeAim;
            }
            boolean useJustPressed = client.field_1690.field_1904.method_1434() && !this.wasUsePressed;
            boolean currentAttackPressed = client.field_1690.field_1886.method_1434();
            boolean attackJustPressed = currentAttackPressed && !this.wasAttackPressed;
            boolean isOnGround = client.field_1724.method_24828();
            if (axeSwapState == 2) {
                if (axeOriginalSlot != -1 && client.field_1761 != null) {
                    client.field_1724.method_31548().field_7545 = axeOriginalSlot;
                }
                axeSwapState = 0;
                axeOriginalSlot = -1;
                axeMaceSlot = -1;
                queuedAxeTarget = null;
            } else if (axeSwapState == 1) {
                if (queuedAxeTarget != null && queuedAxeTarget.method_5805() && client.field_1761 != null) {
                    int maceSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        if (!client.field_1724.method_31548().method_5438(i).method_31574(class_1802.field_49814)) continue;
                        maceSlot = i;
                        break;
                    }
                    if (maceSlot != -1) {
                        axeOriginalSlot = client.field_1724.method_31548().field_7545;
                        axeMaceSlot = maceSlot;
                        axeSwapState = 2;
                        client.field_1724.method_31548().field_7545 = axeMaceSlot;
                        isPerformingInternalAttack = true;
                        try {
                            client.field_1761.method_2918((class_1657)client.field_1724, queuedAxeTarget);
                        }
                        finally {
                            isPerformingInternalAttack = false;
                        }
                        client.field_1724.method_6104(class_1268.field_5808);
                        if (config.autoReturnToWeaponEnabled) {
                            client.field_1724.method_31548().field_7545 = axeOriginalSlot;
                        }
                    } else {
                        axeSwapState = 0;
                        queuedAxeTarget = null;
                    }
                } else {
                    axeSwapState = 0;
                    queuedAxeTarget = null;
                }
            }
            if (swapStep == 1) {
                if (queuedOriginalSlot != -1) {
                    client.field_1724.method_31548().field_7545 = queuedOriginalSlot;
                }
                swapStep = 0;
                queuedOriginalSlot = -1;
            }
            while (axeToggleKey.method_1436()) {
                if (!config.allowAxeSwapHotkey) continue;
                config.axeSwapEnabled = !config.axeSwapEnabled;
                AutoConfig.getConfigHolder(MaceSwapConfig.class).save();
            }
            while (toggleKey.method_1436()) {
                if (TargetAssist.isActive) {
                    TargetAssist.isActive = false;
                    TargetAssist.lockedTarget = null;
                    isAutoMaceEnabled = wasWhiteModeBeforeAim;
                    continue;
                }
                boolean aimActivated = false;
                class_1297 target = MaceSwapClient.getLookingAtEntity(client, config);
                if (target != null) {
                    wasWhiteModeBeforeAim = isAutoMaceEnabled;
                    isAutoMaceEnabled = true;
                    TargetAssist.isActive = true;
                    TargetAssist.lockedTarget = target;
                    aimActivated = true;
                }
                if (aimActivated) continue;
                boolean bl = isAutoMaceEnabled = !isAutoMaceEnabled;
            }
            wasOnGround = isOnGround;
            trackedFallDistance = !isOnGround ? client.field_1724.field_6017 : 0.0f;
            this.wasUsePressed = client.field_1690.field_1904.method_1434();
            this.wasAttackPressed = currentAttackPressed;
        });
    }

    private static void aimAtTarget(class_310 client, class_1297 target) {
        if (target == null || !target.method_5805() || target.method_31481()) {
            return;
        }
        double dx = target.method_23317() - client.field_1724.method_23317();
        double dy = target.method_23320() - client.field_1724.method_23320();
        double dz = target.method_23321() - client.field_1724.method_23321();
        double diffXZ = Math.sqrt(dx * dx + dz * dz);
        float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        float targetPitch = (float)(-Math.toDegrees(Math.atan2(dy, diffXZ)));
        client.field_1724.method_36456(targetYaw);
        client.field_1724.method_36457(targetPitch);
        if (client.method_1562() != null) {
            client.method_1562().method_52787((class_2596)new class_2828.class_2831(targetYaw, targetPitch, client.field_1724.method_24828()));
        }
    }

    public static boolean isWeaponOrTool(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return false;
        }
        class_1792 item = stack.method_7909();
        return item instanceof class_1829 || item instanceof class_1743 || item instanceof class_1810 || item instanceof class_1821 || item instanceof class_1794 || item instanceof class_1835;
    }

    public static boolean isMeleeWeaponOrEmptyOrWindCharge(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return true;
        }
        class_1792 item = stack.method_7909();
        return item instanceof class_1829 || item instanceof class_1743;
    }

    private static int findInHotbar(class_1661 inv, class_1792 item) {
        for (int i = 0; i < 9; ++i) {
            if (!inv.method_5438(i).method_31574(item)) continue;
            return i;
        }
        return -1;
    }

    private static int findChestplateInHotbar(class_1661 inv) {
        for (int i = 0; i < 9; ++i) {
            class_1792 item = inv.method_5438(i).method_7909();
            if (item != class_1802.field_22028 && item != class_1802.field_8058 && item != class_1802.field_8523 && item != class_1802.field_8678 && item != class_1802.field_8873) {
                return i;
            }
        }
        return -1;
    }

    public static boolean hasLineOfSight(class_310 client, class_1297 target) {
        class_243 end;
        if (client.field_1724 == null || client.field_1687 == null) {
            return false;
        }
        class_243 start = client.field_1724.method_5836(1.0f);
        class_3959 context = new class_3959(start, end = new class_243(target.method_23317(), target.method_23320(), target.method_23321()), class_3959.class_3960.field_17558);
        return client.field_1687.method_17742(context).method_17783() == class_239.class_240.field_1333;
    }

    private static class_1297 getLookingAtEntity(class_310 client, MaceSwapConfig config) {
        if (client.field_1724 == null || client.field_1687 == null) {
            return null;
        }
        double maxDistance = config.aimDistance;
        boolean playerOnly = config.playerOnlyAim;
        boolean smartLock = config.smartLockEnabled;
        class_243 cameraPos = client.field_1724.method_5836(1.0f);
        class_243 rotation = client.field_1724.method_5828(1.0f);
        class_243 endPos = cameraPos.method_1031(rotation.field_1352 * maxDistance, rotation.field_1351 * maxDistance, rotation.field_1350 * maxDistance);
        class_238 box = client.field_1724.method_5829().method_18804(rotation.method_1021(maxDistance)).method_1009(1.0, 1.0, 1.0);
        class_1297 closestEntity = null;
        double bestScore = Double.MAX_VALUE;
        for (class_1297 entity : client.field_1687.method_8333((class_1297)client.field_1724, box, e -> e instanceof class_1309 && e.method_5805() && (!playerOnly || e instanceof class_1657))) {
            double dist;
            class_238 entityBox = entity.method_5829().method_1014(0.1);
            Optional hit = entityBox.method_992(cameraPos, endPos);
            if (smartLock) {
                class_1309 le;
                double score;
                class_243 dir = new class_243(entity.method_23317(), entity.method_23318(), entity.method_23321()).method_1020(cameraPos).method_1029();
                if (!(rotation.method_1026(dir) > 0.8) || !MaceSwapClient.hasLineOfSight(client, entity) || !((score = (double)((le = (class_1309)entity).method_6032() + (float)le.method_6096())) < bestScore)) {
                    continue;
                }
                bestScore = score;
                closestEntity = entity;
                continue;
            }
            if (!hit.isPresent() || !MaceSwapClient.hasLineOfSight(client, entity) || !((dist = cameraPos.method_1025((class_243)hit.get())) < bestScore)) continue;
            bestScore = dist;
            closestEntity = entity;
        }
        return closestEntity;
    }

    public static boolean handleGhostSwapAttack(class_1657 player, class_1297 target) {
        if (isPerformingInternalAttack) {
            return false;
        }
        if (player.method_6047().method_31574(class_1802.field_49814)) {
            return false;
        }
        if (!isAutoMaceEnabled) {
            return false;
        }
        MaceSwapConfig config = (MaceSwapConfig)AutoConfig.getConfigHolder(MaceSwapConfig.class).getConfig();
        if (config.swapItemsMode == MaceSwapConfig.SwapItemsMode.ONLY_WEAPONS && !MaceSwapClient.isWeaponOrTool(player.method_6047())) {
            return false;
        }
        if (config.axeSwapEnabled && player.method_6047().method_7909() instanceof class_1743) {
            boolean isBlocking = false;
            if (target instanceof class_1309) {
                class_1309 livingTarget = (class_1309)target;
                isBlocking = livingTarget.method_6039();
            }
            if (isBlocking) {
                if (axeSwapState == 0) {
                    queuedAxeTarget = target;
                    axeSwapState = 1;
                }
                return false;
            }
        }
        class_1661 inv = player.method_31548();
        int maceSlot = -1;
        for (int i = 0; i < 9; ++i) {
            if (!inv.method_5438(i).method_31574(class_1802.field_49814)) continue;
            maceSlot = i;
            break;
        }
        if (maceSlot != -1) {
            class_310 client = class_310.method_1551();
            int originalSlot = inv.field_7545;
            if (swapStep == 0 && client.field_1761 != null) {
                class_1309 livingTarget;
                queuedOriginalSlot = originalSlot;
                swapStep = 1;
                inv.field_7545 = maceSlot;
                boolean bypassed = false;
                class_243 originalPos = new class_243(player.method_23317(), player.method_23318(), player.method_23321());
                if (config.bypassShieldEnabled && target instanceof class_1309 && (livingTarget = (class_1309)target).method_6039()) {
                    class_243 targetLook = target.method_5828(1.0f);
                    class_243 behindPos = new class_243(target.method_23317(), target.method_23318(), target.method_23321()).method_1020(targetLook.method_1021(1.5));
                    client.method_1562().method_52787((class_2596)new class_2828.class_2829(behindPos.field_1352, behindPos.field_1351, behindPos.field_1350, player.method_24828()));
                    bypassed = true;
                }
                isPerformingInternalAttack = true;
                try {
                    client.field_1761.method_2918(player, target);
                }
                finally {
                    isPerformingInternalAttack = false;
                }
                player.method_6104(class_1268.field_5808);
                if (bypassed) {
                    client.method_1562().method_52787((class_2596)new class_2828.class_2829(originalPos.field_1352, originalPos.field_1351, originalPos.field_1350, player.method_24828()));
                }
                return true;
            }
        }
        return false;
    }

    static {
        isAutoMaceEnabled = false;
        wasWhiteModeBeforeAim = false;
        isPerformingInternalAttack = false;
        swapStep = 0;
        queuedOriginalSlot = -1;
        axeSwapState = 0;
        axeOriginalSlot = -1;
        axeMaceSlot = -1;
        queuedAxeTarget = null;
        wasOnGround = true;
        trackedFallDistance = 0.0f;
    }
}
