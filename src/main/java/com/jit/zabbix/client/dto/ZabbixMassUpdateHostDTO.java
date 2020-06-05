package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.model.host.InventoryMode;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.Template;

import java.util.*;

/**
 * Zabbix Host DTO used as parameter in host.massupdate.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massupdate">method host.massupdate</a>
 **/

public class ZabbixMassUpdateHostDTO {
    private List<ZabbixHostDTO> hosts;

    private List<ZabbixHostGroup> groups;

    private List<ZabbixHostInterface> interfaces;

    private Map<HostInventoryProperty, String> inventory;

    @JsonProperty("inventory_mode")
    private InventoryMode inventoryMode;

    private List<GlobalMacro> macros;

    @JsonProperty("templates")
    private List<Template> templates;

    @JsonProperty("templates_clear")
    private List<Template> templatesToClear;

    public void setHosts(List<ZabbixHostDTO> hosts) {
        this.hosts = hosts;
    }

    public void setGroups(List<ZabbixHostGroup> groups) {
        this.groups = groups;
    }

    public void setInterfaces(List<ZabbixHostInterface> interfaces) {
        this.interfaces = interfaces;
    }

    public void setInventory(Map<HostInventoryProperty, String> inventory) {
        this.inventory = inventory;
    }

    @JsonProperty("inventory_mode")
    public void setInventoryMode(InventoryMode inventoryMode) {
        this.inventoryMode = inventoryMode;
    }

    public void setMacros(List<GlobalMacro> macros) {
        this.macros = macros;
    }

    @JsonProperty("templates")
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    @JsonProperty("templates_clear")
    public void setTemplatesToClear(List<Template> templatesToClear) {
        this.templatesToClear = templatesToClear;
    }


    @Override
    public String toString() {
        return "ZabbixMassUpdateHostDTO(hosts=" + getHosts() + ", groups=" + getGroups() + ", interfaces=" + getInterfaces() + ", inventory=" + getInventory() + ", inventoryMode=" + getInventoryMode() + ", macros=" + getMacros() + ", templates=" + getTemplates() + ", templatesToClear=" + getTemplatesToClear() + ")";
    }

    public ZabbixMassUpdateHostDTO() {
    }

    public ZabbixMassUpdateHostDTO(List<ZabbixHostDTO> hosts, List<ZabbixHostGroup> groups, List<ZabbixHostInterface> interfaces, Map<HostInventoryProperty, String> inventory, InventoryMode inventoryMode, List<GlobalMacro> macros, List<Template> templates, List<Template> templatesToClear) {
        this.hosts = hosts;
        this.groups = groups;
        this.interfaces = interfaces;
        this.inventory = inventory;
        this.inventoryMode = inventoryMode;
        this.macros = macros;
        this.templates = templates;
        this.templatesToClear = templatesToClear;
    }

    public static ZabbixMassUpdateHostDTOBuilder builder() {
        return new ZabbixMassUpdateHostDTOBuilder();
    }

    public static class ZabbixMassUpdateHostDTOBuilder {
        private ArrayList<ZabbixHostDTO> hosts;

        private ArrayList<ZabbixHostGroup> groups;

        private ArrayList<ZabbixHostInterface> interfaces;

        private ArrayList<HostInventoryProperty> inventory$key;

        private ArrayList<String> inventory$value;

        private InventoryMode inventoryMode;

        private ArrayList<GlobalMacro> macros;

        private ArrayList<Template> templates;

        private ArrayList<Template> templatesToClear;

