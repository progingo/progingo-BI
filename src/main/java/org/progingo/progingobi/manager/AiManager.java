package org.progingo.progingobi.manager;

import org.progingo.progingobi.exception.BusinessException;
import org.progingo.progingobi.exception.ErrorCode;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用于对接 AI 平台
 */
@Service
public class AiManager {

    @Autowired
    private ChatClient gameChatClient;

    public String analysisFormChat(Long chatId, String prompt) {
        String response = gameChatClient
                .prompt()
                .user(prompt)
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId);
                })
                .call()
                .content();
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response;
    }
}
