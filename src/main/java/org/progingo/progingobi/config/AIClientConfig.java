package org.progingo.progingobi.config;

import org.progingo.progingobi.constant.PromptConstant;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIClientConfig {

    @Bean
    public ChatClient analysisFormChatClient(OpenAiChatModel openAiChatModel, ChatMemory chatMemory){
        return ChatClient
                .builder(openAiChatModel)
                .defaultSystem(PromptConstant.ANALYSIS_FORM_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

}
