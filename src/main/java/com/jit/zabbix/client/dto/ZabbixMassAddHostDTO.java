package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.Template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Zabbix Host DTO used as parameter in host.massadd.
 *
 * @author Mamadou Lamine NIANG
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massadd">method host.massadd</a>
 **/

public class ZabbixMassAddHostDTO {
    private List<ZabbixHostDTO> hosts;
    private List<ZabbixHostGroup> groups;
    private List<ZabbixHostInterface> interfaces;
    private List<GlobalMacro> macros;

    @JsonProperty("templates")
    private List<Template> templates;

    public static ZabbixMassAddHostDTOBuilder builder() {
        return new ZabbixMassAddHostDTOBuilder();
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

    public List<GlobalMacro> getMacros() {
        return this.macros;
    }

    public List<Template> getTemplates() {
        return this.templates;
    }

    public void setHosts(List<ZabbixHostDTO> hosts) {
        this.hosts = hosts;
    }

    public void setGroups(List<ZabbixHostGroup> groups) {
        this.groups = groups;
    }

    public void setInterfaces(List<ZabbixHostInterface> interfaces) {
        this.interfaces = interfaces;
    }

    public void setMacros(List<GlobalMacro> macros) {
        this.macros = macros;
    }

    @JsonProperty("templates")
    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    @Override
    public String toString() {
        return "ZabbixMassAddHostDTO(hosts=" + getHosts() + ", groups=" + getGroups() + ", interfaces=" + getInterfaces() + ", macros=" + getMacros() + ", templates=" + getTemplates() + ")";
    }

    public ZabbixMassAddHostDTO() {
    }

    public ZabbixMassAddHostDTO(List<ZabbixHostDTO> hosts, List<ZabbixHostGroup> groups, List<ZabbixHostInterface> interfaces, List<GlobalMacro> macros, List<Template> templates) {
        this.hosts = hosts;
        this.groups = groups;
        this.interfaces = interfaces;
        this.macros = macros;
        this.templates = templates;
    }

    public static class ZabbixMassAddHostDTOBuilder {
        private ArrayList<ZabbixHostDTO> hosts;
        private ArrayList<ZabbixHostGroup> groups;
        private ArrayList<ZabbixHostInterface> interfaces;
        private ArrayList<GlobalMacro> macros;
        private ArrayList<Template> templates;

        public ZabbixMassAddHostDTOBuilder host(ZabbixHostDTO host) {
            if (this.hosts == null) {
                this.hosts = new ArrayList();
            }
            this.hosts.add(host);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder hosts(Collection<? extends ZabbixHostDTO> hosts) {
            if (this.hosts == null) {
                this.hosts = new ArrayList();
            }
            this.hosts.addAll(hosts);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder clearHosts() {
            if (this.hosts != null) {
                this.hosts.clear();
            }
            return this;
        }

        public ZabbixMassAddHostDTOBuilder group(ZabbixHostGroup group) {
            if (this.groups == null) {
                this.groups = new ArrayList();
            }
            this.groups.add(group);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder groups(Collection<? extends ZabbixHostGroup> groups) {
            if (this.groups == null) {
                this.groups = new ArrayList();
            }
            this.groups.addAll(groups);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder clearGroups() {
            if (this.groups != null) {
                this.groups.clear();
            }
            return this;
        }

        public ZabbixMassAddHostDTOBuilder jdMethod_interface(ZabbixHostInterface interface1) {
            if (this.interfaces == null) {
                this.interfaces = new ArrayList();
            }
            this.interfaces.add(interface1);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder interfaces(Collection<? extends ZabbixHostInterface> interfaces) {
            if (this.interfaces == null) {
                this.interfaces = new ArrayList();
            }
            this.interfaces.addAll(interfaces);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder clearInterfaces() {
            if (this.interfaces != null) {
                this.interfaces.clear();
            }
            return this;
        }

        public ZabbixMassAddHostDTOBuilder macro(GlobalMacro macro) {
            if (this.macros == null) {
                this.macros = new ArrayList();
            }
            this.macros.add(macro);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder macros(Collection<? extends GlobalMacro> macros) {
            if (this.macros == null) {
                this.macros = new ArrayList();
            }
            this.macros.addAll(macros);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder clearMacros() {
            if (this.macros != null) {
                this.macros.clear();
            }
            return this;
        }

        public ZabbixMassAddHostDTOBuilder template(Template template) {
            if (this.templates == null) {
                this.templates = new ArrayList();
            }
            this.templates.add(template);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder templates(Collection<? extends Template> templates) {
            if (this.templates == null) {
                this.templates = new ArrayList();
            }
            this.templates.addAll(templates);
            return this;
        }

        public ZabbixMassAddHostDTOBuilder clearTemplates() {
            if (this.templates != null) {
                this.templates.clear();
            }
            return this;
        }

        public ZabbixMassAddHostDTO build() {
            List hosts;
            switch (this.hosts == null ? 0 : this.hosts.size()) {
                case 0:
                    hosts = Collections.emptyList();
                    break;
                case 1:
                    hosts = Collections.singletonList(this.hosts.get(0));
                    break;
                default:
                    hosts = Collections.unmodifiableList(new ArrayList(this.hosts));
            }
            List groups;
            switch (this.groups == null ? 0 : this.groups.size()) {
                case 0:
                    groups = Collections.emptyList();
                    break;
                case 1:
                    groups = Collections.singletonList(this.groups.get(0));
                    break;
                default:
                    groups = Collections.unmodifiableList(new ArrayList(this.groups));
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
            List templates;
            switch (this.templates == null ? 0 : this.templates.size()) {
                case 0:
                    templates = Collections.emptyList();
                    break;
                case 1:
                    templates = Collections.singletonList(this.templates.get(0));
                    break;
                default:
                    templates = Collections.unmodifiableList(new ArrayList(this.templates));
            }
            return new ZabbixMassAddHostDTO(hosts, groups, interfaces, macros, templates);
        }

        @Override
        public String toString() {
            return "ZabbixMassAddHostDTO.ZabbixMassAddHostDTOBuilder(hosts=" + this.hosts + ", groups=" + this.groups + ", interfaces=" + this.interfaces + ", macros=" + this.macros + ", templates=" + this.templates + ")";
        }

    }
}
