package org.progingo.progingobi.mq;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.progingo.progingobi.constant.BiMqConstant;
import org.progingo.progingobi.domain.entity.Chart;
import org.progingo.progingobi.domain.entity.UserChart;
import org.progingo.progingobi.exception.BusinessException;
import org.progingo.progingobi.exception.ErrorCode;
import org.progingo.progingobi.manager.AiManager;
import org.progingo.progingobi.mapper.ChartMapper;
import org.progingo.progingobi.mapper.UserChartMapper;
import org.progingo.progingobi.repository.ChartRepository;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class BiMessageConsumer {

    @Autowired
    private AiManager aiManager;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private UserChartMapper userChartMapper;
    @Autowired
    private ChartRepository chartRepository;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = BiMqConstant.BI_QUEUE_NAME,durable = "true"),
            exchange = @Exchange(name = BiMqConstant.BI_EXCHANGE_NAME, type = ExchangeTypes.DIRECT),
            key = {BiMqConstant.BI_ROUTING_KEY}
    )}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        CompletableFuture.runAsync(() -> {
            log.info("接受到消息: {}", message);
            if (StringUtils.isBlank(message)) {
                // 如果失败，消息拒绝
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息拒绝失败");
                }
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
            }
            //获取数据库信息
            UserChart userChart = userChartMapper.selectList(new LambdaQueryWrapper<UserChart>()
                            .eq(UserChart::getChartId, message)
                            .eq(UserChart::getIsDelete, false))
                    .stream().findFirst().orElse(null);
            if (userChart == null) {
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息拒绝失败");
                }
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
            }
            Chart chart = chartRepository.queryChart(message);
            // 先修改图表任务状态为 “执行中”。等执行成功后，修改为 “已完成”、保存执行结果；执行失败后，状态修改为 “失败”，记录任务失败信息。
            chartRepository.updateStatus(String.format("chart_%s",message), "running");

            // 调用 AI
            String result = aiManager.analysisFormChat(Long.valueOf(userChart.getId()), buildUserInput(chart));
            String[] splits = result.split("【【【【【");
            if (splits.length < 3) {
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息拒绝失败");
                }
                handleChartUpdateError(message, "AI 生成错误");
                return;
            }
            String genChart = splits[1].trim();
            String genResult = splits[2].trim();

            chartRepository.updateChartResult(message, genChart, genResult, "succeed",null);
            /*if (updateResult != 1) {
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息拒绝失败");
                }
                handleChartUpdateError(message, "更新图表成功状态失败");
            }*/
            // 消息确认
            try {
                channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息确认失败");
            }
        },threadPoolExecutor);


    }

    /**
     * 构建用户输入
     * @param chart
     * @return
     */
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");

        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(String chartId, String execMessage) {
        chartRepository.updateChartResult(chartId, null, null, "failed", execMessage);
    }

}
