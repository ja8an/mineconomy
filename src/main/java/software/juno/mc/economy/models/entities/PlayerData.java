package software.juno.mc.economy.models.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.juno.mc.economy.models.enums.Profession;

import java.util.UUID;

@DatabaseTable(tableName = "players")
@Data
@NoArgsConstructor
public class PlayerData {
    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private Integer money;
    @DatabaseField
    private Profession profession;
    @DatabaseField
    private UUID villager;
}
