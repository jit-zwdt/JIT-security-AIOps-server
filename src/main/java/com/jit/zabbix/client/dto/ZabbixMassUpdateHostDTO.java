package com.jit.zabbix.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jit.zabbix.client.model.GlobalMacro;
import com.jit.zabbix.client.model.host.HostInventoryProperty;
import com.jit.zabbix.client.model.host.InventoryMode;
import com.jit.zabbix.client.model.host.ZabbixHostGroup;
import com.jit.zabbix.client.model.host.ZabbixHostInterface;
import com.jit.zabbix.client.model.template.Template;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Zabbix Host DTO used as parameter in host.massupdate.
 * @see <a href="https://www.zabbix.com/documentation/4.0/manual/api/reference/host/massupdate">method host.massupdate</a>
 *
 * @author Mamadou Lamine NIANG
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZabbixMassUpdateHostDTO {

    @Singular
    private List<ZabbixHostDTO> hosts;
    @Singular
    private List<ZabbixHostGroup> groups;
    @Singular
    private List<ZabbixHostInterface> interfaces;
    @Singular("putInventory")
    private Map<HostInventoryProperty, String> inventory;
    @JsonProperty("inventory_mode")
    private InventoryMode inventoryMode;
    @Singular
    private List<GlobalMacro> macros;
    @JsonProperty("templates")
    @Singular
    private List<Template> templates;
    @JsonProperty("templates_clear")
    @Singular("templateToClear")
    private List<Template> templatesToClear;
}
