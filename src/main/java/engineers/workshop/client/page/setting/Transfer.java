package engineers.workshop.client.page.setting;

import engineers.workshop.common.items.Upgrade;
import engineers.workshop.common.table.TileTable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class Transfer {

	private static final String NBT_ENABLED = "Enabled";
	private static final String NBT_AUTO = "Auto";
	private static final String NBT_WHITE_LIST = "WhiteList";
	private static final String NBT_ITEMS = "Items";
	private static final String NBT_ID = "Slot";
	private static final String NBT_MODE = "MatchMode";
	private static final int COMPOUND_ID = 10;
	private boolean enabled;
	private boolean isInput;
	private boolean auto;
	private ItemSetting[] items;
	private boolean useWhiteList;

	public Transfer(boolean isInput) {
		this.isInput = isInput;
		items = new ItemSetting[ItemSetting.ITEM_COUNT];

		for (int i = 0; i < items.length; i++) {
			items[i] = new ItemSetting(i);
		}
	}

	public boolean isInput() {
		return isInput;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public boolean hasWhiteList() {
		return useWhiteList;
	}

	public void setUseWhiteList(boolean useWhiteList) {
		this.useWhiteList = useWhiteList;
	}

	public ItemSetting getItem(int id) {
		return items[id];
	}

	public boolean isValid(TileTable table, ItemStack item) {
		if (item.isEmpty() || !table.getUpgradePage().hasGlobalUpgrade(Upgrade.FILTER)) {
			return true;
		}

		for (ItemSetting itemSetting : items) {
			ItemStack filterItem = itemSetting.getItem();
			if (!filterItem.isEmpty()) {
				boolean match = itemSetting.getMode().isMatch(item, filterItem);

				if (match) {
					return useWhiteList;
				}
			}
		}
		return !useWhiteList;
	}

	public boolean hasFilter(TileTable table) {
		if (table.getUpgradePage().hasGlobalUpgrade(Upgrade.FILTER)) {
			for (ItemSetting item : items) {
				if (!item.getItem().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public void writeToNBT(CompoundTag compound) {
		compound.putBoolean(NBT_ENABLED, enabled);
		if (enabled) {
			compound.putBoolean(NBT_AUTO, auto);
			compound.putBoolean(NBT_WHITE_LIST, useWhiteList);

			boolean hasItem = false;
			ListTag itemList = new ListTag();
			for (int i = 0; i < items.length; i++) {
				ItemSetting item = items[i];
				if (!item.getItem().isEmpty()) {
					CompoundTag itemCompound = new CompoundTag();
					itemCompound.putByte(NBT_ID, (byte) i);
					itemCompound.putByte(NBT_MODE, (byte) item.getMode().ordinal());
					item.getItem().toTag(itemCompound);
					itemList.add(itemCompound);
					hasItem = true;
				}
			}
			if (hasItem) {
				compound.put(NBT_ITEMS, itemList);
			}
		}
	}

	public void readFromNBT(CompoundTag compound) {
		for (ItemSetting item : items) {
			item.setItem(ItemStack.EMPTY);
			item.setMode(TransferMode.PRECISE);
		}

		enabled = compound.getBoolean(NBT_ENABLED);
		if (enabled) {
			auto = compound.getBoolean(NBT_AUTO);
			useWhiteList = compound.getBoolean(NBT_WHITE_LIST);

			if (compound.containsKey(NBT_ITEMS)) {
				ListTag itemList = compound.getList(NBT_ITEMS, COMPOUND_ID);
				for (int i = 0; i < itemList.size(); i++) {
					CompoundTag itemCompound = itemList.getCompoundTag(i);
					int id = itemCompound.getByte(NBT_ID);
					ItemSetting itemSetting = items[id];
					itemSetting.setMode(TransferMode.values()[itemCompound.getByte(NBT_MODE)]);
					itemSetting.setItem(ItemStack.fromTag(itemCompound));
				}
			}
		}
	}
}
