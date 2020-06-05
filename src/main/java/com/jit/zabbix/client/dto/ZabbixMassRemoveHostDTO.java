package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Zabbix Host DTO used as parameter in method host.massremove
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massremove">method host.massremove</a>
 **/

public class ZabbixMassRemoveHostDTO {
    @JsonProperty("hostids")
    private List<String> ids;

    @JsonProperty("groupids")
    private List<String> groupIds;
    private List<ZabbixHostInterface> interfaces;
    private List<GlobalMacro> macros;

    @JsonProperty("templateids")
    private List<String> templateIds;

    @JsonProperty("templateids_clear")
    private List<String> templateToClearIds;

    public static ZabbixMassRemoveHostDTOBuilder builder() {
        return new ZabbixMassRemoveHostDTOBuilder();
    }

    public List<String> getIds() {
        return this.ids;
    }

    public List<String> getGroupIds() {
        return this.groupIds;
    }

    public List<ZabbixHostInterface> getInterfaces() {
        return this.interfaces;
    }

    public List<GlobalMacro> getMacros() {
        return this.macros;
    }

    public List<String> getTemplateIds() {
        return this.templateIds;
    }

    public List<String> getTemplateToClearIds() {
        return this.templateToClearIds;
    }


    @JsonProperty("hostids")
    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @JsonProperty("groupids")
    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
    }

    public void setInterfaces(List<ZabbixHostInterface> interfaces) {
        this.interfaces = interfaces;
    }

    public void setMacros(List<GlobalMacro> macros) {
        this.macros = macros;
    }

    @JsonProperty("templateids")
    public void setTemplateIds(List<String> templateIds) {
        this.templateIds = templateIds;
    }

    @JsonProperty("templateids_clear")
    public void setTemplateToClearIds(List<String> templateToClearIds) {
        this.templateToClearIds = templateToClearIds;
    }


    @Override
    public String toString() {
        return "ZabbixMassRemoveHostDTO(ids=" + getIds() + ", groupIds=" + getGroupIds() + ", interfaces=" + getInterfaces() + ", macros=" + getMacros() + ", templateIds=" + getTemplateIds() + ", templateToClearIds=" + getTemplateToClearIds() + ")";
    }

    public ZabbixMassRemoveHostDTO() {
    }

    public ZabbixMassRemoveHostDTO(List<String> ids, List<String> groupIds, List<ZabbixHostInterface> interfaces, List<GlobalMacro> macros, List<String> templateIds, List<String> templateToClearIds) {
        this.ids = ids;
        this.groupIds = groupIds;
        this.interfaces = interfaces;
        this.macros = macros;
        this.templateIds = templateIds;
        this.templateToClearIds = templateToClearIds;
    }

    public static class ZabbixMassRemoveHostDTOBuilder {
        private ArrayList<String> ids;
        private ArrayList<String> groupIds;
        private ArrayList<ZabbixHostInterface> interfaces;
        private ArrayList<GlobalMacro> macros;
        private ArrayList<String> templateIds;
        private List<String> templateToClearIds;

        public ZabbixMassRemoveHostDTOBuilder id(String id) {
            if (this.ids == null) {
                this.ids = new ArrayList();
            }
            this.ids.add(id);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder ids(Collection<? extends String> ids) {
            if (this.ids == null) {
                this.ids = new ArrayList();
            }
            this.ids.addAll(ids);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder clearIds() {
            if (this.ids != null) {
                this.ids.clear();
            }
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder groupId(String groupId) {
            if (this.groupIds == null) {
                this.groupIds = new ArrayList();
            }
            this.groupIds.add(groupId);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder groupIds(Collection<? extends String> groupIds) {
            if (this.groupIds == null) {
                this.groupIds = new ArrayList();
            }
            this.groupIds.addAll(groupIds);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder clearGroupIds() {
            if (this.groupIds != null) {
                this.groupIds.clear();
            }
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder jdMethod_interface(ZabbixHostInterface interface1) {
            if (this.interfaces == null) {
                this.interfaces = new ArrayList();
            }
            this.interfaces.add(interface1);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder interfaces(Collection<? extends ZabbixHostInterface> interfaces) {
            if (this.interfaces == null) {
                this.interfaces = new ArrayList();
            }
            this.interfaces.addAll(interfaces);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder clearInterfaces() {
            if (this.interfaces != null) {
                this.interfaces.clear();
            }
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder macro(GlobalMacro macro) {
            if (this.macros == null) {
                this.macros = new ArrayList();
            }
            this.macros.add(macro);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder macros(Collection<? extends GlobalMacro> macros) {
            if (this.macros == null) {
                this.macros = new ArrayList();
            }
            this.macros.addAll(macros);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder clearMacros() {
            if (this.macros != null) {
                this.macros.clear();
            }
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder templateId(String templateId) {
            if (this.templateIds == null) {
                this.templateIds = new ArrayList();
            }
            this.templateIds.add(templateId);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder templateIds(Collection<? extends String> templateIds) {
            if (this.templateIds == null) {
                this.templateIds = new ArrayList();
            }
            this.templateIds.addAll(templateIds);
            return this;
        }

        public ZabbixMassRemoveHostDTOBuilder clearTemplateIds() {
            if (this.templateIds != null) {
                this.templateIds.clear();
            }
            return this;
        }

        @JsonProperty("templateids_clear")
        public ZabbixMassRemoveHostDTOBuilder templateToClearIds(List<String> templateToClearIds) {
            this.templateToClearIds = templateToClearIds;
            return this;
        }

        public ZabbixMassRemoveHostDTO build() {
            List ids;
            switch (this.ids == null ? 0 : this.ids.size()) {
                case 0:
                    ids = Collections.emptyList();
                    break;
                case 1:
                    ids = Collections.singletonList(this.ids.get(0));
                    break;
                default:
                    ids = Collections.unmodifiableList(new ArrayList(this.ids));
            }
            List groupIds;
            switch (this.groupIds == null ? 0 : this.groupIds.size()) {
                case 0:
                    groupIds = Collections.emptyList();
                    break;
                case 1:
                    groupIds = Collections.singletonList(this.groupIds.get(0));
                    break;
                default:
                    groupIds = Collections.unmodifiableList(new ArrayList(this.groupIds));
            }
            List interfaces;
            switch (this.interfaces == null ? 0 : this.interfaces.size()) {
                case 0:
                    interfaces = Collections.emptyList();
                    break;
                case 1:
                    interfaces = Collections.singletonList(this.interfaces.get(0));
                    break;
                default:
                    interfaces = Collections.unmodifiableList(new ArrayList(this.interfaces));
            }
            List macros;
            switch (this.macros == null ? 0 : this.macros.size()) {
                case 0:
                    macros = Collections.emptyList();
                    break;
                case 1:
                    macros = Collections.singletonList(this.macros.get(0));
                    break;
                default:
                    macros = Collections.unmodifiableList(new ArrayList(this.macros));
            }
            List templateIds;
            switch (this.templateIds == null ? 0 : this.templateIds.size()) {
                case 0:
                    templateIds = Collections.emptyList();
                    break;
                case 1:
                    templateIds = Collections.singletonList(this.templateIds.get(0));
                    break;
                default:
                    templateIds = Collections.unmodifiableList(new ArrayList(this.templateIds));
            }
            return new ZabbixMassRemoveHostDTO(ids, groupIds, interfaces, macros, templateIds, this.templateToClearIds);
        }

        @Override
        public String toString() {
            return "ZabbixMassRemoveHostDTO.ZabbixMassRemoveHostDTOBuilder(ids=" + this.ids + ", groupIds=" + this.groupIds + ", interfaces=" + this.interfaces + ", macros=" + this.macros + ", templateIds=" + this.templateIds + ", templateToClearIds=" + this.templateToClearIds + ")";
        }

    }
}
