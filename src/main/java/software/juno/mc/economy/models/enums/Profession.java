package software.juno.mc.economy.models.enums;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import software.juno.mc.economy.utils.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//    METALLURGIST

public enum Profession {

    UNEMPLOYED("Desempregado",
            null,
            null,
            // Start items
            new ItemStack[]{
                    new ItemStack(Material.EMERALD, 20),
                    new ItemStack(Material.STONE_SWORD, 1),
                    new ItemStack(Material.BREAD, 5),
                    ItemUtils.transportTicket(),
                    ItemUtils.transportTicket(),
                    ItemUtils.transportTicket(),
                    ItemUtils.transportTicket(),
                    ItemUtils.transportTicket()
            },
            null,
            null,
            null,
            // Villager skin
            Villager.Profession.NONE),
    FARMER("Agricultor",
            // Craftable items
            new Material[]{
                    Material.BONE_MEAL,
                    Material.BONE_BLOCK
            },
            // Breakable items
            new Material[]{
                    Material.WHEAT,
                    Material.DEAD_BUSH,
                    Material.SAND,
                    Material.RED_SAND,
                    Material.GRASS_BLOCK,
                    Material.GRASS,
                    Material.CACTUS,
                    Material.DANDELION
            },
            // Start items
            new ItemStack[]{
                    new ItemStack(Material.IRON_HOE, 1),
                    new ItemStack(Material.WHEAT_SEEDS, 5),
                    new ItemStack(Material.MELON_SEEDS, 5),
                    new ItemStack(Material.BEETROOT_SEEDS, 5),
                    new ItemStack(Material.PUMPKIN_SEEDS, 5)
            },
            null,
            null,
            null,
            // Villager skin
            Villager.Profession.FARMER),
    METALLURGIST("Metalurgico", null, null, null, null, null, null, Villager.Profession.ARMORER),
    AGRICULTURIST("Agricultor", null, null, null, null, null, null, Villager.Profession.SHEPHERD),
    LUMBERJACK("Lenhador",
            new Material[]{
                    Material.ACACIA_PLANKS,
                    Material.BIRCH_PLANKS,
                    Material.CRIMSON_PLANKS,
                    Material.DARK_OAK_PLANKS,
                    Material.JUNGLE_PLANKS,
                    Material.OAK_PLANKS,
                    Material.SPRUCE_PLANKS,
                    Material.WARPED_PLANKS,
                    Material.COAL_ORE,
                    Material.COAL_BLOCK
            },
            new Material[]{
                    Material.ACACIA_LOG,
                    Material.ACACIA_LEAVES,
                    Material.BIRCH_LOG,
                    Material.BIRCH_LEAVES,
                    Material.DARK_OAK_LOG,
                    Material.DARK_OAK_LEAVES,
                    Material.JUNGLE_LOG,
                    Material.JUNGLE_LEAVES,
                    Material.OAK_LOG,
                    Material.OAK_LEAVES,
                    Material.SPRUCE_LOG,
                    Material.SPRUCE_LEAVES
            },
            new ItemStack[]{
                    new ItemStack(Material.IRON_AXE, 1)
            },
            new Material[]{
                    Material.COAL, Material.COAL_ORE, Material.CHARCOAL
            },
            null,
            new MerchantRecipe[][]{
                    createRecipe(1, new ItemStack(Material.CHARCOAL, 25)),
                    createRecipe(1, new ItemStack(Material.ACACIA_LOG, 15)),
                    createRecipe(1, new ItemStack(Material.BIRCH_LOG, 15)),
                    createRecipe(1, new ItemStack(Material.DARK_OAK_LOG, 15)),
                    createRecipe(1, new ItemStack(Material.JUNGLE_LOG, 15)),
                    createRecipe(1, new ItemStack(Material.OAK_LOG, 15)),
                    createRecipe(1, new ItemStack(Material.SPRUCE_LOG, 15)),
                    createRecipe(2, new ItemStack(Material.ACACIA_PLANKS, 15)),
                    createRecipe(2, new ItemStack(Material.BIRCH_PLANKS, 15)),
                    createRecipe(2, new ItemStack(Material.DARK_OAK_PLANKS, 15)),
                    createRecipe(2, new ItemStack(Material.JUNGLE_PLANKS, 15)),
                    createRecipe(2, new ItemStack(Material.OAK_PLANKS, 15)),
                    createRecipe(5, new ItemStack(Material.CRIMSON_PLANKS, 3)),
                    createRecipe(5, new ItemStack(Material.SPRUCE_PLANKS, 3)),
                    createRecipe(5, new ItemStack(Material.WARPED_PLANKS, 3)),
            },
            Villager.Profession.TOOLSMITH),
    MINER("Minerador",
            null,
            null,
            new ItemStack[]{
                    new ItemStack(Material.IRON_PICKAXE)
            },
            null,
            null,
            null,
            Villager.Profession.ARMORER),
    COOK("Cozinheiro", new Material[]{Material.CAKE, Material.CANDLE_CAKE, Material.BLACK_CANDLE_CAKE}, null, null, new Material[]{
            Material.COOKED_CHICKEN,
            Material.COOKED_BEEF,
            Material.COOKED_COD,
            Material.COOKED_MUTTON, Material.COOKED_PORKCHOP,
            Material.COOKED_RABBIT,
            Material.COOKED_SALMON,
            Material.COOKIE
    }, null, null, Villager.Profession.FLETCHER),
    WARRIOR("Guerreiro", null, null, new ItemStack[]{
            new ItemStack(Material.IRON_SWORD, 1)
    }, null, null, null, Villager.Profession.BUTCHER),
    WIZARD("Bruxo", new Material[]{Material.POTION}, null, new ItemStack[]{
            new ItemStack(Material.POTION, 5),
            ItemUtils.transportTicket(),
            ItemUtils.transportTicket(),
            ItemUtils.transportTicket(),
            ItemUtils.transportTicket(),
            ItemUtils.transportTicket()
    }, null, null, null, Villager.Profession.CLERIC),
    RANCHER("Pecuarista", null, null, new ItemStack[]{
            new ItemStack(Material.LEAD, 10),
            new ItemStack(Material.SHEARS, 5)
    }, null, null, null, Villager.Profession.FARMER),
    BLACKSMITH("Ferreiro", new Material[]{
            Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.GOLDEN_PICKAXE,
            Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.GOLDEN_AXE
    }, null, null, new Material[]{
            Material.IRON_ORE,
            Material.COAL_ORE
    }, null, null, Villager.Profession.WEAPONSMITH),
    JOINER("Marceneiro", new Material[]{
            Material.CHEST
    }, null, null, null, null, null, Villager.Profession.TOOLSMITH);

