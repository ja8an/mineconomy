package software.juno.mc.economy.commands;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import software.juno.mc.economy.MConomy;
import software.juno.mc.economy.annotations.CommandExecutor;
import software.juno.mc.economy.models.entities.PlayerData;
import software.juno.mc.economy.models.enums.Profession;
import software.juno.mc.economy.utils.PlayerUtils;

import java.util.Locale;
import java.util.Objects;

@CommandExecutor("criar")
public class CriarCommand extends BaseCommand {

    public CriarCommand(MConomy mConomy) {
        super(mConomy);
    }

    public void npc(Player player) {

        PlayerData playerID = getPlayerData(player);

        if (playerID.getVillager() != null) {
            getLogger().info("Já existe, teleportando");
            Villager vil = (Villager) getServer().getEntity(playerID.getVillager());
            if (vil != null) {
                vil.setInvulnerable(false);
                vil.setAI(true);
                vil.setHealth(0);
                vil.damage(Double.MAX_VALUE);
            }
        }

        getLogger().info("Não existe, criando");

        Profession profession = playerID.getProfession();

        Villager villager = (Villager) Objects.requireNonNull(player.getLocation().getWorld()).spawnEntity(player.getLocation(), EntityType.VILLAGER);
        villager.setCustomName(String.format("%s - %s", profession.getLabel(), player.getName()));
        villager.setProfession(profession.getDoll());
        villager.setCollidable(false);
        villager.setCanPickupItems(false);
        villager.setAI(false);
        villager.setInvulnerable(true);

        playerID.setVillager(villager.getUniqueId());
        db().getPlayerDAO().update(playerID);

        villager.setRecipes(profession.getMerchantRecipes());

    }

    public void item(Player player, String item) {
        item(player, item, "1");
    }

    public void item(Player player, String item, String _qtd) {
        if (!PlayerUtils.isGod(player)) return;
        int qtd = Integer.parseInt(_qtd);
        Material m = Material.valueOf(item.toUpperCase(Locale.ROOT));
        PlayerUtils.addToPlayerInventory(player, new ItemStack(m, qtd));
    }
}
