package com.client.definitions.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.client.model.EquipmentModelType;
import com.client.model.SkillLevel;
import com.client.sign.Signlink;
import com.client.utilities.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.util.AssetUtils;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ItemDef {

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ItemDef.class.getName());
    private static Map<Integer, ItemDef> definitions = null;
    private static final List<SkillLevel> EMPTY_REQUIREMENTS = Lists.newArrayList();

    public static Map<Integer, ItemDef> getDefinitions() {
        return definitions;
    }

    public static void load() throws Exception {
        definitions = new HashMap<>();
        List<ItemDef> list = JsonUtil.fromYaml(AssetUtils.INSTANCE.getResource("item_definitions.yaml").openStream(), new TypeReference<List<ItemDef>>() {
        });
        list.forEach(it -> definitions.put(it.getId(), it));
        log.info("Loaded " + list.size() + " item definitions.");
    }

    public static ItemDef forId(int itemId) {
        Preconditions.checkState(definitions != null, "Item definitions weren\'t loaded.");
        return definitions.getOrDefault(itemId, builder().id(itemId).build());
    }

    public static ItemDefBuilder builderOf(ItemDef def) {
        ItemDefBuilder builder = new ItemDefBuilder().id(def.id).name(def.name).description(def.description).shopValue(def.shopValue).noteId(def.noteId).noted(def.noted).stackable(def.stackable).untradeable(def.untradeable).deletedOnDeath(def.deletedOnDeath).checkBeforeDrop(def.checkBeforeDrop).undroppable(def.undroppable).equipmentModelType(def.equipmentModelType).requirements(def.requirements);
        Preconditions.checkState(builder.build().equals(def));
        return builder;
    }

    private final int id;
    private final String name;
    private final String description;
    private final int shopValue;
    private final int noteId;
    private final boolean noted;
    private final boolean stackable;
    private final boolean untradeable;
    private final boolean deletedOnDeath;
    private final boolean checkBeforeDrop;
    private final boolean undroppable;
    private final EquipmentModelType equipmentModelType;
    private final List<SkillLevel> requirements;

    public ItemDef(int id, String name, String description, int shopValue, int noteId, boolean noted, boolean stackable, boolean untradeable, boolean deletedOnDeath, boolean checkBeforeDrop, boolean undroppable, EquipmentModelType equipmentModelType, List<SkillLevel> requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.shopValue = shopValue;
        this.noteId = noteId;
        this.noted = noted;
        this.stackable = stackable;
        this.untradeable = untradeable;
        this.deletedOnDeath = deletedOnDeath;
        this.checkBeforeDrop = checkBeforeDrop;
        this.undroppable = undroppable;
        this.equipmentModelType = equipmentModelType;
        this.requirements = requirements;
    }

    private ItemDef() {
        id = -1;
        name = "";
        description = "";
        shopValue = 0;
        noteId = 0;
        noted = false;
        stackable = false;
        untradeable = false;
        deletedOnDeath = false;
        checkBeforeDrop = false;
        undroppable = false;
        equipmentModelType = null;
        requirements = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        if (name == null) {
            return "unknown item " + getId();
        }
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * @return the shop value. for noted items it grabs from the unnoted version to
     * enforce the prices between noted and unnoted.
     */
    public int getShopValue() {
        if (noted && !forId(noteId).isNoted()) {
            return forId(noteId).getShopValue();
        }
        return shopValue;
    }

    /**
     * Gets the actual shop value stored in the definition.
     * Doesn't enforce noted and unnoted having the same value.
     */
    public int getRawShopValue() {
        return shopValue;
    }

    public int getNoteId() {
        return noteId;
    }

    public boolean isNoted() {
        return noted;
    }

    public boolean isStackable() {
        return stackable || isNoted();
    }

    @JsonIgnore
    public boolean isTradable() {
        return !untradeable;
    }

    public boolean isDeletedOnDeath() {
        return deletedOnDeath;
    }

    public boolean isCheckBeforeDrop() {
        return checkBeforeDrop;
    }

    @JsonIgnore
    public boolean isDroppable() {
        return !undroppable;
    }

    public List<SkillLevel> getRequirements() {
        return requirements == null ? EMPTY_REQUIREMENTS : requirements;
    }

    public EquipmentModelType getEquipmentModelType() {
        return equipmentModelType;
    }

    @Override
    public String toString() {
        return "ItemDef{" + "id=" + id + ", name=\'" + name + '\'' + ", description=\'" + description + '\'' + ", shopValue=" + shopValue + ", noteId=" + noteId + ", noted=" + noted + ", stackable=" + stackable + ", untradeable=" + untradeable + ", deletedOnDeath=" + deletedOnDeath + ", checkBeforeDrop=" + checkBeforeDrop + ", undroppable=" + undroppable + ", equipmentModelType=" + equipmentModelType + ", requirements=" + requirements + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDef itemDef = (ItemDef) o;
        return id == itemDef.id && shopValue == itemDef.shopValue && noteId == itemDef.noteId && noted == itemDef.noted && stackable == itemDef.stackable && untradeable == itemDef.untradeable && deletedOnDeath == itemDef.deletedOnDeath && checkBeforeDrop == itemDef.checkBeforeDrop && undroppable == itemDef.undroppable && Objects.equals(name, itemDef.name) && Objects.equals(description, itemDef.description) && equipmentModelType == itemDef.equipmentModelType && Objects.equals(requirements, itemDef.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, shopValue, noteId, noted, stackable, untradeable, deletedOnDeath, checkBeforeDrop, undroppable, equipmentModelType, requirements);
    }

    public static class ItemDefBuilder {

        private int id;

        private String name;

        private String description;

        private int shopValue;

        private int noteId;

        private boolean noted;

        private boolean stackable;

        private boolean untradeable;

        private boolean deletedOnDeath;

        private boolean checkBeforeDrop;

        private boolean undroppable;

        private EquipmentModelType equipmentModelType;

        private List<SkillLevel> requirements;

        ItemDefBuilder() {
        }

        public ItemDefBuilder id(final int id) {
            this.id = id;
            return this;
        }

        public ItemDefBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public ItemDefBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public ItemDefBuilder shopValue(final int shopValue) {
            this.shopValue = shopValue;
            return this;
        }

        public ItemDefBuilder noteId(final int noteId) {
            this.noteId = noteId;
            return this;
        }

        public ItemDefBuilder noted(final boolean noted) {
            this.noted = noted;
            return this;
        }

        public ItemDefBuilder stackable(final boolean stackable) {
            this.stackable = stackable;
            return this;
        }

        public ItemDefBuilder untradeable(final boolean untradeable) {
            this.untradeable = untradeable;
            return this;
        }

        public ItemDefBuilder deletedOnDeath(final boolean deletedOnDeath) {
            this.deletedOnDeath = deletedOnDeath;
            return this;
        }

        public ItemDefBuilder checkBeforeDrop(final boolean checkBeforeDrop) {
            this.checkBeforeDrop = checkBeforeDrop;
            return this;
        }

        public ItemDefBuilder undroppable(final boolean undroppable) {
            this.undroppable = undroppable;
            return this;
        }

        public ItemDefBuilder equipmentModelType(final EquipmentModelType equipmentModelType) {
            this.equipmentModelType = equipmentModelType;
            return this;
        }

        public ItemDefBuilder requirements(final List<SkillLevel> requirements) {
            this.requirements = requirements;
            return this;
        }

        public ItemDef build() {
            return new ItemDef(this.id, this.name, this.description, this.shopValue, this.noteId, this.noted, this.stackable, this.untradeable, this.deletedOnDeath, this.checkBeforeDrop, this.undroppable, this.equipmentModelType, this.requirements);
        }

        @Override
        public String toString() {
            return "ItemDef.ItemDefBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", shopValue=" + this.shopValue + ", noteId=" + this.noteId + ", noted=" + this.noted + ", stackable=" + this.stackable + ", untradeable=" + this.untradeable + ", deletedOnDeath=" + this.deletedOnDeath + ", checkBeforeDrop=" + this.checkBeforeDrop + ", undroppable=" + this.undroppable + ", equipmentModelType=" + this.equipmentModelType + ", requirements=" + this.requirements + ")";
        }
    }

    public static ItemDefBuilder builder() {
        return new ItemDefBuilder();
    }
}