    private final Material[] craftableItems;
    private final Material[] breakableItems;
    private final ItemStack[] startItems;
    private final Material[] forgeableItems;
    private final EntityType[] killableEntities;
    private final MerchantRecipe[] merchantRecipes;
    private final String label;
    private final Villager.Profession doll;

    Profession(String label,
               Material[] craftableItems,
               Material[] breakableItems,
               ItemStack[] startItems,
               Material[] forgeableItems,
               EntityType[] killableEntities,
               MerchantRecipe[][] merchantRecipes,
               Villager.Profession doll) {
        this.label = label;
        this.doll = doll;
        this.craftableItems = craftableItems;
        this.breakableItems = breakableItems;
        this.startItems = startItems;
        this.forgeableItems = forgeableItems;
        this.killableEntities = killableEntities;
        this.merchantRecipes = merchantRecipes != null ? Arrays.stream(merchantRecipes).flatMap(Arrays::stream).toArray(MerchantRecipe[]::new) : null;
    }

    public List<Material> getCraftableItems() {
        return craftableItems != null ? Arrays.asList(craftableItems) : new ArrayList<>();
    }

    public boolean canCraft(Material material) {
        return craftableItems != null && (craftableItems.length == 0 || getCraftableItems().contains(material));
    }

    public List<Material> getBreakableItems() {
        return breakableItems != null ? Arrays.asList(breakableItems) : new ArrayList<>();
    }

    public boolean canBreak(Material material) {
        return breakableItems != null && (breakableItems.length == 0 || getBreakableItems().contains(material));
    }

    public List<ItemStack> getStartItems() {
        return startItems != null ? Arrays.asList(startItems) : new ArrayList<>();
    }

    public List<Material> getForgeableItems() {
        return Arrays.asList(forgeableItems);
    }

    public List<EntityType> getKillableEntities() {
        return Arrays.asList(killableEntities);
    }

    public boolean canKill(EntityType entityType) {
        if (entityType.equals(EntityType.PLAYER))
            return true;
        else if (killableEntities != null && getKillableEntities().contains(entityType)) {
            return true;
        } else
            return killableEntities != null && killableEntities.length == 0;
    }

    public List<MerchantRecipe> getMerchantRecipes() {
        return merchantRecipes != null ? Arrays.asList(merchantRecipes) : new ArrayList<>();
    }

    private static MerchantRecipe[] createRecipe(int emeralds, ItemStack result) {

        // Para comprar um $result
        MerchantRecipe compra = new MerchantRecipe(result, Integer.MAX_VALUE);
        // Precisa dar $emeralds esmeraldas
        compra.setIngredients(Collections.singletonList(new ItemStack(Material.EMERALD, (int) Math.ceil(emeralds * 2.5f))));

        // Para trocar $result por esmeraldas outra vez,
        MerchantRecipe venda = new MerchantRecipe(new ItemStack(Material.EMERALD, emeralds), Integer.MAX_VALUE);
        // Precisa pagar $results * 2
        venda.setIngredients(Collections.singletonList(new ItemStack(result.getType(), (int) Math.ceil(result.getAmount() * 1.5f))));

        // Ex: $emeralds: 1, $results: 15 toras
        // Compra: 3 esmeraldas > 15 toras
        // Venda: 23 toras > 1 esmeralda

        // Ex: $emerald: 100, $results: 1 diamond
        // Compra: 300 esmeraldas > 1 diamante
        // Venda: 2 diamantes > 100 esmeraldas

        return new MerchantRecipe[]{compra, venda};
    }

    public String getLabel() {
        return label;
    }

    public Villager.Profession getDoll() {
        return doll;
    }

    public boolean canSmelt(Material material) {
        if (forgeableItems == null)
            return false;
        if (forgeableItems.length == 0)
            return true;
        return getForgeableItems().contains(material);
    }
}