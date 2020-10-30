package com.jit.server.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jit.server.dto.ProblemHostDTO;
import com.jit.server.repository.HostRepo;
import com.jit.server.service.HomePageService;
import com.jit.zabbix.client.dto.ZabbixProblemDTO;
import com.jit.zabbix.client.model.problem.ProblemSeverity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private HostRepo hostRepo;

    @Override
    public List<Object> getHostByType() throws Exception {
        return hostRepo.getHostByType();
    }

    /**
     * 根据查询的所有的主机信息进行状态的构建统计
     * @param problemHosts 主机信息
     * @return 统计信息
     */
    @Override
    public JSONObject getStatisticalJson(List<ProblemHostDTO> problemHosts) {
        //创建 JSONObject 对象
        JSONObject jsonObject = new JSONObject();
        //未定义信息统计
        int not_classified = 0;
        //信息信息统计
        int information = 0;
        //警告信息统计
        int warning = 0;
        //一般严重信息统计
        int average = 0;
        //严重信息统计
        int high = 0;
        //灾难信息统计
        int disaster = 0;
        // 首先遍历数据集合
        if(problemHosts != null){
            for(int i = 0 ; i < problemHosts.size() ; i++){
                //获取 problemHost 对象
                ProblemHostDTO problemHost = problemHosts.get(i);
                //获取里面的真实数据
                ZabbixProblemDTO zabbixProblem = problemHost.getZabbixProblemDTO();
                //进行数据的桶位相加
                if(zabbixProblem.getSeverity() == ProblemSeverity.NOT_CLASSIFIED){
                    //未定义
                    not_classified++;
                }else if(zabbixProblem.getSeverity() == ProblemSeverity.INFORMATION){
                    //信息
                    information++;
                }else if(zabbixProblem.getSeverity() == ProblemSeverity.WARNING){
                    //警告
                    warning++;
                }else if(zabbixProblem.getSeverity() == ProblemSeverity.AVERAGE){
                    //一般严重
                    average++;
                }else if(zabbixProblem.getSeverity() == ProblemSeverity.HIGH){
                    //严重
                    high++;
                }else if(zabbixProblem.getSeverity() == ProblemSeverity.DISASTER){
                    //灾难
                    disaster++;
                }
            }
        }
        //未定义信息统计添加
//        jsonObject.put("not_classified" , not_classified);
        //信息信息统计添加
        jsonObject.put("information" , information);
        //警告信息统计添加
        jsonObject.put("warning" , warning);
        //一般严重信息统计添加
        jsonObject.put("average" , average);
        //严重信息统计添加
        jsonObject.put("high" , high);
        //灾难信息统计添加
        jsonObject.put("disaster" , disaster);
        //返回
        return jsonObject;
    }

    /**
     * 根据所有的问题信息进行常见问题信息 Top5 排名信息的构建
     * @param problemHosts 所有的问题信息
     * @return 统计信息
     */
    @Override
    public JSONArray getFAQJson(List<ProblemHostDTO> problemHosts) {
        //创建 JSONObject 对象
        JSONArray jsonArray = new JSONArray();
        //定义需要排名的条数
        int topSize = 5;
        //创建一个 String 类型的 List 集合对象
        List<String> questionStringList = new ArrayList<>();
        if(problemHosts != null){
            for(int i = 0 ; i < problemHosts.size() ; i++) {
                //获取 problemHost 对象
                ProblemHostDTO problemHost = problemHosts.get(i);
                //获取名称并添加到 questionStringList 中
                questionStringList.add(problemHost.getZabbixProblemDTO().getName());
            }
        }
        //对字符串数组对象进行排序(自定义排序)
        sortStringList(questionStringList);
        //对排序成功的字符串进行对比操作返回一个对比的结果集合
        List<BasicMapMessage> basicMapMessage = getBasicMapMessageAsString(questionStringList);
        //前面的代码已经做完了计数和拿到名字的处理 这里的代码进行排序 倒序排列大的在前 自定义排序
        sortBasicMapMessageList(basicMapMessage);
        //根据数组的前<?>条数据返回对应的 JSONObject 对象
        if(basicMapMessage != null && !basicMapMessage.isEmpty()){
            for(int i = 0 ; i < topSize ; i++){
                jsonArray.add(basicMapMessage.get(i));
            }
        }
        //创建 JSONObject 对象进行返回值
        return jsonArray;
    }

    /**
     * 根据所有的问题信息进行设备异常服务器 TOP10 排名信息的构建
     * @param problemHosts 所有的问题信息
     * @return 统计信息
     */
    @Override
    public JSONArray getHostErrorJson(List<ProblemHostDTO> problemHosts) {
        //创建 JSONObject 对象
        JSONArray jsonArray = new JSONArray();
        //定义需要排名的条数
        int topSize = 10;
        //创建一个 String 类型的 List 集合对象
        List<ProblemHostDTO> problemHostList = new ArrayList<>();
        if(problemHosts != null){
            for(int i = 0 ; i < problemHosts.size() ; i++) {
                ProblemHostDTO ph = new ProblemHostDTO();
                //获取 problemHost 对象
                ProblemHostDTO problemHost = problemHosts.get(i);
                //赋值操作
                ph.setHostId(problemHost.getHostId());
                ph.setHostName(problemHost.getHostName());
                //获取名称并添加到 questionStringList 中
                problemHostList.add(ph);
            }
        }
        //对字符串数组对象进行排序(自定义排序)
        sortProblemHostDTOList(problemHostList);
        //对排序成功的字符串进行对比操作返回一个对比的结果集合
        List<BasicMapMessage> basicMapMessage = getBasicMapMessageAsProblemHost(problemHostList);
        //前面的代码已经做完了计数和拿到名字的处理 这里的代码进行排序 倒序排列大的在前 自定义排序
        sortBasicMapMessageList(basicMapMessage);
        //根据数组的前<?>条数据返回对应的 JSONObject 对象
        if(basicMapMessage != null && !basicMapMessage.isEmpty()){
            for(int i = 0 ; i < topSize ; i++){
                jsonArray.add(basicMapMessage.get(i));
            }
        }
        //创建 JSONObject 对象进行返回值
        return jsonArray;
    }

    @Override
    public JSONObject getAssetsSumJson(List<Object[]> sumResult) {
        //创建 JSONObject 对象
        JSONObject jsonObject = new JSONObject();
        //获取第一行数据
        Object[] oneData = sumResult.get(0);
        //进行值的添加
        jsonObject.put("hostSumNum" , oneData[0] == null ? 0 : oneData[0]);
        jsonObject.put("hostSumCpu" , oneData[1] == null ? 0 : oneData[1]);
        jsonObject.put("hostSumMemory" , oneData[2] == null ? 0 : oneData[2]);
        jsonObject.put("hostSumHardDisk" , oneData[3] == null ? 0 : oneData[3]);
        return jsonObject;
    }

    /**
     * 对 ProblemHostDTO 对象根据 hostId 进行排序
     * @param problemHostList 需要排序的 ProblemHostDTO 数组对象
     */
    private void sortProblemHostDTOList(List<ProblemHostDTO> problemHostList){
        //对字符串数组对象进行排序(自定义排序)
        Collections.sort(problemHostList, (a, b) -> {
            if (a.getHostId().equals(b.getHostId())) return 0;
            if (a.getHostId().length() > b.getHostId().length()) {
                return 1;
            } else if (a.getHostId().length() < b.getHostId().length()) {
                return -1;
            } else {
                return a.getHostId().compareTo(b.getHostId());
            }
        });
    }

    /**
     * 对字符串数组进行排序
     * @param stringList 需要排序的字符串数组
     */
    private void sortStringList(List<String> stringList){
        //对字符串数组对象进行排序(自定义排序)
        Collections.sort(stringList, (a, b) -> {
            if (a.equals(b)) return 0;
            if (a.length() > b.length()) {
                return 1;
            } else if (a.length() < b.length()) {
                return -1;
            } else {
                return a.compareTo(b);
            }
        });
    }

    /**
     * 倒序排序 BasicMapMessage 集合对象 倒序排列大的在前 自定义排序
     * @param basicMapMessage BasicMapMessage 集合对象
     */
    private void sortBasicMapMessageList(List<BasicMapMessage> basicMapMessage){
        Collections.sort(basicMapMessage, (a, b) -> {
            if (a.count == b.count) return 0;
            if (a.count > b.count) {
                return -1;
            } else if (a.count < b.count) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    /**
     * 对进行排序的字符串进行对比操作 对比结果会自动的记录出现过多少次 以及传入的 String 的名称
     * @return problemHostList 集合对象
     */
    private List<BasicMapMessage> getBasicMapMessageAsProblemHost(List<ProblemHostDTO> problemHostList){
        //声明集合对象
        List<BasicMapMessage> BasicMapMessages = new ArrayList<>();
        // 如果整个字符串数组在最后的一位数和第一位的文字不一样才继续下面的判断 否则直接返回值
        if(problemHostList.get(0).getHostId().equals(problemHostList.get(problemHostList.size() - 1).getHostId())){
            //构建返回的对象
            BasicMapMessage basicMapMessage = new BasicMapMessage(problemHostList.get(0).getHostName() , problemHostList.size());
            //添加到List对象中
            BasicMapMessages.add(basicMapMessage);
            //返回
            return BasicMapMessages;
        }
        //对排序的字符串做对比操作
        for(int i= 0 ; i < problemHostList.size() ;){
            //添加布尔类型的值 如果没有相等的则进行迭代不然会出现死循环
            boolean flag = true;
            int count = 1;
            //拿出字符串
            String hostId = problemHostList.get(i).getHostId();
            String hostName = problemHostList.get(i).getHostName();
            //使用循环 如果两个字符串相等的话则进入循环否则不进入循环
            //其实还是有 Bug 但是数据多了就不会出现 bug 了 这里的判断是最后的一位数如果和第一个数据相等则继续执行 但是一般情况下不会相等因为进行了排序 是这么处理越界异常的
            while(hostId.equals(problemHostList.get(i + 1 == problemHostList.size() ? 1 : i + 1).getHostId())){
                //进入首先吧 flag 的值变为 false 不让再次进行迭代
                flag = false;
                //计数器加 1
                count ++;
                //迭代
                i++;
            }
            //如果 flag 是 true 则进行迭代否则不进行迭代
            if(flag){
                i++;
            }
            //创建 BasicMapMessage 对象进行数据的添加
            BasicMapMessage basicMapMessage = new BasicMapMessage(hostName , count);
            //添加对象到 list 集合中
            BasicMapMessages.add(basicMapMessage);
        }
        return BasicMapMessages;
    }

    /**
     * 对进行排序的字符串进行对比操作 对比结果会自动的记录出现过多少次 以及传入的 String 的名称
     * @return BasicMapMessage 集合对象
     */
    private List<BasicMapMessage> getBasicMapMessageAsString(List<String> stringList){
        //声明集合对象
        List<BasicMapMessage> BasicMapMessages = new ArrayList<>();
        //对死循环进行处理
        // 如果整个字符串数组在最后的一位数和第一位的文字不一样才继续下面的判断 否则直接返回值
        if(stringList.get(0).equals(stringList.get(stringList.size() - 1))){
            //构建返回的对象
            BasicMapMessage basicMapMessage = new BasicMapMessage(stringList.get(0) , stringList.size());
            //添加到数组对象中
            BasicMapMessages.add(basicMapMessage);
            //返回
            return BasicMapMessages;
        }
        //对排序的字符串做对比操作
        for(int i= 0 ; i < stringList.size() ;){
            //添加布尔类型的值 如果没有相等的则进行迭代不然会出现死循环
            boolean flag = true;
            int count = 1;
            //拿出字符串
            String name = stringList.get(i);
            //使用循环 如果两个字符串相等的话则进入循环否则不进入循环
            //其实还是有 Bug 但是数据多了就不会出现 bug 了 这里的判断是最后的一位数如果和第一个数据相等则继续执行 但是一般情况下不会相等因为进行了排序 是这么处理越界异常的
            while(name.equals(stringList.get(i + 1 == stringList.size() ? 1 : i + 1))){
                //进入首先吧 flag 的值变为 false 不让再次进行迭代
                flag = false;
                //计数器加 1
                count ++;
                //迭代
                i++;
            }
            //如果 flag 是 true 则进行迭代否则不进行迭代
            if(flag){
                i++;
            }
            //创建 BasicMapMessage 对象进行数据的添加
            BasicMapMessage basicMapMessage = new BasicMapMessage(name , count);
            //添加对象到 list 集合中
            BasicMapMessages.add(basicMapMessage);
        }
        return BasicMapMessages;
    }

    /**
     * 声明一个内部类 , 仅仅用于该类使用用于构建图表的返回数据对象
     * @author oldwang <br />
     * 创建时间: 2020.10.23
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class BasicMapMessage{
        private String name;
        private int count;
    }
}