        public ZabbixMassUpdateHostDTOBuilder host(ZabbixHostDTO host) {
            if (this.hosts == null) {
                this.hosts = new ArrayList<>();
            }
            this.hosts.add(host);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder hosts(Collection<? extends ZabbixHostDTO> hosts) {
            if (this.hosts == null) {
                this.hosts = new ArrayList<>();
            }
            this.hosts.addAll(hosts);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearHosts() {
            if (this.hosts != null) {
                this.hosts.clear();
            }
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder group(ZabbixHostGroup group) {
            if (this.groups == null) {
                this.groups = new ArrayList<>();
            }
            this.groups.add(group);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder groups(Collection<? extends ZabbixHostGroup> groups) {
            if (this.groups == null) {
                this.groups = new ArrayList<>();
            }
            this.groups.addAll(groups);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearGroups() {
            if (this.groups != null) {
                this.groups.clear();
            }
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder interface1(ZabbixHostInterface interface1) {
            if (this.interfaces == null) {
                this.interfaces = new ArrayList<>();
            }
            this.interfaces.add(interface1);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder interfaces(Collection<? extends ZabbixHostInterface> interfaces) {
            if (this.interfaces == null) {
                this.interfaces = new ArrayList<>();
            }
            this.interfaces.addAll(interfaces);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearInterfaces() {
            if (this.interfaces != null) {
                this.interfaces.clear();
            }
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder putInventory(HostInventoryProperty putInventoryKey, String putInventoryValue) {
            if (this.inventory$key == null) {
                this.inventory$key = new ArrayList<>();
                this.inventory$value = new ArrayList<>();
            }
            this.inventory$key.add(putInventoryKey);
            this.inventory$value.add(putInventoryValue);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder inventory(Map<? extends HostInventoryProperty, ? extends String> inventory) {
            if (this.inventory$key == null) {
                this.inventory$key = new ArrayList<>();
                this.inventory$value = new ArrayList<>();
            }
            for (Map.Entry<? extends HostInventoryProperty, ? extends String> $lombokEntry : inventory.entrySet()) {
                this.inventory$key.add($lombokEntry.getKey());
                this.inventory$value.add($lombokEntry.getValue());
            }
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearInventory() {
            if (this.inventory$key != null) {
                this.inventory$key.clear();
                this.inventory$value.clear();
            }
            return this;
        }

        @JsonProperty("inventory_mode")
        public ZabbixMassUpdateHostDTOBuilder inventoryMode(InventoryMode inventoryMode) {
            this.inventoryMode = inventoryMode;
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder macro(GlobalMacro macro) {
            if (this.macros == null) {
                this.macros = new ArrayList<>();
            }
            this.macros.add(macro);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder macros(Collection<? extends GlobalMacro> macros) {
            if (this.macros == null) {
                this.macros = new ArrayList<>();
            }
            this.macros.addAll(macros);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearMacros() {
            if (this.macros != null) {
                this.macros.clear();
            }
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder template(Template template) {
            if (this.templates == null) {
                this.templates = new ArrayList<>();
            }
            this.templates.add(template);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder templates(Collection<? extends Template> templates) {
            if (this.templates == null) {
                this.templates = new ArrayList<>();
            }
            this.templates.addAll(templates);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearTemplates() {
            if (this.templates != null) {
                this.templates.clear();
            }
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder templateToClear(Template templateToClear) {
            if (this.templatesToClear == null) {
                this.templatesToClear = new ArrayList<>();
            }
            this.templatesToClear.add(templateToClear);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder templatesToClear(Collection<? extends Template> templatesToClear) {
            if (this.templatesToClear == null) {
                this.templatesToClear = new ArrayList<>();
            }
            this.templatesToClear.addAll(templatesToClear);
            return this;
        }

        public ZabbixMassUpdateHostDTOBuilder clearTemplatesToClear() {
            if (this.templatesToClear != null) {
                this.templatesToClear.clear();
            }
            return this;
        }

        public ZabbixMassUpdateHostDTO build() {
            List<ZabbixHostDTO> hosts;
            List<ZabbixHostGroup> groups;
            List<ZabbixHostInterface> interfaces;
            Map<HostInventoryProperty, String> inventory;
            int $i;
            List<GlobalMacro> macros;
            List<Template> templates;
            switch ((this.hosts == null) ? 0 : this.hosts.size()) {
                case 0:
                    hosts = Collections.emptyList();
                    break;
                case 1:
                    hosts = Collections.singletonList(this.hosts.get(0));
                    break;
                default:
                    hosts = Collections.unmodifiableList(new ArrayList<>(this.hosts));
                    break;
            }
            switch ((this.groups == null) ? 0 : this.groups.size()) {
                case 0:
                    groups = Collections.emptyList();
                    break;
                case 1:
                    groups = Collections.singletonList(this.groups.get(0));
                    break;
                default:
                    groups = Collections.unmodifiableList(new ArrayList<>(this.groups));
                    break;
            }
            switch ((this.interfaces == null) ? 0 : this.interfaces.size()) {
                case 0:
                    interfaces = Collections.emptyList();
                    break;
                case 1:
                    interfaces = Collections.singletonList(this.interfaces.get(0));
                    break;
                default:
                    interfaces = Collections.unmodifiableList(new ArrayList<>(this.interfaces));
                    break;
            }
            switch ((this.inventory$key == null) ? 0 : this.inventory$key.size()) {
                case 0:
                    inventory = Collections.emptyMap();
                    break;
                case 1:
                    inventory = Collections.singletonMap(this.inventory$key.get(0), this.inventory$value.get(0));
                    break;
                default:
                    for (inventory = new LinkedHashMap<>((this.inventory$key.size() < 1073741824) ? (1 + this.inventory$key.size() + (this.inventory$key.size() - 3) / 3) : Integer.MAX_VALUE), $i = 0; $i < this.inventory$key.size(); ) {
                        inventory.put(this.inventory$key.get($i), this.inventory$value.get($i));
                        $i++;
                    }
                    inventory = Collections.unmodifiableMap(inventory);
                    break;
            }
            switch ((this.macros == null) ? 0 : this.macros.size()) {
                case 0:
                    macros = Collections.emptyList();
                    break;
                case 1:
                    macros = Collections.singletonList(this.macros.get(0));
                    break;
                default:
                    macros = Collections.unmodifiableList(new ArrayList<>(this.macros));
                    break;
            }
            switch ((this.templates == null) ? 0 : this.templates.size()) {
                case 0:
                    templates = Collections.emptyList();
                    break;
                case 1:
                    templates = Collections.singletonList(this.templates.get(0));
                    break;
                default:
                    templates = Collections.unmodifiableList(new ArrayList<>(this.templates));
                    break;
            }
            List templatesToClear;
            switch (this.templatesToClear == null ? 0 : this.templatesToClear.size()) {
                case 0:
                    templatesToClear = Collections.emptyList();
                    break;
                case 1:
                    templatesToClear = Collections.singletonList(this.templatesToClear.get(0));
                    break;
                default:
                    templatesToClear = Collections.unmodifiableList(new ArrayList(this.templatesToClear));
            }
            return new ZabbixMassUpdateHostDTO(hosts, groups, interfaces, inventory, this.inventoryMode, macros, templates, templatesToClear);
        }

        @Override
        public String toString() {
            return "ZabbixMassUpdateHostDTO.ZabbixMassUpdateHostDTOBuilder(hosts=" + this.hosts + ", groups=" + this.groups + ", interfaces=" + this.interfaces + ", inventory$key=" + this.inventory$key + ", inventory$value=" + this.inventory$value + ", inventoryMode=" + this.inventoryMode + ", macros=" + this.macros + ", templates=" + this.templates + ", templatesToClear=" + this.templatesToClear + ")";
        }

    }

    public List<ZabbixHostDTO> getHosts() {
        return this.hosts;
    }

    public List<ZabbixHostGroup> getGroups() {
        return this.groups;
    }

    public List<ZabbixHostInterface> getInterfaces() {
        return this.interfaces;
    }

    public Map<HostInventoryProperty, String> getInventory() {
        return this.inventory;
    }

    public InventoryMode getInventoryMode() {
        return this.inventoryMode;
    }

    public List<GlobalMacro> getMacros() {
        return this.macros;
    }

    public List<Template> getTemplates() {
        return this.templates;
    }

    public List<Template> getTemplatesToClear() {
        return this.templatesToClear;
    }

}
