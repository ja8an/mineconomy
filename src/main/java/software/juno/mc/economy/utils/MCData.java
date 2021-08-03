package software.juno.mc.economy.utils;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import software.juno.mc.economy.MConomy;

public class MCData implements MetadataValue {

    final MConomy mConomy;
    final Object value;

    private MCData(MConomy mConomy, Object value) {
        this.mConomy = mConomy;
        this.value = value;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public int asInt() {
        return Integer.parseInt(asString());
    }

    @Override
    public float asFloat() {
        return Float.parseFloat(asString());
    }

    @Override
    public double asDouble() {
        return Double.parseDouble(asString());
    }

    @Override
    public long asLong() {
        return Long.parseLong(asString());
    }

    @Override
    public short asShort() {
        return Short.parseShort(asString());
    }

    @Override
    public byte asByte() {
        return Byte.parseByte(asString());
    }

    @Override
    public boolean asBoolean() {
        return Boolean.parseBoolean(asString());
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public Plugin getOwningPlugin() {
        return mConomy;
    }

    @Override
    public void invalidate() {

    }

    public static MCData create(MConomy mConomy, Object value) {
        return new MCData(mConomy, value);
    }

}
